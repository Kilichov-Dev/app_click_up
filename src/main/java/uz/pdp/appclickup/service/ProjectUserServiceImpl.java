package uz.pdp.appclickup.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.ProjectUserDTO;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.entity.ProjectUser;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.repository.ProjectRepository;
import uz.pdp.appclickup.repository.ProjectUserRepository;
import uz.pdp.appclickup.repository.SpaceRepository;
import uz.pdp.appclickup.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectUserServiceImpl implements ProjectUserService {

    final ProjectRepository projectRepository;
    final SpaceRepository spaceRepository;
    final UserRepository userRepository;
    final ProjectUserRepository projectUserRepository;

    public ProjectUserServiceImpl(ProjectRepository projectRepository,
                                  SpaceRepository spaceRepository,
                                  UserRepository userRepository,
                                  ProjectUserRepository projectUserRepository) {
        this.projectRepository = projectRepository;
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
        this.projectUserRepository = projectUserRepository;
    }


    @Override
    public Response addUserToProject(ProjectUserDTO projectUserDTO) {

        ProjectUser projectUser = new ProjectUser(
                projectRepository.findById(projectUserDTO.getProjectId()).orElseThrow(() -> new ResourceNotFoundException("Project")),
                userRepository.findById(projectUserDTO.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User")),
                projectUserDTO.getTaskPermission());

        projectUserRepository.save(projectUser);
        return new Response("User add to project!", true);
    }

    @Override
    public Response editUserProject(Long projectId, UUID newUserId, User currentUser) {

        Optional<ProjectUser> optionalProjectUser = projectUserRepository.findByProjectIdAndCreatedBy(projectId, currentUser.getId());
        optionalProjectUser.ifPresent(projectUser ->
                projectUser.setUser(userRepository.findById(newUserId).orElseThrow(() -> new ResourceNotFoundException("Project"))));

        return new Response("User edited!", true);
    }

    @Override
    public Response deleteUserFromProject(Long projectId, UUID id, User currentUser) {
        Optional<ProjectUser> optionalProjectUser = projectUserRepository.findByProjectIdAndCreatedBy(projectId, currentUser.getId());
        if(optionalProjectUser.isPresent()) {
            projectUserRepository.deleteById(projectId);
            return new Response("User deleted!", true);
        }
        return new Response("Such project user was not found!", false);
    }
}
