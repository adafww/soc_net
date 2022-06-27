package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.logging.DebugLogs;
import ru.skillbox.socnetwork.model.entity.PostComment;
import ru.skillbox.socnetwork.model.mapper.PostCommentMapper;
import ru.skillbox.socnetwork.model.rsdto.postdto.CommentDto;

import java.util.List;

@RequiredArgsConstructor
@Repository

public class PostCommentRepository {
    private final JdbcTemplate jdbc;
    private static final String SELECT = "select pc.*, (cl.person_id = ?) as is_liked from post_comment pc " +
            "left join comment_like cl on cl.comment_id = pc.id and cl.person_id = ? ";

    public List<PostComment> getLikedParentCommentsByPostId(int currentPersonId, int postId) {
        String sql = SELECT + "where pc.post_id = ? and pc.parent_id is null order by id";
        return jdbc.query(sql, new PostCommentMapper(), currentPersonId, currentPersonId, postId);
    }

    public List<PostComment> getLikedSubCommentsByPostId(int currentPersonId, int postId) {
        String sql = SELECT + "where pc.post_id = ? and pc.parent_id > 0 order by id";
        return jdbc.query(sql, new PostCommentMapper(), currentPersonId, currentPersonId, postId);
    }

    public void add(CommentDto comment) {
            String sql = "INSERT INTO post_comment " +
                    "(time, post_id, author_id, comment_text, is_blocked, parent_id) values (?, ?, ?, ?, ?, ?)";
            jdbc.update(sql, System.currentTimeMillis(), comment.getPostId(), comment.getAuthor().getId(),
                    comment.getCommentText(), comment.getIsBlocked(), comment.getParentId());
    }

    public void edit(CommentDto comment) {
        String sql = "UPDATE post_comment set comment_text = ?, time = ? WHERE id = ?";
        jdbc.update(sql, comment.getCommentText(), System.currentTimeMillis(), comment.getId());
    }

    public void deleteCommentById(int commentId) {
        String sql = "DELETE FROM post_comment WHERE id = ?";
        jdbc.update(sql, commentId);
    }

    public PostComment getById(int id, int currentPersonId) throws EmptyResultDataAccessException {
        String sql = SELECT + "where pc.id = ?";
        return jdbc.queryForObject(sql, new PostCommentMapper(), currentPersonId, currentPersonId, id);
    }

    public void updateLikeCount(Integer likes, Integer postId) {
        String sql = "update post_comment set likes = ? where id = ?";
        jdbc.update(sql, likes, postId);
    }

    public void deleteAllPersonComments(Integer personId){
        String sql = "DELETE FROM post_comment WHERE author_id = ?";
        jdbc.update(sql, personId);
    }

    public void deleteAllPersonPostsComments(Integer personId){
        String sql = "DELETE FROM post_comment WHERE post_id IN (SELECT id FROM post WHERE author = ?)";
        jdbc.update(sql, personId);
    }

    public void deleteCommentsByPostId(int postId) {
        String sql = "DELETE FROM post_comment WHERE post_id = ?";
        jdbc.update(sql, postId);
    }

    public void deleteCommentAndSubCommentsByParentId(int commentId) {
        String sql = "DELETE FROM post_comment WHERE parent_id = ? OR id = ?";
        jdbc.update(sql, commentId, commentId);
    }
}