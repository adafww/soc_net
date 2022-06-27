package ru.skillbox.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skillbox.socnetwork.model.entity.Notification;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;
import ru.skillbox.socnetwork.model.entity.enums.TypeReadStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
        Notification mapper = new Notification();
        mapper.setId(rs.getInt("id"));
        mapper.setNotificationType(TypeNotificationCode.valueOf(rs.getString("notification_type")));
        mapper.setSentTime(rs.getLong("sent_time"));
        mapper.setPersonId(rs.getInt("person_id"));
        mapper.setEntityId(rs.getInt("entity_id"));
        mapper.setDistUserId(rs.getInt("dist_user_id"));
        mapper.setStatus(TypeReadStatus.valueOf(rs.getString("status")));
        mapper.setTitle(rs.getString("title"));
        return mapper;
    }
}