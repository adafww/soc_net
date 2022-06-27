package ru.skillbox.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skillbox.socnetwork.model.entity.BlockHistory;
import ru.skillbox.socnetwork.model.entity.enums.TypeAction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlockHistoryMapper implements RowMapper<BlockHistory> {
    @Override
    public BlockHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
        BlockHistory mapper = new BlockHistory();
        mapper.setId(rs.getInt("id"));
        mapper.setTime(rs.getLong("time"));
        mapper.setPersonId(rs.getInt("person_id"));
        mapper.setPostId(rs.getInt("post_id"));
        mapper.setCommentId(rs.getInt("comment_id"));
        mapper.setAction(TypeAction.valueOf(rs.getString("action")));
        return mapper;
    }
}
