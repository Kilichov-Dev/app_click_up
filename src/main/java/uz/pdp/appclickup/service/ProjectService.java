package uz.pdp.appclickup.service;

import uz.pdp.appclickup.dto.ProjectDTO;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.entity.Project;
import uz.pdp.appclickup.entity.User;

import java.util.List;

public interface ProjectService {


    Response addProject(ProjectDTO projectDTO);

    Response editProject(Long id, ProjectDTO projectDTO, User user);

    Response deleteProject(Long id, Long spaceId, User user);

    List<Project> findAllProject(Long spaceId, User user);

}
