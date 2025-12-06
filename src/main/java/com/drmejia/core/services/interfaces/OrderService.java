package com.drmejia.core.services.interfaces;

import java.util.List;

import com.drmejia.core.exceptions.BadRequestException;
import com.drmejia.core.models.Order;

public interface OrderService {
    
    void saveOrder(Order order);
    List<Order> getAllOrders();
    void modifyOrder(Order order) throws BadRequestException;
    void updateOrder(Order order) throws BadRequestException;
    void deleteOrder(Long idOrder);
}
