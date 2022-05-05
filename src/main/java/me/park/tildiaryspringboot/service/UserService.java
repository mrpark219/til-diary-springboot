package me.park.tildiaryspringboot.service;

import me.park.tildiaryspringboot.dto.UserDto;
import me.park.tildiaryspringboot.entity.Authority;
import me.park.tildiaryspringboot.entity.User;
import me.park.tildiaryspringboot.repository.UserRepository;
import me.park.tildiaryspringboot.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    //회원가입
    public UserDto signup(UserDto userDto) {
        if(userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .email_receives(userDto.isEmail_receives())
                .authorities(Collections.singleton(authority))
                .build();

        return UserDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    //username으로 유저, 권한 정보 조회
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    //SecurityContext에 저장된 username으로 유저, 권한 정보 조회
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername).orElse(null));
    }
}

