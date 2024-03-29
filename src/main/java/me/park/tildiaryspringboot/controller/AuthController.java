package me.park.tildiaryspringboot.controller;

import jakarta.validation.Valid;
import me.park.tildiaryspringboot.dto.LoginDto;
import me.park.tildiaryspringboot.dto.TokenDto;
import me.park.tildiaryspringboot.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

	private final TokenProvider tokenProvider;

	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	public final String AUTHORIZATION_HEADER;

	public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, @Value("${jwt.header}") String authorizationHeader) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		AUTHORIZATION_HEADER = authorizationHeader;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

		//authenticationToken으로 Authentication 객체가 생성될 때 CustomUserDetailsService의 loadUserByUsername 메서드 실행
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		//생성된 authentication 객체를 SecurityContext에 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);

		//jwt 토큰 생성
		String jwtToken = tokenProvider.createToken(authentication);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwtToken);

		return new ResponseEntity<>(new TokenDto(jwtToken), httpHeaders, HttpStatus.OK);
	}
}
