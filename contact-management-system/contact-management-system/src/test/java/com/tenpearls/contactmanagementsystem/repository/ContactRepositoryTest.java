package com.tenpearls.contactmanagementsystem.repository;

import com.tenpearls.contactmanagementsystem.entity.Contact;
import com.tenpearls.contactmanagementsystem.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {

        contactRepository.deleteAll();
        userRepository.deleteAll();

        user = User.builder()
                .firstName("Hanzla")
                .lastName("Shehzad")
                .email("contacttest@gmail.com")
                .phoneNumber("03991111111")
                .password("123456")
                .build();

        user = userRepository.save(user);

        Contact contact1 = Contact.builder()
                .firstName("Ali")
                .lastName("Khan")
                .email("ali@gmail.com")
                .phoneNumber("03001111111")
                .title("Software Engineer")
                .favorite(false)
                .user(user)
                .build();

        Contact contact2 = Contact.builder()
                .firstName("Ahmed")
                .lastName("Raza")
                .email("ahmed@gmail.com")
                .phoneNumber("03002222222")
                .title("Developer")
                .favorite(false)
                .user(user)
                .build();

        contactRepository.save(contact1);
        contactRepository.save(contact2);
    }

    @Test
    void findByUserId_ShouldReturnContacts() {

        Page<Contact> page = contactRepository.findByUserId(
                user.getId(),
                PageRequest.of(0, 10));

        assertEquals(2, page.getContent().size());
    }

    @Test
    void searchByFirstName_ShouldReturnResult() {

        List<Contact> contacts =
                contactRepository.searchContacts(
                        user.getId(),
                        "Ali");

        assertFalse(contacts.isEmpty());
        assertEquals("Ali", contacts.get(0).getFirstName());
    }

    @Test
    void searchByLastName_ShouldReturnResult() {

        List<Contact> contacts =
                contactRepository.searchContacts(
                        user.getId(),
                        "Raza");

        assertFalse(contacts.isEmpty());
        assertEquals("Raza", contacts.get(0).getLastName());
    }

    @Test
    void searchByEmail_ShouldReturnResult() {

        List<Contact> contacts =
                contactRepository.searchContacts(
                        user.getId(),
                        "ali@gmail.com");

        assertFalse(contacts.isEmpty());
        assertEquals("ali@gmail.com", contacts.get(0).getEmail());
    }

    @Test
    void searchByPhone_ShouldReturnResult() {

        List<Contact> contacts =
                contactRepository.searchContacts(
                        user.getId(),
                        "03001111111");

        assertFalse(contacts.isEmpty());
        assertEquals("03001111111", contacts.get(0).getPhoneNumber());
    }

    @Test
    void searchByTitle_ShouldReturnResult() {

        List<Contact> contacts =
                contactRepository.searchContacts(
                        user.getId(),
                        "Software");

        assertFalse(contacts.isEmpty());
        assertEquals("Software Engineer", contacts.get(0).getTitle());
    }

    @Test
    void search_ShouldReturnEmptyList_WhenNothingMatches() {

        List<Contact> contacts =
                contactRepository.searchContacts(
                        user.getId(),
                        "XYZ123");

        assertTrue(contacts.isEmpty());
    }
}