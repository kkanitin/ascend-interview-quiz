package com.ascendcorp.interviewquiz.services;

import com.ascendcorp.interviewquiz.clients.CovidReportServiceClient;
import com.ascendcorp.interviewquiz.entities.CovidReportEntity;
import com.ascendcorp.interviewquiz.models.CovidProvinceCaseData;
import com.ascendcorp.interviewquiz.models.CovidProvinceCaseResponse;
import com.ascendcorp.interviewquiz.models.CovidReportData;
import com.ascendcorp.interviewquiz.repositories.CovidReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CovidReportDbService implements CovidReportService {

    private final CovidReportServiceClient covidReportServiceClient;
    private final CovidReportRepository covidReportRepository;
    private final Logger logger = LoggerFactory.getLogger(CovidReportDbService.class);

    public CovidReportDbService(
            CovidReportServiceClient covidReportServiceClient,
            CovidReportRepository covidReportRepository
    ) {
        this.covidReportServiceClient = covidReportServiceClient;
        this.covidReportRepository = covidReportRepository;
    }

    @PostConstruct
    public void fetchCovidReport() {
        logger.info("Start fetchCovidReport()");
        try {
            long start = System.nanoTime();
            ObjectMapper mapper = new ObjectMapper();
            List<CovidReportData> response = covidReportServiceClient.fetchCovidReport();

            Set<CovidReportEntity> entitySet = response
                    .stream()
                    .map(data -> {
                        // DONE 4.1 Initialize data into database
                        return new CovidReportEntity(data.getTxnDate(),
                                data.getNewCase(), data.getTotalCase(),
                                data.getProvince(),
                                data.getNewCaseExcludeAbroad(),
                                data.getTotalCaseExcludeAbroad());
                    }).collect(Collectors.toSet());

            covidReportRepository.saveAll(entitySet);
            logger.info("End fetchCovidReport() [{} ms]", (System.nanoTime() - start) / 1_000_000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public List<CovidReportData> getReports(String date) {
        // DONE 4.2 Implement the first API by query from database
        List<CovidReportEntity> result = covidReportRepository.findByDateIs(date).orElse(new ArrayList<>());
        List<CovidReportData> covidReportDataList = new ArrayList<>();
        result.forEach(item ->
                covidReportDataList.add(new CovidReportData(item.getDate(), item.getNewCase(), item.getTotalCase(),
                        item.getProvince(), item.getNewCaseExcludeAbroad(), item.getTotalCaseExcludeAbroad())));
        return covidReportDataList;
    }

    @Override
    public CovidProvinceCaseResponse getHighestLowestReport() {
        // DONE 4.3 Implement the second API by query from database
        System.out.println("getHighestLowestReport");
        List<CovidReportEntity> maxCovidReportEntity = covidReportRepository.findMaxTotalCase().orElse(new ArrayList<>());
        List<CovidReportEntity> minCovidReportEntity = covidReportRepository.findMinTotalCase().orElse(new ArrayList<>());
        List<CovidProvinceCaseData> highest = maxCovidReportEntity.stream().map(item -> {
            return new CovidProvinceCaseData(item.getProvince(), item.getTotalCase());
        }).collect(Collectors.toList());
        List<CovidProvinceCaseData> lowest = minCovidReportEntity.stream().map(item -> {
            return new CovidProvinceCaseData(item.getProvince(), item.getTotalCase());
        }).collect(Collectors.toList());

        return new CovidProvinceCaseResponse(highest, lowest);
    }

}
