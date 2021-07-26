package uz.pdp.appclickup.dto;

import lombok.Data;
import uz.pdp.appclickup.entity.enums.AddType;

import java.util.UUID;

@Data
public class MemberDTO {

    private UUID memberId;

    private UUID roleId;

    private AddType addType;
}
