package sparta.blogfinal.comment.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.blogfinal.comment.dto.CommentRequestDto;
import sparta.blogfinal.comment.dto.CommentResponseDto;
import sparta.blogfinal.comment.service.CommentService;
import sparta.blogfinal.common.dto.ApiResponseDto;
import sparta.blogfinal.common.security.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
	private final CommentService commentService;

	// 선택한 게시글에 대한 모든 댓글 조회
	@GetMapping("/posts/{postId}/comments")
	public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
		List<CommentResponseDto> results = commentService.getCommentsByPostId(postId);
		return ResponseEntity.ok().body(results);
	}

	// 댓글 작성
	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<ApiResponseDto> createComment(@PathVariable Long postId,
														@RequestBody CommentRequestDto requestDto,
														@AuthenticationPrincipal UserDetailsImpl userDetails) {
		try {
			CommentResponseDto responseDto = commentService.createComment(postId, requestDto, userDetails.getUser());
			return ResponseEntity.ok().body(responseDto);
		} catch (EntityNotFoundException enf) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponseDto(enf.getMessage(), HttpStatus.NOT_FOUND.value()));
		}
	}

	// 선택한 댓글 수정
	@PutMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<ApiResponseDto> updateComment(@PathVariable Long postId,
														@PathVariable Long id,
														@RequestBody CommentRequestDto requestDto,
														@AuthenticationPrincipal UserDetailsImpl userDetails) {
		commentService.updateComment(postId, id, requestDto, userDetails.getUser());
		return ResponseEntity.ok().body(
				new ApiResponseDto("댓글 수정 성공", HttpStatus.OK.value())
		);
	}

	// 선택한 댓글 삭제
	@DeleteMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long postId,
														@PathVariable Long id,
														@AuthenticationPrincipal UserDetailsImpl userDetails) {
		commentService.deleteComment(postId, id, userDetails.getUser());
		return ResponseEntity.ok().body(new ApiResponseDto("댓글 삭제 완료", HttpStatus.OK.value()));
	}

	//////////////////////////////////////////////////////////////////////////////////////////
}
