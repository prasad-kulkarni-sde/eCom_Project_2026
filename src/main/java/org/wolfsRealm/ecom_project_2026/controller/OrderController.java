package org.wolfsRealm.ecom_project_2026.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wolfsRealm.ecom_project_2026.payload.OrderDTO;
import org.wolfsRealm.ecom_project_2026.payload.OrderRequestDTO;
import org.wolfsRealm.ecom_project_2026.service.OrderService;
import org.wolfsRealm.ecom_project_2026.util.AuthUtil;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO>orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO){
        String emailId= authUtil.loggedInEmail();
        OrderDTO orderDTO =     orderService.placeOrder(emailId, orderRequestDTO.getAddressId(), paymentMethod, orderRequestDTO.getPgName(),orderRequestDTO.getPgPaymentId(),orderRequestDTO.getPgStatus(),orderRequestDTO.getPgResponseMessage());

        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
