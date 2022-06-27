package ru.skillbox.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skillbox.socnetwork.model.entity.PostLike;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostLikeMapper implements RowMapper<PostLike> {
    @Override
    public PostLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostLike mapper = new PostLike();
        mapper.setId(rs.getInt("id"));
        mapper.setTime(rs.getLong("time"));
        mapper.setPostId(rs.getInt("post_id"));
        mapper.setPersonId(rs.getInt("person_id"));
        return mapper;
    }
}