package com.tenpearls.contactmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalContacts;

    private long favoriteContacts;

    private long totalUsers;

    private List<ContactResponse> recentContacts;

}