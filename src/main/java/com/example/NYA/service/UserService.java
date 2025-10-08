package com.example.NYA.service;

import com.example.NYA.controller.form.UserForm;
import com.example.NYA.repository.UserRepository;
import com.example.NYA.repository.entity.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //ユーザー全件取得
    public List<UserForm> findAll(){
         List<User> results = userRepository.findAllByOrderByIdAsc();

         return setUserForm(results);
    }

    //アカウント（社員番号）でユーザー情報を取得
    public User findByAccount(String account){
        return userRepository.findByAccount(account).orElse(null);
    }

    public Integer getLoginUserId() {
        // 現在の認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginName = authentication.getName();

        Optional<User> optionalUser = userRepository.findByAccount(loginName);
        return optionalUser
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("ログインユーザーが存在しません: " + loginName));
    }

    //IDでユーザー情報を取得
    public UserForm findById(Integer id){
        User result =  userRepository.findById(id).orElse(null);

        List<User> users = new ArrayList<>();
        users.add(result);
        List<UserForm> user = setUserForm(users);
        return user.get(0);
    }

    //ユーザー情報登録
    public void saveUser(UserForm userForm){
        if(!StringUtils.isBlank(userForm.getPassword())){
            //Formの中のパスワードが空でなければハッシュ化
            String encodePassword = passwordEncoder.encode(userForm.getPassword());
            userForm.setPassword(encodePassword);
        } else{
            //Formにパスワードが入力されていなければ編集元のパスワードをフォームに設定
            User user = userRepository.findById(userForm.getId()).orElse(null);
            userForm.setPassword(user.getPassword());
        }
        User user = setUserEntity(userForm);
        userRepository.save(user);
    }

    /*
     * DBから取得した情報をFormに移し替え
     */
    private List<UserForm> setUserForm(List<User> results){
        List<UserForm> users = new ArrayList<>();
        for(int i = 0; i < results.size(); i++){
            UserForm user = new UserForm();
            User result = results.get(i);

            user.setId(result.getId());
            user.setAccount(result.getAccount());
            user.setName(result.getName());
            user.setPassword(result.getPassword());
            user.setDepartmentId(result.getDepartmentId());
            user.setAuthority(result.getAuthority());
            user.setWorkStart(result.getWorkStart());
            user.setWorkEnd(result.getWorkEnd());
            user.setRestStart(result.getRestStart());
            user.setRestEnd(result.getRestEnd());
            user.setIsStopped(result.getIsStopped());
            user.setCreatedDate(result.getCreatedDate());
            user.setUpdatedDate(result.getUpdatedDate());

            users.add(user);
        }
        return users;
    }

    /*
     * フォームに入力された情報をEntityに詰め替え
     */
    private User setUserEntity(UserForm userForm){
        User user = new User();
        user.setAccount(userForm.getAccount());
        user.setPassword(userForm.getPassword());
        user.setName(userForm.getName());
        user.setDepartmentId(userForm.getDepartmentId());
        user.setAuthority(userForm.getAuthority());
        user.setWorkStart(userForm.getWorkStart());
        user.setWorkEnd(userForm.getWorkEnd());
        user.setRestStart(userForm.getRestStart());
        user.setRestEnd(userForm.getRestEnd());

        if(userForm.getId() != null){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            user.setId(userForm.getId());
            user.setUpdatedDate(timestamp);
        }

        return user;
    }

}
