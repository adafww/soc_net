package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.model.entity.DeletedUser;
import ru.skillbox.socnetwork.model.entity.Person;
import ru.skillbox.socnetwork.model.mapper.DeletedUserMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeletedUsersRepository {

  private final JdbcTemplate jdbc;

  public void addDeletedUser(Person person){
    DeletedUser deletedUser = new DeletedUser(person);
    String sql = "INSERT INTO deleted_users (person_id, photo, first_name, last_name, expire) VALUES (?, ?, ?, ?, ?)";
    jdbc.update(sql,
            deletedUser.getPersonId(),
            deletedUser.getPhoto(),
            deletedUser.getFirstName(),
            deletedUser.getLastName(),
            deletedUser.getExpire());
  }

  public List<DeletedUser> getAll(){
    return jdbc.query("SELECT * FROM deleted_users", new DeletedUserMapper());
  }

  public List<DeletedUser> getAllExpired(){
    return jdbc.query("SELECT * FROM deleted_users WHERE expire <= ?", new DeletedUserMapper(), System.currentTimeMillis());
  }

  public void delete(Integer id){
    String sql = "DELETE FROM deleted_users WHERE id = ?";
    jdbc.update(sql, id);
  }

  public DeletedUser getDeletedUser(Integer personId){
    String sql = "SELECT * FROM deleted_users WHERE person_id = ?";
    return jdbc.queryForObject(sql, new DeletedUserMapper(), personId);
  }
}