package com.zyk.redisclient.repo.jpa;


import com.zyk.redisclient.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User,Long> {

}
