package veniamin.backend.spring_telegram.service;

import jakarta.servlet.http.HttpServletRequest;
import veniamin.backend.spring_telegram.dto.request.TelegramAuthorizeReqDTO;

public interface AuthorizeService {

    void authorizeUserByTelegram(TelegramAuthorizeReqDTO userAuthorizeDTO, HttpServletRequest http);
}
