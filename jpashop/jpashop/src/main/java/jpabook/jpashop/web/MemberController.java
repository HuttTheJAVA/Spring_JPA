package jpabook.jpashop.web;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.service.MemberService;
import jpabook.jpashop.web.MemberForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createMember(Model model){
        MemberForm form = new MemberForm();
        model.addAttribute("memberForm",form);
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result){

        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(),memberForm.getStreet(),memberForm.getZipcode());
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }
}
