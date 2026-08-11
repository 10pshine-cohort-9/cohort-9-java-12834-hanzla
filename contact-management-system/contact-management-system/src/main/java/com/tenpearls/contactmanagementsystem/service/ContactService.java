package com.tenpearls.contactmanagementsystem.service;

import com.tenpearls.contactmanagementsystem.dto.ContactRequest;
import com.tenpearls.contactmanagementsystem.dto.ContactResponse;
import com.tenpearls.contactmanagementsystem.entity.Contact;
import com.tenpearls.contactmanagementsystem.entity.User;
import com.tenpearls.contactmanagementsystem.exception.ContactNotFoundException;
import com.tenpearls.contactmanagementsystem.exception.UserNotFoundException;
import com.tenpearls.contactmanagementsystem.repository.ContactRepository;
import com.tenpearls.contactmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private static final Logger logger =
            LoggerFactory.getLogger(ContactService.class);

    private static final String CONTACT_NOT_FOUND = "Contact not found";
    private static final String USER_NOT_FOUND = "User not found";

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactResponse createContact(ContactRequest request) {

        logger.info("Creating contact for user {}", request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException(USER_NOT_FOUND));

        Contact contact = Contact.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .title(request.getTitle())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .favorite(
                        request.getFavorite() != null
                                ? request.getFavorite()
                                : false
                )
                .user(user)
                .build();

        contact = contactRepository.save(contact);

        logger.info("Contact saved successfully with id {}", contact.getId());

        return mapToResponse(contact);
    }

    public List<ContactResponse> getAllContacts(
            Long userId,
            int page,
            int size,
            String sortBy) {

        logger.info("Fetching contacts page {} size {}", page, size);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy));

        return contactRepository.findByUserId(userId, pageable)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ContactResponse getContactById(Long id) {

        logger.info("Fetching contact {}", id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() ->
                        new ContactNotFoundException(CONTACT_NOT_FOUND));

        return mapToResponse(contact);
    }

    public ContactResponse updateContact(
            Long id,
            ContactRequest request) {

        logger.info("Updating contact {}", id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() ->
                        new ContactNotFoundException(CONTACT_NOT_FOUND));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setTitle(request.getTitle());
        contact.setEmail(request.getEmail());
        contact.setPhoneNumber(request.getPhoneNumber());

        contact.setFavorite(
                request.getFavorite() != null
                        ? request.getFavorite()
                        : false
        );

        contact = contactRepository.save(contact);

        logger.info("Contact {} updated successfully", id);

        return mapToResponse(contact);
    }

    public void deleteContact(Long id) {

        logger.info("Deleting contact {}", id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() ->
                        new ContactNotFoundException(CONTACT_NOT_FOUND));

        contactRepository.delete(contact);

        logger.info("Contact deleted successfully");
    }

    public ContactResponse toggleFavorite(Long id) {

    logger.info("Toggling favorite status for contact {}", id);

    Contact contact = contactRepository.findById(id)
            .orElseThrow(() ->
                    new ContactNotFoundException(CONTACT_NOT_FOUND));

    contact.setFavorite(!contact.getFavorite());

    contact = contactRepository.save(contact);

    logger.info("Favorite status updated successfully");

    return mapToResponse(contact);
}

    public List<ContactResponse> searchContacts(
            Long userId,
            String keyword) {

        logger.info("Searching contacts '{}' for user {}", keyword, userId);

return contactRepository
        .searchContacts(userId, keyword)
        .stream()
        .map(this::mapToResponse)
        .toList();
    }

    private ContactResponse mapToResponse(Contact contact) {

        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .title(contact.getTitle())
                .email(contact.getEmail())
                .phoneNumber(contact.getPhoneNumber())
                .favorite(contact.getFavorite())
                .build();
    }
}