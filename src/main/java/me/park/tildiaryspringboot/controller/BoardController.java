package me.park.tildiaryspringboot.controller;

import me.park.tildiaryspringboot.dto.BoardDto;
import me.park.tildiaryspringboot.dto.BoardSaveDto;
import me.park.tildiaryspringboot.entity.Board;
import me.park.tildiaryspringboot.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("")
    public List<BoardDto> boards() {
        return boardService.getBoardList();
    }

    @GetMapping("/{boardId}")
    public BoardDto board(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
    }

    @PostMapping("")
    public Board save(@Valid @RequestBody BoardSaveDto boardSaveDto) {
        return boardService.writeBoard(boardSaveDto);
    }

    @PutMapping("/{boardId}")
    public Board modify(@PathVariable Long boardId, @Valid @RequestBody BoardSaveDto boardSaveDto) {
        boardSaveDto.setBoardId(boardId);
        return boardService.modifyBoard(boardSaveDto);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

}
