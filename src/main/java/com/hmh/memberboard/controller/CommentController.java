package com.hmh.memberboard.controller;

import com.hmh.memberboard.common.PagingConst;
import com.hmh.memberboard.dto.CommentDetailDTO;
import com.hmh.memberboard.dto.CommentPagingDTO;
import com.hmh.memberboard.dto.CommentSaveDTO;
import com.hmh.memberboard.dto.MemberDetailDTO;
import com.hmh.memberboard.service.CommentService;
import com.hmh.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment/*")
public class CommentController {
    private final CommentService cs;
    private final MemberService ms;

    @PostMapping("save")
    public @ResponseBody List<CommentDetailDTO> save(@ModelAttribute CommentSaveDTO commentSaveDTO) { //, @PageableDefault(page = 1)Pageable pageable, Model model) {
//        String nickName = (String)session.getAttribute("nickName");
//        System.out.println("nickName = " + nickName);
        Long memberId = ms.find(commentSaveDTO.getCommentWriter());
//        System.out.println("memberId = " + memberId);

//        MemberDetailDTO memberDetailDTO = ms.findById(memberId);

        System.out.println("CommentController.save");

        commentSaveDTO.setMemberId(memberId);
        System.out.println("commentSaveDTO = " + commentSaveDTO);
        Long commentId = cs.save(commentSaveDTO);

        List<CommentDetailDTO> commentDetailDTOList = cs.findAll(commentSaveDTO.getBoardId());

//        model.addAttribute("cList", commentDetailDTOList);

        // page Model이 먹히려나... (안되면 다른 방안 찾기)
        // 이건 board에서 findById가 불러오니까...
//        Page<CommentPagingDTO> commentList = cs.paging(pageable);
//        model.addAttribute("cpage", commentList);
//
//        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.C_BLOCK_LIMIT))) - 1) * PagingConst.C_BLOCK_LIMIT + 1;
//        int endPage = ((startPage + PagingConst.C_BLOCK_LIMIT - 1) < commentList.getTotalPages()) ? startPage + PagingConst.C_BLOCK_LIMIT - 1 : commentList.getTotalPages();
//        model.addAttribute("cStartPage", startPage);
//        model.addAttribute("cEndPage", endPage);
        System.out.println("commentDetailDTOList = " + commentDetailDTOList);

        return commentDetailDTOList;
    }
}
