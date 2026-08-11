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
import static org.mockito.ArgumentMatchers.*;
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
                .user(user)
                .build();

        request = new ContactRequest();

        request.setFirstName("Ali");
        request.setLastName("Khan");
        request.setTitle("Developer");
        request.setEmail("ali@gmail.com");
        request.setPhoneNumber("03009999999");
        request.setUserId(1L);
    }
    @Test
void createContact_ShouldCreateSuccessfully() {

    when(userRepository.findById(1L))
            .thenReturn(Optional.of(user));

    when(contactRepository.save(any(Contact.class)))
            .thenReturn(contact);

    ContactResponse response =
            contactService.createContact(request);

    assertNotNull(response);

    assertEquals("Ali",
            response.getFirstName());

    verify(contactRepository)
            .save(any(Contact.class));
}

@Test
void createContact_ShouldThrowUserNotFoundException() {

    when(userRepository.findById(1L))
            .thenReturn(Optional.empty());

    assertThrows(
            UserNotFoundException.class,
            () -> contactService.createContact(request));

    verify(contactRepository, never())
            .save(any());
}
@Test
void getContactById_ShouldReturnContact() {

    when(contactRepository.findById(1L))
            .thenReturn(Optional.of(contact));

    ContactResponse response =
            contactService.getContactById(1L);

    assertEquals("Ali",
            response.getFirstName());
}

@Test
void getContactById_ShouldThrowException() {

    when(contactRepository.findById(1L))
            .thenReturn(Optional.empty());

    assertThrows(
            ContactNotFoundException.class,
            () -> contactService.getContactById(1L));
}
@Test
void getAllContacts_ShouldReturnPage() {

    Page<Contact> page =
            new PageImpl<>(List.of(contact));

    when(contactRepository.findByUserId(
            eq(1L),
            any(PageRequest.class)))
            .thenReturn(page);

    List<ContactResponse> responses =
            contactService.getAllContacts(
                    1L,
                    0,
                    5,
                    "firstName");

    assertEquals(1,
            responses.size());
}
@Test
void updateContact_ShouldUpdateSuccessfully() {

    when(contactRepository.findById(1L))
            .thenReturn(Optional.of(contact));

    when(contactRepository.save(any(Contact.class)))
            .thenReturn(contact);

    ContactResponse response =
            contactService.updateContact(1L, request);

    assertNotNull(response);
    assertEquals("Ali", response.getFirstName());

    verify(contactRepository).save(any(Contact.class));
}
@Test
void updateContact_ShouldThrowContactNotFoundException() {

    when(contactRepository.findById(1L))
            .thenReturn(Optional.empty());

    assertThrows(
            ContactNotFoundException.class,
            () -> contactService.updateContact(1L, request));
}
@Test
void deleteContact_ShouldDeleteSuccessfully() {

    when(contactRepository.findById(1L))
            .thenReturn(Optional.of(contact));

    contactService.deleteContact(1L);

    verify(contactRepository).delete(contact);
}
@Test
void deleteContact_ShouldThrowContactNotFoundException() {

    when(contactRepository.findById(1L))
            .thenReturn(Optional.empty());

    assertThrows(
            ContactNotFoundException.class,
            () -> contactService.deleteContact(1L));
}
@Test
void searchContacts_ShouldReturnMatchingContacts() {

when(contactRepository.searchContacts(
        anyLong(),
        anyString()))
.thenReturn(List.of(contact));

    List<ContactResponse> responses =
            contactService.searchContacts(1L, "Ali");

    assertEquals(1, responses.size());
    assertEquals("Ali", responses.get(0).getFirstName());
}
@Test
void searchContacts_ShouldReturnEmptyList() {

    when(contactRepository.searchContacts(
            anyLong(),
            anyString()))
            .thenReturn(List.of());

    List<ContactResponse> responses =
            contactService.searchContacts(1L, "XYZ");

    assertTrue(responses.isEmpty());
}
}