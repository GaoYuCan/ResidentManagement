package top.sinkdev.rm.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import top.sinkdev.rm.common.entity.User;
import top.sinkdev.rm.dao.UserDao;

import javax.annotation.Nullable;
import java.sql.SQLType;
import java.util.Date;
import java.util.List;

@Repository
public class UserDaoMySql implements UserDao {

    private final JdbcTemplate template;

    @Autowired
    public UserDaoMySql(JdbcTemplate template) {
        this.template = template;
    }

    @Nullable
    @Override
    public List<User> queryUserByEmail(@NonNull String email) {
        return template.query("select * from user where email = ?", new BeanPropertyRowMapper<>(User.class), email);
    }

    @Override
    public int addUser(@NonNull  User user) {
        return template.update("insert into user (uid, username, email, password, gender, register_time) values (?,?,?,?,?,?)",
                user.getUid(), user.getUsername(), user.getEmail(), user.getPassword(), user.getGender(), user.getRegisterTime());
    }

}
