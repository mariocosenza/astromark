package it.astromark.chat.service;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.entity.Message;
import it.astromark.user.commons.model.SchoolUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Override
    public SchoolUser getSender(Message message) {
        if (message.getStudent() != null) {
            return message.getStudent();
        } else if (message.getParent() != null) {
            return message.getParent();
        } else if (message.getTeacher() != null) {
            return message.getTeacher();
        } else if (message.getSecretary() != null) {
            return message.getSecretary();
        }
        return null;
    }

    @Override
    public MessageResponse create(Message message) {
        return null;
    }

    @Override
    public MessageResponse update(UUID uuid, Message message) {
        return null;
    }

    @Override
    public boolean delete(UUID uuid) {
        return false;
    }

    @Override
    public Message getById(UUID uuid) {
        return null;
    }
}
