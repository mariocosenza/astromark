package it.astromark.communication.mapping;

import it.astromark.communication.dto.CommunicationResponse;
import it.astromark.communication.entity.Communication;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommunicationMapper {
    CommunicationResponse toCommunicationResponse(Communication communication);

    List<CommunicationResponse> toCommunicationResponseList(List<Communication> communications);
}
