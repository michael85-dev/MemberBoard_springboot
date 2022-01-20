package com.hmh.memberboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class MemberPagingDTO extends MemberDetailDTO {

    public MemberPagingDTO(Long id, String memberEmail, String memberPassword, String memberName, String memberNickName, String memberMemo, String memberPhone, String memberAddress, String memberPhotoName) {
    }
}
