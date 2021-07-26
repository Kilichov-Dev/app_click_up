package uz.pdp.appclickup.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.MemberDTO;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.dto.WorkspaceDto;
import uz.pdp.appclickup.dto.WorkspaceRoleDTO;
import uz.pdp.appclickup.entity.*;
import uz.pdp.appclickup.entity.enums.WorkspacePermissionName;
import uz.pdp.appclickup.entity.enums.WorkspaceRoleName;
import uz.pdp.appclickup.repository.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    final WorkspaceRepository workspaceRepository;
    final AttachmentRepository attachmentRepository;
    final UserRepository userRepository;
    final WorkspaceUserRepository workspaceUserRepository;
    final WorkspaceRoleRepository workspaceRoleRepository;
    final WorkspacePermissionRepository workspacePermissionRepository;
    final JavaMailSender mailSender;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository,
                                AttachmentRepository attachmentRepository,
                                UserRepository userRepository,
                                WorkspaceUserRepository workspaceUserRepository,
                                WorkspaceRoleRepository workspaceRoleRepository,
                                WorkspacePermissionRepository workspacePermissionRepository,
                                JavaMailSender mailSender) {
        this.workspaceRepository = workspaceRepository;
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
        this.workspaceUserRepository = workspaceUserRepository;
        this.workspaceRoleRepository = workspaceRoleRepository;
        this.workspacePermissionRepository = workspacePermissionRepository;
        this.mailSender = mailSender;
    }

    @Override
    public Response addWorkspace(WorkspaceDto workspaceDto, User currentUser) {

        // WORKSPACE OCHDIK
        if (workspaceRepository.existsByOwnerIdAndName(currentUser.getId(), workspaceDto.getName()))
            return new Response("You have already exists such named workspace!", false);
        Workspace workspace = new Workspace(
                workspaceDto.getName(),
                workspaceDto.getColor(),
                currentUser,
                workspaceDto.getAvatarId() == null ? null : attachmentRepository.findById(workspaceDto.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("Attachment"))
        );
        workspaceRepository.save(workspace);

        // WORKSPACE ROLE OCHDIK
        WorkspaceRole ownerRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_OWNER.name(), null));
        WorkspaceRole adminRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_ADMIN.name(), null));
        WorkspaceRole memberRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_MEMBER.name(), null));
        WorkspaceRole guestRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_GUEST.name(), null));

        // OWNER, ADMIN, MEMBER, GUEST larga PERMISSION larni beryapmiz
        WorkspacePermissionName[] values = WorkspacePermissionName.values();
        List<WorkspacePermission> workspacePermissionList = new ArrayList<>();
        for (WorkspacePermissionName permissionName : values) {

            workspacePermissionList.add(new WorkspacePermission(ownerRole, permissionName));

            if (permissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)) {
                workspacePermissionList.add(new WorkspacePermission(adminRole, permissionName));
            }

            if (permissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_MEMBER)) {
                workspacePermissionList.add(new WorkspacePermission(memberRole, permissionName));
            }

            if (permissionName.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_GUEST)) {
                workspacePermissionList.add(new WorkspacePermission(guestRole, permissionName));
            }

        }
        workspacePermissionRepository.saveAll(workspacePermissionList);

        // WORKSPACE USER OCHDIK
        workspaceUserRepository.save(new WorkspaceUser(
                workspace,
                currentUser,
                ownerRole,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()))
        );

        return new Response("Workspace saved!", true);
    }

    /**
     * NAME, COLOR, AVATAR o'zgarishi mumkin
     *
     * @param id
     * @param workspaceDto
     * @return
     */
    @Override
    public Response editWorkspace(Long id, WorkspaceDto workspaceDto) {
        try {

            Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
            if (!optionalWorkspace.isPresent())
                return new Response("Such workspace was not found!", false);

            Optional<Attachment> optionalAttachment = attachmentRepository.findById(workspaceDto.getAvatarId());
            if (!optionalAttachment.isPresent())
                return new Response("Such avatar was not found!", false);

            optionalWorkspace.get().setName(workspaceDto.getName());
            optionalWorkspace.get().setColor(workspaceDto.getColor());
            optionalWorkspace.get().setAvatar(optionalAttachment.get());

            workspaceRepository.save(optionalWorkspace.get());
            return new Response("Workspace edited!", true);

        } catch (Exception exception) {
            return new Response("Error!", false);
        }
    }

    /**
     * @param id
     * @param ownerId
     * @param currentUser
     * @return
     */
    @Override
    public Response changeOwnerWorkspace(Long id, UUID ownerId, User currentUser) {

        if (workspaceRepository.existsByIdAndOwnerId(id, currentUser.getId()))
            return new Response("Such workspace was not found!", false);

        Optional<User> optionalUser = userRepository.findById(ownerId);
        if (!optionalUser.isPresent())
            return new Response("Such owner id was not found!", false);

        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
        optionalWorkspace.get().setOwner(optionalUser.get());
        workspaceRepository.save(optionalWorkspace.get());
        return new Response("Workspace owner changed!", true);
    }

    /**
     * ISHXONANI O'CHIRISH
     *
     * @param id
     * @return
     */
    @Override
    public Response deleteWorkspace(Long id) {
        try {
            workspaceRepository.deleteById(id);
            return new Response("Workspace deleted! Id:" + id, true);
        } catch (Exception exception) {
            return new Response("Error!", false);
        }
    }

    @Override
    public Response addOrEditOrRemoveWorkspace(Long id, MemberDTO memberDto) {

        switch (memberDto.getAddType()) {
            case ADD:
                WorkspaceUser workspaceUser = new WorkspaceUser(
                        workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id")),
                        userRepository.findById(memberDto.getMemberId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                        workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                        new Timestamp(System.currentTimeMillis()),
                        null
                );
                workspaceUserRepository.save(workspaceUser);

                // TODO EMAIL ga invite message jo'natish
                Optional<User> optionalMember = userRepository.findById(memberDto.getMemberId());
                optionalMember.ifPresent(user -> sendEmail(workspaceUser.getWorkspace(), user.getEmail()));

                break;
            case EDIT:
                WorkspaceUser workspaceUser1 = workspaceUserRepository.findByWorkspaceIdAndMemberId(id, memberDto.getMemberId()).orElseGet(WorkspaceUser::new);
                workspaceUser1.setWorkspaceRole(workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("id")));
                workspaceUserRepository.save(workspaceUser1);
                break;
            case REMOVE:
                workspaceUserRepository.deleteByWorkspaceIdAndMemberId(id, memberDto.getMemberId());
                break;
        }

        return new Response("Successful!", true);
    }

    public boolean sendEmail(Workspace workspace, String sendingEmail) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("kilichov190394@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("You have a invitation by " + workspace.getName() + ". Do you join it?");
            mailMessage.setText("<a href='http://localhost:8080/api/workspace/join?workspaceId=" + workspace.getId() + ">Confirm</a>");

            mailSender.send(mailMessage);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public Response joinToWorkspace(Long id, User user) {
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByWorkspaceIdAndMemberId(id, user.getId());
        if (optionalWorkspaceUser.isPresent()) {
            optionalWorkspaceUser.get().setDateJoined(new Timestamp(System.currentTimeMillis()));
            workspaceUserRepository.save(optionalWorkspaceUser.get());
            return new Response("Successfully joined!", true);
        }
        return new Response("Error!", false);
    }

    @Override
    public List<User> findAllMembersAndGuestsFromWorkspace(Long workspace_id, User user) {
        if (workspaceRepository.existsByIdAndOwnerId(workspace_id, user.getId())) {
            List<WorkspaceUser> workspaceUserList = workspaceUserRepository.findAllByWorkspaceId(workspace_id);

            List<User> userList = new ArrayList<>();
            for (WorkspaceUser workspaceUser : workspaceUserList) {
                userList.add(workspaceUser.getMember());
            }
            return userList;
        }
        return null;
    }

    @Override
    public List<Workspace> findAllWorkspaces(User user) {
        if (workspaceRepository.existsByOwnerId(user.getId())) {
            List<Workspace> workspaceList = workspaceRepository.findAllByOwnerId(user.getId());
            return workspaceList;
        }
        return null;
    }

    @Override
    public Response addRoleToWorkspace(WorkspaceRoleDTO workspaceRoleDTO, User user) {

        if (workspaceRepository.existsByIdAndOwnerId(workspaceRoleDTO.getWorkspaceId(), user.getId())) {
            WorkspaceRole workspaceRole = new WorkspaceRole(
                    workspaceRepository.findById(workspaceRoleDTO.getWorkspaceId()).orElseThrow(() -> new ResourceNotFoundException("id")),
                    workspaceRoleDTO.getName(),
                    workspaceRoleDTO.getExtendsRole()
            );

            workspaceRoleRepository.save(workspaceRole);
            return new Response("Role saved!", true);
        }

        return new Response("Error!", false);
    }
}
