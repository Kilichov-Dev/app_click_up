package uz.pdp.appclickup.dto;

import lombok.Data;
import uz.pdp.appclickup.entity.enums.WorkspaceRoleName;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
public class WorkspaceRoleDTO {

    @NotNull
    private Long workspaceId;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private WorkspaceRoleName extendsRole;
}
