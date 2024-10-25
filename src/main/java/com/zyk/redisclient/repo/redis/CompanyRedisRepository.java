package com.zyk.redisclient.repo.redis;

import com.zyk.redisclient.bean.Company;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface CompanyRedisRepository extends KeyValueRepository<Company,Long> {

}
