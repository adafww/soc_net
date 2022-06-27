package ru.skillbox.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skillbox.socnetwork.model.entity.Post;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post mapper = new Post();
        mapper.setId(rs.getInt("id"));
        mapper.setPostText(rs.getString("post_text"));
        mapper.setAuthor(rs.getInt("author"));
        mapper.setTitle(rs.getString("title"));
        mapper.setTime(rs.getLong("time"));
        mapper.setIsBlocked(rs.getBoolean("is_blocked"));
        mapper.setLikes(rs.getInt("likes"));
        mapper.setIsLiked(rs.getBoolean("is_liked"));
        return mapper;
    }
}