package it.astromark.chat.controller;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.dto.TicketResponse;
import it.astromark.chat.repository.TicketRepository;
import it.astromark.chat.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketController(TicketService ticketService, TicketRepository ticketRepository) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
    }

    @Operation(
            summary = "Get all tickets",
            description = "Retrieves a list of all tickets available in the system."
    )
    @GetMapping("/ticket")
    public List<TicketResponse> getTickets() {
        return ticketService.getTickets();
    }

    @Operation(
            summary = "Get messages for a ticket",
            description = "Retrieves all messages associated with a specific ticket by its ID."
    )
    @GetMapping("/{ticketId}/messages")
    public List<MessageResponse> getMessages(@PathVariable UUID ticketId) {
        if (ticketId == null)
            return null;

        var ticket = ticketRepository.findById(ticketId).orElseThrow();
        return ticketService.getMessages(ticket);
    }

    @Operation(
            summary = "Send a message to a ticket",
            description = "Adds a new message to a specific ticket by its ID."
    )
    @PostMapping("/{ticketId}/addMessage")
    public UUID sendMessage(@PathVariable UUID ticketId, @RequestBody String textMessage) {
        return ticketService.sendMessage(ticketId, textMessage);
    }

    @Operation(
            summary = "Create a new ticket",
            description = "Creates a new ticket with the given title."
    )
    @PostMapping("/newTicket")
    public void createTicket(@RequestBody String title) {
        ticketService.createTicket(title);
    }
}
