package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Order;
import com.drmejia.core.exceptions.BadRequestException;

public interface OrderService {
    
    void saveOrder(Order order);
    List<Order> getAllOrders();
    void modifyOrder(Order order) throws BadRequestException;
    void updateOrder(Order order) throws BadRequestException;
    void deleteOrder(Long idOrder);
}
