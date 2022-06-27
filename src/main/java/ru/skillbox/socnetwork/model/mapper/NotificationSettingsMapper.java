package ru.skillbox.socnetwork.model.mapper;

import ru.skillbox.socnetwork.model.entity.NotificationSettings;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationSettingsMapper implements RowMapper<NotificationSettings> {
    @Override
    public NotificationSettings mapRow(ResultSet rs, int rowNun) throws SQLException {
        NotificationSettings mapper = new NotificationSettings();
        //mapper.setId(rs.getInt("id"));
        //mapper.setPersonId(rs.getInt("person_id"));
        mapper.setNotificationTypeCode(TypeNotificationCode.valueOf(rs.getString("type")));
        mapper.setEnable(rs.getBoolean("enable"));
        return mapper;
    }

}
