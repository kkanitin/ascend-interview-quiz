package com.ascendcorp.interviewquiz.services;

import com.ascendcorp.interviewquiz.models.CovidProvinceCaseResponse;
import com.ascendcorp.interviewquiz.models.CovidReportData;

import java.net.URISyntaxException;
import java.util.List;

public interface CovidReportService {

    List<CovidReportData> getReports(String date) throws URISyntaxException;

    CovidProvinceCaseResponse getHighestLowestReport() throws URISyntaxException;

}