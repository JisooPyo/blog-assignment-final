package sparta.blogfinal.comment.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.blogfinal.comment.dto.CommentRequestDto;
import sparta.blogfinal.comment.dto.CommentResponseDto;
import sparta.blogfinal.comment.entity.Comment;
import sparta.blogfinal.comment.repository.CommentRepository;
import sparta.blogfinal.post.entity.Post;
import sparta.blogfinal.post.repository.PostRepository;
import sparta.blogfinal.user.entity.User;
import sparta.blogfinal.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;

	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
		User targetUser = findUser(user.getId());
		Post post = findPost(postId);

		Comment parent = null;

		// 대댓글인 경우
		if (requestDto.getParentId() != null) {
			parent = findComment(requestDto.getParentId());
		}

		Comment comment = new Comment(requestDto.getContents(), post, user);

		// 대댓글인 경우
		if (parent != null) {
			comment.updateParent(parent);
		}

		commentRepository.save(comment);
		CommentResponseDto responseDto = new CommentResponseDto(comment);
		return responseDto;
	}

	public List<CommentResponseDto> getCommentsByPostId(Long postId) {
		Post post = findPost(postId);
		List<Comment> commentList = commentRepository.findAllCommentsByPost(post);
		return convertNestedStructure(commentList);
	}

	/////////////////////////////////////////////////////////////////////////////////

	private User findUser(Long id) {
		return userRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("선택한 유저는 존재하지 않습니다.")
		);
	}

	private Post findPost(Long id) {
		return postRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("선택한 게시글은 존재하지 않습니다.")
		);
	}

	private Comment findComment(Long parentId) {
		return commentRepository.findById(parentId).orElseThrow(() ->
				new EntityNotFoundException("부모댓글이 존재하지 않습니다.")
		);
	}

	private List<CommentResponseDto> convertNestedStructure(List<Comment> comments) {
		List<CommentResponseDto> results = new ArrayList<>();
		Map<Long, CommentResponseDto> map = new HashMap<>();
		comments.stream().forEach(c -> {
			CommentResponseDto responseDto = new CommentResponseDto(c);
			map.put(responseDto.getId(), responseDto);
			if (c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(responseDto);
			else results.add(responseDto);
		});
		return results;
	}
}
