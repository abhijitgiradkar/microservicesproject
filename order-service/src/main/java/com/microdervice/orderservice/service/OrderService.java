package com.microdervice.orderservice.service;

import com.microdervice.orderservice.dto.InventoryResponse;
import com.microdervice.orderservice.dto.OrderLineItemsDto;
import com.microdervice.orderservice.dto.OrderRequest;
import com.microdervice.orderservice.model.Order;
import com.microdervice.orderservice.model.OrderLineItems;
import com.microdervice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtosList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
                .collect(Collectors.toList());
        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodeList = order.getOrderLineItemsList()
                .stream()
                .map(orderLineItemsDto -> orderLineItemsDto.getSkuCode())
                .collect(Collectors.toList());

        //Check the inventory first then save the order
        InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode",skuCodeList).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(inventoryResponse -> inventoryResponse.getIsInStock());
        if(allProductsInStock) {
            orderRepository.save(order);
        }
        else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setId(orderLineItemsDto.getId());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        return orderLineItems;
    }

}
