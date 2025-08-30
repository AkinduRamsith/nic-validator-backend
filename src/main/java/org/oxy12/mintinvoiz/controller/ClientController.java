package org.oxy12.mintinvoiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oxy12.mintinvoiz.dto.ApiResponse;
import org.oxy12.mintinvoiz.dto.ClientDTO;
import org.oxy12.mintinvoiz.service.ClientService;
import org.oxy12.mintinvoiz.utils.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Clients", description = "Client management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    private final ClientService clientService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Create client", description = "Create a new client")
    public ResponseEntity<ApiResponse<ClientDTO.ClientResponse>> createClient(
            @Valid @RequestBody ClientDTO.CreateClientRequest request,
            HttpServletRequest httpRequest) {
        try {
            Long userId = extractUserIdFromToken(httpRequest);
            ClientDTO.ClientResponse response = clientService.createClient(userId, request);
            return ResponseEntity.ok(ApiResponse.success("Client created successfully", response));
        } catch (Exception e) {
            log.error("Failed to create client: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Get user clients", description = "Get paginated list of user's clients")
    public ResponseEntity<ApiResponse<Page<ClientDTO.ClientResponse>>> getUserClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            HttpServletRequest httpRequest) {
        try {
            Long userId = extractUserIdFromToken(httpRequest);
            Pageable pageable = PageRequest.of(page, size);
            Page<ClientDTO.ClientResponse> clients = clientService.getUserClients(userId, search, pageable);
            return ResponseEntity.ok(ApiResponse.success(clients));
        } catch (Exception e) {
            log.error("Failed to get user clients: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Get client by ID", description = "Get client details by ID")
    public ResponseEntity<ApiResponse<ClientDTO.ClientResponse>> getClient(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        try {
            Long userId = extractUserIdFromToken(httpRequest);
            ClientDTO.ClientResponse response = clientService.getClient(userId, id);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("Failed to get client {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long extractUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new RuntimeException("Invalid authorization header");
    }
}