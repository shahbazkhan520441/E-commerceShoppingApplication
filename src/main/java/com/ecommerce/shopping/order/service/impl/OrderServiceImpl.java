package com.ecommerce.shopping.order.service.impl;

import com.ecommerce.shopping.address.entity.Address;
import com.ecommerce.shopping.address.repository.AddressRepository;
import com.ecommerce.shopping.config.RestTemplateProvider;
import com.ecommerce.shopping.contact.entity.Contact;
import com.ecommerce.shopping.customer.entity.Customer;
import com.ecommerce.shopping.customer.repository.CustomerRepository;
import com.ecommerce.shopping.exception.AddressNotExistException;
import com.ecommerce.shopping.exception.CartProductNotExistException;
import com.ecommerce.shopping.exception.CustomerNotExistException;
import com.ecommerce.shopping.order.dto.*;
import com.ecommerce.shopping.order.entity.Order;
import com.ecommerce.shopping.order.mapper.OrderMapper;
import com.ecommerce.shopping.order.repository.OrderRepository;
import com.ecommerce.shopping.order.service.OrderService;
import com.ecommerce.shopping.product.entity.Product;
import com.ecommerce.shopping.product.repository.ProductRepository;
import com.ecommerce.shopping.utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final RestTemplateProvider restTemplateProvider;

    @Override
    public ResponseEntity<ResponseStructure<OrderResponseDto>> generatePurchaseOrder(
            OrderRequest orderRequest,
            Long productId,
            Long customerId,
            Long addressId) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotExistException("Customer Id : " + customerId + ", is not exist"));

        Address address = addressRepository
                .findById(addressId)
                .orElseThrow(() -> new AddressNotExistException("Address Id : " + addressId + ". not exist"));

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new CartProductNotExistException("Product id : " + productId + ", is not exist"));

        Order order = orderMapper.mapOrderRequestDtoToOrder(orderRequest, new Order());
        order.setProduct(product);
        order.setAddress(address);
        order = orderRepository.save(order);

        List<Contact> contacts = address.getContacts();
        Contact contact1 = contacts.getFirst();
        Contact contact2 = contacts.getLast();

        AddressDto addressDto = AddressDto.builder()
                .addressType(address.getAddressType())
                .streetAddress(address.getStreetAddress())
                .streetAddressAdditional(address.getStreetAddressAdditional())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .pincode(address.getPincode())
                .contactNumber1(contact1.getPriority() + " : " + contact1.getContactNumber())
                .contactNumber2(contact2.getPriority() + " : " + contact2.getContactNumber())
                .build();

        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .orderId(order.getOrderId())
                .customerId(customerId)
                .totalQuantity(orderRequest.getTotalQuantity())
                .totalPrice(orderRequest.getTotalPrice())
                .discountPrice(orderRequest.getDiscountPrice())
                .totalPayableAmount(orderRequest.getTotalPayableAmount())
                .addressDto(addressDto)
                .build();
        return restTemplateProvider.generatePurchaseOrder(orderRequestDto, productId);
    }
//---------------------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------------------------------------------------------

}
