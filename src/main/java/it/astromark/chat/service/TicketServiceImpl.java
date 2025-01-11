package it.astromark.chat.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.dto.TicketResponse;
import it.astromark.chat.entity.Message;
import it.astromark.chat.entity.Ticket;
import it.astromark.chat.mapper.ChatMapper;
import it.astromark.chat.repository.MessageRepository;
import it.astromark.chat.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final AuthenticationService authenticationService;
    private final MessageRepository messageRepository;
    private final ChatMapper chatMapper;
    private final MessageService messageService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, AuthenticationService authenticationService, MessageRepository messageRepository, ChatMapper chatMapper, MessageService messageService) {
        this.ticketRepository = ticketRepository;
        this.authenticationService = authenticationService;
        this.messageRepository = messageRepository;
        this.chatMapper = chatMapper;
        this.messageService = messageService;
    }

    @Override
    @PreAuthorize("hasRole('teacher') || hasRole('parent')")
    public List<TicketResponse> getTickets() {
        List<Ticket> ticketList = new ArrayList<>();

        if(authenticationService.isTeacher()){
            var teacher = authenticationService.getTeacher().orElseThrow();
            ticketList = ticketRepository.findByTeacherAndClosedAndSolved(teacher, false, false);
        } else if(authenticationService.isParent()){
            var parent = authenticationService.getParent().orElseThrow();
            ticketList = ticketRepository.findByParentAndClosedAndSolved(parent, false, false);
        }

        ticketList.sort(Comparator.comparing(Ticket::getDatetime));
        return ticketList.isEmpty() ? null : chatMapper.toTicketResponseList(ticketList);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('teacher') || hasRole('parent') || hasRole('secretary')")
    public List<MessageResponse> getMessages(Ticket ticket){

        var messages = messageRepository.findByTicket(ticket);
        if(!messages.isEmpty())
            messages.sort(Comparator.comparing(Message::getDateTime));

        return chatMapper.toMessageResponseList(messages, messageService);
    }

    @Override
    @PreAuthorize("hasRole('teacher') || hasRole('parent') || hasRole('secretary')")
    public void addMessage(Ticket ticket, String text) {
        var message = new Message();
        message.setId(UUID.randomUUID());
        message.setTicket(ticket);
        message.setText(text.substring(1, text.length() - 1));
        message.setDateTime(new Date().toInstant());

        if(authenticationService.isTeacher()){
            message.setTeacher(authenticationService.getTeacher().orElseThrow());
        } else if(authenticationService.isParent()){
            message.setParent(authenticationService.getParent().orElseThrow());
        } else if(authenticationService.isSecretary()){
            message.setSecretary(authenticationService.getSecretary().orElseThrow());
        } else return;

        messageRepository.save(message);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('teacher') || hasRole('parent')")
    public void newTicket(String title) {

        var ticket = new Ticket();
        ticket.setDatetime(new Date().toInstant());
        ticket.setCategory("Category");
        ticket.setTitle(title.substring(1, title.length() - 1));

        if(authenticationService.isTeacher()){
            ticket.setTeacher(authenticationService.getTeacher().orElseThrow());
        } else if(authenticationService.isParent()){
            ticket.setParent(authenticationService.getParent().orElseThrow());
        } else return;

        ticketRepository.save(ticket);
    }

    @Override
    public TicketResponse create(Ticket ticket) {
        return null;
    }

    @Override
    public TicketResponse update(UUID uuid, Ticket ticket) {
        return null;
    }

    @Override
    public TicketResponse delete(UUID uuid) {
        return null;
    }

    @Override
    public TicketResponse getById(UUID uuid) {
        return null;
    }
}
