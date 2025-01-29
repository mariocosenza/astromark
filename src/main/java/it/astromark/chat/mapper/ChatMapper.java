package it.astromark.chat.mapper;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.dto.TicketResponse;
import it.astromark.chat.entity.Message;
import it.astromark.chat.entity.Ticket;
import it.astromark.chat.service.MessageService;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {

    @Mapping(target = "isTeacher", expression = "java(ticket.getTeacher() != null)")
    TicketResponse toTicketResponse(Ticket ticket);

    List<TicketResponse> toTicketResponseList(List<Ticket> tickets);

    @Mapping(target = "senderName", source = "message", qualifiedByName = "getSenderName")
    @Mapping(target = "isSecretary", expression = "java(message.getSecretary() != null)")
    MessageResponse toMessageResponse(Message message, @Context MessageService messageService);

    @Named("getSenderName")
    default String getSenderName(Message message, @Context MessageService messageService) {
        var sender = messageService.getSender(message);
        return sender != null ? sender.getName() : null;
    }

    List<MessageResponse> toMessageResponseList(List<Message> messages, @Context MessageService messageService);

}
