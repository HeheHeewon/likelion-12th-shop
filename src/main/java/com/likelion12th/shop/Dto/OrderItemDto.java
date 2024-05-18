package com.likelion12th.shop.Dto;

import com.likelion12th.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class OrderItemDto {
    private Long itemId;
    private String itemName;
    private Integer itemPrice;
    private Integer count;
    private int totalPrice;

    private static ModelMapper modelMapper = new ModelMapper();

    //orderItem 객체를 orderItemDto로 변환
    public static OrderItemDto of(OrderItem orderItem) {
        OrderItemDto orderItemDto = modelMapper.map(orderItem, OrderItemDto.class);
        return orderItemDto;
    }
}