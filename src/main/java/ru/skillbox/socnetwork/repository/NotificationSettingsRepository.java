package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.logging.DebugLogs;
import ru.skillbox.socnetwork.model.entity.NotificationSettings;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;
import ru.skillbox.socnetwork.model.mapper.NotificationSettingsMapper;

import java.util.List;


@RequiredArgsConstructor
@Repository
@DebugLogs
public class NotificationSettingsRepository {
    private final JdbcTemplate jdbc;

    public void changeNotificationSettings(TypeNotificationCode notificationCode, Boolean enable, Integer personId) {
        String sql = "UPDATE notification_settings SET enable = CAST(? AS boolean) WHERE person_id = ?" +
                " and type = CAST(? AS notification_code_type)";
        jdbc.update(sql, enable, personId, notificationCode.toString());
    }

    public boolean checkSettingsForNotification(TypeNotificationCode notificationCode, Integer personId) {
        String sql = "select enable from notification_settings where person_id = ? " +
                "and type = CAST(? AS notification_code_type)";
        return Boolean.TRUE.equals(jdbc.queryForObject(sql, Boolean.class, personId, notificationCode.toString()));

    }

    public List<NotificationSettings> getAllNotificationsSettingsForUser(Integer personId) {

        String sql = "select * from notification_settings where person_id = ?";
        return jdbc.query(sql, new NotificationSettingsMapper(), personId);

    }

    public void addNotificationSettingsForNewUser(Integer personId){
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("person_id", personId);
        parameters.addValue("enable", true);

        StringBuilder sql = new StringBuilder();
        sql.append("insert into notification_settings (person_id, type, enable) values")
                .append("(:person_id, 'POST', :enable),")
                .append("(:person_id, 'POST_COMMENT', :enable),")
                .append("(:person_id, 'FRIEND_REQUEST', :enable),")
                .append("(:person_id, 'COMMENT_COMMENT', :enable),")
                .append("(:person_id, 'MESSAGE', :enable),")
                .append("(:person_id, 'FRIEND_BIRTHDAY', :enable)");

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbc);
        template.update(sql.toString(), parameters);

    }
}
