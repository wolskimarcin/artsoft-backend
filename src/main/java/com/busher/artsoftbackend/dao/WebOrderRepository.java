package com.busher.artsoftbackend.dao;

import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.model.WebOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebOrderRepository extends JpaRepository<WebOrder, Long> {

    List<WebOrder> findByUser(LocalUser user);

}
