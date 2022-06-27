package ru.skillbox.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skillbox.socnetwork.model.entity.CommentLike;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentLikeMapper implements RowMapper<CommentLike> {
    @Override
    public CommentLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        CommentLike mapper = new CommentLike();
        mapper.setId(rs.getInt("id"));
        mapper.setTime(rs.getLong("time"));
        mapper.setCommentId(rs.getInt("comment_id"));
        mapper.setPersonId(rs.getInt("person_id"));
        return mapper;
    }
}
