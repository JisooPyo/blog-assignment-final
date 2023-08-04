package sparta.blogfinal.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
public class ApiResponseDto {
	private String msg;
	private Integer statusCode;

	public ApiResponseDto(String msg, Integer statusCode) {
		this.msg = msg;
		this.statusCode = statusCode;
	}
}
