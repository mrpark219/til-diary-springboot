package me.park.tildiaryspringboot.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
//Jwt 커스텀 필터
public class JwtFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;

	public final String AUTHORIZATION_HEADER;

	public JwtFilter(TokenProvider tokenProvider, String authorizationHeader) {
		this.AUTHORIZATION_HEADER = authorizationHeader;
		this.tokenProvider = tokenProvider;
	}

	//토큰의 인증정보를 Security Context에 저장하는 역할 수행
	@Override
	public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {

		String jwt = resolveToken(request);
		String requestURI = request.getRequestURI();

		if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			//SecurityContext에 Authentication 객체 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
		}
		else {
			log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
		}

		filterChain.doFilter(request, response);
	}

	//Request Header에서 토큰 정보를 꺼내옴
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}