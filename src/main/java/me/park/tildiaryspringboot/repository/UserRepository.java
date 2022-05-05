package me.park.tildiaryspringboot.repository;

import me.park.tildiaryspringboot.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //Lazy 조회가 아닌 Eager 조회를 하기 위해 사용
    @EntityGraph(attributePaths = "authorities")
    //username을 기준으로 User 정보를 가져올 때 권한 정보도 같이 가져옴
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
