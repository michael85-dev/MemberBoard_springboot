package com.hmh.memberboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentSaveDTO {
    private Long memberId;
    private Long boardId;
    private String commentWriter; // 게시글 작성자. 로그인시 닉네임을 받으며 비로그인시 임의로 작성 가능
    private String commentContents; // 내용 적는 것
}
