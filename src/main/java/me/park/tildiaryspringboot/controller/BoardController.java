package me.park.tildiaryspringboot.controller;

import me.park.tildiaryspringboot.dto.BoardDto;
import me.park.tildiaryspringboot.dto.BoardSaveDto;
import me.park.tildiaryspringboot.entity.Board;
import me.park.tildiaryspringboot.service.BoardService;
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

}
