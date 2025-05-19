package rph.jwt;

import io.jsonwebtoken.*;
import rph.exception.TokenException;
import rph.exception.ErrorCode.TokenErrorCode;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "secret1234"; // 👉 꼭 환경변수나 외부로 뺄 것!
    private final long EXPIRATION = 1000L * 60 * 60; // 1시간

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

        public String getUsernameFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new TokenException(TokenErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new TokenException(TokenErrorCode.TOKEN_INVALID);
        }
    }

        public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

        public boolean validateTokenOrThrow(String token) { // 나중에 확실한 expection 메세지를 주는 api가 만들어지면 쓰자.
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true; 
        } catch (ExpiredJwtException e) {
            throw new TokenException(TokenErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new TokenException(TokenErrorCode.TOKEN_INVALID);
        }
    }
}
