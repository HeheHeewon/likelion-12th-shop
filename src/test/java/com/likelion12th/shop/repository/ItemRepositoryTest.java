package com.likelion12th.shop.repository;

import com.likelion12th.shop.constant.ItemSellStatus;
import com.likelion12th.shop.entity.Item;
import com.likelion12th.shop.entity.QItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import com.likelion12th.shop.Dto.MemberFormDto;
import com.likelion12th.shop.constant.Role;
import com.likelion12th.shop.entity.Member;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")

public class ItemRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;


    @Autowired
    public ItemRepositoryTest(ItemRepository itemRepository, OrderRepository orderRepository, MemberRepository memberRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
    }

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
            Item item = new Item();
            item.setItemName("테스트 상품");
            item.setPrice(10000);
            item.setItemDetail("테스트 상품 상세 설명");
            item.setItemSellStatus(ItemSellStatus.Sell);
            item.setStock(100);
            item.setCreatedBy(LocalDateTime.now());
            item.setModifiedBy(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
            System.out.println(savedItem.toString());
    }

    private List<Item> createItemList() {
        List<Item> itemList = new ArrayList<>(); // itemList 변수 선언 및 초기화

        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.Sell);
            item.setStock(100);
            item.setCreatedBy(LocalDateTime.now());
            item.setModifiedBy(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
            itemList.add(savedItem);
        }
        return itemList;
    }


    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemName("테스트 상품1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

   @Test
   @DisplayName("가격 내림차순 조회 테스트")
   public void findByPriceLessThanOrderByPriceDescTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
        System.out.println(item.toString());
    }
}


    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList=itemRepository.findByItemDetail("%테스트 상품 상세 설명%");
        for(Item item:itemList)System.out.println(item.toString());
    }

    @Test
    @DisplayName("Querydsl 조회 테스트")
    public void queryDslTest() {
        List<Item> itemList = this.createItemList(); // createItemList() 메서드 호출하여 아이템 리스트 생성
        Item firstItem = itemList.get(4); // 리스트에서 첫 번째 아이템 선택

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        ItemSellStatus sellStatus = ItemSellStatus.Sell; // SellStatus를 ItemSellStatus 열거형 타입으로 초기화

        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(sellStatus)) // 초기화된 sellStatus를 사용하여 비교
                .where(qItem.itemDetail.like("%" + firstItem.getItemDetail() + "%"))
                .orderBy(qItem.price.desc());

        List<Item> resultItemList = query.fetch();
        for (Item item : resultItemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    public void testAuditing() {
        // Given
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("John Doe");
        memberFormDto.setEmail("john@example.com");
        memberFormDto.setPassword("password123");
        memberFormDto.setAddress("123 Street, City");

        // When
        Member member = Member.createMember(memberFormDto);
        member.setRole(Role.USER); // Optional: Set the role if needed
        Member savedMember = memberRepository.save(member);

        // Then
        assertNotNull(savedMember.getId());
        assertNotNull(savedMember.getRegTime());
        assertNotNull(savedMember.getUpdateTime());
        // 이후 필요한 필드에 대한 검증을 수행할 수 있습니다.
    }
}