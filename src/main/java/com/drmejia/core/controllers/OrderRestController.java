package com.drmejia.core.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drmejia.core.domain.models.Order;
import com.drmejia.core.domain.services.interfaces.OrderService;
import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/orders")
public class OrderRestController {
    
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return orderService.getAllOrders().stream()
            .filter(order -> order.getIdOrder().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Order: " + id + " Not Found"));
    }

    @PostMapping
    public ResponseEntity<?> postOrder(@RequestBody Order order) {
        if (order.hasNullAttributes()) {
            throw new BadRequestException("Null Attributes Are Not Allowed"
                + "\nId: " + order.getIdOrder()
                + "\nNumber: " + order.getNumber()
                + "\nDocument Patient: " + order.getDocumentPatient()
                + "\nHeadquarter: " + order.getIdHeadquarter()
                + "\nProvider: " + order.getIdProvider()
                + "\nShipping Date: " + order.getShippingDate()
                + "\nDelivery Date: " + order.getDeliveryDate()
                + "\nDays Passed: " + order.getDaysPassed()
            );
        }

        if (order.getNumber().length() < 3) {
            throw new BadRequestException("Order Number Must Have At Least 3 Characters");
        }

        if (order.getDocumentPatient().length() < 3) {
            throw new BadRequestException("Document Patient Must Have At Least 3 Characters");
        }

        orderService.saveOrder(order);
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(order.getIdOrder())
            .toUri();
        return ResponseEntity.created(location).body(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putOrder(@PathVariable("id") Long id, @RequestBody Order order) {
        order.setIdOrder(id);
        try {
            orderService.updateOrder(order);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchOrder(@PathVariable("id") Long id, @RequestBody Order order) {
        order.setIdOrder(id);
        try {
            orderService.modifyOrder(order);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}