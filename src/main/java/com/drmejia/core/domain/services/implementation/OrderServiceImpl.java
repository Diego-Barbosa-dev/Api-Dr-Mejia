package com.drmejia.core.domain.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drmejia.core.domain.models.Order;
import com.drmejia.core.domain.services.interfaces.OrderService;
import com.drmejia.core.exceptions.ResourceNotFoundException;
import com.drmejia.core.persistence.entities.OrderEntity;
import com.drmejia.core.persistence.repository.HeadquaterRepository;
import com.drmejia.core.persistence.repository.OrderRepository;
import com.drmejia.core.persistence.repository.PatientRepository;
import com.drmejia.core.persistence.repository.ProviderRepository;

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

            orders.add(order);
        }
        return orders;
    }



    /* UPDATE METHODS */
    @Override
    public void modifyOrder(Order order) {
        OrderEntity orderEntity = orderRepository.findById(order.getIdOrder()).orElseThrow(this::nonExistingOrder);

        orderEntity.setNumber(order.getNumber());
        orderEntity.setPatient(patientRepository.findByDocument(order.getDocumentPatient()).orElseThrow(this::nonExistingPatient));
        orderEntity.setHeadquarters(headquaterRepository.findById(order.getIdHeadquarter()).orElseThrow(this::nonExistingHeadquartet));
        orderEntity.setProvider(providerRepository.findById(order.getIdProvider()).orElseThrow(this::nonExistingProvider));
        orderEntity.setShippingDate(order.getShippingDate());
        orderEntity.setDeliveryDate(order.getDeliveryDate());
        orderEntity.setDaysPassed(order.getDaysPassed());
    }


    public void modifyOrderNumber(@NonNull Long idOrder, @NonNull String number){
        OrderEntity orderEntity = orderRepository.findById(idOrder).orElseThrow(this::nonExistingOrder);
        orderEntity.setNumber(number);
    }

    public void modifyOrderPatient(@NonNull Long idOrder, @NonNull String documentPatient){
        OrderEntity orderEntity = orderRepository.findById(idOrder).orElseThrow(this::nonExistingOrder);
        orderEntity.setPatient(patientRepository.findByDocument(documentPatient).orElseThrow(this::nonExistingPatient));

    }


    /* DELETE METHOD */
    @Override
    public void deleteOrder(@NonNull Long idOrder) {
        orderRepository.deleteById(idOrder);
    }
    
}
