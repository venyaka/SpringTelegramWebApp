package veniamin.backend.spring_telegram.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import veniamin.backend.spring_telegram.dto.response.UserRespDTO;
import veniamin.backend.spring_telegram.entity.User;
import veniamin.backend.spring_telegram.exception.NotFoundException;
import veniamin.backend.spring_telegram.exception.error.NotFoundError;
import veniamin.backend.spring_telegram.repository.UserRepository;
import veniamin.backend.spring_telegram.service.UserService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
    }


    @Override
    @Transactional
    public UserRespDTO getCurrentUserInfo() {
        User user = this.getCurrentUser();
        return getResponseDTO(user);
    }

    @Override
    @Transactional
    public UserRespDTO getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(NotFoundError.USER_NOT_FOUND);
        }

        return getResponseDTO(optionalUser.get());
    }

    @Override
    @Transactional
    public UserRespDTO getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(NotFoundError.USER_NOT_FOUND);
        }

        return getResponseDTO(optionalUser.get());
    }

    @Override
    public UserRespDTO getResponseDTO(User user) {
        UserRespDTO userRespDTO = new UserRespDTO();
        userRespDTO.setId(user.getId());
        userRespDTO.setUsername(user.getUsername());
        userRespDTO.setFirstName(user.getFirstName());
        userRespDTO.setPhotoUrl(user.getPhotoUrl());
        return userRespDTO;
    }

    private User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).get();
    }
}
