package com.ascendcorp.interviewquiz.clients;

import com.ascendcorp.interviewquiz.models.CovidReportData;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CovidReportServiceClient {

    @Qualifier("webClient")
    private final WebClient webClient;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String COVID_REPORT_SOURCE_ENDPOINT = "https://covid19.ddc.moph.go.th/api/Cases/round-1to2-by-provinces";

    public CovidReportServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<CovidReportData> fetchCovidReport() throws IOException, URISyntaxException {//
        // DONE 1.1 Implement calling public api to get covid cases data
        List<CovidReportData> responseList = new ArrayList<>();
        try {
            URI uri = new URI(COVID_REPORT_SOURCE_ENDPOINT);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<List> responseEntity = restTemplate.getForEntity(uri, List.class);
            HttpStatus statusCode = responseEntity.getStatusCode();

            ObjectMapper mapper = new ObjectMapper();

            if (statusCode.equals(HttpStatus.OK)) {
                JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, CovidReportData.class);
                responseList.addAll(mapper.convertValue(responseEntity.getBody(), type));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseList;
    }

}
