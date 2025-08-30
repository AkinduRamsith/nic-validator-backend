package org.oxy12.mintinvoiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oxy12.mintinvoiz.dto.ApiResponse;
import org.oxy12.mintinvoiz.dto.InvoiceDTO;
import org.oxy12.mintinvoiz.service.InvoiceService;
import org.oxy12.mintinvoiz.service.PdfService;
import org.oxy12.mintinvoiz.utils.JwtUtil;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Invoices", description = "Invoice management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfService pdfService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Create invoice", description = "Create a new invoice")
    public ResponseEntity<ApiResponse<InvoiceDTO.InvoiceResponse>> createInvoice(
            @Valid @RequestBody InvoiceDTO.CreateInvoiceRequest request,
            HttpServletRequest httpRequest) {
        try {
            Long userId = extractUserIdFromToken(httpRequest);
            InvoiceDTO.InvoiceResponse response = invoiceService.createInvoice(userId, request);
            return ResponseEntity.ok(ApiResponse.success("Invoice created successfully", response));
        } catch (Exception e) {
            log.error("Failed to create invoice: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Get user invoices", description = "Get paginated list of user's invoices")
    public ResponseEntity<ApiResponse<Page<InvoiceDTO.InvoiceResponse>>> getUserInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        try {
            Long userId = extractUserIdFromToken(httpRequest);
            Pageable pageable = PageRequest.of(page, size);
            Page<InvoiceDTO.InvoiceResponse> invoices = invoiceService.getUserInvoices(userId, pageable);
            return ResponseEntity.ok(ApiResponse.success(invoices));
        } catch (Exception e) {
            log.error("Failed to get user invoices: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Get invoice by ID", description = "Get invoice details by ID")
    public ResponseEntity<ApiResponse<InvoiceDTO.InvoiceResponse>> getInvoice(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        try {
            Long userId = extractUserIdFromToken(httpRequest);
            InvoiceDTO.InvoiceResponse response = invoiceService.getInvoice(userId, id);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("Failed to get invoice {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Download invoice PDF", description = "Generate and download invoice as PDF")
    public ResponseEntity<Resource> downloadInvoicePdf(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        try {
            Long userId = extractUserIdFromToken(httpRequest);
            Resource pdfResource = pdfService.generateInvoicePdf(userId, id);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"invoice-" + id + ".pdf\"")
                    .body(pdfResource);
        } catch (Exception e) {
            log.error("Failed to generate PDF for invoice {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/share")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Share invoice", description = "Share invoice via email, SMS, or generate link")
    public ResponseEntity<ApiResponse<InvoiceDTO.ShareResponse>> shareInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceDTO.ShareInvoiceRequest request,
            HttpServletRequest httpRequest) {
        try {
            Long userId = extractUserIdFromToken(httpRequest);
            InvoiceDTO.ShareResponse response = invoiceService.shareInvoice(userId, id, request);
            return ResponseEntity.ok(ApiResponse.success("Invoice shared successfully", response));
        } catch (Exception e) {
            log.error("Failed to share invoice {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/view/{shareToken}")
    @Operation(summary = "View shared invoice", description = "View invoice using share token (public endpoint)")
    public ResponseEntity<ApiResponse<InvoiceDTO.InvoiceResponse>> viewSharedInvoice(
            @PathVariable String shareToken) {
        try {
            InvoiceDTO.InvoiceResponse response = invoiceService.getInvoiceByShareToken(shareToken);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("Failed to view shared invoice with token {}: {}", shareToken, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invoice not found"));
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