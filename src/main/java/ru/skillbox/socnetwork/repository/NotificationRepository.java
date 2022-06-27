package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.logging.DebugLogs;

import ru.skillbox.socnetwork.model.entity.Notification;
import ru.skillbox.socnetwork.model.entity.enums.TypeReadStatus;
import ru.skillbox.socnetwork.model.mapper.NotificationMapper;
import ru.skillbox.socnetwork.model.rsdto.NotificationDto;

import java.util.List;

@RequiredArgsConstructor
@Repository

public class NotificationRepository {
    private final JdbcTemplate jdbc;

    public void deleteAllPersonNotifications(Integer personId) {

        String sql = "delete from notification where person_id = ?";
        jdbc.update(sql, personId);
    }

    public void addNotification(NotificationDto notificationDto) {

        String sql = "insert into notification (notification_type, sent_time, person_id, entity_id, dist_user_id, status, title)" +
                " values ((CAST(? AS notification_code_type)), ?, ?, ?, ?, (CAST(? AS read_status_type)), ?)";

        jdbc.update(sql, notificationDto.getNotificationType().toString(), notificationDto.getSentTime(),
                notificationDto.getPersonId(), notificationDto.getEntityId(), notificationDto.getDistUserId(),
                notificationDto.getStatus().toString(), notificationDto.getTitle());
    }

    public List<Notification> getAllNotificationsForPerson(int personId) {

        String sql = "select * from notification where dist_user_id = ? and status = (CAST(? AS read_status_type))";
        return jdbc.query(sql, new NotificationMapper(), personId, TypeReadStatus.SENT.toString());
    }

    public void readAllNotificationByUser(int personId) {

        String sql = "UPDATE notification SET status = CAST(? AS read_status_type) WHERE dist_user_id = ?";
        jdbc.update(sql, TypeReadStatus.READ.toString(), personId);
    }

    public void readUsersNotificationById(int notificationId) {

        String sql = "UPDATE notification SET status = CAST(? AS read_status_type) WHERE id = ?";
        jdbc.update(sql, TypeReadStatus.READ.toString(), notificationId);
    }

}
