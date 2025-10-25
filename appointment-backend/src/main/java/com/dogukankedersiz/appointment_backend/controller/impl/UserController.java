package com.dogukankedersiz.appointment_backend.controller.impl;

import com.dogukankedersiz.appointment_backend.dto.DtoUser;
import com.dogukankedersiz.appointment_backend.entities.User;
import com.dogukankedersiz.appointment_backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/current")
    public ResponseEntity<DtoUser> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = userService.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + user.getEmail()));
        DtoUser userDTO = new DtoUser();
        userDTO.setId(currentUser.getId());
        userDTO.setEmail(currentUser.getEmail());
        return ResponseEntity.ok(userDTO);
    }
}