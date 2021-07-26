package uz.pdp.appclickup.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SpaceDTO {

    @NotNull
    private String name;

    @NotNull
    private String color;

    private UUID iconId;

    private UUID avatarId;

    @NotNull
    private String accessType;

    @NotNull
    private Long workspaceId;

    @NotNull
    private UUID ownerId;           // Space ni owner ini ham o'zgartirish mumkin
}
