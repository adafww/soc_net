package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.logging.DebugLogs;
import ru.skillbox.socnetwork.model.entity.CommentLike;
import ru.skillbox.socnetwork.model.mapper.CommentLikeMapper;

import java.util.List;

@RequiredArgsConstructor
@Repository

public class CommentLikeRepository {

    private final JdbcTemplate jdbc;

    public List<CommentLike> getLikes(int itemId) {
        String sql = "SELECT * FROM comment_like WHERE comment_id = ?";
        return jdbc.query(sql, new CommentLikeMapper(), itemId);
    }

    public CommentLike getPersonLike(int personId, int itemId) {
        String sql = "SELECT * FROM comment_like WHERE comment_id = ? AND person_id = ?";
        try {
            return jdbc.queryForObject(sql, new CommentLikeMapper(), itemId, personId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void addLike(int personId, int itemId) {
        String sql = "INSERT INTO comment_like (time, person_id, comment_id) values (?, ?, ?)";
        jdbc.update(sql, System.currentTimeMillis(), personId, itemId);
    }

    public boolean getIsLiked(int personId, int itemId) {
        return getPersonLike(personId, itemId) != null;
    }

    public void deleteLike(int id, int itemId) {
        String sql = "DELETE FROM comment_like WHERE person_id = ? AND comment_id = ?";
        jdbc.update(sql, id, itemId);
    }

    public void deleteAllPersonCommentLikes(int personId){
        String sql = "delete from comment_like where person_id = ?";
        jdbc.update(sql, personId);
    }

    public void deleteAllCommentLikesFromPersonComments(int personId){
        String sql = "DELETE FROM comment_like WHERE comment_id IN (SELECT id FROM post_comment WHERE author_id = ?)";
        jdbc.update(sql, personId);
    }

    public void deleteCommentAndSubCommentLikes(int commentId) {
        String sql = "DELETE FROM comment_like WHERE comment_id = ? OR " +
                "comment_id IN (SELECT id FROM post_comment WHERE parent_id = ?)";
        jdbc.update(sql, commentId, commentId);
    }

    public void deleteCommentLikes(int commentId) {
        String sql = "DELETE FROM comment_like WHERE comment_id = ?";
        jdbc.update(sql, commentId);
    }
}
