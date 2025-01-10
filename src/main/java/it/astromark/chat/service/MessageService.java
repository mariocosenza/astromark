package it.astromark.chat.service;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.entity.Message;
import it.astromark.commons.service.CrudService;
import it.astromark.user.commons.model.SchoolUser;

import java.util.UUID;

public interface MessageService extends CrudService<Message, Message, MessageResponse, UUID> {

    SchoolUser getSender(Message message);
}
