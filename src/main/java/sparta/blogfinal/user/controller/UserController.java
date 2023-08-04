package sparta.blogfinal.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparta.blogfinal.common.dto.ApiResponseDto;
import sparta.blogfinal.user.dto.SignupRequestDto;
import sparta.blogfinal.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
	private final UserService userService;

	@PostMapping("/user/signup")
	public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if (fieldErrors.size() > 0) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				log.error(fieldError.getField() + "필드 : " + fieldError.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(new ApiResponseDto("잘못된 필드가 있습니다.", HttpStatus.BAD_REQUEST.value()));
		}
		userService.signup(requestDto);
		return ResponseEntity.ok().body(new ApiResponseDto("회원가입 성공", HttpStatus.OK.value()));
	}
}
