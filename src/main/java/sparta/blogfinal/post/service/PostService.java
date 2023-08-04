package sparta.blogfinal.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.blogfinal.post.dto.PostRequestDto;
import sparta.blogfinal.post.dto.PostResponseDto;
import sparta.blogfinal.post.entity.Post;
import sparta.blogfinal.post.repository.PostRepository;
import sparta.blogfinal.user.entity.User;

import java.util.List;
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
}
