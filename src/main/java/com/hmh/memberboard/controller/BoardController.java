package com.hmh.memberboard.controller;

import com.hmh.memberboard.common.PagingConst;
import com.hmh.memberboard.dto.BoardDetailDTO;
import com.hmh.memberboard.dto.BoardPagingDTO;
import com.hmh.memberboard.dto.BoardSaveDTO;
import com.hmh.memberboard.dto.MemberDetailDTO;
import com.hmh.memberboard.entity.BoardEntity;
import com.hmh.memberboard.service.BoardService;
import com.hmh.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService bs;
    private final MemberService ms;

    @GetMapping("save")
    public String saveForm(Model model, HttpSession session) {
        System.out.println("BoardController.saveForm");

        Long memberId = (Long)session.getAttribute("member");
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);

        model.addAttribute("bsave", new BoardSaveDTO());

        return "board/save";
    }

    @PostMapping("save")
    public String save(@PathVariable @ModelAttribute BoardSaveDTO boardSaveDTO, Model model, BindingResult bindingResult) throws IOException {
        // 내 생각이 맞다면 MemberDetailDTO는 필요가 없을꺼라고 생각됨.
        // 게시글은 중복 체크가 필요 없고 다른 것도 필요 없음.
        MemberDetailDTO memberDetailDTO = ms.findById(boardSaveDTO.getMemberId());
        model.addAttribute("member", memberDetailDTO);

        Long boardId = bs.save(boardSaveDTO);

        return "main";
    }

    @GetMapping
    public String findAll(Model model, HttpSession session, @PageableDefault(page = 1)Pageable pageable) {
        Long memberId = (Long)session.getAttribute("member");
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);

        List<BoardDetailDTO> boardDetailDTOList = bs.findAll();
        model.addAttribute("bList", boardDetailDTOList);

        Page<BoardPagingDTO> boardPaging = bs.paging(pageable);
        model.addAttribute("bpage", boardPaging);

        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.B_BLOCK_LIMIT))) - 1) * PagingConst.B_BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.B_BLOCK_LIMIT - 1) < boardPaging.getTotalPages()) ? startPage + PagingConst.B_BLOCK_LIMIT - 1 : boardPaging.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "main";
    }

    @GetMapping("{boardId")
    public String findById(@PathVariable("boardId") Long boardId, Model model, HttpSession session) {
        Long memberId = (Long)session.getAttribute("member");
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);

        BoardDetailDTO boardDetailDTO = bs.findById(boardId);
        model.addAttribute("board", boardDetailDTO);

        // 1번째 방법 . 여기다가 comment 과녈ㄴ 호출을 전부 한다.
        // 2반째 방법 예전에 jsp에서는 어떻게 했는지 확인해본다.

        return "board/findById";
    }

    @GetMapping("update")
    public String updateForm(@PathVariable @ModelAttribute BoardDetailDTO boardDetailDTO, Model model, HttpSession session) {
        Long memberId = (Long)session.getAttribute("member");
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);

        BoardDetailDTO boardDetailDTO1 = bs.findById(boardDetailDTO.getBoardId());
        model.addAttribute("board", boardDetailDTO);

        return "board/update";
    }

    @PostMapping("update")
    public String update(@ModelAttribute BoardDetailDTO boardDetailDTO, HttpSession session, Model model) {
        Long memberId = (Long)session.getAttribute("member");
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);

        Long boardId = bs.update(boardDetailDTO);

        return "redirect:/board/" + boardId;
    }
}
