package com.drmejia.core.domain.services.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Payment;
import com.drmejia.core.domain.services.interfaces.PaymentService;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.PaymentEntity;
import com.drmejia.core.persistence.repository.ComprobantRepository;
import com.drmejia.core.persistence.repository.OrderRepository;
import com.drmejia.core.persistence.repository.PaymentRepository;

import lombok.NonNull;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ComprobantRepository comprobantRepository;

    @Autowired
    private OrderRepository orderRepository;

    private ResourceNotFoundException nonExistingPayment(){
        return new ResourceNotFoundException("Payment Not Found In DataBase");
    }

    /* 
     ************************************************************
     *                    CRUD METHODS
     ************************************************************
     */


    /* CREATE METHOD */
    @Override
    public void savePayment(Payment payment) {
        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setPaymentDate(payment.getPaymentDate());
        paymentEntity.setOrder(orderRepository.findById(payment.getIdOrder()).orElseThrow(this::nonExistingPayment));
        paymentEntity.setComprobant(comprobantRepository.findById(payment.getIdComprobant()).orElseThrow(this::nonExistingPayment));
    }

    /* READ METHOD */
    @Override
    public List<Payment> getAllPayments() {
        List<PaymentEntity> paymentEntities = paymentRepository.findAll();
        List<Payment> payments = new ArrayList<>();

        for(PaymentEntity paymentEntity : paymentEntities){
            Payment payment = new Payment();
            payment.setIdPayment(paymentEntity.getIdPayment());
            payment.setPaymentDate(paymentEntity.getPaymentDate());
            payment.setIdOrder(paymentEntity.getOrder().getIdOrder());
            payment.setIdComprobant(paymentEntity.getComprobant().getIdComprobant());
        }
        return payments;
    }

    /* UPDATE METHODS */
    @Override
    public void modifyPayment(Payment payment) {
        PaymentEntity paymentEntity = paymentRepository.findById(payment.getIdPayment()).orElseThrow(this::nonExistingPayment);

        paymentEntity.setPaymentDate(payment.getPaymentDate());
        paymentEntity.setOrder(orderRepository.findById(payment.getIdOrder()).orElseThrow(this::nonExistingPayment));
        paymentEntity.setComprobant(comprobantRepository.findById(payment.getIdComprobant()).orElseThrow(this::nonExistingPayment));
        
    }

    public void modifyPaymentDate(@NonNull Long idPayment, @NonNull LocalDate date){
        PaymentEntity paymentEntity = paymentRepository.findById(idPayment).orElseThrow(this::nonExistingPayment);

        paymentEntity.setPaymentDate(date);

        paymentRepository.save(paymentEntity);

    }

    public void modifyPaymentOrder(@NonNull Long idPayment, @NonNull Long idOrder){
        PaymentEntity paymentEntity = paymentRepository.findById(idPayment).orElseThrow(this::nonExistingPayment);

        paymentEntity.setOrder(orderRepository.findById(idOrder).orElseThrow(this::nonExistingPayment));

        paymentRepository.save(paymentEntity);
    }

    public void modifyPaymentComprobant(@NonNull Long idPayment, @NonNull Long idComprobant){
        PaymentEntity paymentEntity = paymentRepository.findById(idPayment).orElseThrow(this::nonExistingPayment);

        paymentEntity.setComprobant(comprobantRepository.findById(idComprobant).orElseThrow(this::nonExistingPayment));
        
        paymentRepository.save(paymentEntity);
    }

    /* DELETE METHOD */
    @Override
    public void deletePayment(@NonNull Long idPayment) {
        paymentRepository.deleteById(idPayment);   
    }
    
}
