package project.Challenge_6BinarFood.service.user_auth;

import project.Challenge_6BinarFood.dto.request.user.ChangePasswordRequest;
import project.Challenge_6BinarFood.dto.request.user.ForgotPasswordRequest;
import project.Challenge_6BinarFood.dto.request.user.LoginRequest;
import project.Challenge_6BinarFood.dto.request.user.RegisterRequest;
import project.Challenge_6BinarFood.dto.response.user.*;
import project.Challenge_6BinarFood.model.user.ERole;
import project.Challenge_6BinarFood.model.user.Role;
import project.Challenge_6BinarFood.model.user.User;

import java.security.Principal;
import java.util.Set;

public interface AuhenticationService{
    JwtResponseRegister register(RegisterRequest request);

    JwtResponseRegister verifyAccount(String email, String otp);

    RegenerateOtpResponse regenerateOtp(String email);

    JwtResponseLogin login(LoginRequest request);
    Set<Role> addRole(ERole role);
    User getIdUser(String name);

    JwtResponseVerifyForgot changePassword(ChangePasswordRequest request, String email);

    JwtResponseForgotPassword forgotPassword(ForgotPasswordRequest request);

    JwtResponseVerifyForgot verifyAccountPassword(String email, String otp);
}
