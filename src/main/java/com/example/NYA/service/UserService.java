package com.example.NYA.service;

import com.example.NYA.repository.UserRepository;
import com.example.NYA.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Integer getLoginUserId() {
        // 現在の認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginName = authentication.getName();

        // デフォルトユーザー("user")のときは仮値を返す
        if ("user".equals(loginName)) {
            return 1; // 仮のuserIdを返しておく
        }

        User user = userRepository.findByAccount(loginName);
        return user.getId();
    }

    public User getLoginUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
