package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.model.mapper.*;
import ru.skillbox.socnetwork.model.rsdto.DialogsDto;
import ru.skillbox.socnetwork.model.rsdto.MessageDto;
import ru.skillbox.socnetwork.model.rsdto.PersonForDialogsDto;

import java.util.List;
import ru.skillbox.socnetwork.logging.DebugLogs;

@RequiredArgsConstructor
@Repository

public class MessageRepository {
    private final JdbcTemplate jdbc;

    public void updateReadStatus (Integer id) {
        String sql = "UPDATE message SET read_status = 'READ' WHERE dialog_id = ? AND read_status = 'SENT'";
        jdbc.update(sql, id);
    }
    public Integer sendMessage (Long time, Integer authorId,
                             Integer recipientId, String messageText, Integer dialogId) {
        String sql = "INSERT INTO message (time, author_id, recipient_id, message_text, read_status, dialog_id) " +
                "VALUES (?, ?, ?, ?, 'SENT', ?) RETURNING id ";
        return jdbc.queryForObject(sql, Integer.class, time, authorId, recipientId, messageText, dialogId);
    }

    public PersonForDialogsDto getPersonForDialog (Integer id) {
        String sql = "SELECT id, photo, first_name, last_name, e_mail, last_online_time FROM person WHERE id = ?";

        return jdbc.queryForObject(sql, new PersonForDialogsMapper(), id);
    }

    public List<MessageDto> getMessageList(Integer id) {
        String sql = "select id, time, author_id, recipient_id, message_text, read_status FROM message WHERE dialog_id = ?";
        return jdbc.query(sql, new MessageMapper(), id);
    }
    public DialogsDto getUnreadCount(Integer id) {
        String sql = "SELECT COUNT(*) AS unread_count FROM message WHERE read_status = 'SENT' AND recipient_id = ?";

        return jdbc.queryForObject(sql, new CountMapper(), id);
    }

    public void deleteAllPersonMessages(Integer personId){
        String sql = "delete from message where author_id = ? or recipient_id = ?";
        jdbc.update(sql, personId, personId);
    }
}
