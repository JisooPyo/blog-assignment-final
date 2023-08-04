package sparta.blogfinal.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sparta.blogfinal.comment.entity.Comment;
import sparta.blogfinal.common.entity.TimeStamped;
import sparta.blogfinal.post.dto.PostRequestDto;
import sparta.blogfinal.user.entity.User;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String contents;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	private List<Comment> comments;

	public Post(PostRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.contents = requestDto.getContents();
	}

}
