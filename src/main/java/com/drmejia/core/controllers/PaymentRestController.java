package com.drmejia.core.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.models.Payment;
import com.drmejia.core.services.interfaces.PaymentService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/payments")
public class PaymentRestController {
    
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<?> getMethodName() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return paymentService.getAllPayments().stream()
            .filter(payment -> payment.getIdPayment().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Payment " + id + "Not Found"));
    }
    
    @PostMapping
    public ResponseEntity<?> postMethodName(@RequestBody Payment payment) throws BadRequestException{
        
        if(payment.hasNullAttributes()){
            throw new BadRequestException("Null Attributes Are Not Allowed"
                + "\nId: " + payment.getIdPayment()
                + "\nOrder: " + payment.getIdOrder()
                + "\nAmount: " + payment.getAmount()
                + "\nDate: " + payment.getPaymentDate()
            );
        }

        paymentService.savePayment(payment);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(payment.getIdOrder())
            .toUri();
        
        return ResponseEntity.created(location).body(payment);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> putMethodName(@PathVariable("id") Long id, @RequestBody Payment payment) {
        payment.setIdPayment(id);

        try {
            paymentService.updatePayment(payment);
        } catch (org.apache.coyote.BadRequestException | BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(payment);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchPayment(@PathVariable("id") Long id, @RequestBody Payment payment){
        payment.setIdPayment(id);
        try {
            paymentService.modifyPayment(payment);
        } catch (org.apache.coyote.BadRequestException | BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable("id") Long id){
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
