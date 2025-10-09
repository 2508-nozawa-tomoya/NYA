package com.example.NYA.security;

import com.example.NYA.repository.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class LoginUserDetails implements UserDetails {

    private final Integer id;
    private final String account;
    private final String password;
    private final String name;
    private final Integer departmentId;
    private final Integer authority;
    private final short isStopped;
    private final Collection<? extends GrantedAuthority> authorities;

    public LoginUserDetails(User user){
        this.id = user.getId();
        this.account = user.getAccount();
        this.password = user.getPassword();
        this.name = user.getName();
        this.departmentId = user.getDepartmentId();
        this.authority = user.getAuthority();
        this.isStopped = user.getIsStopped();

        if(user.getDepartmentId() == 1){
            if(user.getAuthority() == 0){
                //部署IDが1（人事部）かつ権限が0(承認者)ならシステム管理者権限+承認者権限を付与
                this.authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_APPROVER"));
            } else {
                //部署IDが1（人事部）かつ権限が0以外ならシステム管理者権限を付与
                this.authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
            }

        } else {
            if(user.getAuthority() == 0){
                //部署IDが1以外（人事部以外）かつ権限が0(承認者)なら承認者権限を付与
                this.authorities = List.of(new SimpleGrantedAuthority("ROLE_APPROVER"), new SimpleGrantedAuthority("ROLE_USER"));
            } else {
                //部署IDが1以外（人事部以外）かつ権限が0以外なら一般ユーザー
                this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    //ログイン名を返す（社員番号をString型へ変換）
    @Override
    public String getUsername(){
        return account;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isEnabled(){
        if(isStopped == 0){
            return true;
        } else {
            return false;
        }
    }
}
