package com.tenpearls.contactmanagementsystem.controller;

import com.tenpearls.contactmanagementsystem.dto.ContactRequest;
import com.tenpearls.contactmanagementsystem.dto.ContactResponse;
import com.tenpearls.contactmanagementsystem.service.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Contact APIs",
        description = "APIs for managing user contacts"
)
@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @Operation(
            summary = "Create Contact",
            description = "Creates a new contact for the authenticated user."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Contact created successfully"
    )
    @PostMapping
    public ResponseEntity<ContactResponse> createContact(
            @Valid @RequestBody ContactRequest request,
            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        contactService.createContact(
                                userId,
                                request
                        )
                );
    }

    @Operation(
            summary = "Get All Contacts",
            description = "Retrieves all contacts of the authenticated user with pagination and sorting."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Contacts retrieved successfully"
    )
    @GetMapping
    public ResponseEntity<?> getAllContacts(

            Authentication authentication,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "firstName") String sortBy) {

        /*
         * Validate pagination parameters.
         */
        if (page < 0) {
            return ResponseEntity.badRequest()
                    .body("Page must be greater than or equal to 0");
        }

        if (size < 1 || size > 100) {
            return ResponseEntity.badRequest()
                    .body("Size must be between 1 and 100");
        }

        /*
         * Only allow known contact fields for sorting.
         * This prevents arbitrary/invalid sort properties from
         * reaching the data-access layer.
         */
        if (!isValidSortField(sortBy)) {
            return ResponseEntity.badRequest()
                    .body(
                            "Invalid sortBy. Allowed values: firstName, lastName, email, phoneNumber"
                    );
        }

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                contactService.getAllContacts(
                        userId,
                        page,
                        size,
                        sortBy
                )
        );
    }

    @Operation(
            summary = "Get Contact By ID",
            description = "Retrieves a contact belonging to the authenticated user."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Contact retrieved successfully"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(

            @PathVariable Long id,

            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                contactService.getContactById(
                        userId,
                        id
                )
        );
    }

    @Operation(
            summary = "Update Contact",
            description = "Updates a contact belonging to the authenticated user."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Contact updated successfully"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(

            @PathVariable Long id,

            @Valid @RequestBody ContactRequest request,

            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                contactService.updateContact(
                        userId,
                        id,
                        request
                )
        );
    }

    @Operation(
            summary = "Delete Contact",
            description = "Deletes a contact belonging to the authenticated user."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Contact deleted successfully"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(

            @PathVariable Long id,

            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        contactService.deleteContact(
                userId,
                id
        );

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search Contacts",
            description = "Searches contacts belonging to the authenticated user."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully"
    )
    @GetMapping("/search")
    public ResponseEntity<List<ContactResponse>> searchContacts(

            @RequestParam String keyword,

            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                contactService.searchContacts(
                        userId,
                        keyword
                )
        );
    }

    @Operation(
            summary = "Toggle Favorite",
            description = "Marks or unmarks a contact as favorite."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Favorite status updated successfully"
    )
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ContactResponse> toggleFavorite(

            @PathVariable Long id,

            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                contactService.toggleFavorite(
                        userId,
                        id
                )
        );
    }

    private boolean isValidSortField(String sortBy) {

        return sortBy.equals("firstName")
                || sortBy.equals("lastName")
                || sortBy.equals("email")
                || sortBy.equals("phoneNumber");
    }

    private Long getAuthenticatedUserId(
            Authentication authentication) {

        return (Long) authentication.getPrincipal();
    }
}