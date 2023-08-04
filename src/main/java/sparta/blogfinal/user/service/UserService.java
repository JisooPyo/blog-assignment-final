package sparta.blogfinal.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.blogfinal.common.jwt.JwtUtil;
import sparta.blogfinal.user.dto.SignupRequestDto;
import sparta.blogfinal.user.entity.User;
import sparta.blogfinal.user.entity.UserRoleEnum;
import sparta.blogfinal.user.repository.UserRepository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${admin.token}")
	private String ADMIN_TOKEN;

	@Transactional
	public void signup(SignupRequestDto requestDto) {
		//email, nickname, password, birthday, introduction, role
		String email = requestDto.getEmail();
		String nickname = requestDto.getNickname();
		String password = passwordEncoder.encode(requestDto.getPassword());
		LocalDate birthday = requestDto.getBirthday();
		String introduction = null;
		if (requestDto.getIntroduction() != null) {
			introduction = requestDto.getIntroduction();
		}

		// 이메일 중복 확인
		Optional<User> checkEmail = userRepository.findByEmail(email);
		if(checkEmail.isPresent()){
			throw new DuplicateKeyException("이미 존재하는 이메일입니다.");
		}

		// 사용자 ROLE 확인
		UserRoleEnum role = UserRoleEnum.USER;
		if(requestDto.isAdmin()){
			if(!ADMIN_TOKEN.equals(requestDto.getAdminToken())){
				throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
			}
			role = UserRoleEnum.ADMIN;
		}

		// 사용자
		User user = new User(email, nickname, password, birthday, role);

		// 소개글이 있을 경우
		if(introduction != null){
			user.updateIntroduction(introduction);
		}

		userRepository.save(user);
	}

}
