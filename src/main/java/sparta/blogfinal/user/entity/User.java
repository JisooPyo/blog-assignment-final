package sparta.blogfinal.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String nickname;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private LocalDate birthday;
	private String introduction;
	private Long kakaoId;
	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;
	@CreatedDate
	@Column(updatable = false)
	@Temporal(TemporalType.DATE)
	private LocalDate createdAt;


}
