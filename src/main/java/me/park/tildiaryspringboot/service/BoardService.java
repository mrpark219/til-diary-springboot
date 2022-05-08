package me.park.tildiaryspringboot.service;

import me.park.tildiaryspringboot.dto.BoardDto;
import me.park.tildiaryspringboot.dto.BoardSaveDto;
import me.park.tildiaryspringboot.entity.Board;
import me.park.tildiaryspringboot.repository.BoardRepository;
import me.park.tildiaryspringboot.repository.UserRepository;
import me.park.tildiaryspringboot.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public List<BoardDto> getBoardList() {
        return boardRepository.findJoinUserId();
    }

    public BoardDto getBoard(Long boardId) {
        return boardRepository.findByIdJoinUserId(boardId);
    }

    public Board writeBoard(BoardSaveDto boardSaveDto) {
        Optional<String> username = SecurityUtil.getCurrentUsername();
        if(username.isEmpty()) {
            throw new RuntimeException("로그인한 사용자만 글을 등록할 수 있습니다.");
        }
        Board board = Board.builder()
                .title(boardSaveDto.getTitle())
                .content(boardSaveDto.getContent())
                .emotion(boardSaveDto.getEmotion())
                .user(userRepository.findByUsername(username.get()))
                .build();
        return boardRepository.save(board);
    }
}
