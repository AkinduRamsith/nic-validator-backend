package org.oxy12.mintinvoiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Mint Invoiz - Invoice Management System
 * 
 * A comprehensive invoice management system built by OXY12
 * Features: Invoice generation, Client management, Project tracking,
 * PDF generation, Email/SMS notifications, Role-based access control
 * 
 * @author OXY12
 * @version 1.0.0
 */
@SpringBootApplication
@EnableAsync
public class MintInvoizApplication {

    public static void main(String[] args) {
        SpringApplication.run(MintInvoizApplication.class, args);
    }
}