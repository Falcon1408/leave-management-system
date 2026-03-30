package springboot.leavemanagementsystem.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;
import springboot.leavemanagementsystem.entity.LeaveType;
import springboot.leavemanagementsystem.entity.User;

@Entity
@Table(
        name = "leave_balances",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "user_id",
                        "leave_type_id",
                        "year"
                })
        }
)
@Check(constraints = "remaining_days >= 0 AND used_days >= 0")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "total_allocated", nullable = false)
    private Integer totalAllocated;

    @Column(name = "used_days", nullable = false)
    private Integer usedDays;

    @Column(name = "remaining_days", nullable = false)
    private Integer remainingDays;

    public LeaveBalance() {
    }

    public LeaveBalance(User user,
                        LeaveType leaveType,
                        Integer year,
                        Integer totalAllocated) {

        this.user = user;
        this.leaveType = leaveType;
        this.year = year;
        this.totalAllocated = totalAllocated;
        this.usedDays = 0;
        this.remainingDays = totalAllocated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTotalAllocated() {
        return totalAllocated;
    }

    public void setTotalAllocated(Integer totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public Integer getUsedDays() {
        return usedDays;
    }

    public void setUsedDays(Integer usedDays) {
        this.usedDays = usedDays;
    }

    public Integer getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(Integer remainingDays) {
        this.remainingDays = remainingDays;
    }
}