package springboot.leavemanagementsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.leavemanagementsystem.dto.CreateHolidayRequest;
import springboot.leavemanagementsystem.dto.HolidayResponse;
import springboot.leavemanagementsystem.dto.UpdateHolidayRequest;
import springboot.leavemanagementsystem.entity.Holiday;
import springboot.leavemanagementsystem.repository.HolidayRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    // CREATE HOLIDAY
    public HolidayResponse createHoliday(CreateHolidayRequest request) {

        validateDateRange(request.getStartDate(), request.getEndDate());
        validateOverlappingHoliday(request.getStartDate(), request.getEndDate(), null);

        Holiday holiday = new Holiday(
                request.getName(),
                request.getStartDate(),
                request.getEndDate()
        );

        Holiday savedHoliday = holidayRepository.save(holiday);

        return mapToDto(savedHoliday);
    }

    // UPDATE HOLIDAY
    public HolidayResponse updateHoliday(Long id, UpdateHolidayRequest request) {

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));

        validateDateRange(request.getStartDate(), request.getEndDate());
        validateOverlappingHoliday(request.getStartDate(), request.getEndDate(), id);

        holiday.setName(request.getName());
        holiday.setStartDate(request.getStartDate());
        holiday.setEndDate(request.getEndDate());

        Holiday updatedHoliday = holidayRepository.save(holiday);

        return mapToDto(updatedHoliday);
    }

    // DELETE HOLIDAY
    public void deleteHoliday(Long id) {

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));

        holidayRepository.delete(holiday);
    }

    // GET HOLIDAYS BY YEAR
    @Transactional(readOnly = true)
    public List<HolidayResponse> getHolidaysByYear(int year) {

        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        List<Holiday> holidays = holidayRepository
                .findOverlappingHolidays(startOfYear, endOfYear);

        return holidays.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // VALIDATE DATE RANGE
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    // VALIDATE OVERLAPPING HOLIDAYS
    private void validateOverlappingHoliday(LocalDate startDate, LocalDate endDate, Long currentHolidayId) {

        List<Holiday> overlapping = holidayRepository
                .findOverlappingHolidays(startDate, endDate);

        if (!overlapping.isEmpty()) {

            if (currentHolidayId == null) {
                throw new IllegalArgumentException("Holiday overlaps with an existing holiday");
            }

            boolean conflict = overlapping.stream()
                    .anyMatch(h -> !h.getId().equals(currentHolidayId));

            if (conflict) {
                throw new IllegalArgumentException("Holiday overlaps with an existing holiday");
            }
        }
    }

    // MAP ENTITY TO DTO
    private HolidayResponse mapToDto(Holiday holiday) {

        return new HolidayResponse(
                holiday.getId(),
                holiday.getName(),
                holiday.getStartDate(),
                holiday.getEndDate()
        );
    }
}