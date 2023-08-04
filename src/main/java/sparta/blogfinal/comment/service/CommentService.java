package sparta.blogfinal.comment.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.blogfinal.comment.dto.CommentRequestDto;
import sparta.blogfinal.comment.dto.CommentResponseDto;
import sparta.blogfinal.comment.entity.Comment;
import sparta.blogfinal.comment.repository.CommentRepository;
import sparta.blogfinal.post.entity.Post;
import sparta.blogfinal.post.repository.PostRepository;
import sparta.blogfinal.user.entity.User;
import sparta.blogfinal.user.entity.UserRoleEnum;
import sparta.blogfinal.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

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

	@Transactional
	public void updateComment(Long postId, Long id, CommentRequestDto requestDto, User user) {
		isAccessible(postId, id, user);
		Comment comment = findComment(id);
		comment.update(requestDto);
	}

	public void deleteComment(Long postId, Long id, User user) {
		isAccessible(postId, id, user);
		commentRepository.deleteById(id);
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

	private void isAccessible(Long postId, Long commentId, User user) {
		Comment comment = findComment(commentId);

		if (postId != findComment(commentId).getPost().getId()) {
			throw new EntityNotFoundException("해당 페이지를 찾을 수 없습니다.");
		}

		User targetUser = findUser(user.getId());

		if (!(targetUser.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().equals(targetUser))) {
			throw new RejectedExecutionException("작성자만 접근할 수 있습니다.");
		}
	}
}
