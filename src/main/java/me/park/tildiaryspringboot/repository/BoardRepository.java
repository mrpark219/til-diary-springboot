package me.park.tildiaryspringboot.repository;

import me.park.tildiaryspringboot.dto.BoardDto;
import me.park.tildiaryspringboot.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "SELECT b.board_id AS boardId, b.title, b.content, b.emotion, b.created_at AS createdAt, b.modified_at AS modifiedAt, b.user_id AS userId, u.nickname " +
            "FROM board b LEFT JOIN user u ON u.user_id = b.user_id WHERE b.created_at BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<BoardDto> findJoinUserIdCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT b.board_id AS boardId, b.title, b.content, b.emotion, b.created_at AS createdAt, b.modified_at AS modifiedAt, b.user_id AS userId, u.nickname, u.username " +
            "FROM board b LEFT JOIN user u ON u.user_id = b.user_id WHERE b.board_id = :boardId", nativeQuery = true)
    BoardDto findByIdJoinUserId(Long boardId);
}
