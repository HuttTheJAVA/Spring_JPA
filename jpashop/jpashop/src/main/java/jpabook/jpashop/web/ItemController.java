package jpabook.jpashop.web;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items")
    public String list(Model model){
        model.addAttribute("items",itemService.findItems());
        return "items/itemList";
    }

    @GetMapping("items/new")
    public String itemForm(Model model){
        BookForm form = new BookForm();
        model.addAttribute("form",form);
        return "items/createItemForm";
    }

    @PostMapping("items/new")
    public String createItem(BookForm bookForm){
        Book book = new Book();
        book.setName(bookForm.getName());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items/{id}/edit")
    public String updateForm(@PathVariable(name = "id" ) Long itemId,Model model){
        Book item = (Book)itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form",form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{id}/edit")
    public String updateItem(@PathVariable Long id, @ModelAttribute("form") BookForm form){
        itemService.updateItem(id, form.getName(), form.getPrice(), form.getStockQuantity());

        return "redirect:/items";
    }
}
