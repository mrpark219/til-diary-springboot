package me.park.tildiaryspringboot.service;

import me.park.tildiaryspringboot.dto.StatusListDto;
import me.park.tildiaryspringboot.entity.User;
import me.park.tildiaryspringboot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusService {
    private final UserRepository userRepository;

    public StatusService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<StatusListDto> getStatusList() {
        List<StatusListDto> statusListDtoList = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        userList.forEach(user -> {
            StatusListDto statusListDto = new StatusListDto(user.getUserId(), user.getNickname(), userRepository.findAllStatusByUserId(user.getUserId()));
            statusListDtoList.add(statusListDto);
        });

        return statusListDtoList;
    }

    public StatusListDto getStatusByNickname(String nickname) {
        StatusListDto statusListDto = null;
        User user = userRepository.findByUsername(nickname);
        if(!ObjectUtils.isEmpty(user)) {
            statusListDto = new StatusListDto(user.getUserId(), user.getNickname(), userRepository.findAllStatusByUserId(user.getUserId()));
        }

        return statusListDto;
    }
}
