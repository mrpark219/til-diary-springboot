package me.park.tildiaryspringboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//내정보관리할 때 사용
public class UserInfoDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)


    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

    @Email
    private String email;

    @Getter
    private boolean email_receives;

}
