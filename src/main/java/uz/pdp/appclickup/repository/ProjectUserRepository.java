package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appclickup.entity.ProjectUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

    Optional<ProjectUser> findByProjectIdAndCreatedBy(Long project_id, UUID createdBy);

}
