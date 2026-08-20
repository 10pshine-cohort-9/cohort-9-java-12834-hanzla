package com.tenpearls.contactmanagementsystem.service;

import com.tenpearls.contactmanagementsystem.dto.ContactResponse;
import com.tenpearls.contactmanagementsystem.dto.DashboardResponse;
import com.tenpearls.contactmanagementsystem.entity.Contact;
import com.tenpearls.contactmanagementsystem.repository.ContactRepository;
import com.tenpearls.contactmanagementsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ContactRepository contactRepository;

    private final UserRepository userRepository;


    public DashboardResponse getDashboard(Long userId) {

        List<Contact> contacts =
                contactRepository.findByUserIdOrderByIdDesc(
                        userId,
                        PageRequest.of(0, 5)
                );

        List<ContactResponse> recentContacts =
                new ArrayList<>();

        for (Contact contact : contacts) {

            ContactResponse response =
                    ContactResponse.builder()
                            .id(contact.getId())
                            .firstName(contact.getFirstName())
                            .lastName(contact.getLastName())
                            .title(contact.getTitle())
                            .email(contact.getEmail())
                            .phoneNumber(contact.getPhoneNumber())
                            .favorite(contact.getFavorite())
                            .build();

            recentContacts.add(response);
        }

        return DashboardResponse.builder()
                .totalContacts(
                        contactRepository.countByUserId(userId)
                )
                .favoriteContacts(
                        contactRepository
                                .countByUserIdAndFavoriteTrue(userId)
                )
                .totalUsers(userRepository.count())
                .recentContacts(recentContacts)
                .build();
    }
}