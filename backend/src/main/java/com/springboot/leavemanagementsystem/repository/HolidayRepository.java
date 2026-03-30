package springboot.leavemanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springboot.leavemanagementsystem.entity.Holiday;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    @Query("""
        SELECT h FROM Holiday h
        WHERE h.startDate <= :endDate
        AND h.endDate >= :startDate
    """)
    List<Holiday> findOverlappingHolidays(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}