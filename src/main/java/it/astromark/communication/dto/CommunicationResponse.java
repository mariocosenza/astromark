package it.astromark.communication.dto;

import java.time.LocalDate;

public record CommunicationResponse(Integer id, String title, LocalDate date, String description) {

}
