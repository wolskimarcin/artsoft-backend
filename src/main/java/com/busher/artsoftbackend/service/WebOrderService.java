package com.busher.artsoftbackend.service;

import com.busher.artsoftbackend.dao.WebOrderRepository;
import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.model.WebOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebOrderService {

    private final WebOrderRepository repository;

    public WebOrderService(WebOrderRepository repository) {
        this.repository = repository;
    }

    public List<WebOrder> getWebOrders(LocalUser user) {
        return repository.findByUser(user);
    }

}