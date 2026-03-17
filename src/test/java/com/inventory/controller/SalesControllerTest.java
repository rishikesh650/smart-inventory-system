package com.inventory.controller;

import com.inventory.entity.PaymentMethod;
import com.inventory.entity.PaymentStatus;
import com.inventory.service.ProductService;
import com.inventory.service.SalesService;
import com.inventory.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SalesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SalesService salesService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private SalesController salesController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(salesController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testRecordSale_ValidRequest() throws Exception {
        mockMvc.perform(post("/sales/record")
                .param("productId", "1")
                .param("quantity", "5")
                .param("paymentMethod", "CARD")
                .param("paymentStatus", "COMPLETED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

        verify(salesService).recordSale(1L, 5, PaymentMethod.CARD, PaymentStatus.COMPLETED);
    }

    @Test
    void testRecordSale_MissingPaymentMethod_ShouldRouteToError() throws Exception {
        // Missing the paymentMethod param, meaning Spring throws MissingServletRequestParameterException
        // The GlobalExceptionHandler catches it and returns the 'error' view
        mockMvc.perform(post("/sales/record")
                .param("productId", "1")
                .param("quantity", "5")
                .param("paymentStatus", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void testRecordSale_InvalidEnumString_ShouldRouteToError() throws Exception {
        // Invalid Enum String throws MethodArgumentTypeMismatchException
        mockMvc.perform(post("/sales/record")
                .param("productId", "1")
                .param("quantity", "5")
                .param("paymentMethod", "DOGE_COIN") // Invalid enum
                .param("paymentStatus", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"));
    }
}
