package sparta.blogfinal.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.blogfinal.comment.dto.CommentRequestDto;
import sparta.blogfinal.common.entity.TimeStamped;
import sparta.blogfinal.post.entity.Post;
import sparta.blogfinal.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends TimeStamped {
	//parent_id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postId")
	private Post post;

	@Column(nullable = false)
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentId")
	private Comment parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
	private List<Comment> children = new ArrayList<>();

	public Comment(String contents, Post post, User user) {
		this.user = user;
		this.post = post;
		this.contents = contents;
	}

	public void updateParent(Comment parent) {
		this.parent = parent;
	}


	public void update(CommentRequestDto requestDto) {
		this.contents = requestDto.getContents();
	}
}
