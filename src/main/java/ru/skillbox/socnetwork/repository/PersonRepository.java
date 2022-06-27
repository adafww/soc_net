package ru.skillbox.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillbox.socnetwork.exception.InvalidRequestException;
import ru.skillbox.socnetwork.logging.DebugLogs;
import ru.skillbox.socnetwork.model.entity.Person;
import ru.skillbox.socnetwork.model.mapper.PersonMapper;
import ru.skillbox.socnetwork.model.rqdto.LoginDto;
import ru.skillbox.socnetwork.service.Constants;

import java.util.List;

@Repository
@RequiredArgsConstructor
@DebugLogs
public class PersonRepository {

    private final JdbcTemplate jdbc;

    public void updateLastOnlineTimeByEmail(String email, Long time) {
        String sql = "UPDATE person SET last_online_time = ? WHERE email = ?";

        jdbc.update(sql, time, email);
    }

    public Person getById(int id)  {
        String sql = "select * from person where id = ?";
        return jdbc.queryForObject(sql, new PersonMapper(), id);
    }

    public Person getByEmail(String email) {
        String sql = "select * from person where e_mail = ?";
        return jdbc.queryForObject(sql, new PersonMapper(), email);
    }

    public boolean isEmptyEmail(String email) {
        try {
            getByEmail(email);
        } catch (DataAccessException e) {
            return true;
        }
        return false;
    }

    public Person getPersonAfterLogin(LoginDto loginDto) throws InvalidRequestException {
        Person person;
        try {
            person = getByEmail(loginDto.getEmail());
        } catch (DataAccessException e) {
            throw new InvalidRequestException("invalid login");
        }
        if (!loginDto.checkPassword(person.getPassword())) {
            throw new InvalidRequestException("invalid password");
        }
        return person;
    }

    public List<Person> getAll() {
        return jdbc.query("select * from person", new PersonMapper());
    }

    public Person saveFromRegistration(Person person) {
        person.setRegDate(System.currentTimeMillis());
        String sql = "insert into person (first_name, last_name, reg_date, e_mail, password, photo, is_deleted) " +
                "values (?, ?, ?, ?, ?, ?, ?) RETURNING *";

        return jdbc.queryForObject(sql, new PersonMapper(),
                person.getFirstName(),
                person.getLastName(),
                System.currentTimeMillis(),
                person.getEmail(),
                person.getPassword(),
                person.getPhoto(),
                false);
    }

    public List<Person> getListRecommendedFriends(String email) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("WITH authorized_person_id as (SELECT p.id FROM person p WHERE p.e_mail = ?), ")
                .append("friends_ids AS (SELECT f.dst_person_id AS id FROM friendship f WHERE f.src_person_id = ")
                .append("(SELECT * FROM authorized_person_id) AND f.code IN ('FRIEND', 'REQUEST', 'BLOCKED') ")
                .append("UNION SELECT f.src_person_id AS id FROM friendship f WHERE f.dst_person_id = ")
                .append("(SELECT * FROM authorized_person_id) AND f.code IN ('FRIEND', 'BLOCKED')) ")
                .append("SELECT * FROM person WHERE id <> ")
                .append("(SELECT * FROM authorized_person_id) and id NOT IN (SELECT * FROM friends_ids) ")
                .append("ORDER BY RANDOM() LIMIT 20");

        return jdbc.query(sqlQuery.toString(), new PersonMapper(), email);
    }

    public List<Person> getUserFriends(String email) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("WITH authorized_person_id as (SELECT p.id FROM person p WHERE p.e_mail = ?), ")
                .append("friends_ids AS (SELECT f.dst_person_id AS id FROM friendship f WHERE f.src_person_id = ")
                .append("(SELECT * FROM authorized_person_id) AND f.code = 'FRIEND' ")
                .append("UNION SELECT f.src_person_id AS id ")
                .append("FROM friendship f WHERE f.dst_person_id = (SELECT * FROM authorized_person_id) ")
                .append("AND f.code = 'FRIEND') SELECT * FROM person p WHERE p.id IN (SELECT * FROM friends_ids) ")
                .append("ORDER BY p.last_name, p.first_name");

        return jdbc.query(sqlQuery.toString(), new PersonMapper(), email);
    }

    public List<Person> getListIncomingFriendRequests(String email) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("WITH authorized_person_id as (SELECT p.id FROM person p WHERE p.e_mail = ?), ")
                .append("persons_request_ids AS (SELECT f.src_person_id AS id FROM friendship f ")
                .append("WHERE f.dst_person_id = (SELECT * FROM authorized_person_id) AND f.code = 'REQUEST') ")
                .append("SELECT * FROM person p WHERE p.id IN (SELECT * FROM persons_request_ids) ")
                .append("ORDER BY p.last_name, p.first_name");

        return jdbc.query(sqlQuery.toString(), new PersonMapper(), email);
    }

    public Person updatePersonByEmail(Person person) {
        String sql = "UPDATE person SET (first_name, last_name, birth_date, phone, about, city, country) = " +
                "(?, ?, ?, ?, ?, ?, ?) WHERE person.e_mail = ? RETURNING person.*";
        return jdbc.queryForObject(sql, new PersonMapper(),
                person.getFirstName(),
                person.getLastName(),
                person.getBirthDate(),
                person.getPhone(),
                person.getAbout(),
                person.getCity(),
                person.getCountry(),
                person.getEmail());
    }

    public void updatePhotoByEmail(Person person) {
        String sql = "update person set photo = ? where person.e_mail = ?";
        jdbc.update(sql,
                person.getPhoto(),
                person.getEmail());
    }

    public void updatePassword(Person person) {
        String sql = "update person set password = ? where person.e_mail = ?";
        jdbc.update(sql,
                person.getPassword(),
                person.getEmail());
    }

    public void updateEmail(Person person, String email) {
        String sql = "update person set e_mail = ? where person.e_mail = ?";
        jdbc.update(sql,
                email,
                person.getEmail());
    }

    public List<Person> getPersonsFromSearch(String firstName, String lastName,
                                             long ageFrom, long ageTo,
                                             int countryId, int cityId,
                                             int perPage) {

        long milliSecInYear = Constants.MILLISECONDS_IN_YEAR;
        long currentTime = System.currentTimeMillis();

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("firstName", "%" + firstName + "%");
        parameters.addValue("lastName", "%" + lastName + "%");
        parameters.addValue("dateTo", currentTime - ageFrom * milliSecInYear);
        parameters.addValue("dateFrom", currentTime - ageTo * milliSecInYear);

        String sql = "select * from person" +
                " where first_name like :firstName and last_name like :lastName " +
                "and birth_date >= :dateFrom and birth_date <= :dateTo ";

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbc);
        return template.query(sql, parameters, new PersonMapper());
    }

    public void delete(Integer id) {
        String sql = "delete from person where id = ?";
        jdbc.update(sql, id);
    }

    public void setDeleted(Integer id, Boolean b) {
        String sql = "update person set is_deleted = ? where person.id = ?";
        jdbc.update(sql, b, id);
    }

    public void updateLastOnlineTimeFromMap(List<Integer> offlineMap) {
        StringBuilder sql = new StringBuilder("UPDATE person SET last_online_time = ? WHERE");
        for (int i = 0; i < offlineMap.size(); i++) {
            int id = offlineMap.get(i);
            sql.append(" id = ").append(id);
            if (i < offlineMap.size() - 1) {
                sql.append(" or");
            }
        }
        jdbc.update(sql.toString(), System.currentTimeMillis() - Constants.FIFTY_SECONDS_IN_MILLIS);
    }

    public Person getPersonBirthDay(Integer personId) {

        String sql = "Select * from person where id = ?";
        return jdbc.queryForObject(sql, new PersonMapper(), personId);
    }
}