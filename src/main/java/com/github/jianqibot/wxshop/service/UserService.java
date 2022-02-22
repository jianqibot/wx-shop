package com.github.jianqibot.wxshop.service;

import com.github.jianqibot.wxshop.dao.UserDao;
import com.github.jianqibot.wxshop.generate.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Service
public class UserService {
    private final UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUserIfNotExist(String tel) {
        User user = new User();
        user.setTel(tel);
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        try {
            userDao.insertUser(user);
        } catch (DuplicateKeyException e) {
            return userDao.getUserByTel(tel);
        }

        return user;
    }

    public Optional<User> getUserByTel(String tel) {
        return Optional.ofNullable(userDao.getUserByTel(tel));
    }
}
