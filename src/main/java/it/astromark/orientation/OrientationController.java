package it.astromark.orientation;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/orientations")
public class OrientationController {

    @GetMapping
    public String attitude() {
        RestTemplate restTemplate = new RestTemplate();

        String uri = "http://127.0.0.1:5000/ai/orientation";
        ResponseEntity<String> result =
                restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        return result.getBody();
    }
}
