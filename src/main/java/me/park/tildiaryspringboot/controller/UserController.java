package me.park.tildiaryspringboot.controller;

import me.park.tildiaryspringboot.dto.UserDto;
import me.park.tildiaryspringboot.dto.UserInfoDto;
import me.park.tildiaryspringboot.entity.User;
import me.park.tildiaryspringboot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @GetMapping("/signup/{username}")
    public ResponseEntity<Boolean> usernameDuplicate(@PathVariable String username) {
        return ResponseEntity.ok(userService.isUsernameDuplicate(username));
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @PutMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> updateUserInfo(@Valid @RequestBody UserInfoDto userInfoDto) {
        return ResponseEntity.ok(userService.updateUserInfo(userInfoDto));
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
}
