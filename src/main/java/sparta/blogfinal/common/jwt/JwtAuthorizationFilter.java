package sparta.blogfinal.common.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import sparta.blogfinal.common.security.UserDetailsServiceImpl;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest req,
									HttpServletResponse res,
									FilterChain filterChain) throws ServletException, IOException {
		String tokenValue = jwtUtil.getTokenFromRequest(req);

		if (StringUtils.hasText(tokenValue)) {
			tokenValue = jwtUtil.substringToken(tokenValue);
			// 토큰 제대로 찍어오는지 log에 찍어보기
			log.info(tokenValue);

			// JWT 토큰 검증
			if (!jwtUtil.validateToken(tokenValue)) {
				log.error("Token Error");
				return;
			}

			Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

			try {
				setAuthentication(info.getSubject());
			} catch (Exception e) {
				log.error(e.getMessage());
				return;
			}
		}

		filterChain.doFilter(req, res);
	}

	// 인증 처리
	public void setAuthentication(String email) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(email);
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}

	// 인증 객체 생성
	private Authentication createAuthentication(String email) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		// 인증 객체 만들 때 사용.
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
