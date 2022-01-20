package com.hmh.memberboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveDTO {
    private Long memberId;
    private String boardWriter; // 게시글 작성자, 로그인시 닉네임 받음, 비로그인시 임의 작성
    private String boardTitle; // 게시글 제목
    private String boardPassword;
    private String boardContents; // 게시글 내용
    private MultipartFile boardPhoto;
    private String boardPhotoName;

}
