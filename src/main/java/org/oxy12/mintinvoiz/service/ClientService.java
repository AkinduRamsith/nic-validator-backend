package org.oxy12.mintinvoiz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.oxy12.mintinvoiz.dto.ClientDTO;
import org.oxy12.mintinvoiz.entity.ClientEntity;
import org.oxy12.mintinvoiz.entity.UserEntity;
import org.oxy12.mintinvoiz.repository.ClientRepository;
import org.oxy12.mintinvoiz.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ClientDTO.ClientResponse createClient(Long userId, ClientDTO.CreateClientRequest request) {
        // Check if email already exists for this user
        if (clientRepository.existsByEmailAndUserId(request.getEmail(), userId)) {
            throw new RuntimeException("Client with this email already exists");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ClientEntity client = ClientEntity.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .businessRegistrationNumber(request.getBusinessRegistrationNumber())
                .contactPerson(request.getContactPerson())
                .websiteUrl(request.getWebsiteUrl())
                .logoUrl(request.getLogoUrl())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .stateProvince(request.getStateProvince())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .isActive(true)
                .build();

        client = clientRepository.save(client);
        
        log.info("Client created successfully: {} for user: {}", client.getCompanyName(), userId);
        
        return convertToClientResponse(client);
    }

    public Page<ClientDTO.ClientResponse> getUserClients(Long userId, String search, Pageable pageable) {
        Page<ClientEntity> clients;
        
        if (search != null && !search.trim().isEmpty()) {
            clients = clientRepository.searchClientsByUser(userId, search.trim(), pageable);
        } else {
            clients = clientRepository.findByUserIdAndIsActiveTrueOrderByCompanyNameAsc(userId, pageable);
        }
        
        return clients.map(this::convertToClientResponse);
    }

    public ClientDTO.ClientResponse getClient(Long userId, Long clientId) {
        ClientEntity client = clientRepository.findByIdAndUserIdAndIsActiveTrue(clientId, userId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        
        return convertToClientResponse(client);
    }

    @Transactional
    public ClientDTO.ClientResponse updateClient(Long userId, Long clientId, ClientDTO.CreateClientRequest request) {
        ClientEntity client = clientRepository.findByIdAndUserIdAndIsActiveTrue(clientId, userId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Check if email already exists for another client
        if (!client.getEmail().equals(request.getEmail()) && 
            clientRepository.existsByEmailAndUserId(request.getEmail(), userId)) {
            throw new RuntimeException("Another client with this email already exists");
        }

        // Update client fields
        client.setCompanyName(request.getCompanyName());
        client.setEmail(request.getEmail());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setBusinessRegistrationNumber(request.getBusinessRegistrationNumber());
        client.setContactPerson(request.getContactPerson());
        client.setWebsiteUrl(request.getWebsiteUrl());
        client.setLogoUrl(request.getLogoUrl());
        client.setAddressLine1(request.getAddressLine1());
        client.setAddressLine2(request.getAddressLine2());
        client.setCity(request.getCity());
        client.setStateProvince(request.getStateProvince());
        client.setPostalCode(request.getPostalCode());
        client.setCountry(request.getCountry());

        client = clientRepository.save(client);
        
        log.info("Client updated successfully: {} for user: {}", client.getCompanyName(), userId);
        
        return convertToClientResponse(client);
    }

    @Transactional
    public void deleteClient(Long userId, Long clientId) {
        ClientEntity client = clientRepository.findByIdAndUserIdAndIsActiveTrue(clientId, userId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Soft delete
        client.setIsActive(false);
        clientRepository.save(client);
        
        log.info("Client soft deleted: {} for user: {}", client.getCompanyName(), userId);
    }

    private ClientDTO.ClientResponse convertToClientResponse(ClientEntity client) {
        ClientDTO.ClientResponse response = modelMapper.map(client, ClientDTO.ClientResponse.class);
        
        // Add computed fields
        if (client.getInvoices() != null) {
            response.setTotalInvoices(client.getInvoices().size());
        }
        if (client.getProjects() != null) {
            response.setTotalProjects(client.getProjects().size());
        }
        
        return response;
    }
}