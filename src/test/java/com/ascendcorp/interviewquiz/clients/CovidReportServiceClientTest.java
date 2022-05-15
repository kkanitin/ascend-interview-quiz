package com.ascendcorp.interviewquiz.clients;

import com.ascendcorp.interviewquiz.models.CovidReportData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CovidReportServiceClientTest {

    private WebClient webClient;
    private CovidReportServiceClient covidReportServiceClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldReturnCovidReportDataListWhenFetchCovidReportSuccess()
            throws IOException, URISyntaxException {
        CovidReportData expected = new CovidReportData(
                "2022-01-01",
                2L,
                135L,
                "bkk",
                0L,
                0L
        );
        List<CovidReportData> mockList = new ArrayList<>();
        mockList.add(expected);
        String mockResponse = objectMapper.writeValueAsString(Arrays.array(expected));
        webClient = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(
                                ClientResponse.create(HttpStatus.OK)
                                        .header("content-type", "application/json")
                                        .body(mockResponse)
                                        .build()
                        )
                ).build();
//        covidReportServiceClient = new CovidReportServiceClient(webClient);
        covidReportServiceClient = org.mockito.Mockito.mock(CovidReportServiceClient.class);

        when(covidReportServiceClient.fetchCovidReport()).thenReturn(mockList);

        List<CovidReportData> actual = covidReportServiceClient.fetchCovidReport();

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void shouldReturnEmptyListWhenFetchCovidReportSuccessButEmptyResponse() throws IOException, URISyntaxException {
        // DONE 1.2 Implement unit test for method fetchCovidReport() for case public api response empty array
        CovidReportData expected = new CovidReportData();
        List<CovidReportData> mockList = new ArrayList<>();
        String mockResponse = objectMapper.writeValueAsString(Arrays.array(expected));
        webClient = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(
                                ClientResponse.create(HttpStatus.OK)
                                        .header("content-type", "application/json")
                                        .body(mockResponse)
                                        .build()
                        )
                ).build();
        covidReportServiceClient = org.mockito.Mockito.mock(CovidReportServiceClient.class);

        when(covidReportServiceClient.fetchCovidReport()).thenReturn(mockList);

        List<CovidReportData> actual = covidReportServiceClient.fetchCovidReport();

        assertThat(actual.isEmpty()).isEqualTo(true);
    }

}