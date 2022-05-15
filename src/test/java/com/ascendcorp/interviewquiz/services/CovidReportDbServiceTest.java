package com.ascendcorp.interviewquiz.services;

import com.ascendcorp.interviewquiz.clients.CovidReportServiceClient;
import com.ascendcorp.interviewquiz.entities.CovidReportEntity;
import com.ascendcorp.interviewquiz.models.CovidProvinceCaseResponse;
import com.ascendcorp.interviewquiz.models.CovidReportData;
import com.ascendcorp.interviewquiz.repositories.CovidReportRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CovidReportDbServiceTest {

    @Mock
    private CovidReportServiceClient covidReportServiceClient;

    @Mock
    private CovidReportRepository covidReportRepository;

    private CovidReportDbService covidReportDbService;

    @Before
    public void setup() {
        covidReportDbService = new CovidReportDbService(
                covidReportServiceClient,
                covidReportRepository
        );
    }

    @Test
    public void shouldSaveDataToDBWhenFetchCovidReportSuccess() throws IOException, URISyntaxException {
        CovidReportData mockResponse1 = new CovidReportData(
                "2022-01-01",
                2L,
                135L,
                "bkk",
                0L,
                0L
        );

        when(covidReportServiceClient.fetchCovidReport()).thenReturn(List.of(mockResponse1));

        covidReportDbService.fetchCovidReport();

        verify(covidReportRepository).saveAll(any());
    }

    // DONE 4.4 Add unit tests for method getReports() and line coverage must be 100%
    @Test
    public void getReportsShouldSuccessAndReturnWithResponse() {
        List<CovidReportData> covidReportData = new ArrayList<>() {
            {
                add(new CovidReportData("2022-01-02", 100L, 1000L, "province1", 20L, 30L));
                add(new CovidReportData("2022-01-02", 100L, 2000L, "province2", 20L, 30L));
            }
        };

        List<CovidReportEntity> covidReportEntityList = new ArrayList<>() {
            {
                add(new CovidReportEntity("2022-01-02", 100L, 1000L, "province1", 20L, 30L));
                add(new CovidReportEntity("2022-01-02", 100L, 1000L, "province2", 20L, 30L));
            }
        };

        when(covidReportRepository.findByDateIs(anyString())).thenReturn(Optional.of(covidReportEntityList));

        List<CovidReportData> result = covidReportDbService.getReports("2022-01-02");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.size(), covidReportData.size());
    }

    // DONE 4.5 Add unit tests for method getHighestLowestReport() and line coverage must be 100%
    @Test
    public void getHighestLowestReportShouldSuccessAndReturnWithResponse() {
        List<CovidReportEntity> highestReportEntityList = new ArrayList<>() {
            {
                add(new CovidReportEntity("2022-01-02", 100L, 1000L, "province1", 20L, 30L));
                add(new CovidReportEntity("2022-01-02", 100L, 1000L, "province2", 20L, 30L));
            }
        };
        List<CovidReportEntity> lowestReportEntityList = new ArrayList<>() {
            {
                add(new CovidReportEntity("2022-01-02", 1L, 10L, "province1", 20L, 30L));
                add(new CovidReportEntity("2022-01-02", 1L, 10L, "province2", 20L, 30L));
                add(new CovidReportEntity("2022-01-02", 1L, 10L, "province3", 20L, 30L));
            }
        };

        when(covidReportRepository.findMaxTotalCase()).thenReturn(Optional.of(highestReportEntityList));
        when(covidReportRepository.findMinTotalCase()).thenReturn(Optional.of(lowestReportEntityList));

        CovidProvinceCaseResponse result = covidReportDbService.getHighestLowestReport();

        assertNotNull(result);
        assertFalse(result.getHighest().isEmpty());
        assertFalse(result.getLowest().isEmpty());
        assertEquals(result.getHighest().size(), highestReportEntityList.size());
        assertEquals(result.getLowest().size(), lowestReportEntityList.size());
    }

}