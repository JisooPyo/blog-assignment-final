package sparta.blogfinal.common.jwt;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sparta.blogfinal.common.dto.ApiResponseDto;
import sparta.blogfinal.common.security.UserDetailsImpl;
import sparta.blogfinal.user.dto.LoginRequestDto;
import sparta.blogfinal.user.entity.UserRoleEnum;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/api/user/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		log.info("로그인 시도");
		try {
			LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							requestDto.getEmail(),
							requestDto.getPassword(),
							null
					)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response,
											FilterChain chain,
											Authentication authResult) throws IOException, ServletException {
		log.info("로그인 성공 및 JWT 생성");
		String email = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
		UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

		String token = jwtUtil.createToken(email, role);
		jwtUtil.addJwtToCookie(token, response);

		response.setStatus(200);

		ApiResponseDto apiResponseDto = new ApiResponseDto("로그인 성공", response.getStatus());
		String jsonResponseBody = new ObjectMapper().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true).writeValueAsString(apiResponseDto);
		response.setContentType("application/json");
		response.getWriter().write(jsonResponseBody);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
											  HttpServletResponse response,
											  AuthenticationException failed) throws IOException, ServletException {
		log.info("로그인 실패");
		response.setStatus(401);
		ApiResponseDto apiResponseDto = new ApiResponseDto("로그인 실패", response.getStatus());
		String jsonResponseBody = new ObjectMapper().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true).writeValueAsString(apiResponseDto);
		response.setContentType("application/json");
		response.getWriter().write(jsonResponseBody);
		response.getWriter().flush();
		response.getWriter().close();
	}
}
