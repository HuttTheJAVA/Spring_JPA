package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY) // OrderItem은 하나의 주문에만 포함 됨. 하나의 주문에는 여러 OrderItem이 포함 됨.
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;
}
