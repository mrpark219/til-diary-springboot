package me.park.tildiaryspringboot.repository;

import me.park.tildiaryspringboot.dto.StatusDto;
import me.park.tildiaryspringboot.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //Lazy 조회가 아닌 Eager 조회를 하기 위해 사용
    @EntityGraph(attributePaths = "authorities")
    //username을 기준으로 User 정보를 가져올 때 권한 정보도 같이 가져옴
    Optional<User> findOneWithAuthoritiesByUsername(String username);

    User findByUsername(String userName);

    @Query(value = "WITH RECURSIVE DATES AS (SELECT DATE_FORMAT(NOW(), '%Y-01-01') DT " +
            "UNION ALL SELECT DATE_ADD(DT, INTERVAL 1 DAY) FROM DATES WHERE DT < CURDATE()) " +
            "SELECT COUNT(BOARD_ID) AS COUNT, DATE_FORMAT(DT, '%Y/%m/%d') AS DATE FROM DATES LEFT JOIN BOARD ON " +
            "DATE(BOARD.CREATED_AT) = DATE(DT) AND USER_ID = :userId " +
            "GROUP BY DT " +
            "HAVING COUNT > 0", nativeQuery = true)
    List<StatusDto> findAllStatusByUserId(Long userId);

    boolean existsByUsername(String username);
}
