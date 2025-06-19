package com.pickandeat.api.authentication.controllers;

import com.pickandeat.api.authentication.dto.UpdatePasswordRequestDto;
import com.pickandeat.api.authentication.mapper.UpdatePasswordRequestMapper;
import com.pickandeat.api.config.filter.CustomUserDetails;
import com.pickandeat.api.shared.GenericApiResponse;
import com.pickandeat.authentication.application.usecase.update_password.IUpdatePasswordUseCase;
import com.pickandeat.authentication.application.usecase.update_password.UpdatePasswordCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("private/api/v1/authentication")
public class PrivateAuthenticationController {

    private final IUpdatePasswordUseCase updatePasswordUseCase;

    public PrivateAuthenticationController(IUpdatePasswordUseCase updatePasswordUseCase) {
        this.updatePasswordUseCase = updatePasswordUseCase;
    }

    @PreAuthorize("hasRole('CONSUMER') and hasAuthority('SCOPE_UPDATE:USER-PASSWORD')")
    @PutMapping("update-password")
    public ResponseEntity<GenericApiResponse<String>> test(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UpdatePasswordRequestDto dto) {
        UpdatePasswordCommand command = UpdatePasswordRequestMapper.toCommand(dto, userDetails.getUsername());
        this.updatePasswordUseCase.execute(command);
        return ResponseEntity.ok(new GenericApiResponse<>("Password updated successfully", null));
    }

}
