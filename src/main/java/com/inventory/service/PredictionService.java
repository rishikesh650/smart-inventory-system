package com.inventory.service;

import com.inventory.entity.Product;
import com.inventory.entity.Sale;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SaleRepository;
import com.inventory.dto.PredictionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PredictionService {
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<PredictionDTO> getPredictions() {
        List<Product> products = productRepository.findAll();
        List<PredictionDTO> predictions = new ArrayList<>();
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        for (Product product : products) {
            List<Sale> allSales = saleRepository.findAll(); // Optimization: should use custom query

            double last30DaysUsage = allSales.stream()
                    .filter(s -> s.getProduct() != null && s.getProduct().getId().equals(product.getId()))
                    .filter(s -> s.getSaleDate() != null && s.getSaleDate().isAfter(thirtyDaysAgo))
                    .mapToInt(Sale::getQuantity).sum() / 30.0;

            double last7DaysUsage = allSales.stream()
                    .filter(s -> s.getProduct() != null && s.getProduct().getId().equals(product.getId()))
                    .filter(s -> s.getSaleDate() != null && s.getSaleDate().isAfter(sevenDaysAgo))
                    .mapToInt(Sale::getQuantity).sum() / 7.0;

            // Weighted usage: 70% last 7 days, 30% last 30 days
            double dailyUsage = (last7DaysUsage * 0.7) + (last30DaysUsage * 0.3);

            int currentStock = product.getInventory() != null ? product.getInventory().getCurrentStock() : 0;
            int daysLeft = dailyUsage > 0 ? (int) (currentStock / dailyUsage) : 999;

            PredictionDTO dto = new PredictionDTO();
            dto.setProductName(product.getName());
            dto.setCurrentStock(currentStock);
            dto.setDailyUsage(dailyUsage);
            dto.setEstimatedDaysLeft(daysLeft);

            // Enterprise logic: Check against reorder threshold and lead time
            if (currentStock <= product.getMinStockLevel() || daysLeft < product.getLeadTimeDays()) {
                dto.setAlertLevel("CRITICAL");
            } else if (currentStock <= product.getReorderThreshold() || daysLeft < 7) {
                dto.setAlertLevel("WARNING");
            } else if (currentStock >= product.getMaxStockLevel()) {
                dto.setAlertLevel("OVERSTOCK");
            } else {
                dto.setAlertLevel("NORMAL");
            }

            predictions.add(dto);
        }
        return predictions;
    }
}
