package com.sonfind.chelsea.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecipientRole {
    PUBLISHER("publisher"),
    SUBSCRIBER("subscriber");

    private final String role;

    public static RecipientRole from(String role) {
        for (RecipientRole recipientRole : RecipientRole.values()) {
            if (recipientRole.role.equalsIgnoreCase(role)) {
                return recipientRole;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}
