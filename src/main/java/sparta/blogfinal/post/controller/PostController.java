package sparta.blogfinal.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.blogfinal.common.dto.ApiResponseDto;
import sparta.blogfinal.common.security.UserDetailsImpl;
import sparta.blogfinal.post.dto.PostRequestDto;
import sparta.blogfinal.post.dto.PostResponseDto;
import sparta.blogfinal.post.entity.Post;
import sparta.blogfinal.post.service.PostService;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

	private final PostService postService;

	// 포스트 작성
	@PostMapping("/posts")
	public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
													  @RequestBody PostRequestDto requestDto) {
		PostResponseDto result = postService.createPost(requestDto, userDetails.getUser());
		return ResponseEntity.status(201).body(result);
	}

	// 전체 포스트 조회
	@GetMapping("/posts")
	public ResponseEntity<List<PostResponseDto>> getPosts() {
		List<PostResponseDto> results = postService.getPosts();

		return ResponseEntity.ok().body(results);
	}

	// 선택 포스트 조회
	@GetMapping("/posts/{id}")
	public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
		PostResponseDto result = postService.getPostById(id);

		return ResponseEntity.ok().body(result);
	}

	// 포스트 수정
	@PutMapping("/posts/{id}")
	public ResponseEntity<ApiResponseDto> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
													 @PathVariable Long id,
													 @RequestBody PostRequestDto requestDto) {
		try {
			PostResponseDto result = postService.updatePost(id, requestDto, userDetails.getUser());
			return ResponseEntity.ok().body(new ApiResponseDto("수정 완료.", HttpStatus.OK.value()));
		} catch (RejectedExecutionException e) {
			return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
		}
	}

	// 포스트 삭제
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<ApiResponseDto> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
													 @PathVariable Long id) {
		return null;
	}


}
