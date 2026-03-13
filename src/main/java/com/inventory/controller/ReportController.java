package com.inventory.controller;

import com.inventory.dto.PredictionDTO;
import com.inventory.service.PredictionService;
import com.inventory.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private PredictionService predictionService;

    @GetMapping("/inventory/export")
    public ResponseEntity<byte[]> exportInventoryReport() {
        List<PredictionDTO> predictions = predictionService.getPredictions();
        byte[] pdfContent = reportService.generateInventoryReportPdf(predictions);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }
}
