package me.park.tildiaryspringboot.redis;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RefreshRedisToken, Long> {
}
