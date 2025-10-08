package com.example.NYA.repository;

import com.example.NYA.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //アカウント名で検索
    Optional<User> findByAccount(String  account);
}
