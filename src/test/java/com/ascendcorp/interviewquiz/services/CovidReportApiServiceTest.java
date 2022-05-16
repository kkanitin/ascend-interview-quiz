package com.ascendcorp.interviewquiz.services;

import com.ascendcorp.interviewquiz.models.CovidProvinceCaseResponse;
import com.ascendcorp.interviewquiz.models.CovidReportData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class CovidReportApiServiceTest {


    private CovidReportApiService covidReportApiService;

    @Before
    public void setUp(){
        RestTemplate restTemplate = new RestTemplate();
        covidReportApiService = new CovidReportApiService(restTemplate);
    }

    // DONE 2.3 Add unit tests for method getReports() and line coverage must be 100%
    @Test
    public void getReportsShouldSuccessAndReturnWithEmptyResponse() throws URISyntaxException, ParseException {

        List<CovidReportData> result = covidReportApiService.getReports("2022-01-01");

        assertNotNull(result);
    }

    @Test
    public void getReportsShouldSuccessAndReturnWithResponse() throws URISyntaxException, ParseException {

        List<CovidReportData> result = covidReportApiService.getReports("2020-01-12");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getReportsShouldError() {
        ParseException thrown = assertThrows(ParseException.class, () -> covidReportApiService.getReports(""), "date cannot be null or empty");

        assertEquals("date cannot be null or empty", thrown.getMessage());
    }

    // DONE 2.4 Add unit tests for method getHighestLowestReport() and line coverage must be 100%
    @Test
    public void getHighestLowestReportSuccessAndReturnWithResponse() throws URISyntaxException {
        CovidProvinceCaseResponse result = covidReportApiService.getHighestLowestReport();

        assertNotNull(result);
        assertNotNull(result.getLowest());
        assertNotNull(result.getHighest());
    }

}