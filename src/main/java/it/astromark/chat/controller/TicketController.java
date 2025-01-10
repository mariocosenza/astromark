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
    List<TicketResponse> getTickets() {
        return ticketService.getTickets();
    }

    @GetMapping("/{ticketId}/messages")
    List<MessageResponse> getMessages(@PathVariable UUID ticketId) {
        if(ticketId == null)
            return null;

        var ticket = ticketRepository.findById(ticketId).orElseThrow();
        return ticketService.getMessages(ticket);
    }

    @PostMapping("/{ticketId}/addMessage")
    void sendMessage(@PathVariable UUID ticketId, @RequestBody String textMessage) {
        var ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticketService.addMessage(ticket, textMessage.substring(1, textMessage.length() - 1));
    }

    @PostMapping("/newTicket")
    void newTicket(@RequestBody String title) {
        ticketService.newTicket(title);
    }

}


