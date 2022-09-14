package com.bank.transfers.application.app.aspect;

import com.bank.transfers.application.app.messages.ISendMessage;
import com.bank.transfers.application.domains.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SendAccountAspect {

    private static final String SEND_ACCOUNT = "send.account";
    private final ISendMessage sendMessage;
    private final ObjectMapper objectMapper;

    public SendAccountAspect(final ISendMessage sendMessage, final ObjectMapper objectMapper) {
        this.sendMessage = sendMessage;
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "execution(* com.bank.transfers.application.app.repositories.IAccountRepository.save(..))", returning = "account")
    public void execute(final Account account) {
        sendMessage.execute(this.getObjectAsJson(account), SEND_ACCOUNT);
    }


    private String getObjectAsJson(final Account account) {
        try {
            return objectMapper.writeValueAsString(account);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Error convert object to json");
        }
    }
}
