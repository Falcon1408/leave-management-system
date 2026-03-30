package springboot.leavemanagementsystem.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.Check;

import java.util.Objects;

@Entity
@Table(name = "leave_types")
@Check(constraints = "max_days_per_year >= 0 AND max_days_per_year <= 365")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @PositiveOrZero(message = "Max days must be zero or positive")
    @Max(value = 365, message = "Max days cannot exceed 365")
    @Column(nullable = false)
    private Integer maxDaysPerYear;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveTypeStatus status;

    @Column(nullable = false)
    private Boolean carryForwardAllowed;

    public LeaveType() {

    }

    public LeaveType(String name, Integer maxDaysPerYear, String description) {
        this.name = name;
        this.maxDaysPerYear = maxDaysPerYear;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
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

    public LeaveTypeStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveTypeStatus status) {
        this.status = status;
    }

    public Boolean getCarryForwardAllowed() {
        return carryForwardAllowed;
    }

    public void setCarryForwardAllowed(Boolean carryForwardAllowed) {
        this.carryForwardAllowed = carryForwardAllowed;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LeaveType leaveType)){
            return false;
        }
        return Objects.equals(id, leaveType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LeaveType{" +
                "name='" + name + '\'' +
                ", maxDaysPerYear=" + maxDaysPerYear +
                ", description='" + description + '\'' +
                '}';
    }
}
