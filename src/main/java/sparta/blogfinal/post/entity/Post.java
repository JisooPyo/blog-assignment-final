package sparta.blogfinal.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import sparta.blogfinal.common.entity.TimeStamped;
import sparta.blogfinal.user.entity.User;

@Entity
@Getter
public class Post extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String contents;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
}
