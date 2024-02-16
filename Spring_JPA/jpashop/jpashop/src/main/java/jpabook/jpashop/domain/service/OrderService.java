package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.ItemRepository;
import jpabook.jpashop.domain.repository.MemberRepository;
import jpabook.jpashop.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 데이터를 변경하는 order나 cancelOrder메소드는 readOnly를 쓰면 안됨.
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    /** 주문 */
    @Transactional
    public Long order(Long memberId, Long itemId,int count){

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성  // 여러 개의 orderItem을 매개변수로 넣을 수 있음.
        Order order = Order.createOrder(member,delivery,orderItem);

        //주문 저장
        orderRepository.save(order);
        return order.getId(); //persist 하고 나면 일단 DB에 바로 저장되지 않아도 Id가 부여됨. DB에 저장되는건 Transactional이 끝나서 commit되는 시점에 저장됨.
    }

    /** 주문 취소 */
    @Transactional
    public void cancelOrder(Long OrderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(OrderId);

        //주문 취소
        order.cancel();
    }


    /** 주문 검색 */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
