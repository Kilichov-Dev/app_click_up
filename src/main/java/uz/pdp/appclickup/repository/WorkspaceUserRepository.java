package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.entity.WorkspaceUser;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceUserRepository extends JpaRepository<WorkspaceUser, UUID> {

    Optional<WorkspaceUser> findByWorkspaceIdAndMemberId(Long workspace_id, UUID member_id);

    List<WorkspaceUser> findAllByWorkspaceId(Long workspace_id);

    @Transactional
    @Modifying
    void deleteByWorkspaceIdAndMemberId(Long workspace_id, UUID member_id);
}
