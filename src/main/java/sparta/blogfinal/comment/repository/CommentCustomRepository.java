package sparta.blogfinal.comment.repository;

import sparta.blogfinal.comment.entity.Comment;
import sparta.blogfinal.post.entity.Post;

import java.util.List;

public interface CommentCustomRepository {
	List<Comment> findAllCommentsByPost(Post post);
}
