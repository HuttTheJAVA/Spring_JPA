package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    @Transactional
    public void updateItem(Long id,String name,int price,int stockQuantity){
        // 업데이트 할 필요 x 영속성 꼬리표가 붙은 엔티티는 JPA가 항상 감시(더티 체크)하기 때문에 커밋 시점에서 알아서 update 쿼리가 db로 날아감.
        Item findItem = itemRepository.findOne(id);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }
}
