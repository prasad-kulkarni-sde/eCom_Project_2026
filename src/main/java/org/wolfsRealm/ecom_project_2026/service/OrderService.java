package org.wolfsRealm.ecom_project_2026.service;

import jakarta.transaction.Transactional;
import org.wolfsRealm.ecom_project_2026.payload.OrderDTO;

public interface OrderService {
    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
