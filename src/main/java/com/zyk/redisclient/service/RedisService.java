package com.zyk.redisclient.service;

import com.zyk.redisclient.bean.Company;
import com.zyk.redisclient.bean.User;
import com.zyk.redisclient.repo.jpa.CompanyJpaRepository;
import com.zyk.redisclient.repo.jpa.UserJpaRepository;
import com.zyk.redisclient.repo.redis.CompanyRedisRepository;
import com.zyk.redisclient.repo.redis.UserRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private UserRedisRepository userRedisRepository;
    @Autowired
    private CompanyRedisRepository companyRedisRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private CompanyJpaRepository companyJpaRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 首次查询进行初始化redis
     * 后续查询使用redis
     * 变更时重新初始化
     * @param id
     * @return
     */
    public User queryUserById(Long id) {
        Optional<User> optionalUser = userRedisRepository.findById(id);
        if(optionalUser.isPresent()){
            log.info("redis数据查询");
            return optionalUser.get();
        }else{
            Optional<User> userOptional = userJpaRepository.findById(id);
            if(userOptional.isEmpty()){
                throw new RuntimeException("id："+id+"不存在");
            }
            log.info("mysql数据查询");
            User user = userOptional.get();
            userRedisRepository.save(user);//数据结构是hash
            return user;
        }
    }

    /**
     * 同上
     * @param id
     * @return
     */
    public Company queryCompanyById(Long id) {
        Optional<Company> optionalCompany = companyRedisRepository.findById(id);
        if(optionalCompany.isPresent()){
            log.info("redis数据查询");
            return optionalCompany.get();
        }else{
            Optional<Company> companyOptional = companyJpaRepository.findById(id);
            if(companyOptional.isEmpty()){
                throw new RuntimeException("id："+id+"不存在");
            }
            log.info("mysql数据查询");
            Company company = companyOptional.get();
            companyRedisRepository.save(company);
            return company;
        }
    }

    /**
     * 幂等校验
     * 1.超时重试
     * 2.网络抖动
     * @param businessNo
     */
    public void doBusiness(String businessNo) {
        //假设业务数据处理需要两秒
        if(redisTemplate.hasKey(businessNo)){
            throw new RuntimeException("业务号："+businessNo+"正在处理中，请稍候");
        }else {
            redisTemplate.opsForValue().set(businessNo,1,2,TimeUnit.SECONDS);
        }
        //这里添加db校验是否处理完成
        log.info("业务号：{},处理完成",businessNo);
    }

    /**
     * 5秒内限制2次请求
     * @param clientNo
     */
    public void doLimit_fixed(String clientNo) {
        String key = "limit:"+clientNo+"-"+System.currentTimeMillis() / 1000 / 5;
        long times = redisTemplate.opsForValue().increment(key);
        if(times==1){
            redisTemplate.expire(key,5,TimeUnit.SECONDS);
        }
        log.info("{}第{}次请求",key,times);
        if(times<=2){
            log.info("{}请求成功",key);
        }else{
            log.info("{}请求失败",key);
            throw new RuntimeException("请求频率超限制");
        }
    }

    /**
     * 5秒内限制2次请求
     * @param clientNo
     */
    public void doLimit_sliding(String clientNo) {
        String key = "limit:"+clientNo;
        long seconds = System.currentTimeMillis()/1000;
        redisTemplate.opsForZSet().add(key,seconds,seconds);
        redisTemplate.opsForZSet().removeRangeByScore(key,0,seconds-5);
        long times = redisTemplate.opsForZSet().zCard(key);
        if(times>2){
            throw new RuntimeException("请求频率超限制");
        }
        redisTemplate.expire(key,5,TimeUnit.SECONDS);
    }

}
