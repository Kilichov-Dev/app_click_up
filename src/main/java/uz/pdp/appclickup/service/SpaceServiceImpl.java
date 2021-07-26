package uz.pdp.appclickup.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.dto.SpaceDTO;
import uz.pdp.appclickup.entity.Space;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.repository.*;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceServiceImpl implements SpaceService {

    final SpaceRepository spaceRepository;
    final IconRepository iconRepository;
    final AttachmentRepository attachmentRepository;
    final WorkspaceRepository workspaceRepository;
    final UserRepository userRepository;

    public SpaceServiceImpl(SpaceRepository spaceRepository,
                            IconRepository iconRepository,
                            AttachmentRepository attachmentRepository,
                            WorkspaceRepository workspaceRepository,
                            UserRepository userRepository) {
        this.spaceRepository = spaceRepository;
        this.iconRepository = iconRepository;
        this.attachmentRepository = attachmentRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Space> findAll(Long workspaceId, User user) {
        return spaceRepository.findAllByWorkspaceIdAndOwnerId(workspaceId, user.getId());
    }

    @Override
    public Response addSpace(SpaceDTO spaceDTO, User user) {

        if (workspaceRepository.existsByIdAndOwnerId(spaceDTO.getWorkspaceId(), user.getId())) {
            if (!spaceRepository.existsByName(spaceDTO.getName())) {
                Space space = new Space(
                        spaceDTO.getName(),
                        spaceDTO.getColor(),
                        spaceDTO.getIconId() == null ? null : iconRepository.findById(spaceDTO.getIconId()).orElseThrow(() -> new ResourceNotFoundException("Icon")),
                        spaceDTO.getAvatarId() == null ? null : attachmentRepository.findById(spaceDTO.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("Attachment")),
                        spaceDTO.getAccessType(),
                        workspaceRepository.findById(spaceDTO.getWorkspaceId()).orElseThrow(() -> new ResourceNotFoundException("Workspace")),
                        user
                );
                spaceRepository.save(space);
                return new Response("Space saved!", true);
            }
            return new Response("Space already exists!", false);

        }
        return new Response("Such workspace was not found!", false);
    }

    @Override
    public Response editSpace(Long id, SpaceDTO spaceDTO, User user) {

        if (spaceRepository.existsByIdAndWorkspaceIdAndOwnerId(id, spaceDTO.getWorkspaceId(), user.getId()))
            return new Response("Such space was not found!", false);

        Optional<Space> optionalSpace = spaceRepository.findById(id);
        optionalSpace.get().setName(spaceDTO.getName());
        optionalSpace.get().setColor(spaceDTO.getColor());
        optionalSpace.get().setIcon(iconRepository.findById(spaceDTO.getIconId()).orElseThrow(() -> new ResourceNotFoundException("Icon")));
        optionalSpace.get().setAvatar(attachmentRepository.findById(spaceDTO.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("Avatar")));
        optionalSpace.get().setAccessType(spaceDTO.getAccessType());
        optionalSpace.get().setOwner(userRepository.findById(spaceDTO.getOwnerId()).orElseThrow(() -> new ResourceNotFoundException("Owner")));

        spaceRepository.save(optionalSpace.get());

        return new Response("Space edited!", true);
    }

    @Override
    public Response deleteSpace(Long id, Long workspaceId, User user) {

        if(spaceRepository.existsByIdAndWorkspaceIdAndOwnerId(id, workspaceId, user.getId()))
            return new Response("Such space was not found!", false);

        spaceRepository.deleteById(id);
        return new Response("Space deleted!", true);
    }
}
