package me.park.tildiaryspringboot.service;

import me.park.tildiaryspringboot.dto.UserDto;
import me.park.tildiaryspringboot.dto.UserInfoDto;
import me.park.tildiaryspringboot.entity.Authority;
import me.park.tildiaryspringboot.entity.User;
import me.park.tildiaryspringboot.repository.UserRepository;
import me.park.tildiaryspringboot.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    public User updateUserInfo(UserInfoDto userInfoDto) {

        String username = SecurityUtil.getCurrentUsername().orElseThrow(RuntimeException::new);

        User userToUpdate = userRepository.findByUsername(username);
        if(userToUpdate == null) {
            throw new RuntimeException("수정할 회원 정보를 가져오지 못했습니다.");
        }

        if(!ObjectUtils.isEmpty(userInfoDto.getPassword())) {
            userToUpdate.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        }
        if(!ObjectUtils.isEmpty(userInfoDto.getNickname())) {
            userToUpdate.setNickname(userInfoDto.getNickname());
        }
        if(!ObjectUtils.isEmpty(userInfoDto.getEmail())) {
            userToUpdate.setEmail(userInfoDto.getEmail());
        }
        if(!ObjectUtils.isEmpty(userInfoDto.isEmail_receives())) {
            userToUpdate.setEmail_receives(userInfoDto.isEmail_receives());
        }

        return userRepository.save(userToUpdate);
    }
}

