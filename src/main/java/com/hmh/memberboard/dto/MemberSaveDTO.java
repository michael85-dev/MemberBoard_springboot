package com.hmh.memberboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSaveDTO {

    private String memberEmail; //    아이디
    private String memberPassword; //    비밀번호 :
    private String memberName; //    성함 :
    private String memberNickName; //    닉네임 :
    private String memberPhone; //    전화번호 :
    private String memberAddress; //    주소 :
    private String memberMemo; //    메모 :
    private MultipartFile memberPhoto;
    private String memberPhotoName;

}
