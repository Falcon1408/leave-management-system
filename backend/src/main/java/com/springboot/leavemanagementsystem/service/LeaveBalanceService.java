package springboot.leavemanagementsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.leavemanagementsystem.dto.LeaveBalanceDto;
import springboot.leavemanagementsystem.entity.LeaveBalance;
import springboot.leavemanagementsystem.entity.LeaveType;
import springboot.leavemanagementsystem.entity.User;
import springboot.leavemanagementsystem.repository.LeaveBalanceRepository;
import springboot.leavemanagementsystem.repository.LeaveTypeRepository;
import springboot.leavemanagementsystem.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveBalanceService(
            LeaveBalanceRepository leaveBalanceRepository,
            UserRepository userRepository,
            LeaveTypeRepository leaveTypeRepository
    ) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.userRepository = userRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    /*
     * Fetch paginated leave balances with optional user filter
     */
    @Transactional
    public Page<LeaveBalanceDto> getBalances(
            Long userId,
            Integer year,
            Pageable pageable
    ) {

        // DEFAULT YEAR
        int targetYear = (year != null) ? year : LocalDate.now().getYear();

        // ENSURE DATA EXISTS
        ensureYearInitialized(targetYear);

        Page<LeaveBalance> page;

        if (userId != null) {
            page = leaveBalanceRepository
                    .findByUser_IdAndYear(userId, targetYear, pageable);
        } else {
            page = leaveBalanceRepository
                    .findByYear(targetYear, pageable);
        }

        return page.map(this::mapToDto);
    }

    /*
     * Ensure leave balances exist for given year (AUTO FIX)
     */
    @Transactional
    public void ensureYearInitialized(int year) {

        List<User> users = userRepository.findAll();
        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        for (User user : users) {
            for (LeaveType leaveType : leaveTypes) {

                leaveBalanceRepository
                        .findByUserAndLeaveTypeAndYear(user, leaveType, year)
                        .orElseGet(() -> {

                            LeaveBalance balance = new LeaveBalance(
                                    user,
                                    leaveType,
                                    year,
                                    leaveType.getMaxDaysPerYear()
                            );

                            return leaveBalanceRepository.save(balance);
                        });
            }
        }
    }

    /*
     * Manual yearly initialization (Admin trigger)
     */
    @Transactional
    public void initializeYear(Integer year) {
        ensureYearInitialized(year);
    }

    /*
     * Adjust total allocated leave
     */
    @Transactional
    public void adjustBalance(Long balanceId, Integer newTotalAllocated) {

        LeaveBalance balance = leaveBalanceRepository.findById(balanceId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        if (newTotalAllocated < balance.getUsedDays()) {
            throw new RuntimeException("Cannot allocate less than used days");
        }

        balance.setTotalAllocated(newTotalAllocated);
        balance.setRemainingDays(newTotalAllocated - balance.getUsedDays());

        leaveBalanceRepository.save(balance);
    }

    /*
     * Convert entity to DTO
     */
    private LeaveBalanceDto mapToDto(LeaveBalance lb) {

        return new LeaveBalanceDto(
                lb.getId(),
                lb.getUser().getId(),
                lb.getUser().getFirstName() + " " + lb.getUser().getLastName(),
                lb.getLeaveType().getId(),
                lb.getLeaveType().getName(),
                lb.getYear(),
                lb.getTotalAllocated(),
                lb.getUsedDays(),
                lb.getRemainingDays()
        );
    }
}