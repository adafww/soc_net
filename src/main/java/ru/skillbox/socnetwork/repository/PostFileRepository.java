package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.model.entity.PostFile;
import ru.skillbox.socnetwork.model.mapper.PostFileMapper;

import java.util.List;
import ru.skillbox.socnetwork.logging.DebugLogs;

@RequiredArgsConstructor
@Repository

public class PostFileRepository {
    private final JdbcTemplate jdbc;

    public List<PostFile> getAllPersonFiles(Integer personId){
        String sql = "SELECT * FROM post_file WHERE post_id IN (SELECT id FROM post WHERE author = ?)";
        return jdbc.query(sql, new PostFileMapper(), personId);
    }
    public void deleteAllPersonFiles(Integer personId){
        String sql = "DELETE FROM post_file WHERE post_id IN (SELECT id FROM post WHERE author = ?)";
        jdbc.update(sql, personId);
    }
}
