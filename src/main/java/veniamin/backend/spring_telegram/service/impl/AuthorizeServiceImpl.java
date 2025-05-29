package veniamin.backend.spring_telegram.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import veniamin.backend.spring_telegram.dto.request.TelegramAuthorizeReqDTO;
import veniamin.backend.spring_telegram.entity.Role;
import veniamin.backend.spring_telegram.entity.User;
import veniamin.backend.spring_telegram.exception.NotFoundException;
import veniamin.backend.spring_telegram.exception.error.NotFoundError;
import veniamin.backend.spring_telegram.repository.UserRepository;
import veniamin.backend.spring_telegram.service.AuthorizeService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class AuthorizeServiceImpl implements AuthorizeService {

    private final UserRepository userRepository;

    @Value("${telegram.bot.token}")
    private String TELEGRAM_BOT_TOKEN;


    @Override
    @Transactional
    public void authorizeUserByTelegram(TelegramAuthorizeReqDTO authorizeReqDTO, HttpServletRequest http) {

        if (!telegramDataIsValid(getTelegramData(authorizeReqDTO))) {
            throw new NotFoundException(NotFoundError.USER_NOT_FOUND);
        }

        User user = userRepository.findById(authorizeReqDTO.getId()).orElse(new User());
        user.setId(authorizeReqDTO.getId());
        user.setFirstName(authorizeReqDTO.getFirstName());
        user.setLastName(authorizeReqDTO.getLastName());
        user.setUsername(authorizeReqDTO.getUsername());
        user.setPhotoUrl(authorizeReqDTO.getPhotoUrl());
        user.setAuthDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(authorizeReqDTO.getAuthDate()), ZoneOffset.UTC));
        user.setHash(authorizeReqDTO.getHash());
        user.getRoles().add(Role.ROLE_USER);
        userRepository.save(user);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        http.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
    }


    private boolean telegramDataIsValid(Map<String, Object> telegramData) {
        String hash = (String) telegramData.get("hash");
        telegramData.remove("hash");

        StringBuilder sb = new StringBuilder();
        telegramData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n"));
        sb.deleteCharAt(sb.length() - 1);
        String dataCheckString = sb.toString();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] key = digest.digest(TELEGRAM_BOT_TOKEN.getBytes(UTF_8));

            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            hmac.init(secretKeySpec);

            byte[] hmacBytes = hmac.doFinal(dataCheckString.getBytes(UTF_8));
            StringBuilder validateHash = new StringBuilder();
            for (byte b : hmacBytes) {
                validateHash.append(String.format("%02x", b));
            }

            return hash.contentEquals(validateHash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }


    private Map<String, Object> getTelegramData(TelegramAuthorizeReqDTO authorizeReqDTO) {
        Map<String, Object> telegramData = new HashMap<>();
        if (authorizeReqDTO.getId() != null) telegramData.put("id", authorizeReqDTO.getId());
        if (authorizeReqDTO.getFirstName() != null) telegramData.put("first_name", authorizeReqDTO.getFirstName());
        if (authorizeReqDTO.getLastName() != null) telegramData.put("last_name", authorizeReqDTO.getLastName());
        if (authorizeReqDTO.getUsername() != null) telegramData.put("username", authorizeReqDTO.getUsername());
        if (authorizeReqDTO.getPhotoUrl() != null) telegramData.put("photo_url", authorizeReqDTO.getPhotoUrl());
        if (authorizeReqDTO.getAuthDate() != null) telegramData.put("auth_date", authorizeReqDTO.getAuthDate());
        if (authorizeReqDTO.getHash() != null) telegramData.put("hash", authorizeReqDTO.getHash());

        return telegramData;
    }
}
