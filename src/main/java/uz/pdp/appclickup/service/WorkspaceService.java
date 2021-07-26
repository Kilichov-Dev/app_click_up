package uz.pdp.appclickup.service;

import uz.pdp.appclickup.dto.MemberDTO;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.dto.WorkspaceDto;
import uz.pdp.appclickup.dto.WorkspaceRoleDTO;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.entity.Workspace;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    Response addWorkspace(WorkspaceDto workspaceDto, User currentUser);

    Response editWorkspace(Long id, WorkspaceDto workspaceDto);

    Response changeOwnerWorkspace(Long id, UUID ownerId, User currentUser);

    Response deleteWorkspace(Long id);

    Response addOrEditOrRemoveWorkspace(Long id, MemberDTO memberDto);

    Response joinToWorkspace(Long id, User user);

    List<User> findAllMembersAndGuestsFromWorkspace(Long id, User user);

    List<Workspace> findAllWorkspaces(User user);

    Response addRoleToWorkspace(WorkspaceRoleDTO workspaceRoleDTO, User user);
}
