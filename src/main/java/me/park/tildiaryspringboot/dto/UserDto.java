package me.park.tildiaryspringboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import me.park.tildiaryspringboot.entity.User;


import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//회원가입할 때 사용
public class UserDto {

	@NotNull
	@Size(min = 3, max = 50)
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
	@Size(min = 3, max = 100)
	private String password;

	@NotNull
	@Size(min = 3, max = 50)
	private String nickname;

	@Email
	private String email;

	@Getter
	private boolean email_receives;

	private Set<AuthorityDto> authorityDtoSet;

	public static UserDto from(User user) {
		if(user == null) {
			return null;
		}

		return UserDto.builder()
				.username(user.getUsername())
				.nickname(user.getNickname())
				.email(user.getEmail())
				.email_receives(user.isEmail_receives())
				.authorityDtoSet(user.getAuthorities().stream()
						.map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
						.collect(Collectors.toSet()))
				.build();
	}

}
