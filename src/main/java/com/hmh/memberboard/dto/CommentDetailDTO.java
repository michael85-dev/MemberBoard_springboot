package com.hmh.memberboard.dto;

import com.hmh.memberboard.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetailDTO {
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private Long memberId;
    private Long commentId;

    public static CommentDetailDTO toMove(CommentEntity c) {
        CommentDetailDTO commentDetailDTO = new CommentDetailDTO();
        commentDetailDTO.setCommentId(c.getId());
        commentDetailDTO.setCommentContents(c.getCommentContents());
        commentDetailDTO.setCommentWriter(c.getCommentWriter());
        commentDetailDTO.setBoardId(c.getBoardEntity().getId());
        commentDetailDTO.setMemberId(c.getMemberEntity().getId());

        return commentDetailDTO;
    }
}
