package uz.pdp.appclickup.service;

import uz.pdp.appclickup.dto.ProjectUserDTO;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.entity.User;

import java.util.UUID;

public interface ProjectUserService {

    Response addUserToProject(ProjectUserDTO projectUserDTO);

    Response editUserProject(Long projectId, UUID id, User currentUser);

    Response deleteUserFromProject(Long projectId, UUID id, User currentUser);
}
