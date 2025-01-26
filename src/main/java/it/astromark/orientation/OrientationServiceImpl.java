package it.astromark.orientation;

import it.astromark.rating.dto.MarkOrientationRequest;
import it.astromark.rating.mapper.MarkMapper;
import it.astromark.user.student.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrientationServiceImpl implements OrientationService {

    private final MarkMapper markMapper;

    public OrientationServiceImpl(MarkMapper markMapper) {
        this.markMapper = markMapper;
    }

    public String attitude(Student student) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://127.0.0.1:5000/ai/orientation";
            HttpEntity<List<MarkOrientationRequest>> requestEntity =
                    new HttpEntity<>(markMapper.toMarkOrientationRequestList(student.getMarks().stream().toList()));

            ResponseEntity<String> result = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return Optional.ofNullable(result.getBody())
                    .orElseThrow(() -> new ServiceException("Empty response from AI orientation service"));
        } catch (RestClientException e) {
            log.error("Error calling AI orientation service", e);
            throw new ServiceException("Failed to retrieve student attitude", e);
        }
    }
}
