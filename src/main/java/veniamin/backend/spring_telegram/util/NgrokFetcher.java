package veniamin.backend.spring_telegram.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

public class NgrokFetcher {

    public static String getNgrokUrl() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://host.docker.internal:4040/api/tunnels", String.class);
//        String response = restTemplate.getForObject("http://127.0.0.1:4040/api/tunnels", String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        JsonNode tunnels = root.path("tunnels");

        if (tunnels.isArray() && tunnels.size() > 0) {
            String publicUrl = tunnels.get(0).path("public_url").asText();
            return publicUrl;
        } else {
            throw new RuntimeException("No active tunnels found");
        }
    }
}
