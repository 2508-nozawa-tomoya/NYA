package com.example.NYA.service;

import com.example.NYA.controller.form.UserForm;
import com.example.NYA.repository.UserRepository;
import com.example.NYA.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
            userForm.setWork_start(result.getWorkStart());
            userForm.setWork_end(result.getWorkEnd());
            userForm.setRest(result.getRest());
            userForm.setIsStopped(result.getIsStopped());
            userForm.setCreateDate(result.getCreateDate());
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
        user.setWorkStart(reqUser.getWork_start());
        user.setWorkEnd(reqUser.getWork_end());
        user.setRest(reqUser.getRest());
        user.setIsStopped(reqUser.getIsStopped());
        return user;
    }

}
