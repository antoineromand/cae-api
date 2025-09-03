package com.clickandeat.api.authentication.controllers;

import com.clickandeat.api.authentication.dto.UpdatePasswordRequestDto;
import com.clickandeat.api.authentication.mapper.UpdatePasswordRequestMapper;
import com.clickandeat.api.authentication.swagger.ErrorResponse;
import com.clickandeat.api.authentication.swagger.UpdatePasswordApiResponse;
import com.clickandeat.api.config.filter.CustomUserDetails;
import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.authentication.application.usecase.update_password.IUpdatePasswordUseCase;
import com.clickandeat.authentication.application.usecase.update_password.UpdatePasswordCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("private/api/v1/authentication")
public class PrivateAuthenticationController {

  private final IUpdatePasswordUseCase updatePasswordUseCase;

  public PrivateAuthenticationController(IUpdatePasswordUseCase updatePasswordUseCase) {
    this.updatePasswordUseCase = updatePasswordUseCase;
  }

  @PreAuthorize("hasRole('CONSUMER') and hasAuthority('SCOPE_UPDATE:USER-PASSWORD')")
  @PutMapping("update-password")
  @Operation(summary = "Update password when user is authenticated.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Update password with success",
            content = @Content(schema = @Schema(implementation = UpdatePasswordApiResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Given password does not match with actual password",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  public ResponseEntity<GenericApiResponse<String>> updatePassword(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody UpdatePasswordRequestDto dto) {
    UpdatePasswordCommand command =
        UpdatePasswordRequestMapper.toCommand(dto, userDetails.getUsername());
    this.updatePasswordUseCase.execute(command);
    return ResponseEntity.ok(new GenericApiResponse<>("Password updated successfully", null));
  }
}
