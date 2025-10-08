package com.example.NYA.service;

import com.example.NYA.repository.UserRepository;
import com.example.NYA.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Integer getLoginUserId() {
        // 現在の認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginName = authentication.getName();

        Optional<User> optionalUser = userRepository.findByAccount(loginName);
        return optionalUser
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("ログインユーザーが存在しません: " + loginName));
    }
}
