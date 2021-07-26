package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appclickup.entity.Space;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

    List<Space> findAllByWorkspaceIdAndOwnerId(Long workspace_id, UUID owner_id);

    boolean existsByName(String name);

    boolean existsByIdAndWorkspaceIdAndOwnerId(Long id, Long workspace_id, UUID owner_id);

}
