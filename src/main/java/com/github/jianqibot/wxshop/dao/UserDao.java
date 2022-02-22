package com.github.jianqibot.wxshop.dao;

import com.github.jianqibot.wxshop.generate.User;
import com.github.jianqibot.wxshop.generate.UserExample;
import com.github.jianqibot.wxshop.generate.UserMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
@Service
public class UserDao {
    private final UserMapper userMapper;

    @Inject
    public UserDao(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void insertUser(User user) {
        userMapper.insert(user);
    }

    public User getUserByTel(String tel) {
        UserExample example = new UserExample();
        example.createCriteria().andTelEqualTo(tel);
        return userMapper.selectByExample(example).get(0);
    }
}
