package springboot.leavemanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springboot.leavemanagementsystem.entity.LeaveRequest;
import springboot.leavemanagementsystem.entity.LeaveRequestStatus;
import springboot.leavemanagementsystem.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(User user);

    List<LeaveRequest> findByStatus(LeaveRequestStatus status);

    List<LeaveRequest> findByEmployeeAndStatus(User user, LeaveRequestStatus status);

    List<LeaveRequest> findByEmployeeAndStatusIn(User employee, List<LeaveRequestStatus> statuses);

    List<LeaveRequest> findByStatusAndEmployee_Manager(LeaveRequestStatus status, User manager);

    List<LeaveRequest> findByEmployeeOrderByAppliedAtDesc(User employee);

    @Query("""
        SELECT lr FROM LeaveRequest lr
        WHERE (:startDate IS NULL OR lr.startDate >= :startDate)
        AND (:endDate IS NULL OR lr.endDate <= :endDate)
        AND (:leaveTypeId IS NULL OR lr.leaveType.id = :leaveTypeId)
        AND (:status IS NULL OR lr.status = :status)
    """)
    Page<LeaveRequest> findLeaveReports(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("leaveTypeId") Long leaveTypeId,
            @Param("status") LeaveRequestStatus status,
            Pageable pageable
    );

    @Query("""
        SELECT lr
        FROM LeaveRequest lr
        JOIN FETCH lr.leaveType
        WHERE lr.employee = :employee
        ORDER BY lr.appliedAt DESC
    """)
    List<LeaveRequest> findByEmployeeWithLeaveType(User employee);

    @Query("""
    SELECT lr
    FROM LeaveRequest lr
    JOIN FETCH lr.employee e
    JOIN FETCH lr.leaveType lt
    WHERE e.manager = :manager
    ORDER BY lr.startDate DESC
""")
    List<LeaveRequest> findByManagerWithEmployeeAndLeaveType(User manager);
}
