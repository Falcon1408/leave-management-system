package springboot.leavemanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.leavemanagementsystem.entity.LeaveType;
import springboot.leavemanagementsystem.entity.LeaveTypeStatus;

import java.util.List;
import java.util.Optional;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

    Optional<LeaveType> findByNameIgnoreCase(String name);

    List<LeaveType> findByStatus(LeaveTypeStatus status);
}
