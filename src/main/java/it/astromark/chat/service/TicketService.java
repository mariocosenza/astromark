package it.astromark.chat.service;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.dto.TicketResponse;
import it.astromark.chat.entity.Ticket;
import it.astromark.commons.service.CrudService;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing ticket-related operations.
 * Extends the generic CrudService interface to provide CRUD functionality for tickets.
 */
public interface TicketService extends CrudService<Ticket, Ticket, TicketResponse, UUID> {
    /**
     * Retrieves a list of all tickets.
     *
     * @return a list of `TicketResponse` objects representing the tickets
     * Pre-condition: None.
     * Post-condition: Returns a list of all existing tickets.
     */
    List<TicketResponse> getTickets();

    /**
     * Retrieves the messages associated with a specific ticket.
     *
     * @param ticket the `Ticket` object
     * @return a list of `MessageResponse` objects representing the messages related to the ticket
     * Pre-condition: The `ticket` must not be null and must refer to an existing ticket.
     * Post-condition: Returns a list of messages associated with the specified ticket.
     */
    List<MessageResponse> getMessages(Ticket ticket);

    /**
     * Sends a message related to a specific ticket.
     *
     * @param ticketId the UUID of the ticket
     * @param text the text of the message to be sent
     * @return the UUID of the sent message
     * Pre-condition: The `ticketId` must not be null and must refer to an existing ticket. The `text` must not be null or empty.
     * Post-condition: A new message is sent and associated with the specified ticket. Returns the UUID of the sent message.
     */
    UUID sendMessage(UUID ticketId, String text);

    /**
     * Creates a new ticket with the specified title.
     *
     * @param title the title of the ticket
     * Pre-condition: The `title` must not be null or empty.
     * Post-condition: A new ticket is created with the given title.
     */
    void createTicket(String title);

}
