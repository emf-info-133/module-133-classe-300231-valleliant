package com.monprojet.service2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monprojet.service2.dto.AdminDTO;
import com.monprojet.service2.model.Admin;
import com.monprojet.service2.repository.AdminRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    
    private final AdminRepository adminRepository;
    
    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    
    // Récupérer tous les admins
    public List<AdminDTO> getAllAdmins() {
        List<AdminDTO> adminDTOs = new ArrayList<>();
        adminRepository.findAll().forEach(admin -> {
            adminDTOs.add(convertToDTO(admin));
        });
        return adminDTOs;
    }
    
    // Récupérer un admin par ID
    public AdminDTO getAdminById(Integer id) {
        Optional<Admin> admin = adminRepository.findById(id);
        return admin.map(this::convertToDTO).orElse(null);
    }
    
    // Ajouter un nouvel admin
    @Transactional
    public AdminDTO addAdmin(String name, String email, String password) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(password);
        
        Admin savedAdmin = adminRepository.save(admin);
        return convertToDTO(savedAdmin);
    }
    
    // Mettre à jour un admin existant
    @Transactional
    public AdminDTO updateAdmin(Integer id, String name, String email, String password) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            if (name != null) admin.setName(name);
            if (email != null) admin.setEmail(email);
            if (password != null) admin.setPassword(password);
            
            Admin updatedAdmin = adminRepository.save(admin);
            return convertToDTO(updatedAdmin);
        }
        
        return null;
    }
    
    // Supprimer un admin
    @Transactional
    public boolean deleteAdmin(Integer id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Convertir un Admin en AdminDTO
    private AdminDTO convertToDTO(Admin admin) {
        return new AdminDTO(
            admin.getId(),
            admin.getName(),
            admin.getEmail()
        );
    }
} 