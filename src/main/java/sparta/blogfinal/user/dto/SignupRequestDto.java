package sparta.blogfinal.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class SignupRequestDto {
	@Email @NotBlank
	private String email;

	@NotBlank
	private String nickname;

	@NotBlank
	@Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 15자 이하여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]*$", message = "비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다.")
	private String password;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday; // 2023-08-04

	private String introduction;

	private boolean admin = false;

	private String adminToken = "";
}
