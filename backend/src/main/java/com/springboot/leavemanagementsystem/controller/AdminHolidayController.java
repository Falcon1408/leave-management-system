package springboot.leavemanagementsystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import springboot.leavemanagementsystem.dto.CreateHolidayRequest;
import springboot.leavemanagementsystem.dto.HolidayResponse;
import springboot.leavemanagementsystem.dto.UpdateHolidayRequest;
import springboot.leavemanagementsystem.service.HolidayService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/holidays")
@PreAuthorize("hasRole('ADMIN')")
public class AdminHolidayController {


    private final HolidayService holidayService;

    public AdminHolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    // CREATE
    @PostMapping
    public HolidayResponse createHoliday(@RequestBody CreateHolidayRequest request) {
        return holidayService.createHoliday(request);
    }

    // GET BY YEAR
    @GetMapping
    public List<HolidayResponse> getHolidays(@RequestParam int year) {
        return holidayService.getHolidaysByYear(year);
    }

    // UPDATE
    @PutMapping("/{id}")
    public HolidayResponse updateHoliday(
            @PathVariable Long id,
            @RequestBody UpdateHolidayRequest request) {

        return holidayService.updateHoliday(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteHoliday(@PathVariable Long id) {
        holidayService.deleteHoliday(id);
    }
}