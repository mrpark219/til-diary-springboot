package me.park.tildiaryspringboot.service;

import me.park.tildiaryspringboot.dto.BoardDto;
import me.park.tildiaryspringboot.dto.BoardListDto;
import me.park.tildiaryspringboot.dto.BoardSaveDto;
import me.park.tildiaryspringboot.entity.Board;
import me.park.tildiaryspringboot.repository.BoardRepository;
import me.park.tildiaryspringboot.repository.UserRepository;
import me.park.tildiaryspringboot.util.SecurityUtil;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
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

    public List<BoardDto> getBoardList(BoardListDto boardListDto) {
        //시작 날짜 종료 날짜 없으면 현재 달로 조회
        if(ObjectUtils.isEmpty(boardListDto.getStartDate()) || ObjectUtils.isEmpty(boardListDto.getEndDate())) {
            boardListDto.setStartDate(LocalDateTime.now().withDayOfMonth(1));
            boardListDto.setEndDate(LocalDateTime.now().plusMonths(1).minusDays(1));
        }
        return boardRepository.findJoinUserIdCreatedAtBetween(boardListDto.getStartDate(), boardListDto.getEndDate());
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
                .contentHtml(makeHTML(boardSaveDto.getContent()))
                .emotion(boardSaveDto.getEmotion())
                .user(userRepository.findByUsername(username.get()))
                .build();
        return boardRepository.save(board);
    }

    public Board modifyBoard(BoardSaveDto boardSaveDto) {
        if(ObjectUtils.isEmpty(boardSaveDto.getBoardId())) {
            throw new RuntimeException("수정할 boardId가 없습니다.");
        }
        Board boardToUpdate = boardRepository.getReferenceById(boardSaveDto.getBoardId());

        if(!ObjectUtils.isEmpty(boardSaveDto.getTitle())) {
            boardToUpdate.setTitle(boardSaveDto.getTitle());
        }
        if(!ObjectUtils.isEmpty(boardSaveDto.getContent())) {
            boardToUpdate.setContent(boardSaveDto.getContent());
        }
        if(!ObjectUtils.isEmpty(boardSaveDto.getEmotion())) {
            boardToUpdate.setEmotion(boardSaveDto.getEmotion());
        }
        return boardRepository.save(boardToUpdate);
    }

    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    public String makeHTML(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
