package rph.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rph.entity.User;
import rph.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 생성자 주입
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //  이 메서드는 Spring Security가 자동으로 호출함
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 username으로 유저 조회
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("유저 없음");
        }
        return new CustomUserDetails(user);
    }
}
