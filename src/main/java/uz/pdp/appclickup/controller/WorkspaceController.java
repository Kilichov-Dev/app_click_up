package uz.pdp.appclickup.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.MemberDTO;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.dto.WorkspaceDto;
import uz.pdp.appclickup.dto.WorkspaceRoleDTO;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.entity.Workspace;
import uz.pdp.appclickup.security.CurrentUser;
import uz.pdp.appclickup.service.WorkspaceService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {

    final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public HttpEntity<?> addWorkspace(@Valid @RequestBody WorkspaceDto workspaceDto, @CurrentUser User currentUser) {
        Response response = workspaceService.addWorkspace(workspaceDto, currentUser);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    // Workspace edit qilish
    @PutMapping("/{id}")
    public HttpEntity<?> editWorkspace(@PathVariable Long id, @RequestBody WorkspaceDto workspaceDto) {
        Response response = workspaceService.editWorkspace(id, workspaceDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    // OWNER ini o'zgartish
    @PutMapping("/changeOwner/{id}")
    public HttpEntity<?> changeWorkspaceOwner(@PathVariable Long id,
                                              @RequestParam UUID ownerId, @CurrentUser User currentUser) {
        Response response = workspaceService.changeOwnerWorkspace(id, ownerId, currentUser);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    // member va mehmonlarini ko'rish
    @GetMapping("/memberAndGuests/{id}")
    public HttpEntity<?> findAllMembersAndGuestsFromWorkspace(@PathVariable Long id, @CurrentUser User user) {
        List<User> userList = workspaceService.findAllMembersAndGuestsFromWorkspace(id, user);
        return ResponseEntity.ok(userList);
    }

    // Workspacelari ro'yxatini olish
    @GetMapping
    public HttpEntity<?> findAllWorkspaces(@CurrentUser User user) {
        List<Workspace> workspaceList = workspaceService.findAllWorkspaces(user);
        return ResponseEntity.ok(workspaceList);
    }

    // Workspace ga role qo'shish
    @PostMapping("/addRole")
    public HttpEntity<?> addRoleToWorkspace(@RequestBody WorkspaceRoleDTO workspaceRoleDTO,
                                            @CurrentUser User user) {
        Response response = workspaceService.addRoleToWorkspace(workspaceRoleDTO, user);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    // Workspace ga join bo'lish
    @PutMapping("/join")
    public HttpEntity<?> joinToWorkspace(@RequestParam(name = "workspaceId") Long id,
                                         @CurrentUser User user) {
        Response response = workspaceService.joinToWorkspace(id, user);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    // Workspace ni delete qilish
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteWorkspace(@PathVariable Long id) {
        Response response = workspaceService.deleteWorkspace(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PostMapping("/addOrEditOrRemove/{id}")
    public HttpEntity<?> addOrEditOrRemoveWorkspace(@PathVariable Long id,
                                                    @RequestBody MemberDTO memberDto) {
        Response response = workspaceService.addOrEditOrRemoveWorkspace(id, memberDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


}
