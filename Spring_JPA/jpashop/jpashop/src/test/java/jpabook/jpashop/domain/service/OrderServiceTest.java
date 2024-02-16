package jpabook.jpashop.domain.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.exception.NotEnoughStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    // 주문 생성
    @Test
    public void 주문(){
        //given
        Member member = createMember();
        Item item = createBook("노인과 바다",10000,10);
        int orderCount = 2;

        //when
        Long OrderId = orderService.order(member.getId(), item.getId(), orderCount);

        //Then
        Order getOrder = orderRepository.findOne(OrderId);

        assertEquals("주문 상태는 Order여야 합니다.", OrderStatus.ORDER,getOrder.getStatus());
        assertEquals("주문 상품 종류는 1개여야 한다.",1,getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000*2,getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8,item.getStockQuantity());


    }

    // 재고 수량 초과
    @Test(expected = NotEnoughStockException.class)
    public void 재고_수량_초과(){
        Member member = createMember();
        Item item = createBook("노인과 바다",10000,10);
        int orderCount = 11;

        //when
        Long OrderId = orderService.order(member.getId(), item.getId(), orderCount);

        fail("여기로 오면 실패!");
    }

    // 주문 취소
    @Test
    public void 주문_취소(){
        //given
        Member member = createMember();
        Item item = createBook("노인과 바다",10000,10);
        int orderCount = 2;

        //when
        Long OrderId = orderService.order(member.getId(), item.getId(), orderCount);
        orderService.cancelOrder(OrderId);

        //then
        Order getOrder = orderRepository.findOne(OrderId);

        assertEquals("주문 상태는 Cancel이여야 한다.", OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("취소 수량만큼 재고가 늘어야 한다.", 10,item.getStockQuantity());

    }

    private Member createMember(){
        Member member = new Member();
        member.setName("김깝십");
        member.setAddress(new Address("City of Seoul","경리단길","159-021"));
        em.persist(member);

        return member;
    }

    private Book createBook(String name, int price, int stockQuantity){
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);

        return book;
    }

}