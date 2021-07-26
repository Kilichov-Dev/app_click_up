package uz.pdp.appclickup.dto;

import lombok.Data;
import uz.pdp.appclickup.entity.enums.TaskPermission;

import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
public class ProjectUserDTO {

    @NotNull
    private Long projectId;

    @NotNull
    private UUID userId;

    private TaskPermission taskPermission;
}
