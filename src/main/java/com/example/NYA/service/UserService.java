package com.example.NYA.service;

import com.example.NYA.controller.form.UserForm;
import com.example.NYA.repository.UserRepository;
import com.example.NYA.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    // レコード取得(ID)
    public UserForm selectUserById(Integer id) {

        User result = userRepository.findById(id).orElse(null);
        UserForm userForm = new UserForm();

        if (result == null) {
            return null;
        } else {
            userForm.setId(result.getId());
            userForm.setAccount(result.getAccount());
            userForm.setPassword(result.getPassword());
            userForm.setName(result.getName());
            userForm.setDepartmentId(result.getDepartmentId());
            userForm.setAuthority(result.getAuthority());
            userForm.setWorkStart(result.getWorkStart());
            userForm.setWorkEnd(result.getWorkEnd());
            userForm.setRestStart(result.getRestStart());
            userForm.setRestEnd(result.getRestEnd());
            userForm.setIsStopped(result.getIsStopped());
            userForm.setCreatedDate(result.getCreatedDate());
            userForm.setUpdatedDate(result.getUpdatedDate());
        }
        return userForm;
    }

    // レコード追加・更新
    public void save(UserForm userForm) {

        boolean isNewUser = userForm.getId() == null;
        boolean changeIsStopped = false;
        User dbUser = null;

        if (!isNewUser) {
            dbUser = userRepository.findById(userForm.getId()).orElse(null);
        }
        if (dbUser != null && dbUser.getIsStopped() != userForm.getIsStopped()) {
            changeIsStopped = true;
        }
        if (isNewUser || !userForm.getPassword().isBlank() && !changeIsStopped) {
            String rawPassword = userForm.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            userForm.setPassword(encodedPassword);
        } else {
            User user = userRepository.findById(userForm.getId()).orElse(null);
            userForm.setPassword(user.getPassword());
        }

        User user = setUserEntity(userForm);
        userRepository.save(user);
    }

    // DBから取得したデータをFormに設定
    private List<UserForm> setUserForm(List<User> results) {

        List<UserForm> users = new ArrayList<>();

        for (User result : results) {
            UserForm user = new UserForm();
            user.setId(result.getId());
            user.setAccount(result.getAccount());
            user.setPassword(result.getPassword());
            user.setName(result.getName());
            user.setDepartmentId(result.getDepartmentId());
            user.setAuthority(result.getAuthority());
            user.setWorkStart(result.getWorkStart());
            user.setWorkEnd(result.getWorkEnd());
            user.setRestStart(result.getRestStart());
            user.setRestEnd(result.getRestEnd());
            user.setIsStopped(result.getIsStopped());
            user.setCreatedDate(result.getCreatedDate());
            user.setUpdatedDate(result.getCreatedDate());
            users.add(user);
        }
        return users;
    }

    // リクエストから取得した情報をentityに設定
    private User setUserEntity(UserForm reqUser) {

        User user = new User();

        if (reqUser.getId() != null) {
            user.setId(reqUser.getId());
            user.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        user.setAccount(reqUser.getAccount());
        user.setPassword(reqUser.getPassword());
        user.setName(reqUser.getName());
        user.setDepartmentId(reqUser.getDepartmentId());
        user.setAuthority(reqUser.getAuthority());
        user.setWorkStart(reqUser.getWorkStart());
        user.setWorkEnd(reqUser.getWorkEnd());
        user.setRestStart(reqUser.getRestStart());
        user.setRestEnd(reqUser.getRestEnd());
        user.setIsStopped(reqUser.getIsStopped());
        return user;
    }

}
