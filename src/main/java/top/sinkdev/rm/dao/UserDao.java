package top.sinkdev.rm.dao;

import org.springframework.lang.NonNull;
import top.sinkdev.rm.common.entity.User;

import javax.annotation.Nullable;
import java.util.List;

public interface UserDao {
    @Nullable
    List<User> queryUserByEmail(@NonNull String email);

    int addUser(@NonNull User user);

}
