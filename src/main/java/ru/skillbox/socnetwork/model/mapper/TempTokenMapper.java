package ru.skillbox.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.skillbox.socnetwork.model.entity.TempToken;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TempTokenMapper implements RowMapper<TempToken> {

  @Override
  public TempToken mapRow(ResultSet rs, int rowNum) throws SQLException {
    TempToken token = new TempToken();
    token.setId(rs.getInt("id"));
    token.setToken(rs.getString("token"));
    token.setEmail(rs.getString("email"));
    return token;
  }
}
