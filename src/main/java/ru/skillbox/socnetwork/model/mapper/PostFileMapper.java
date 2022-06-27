package ru.skillbox.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skillbox.socnetwork.model.entity.PostFile;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostFileMapper implements RowMapper<PostFile> {
    @Override
    public PostFile mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostFile mapper = new PostFile();
        mapper.setId(rs.getInt("id"));
        mapper.setPostId(rs.getInt("post_id"));
        mapper.setName(rs.getString("name"));
        mapper.setPath(rs.getString("path"));
        return mapper;
    }
}