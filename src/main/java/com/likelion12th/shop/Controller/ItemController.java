package com.likelion12th.shop.Controller;

import com.likelion12th.shop.Dto.ItemFormDto;
import com.likelion12th.shop.Service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/new")
    public ResponseEntity<Long> createItem(@RequestPart(name = "itemFormDto") ItemFormDto itemFormDto,
                                           @RequestPart(value = "itemImg", required = false) MultipartFile itemImg) {
        if (itemImg == null) {
            try {
                // 새로운 아이템 저장
                Long itemId = itemService.saveItem(itemFormDto);

                // 저장된 아이템의 아이디 변환
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                // 예외 발생 시 처리
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            try {
                // 새로운 아이템 저장
                Long itemId = itemService.saveItem(itemFormDto, itemImg);

                // 저장된 아이템의 아이디 변환
                return ResponseEntity.status(HttpStatus.CREATED).body(itemId);
            } catch (Exception e) {
                // 예외 발생 시 처리
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    // 전체 상품 조회
    @GetMapping
    public ResponseEntity<List<ItemFormDto>> getItems() {
        return ResponseEntity.ok().body(itemService.getItems());

    }

    // 특정 상품 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemFormDto> getItemById(@PathVariable Long itemId) {
        try {
            // 아이템 ID를 사용하여 특정 상품 조회
            ItemFormDto itemFormDto = itemService.getItemById(itemId);
            // 조회한 상품 정보 변환
            return ResponseEntity.ok().body(itemFormDto);
        } catch (HttpClientErrorException e) {
            // 아이템을 찾지 못한 경우 404 에러 반환
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            // 그 외 예외 발생 시 500 에러 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}