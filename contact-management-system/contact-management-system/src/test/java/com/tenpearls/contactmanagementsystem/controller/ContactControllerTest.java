package com.tenpearls.contactmanagementsystem.controller;

import com.tenpearls.contactmanagementsystem.dto.ContactRequest;
import com.tenpearls.contactmanagementsystem.dto.ContactResponse;
import com.tenpearls.contactmanagementsystem.service.ContactService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ContactController controller;

    private ContactResponse createResponse() {
        return ContactResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .title("Developer")
                .email("john@example.com")
                .phoneNumber("123456789")
                .favorite(false)
                .build();
    }

    @Test
    void shouldCreateContact() {

        ContactRequest request = new ContactRequest();
        ContactResponse response = createResponse();

        when(authentication.getPrincipal()).thenReturn(1L);
        when(contactService.createContact(1L, request))
                .thenReturn(response);

        ResponseEntity<ContactResponse> result =
                controller.createContact(request, authentication);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());

        verify(contactService).createContact(1L, request);
    }

    @Test
    void shouldGetAllContacts() {

        List<ContactResponse> contacts =
                Collections.singletonList(createResponse());

        when(authentication.getPrincipal()).thenReturn(1L);
        when(contactService.getAllContacts(
                1L, 0, 5, "firstName"))
                .thenReturn(contacts);

        ResponseEntity<Object> result =
                controller.getAllContacts(
                        authentication,
                        0,
                        5,
                        "firstName");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(contacts, result.getBody());

        verify(contactService)
                .getAllContacts(1L, 0, 5, "firstName");
    }

    @Test
    void shouldRejectNegativePage() {

        ResponseEntity<Object> result =
                controller.getAllContacts(
                        authentication,
                        -1,
                        5,
                        "firstName");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(
                "Page must be greater than or equal to 0",
                result.getBody());

        verifyNoInteractions(contactService);
    }

    @Test
    void shouldRejectInvalidSizeBelowOne() {

        ResponseEntity<Object> result =
                controller.getAllContacts(
                        authentication,
                        0,
                        0,
                        "firstName");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(
                "Size must be between 1 and 100",
                result.getBody());

        verifyNoInteractions(contactService);
    }

    @Test
    void shouldRejectInvalidSizeAbove100() {

        ResponseEntity<Object> result =
                controller.getAllContacts(
                        authentication,
                        0,
                        101,
                        "firstName");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(
                "Size must be between 1 and 100",
                result.getBody());

        verifyNoInteractions(contactService);
    }

    @Test
    void shouldRejectInvalidSortField() {

        ResponseEntity<Object> result =
                controller.getAllContacts(
                        authentication,
                        0,
                        5,
                        "invalid");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        verifyNoInteractions(contactService);
    }

    @Test
    void shouldGetContactById() {

        ContactResponse response = createResponse();

        when(authentication.getPrincipal()).thenReturn(1L);
        when(contactService.getContactById(1L, 10L))
                .thenReturn(response);

        ResponseEntity<ContactResponse> result =
                controller.getContactById(
                        10L,
                        authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldUpdateContact() {

        ContactRequest request = new ContactRequest();
        ContactResponse response = createResponse();

        when(authentication.getPrincipal()).thenReturn(1L);
        when(contactService.updateContact(
                1L,
                10L,
                request))
                .thenReturn(response);

        ResponseEntity<ContactResponse> result =
                controller.updateContact(
                        10L,
                        request,
                        authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldDeleteContact() {

        when(authentication.getPrincipal()).thenReturn(1L);

        ResponseEntity<Void> result =
                controller.deleteContact(
                        10L,
                        authentication);

        assertEquals(
                HttpStatus.NO_CONTENT,
                result.getStatusCode());

        verify(contactService)
                .deleteContact(1L, 10L);
    }

    @Test
    void shouldSearchContacts() {

        List<ContactResponse> responses =
                Collections.singletonList(createResponse());

        when(authentication.getPrincipal()).thenReturn(1L);
        when(contactService.searchContacts(
                1L,
                "john"))
                .thenReturn(responses);

        ResponseEntity<List<ContactResponse>> result =
                controller.searchContacts(
                        "john",
                        authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responses, result.getBody());
    }

    @Test
    void shouldToggleFavorite() {

        ContactResponse response = createResponse();

        when(authentication.getPrincipal()).thenReturn(1L);
        when(contactService.toggleFavorite(
                1L,
                10L))
                .thenReturn(response);

        ResponseEntity<ContactResponse> result =
                controller.toggleFavorite(
                        10L,
                        authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}