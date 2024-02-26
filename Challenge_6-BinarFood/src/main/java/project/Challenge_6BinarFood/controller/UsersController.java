package project.Challenge_6BinarFood.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import project.Challenge_6BinarFood.dto.response.user.UsersUpdateResponse;
import project.Challenge_6BinarFood.dto.response.web.WebResponse;
import project.Challenge_6BinarFood.service.user.UsersService;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    @Autowired
    UsersService usersService;

    @Autowired
    TemplateEngine templateEngine;


    @DeleteMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<String> deleteUsers(Principal principal){
        try {
            usersService.deleteUser(principal);
            return WebResponse.<String>builder().data("Deleted").messages("Success").build();
        }catch (Exception e){
            return WebResponse.<String>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @PatchMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<UsersUpdateResponse> updateUser(Principal principal, @RequestBody Map<String, Optional> field){
        try {
            UsersUpdateResponse usersResponse = usersService.updateUser(principal, field);
            return WebResponse.<UsersUpdateResponse>builder().countRecord(1).data(usersResponse).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<UsersUpdateResponse>builder().data(null).messages(e.getMessage()).build();
        }
    }

}
