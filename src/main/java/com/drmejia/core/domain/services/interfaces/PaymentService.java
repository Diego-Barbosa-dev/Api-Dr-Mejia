package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.drmejia.core.domain.models.Payment;

public interface PaymentService {

    void savePayment(Payment payment);
    List<Payment> getAllPayments();
    void modifyPayment(Payment payment) throws BadRequestException;
    void updatePayment(Payment payment) throws BadRequestException;
    void deletePayment(Long idPayment);
    
}
