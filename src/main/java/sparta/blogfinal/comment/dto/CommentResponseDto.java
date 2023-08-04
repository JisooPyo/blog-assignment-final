package sparta.blogfinal.comment.dto;

import lombok.Getter;
import sparta.blogfinal.comment.entity.Comment;
import sparta.blogfinal.common.dto.ApiResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto extends ApiResponseDto {
	private Long id;
	private String nickname;
	private String contents;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	private List<CommentResponseDto> children = new ArrayList<>();

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.nickname = comment.getUser().getNickname();
		this.contents = comment.getContents();
		this.createdAt = comment.getCreatedAt();
		this.modifiedAt = comment.getModifiedAt();
	}

}
