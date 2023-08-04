package sparta.blogfinal.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.blogfinal.post.dto.PostRequestDto;
import sparta.blogfinal.post.dto.PostResponseDto;
import sparta.blogfinal.post.entity.Post;
import sparta.blogfinal.post.repository.PostRepository;
import sparta.blogfinal.user.entity.User;

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
}
