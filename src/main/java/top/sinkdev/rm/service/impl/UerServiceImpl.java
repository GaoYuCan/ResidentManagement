package top.sinkdev.rm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import top.sinkdev.rm.common.SimpleResponse;
import top.sinkdev.rm.common.entity.User;
import top.sinkdev.rm.dao.UserDao;
import top.sinkdev.rm.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UerServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UerServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public SimpleResponse<User> login(@NonNull String email, @NonNull String password) {
        List<User> userList = userDao.queryUserByEmail(email);
        if (userList == null || userList.isEmpty()) {
            return SimpleResponse.createFailureResponse("该邮箱未绑定任何账号！");
        }
        User user = userList.get(0);
        if (password.equals(user.getPassword())) {
            return SimpleResponse.createSuccessResponse(user,
                    "欢迎您，%s".formatted(user.getUsername()));
        }
        return SimpleResponse.createFailureResponse("登录失败，密码错误！");
    }

    @Override
    public SimpleResponse<User> register(@NonNull User user) {
        List<User> userList = userDao.queryUserByEmail(user.getEmail());
        if (userList != null && !userList.isEmpty()) {
            return SimpleResponse.createFailureResponse("当前邮箱已被使用。");
        }
        // add extra message
        String uid = UUID.randomUUID().toString().replace("-", "");
        user.setUid(uid);
        user.setRegisterTime(new Date());
        // insert user into database
        int i = userDao.addUser(user);
        if (1 != i) {
            return SimpleResponse.createFailureResponse("注册失败，未知原因。");
        }
        return SimpleResponse.createSuccessResponse(user, "注册成功, %s".formatted(user.getUsername()));
    }
}
