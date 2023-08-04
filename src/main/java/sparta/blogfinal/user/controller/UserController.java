package sparta.blogfinal.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sparta.blogfinal.common.dto.ApiResponseDto;
import sparta.blogfinal.user.dto.SignupRequestDto;
import sparta.blogfinal.user.service.UserService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
	private final UserService userService;

	@PostMapping("/user/signup")
	public ResponseEntity<ApiResponseDto> signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if (fieldErrors.size() > 0) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				log.error(fieldError.getField() + "필드 : " + fieldError.getDefaultMessage());
			}
			throw new IllegalArgumentException("잘못된 항목이 있습니다.");
		}

		userService.signup(requestDto);
		return ResponseEntity.ok().body(new ApiResponseDto("회원가입 성공", HttpStatus.OK.value()));
	}
}
