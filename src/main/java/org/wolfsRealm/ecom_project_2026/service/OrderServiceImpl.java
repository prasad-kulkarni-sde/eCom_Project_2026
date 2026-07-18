package org.wolfsRealm.ecom_project_2026.service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wolfsRealm.ecom_project_2026.exceptions.APIException;
import org.wolfsRealm.ecom_project_2026.exceptions.ResourceNotFoundException;
import org.wolfsRealm.ecom_project_2026.model.*;
import org.wolfsRealm.ecom_project_2026.payload.*;
import org.wolfsRealm.ecom_project_2026.repositories.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        Cart cart= cartRepository.findCartByEmail(emailId);
        if (cart==null)throw new ResourceNotFoundException("Cart","email",emailId);

        Address address= addressRepository.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address","addressId",addressId));



        Order order = new Order();
        order.setEmail(emailId);
        order.setAddress(address);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !!");

        Payment payment= new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);
        payment.setOrder(order);
        paymentRepository.save(payment);

        order.setPayment(payment);

        order = orderRepository.save(order);

        List<CartItem> cartItemList= cart.getCartItems();
        if (cartItemList.isEmpty())throw new APIException("Cart is Empty !!");

        List<OrderItem>orderItemList= new ArrayList<>();

        for(CartItem cartItem:cartItemList){
            OrderItem orderItem= new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        }
        orderItemList= orderItemRepository.saveAll(orderItemList);

        cart.getCartItems().forEach(item->{
            int qunatity= item.getQuantity();
            Product product= item.getProduct();
            product.setQuantity(product.getQuantity()- qunatity);
            productRepository.save(product);

            cartService.deleteProductFromCart(cart.getCartId(),item.getProduct().getProductId());
        });

        OrderDTO orderDTO= modelMapper.map(order,OrderDTO.class);
        List<OrderItemDTO>OrderItemDTOList= new ArrayList<>();
        orderItemList.forEach(item -> {
            OrderItemDTO itemDTO = modelMapper.map(item, OrderItemDTO.class);
            itemDTO.setProductDTO(modelMapper.map(item.getProduct(), ProductDTO.class)); // <-- add this
            OrderItemDTOList.add(itemDTO);
        });
        orderDTO.setOrderItems(OrderItemDTOList);
        orderDTO.setPaymentDTO(modelMapper.map(payment, PaymentDTO.class));  // <-- add this
        orderDTO.setAddressId(addressId);

        return orderDTO;


    }
}
