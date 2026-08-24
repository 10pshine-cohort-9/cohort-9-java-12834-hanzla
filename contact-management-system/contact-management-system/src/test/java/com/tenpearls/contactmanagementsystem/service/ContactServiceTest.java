package com.tenpearls.contactmanagementsystem.service;

import com.tenpearls.contactmanagementsystem.dto.ContactRequest;
import com.tenpearls.contactmanagementsystem.dto.ContactResponse;
import com.tenpearls.contactmanagementsystem.entity.Contact;
import com.tenpearls.contactmanagementsystem.entity.User;
import com.tenpearls.contactmanagementsystem.exception.ContactNotFoundException;
import com.tenpearls.contactmanagementsystem.exception.UserNotFoundException;
import com.tenpearls.contactmanagementsystem.repository.ContactRepository;
import com.tenpearls.contactmanagementsystem.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactService contactService;

    private User user;
    private Contact contact;
    private ContactRequest request;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .firstName("Hanzla")
                .lastName("Shehzad")
                .email("hanzla@gmail.com")
                .phoneNumber("03001234567")
                .password("123456")
                .build();

        contact = Contact.builder()
                .id(1L)
                .firstName("Ali")
                .lastName("Khan")
                .title("Developer")
                .email("ali@gmail.com")
                .phoneNumber("03009999999")
                .favorite(false)
                .user(user)
                .build();

        request = new ContactRequest();

        request.setFirstName("Ali");
        request.setLastName("Khan");
        request.setTitle("Developer");
        request.setEmail("ali@gmail.com");
        request.setPhoneNumber("03009999999");
        request.setFavorite(false);
    }

    @Test
    void createContact_ShouldCreateSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(contactRepository.save(any(Contact.class)))
                .thenReturn(contact);

        ContactResponse response =
                contactService.createContact(1L, request);

        assertNotNull(response);

        assertEquals(
                "Ali",
                response.getFirstName()
        );

        assertEquals(
                "Khan",
                response.getLastName()
        );

        verify(userRepository)
                .findById(1L);

        verify(contactRepository)
                .save(any(Contact.class));
    }

    @Test
    void createContact_ShouldThrowUserNotFoundException() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> contactService.createContact(1L, request)
        );

        verify(contactRepository, never())
                .save(any(Contact.class));
    }

    @Test
    void getContactById_ShouldReturnContactForOwner() {

        when(contactRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(contact));

        ContactResponse response =
                contactService.getContactById(1L, 1L);

        assertNotNull(response);

        assertEquals(
                "Ali",
                response.getFirstName()
        );

        verify(contactRepository)
                .findByIdAndUserId(1L, 1L);
    }

    @Test
    void getContactById_ShouldThrowExceptionWhenContactDoesNotBelongToUser() {

        when(contactRepository.findByIdAndUserId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ContactNotFoundException.class,
                () -> contactService.getContactById(2L, 1L)
        );
    }

    @Test
    void getAllContacts_ShouldReturnContactsForUser() {

        Page<Contact> page =
                new PageImpl<>(List.of(contact));

        when(contactRepository.findByUserId(
                eq(1L),
                any(PageRequest.class)
        )).thenReturn(page);

        List<ContactResponse> responses =
                contactService.getAllContacts(
                        1L,
                        0,
                        5,
                        "firstName"
                );

        assertNotNull(responses);

        assertEquals(
                1,
                responses.size()
        );

        assertEquals(
                "Ali",
                responses.get(0).getFirstName()
        );

        verify(contactRepository)
                .findByUserId(
                        eq(1L),
                        any(PageRequest.class)
                );
    }

    @Test
    void updateContact_ShouldUpdateSuccessfully() {

        when(contactRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(contact));

        when(contactRepository.save(any(Contact.class)))
                .thenReturn(contact);

        ContactResponse response =
                contactService.updateContact(
                        1L,
                        1L,
                        request
                );

        assertNotNull(response);

        assertEquals(
                "Ali",
                response.getFirstName()
        );

        verify(contactRepository)
                .findByIdAndUserId(1L, 1L);

        verify(contactRepository)
                .save(any(Contact.class));
    }

    @Test
    void updateContact_ShouldThrowExceptionWhenContactDoesNotBelongToUser() {

        when(contactRepository.findByIdAndUserId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ContactNotFoundException.class,
                () -> contactService.updateContact(
                        2L,
                        1L,
                        request
                )
        );

        verify(contactRepository, never())
                .save(any(Contact.class));
    }

    @Test
    void deleteContact_ShouldDeleteSuccessfully() {

        when(contactRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(contact));

        contactService.deleteContact(1L, 1L);

        verify(contactRepository)
                .findByIdAndUserId(1L, 1L);

        verify(contactRepository)
                .delete(contact);
    }

    @Test
    void deleteContact_ShouldThrowExceptionWhenContactDoesNotBelongToUser() {

        when(contactRepository.findByIdAndUserId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ContactNotFoundException.class,
                () -> contactService.deleteContact(2L, 1L)
        );

        verify(contactRepository, never())
                .delete(any(Contact.class));
    }

    @Test
    void toggleFavorite_ShouldToggleSuccessfully() {

        contact.setFavorite(false);

        when(contactRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(contact));

        when(contactRepository.save(any(Contact.class)))
                .thenReturn(contact);

        ContactResponse response =
                contactService.toggleFavorite(1L, 1L);

        assertNotNull(response);

        assertTrue(
                contact.getFavorite()
        );

        verify(contactRepository)
                .findByIdAndUserId(1L, 1L);

        verify(contactRepository)
                .save(contact);
    }

    @Test
    void toggleFavorite_ShouldThrowExceptionWhenContactDoesNotBelongToUser() {

        when(contactRepository.findByIdAndUserId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ContactNotFoundException.class,
                () -> contactService.toggleFavorite(2L, 1L)
        );

        verify(contactRepository, never())
                .save(any(Contact.class));
    }

    @Test
    void searchContacts_ShouldReturnMatchingContacts() {

       when(contactRepository.searchContacts(
        1L,
        "Ali"
)).thenReturn(List.of(contact));
        List<ContactResponse> responses =
                contactService.searchContacts(
                        1L,
                        "Ali"
                );

        assertNotNull(responses);

        assertEquals(
                1,
                responses.size()
        );

        assertEquals(
                "Ali",
                responses.get(0).getFirstName()
        );

        verify(contactRepository)
                .searchContacts(1L, "Ali");
    }

    @Test
    void searchContacts_ShouldReturnEmptyList() {

       when(contactRepository.searchContacts(
        1L,
        "XYZ"
)).thenReturn(List.of());

        List<ContactResponse> responses =
                contactService.searchContacts(
                        1L,
                        "XYZ"
                );

        assertNotNull(responses);

        assertTrue(
                responses.isEmpty()
        );

        verify(contactRepository)
                .searchContacts(1L, "XYZ");
    }
}