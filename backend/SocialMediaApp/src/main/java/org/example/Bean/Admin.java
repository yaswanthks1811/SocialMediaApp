package org.example.Bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Admin implements Serializable {
    private final int adminUserId;
    private final String adminUsername;
    private final String adminPassword;
    private final String adminFirstName;
    private final String adminLastName;
    private final String email;
    private final String phoneNumber;
    private final Timestamp createdAt;

    public Admin(Builder builder) {
        this.adminUserId = builder.adminUserId;
        this.adminUsername = builder.adminUsername;
        this.adminPassword = builder.adminPassword;
        this.adminFirstName = builder.adminFirstName;
        this.adminLastName = builder.adminLastName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {
        private int adminUserId;
        private String adminUsername;
        private String adminPassword;
        private String adminFirstName;
        private String adminLastName;
        private String email;
        private String phoneNumber;
        private Timestamp createdAt;

        public Builder() {
            this.email="";
        }

        public Builder adminUserId(int adminUserId) {
            this.adminUserId = adminUserId;
            return this;
        }

        public Builder adminUsername(String adminUsername) {
            validateUsername(adminUsername);
            this.adminUsername = adminUsername;
            return this;
        }

        public Builder adminPassword(String adminPassword) {
            validatePassword(adminPassword);
            this.adminPassword = adminPassword;
            return this;
        }

        public Builder adminFirstName(String adminFirstName) {
            this.adminFirstName = adminFirstName;
            return this;
        }

        public Builder adminLastName(String adminLastName) {
            this.adminLastName = adminLastName;
            return this;
        }

        public Builder email(String email) {
            validateEmail(email);
            this.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            validatePhoneNumber(phoneNumber);
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }

        private void validateUsername(String username) {
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Admin username cannot be empty");
            }
            if (username.length() > 50) {
                throw new IllegalArgumentException("Admin username cannot exceed 50 characters");
            }
        }

        private void validatePassword(String password) {
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Admin password cannot be empty");
            }
        }

        private void validateEmail(String email) {
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }

        private void validatePhoneNumber(String phoneNumber) {
            if (phoneNumber != null && !phoneNumber.matches("^[6-9][0-9]{9}$")) {
                throw new IllegalArgumentException("Phone number must start with 6-9 and be 10 digits");
            }
        }
    }

    public int getAdminUserId() { return adminUserId; }
    public String getAdminUsername() { return adminUsername; }
    public String getAdminPassword() { return adminPassword; }
    public String getAdminFirstName() { return adminFirstName; }
    public String getAdminLastName() { return adminLastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Timestamp getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Admin{" +
                "adminUserId=" + adminUserId +
                ", adminUsername='" + adminUsername + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}