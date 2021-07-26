package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appclickup.entity.Workspace;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    List<Workspace> findAllByOwnerId(UUID owner_id);

    boolean existsByOwnerIdAndName(UUID owner_id, String name);

    boolean existsByOwnerId(UUID owner_id);

    boolean existsByIdAndOwnerId(Long id, UUID owner_id);
}
