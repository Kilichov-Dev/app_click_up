package uz.pdp.appclickup.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.ProjectUserDTO;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.security.CurrentUser;
import uz.pdp.appclickup.service.ProjectService;
import uz.pdp.appclickup.service.ProjectUserService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/project/projectUser")
public class ProjectUserController {

    final ProjectUserService projectUserService;

    public ProjectUserController(ProjectUserService projectUserService) {
        this.projectUserService = projectUserService;
    }

    // Folderga user qo'shish
    @PostMapping("/addUser")
    public HttpEntity<?> addUserToProject(@Valid @RequestBody ProjectUserDTO projectUserDTO) {
        Response response = projectUserService.addUserToProject(projectUserDTO);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    // Folderdagi userni edit qilish
    @PostMapping("/{id}")
    public HttpEntity<?> editUserProject(@PathVariable(name = "id") Long projectId, @RequestParam UUID newUserId, @CurrentUser User currentUser) {
        Response response = projectUserService.editUserProject(projectId, newUserId, currentUser);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    // Folder dan user ni o'chirish
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteUserFromProject(@PathVariable(name = "id") Long projectId, @RequestParam User user, @CurrentUser User currentUser) {
        Response response = projectUserService.deleteUserFromProject(projectId, user.getId(), currentUser);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

}
