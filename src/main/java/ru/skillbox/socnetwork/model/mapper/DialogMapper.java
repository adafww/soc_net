package ru.skillbox.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skillbox.socnetwork.model.rsdto.DialogsDto;
import ru.skillbox.socnetwork.model.rsdto.DialogsResponse;
import ru.skillbox.socnetwork.model.rsdto.MessageDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DialogMapper implements RowMapper<DialogsDto> {
    @Override
    public DialogsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        DialogsDto dialogsDto = new DialogsDto();

        dialogsDto.setDialogId(rs.getInt("dialog_id"));
        dialogsDto.setTime(rs.getLong("time"));
        dialogsDto.setUnreadCount(rs.getInt("unread_count"));
        dialogsDto.setMessageText(rs.getString("message_text"));
        dialogsDto.setReadStatus(rs.getString("read_status"));
        dialogsDto.setAuthorId(rs.getInt("author_id"));
        dialogsDto.setRecipientId(rs.getInt("recipient_id"));
        dialogsDto.setMessageId(rs.getInt("message_id"));
        dialogsDto.setFirstName(rs.getString("first_name"));
        dialogsDto.setLastName(rs.getString("last_name"));
        dialogsDto.setEMail(rs.getString("e_mail"));
        dialogsDto.setPhoto(rs.getString("photo"));
        dialogsDto.setLastOnlineTime(rs.getLong("last_online_time"));

        return dialogsDto;
    }
}
