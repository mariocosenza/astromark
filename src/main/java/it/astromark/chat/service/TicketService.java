package it.astromark.chat.service;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.dto.TicketResponse;
import it.astromark.chat.entity.Ticket;
import it.astromark.commons.service.CrudService;

import java.util.List;
import java.util.UUID;

public interface TicketService extends CrudService<Ticket, Ticket, TicketResponse, UUID> {
    List<TicketResponse> getTickets();
    List<MessageResponse> getMessages(Ticket ticket);
    void addMessage(Ticket ticket, String text);
    void newTicket(String title);
}
