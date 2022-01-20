package com.hmh.memberboard.dto;

import com.hmh.memberboard.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailDTO {

    private Long memberId;
    private String memberEmail; //    아이디
    private String memberPassword; //    비밀번호 :
    private String memberName; //    성함 :
    private String memberNickName; //    닉네임 :
    private String memberPhone; //    전화번호 :
    private String memberAddress; //    주소 :
    private String memberMemo; //    메모 :
//    private MultipartFile memberPhoto;
    private String memberPhotoName;

    public static MemberDetailDTO injectInfo(MemberEntity memberEntity) {
        MemberDetailDTO memberDetailDTO = new MemberDetailDTO();

        memberDetailDTO.setMemberId(memberEntity.getId());
        memberDetailDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDetailDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDetailDTO.setMemberName(memberEntity.getMemberName());
        memberDetailDTO.setMemberMemo(memberEntity.getMemberMemo());
        memberDetailDTO.setMemberAddress(memberEntity.getMemberAddress());
        memberDetailDTO.setMemberPhone(memberEntity.getMemberPhone());
        memberDetailDTO.setMemberPhotoName(memberEntity.getMemberPhotoName());
        memberDetailDTO.setMemberNickName(memberEntity.getMemberNickName());

        return memberDetailDTO;
    }
}
