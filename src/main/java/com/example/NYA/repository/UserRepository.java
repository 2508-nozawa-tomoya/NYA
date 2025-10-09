package com.example.NYA.repository;

import com.example.NYA.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByOrderByIdAsc();

    //アカウント名で検索
    Optional<User> findByAccount(String account);

    // 部署IDで検索
    List<User> findByDepartmentId(Integer departmentId);
}
