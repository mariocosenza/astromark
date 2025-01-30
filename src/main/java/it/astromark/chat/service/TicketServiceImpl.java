package it.astromark.chat.service;

import it.astromark.authentication.service.AuthenticationService;
import it.astromark.chat.dto.MessageResponse;
import it.astromark.chat.dto.TicketResponse;
import it.astromark.chat.entity.Message;
import it.astromark.chat.entity.Ticket;
import it.astromark.chat.mapper.ChatMapper;
import it.astromark.chat.repository.MessageRepository;
import it.astromark.chat.repository.TicketRepository;
import it.astromark.school.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final AuthenticationService authenticationService;
    private final MessageRepository messageRepository;
    private final ChatMapper chatMapper;
    private final MessageService messageService;
    private final SchoolRepository schoolRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, AuthenticationService authenticationService, MessageRepository messageRepository, ChatMapper chatMapper, MessageService messageService, SchoolRepository schoolRepository) {
        this.ticketRepository = ticketRepository;
        this.authenticationService = authenticationService;
        this.messageRepository = messageRepository;
        this.chatMapper = chatMapper;
        this.messageService = messageService;
        this.schoolRepository = schoolRepository;
    }

    @Override
    @PreAuthorize("hasRole('TEACHER') || hasRole('PARENT') || hasRole('SECRETARY')")
    public List<TicketResponse> getTickets() {
        List<Ticket> ticketList = new ArrayList<>();

        if (authenticationService.isTeacher()) {
            var teacher = authenticationService.getTeacher().orElseThrow();
            ticketList = ticketRepository.findByTeacherAndClosedAndSolved(teacher, false, false);
        } else if (authenticationService.isParent()) {
            var parent = authenticationService.getParent().orElseThrow();
            ticketList = ticketRepository.findByParentAndClosedAndSolved(parent, false, false);
        } else if (authenticationService.isSecretary()) {
            var secretary = authenticationService.getSecretary().orElseThrow();
            var school = schoolRepository.findBySecretariesContains(Set.of(secretary));
            ticketList = ticketRepository.findByTeacher_SchoolOrParent_School(school, school);
        }

        ticketList.sort(Comparator.comparing(Ticket::getDatetime));
        return ticketList.isEmpty() ? null : chatMapper.toTicketResponseList(ticketList);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER') || hasRole('PARENT') || hasRole('SECRETARY')")
    public List<MessageResponse> getMessages(@NotNull Ticket ticket) {

        var messages = messageRepository.findByTicket(ticket);
        if (!messages.isEmpty())
            messages.sort(Comparator.comparing(Message::getDateTime));

        return chatMapper.toMessageResponseList(messages, messageService);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER') || hasRole('PARENT') || hasRole('SECRETARY')")
    public UUID sendMessage(@NotNull UUID ticketId, @NotEmpty String text) {
        var ticket = ticketRepository.findById(ticketId).orElseThrow();
        if (ticket.getClosed() || ticket.getSolved()) {
            throw new IllegalArgumentException("Ticket is closed or solved");
        }
        var message = new Message();
        message.setTicket(ticket);
        message.setText(text);
        message.setDateTime(Instant.now());

        if (authenticationService.isTeacher()) {
            message.setTeacher(authenticationService.getTeacher().orElseThrow());
        } else if (authenticationService.isParent()) {
            message.setParent(authenticationService.getParent().orElseThrow());
        } else if (authenticationService.isSecretary()) {
            message.setSecretary(authenticationService.getSecretary().orElseThrow());
        } else {
            throw new AccessDeniedException("Cannot send ticket");
        }

        return messageRepository.save(message).getId();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TEACHER') || hasRole('PARENT')")
    public void createTicket(@NotEmpty String title) {

        var ticket = new Ticket();
        ticket.setDatetime(new Date().toInstant());
        ticket.setCategory("Category");
        ticket.setTitle(title.substring(1, title.length() - 1));

        if (authenticationService.isTeacher()) {
            ticket.setTeacher(authenticationService.getTeacher().orElseThrow());
        } else if (authenticationService.isParent()) {
            ticket.setParent(authenticationService.getParent().orElseThrow());
        } else return;

        ticketRepository.save(ticket);
    }


    @Override
    @PreAuthorize("hasRole('SECRETARY')")
    public boolean closeUnsolved(@NotNull UUID ticketId) {
        var secretary = authenticationService.getSecretary().orElseThrow();
        var school = schoolRepository.findBySecretariesContains(Set.of(secretary));
        var ticket = ticketRepository.findById(ticketId).orElseThrow();
        if (ticketRepository.existsByIdAndTeacher_SchoolOrParent_School(ticketId, school, school)) {
            return closeTicket(ticket, false);
        }
        return false;
    }

    @Override
    @PreAuthorize("hasRole('SECRETARY')")
    public boolean closeAndSolve(@NotNull UUID ticketId) {
        var secretary = authenticationService.getSecretary().orElseThrow();
        var school = schoolRepository.findBySecretariesContains(Set.of(secretary));
        var ticket = ticketRepository.findById(ticketId).orElseThrow();
        if (ticketRepository.existsByIdAndTeacher_SchoolOrParent_School(ticketId, school, school)) {
            return closeTicket(ticket, true);
        }
        return false;
    }

    private boolean closeTicket(Ticket ticket, boolean solved) {
        if (!ticket.getClosed() && !ticket.getSolved()) {
            ticket.setClosed(true);
            ticket.setSolved(solved);
            ticketRepository.save(ticket);
            return true;
        }
        return false;
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
    public boolean delete(UUID uuid) {
        return false;
    }

    @Override
    public Ticket getById(UUID uuid) {
        return null;
    }
}
