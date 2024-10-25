package com.zyk.redisclient.api;


import com.zyk.redisclient.bean.Company;
import com.zyk.redisclient.bean.User;
import com.zyk.redisclient.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/redis")
public class RedisApi {

    @Autowired
    private RedisService redisService;

    /**
     * 缓存
     * @param id
     * @return
     */
    @RequestMapping("/users/{id}")
    public User queryUserById(@PathVariable Long id) {
        return redisService.queryUserById(id);
    }

    /**
     * 缓存
     * @param id
     * @return
     */
    @RequestMapping("/companies/{id}")
    public Company queryCompanyById(@PathVariable Long id) {
        return redisService.queryCompanyById(id);
    }

    /**
     * 幂等校验
     * @param businessNo
     */
    @RequestMapping("/doBusiness/{businessNo}")
    public void doBusiness(@PathVariable String businessNo){
        redisService.doBusiness(businessNo);
    }

    /**
     * 固定限流
     * @param clientNo
     */
    @RequestMapping("/doLimit-fixed/{clientNo}")
    public void doLimit_fixed(@PathVariable String clientNo){
        redisService.doLimit_fixed(clientNo);
    }

    /**
     * 滑动限流
     * @param clientNo
     */
    @RequestMapping("/doLimit-sliding/{clientNo}")
    public void doLimit_sliding(@PathVariable String clientNo){
        redisService.doLimit_sliding(clientNo);
    }

}
