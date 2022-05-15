package com.ascendcorp.interviewquiz.controllers;

import com.ascendcorp.interviewquiz.models.CovidProvinceCaseData;
import com.ascendcorp.interviewquiz.models.CovidProvinceCaseResponse;
import com.ascendcorp.interviewquiz.models.CovidReportData;
import com.ascendcorp.interviewquiz.services.CovidReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CovidReportControllerTest {

    @Mock
    private CovidReportService covidReportService;

    private CovidReportController covidReportController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private TestRestTemplate testRestTemplate = new TestRestTemplate();
    private String ip;
    private final String CONTEXT_PATH = "/demo/api";
    private final int PORT = 8080;

//    @BeforeAll
//    public void getLocalHost() throws UnknownHostException {
//
//    }

    @Before
    public void setup() throws UnknownHostException {
        covidReportController = new CovidReportController(covidReportService);
        mockMvc = MockMvcBuilders.standaloneSetup(covidReportController).build();
        InetAddress inetAddress = InetAddress.getLocalHost();
        ip = String.format("%s%s%s", "http://", inetAddress.getHostAddress(), ":");
    }

    @Test
    public void shouldResponseOkAndCovidReportDataListWhenCallApiGetReportsWithDateSuccess()
            throws Exception {
        CovidReportData mockResponse1 = new CovidReportData(
                "2022-01-01",
                2L,
                135L,
                "bkk",
                0L,
                0L
        );
        CovidReportData mockResponse2 = new CovidReportData(
                "2022-01-02",
                3L,
                138L,
                "bkk",
                0L,
                0L
        );

        when(covidReportService.getReports(anyString())).thenReturn(List.of(mockResponse2, mockResponse1));

        mockMvc.perform(
                        get("/covid-cases/reports").param("date", "2022-01-01")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(List.of(mockResponse2, mockResponse1))));
    }

    @Test
    public void shouldResponseOkAndEmptyListWhenCallApiGetReportsWithoutDateSuccessButNoData() throws Exception {
        // DONE 1.3 Implement unit test for api Get Reports for case no date param and api result is empty list
        when(covidReportService.getReports(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/covid-cases/reports").param("date", "2022-01-01")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(new ArrayList<>())));
    }

    @Test
    public void shouldResponseOkAndCovidProvinceCaseResponseWhenCallApiGetHighestLowestReportsSuccess() throws Exception {
        // DONE 1.4 Implement unit test for api Get Highest Lowest Reports for case success
        List<CovidProvinceCaseData> highest = new ArrayList<>() {
            {
                add(new CovidProvinceCaseData("BKK", 1000L));
                add(new CovidProvinceCaseData("NON", 800L));
            }
        };
        List<CovidProvinceCaseData> lowest = new ArrayList<>() {
            {
                add(new CovidProvinceCaseData("CNX", 20L));
            }
        };
        CovidProvinceCaseResponse covidProvinceCaseResponse = new CovidProvinceCaseResponse(highest, lowest);
        when(covidReportService.getHighestLowestReport()).thenReturn(covidProvinceCaseResponse);

        mockMvc.perform(
                        get("/covid-cases/reports/highest-lowest")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(covidProvinceCaseResponse)));
    }

    @Test
    public void shouldResponseOkAndEmptyResponseWhenCallApiGetHighestLowestReportsSuccessButNodata()
            throws Exception {
        CovidProvinceCaseResponse expected = new CovidProvinceCaseResponse(List.of(), List.of());

        when(covidReportService.getHighestLowestReport()).thenReturn(expected);

        mockMvc.perform(
                        get("/covid-cases/reports/highest-lowest")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

}