package com.drmejia.core.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.models.Payment;
import com.drmejia.core.persistence.entities.PaymentEntity;
import com.drmejia.core.persistence.repository.OrderRepository;
import com.drmejia.core.persistence.repository.PaymentRepository;
import com.drmejia.core.services.interfaces.PaymentService;

import lombok.NonNull;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

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
        paymentEntity.setAmount(payment.getAmount());
        paymentEntity.setVoucher(payment.getVoucher());
        paymentEntity.setOrder(orderRepository.findById(payment.getIdOrder()).orElseThrow(this::nonExistingPayment));
        paymentRepository.save(paymentEntity);
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
            payment.setAmount(paymentEntity.getAmount());
            payment.setVoucher(paymentEntity.getVoucher());
            payments.add(payment);
        }
        return payments;
    }

    /* UPDATE METHODS */
    @Override
    public void modifyPayment(Payment payment) throws BadRequestException {
        /* PATCH HTTP METHOD */
        if(payment.getIdPayment() == null){
            throw new BadRequestException("Payment Id Can't be Null");
        }
        PaymentEntity paymentEntity = paymentRepository.findById(payment.getIdPayment()).orElseThrow(this::nonExistingPayment);
        if(payment.getPaymentDate() != null){
            paymentEntity.setPaymentDate(payment.getPaymentDate());
        }
        if(payment.getIdOrder() != null){
            paymentEntity.setOrder(orderRepository.findById(payment.getIdOrder()).orElseThrow(()-> new BadRequestException("Order Not Found")));
        }
        if(payment.getAmount() != null){
            paymentEntity.setAmount(payment.getAmount());
        }
        if(payment.getVoucher() != null){
            paymentEntity.setVoucher(payment.getVoucher());
        }
        if(paymentEntity == null){
           throw nonExistingPayment();
        }
        paymentRepository.save(paymentEntity);
    }

    @Override
    public void updatePayment(Payment payment) throws BadRequestException{
        /* PUT HTTP Method */
        if(payment.getIdPayment() == null){
            throw nonExistingPayment();
        }

        if(payment.getIdOrder() == null){
            throw new BadRequestException("Payment Order Can't Be Null");
        }
        
        if(payment.getPaymentDate() == null){
            throw new BadRequestException("Payment Date Can't Be Null");
        }
        if(payment.getAmount() == null){
            throw new BadRequestException("Payment Amount Can't Be Null");
        }

        PaymentEntity paymentEntity = paymentRepository.findById(payment.getIdPayment()).orElseThrow(this::nonExistingPayment);
        
        paymentEntity.setPaymentDate(payment.getPaymentDate());
        paymentEntity.setAmount(payment.getAmount());
        paymentEntity.setVoucher(payment.getVoucher());
        paymentEntity.setOrder(orderRepository.findById(payment.getIdOrder()).orElseThrow(() -> new BadRequestException("Order Not Found")));
        paymentRepository.save(paymentEntity);
    } 

    /* DELETE METHOD */
    @Override
    public void deletePayment(@NonNull Long idPayment) {
        if (!paymentRepository.existsById(idPayment)) {
            throw nonExistingPayment();
        }
        paymentRepository.deleteById(idPayment);
    }
    
}
