package uz.pdp.appclickup.service;

import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.dto.SpaceDTO;
import uz.pdp.appclickup.entity.Space;
import uz.pdp.appclickup.entity.User;

import java.util.List;

public interface SpaceService {

    List<Space> findAll(Long workspaceId, User user);

    Response addSpace(SpaceDTO spaceDTO, User user);

    Response editSpace(Long id, SpaceDTO spaceDTO, User user);

    Response deleteSpace(Long id, Long workspaceId, User user);
}
