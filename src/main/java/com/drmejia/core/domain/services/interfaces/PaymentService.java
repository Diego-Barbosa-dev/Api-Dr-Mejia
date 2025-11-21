package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Payment;

public interface PaymentService {

    void savePayment(Payment payment);
    List<Payment> getAllPayments();
    void modifyPayment(Payment payment);
    void deletePayment(Long idPayment);
    
}
