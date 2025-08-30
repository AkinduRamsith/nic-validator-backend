package org.oxy12.mintinvoiz.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class InvoiceNumberGenerator {

    @Value("${invoice.default-prefix:MI}")
    private String defaultPrefix;

    private final AtomicLong sequence = new AtomicLong(1);

    /**
     * Generates a unique invoice number using the format: PREFIX-YYYYMMDD-NNNN
     * 
     * @param customPrefix Optional custom prefix (if null, uses default)
     * @return Generated invoice number
     */
    public String generateInvoiceNumber(String customPrefix) {
        String prefix = (customPrefix != null && !customPrefix.trim().isEmpty()) 
                       ? customPrefix.trim().toUpperCase() 
                       : defaultPrefix;
        
        // Get current date in YYYYMMDD format
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // Get sequence number (4 digits, zero-padded)
        long seqNum = sequence.getAndIncrement();
        String sequenceStr = String.format("%04d", seqNum % 10000);
        
        // Combine to create invoice number
        String invoiceNumber = String.format("%s-%s-%s", prefix, dateStr, sequenceStr);
        
        log.info("Generated invoice number: {}", invoiceNumber);
        return invoiceNumber;
    }

    /**
     * Alternative method using timestamp hash for better uniqueness
     * Format: PREFIX-XXXXXXXX (8-digit hash)
     */
    public String generateHashedInvoiceNumber(String customPrefix) {
        String prefix = (customPrefix != null && !customPrefix.trim().isEmpty()) 
                       ? customPrefix.trim().toUpperCase() 
                       : defaultPrefix;
        
        // Create 8-digit hash from current timestamp and sequence
        long timestamp = System.currentTimeMillis();
        long seqNum = sequence.getAndIncrement();
        long combined = timestamp + seqNum;
        
        // Generate 8-digit number from hash
        String hashStr = String.format("%08d", Math.abs((int) (combined % 100000000)));
        
        String invoiceNumber = String.format("%s-%s", prefix, hashStr);
        
        log.info("Generated hashed invoice number: {}", invoiceNumber);
        return invoiceNumber;
    }

    /**
     * Reset sequence (typically called at start of new day)
     */
    public void resetSequence() {
        sequence.set(1);
        log.info("Invoice number sequence reset");
    }
}