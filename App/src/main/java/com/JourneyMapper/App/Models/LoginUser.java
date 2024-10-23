package com.JourneyMapper.App.Models;

//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotBlank;

public class LoginUser {

//    @NotBlank(message = "Email is required")
//    @Email(message = "Email should be valid")
    private String login_email;

//    @NotBlank(message = "Password is required")
    private String login_password;

    // Default constructor
    public LoginUser() {
        super();
    }

    // Parameterized constructor
    public LoginUser(String login_email, String login_password) {
        super();
        this.login_email = login_email;
        this.login_password = login_password;
    }

    // Getters and Setters
    public String getLogin_email() {
        return login_email;
    }

    public void setLogin_email(String login_email) {
        this.login_email = login_email;
    }

    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
    }

    @Override
    public String toString() {
        return "LoginUser [login_email=" + login_email + ", login_password=" + login_password + "]";
    }
}
