package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.logging.DebugLogs;
import ru.skillbox.socnetwork.model.entity.Friendship;
import ru.skillbox.socnetwork.model.mapper.FriendshipMapper;
import ru.skillbox.socnetwork.model.mapper.FriendshipPersonMapper;
import ru.skillbox.socnetwork.model.rsdto.FriendshipPersonDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository

public class FriendshipRepository {

    private final JdbcTemplate jdbc;

    public int removeFriendlyStatusByPersonIdsAndCode(Integer srcPersonId, Integer dstPersonId, String typeCode) {
        return jdbc.update("DELETE FROM friendship f WHERE f.src_person_id = ? and f. dst_person_id = ? " +
                "AND f.code = CAST(? AS code_type)", srcPersonId, dstPersonId, typeCode);
    }

    public Friendship createFriendlyStatusByPersonIdsAndCode(Integer srcPersonId, Integer dstPersonId, String typeCode) {
       return jdbc.queryForObject("INSERT INTO friendship (src_person_id, dst_person_id, time, code) " +
                "VALUES (?, ?, ?, CAST(? AS code_type)) returning *",
               new FriendshipMapper(),srcPersonId, dstPersonId, System.currentTimeMillis(), typeCode);
    }

    public Optional<Friendship> getFriendlyStatusByPersonIdsAndCode(Integer srcPersonId, Integer dstPersonId,
                                                                    String typeCode) {
        return jdbc.query("SELECT * FROM friendship f " +
                "WHERE f.code = CAST(? AS code_type) AND f.src_person_id = ? AND f.dst_person_id = ?",
                new FriendshipMapper(), typeCode, srcPersonId, dstPersonId).stream().findAny();
    }

    public Optional<Friendship> getFriendlyStatusByPersonIds(Integer srcPersonId, Integer dstPersonId) {
        return jdbc.query("SELECT * FROM friendship f WHERE f.src_person_id = ? AND f.dst_person_id = ?",
                new FriendshipMapper(), srcPersonId, dstPersonId).stream().findAny();
    }

    public void updateFriendlyStatusByPersonIdsAndCode(Integer srcPersonId, Integer dstPersonId, String typeCode) {
        jdbc.update("UPDATE friendship SET time = ?, code = CAST(? AS code_type) " +
                        "WHERE src_person_id = ? and dst_person_id = ?",
                System.currentTimeMillis(), typeCode, srcPersonId, dstPersonId);
    }

    public void fullUpdateFriendlyStatusByPersonIdsAndCode(Integer srcPersonId, Integer dstPersonId, String typeCode) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("srcPersonId", srcPersonId);
        parameters.addValue("dstPersonId", dstPersonId);
        parameters.addValue("typeCode", typeCode);
        parameters.addValue("currentTime", System.currentTimeMillis());
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbc);

        template.update("UPDATE friendship SET time = :currentTime, code = CAST(:typeCode AS code_type), " +
                "src_person_id = :dstPersonId,  dst_person_id = :srcPersonId WHERE src_person_id = :srcPersonId " +
                "AND dst_person_id = :dstPersonId", parameters);
    }

    public List<FriendshipPersonDto> getInformationAboutFriendships(String email, List<Integer> userIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("email", email);
        parameters.addValue("userIds", userIds);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbc);

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("WITH authorized_person_id AS (SELECT p.id FROM person p WHERE p.e_mail = :email) ")
                .append("SELECT f.dst_person_id AS user_id, f.code AS status FROM friendship f ")
                .append("WHERE f.src_person_id = (SELECT * FROM authorized_person_id) AND f.dst_person_id IN (:userIds) ")
                .append("UNION SELECT f.src_person_id AS user_id, f.code AS status FROM friendship f ")
                .append("WHERE f.dst_person_id = (SELECT * FROM authorized_person_id) AND f.src_person_id IN (:userIds)");

        return template.query(sqlQuery.toString(), parameters, new FriendshipPersonMapper());
    }

    public void deleteAllPersonFriendships(Integer personId){
        String sql = "delete from friendship where src_person_id = ? or dst_person_id = ?";
        jdbc.update(sql, personId, personId);
    }
}
