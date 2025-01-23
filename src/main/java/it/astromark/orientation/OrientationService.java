package it.astromark.orientation;

import it.astromark.user.student.entity.Student;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Service
@RequestMapping("api/orientations")
public class OrientationService {

    public String attitude(Student student) {
        RestTemplate restTemplate = new RestTemplate();

        String uri = "http://127.0.0.1:5000/ai/orientation";
        ResponseEntity<String> result =
                restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        return result.getBody();
    }
}
