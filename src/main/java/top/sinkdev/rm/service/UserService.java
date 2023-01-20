package top.sinkdev.rm.service;

import org.springframework.lang.NonNull;
import top.sinkdev.rm.common.SimpleResponse;
import top.sinkdev.rm.common.entity.User;

public interface UserService {
    SimpleResponse<User> login(@NonNull String email, @NonNull String password);

    SimpleResponse<User> register(@NonNull User user);
}
