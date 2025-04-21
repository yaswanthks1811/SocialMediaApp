package org.example.Bean;

import java.io.Serializable;

public class User implements Serializable {
    private int user_id;
    private String username;
    private String password;
    private String first_name="";
    private String last_name="";
    private String account_status;
    private String bio="";
    private String email;

    public User() {

    }
    public int getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
    	if(first_name==null){
    		return "";
    	}
        return first_name;
    }

    public String getLastName() {
    	if(last_name==null){
    		return "";
    	}
        return last_name;
    }

    public String getAccountStatus() {
        return account_status;
    }

    public String getBio() {
    	if(bio==null){
    		return "";
    	}
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public static class Builder {
        private int user_id;
        private String username;
        private String password;
        private String first_name;
        private String last_name;
        private String account_status;
        private String bio;
        private String email;

        public Builder() {
            first_name="";
            last_name="";
            bio="";
        }

        public Builder userId(int user_id) {
            this.user_id = user_id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder firstName(String first_name) {
            this.first_name = first_name;
            return this;
        }

        public Builder lastName(String last_name) {
            this.last_name = last_name;
            return this;
        }

        public Builder accountStatus(String account_status) {
            this.account_status = account_status;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            User user = new User();
            user.user_id = this.user_id;
            user.username = this.username;
            user.password = this.password;
            user.first_name = this.first_name;
            user.last_name = this.last_name;
            user.account_status = this.account_status;
            user.bio = this.bio;
            user.email = this.email;
            return user;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", account_status='" + account_status + '\'' +
                ", bio='" + bio + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // Example of how to use the builder:
    // User user = new User.Builder()
    //     .userId(1)
    //     .username("johndoe")
    //     .firstName("John")
    //     .lastName("Doe")
    //     .email("john@example.com")
    //     .build();
}





