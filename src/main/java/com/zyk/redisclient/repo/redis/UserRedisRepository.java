package com.zyk.redisclient.repo.redis;


import com.zyk.redisclient.bean.User;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface UserRedisRepository extends KeyValueRepository<User,Long> {

}
