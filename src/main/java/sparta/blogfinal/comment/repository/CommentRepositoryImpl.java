package sparta.blogfinal.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sparta.blogfinal.comment.entity.Comment;
import sparta.blogfinal.post.entity.Post;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentCustomRepository {
	private final JPAQueryFactory queryFactory;


	@Override
	public List<Comment> findAllCommentsByPost(Post post) {
		return null;
	}
}
