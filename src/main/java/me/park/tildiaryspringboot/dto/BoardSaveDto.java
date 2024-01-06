package me.park.tildiaryspringboot.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveDto {

	private Long boardId;

	@NotNull
	@Size(min = 3)
	private String title;

	@NotNull
	@Size(min = 3)
	private String content;

	@NotNull
	@Min(0)
	@Max(6)
	private Integer emotion;

}
