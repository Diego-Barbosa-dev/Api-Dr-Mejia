package com.drmejia.core.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.models.Order;
import com.drmejia.core.persistence.entities.OrderEntity;
import com.drmejia.core.persistence.repository.HeadquaterRepository;
import com.drmejia.core.persistence.repository.OrderRepository;
import com.drmejia.core.persistence.repository.PatientRepository;
import com.drmejia.core.persistence.repository.ProviderRepository;
import com.drmejia.core.services.interfaces.OrderService;

import lombok.NonNull;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private HeadquaterRepository headquaterRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired 
    private ProviderRepository providerRepository;

    private ResourceNotFoundException nonExistingOrder(){
        return new ResourceNotFoundException("Order Not Found In DataBase");
    }
        private ResourceNotFoundException nonExistingPatient(){
        return new ResourceNotFoundException("Patient Not Found In DataBase");

    }

    private ResourceNotFoundException nonExistingHeadquartet(){
        return new ResourceNotFoundException("Headquartert Not Found In DataBase");
    }

    private ResourceNotFoundException nonExistingProvider(){
        return new ResourceNotFoundException("Provider Not Found In DataBase");
    }

    /*
     *************************************************************
     *                   CRUD METHODS
     *************************************************************
     */



    /* CREATE METHOD */
    @Override
    public void saveOrder(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setNumber(order.getNumber());
        orderEntity.setPatient(patientRepository.findByDocument(order.getDocumentPatient()).orElseThrow(this::nonExistingPatient));
        orderEntity.setHeadquarters(headquaterRepository.findById(order.getIdHeadquarter()).orElseThrow(this::nonExistingHeadquartet));
        orderEntity.setProvider(providerRepository.findById(order.getIdProvider()).orElseThrow(this::nonExistingProvider));
        orderEntity.setShippingDate(order.getShippingDate());
        orderEntity.setDeliveryDate(order.getDeliveryDate());
        orderEntity.setDaysPassed(order.getDaysPassed());
        orderEntity.setState(order.getState());

        orderRepository.save(orderEntity);
    }



    /* READ METHOD */
    @Override
    public List<Order> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        List<Order> orders = new ArrayList<>();

        for (OrderEntity orderEntity : orderEntities){
            Order order = new Order();
            order.setNumber(orderEntity.getNumber());
            order.setDocumentPatient(orderEntity.getPatient().getDocument());
            order.setIdHeadquarter(orderEntity.getHeadquarters().getIdHeadquarters());
            order.setIdProvider(orderEntity.getProvider().getIdProvider());
            order.setShippingDate(orderEntity.getShippingDate());
            order.setDeliveryDate(orderEntity.getDeliveryDate());
            order.setDaysPassed(orderEntity.getDaysPassed());
            order.setState(orderEntity.getState());

            orders.add(order);
        }
        return orders;
    }



    /* UPDATE METHODS */
    @Override
    public void modifyOrder(Order order) throws BadRequestException {
        /* PATCH HTTP METHOD */
        if(order.getIdOrder() == null){
            throw new BadRequestException("Order Id Can't Be Null");
        }
        OrderEntity orderEntity = orderRepository.findById(order.getIdOrder()).orElseThrow(this::nonExistingOrder);

        if(order.getNumber() != null && !order.getNumber().isBlank()){
            orderEntity.setNumber(order.getNumber());
        }
        if(order.getDocumentPatient() != null && !order.getDocumentPatient().isBlank()){
            orderEntity.setPatient(patientRepository.findByDocument(order.getDocumentPatient()).orElseThrow(this::nonExistingPatient));
        }
        if(order.getIdHeadquarter() != null){
            orderEntity.setHeadquarters(headquaterRepository.findById(order.getIdHeadquarter()).orElseThrow(this::nonExistingHeadquartet));
        }
        if(order.getIdProvider() != null){
            orderEntity.setProvider(providerRepository.findById(order.getIdProvider()).orElseThrow(this::nonExistingProvider));
        }
        if(order.getShippingDate() != null){
            orderEntity.setShippingDate(order.getShippingDate());
        }
        if(order.getDeliveryDate() != null){
            orderEntity.setDeliveryDate(order.getDeliveryDate());
        }
        if(order.getDaysPassed() != null){
            orderEntity.setDaysPassed(order.getDaysPassed());
        }
        if(order.getState() != null){
            orderEntity.setState(order.getState());
        }
        
        orderRepository.save(orderEntity);
    }

    @Override
    public void updateOrder(Order order) throws BadRequestException {
        /* PUT HTTP METHOD */
        if(order.getIdOrder() == null){
            throw new BadRequestException("Order Id Can't Be Null");
        }
        if(order.getNumber() == null || order.getNumber().isBlank()){
            throw new BadRequestException("Order Number Can't Be Null Or Blank");
        }
        if(order.getDocumentPatient() == null || order.getDocumentPatient().isBlank()){
            throw new BadRequestException("Order Document Patient Can't Be Null Or Blank");
        }
        if(order.getIdHeadquarter() == null){
            throw new BadRequestException("Order Headquarter Can't Be Null");
        }
        if(order.getIdProvider() == null){
            throw new BadRequestException("Order Provider Can't Be Null");
        }
        if(order.getShippingDate() == null){
            throw new BadRequestException("Order Shipping Date Can't Be Null");
        }
        if(order.getDeliveryDate() == null){
            throw new BadRequestException("Order Delivery Date Can't Be Null");
        }
        if(order.getDaysPassed() == null){
            throw new BadRequestException("Order Days Passed Can't Be Null");
        }
        if(order.getState() == null){
            throw new BadRequestException("Order State Can't Be Null");
        }

        OrderEntity orderEntity = orderRepository.findById(order.getIdOrder()).orElseThrow(this::nonExistingOrder);

        orderEntity.setNumber(order.getNumber());
        orderEntity.setPatient(patientRepository.findByDocument(order.getDocumentPatient()).orElseThrow(this::nonExistingPatient));
        orderEntity.setHeadquarters(headquaterRepository.findById(order.getIdHeadquarter()).orElseThrow(this::nonExistingHeadquartet));
        orderEntity.setProvider(providerRepository.findById(order.getIdProvider()).orElseThrow(this::nonExistingProvider));
        orderEntity.setShippingDate(order.getShippingDate());
        orderEntity.setDeliveryDate(order.getDeliveryDate());
        orderEntity.setDaysPassed(order.getDaysPassed());
        orderEntity.setState(order.getState());
        
        orderRepository.save(orderEntity);
    }

    /* DELETE METHOD */
    @Override
    public void deleteOrder(@NonNull Long idOrder) {
        if (!orderRepository.existsById(idOrder)) {
            throw nonExistingOrder();
        }
        orderRepository.deleteById(idOrder);
    }
    
}
