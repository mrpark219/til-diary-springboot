package me.park.tildiaryspringboot.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//Token 정보를 Response할 때 사용
public class TokenDto {

    private String token;
}
