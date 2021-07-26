package uz.pdp.appclickup.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.dto.SpaceDTO;
import uz.pdp.appclickup.entity.Space;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.security.CurrentUser;
import uz.pdp.appclickup.service.SpaceService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/space")
public class SpaceController {

    final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping
    public HttpEntity<?> findAllSpace(@RequestParam Long workspaceId, @CurrentUser User user) {
        List<Space> spaceList = spaceService.findAll(workspaceId, user);
        return ResponseEntity.status(spaceList != null ? 200 : 409).body(spaceList);
    }

    @PostMapping
    public HttpEntity<?> addSpace(@Valid @RequestBody SpaceDTO spaceDTO, @CurrentUser User user) {
        Response response = spaceService.addSpace(spaceDTO, user);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editSpace(@PathVariable Long id, @RequestBody SpaceDTO spaceDTO, @CurrentUser User user) {
        Response response = spaceService.editSpace(id, spaceDTO, user);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteSpace(@PathVariable Long id, @RequestParam Long workspaceId, @CurrentUser User user) {
        Response response = spaceService.deleteSpace(id, workspaceId, user);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


}
