package com.drmejia.core.domain.services.interfaces;

import java.util.List;

import com.drmejia.core.domain.models.Order;

public interface OrderService {
    
    void saveOrder(Order order);
    List<Order> getAllOrders();
    void modifyOrder(Order order);
    void deleteOrder(Long idOrder);
}
