package sparta.blogfinal.post.dto;

import sparta.blogfinal.common.dto.ApiResponseDto;
import sparta.blogfinal.post.entity.Post;

import java.time.LocalDateTime;

public class PostResponseDto extends ApiResponseDto {
	private Long id;
	private String nickname;
	private String title;
	private String contents;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.nickname = post.getUser().getNickname();
		this.title = post.getTitle();
		this.contents = post.getContents();
		this.createdAt = post.getCreatedAt();
		this.modifiedAt = post.getModifiedAt();
	}

}
