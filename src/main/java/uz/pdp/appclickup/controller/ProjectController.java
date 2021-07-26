package uz.pdp.appclickup.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.ProjectDTO;
import uz.pdp.appclickup.dto.ProjectUserDTO;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.entity.Project;
import uz.pdp.appclickup.entity.Space;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.security.CurrentUser;
import uz.pdp.appclickup.service.ProjectService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public HttpEntity<?> findAllProject(@RequestParam Long spaceId, @CurrentUser User user) {
        List<Project> projectList = projectService.findAllProject(spaceId, user);
        return ResponseEntity.status(projectList != null ? 200 : 409).body(projectList);
    }

    @PostMapping
    public HttpEntity<?> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
        Response response = projectService.addProject(projectDTO);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO, @CurrentUser User user) {
        Response response = projectService.editProject(id, projectDTO, user);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteProject(@PathVariable Long id, @RequestParam Long spaceId, @CurrentUser User user) {
        Response response = projectService.deleteProject(id, spaceId, user);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }




}
