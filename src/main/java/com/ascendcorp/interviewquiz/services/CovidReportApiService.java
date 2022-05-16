package com.ascendcorp.interviewquiz.services;

import com.ascendcorp.interviewquiz.models.CovidProvinceCaseData;
import com.ascendcorp.interviewquiz.models.CovidProvinceCaseResponse;
import com.ascendcorp.interviewquiz.models.CovidReportData;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CovidReportApiService implements CovidReportService {

    @Autowired
    private RestTemplate restTemplate;
    private final String COVID_REPORT_SOURCE_ENDPOINT = "https://covid19.ddc.moph.go.th/api/Cases/round-1to2-by-provinces";

    public CovidReportApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<CovidReportData> getReports(String date) throws URISyntaxException, ParseException {
        // DONE 2.1 Implement the first API by call public API
        List<CovidReportData> covidReportDataList = new ArrayList<>();
        if (null == date || date.isEmpty() || date.isBlank()) {
            throw new ParseException("date cannot be null or empty", 0);
        }
        if (!this.isDateFormatValid(date)) {
            throw new ParseException(String.format("%s cannot parse to date format(%s)", date.trim(), dateFormat), 0);
        }
        URI uri = new URI(COVID_REPORT_SOURCE_ENDPOINT);
        ResponseEntity<List> responseEntity = restTemplate.getForEntity(uri, List.class);
        HttpStatus statusCode = responseEntity.getStatusCode();

        ObjectMapper mapper = new ObjectMapper();

        if (statusCode.equals(HttpStatus.OK)) {
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, CovidReportData.class);
            covidReportDataList.addAll(mapper.convertValue(responseEntity.getBody(), type));
            if (null != date && !date.isEmpty() && !date.isBlank()) {
                covidReportDataList = covidReportDataList.stream().filter(item -> item.getTxnDate().equals(date)).collect(Collectors.toList());
            }
        }

        return covidReportDataList;
    }

    @Override
    public CovidProvinceCaseResponse getHighestLowestReport() throws URISyntaxException {
        // DONE 2.2 Implement the second API by call public API
        List<CovidReportData> covidReportDataList = new ArrayList<>();
        CovidProvinceCaseResponse covidProvinceCaseResponse = new CovidProvinceCaseResponse();
        URI uri = new URI(COVID_REPORT_SOURCE_ENDPOINT);
        ResponseEntity<List> responseEntity = restTemplate.getForEntity(uri, List.class);
        HttpStatus statusCode = responseEntity.getStatusCode();

        ObjectMapper mapper = new ObjectMapper();

        if (statusCode.equals(HttpStatus.OK)) {
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, CovidReportData.class);
            covidReportDataList.addAll(mapper.convertValue(responseEntity.getBody(), type));
            List<CovidProvinceCaseData> highest = new ArrayList<>();
            List<CovidProvinceCaseData> lowest = new ArrayList<>();
            Long max = Long.MIN_VALUE;
            Long min = Long.MAX_VALUE;
            List<Integer> indexOfMaxList = new ArrayList<>();
            List<Integer> indexOfMinList = new ArrayList<>();
            for (int i = 0; i < covidReportDataList.size(); i++) {
                CovidReportData currentItem = covidReportDataList.get(i);
                if (currentItem.getTotalCase() <= min) {
                    if (currentItem.getTotalCase() < min) {
                        indexOfMinList.clear();
                    }
                    indexOfMinList.add(i);
                    min = currentItem.getTotalCase();
                }
                if (currentItem.getTotalCase() >= max) {
                    if (currentItem.getTotalCase() > max) {
                        indexOfMaxList.clear();
                    }
                    indexOfMaxList.add(i);
                    max = currentItem.getTotalCase();
                }
            }
            indexOfMaxList.forEach(i ->
                    highest.add(new CovidProvinceCaseData(covidReportDataList.get(i).getProvince(),
                            covidReportDataList.get(i).getTotalCase())));
            indexOfMinList.forEach(i ->
                    lowest.add(new CovidProvinceCaseData(covidReportDataList.get(i).getProvince(),
                            covidReportDataList.get(i).getTotalCase())));
            covidProvinceCaseResponse.setHighest(highest);
            covidProvinceCaseResponse.setLowest(lowest);

        }
        return covidProvinceCaseResponse;
    }

    @Override
    public boolean isDateFormatValid(String dateStr) {
        return dateStr.matches(datePattern);
    }

    public String getDateFormat() {
        return dateFormat;
    }

}
