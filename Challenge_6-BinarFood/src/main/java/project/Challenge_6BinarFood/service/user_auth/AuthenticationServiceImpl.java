package project.Challenge_6BinarFood.service.user_auth;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.Challenge_6BinarFood.dto.request.user.*;
import project.Challenge_6BinarFood.dto.response.user.*;
import project.Challenge_6BinarFood.model.user.ERole;
import project.Challenge_6BinarFood.model.user.Role;
import project.Challenge_6BinarFood.model.user.User;
import project.Challenge_6BinarFood.repository.user.RoleRepository;
import project.Challenge_6BinarFood.repository.user.UserRepository;
import project.Challenge_6BinarFood.security.service.JwtService;
import project.Challenge_6BinarFood.security.service.UserDetailsImpl;
import project.Challenge_6BinarFood.security.service.UserService;
import project.Challenge_6BinarFood.security.util.EmailUtil;
import project.Challenge_6BinarFood.security.util.OtpUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuhenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;

    @Override
    public JwtResponseRegister register(RegisterRequest request) {
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmailRegister(request.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        Set<Role> roles = addRole(ERole.valueOf(request.getRole()));
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .otp(otp)
                .otpGeneratedTime(LocalDateTime.now())
                .build();
        userRepository.save(user);
        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
        List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
//        var jwtToken = jwtService.generateToken(userDetails);
        return JwtResponseRegister.builder()
                .token("User not verify")
                .type("Bearer")
                .username(user.getUserName())
                .roles(rolesList)
                .otp(user.getOtp())
                .otpGeneratedTime(user.getOtpGeneratedTime())
                .build();
    }

    @Override
    public JwtResponseRegister verifyAccount(String email, String otp) {
        User user = getIdUser(email);
        UserDetails userDetails = userService.loadUserByUsername(email);
        List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
            user.setActive(true);
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(userDetails);
            return JwtResponseRegister.builder()
                    .token(jwtToken)
                    .type("Bearer")
                    .username(user.getUserName())
                    .roles(rolesList)
                    .otp(user.getOtp())
                    .otpGeneratedTime(user.getOtpGeneratedTime())
                    .build();
        }
        return JwtResponseRegister.builder()
                .token("User not verify")
                .type("Bearer")
                .username(user.getUserName())
                .roles(rolesList)
                .otp("Please regenerate otp and try again")
                .build();
    }

    @Override
    public RegenerateOtpResponse regenerateOtp(String email) {
        User user = getIdUser(email);
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmailRegister(email, otp);
        } catch (MessagingException e) {
            return RegenerateOtpResponse.builder()
                    .message("Unable to send otp please try again")
                    .build();
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return RegenerateOtpResponse.builder()
                .Otp(otp)
                .message("Email sent... please verify account within 1 minute")
                .build();
    }

    @Override
    public JwtResponseLogin login(LoginRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserDetails user = userService.loadUserByUsername(request.getEmail());
        User userId = getIdUser(request.getEmail());
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        if (userId.isActive()) {
            var jwtToken = jwtService.generateToken(user);
            return JwtResponseLogin.builder()
                    .token(jwtToken)
                    .type("Bearer")
                    .username(user.getUsername())
                    .roles(roles)
                    .build();
        }
        return JwtResponseLogin.builder()
                .token("User not verify")
                .type("Bearer")
                .username(user.getUsername())
                .roles(roles)
                .build();
    }

    @Override
    public Set<Role> addRole(ERole role) {
        Role getRole = roleRepository.findRoleByName(role).get();
        Set<Role> roleHashSet = new HashSet<>();
        roleHashSet.add(getRole);
        return roleHashSet;
    }

    @Override
    public User getIdUser(String name) {
        return userRepository.findUserByEmail(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email: " + name));
    }

    @Override
    public JwtResponseVerifyForgot changePassword(ChangePasswordRequest request, String email) {

        User user = getIdUser(email);
        if (user.isActive()){
            // check if the two new passwords are the same
            if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                return JwtResponseVerifyForgot.builder()
                        .message("Password are not same")
                        .build();
            }

            // update the password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));

            // save the new password
            userRepository.save(user);
            return JwtResponseVerifyForgot.builder()
                    .message("Your password has been change")
                    .build();
        }
        return JwtResponseVerifyForgot.builder()
                .message("Your account not veriffy")
                .build();
    }

    @Override
    public JwtResponseForgotPassword forgotPassword(ForgotPasswordRequest request) {
        User userId = getIdUser(request.getEmail());
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmailForgot(request.getEmail(), otp);
            userId.setActive(false);
            userId.setOtp(otp);
            userId.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(userId);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        return JwtResponseForgotPassword.builder()
                .message("Check your email to verification using OTP")
                .otp(otp)
                .build();
    }

    @Override
    public JwtResponseVerifyForgot verifyAccountPassword(String email, String otp) {
        User user = getIdUser(email);
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
            user.setActive(true);
            userRepository.save(user);
            return JwtResponseVerifyForgot.builder()
                    .message("Your account has been verify, change password")
                    .build();
        }
        return JwtResponseVerifyForgot.builder()
                .message("Your account not veriffy")
                .build();
    }
}
