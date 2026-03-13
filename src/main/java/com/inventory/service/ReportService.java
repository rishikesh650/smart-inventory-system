package com.inventory.service;

import com.inventory.entity.PurchaseOrder;
import com.inventory.entity.PurchaseOrderItem;
import com.inventory.dto.PredictionDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportService {

    public byte[] generatePurchaseOrderPdf(PurchaseOrder order) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("PURCHASE ORDER #" + order.getId(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Supplier: " + order.getSupplier().getName()));
            document.add(new Paragraph("Date: " + order.getOrderDate()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Product");
            table.addCell("Quantity");
            table.addCell("Unit Price");
            table.addCell("Total");

            for (PurchaseOrderItem item : order.getItems()) {
                table.addCell(item.getProduct().getName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(item.getUnitPrice().toString());
                table.addCell(
                        item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())).toString());
            }
            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Amount: " + order.getTotalAmount()));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public byte[] generateInventoryReportPdf(List<PredictionDTO> predictions) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            Paragraph title = new Paragraph("Inventory Intelligence Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Generated: " + java.time.LocalDateTime.now()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Product");
            table.addCell("Current Stock");
            table.addCell("Estimated Days Left");
            table.addCell("Alert Level");

            for (PredictionDTO p : predictions) {
                table.addCell(p.getProductName());
                table.addCell(String.valueOf(p.getCurrentStock()));
                table.addCell(String.valueOf(p.getEstimatedDaysLeft()));
                table.addCell(p.getAlertLevel());
            }
            document.add(table);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Inventory PDF", e);
        }
    }
}
