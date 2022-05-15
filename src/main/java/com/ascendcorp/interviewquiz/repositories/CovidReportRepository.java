package com.ascendcorp.interviewquiz.repositories;

import com.ascendcorp.interviewquiz.entities.CovidReportEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CovidReportRepository extends JpaRepository<CovidReportEntity, Long> {

    @Query("SELECT cr FROM CovidReportEntity cr WHERE cr.date = :date")
    Optional<List<CovidReportEntity>> findByDateIs(@Param("date") String date);

//    @Query("SELECT IFNULL(GREATEST(cr.totalCase),0) FROM CovidReportEntity cr")
//    Long findMaxTotalCase();
//
//    @Query("SELECT IFNULL(LEAST(cr.totalCase),0) FROM CovidReportEntity cr")
//    Long findMinTotalCase();
//
//    @Query("SELECT cr FROM CovidReportEntity cr WHERE cr.totalCase = :totalCase")
//    Optional<List<CovidReportEntity>> findByTotalCase(@Param("totalCase") Long totalCase);

    @Query(value = "SELECT cr1.* FROM COVID_REPORT cr1 WHERE cr1.TOTAL_CASE= (SELECT NVL(MAX(cr2.TOTAL_CASE),0) FROM COVID_REPORT cr2)", nativeQuery = true)
    Optional<List<CovidReportEntity>> findMaxTotalCase();

    @Query(value = "SELECT cr1.* FROM COVID_REPORT cr1 WHERE cr1.TOTAL_CASE= (SELECT NVL(MIN(cr2.TOTAL_CASE),0) FROM COVID_REPORT cr2)", nativeQuery = true)
    Optional<List<CovidReportEntity>> findMinTotalCase();
}
