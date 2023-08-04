package sparta.blogfinal.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {
	private String msg;
	private Integer statusCode;
}
