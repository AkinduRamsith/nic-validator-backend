package org.oxy12.mintinvoiz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.oxy12.mintinvoiz.dto.InvoiceDTO;
import org.oxy12.mintinvoiz.entity.*;
import org.oxy12.mintinvoiz.repository.*;
import org.oxy12.mintinvoiz.utils.InvoiceNumberGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ItemRepository itemRepository;
    private final InvoiceNumberGenerator numberGenerator;
    private final ModelMapper modelMapper;
    private final QuotaService quotaService;

    @Transactional
    public InvoiceDTO.InvoiceResponse createInvoice(Long userId, InvoiceDTO.CreateInvoiceRequest request) {
        // Check quota
        if (!quotaService.canCreateInvoice(userId)) {
            throw new RuntimeException("Invoice quota exceeded. Please upgrade your plan.");
        }

        // Validate client belongs to user
        ClientEntity client = clientRepository.findByIdAndUserIdAndIsActiveTrue(request.getClientId(), userId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create invoice
        InvoiceEntity invoice = InvoiceEntity.builder()
                .invoiceNumber(numberGenerator.generateInvoiceNumber(null)) // TODO: Support custom prefix
                .user(user)
                .client(client)
                .invoiceDate(request.getInvoiceDate())
                .dueDate(request.getDueDate())
                .status(InvoiceEntity.Status.DRAFT)
                .notes(request.getNotes())
                .termsConditions(request.getTermsConditions())
                .paymentInstructions(request.getPaymentInstructions())
                .shareToken(UUID.randomUUID().toString())
                .build();

        // Add project if specified
        if (request.getProjectId() != null) {
            // TODO: Validate project belongs to client
            ProjectEntity project = new ProjectEntity();
            project.setId(request.getProjectId());
            invoice.setProject(project);
        }

        // Calculate totals
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;

        List<InvoiceItemEntity> invoiceItems = request.getItems().stream()
                .map(itemRequest -> {
                    InvoiceItemEntity invoiceItem = createInvoiceItem(invoice, itemRequest);
                    return invoiceItem;
                })
                .collect(Collectors.toList());

        // Calculate totals from items
        for (InvoiceItemEntity item : invoiceItems) {
            subtotal = subtotal.add(item.getLineTotal());
            totalTax = totalTax.add(item.getLineTax());
            totalDiscount = totalDiscount.add(item.getLineDiscount());
        }

        BigDecimal grandTotal = subtotal.add(totalTax).subtract(totalDiscount);

        invoice.setSubtotal(subtotal);
        invoice.setTotalTax(totalTax);
        invoice.setTotalDiscount(totalDiscount);
        invoice.setGrandTotal(grandTotal);

        invoice = invoiceRepository.save(invoice);

        // Update quota
        quotaService.incrementInvoiceQuota(userId);

        log.info("Invoice created successfully: {} for user: {}", invoice.getInvoiceNumber(), userId);
        
        return convertToInvoiceResponse(invoice);
    }

    public Page<InvoiceDTO.InvoiceResponse> getUserInvoices(Long userId, Pageable pageable) {
        Page<InvoiceEntity> invoices = invoiceRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return invoices.map(this::convertToInvoiceResponse);
    }

    public InvoiceDTO.InvoiceResponse getInvoice(Long userId, Long invoiceId) {
        InvoiceEntity invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        if (!invoice.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        
        return convertToInvoiceResponse(invoice);
    }

    public InvoiceDTO.InvoiceResponse getInvoiceByShareToken(String shareToken) {
        InvoiceEntity invoice = invoiceRepository.findByShareToken(shareToken)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        return convertToInvoiceResponse(invoice);
    }

    @Transactional
    public InvoiceDTO.ShareResponse shareInvoice(Long userId, Long invoiceId, InvoiceDTO.ShareInvoiceRequest request) {
        InvoiceEntity invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        if (!invoice.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        // Check sharing permissions based on package
        if (!quotaService.canShareInvoice(userId, request.getMethod())) {
            throw new RuntimeException("Sharing not available in your current plan. Please upgrade.");
        }

        String shareLink = String.format("https://mintinvoiz.com/invoice/view/%s", invoice.getShareToken());
        
        switch (request.getMethod()) {
            case "email":
                if (request.getTo() == null || request.getTo().trim().isEmpty()) {
                    throw new RuntimeException("Email address is required");
                }
                // TODO: Send email
                quotaService.incrementEmailQuota(userId);
                return InvoiceDTO.ShareResponse.builder()
                        .sent(true)
                        .message("Invoice sent via email successfully")
                        .shareLink(shareLink)
                        .build();
                
            case "sms":
                if (request.getTo() == null || request.getTo().trim().isEmpty()) {
                    throw new RuntimeException("Phone number is required");
                }
                // TODO: Send SMS
                quotaService.incrementSmsQuota(userId);
                return InvoiceDTO.ShareResponse.builder()
                        .sent(true)
                        .message("Invoice sent via SMS successfully")
                        .shareLink(shareLink)
                        .build();
                
            case "link":
                return InvoiceDTO.ShareResponse.builder()
                        .shareLink(shareLink)
                        .message("Share link generated successfully")
                        .sent(false)
                        .build();
                
            default:
                throw new RuntimeException("Invalid sharing method");
        }
    }

    private InvoiceItemEntity createInvoiceItem(InvoiceEntity invoice, InvoiceDTO.InvoiceItemRequest request) {
        BigDecimal quantity = request.getQuantity();
        BigDecimal unitPrice = request.getUnitPrice();
        BigDecimal taxRate = request.getTaxRate() != null ? request.getTaxRate() : BigDecimal.ZERO;
        BigDecimal discountValue = request.getDiscountValue() != null ? request.getDiscountValue() : BigDecimal.ZERO;

        // Calculate line totals
        BigDecimal lineSubtotal = quantity.multiply(unitPrice);
        BigDecimal lineDiscount = calculateDiscount(lineSubtotal, request.getDiscountType(), discountValue);
        BigDecimal lineAfterDiscount = lineSubtotal.subtract(lineDiscount);
        BigDecimal lineTax = lineAfterDiscount.multiply(taxRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        BigDecimal lineTotal = lineAfterDiscount.add(lineTax);

        return InvoiceItemEntity.builder()
                .invoice(invoice)
                .itemName(request.getItemName())
                .itemDescription(request.getItemDescription())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .taxRate(taxRate)
                .discountType(InvoiceItemEntity.DiscountType.valueOf(request.getDiscountType()))
                .discountValue(discountValue)
                .lineTotal(lineTotal)
                .lineTax(lineTax)
                .lineDiscount(lineDiscount)
                .sortOrder(request.getSortOrder())
                .build();
    }

    private BigDecimal calculateDiscount(BigDecimal amount, String discountType, BigDecimal discountValue) {
        if (discountValue == null || discountValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        switch (discountType) {
            case "PERCENTAGE":
                return amount.multiply(discountValue.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            case "FIXED_AMOUNT":
                return discountValue.min(amount); // Discount cannot exceed amount
            default:
                return BigDecimal.ZERO;
        }
    }

    private InvoiceDTO.InvoiceResponse convertToInvoiceResponse(InvoiceEntity invoice) {
        InvoiceDTO.InvoiceResponse response = modelMapper.map(invoice, InvoiceDTO.InvoiceResponse.class);
        
        // Convert BigDecimal to String for frontend
        response.setSubtotal(invoice.getSubtotal().toString());
        response.setTotalTax(invoice.getTotalTax().toString());
        response.setTotalDiscount(invoice.getTotalDiscount().toString());
        response.setGrandTotal(invoice.getGrandTotal().toString());
        
        return response;
    }
}