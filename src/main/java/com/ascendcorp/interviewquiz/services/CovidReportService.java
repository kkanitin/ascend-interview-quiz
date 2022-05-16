package com.ascendcorp.interviewquiz.services;

import com.ascendcorp.interviewquiz.models.CovidProvinceCaseResponse;
import com.ascendcorp.interviewquiz.models.CovidReportData;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

public interface CovidReportService {
    String dateFormat = "yyyy-MM-dd";
    String datePattern = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

    List<CovidReportData> getReports(String date) throws URISyntaxException, ParseException;

    CovidProvinceCaseResponse getHighestLowestReport() throws URISyntaxException;

    boolean isDateFormatValid(String dateStr);

}