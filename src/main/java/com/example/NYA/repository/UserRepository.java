package com.example.NYA.repository;

import com.example.NYA.repository.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByOrderByIdAsc();

    //アカウント名で検索
    Optional<User> findByAccount(String account);

    // 部署IDで検索
    List<User> findByDepartmentId(Integer departmentId);

    //ユーザー停止有効切り替え
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isStopped = :isStopped, u.updatedDate = :updatedDate WHERE u.id = :id")
    public void changeIsStopped(@Param("id")Integer id, @Param("isStopped")short isStopped, @Param("updatedDate")Timestamp ts);
}
