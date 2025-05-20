package rph.jwt;

import io.jsonwebtoken.*;
import rph.exception.TokenException;
import rph.exception.ErrorCode.TokenErrorCode;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");//.env

    private final long EXPIRATION = 1000L * 60 * 120; // 2시간
    private final long R_EXPRIRATION = 1000L *60 *60 *24 *7; //7일

    public String generateAccessToken(String username) {
    return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 30분
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    public String generateRefreshToken(String username) {
    return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + R_EXPRIRATION)) // 7일
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
