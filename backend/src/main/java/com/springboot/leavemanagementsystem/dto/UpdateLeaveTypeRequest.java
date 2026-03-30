package springboot.leavemanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class UpdateLeaveTypeRequest {

    @NotNull
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer maxDaysPerYear;

    private String description;

    @NotNull
    private Boolean carryForwardAllowed;

    // getters & setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxDaysPerYear() {
        return maxDaysPerYear;
    }

    public void setMaxDaysPerYear(Integer maxDaysPerYear) {
        this.maxDaysPerYear = maxDaysPerYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCarryForwardAllowed() {
        return carryForwardAllowed;
    }

    public void setCarryForwardAllowed(Boolean carryForwardAllowed) {
        this.carryForwardAllowed = carryForwardAllowed;
    }
}