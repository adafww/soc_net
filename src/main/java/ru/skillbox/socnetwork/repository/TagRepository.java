package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.logging.DebugLogs;
import ru.skillbox.socnetwork.model.entity.Tag;
import ru.skillbox.socnetwork.model.mapper.TagMapper;

import java.util.List;

@RequiredArgsConstructor
@Repository

public class TagRepository {
    private final JdbcTemplate jdbc;

    public List<Tag> getAllTags() {
        String sql = "SELECT * FROM tag";
        return jdbc.query(sql, new TagMapper());
    }

    public void addTag(String tag) {
        String sql = "insert into tag (tag) values (?)";
        jdbc.update(sql, tag);
    }

    public List<Tag> getPostTags(int postId) {
        String sql = "select t.* from tag t join post2tag pt on t.id = pt.tag_id where pt.post_id = ?";
        return jdbc.query(sql, new TagMapper(), postId);
    }

}
