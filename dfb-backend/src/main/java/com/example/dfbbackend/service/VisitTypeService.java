package com.example.dfbbackend.service;

import com.example.dfbbackend.dto.VisitTypeDeleteInfoResponse;
import com.example.dfbbackend.entity.Domain;
import com.example.dfbbackend.entity.FormSubmission;
import com.example.dfbbackend.entity.User;
import com.example.dfbbackend.entity.VisitType;
import com.example.dfbbackend.repository.DomainRepository;
import com.example.dfbbackend.repository.FormFieldVisitTypeRepository;
import com.example.dfbbackend.repository.FormSubmissionRepository;
import com.example.dfbbackend.repository.UserRepository;
import com.example.dfbbackend.repository.VisitTypeRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VisitTypeService {

    private final VisitTypeRepository visitTypeRepository;

    private final DomainRepository domainRepository;

    private final UserRepository userRepository;

    private final FormFieldVisitTypeRepository
            formFieldVisitTypeRepository;

    private final FormSubmissionRepository
            formSubmissionRepository;

    private final NotificationService notificationService;

    public VisitTypeService(
            VisitTypeRepository visitTypeRepository,
            DomainRepository domainRepository,
            UserRepository userRepository,
            FormFieldVisitTypeRepository
                    formFieldVisitTypeRepository,
            FormSubmissionRepository
                    formSubmissionRepository,
            NotificationService notificationService
    ) {

        this.visitTypeRepository =
                visitTypeRepository;

        this.domainRepository =
                domainRepository;

        this.userRepository =
                userRepository;

        this.formFieldVisitTypeRepository =
                formFieldVisitTypeRepository;

        this.formSubmissionRepository =
                formSubmissionRepository;

        this.notificationService =
                notificationService;
    }

    // =====================================================
    // CREATE VISIT TYPE
    // =====================================================

    public VisitType createVisitType(
            Long domainId,
            String name
    ) {

        validateName(name);

        Domain domain =
                domainRepository
                        .findById(domainId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Domain not found with id: "
                                                + domainId
                                )
                        );

        if (visitTypeRepository
                .existsByDomainIdAndName(
                        domainId,
                        name.trim()
                )) {

            throw new RuntimeException(
                    "Visit type already exists in this domain: "
                            + name.trim()
            );
        }

        VisitType visitType =
                new VisitType();

        visitType.setDomain(domain);

        visitType.setName(
                name.trim()
        );

        visitType.setActive(true);

        LocalDateTime now =
                LocalDateTime.now();

        visitType.setCreatedAt(now);

        visitType.setUpdatedAt(now);

        VisitType savedVisitType =
                visitTypeRepository.save(
                        visitType
                );

        // =================================================
        // NOTIFY DOMAIN USERS
        // =================================================

        User admin =
                getAuthenticatedUser();

        notificationService.notifyDomainUsers(
                domain.getId(),
                admin,
                "VISIT_TYPE_CREATED",
                "New Visit Type Added",
                "A new visit type '"
                        + savedVisitType.getName()
                        + "' was added to your domain.",
                "VISIT_TYPE",
                savedVisitType.getId()
        );

        return savedVisitType;
    }

    // =====================================================
    // GET VISIT TYPES BY DOMAIN
    // =====================================================

    @Transactional(readOnly = true)
    public List<VisitType> getVisitTypesByDomain(
            Long domainId
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new AccessDeniedException(
                    "Authentication required"
            );
        }

        boolean isAdmin =
                isAdmin(authentication);

        // =================================================
        // ADMIN
        // =================================================

        if (isAdmin) {

            return visitTypeRepository
                    .findByDomainId(domainId);
        }

        // =================================================
        // USER
        // =================================================

        User user =
                getAuthenticatedUser(
                        authentication
                );

        if (user.getDomain() == null ||
                !user.getDomain()
                        .getId()
                        .equals(domainId)) {

            throw new AccessDeniedException(
                    "You do not have access to this domain"
            );
        }

        // =================================================
        // USER DOMAIN MUST BE ACTIVE
        // =================================================

        if (!Boolean.TRUE.equals(
                user.getDomain().getActive()
        )) {

            throw new AccessDeniedException(
                    "User's domain is inactive"
            );
        }

        // =================================================
        // USER GETS ONLY ACTIVE VISIT TYPES
        // =================================================

        return visitTypeRepository
                .findByDomainIdAndActiveTrue(
                        domainId
                );
    }

    // =====================================================
    // GET ONE VISIT TYPE
    // =====================================================

    @Transactional(readOnly = true)
    public VisitType getVisitTypeById(
            Long id
    ) {

        VisitType visitType =
                visitTypeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Visit type not found with id: "
                                                + id
                                )
                        );

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new AccessDeniedException(
                    "Authentication required"
            );
        }

        boolean isAdmin =
                isAdmin(authentication);

        // =================================================
        // ADMIN CAN ACCESS ANY VISIT TYPE
        // =================================================

        if (isAdmin) {

            return visitType;
        }

        // =================================================
        // USER
        // =================================================

        User user =
                getAuthenticatedUser(
                        authentication
                );

        if (user.getDomain() == null ||
                !user.getDomain()
                        .getId()
                        .equals(
                                visitType
                                        .getDomain()
                                        .getId()
                        )) {

            throw new AccessDeniedException(
                    "You do not have access to this visit type"
            );
        }

        // =================================================
        // USER DOMAIN MUST BE ACTIVE
        // =================================================

        if (!Boolean.TRUE.equals(
                user.getDomain().getActive()
        )) {

            throw new AccessDeniedException(
                    "User's domain is inactive"
            );
        }

        // =================================================
        // USER CAN ONLY ACCESS ACTIVE VISIT TYPE
        // =================================================

        if (!Boolean.TRUE.equals(
                visitType.getActive()
        )) {

            throw new AccessDeniedException(
                    "Visit type is inactive"
            );
        }

        return visitType;
    }

    // =====================================================
    // UPDATE VISIT TYPE
    // =====================================================

    public VisitType updateVisitType(
            Long id,
            String name
    ) {

        validateName(name);

        VisitType visitType =
                visitTypeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Visit type not found with id: "
                                                + id
                                )
                        );

        Long domainId =
                visitType
                        .getDomain()
                        .getId();

        String updatedName =
                name.trim();

        // =================================================
        // PREVENT DUPLICATE NAME IN SAME DOMAIN
        // =================================================

        if (visitTypeRepository
                .existsByDomainIdAndNameAndIdNot(
                        domainId,
                        updatedName,
                        id
                )) {

            throw new RuntimeException(
                    "Visit type already exists in this domain: "
                            + updatedName
            );
        }

        // =================================================
        // UPDATE NAME ONLY
        // =================================================

        visitType.setName(
                updatedName
        );

        visitType.setUpdatedAt(
                LocalDateTime.now()
        );

        VisitType savedVisitType =
                visitTypeRepository.save(
                        visitType
                );

        // =================================================
        // NOTIFY DOMAIN USERS
        // =================================================

        User admin =
                getAuthenticatedUser();

        notificationService.notifyDomainUsers(
                domainId,
                admin,
                "VISIT_TYPE_UPDATED",
                "Visit Type Updated",
                "The visit type '"
                        + savedVisitType.getName()
                        + "' was updated by an administrator.",
                "VISIT_TYPE",
                savedVisitType.getId()
        );

        return savedVisitType;
    }

    // =====================================================
    // DEACTIVATE VISIT TYPE
    // =====================================================

    public VisitType deactivateVisitType(
            Long id
    ) {

        VisitType visitType =
                visitTypeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Visit type not found with id: "
                                                + id
                                )
                        );

        if (!Boolean.TRUE.equals(
                visitType.getActive()
        )) {

            return visitType;
        }

        visitType.setActive(false);

        visitType.setUpdatedAt(
                LocalDateTime.now()
        );

        VisitType savedVisitType =
                visitTypeRepository.save(
                        visitType
                );

        // =================================================
        // NOTIFY DOMAIN USERS
        // =================================================

        User admin =
                getAuthenticatedUser();

        notificationService.notifyDomainUsers(
                visitType.getDomain().getId(),
                admin,
                "VISIT_TYPE_DEACTIVATED",
                "Visit Type Deactivated",
                "The visit type '"
                        + savedVisitType.getName()
                        + "' has been deactivated and is no longer available.",
                "VISIT_TYPE",
                savedVisitType.getId()
        );

        return savedVisitType;
    }

    // =====================================================
    // ACTIVATE VISIT TYPE
    // =====================================================

    public VisitType activateVisitType(
            Long id
    ) {

        VisitType visitType =
                visitTypeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Visit type not found with id: "
                                                + id
                                )
                        );

        if (Boolean.TRUE.equals(
                visitType.getActive()
        )) {

            return visitType;
        }

        visitType.setActive(true);

        visitType.setUpdatedAt(
                LocalDateTime.now()
        );

        VisitType savedVisitType =
                visitTypeRepository.save(
                        visitType
                );

        // =================================================
        // NOTIFY DOMAIN USERS
        // =================================================

        User admin =
                getAuthenticatedUser();

        notificationService.notifyDomainUsers(
                visitType.getDomain().getId(),
                admin,
                "VISIT_TYPE_ACTIVATED",
                "Visit Type Activated",
                "The visit type '"
                        + savedVisitType.getName()
                        + "' is now active and available.",
                "VISIT_TYPE",
                savedVisitType.getId()
        );

        return savedVisitType;
    }

    // =====================================================
    // GET DELETE INFORMATION
    // =====================================================
    //
    // This is called BEFORE permanent deletion.
    //
    // It tells the Admin:
    //
    // - Visit Type ID
    // - Visit Type name
    // - Number of submissions
    // - Whether submissions exist
    //
    // No data is deleted here.
    // =====================================================

    @Transactional(readOnly = true)
    public VisitTypeDeleteInfoResponse getDeleteInfo(
            Long id
    ) {

        VisitType visitType =
                visitTypeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Visit type not found with id: "
                                                + id
                                )
                        );

        long submissionCount =
                formSubmissionRepository
                        .countByVisitType_Id(id);

        return new VisitTypeDeleteInfoResponse(
                visitType.getId(),
                visitType.getName(),
                submissionCount,
                submissionCount > 0
        );
    }

    // =====================================================
    // PERMANENT DELETE VISIT TYPE
    // =====================================================
    //
    // Permanently deletes:
    //
    // 1. Form submissions
    // 2. Submission values through JPA cascade
    // 3. Form-field / visit-type mappings
    // 4. Visit Type
    //
    // This cannot be undone.
    // =====================================================

    public long deleteVisitType(
            Long id
    ) {

        VisitType visitType =
                visitTypeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Visit type not found with id: "
                                                + id
                                )
                        );

        // =================================================
        // CAPTURE INFORMATION BEFORE DELETE
        // =================================================

        Long domainId =
                visitType.getDomain() != null
                        ? visitType.getDomain().getId()
                        : null;

        String visitTypeName =
                visitType.getName();

        // =================================================
        // FIND RELATED SUBMISSIONS
        // =================================================

        List<FormSubmission> submissions =
                formSubmissionRepository
                        .findByVisitType_Id(id);

        long submissionCount =
                submissions.size();

        // =================================================
        // DELETE SUBMISSIONS
        // =================================================

        if (!submissions.isEmpty()) {

            formSubmissionRepository
                    .deleteAll(submissions);

            formSubmissionRepository.flush();
        }

        // =================================================
        // DELETE FORM FIELD / VISIT TYPE MAPPINGS
        // =================================================

        formFieldVisitTypeRepository
                .deleteByVisitType_Id(id);

        // =================================================
        // DELETE VISIT TYPE
        // =================================================

        visitTypeRepository.delete(
                visitType
        );

        visitTypeRepository.flush();

        // =================================================
        // NOTIFY DOMAIN USERS
        // =================================================

        if (domainId != null) {

            User admin =
                    getAuthenticatedUser();

            notificationService.notifyDomainUsers(
                    domainId,
                    admin,
                    "VISIT_TYPE_DELETED",
                    "Visit Type Deleted",
                    "The visit type '"
                            + visitTypeName
                            + "' was deleted by an administrator.",
                    "VISIT_TYPE",
                    id
            );
        }

        // =================================================
        // RETURN DELETED SUBMISSION COUNT
        // =================================================

        return submissionCount;
    }

    // =====================================================
    // CHECK ADMIN
    // =====================================================

    private boolean isAdmin(
            Authentication authentication
    ) {

        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority
                                .getAuthority()
                                .equals("ROLE_ADMIN")
                );
    }

    // =====================================================
    // GET AUTHENTICATED USER
    // =====================================================

    private User getAuthenticatedUser(
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }

    // =====================================================
    // GET CURRENT AUTHENTICATED USER
    // =====================================================

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new AccessDeniedException(
                    "Authentication required"
            );
        }

        return getAuthenticatedUser(
                authentication
        );
    }

    // =====================================================
    // VALIDATE NAME
    // =====================================================

    private void validateName(
            String name
    ) {

        if (name == null ||
                name.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Visit type name is required"
            );
        }

        if (name.trim().length() > 150) {

            throw new IllegalArgumentException(
                    "Visit type name cannot exceed 150 characters"
            );
        }
    }
}