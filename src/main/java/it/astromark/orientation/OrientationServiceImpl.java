package it.astromark.orientation;

import it.astromark.rating.dto.MarkOrientationRequest;
import it.astromark.rating.mapper.MarkMapper;
import it.astromark.user.student.entity.Student;
import it.astromark.user.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrientationServiceImpl implements OrientationService {

    private final MarkMapper markMapper;
    private final StudentRepository studentRepository;

    public OrientationServiceImpl(MarkMapper markMapper, StudentRepository studentRepository) {
        this.markMapper = markMapper;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public String attitude(Student student) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://127.0.0.1:5000/ai/orientation";
            student = studentRepository.findById(student.getId())
                    .orElseThrow(() -> new ServiceException("Student not found"));
            // Convert marks to MarkOrientationRequest list
            List<MarkOrientationRequest> markOrientationRequests = markMapper.toMarkOrientationRequestList(student.getMarks().stream().toList());

            // Set up headers to specify JSON content type
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HttpEntity with JSON body
            HttpEntity<List<MarkOrientationRequest>> requestEntity =
                    new HttpEntity<>(markOrientationRequests, headers);

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
