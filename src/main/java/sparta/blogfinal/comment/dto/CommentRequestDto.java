package sparta.blogfinal.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {
	private Long parentId;

	@NotNull(message = "내용을 입력하세요.")
	private String contents;
}
