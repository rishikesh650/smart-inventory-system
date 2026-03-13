package com.inventory.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        logger.error("DIAGNOSTIC - Global Error caught: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error",
                "DIAGNOSTIC ERROR -> Class: " + ex.getClass().getName() + " | Message: " + ex.getMessage());
        return mav;
    }
}
