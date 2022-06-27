package ru.skillbox.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skillbox.socnetwork.model.entity.Post2Tag;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Post2TagMapper implements RowMapper<Post2Tag> {
    @Override
    public Post2Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post2Tag mapper = new Post2Tag();
        mapper.setId(rs.getInt("id"));
        mapper.setPostId(rs.getInt("post_id"));
        mapper.setTagId(rs.getInt("tag_id"));
        return mapper;
    }
}
