package springboot.leavemanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.leavemanagementsystem.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
