1.使用spring data jpa+mysql操作源数据  
2.spring data redis使用场景  
1）Hash操作缓存数据，基于@RedisHash、KeyValueRepository 
2）String做幂等校验 ，基于redisTemplate
3）自增做固定限流  ，基于increment
4）Set做滑动限流 ， 基于ZSet
