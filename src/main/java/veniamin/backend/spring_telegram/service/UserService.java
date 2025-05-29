package veniamin.backend.spring_telegram.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import veniamin.backend.spring_telegram.dto.response.UserRespDTO;
import veniamin.backend.spring_telegram.entity.User;

public interface UserService extends UserDetailsService {

    UserRespDTO getCurrentUserInfo();

    UserRespDTO getUserById(Long id);

    UserRespDTO getUserByUsername(String username);

    UserRespDTO getResponseDTO(User user);
}
