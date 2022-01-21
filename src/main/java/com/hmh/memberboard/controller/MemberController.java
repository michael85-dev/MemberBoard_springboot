package com.hmh.memberboard.controller;

import com.hmh.memberboard.common.PagingConst;
import com.hmh.memberboard.dto.*;
import com.hmh.memberboard.service.BoardService;
import com.hmh.memberboard.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/member/*")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService ms;
    private final BoardService bs;

    @GetMapping("save")
    public String saveForm(Model model) {
        System.out.println("MemberController.saveForm");

        model.addAttribute("member", new MemberSaveDTO());

        return "member/save";
    }

    @PostMapping("save")
    public String save(@Validated @ModelAttribute("member") MemberSaveDTO memberSaveDTO, BindingResult bindingResult) throws IOException {
        System.out.println("MemberController.save");
        Long memberId = ms.save(memberSaveDTO);
        if (memberId == null) {
//            bindingResult.hasErrors();
//            bindingResult.reject("email dup");
            // 해당 부분 추가하기.
            return "member/save";
        } else {
            return "member/login";
        }
    }

    @GetMapping("login")
    public String loginForm(Model model) {
        System.out.println("MemberController.loginForm");

        model.addAttribute("member", new MemberLoginDTO());

        return "member/login"; // 현재 여기까진 완료.
    }

    @PostMapping("login") // @ModelAttribute의 명명이 어떤걸 기준으로 되는지 모르겠는데.
    public String login(@Validated @ModelAttribute MemberLoginDTO memberLoginDTO, HttpSession session, Model model, @PageableDefault(page = 1) Pageable pageable) {
        System.out.println("MemberController.login");
        // valicated 를 쓰게 되면 하기 문장 사용이 가능함.
//        if (bindingResult.hasErrors()) { // 벨리데이션 체크
//            return "member/login";
//        }
        boolean checkResult = ms.login(memberLoginDTO); // 왜 memberLogin이 존재하지 않는다고 하지...?

        System.out.println("checkResult = " + checkResult);

        if (checkResult) {
            System.out.println("MemberController.login");

            session.setAttribute("id", memberLoginDTO.getMemberEmail());

            String nickName = ms.findByNickName(memberLoginDTO);


            System.out.println("nickName = " + nickName);
            session.setAttribute("nickName", nickName);

            MemberDetailDTO memberDetailDTO = ms.findByMemberId(memberLoginDTO);
            System.out.println("memberDetailDTO = " + memberDetailDTO);
            model.addAttribute("m", memberDetailDTO);

// boardfindAll 관련
            List<BoardDetailDTO> boardDetailDTOList = bs.findAll();
            model.addAttribute("bList", boardDetailDTOList);

            Page<BoardPagingDTO> boardPaging = bs.paging(pageable);
            model.addAttribute("bpage", boardPaging);

            int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.B_BLOCK_LIMIT))) - 1) * PagingConst.B_BLOCK_LIMIT + 1;
            int endPage = ((startPage + PagingConst.B_BLOCK_LIMIT - 1) < boardPaging.getTotalPages()) ? startPage + PagingConst.B_BLOCK_LIMIT - 1 : boardPaging.getTotalPages();
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "main";
        } else {
//            bindingResult.reject("LoginFail", "이메일 또는 비밀번호가 틀립니다.");

            return "member/login";
        }
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "index";
    }

    @GetMapping("{memberId}")
    public String findById(@PathVariable("memberId") Long memberId, Model model) {
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);

        model.addAttribute("member", memberDetailDTO);

        return "member/findById";
    }

    @PostMapping("update")
    public String update(@ModelAttribute MemberDetailDTO memberDetailDTO) {
        ms.update(memberDetailDTO);

        return "main";
    }

    @GetMapping
    public String findAll(Model model, HttpSession session, @PageableDefault(page = 1) Pageable pageable) {
        Long memberId = (Long)session.getAttribute("member");
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);

        List<MemberDetailDTO> memberDetailDTOList = ms.findAll();
        model.addAttribute("memberList", memberDetailDTOList);

        Page<MemberPagingDTO> memberPage = ms.paging(pageable);
        model.addAttribute("memberPage", memberPage);

        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.M_BLOCK_LIMIT))) - 1) * PagingConst.M_BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.M_BLOCK_LIMIT - 1) < memberPage.getTotalPages()) ? startPage + PagingConst.M_BLOCK_LIMIT - 1 : memberPage.getTotalPages();

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "member/findAll";
    }

    @DeleteMapping("{memberId}")
    public ResponseEntity delete(@PathVariable("memberId") Long memberId) {
        ms.deleteById(memberId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
