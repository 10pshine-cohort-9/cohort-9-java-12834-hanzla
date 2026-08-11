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
            description = "Creates a new contact for a specific user."
    )
    @ApiResponse(responseCode = "201", description = "Contact created successfully")
    @PostMapping
    public ResponseEntity<ContactResponse> createContact(
            @Valid @RequestBody ContactRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactService.createContact(request));
    }

    @Operation(
            summary = "Get All Contacts",
            description = "Retrieves all contacts of a user with pagination and sorting."
    )
    @ApiResponse(responseCode = "200", description = "Contacts retrieved successfully")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContactResponse>> getAllContacts(

            @PathVariable Long userId,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "firstName") String sortBy) {

        return ResponseEntity.ok(
                contactService.getAllContacts(
                        userId,
                        page,
                        size,
                        sortBy));
    }

    @Operation(
            summary = "Get Contact By ID",
            description = "Retrieves a contact using its ID."
    )
    @ApiResponse(responseCode = "200", description = "Contact retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                contactService.getContactById(id));
    }

    @Operation(
            summary = "Update Contact",
            description = "Updates an existing contact."
    )
    @ApiResponse(responseCode = "200", description = "Contact updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(

            @PathVariable Long id,

            @Valid @RequestBody ContactRequest request) {

        return ResponseEntity.ok(
                contactService.updateContact(id, request));
    }

    @Operation(
            summary = "Delete Contact",
            description = "Deletes a contact by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Contact deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable Long id) {

        contactService.deleteContact(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search Contacts",
            description = "Searches contacts by first name, last name, email, phone number or title."
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<ContactResponse>> searchContacts(

            @PathVariable Long userId,

            @RequestParam String keyword) {

        return ResponseEntity.ok(
                contactService.searchContacts(userId, keyword));
    }

    @Operation(
            summary = "Toggle Favorite",
            description = "Marks or unmarks a contact as favorite."
    )
    @ApiResponse(responseCode = "200", description = "Favorite status updated successfully")
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ContactResponse> toggleFavorite(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                contactService.toggleFavorite(id));
    }
}