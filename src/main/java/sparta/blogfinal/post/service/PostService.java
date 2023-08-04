package sparta.blogfinal.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.blogfinal.post.dto.PostRequestDto;
import sparta.blogfinal.post.dto.PostResponseDto;
import sparta.blogfinal.post.entity.Post;
import sparta.blogfinal.post.repository.PostRepository;
import sparta.blogfinal.user.entity.User;
import sparta.blogfinal.user.entity.UserRoleEnum;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;

	public PostResponseDto createPost(PostRequestDto requestDto, User user) {
		Post post = new Post(requestDto);
		post.updateUser(user);

		postRepository.save(post);
		return new PostResponseDto(post);
	}

	public List<PostResponseDto> getPosts() {
		List<PostResponseDto> postList = postRepository.findAllByOrderByCreatedAtDesc()
				.stream()
				.map(PostResponseDto::new)
				.collect(Collectors.toList());
		return postList;
	}

	public PostResponseDto getPostById(Long id) {
		Post post = findPost(id);
		return new PostResponseDto(post);
	}

	@Transactional
	public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
		Post post = findPost(id);

		if (!(user.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().getId().equals(user.getId()))) {
			throw new RejectedExecutionException();
		}

		post.setTitle(requestDto.getTitle());
		post.setContents(requestDto.getContents());

		return new PostResponseDto(post);

	}

	/////////////////////////////////////////////////////////////

	private Post findPost(Long id) {
		return postRepository.findById(id).orElseThrow(() ->
				new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
		);
	}


}
