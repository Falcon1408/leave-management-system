package springboot.leavemanagementsystem.dto;

import java.time.LocalDate;

public class HolidayResponse {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    public HolidayResponse(Long id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public LocalDate getStartDate() { return startDate; }

    public LocalDate getEndDate() { return endDate; }
}