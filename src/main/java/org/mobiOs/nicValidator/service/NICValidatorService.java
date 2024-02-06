package org.mobiOs.nicValidator.service;

import jakarta.servlet.http.HttpServletResponse;
import org.mobiOs.nicValidator.dao.NICValidatorEntity;
import org.mobiOs.nicValidator.dto.NICValidatorDTO;
import org.mobiOs.nicValidator.util.DashboardResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface NICValidatorService {
    Set<NICValidatorDTO> saveNIC(MultipartFile[] file) throws IOException;

    List<NICValidatorEntity> findAll();

    void exportAsPDF(HttpServletResponse response) throws IOException;

    void exportAsExcel(HttpServletResponse response) throws IOException;

    Set<NICValidatorDTO> getAllDetails();

    DashboardResponse getDashBoardDetails();
}
