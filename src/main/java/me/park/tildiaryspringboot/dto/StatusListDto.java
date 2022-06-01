package me.park.tildiaryspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusListDto {

    private Long user_id;

    private String nickname;

    private List<StatusDto> statusList;
}

