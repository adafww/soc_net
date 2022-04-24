package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.model.entity.enums.TypeReadStatus;
import ru.skillbox.socnetwork.model.mapper.CountMapper;
import ru.skillbox.socnetwork.model.mapper.DialogMapper;
import ru.skillbox.socnetwork.model.mapper.MessageMapper;
import ru.skillbox.socnetwork.model.mapper.PersonForDialogsMapper;
import ru.skillbox.socnetwork.model.rsdto.DialogsDto;
import ru.skillbox.socnetwork.model.rsdto.DialogsResponse;
import ru.skillbox.socnetwork.model.rsdto.MessageDto;
import ru.skillbox.socnetwork.model.rsdto.PersonForDialogsDto;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MessageRepository {
    private final JdbcTemplate jdbc;

    public PersonForDialogsDto getPersonForDialog(Integer id) {
        String sql = "SELECT id, photo, first_name, last_name, e_mail, last_online_time FROM person WHERE id = ?";

        return jdbc.queryForObject(sql, new PersonForDialogsMapper(), id);
    }
    public List<DialogsDto> getDialogList(Integer id) {
        String sql = "SELECT dialog_id, MAX(time) AS time, COUNT(*) AS unread_count, MAX(message_text) AS message_text, " +
            "MAX(read_status) AS read_status, MAX(author_id) AS author_id, MAX(recipient_id) AS recipient_id, MAX(message.id) AS message_id, " +
            "MAX(first_name) AS first_name, MAX(last_name) AS last_name, MAX(e_mail) AS e_mail, MAX(photo) AS photo, MAX(last_online_time) AS last_online_time " +
            "FROM message " +
            "INNER JOIN person ON recipient_id = person.id " +
            "WHERE read_status = 'SENT' AND recipient_id = ? OR author_id = ? GROUP BY dialog_id " +
            "UNION " +
            "SELECT dialog_id, MAX(time) AS time, 0 AS unread_count, MAX(message_text) AS message_text, " +
            "MAX(read_status) AS read_status, MAX(author_id) AS author_id, MAX(recipient_id) AS recipient_id, MAX(message.id) AS message_id, " +
            "MAX(first_name) AS first_name, MAX(last_name) AS last_name, MAX(e_mail) AS e_mail, MAX(photo) AS photo, MAX(last_online_time) AS last_online_time " +
            "FROM message " +
            "INNER JOIN person ON recipient_id = person.id " +
            "WHERE read_status = 'READ' AND recipient_id = ? OR author_id = ? GROUP BY dialog_id ";
        return jdbc.query(sql, new DialogMapper(), id, id, id, id);
    }
    public List<MessageDto> getMessageList(Integer id) {
        String sql = "select id, time, author_id, recipient_id, message_text, read_status FROM message WHERE dialog_id = ?";
        return jdbc.query(sql, new MessageMapper(), id);
    }
    public DialogsResponse getUnreadCount(Integer id) {
        String sql = "SELECT COUNT(*) AS unread_count FROM message WHERE read_status = 'SENT' AND recipient_id = ?";

        return jdbc.queryForObject(sql, new CountMapper(), id);
    }
}
