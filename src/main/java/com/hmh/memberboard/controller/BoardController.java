package com.hmh.memberboard.controller;

import com.hmh.memberboard.common.PagingConst;
import com.hmh.memberboard.dto.*;
import com.hmh.memberboard.entity.BoardEntity;
import com.hmh.memberboard.service.BoardService;
import com.hmh.memberboard.service.CommentService;
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
import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService bs;
    private final MemberService ms;
    private final CommentService cs;

    @GetMapping("save")
    public String saveForm(Model model, HttpSession session) {
        System.out.println("BoardController.saveForm");

        String nickName = (String)session.getAttribute("nickName");
        System.out.println("nickName = " + nickName);

        Long memberId = ms.find(nickName);

        System.out.println("memberId = " + memberId);
        // 이 정보가 null로 확인됨. -> java에서 html로는 세션을 받아오지만 세션을 java로는 받아오기 위해서는 다른 무언가 필요한 것으로 보임.
//        Long memberId = (Long)session.getAttribute("member"); // 이게 지금 not null이 뜨고 있는건데 이걸 어떻게 해결할지에 대해서 고민해야할 꺼라고 생각이 된다.
//        System.out.println("memberId = " + memberId);
//      MemberDetailDTO memberDetailDTO = ms.findById(memberId);
//
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        System.out.println("memberDetailDTO = " + memberDetailDTO);

        model.addAttribute("member", memberDetailDTO);

        model.addAttribute("bsave", new BoardSaveDTO());

        return "board/save";
    }

    @PostMapping("save")
    public String save(@ModelAttribute BoardSaveDTO boardSaveDTO, Model model, BindingResult bindingResult, @PageableDefault(page = 1) Pageable pageable) throws IOException {
        System.out.println("BoardController.save");

        // 내 생각이 맞다면 MemberDetailDTO는 필요가 없을꺼라고 생각됨.
        // 게시글은 중복 체크가 필요 없고 다른 것도 필요 없음.
        MemberDetailDTO memberDetailDTO = ms.findById(boardSaveDTO.getMemberId());
        model.addAttribute("member", memberDetailDTO);

        Long boardId = bs.save(boardSaveDTO);

        // 로그인에 있는 정보를 가지고 메인으로
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
    }

    @GetMapping
    public String findAll(Model model, HttpSession session, @PageableDefault(page = 1)Pageable pageable) {
        String nickName = (String)session.getAttribute("nickName");
        System.out.println("nickName = " + nickName);
        Long memberId = ms.find(nickName);
        System.out.println("memberId = " + memberId);
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

    @GetMapping("{boardId}")
    public String findById(@PathVariable("boardId") Long boardId, Model model, HttpSession session, @PageableDefault(page = 1) Pageable pageable) {
        String nickName = (String)session.getAttribute("nickName");
        System.out.println("nickName = " + nickName);
        Long memberId = ms.find(nickName);
        System.out.println("memberId = " + memberId);

        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);

        BoardDetailDTO boardDetailDTO = bs.findById(boardId);
        model.addAttribute("board", boardDetailDTO);

        // 1번째 방법 . 여기다가 comment 과녈ㄴ 호출을 전부 한다.
        // 2반째 방법 예전에 jsp에서는 어떻게 했는지 확인해본다.

        List<CommentDetailDTO> commentDetailDTOList = cs.findAll(boardDetailDTO.getBoardId());
        model.addAttribute("cList", commentDetailDTOList);

        // page Model이 먹히려나... (안되면 다른 방안 찾기)
        Page<CommentPagingDTO> commentList = cs.paging(pageable);
        model.addAttribute("cpage", commentList);

        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.C_BLOCK_LIMIT))) - 1) * PagingConst.C_BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.C_BLOCK_LIMIT - 1) < commentList.getTotalPages()) ? startPage + PagingConst.C_BLOCK_LIMIT - 1 : commentList.getTotalPages();
        model.addAttribute("cStartPage", startPage);
        model.addAttribute("cEndPage", endPage);

        return "board/findById";
    }

    @GetMapping("update")
    public String updateForm(Model model, HttpSession session) {
        String nickName = (String)session.getAttribute("nickName");
        System.out.println("nickName = " + nickName);
        Long memberId = ms.find(nickName);
        System.out.println("memberId = " + memberId);
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);

        Long boardId = bs.find(nickName);
        BoardDetailDTO boardDetailDTO = bs.findById(boardId);
        model.addAttribute("board", boardDetailDTO);

        return "board/update"; // 여기까지 완료
    }

    @PostMapping("update")
    public String update(@PathVariable @ModelAttribute BoardDetailDTO boardDetailDTO, HttpSession session, Model model) {
        String nickName = (String)session.getAttribute("nickName");
        System.out.println("nickName = " + nickName);
        Long memberId = ms.find(nickName);
        System.out.println("memberId = " + memberId);

        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        model.addAttribute("member", memberDetailDTO);

        Long boardId = bs.update(boardDetailDTO);

        return "redirect:/board/" + boardId;
    }
}
