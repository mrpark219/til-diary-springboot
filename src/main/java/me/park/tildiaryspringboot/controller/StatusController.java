package me.park.tildiaryspringboot.controller;

import me.park.tildiaryspringboot.dto.StatusListDto;
import me.park.tildiaryspringboot.service.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("")
    public List<StatusListDto> statuses() {
        return statusService.getStatusList();
    }

    @GetMapping("/{nickname}")
    public StatusListDto statusesByNickname(@PathVariable String nickname) {
        return statusService.getStatusByNickname(nickname);
    }
}
