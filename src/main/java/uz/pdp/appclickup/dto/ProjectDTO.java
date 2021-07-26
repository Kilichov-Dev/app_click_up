package uz.pdp.appclickup.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ProjectDTO {

    @NotNull
    private String name;

    @NotNull
    private Long spaceId;

    @NotNull
    private String accessType;

    private boolean archived;

    @NotNull
    private String color;
}
