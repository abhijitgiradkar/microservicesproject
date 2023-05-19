package com.microdervice.orderservice.dto;

import com.microdervice.orderservice.model.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    List<OrderLineItemsDto> orderLineItemsDtosList;
}
