package it.astromark.chat.controller;

import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.dto.TicketResponse;
import it.astromark.chat.repository.TicketRepository;
import it.astromark.chat.service.TicketService;
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

    @GetMapping("/ticket")
    public List<TicketResponse> getTickets() {
        return ticketService.getTickets();
    }

    @GetMapping("/{ticketId}/messages")
    public List<MessageResponse> getMessages(@PathVariable UUID ticketId) {
        if (ticketId == null)
            return null;

        var ticket = ticketRepository.findById(ticketId).orElseThrow();
        return ticketService.getMessages(ticket);
    }

    @PostMapping("/{ticketId}/addMessage")
    public UUID sendMessage(@PathVariable UUID ticketId, @RequestBody String textMessage) {
        return ticketService.sendMessage(ticketId, textMessage);
    }

    @PostMapping("/newTicket")
    public void createTicket(@RequestBody String title) {
        ticketService.createTicket(title);
    }

}


