package veniamin.backend.spring_telegram.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserRespDTO {
    @NotBlank
    private Long id;

    private String firstName;

    private String username;

    private String photoUrl;
}