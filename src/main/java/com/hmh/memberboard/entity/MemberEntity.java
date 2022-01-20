package com.hmh.memberboard.entity;

import com.hmh.memberboard.dto.MemberDetailDTO;
import com.hmh.memberboard.dto.MemberSaveDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "member_table")
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(length = 30)
    @NotBlank
    private String memberEmail; //    아이디
    @Length(min = 8, max = 30, message = "8 ~ 30자를 사용해 주세요")
    @NotNull
    private String memberPassword; //    비밀번호 :
    @Column
    @NotBlank
    private String memberName; //    성함 :
    @Column(length = 50)
    private String memberNickName; //    닉네임 :
    @Column(length = 15)
    private String memberPhone; //    전화번호 :
    @Column
    private String memberAddress; //    주소 :
    @Column(length = 1000)
    private String memberMemo; //    메모 :
    @Column
    private String memberPhotoName; //    이미지 :

    // 기본적으로 회원탈퇴해도 그사람의 작성글 및 댓글은 삭제되지 않음.
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "memberEntity", orphanRemoval = false, cascade = CascadeType.PERSIST)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "memberEntity", orphanRemoval = false, cascade = CascadeType.PERSIST)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        System.out.println("MemberEntity.preRemove");
        boardEntityList.forEach(board -> board.setMemberEntity(null));

        commentEntityList.forEach(comment -> comment.setMemberEntity(null));
    }

    public static MemberEntity toSaveEntity(MemberSaveDTO memberSaveDTO) {
        MemberEntity memberEntity = new MemberEntity(); // 기본 생성자 호출

        memberEntity.setMemberEmail(memberSaveDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberSaveDTO.getMemberPassword());
        memberEntity.setMemberName(memberSaveDTO.getMemberName());
        memberEntity.setMemberNickName(memberSaveDTO.getMemberNickName());
        memberEntity.setMemberMemo(memberSaveDTO.getMemberMemo());
        memberEntity.setMemberPhone(memberSaveDTO.getMemberPhone());
        memberEntity.setMemberPhotoName(memberSaveDTO.getMemberPhotoName());
        memberEntity.setMemberAddress(memberSaveDTO.getMemberAddress());

        return memberEntity;
    }

    public static MemberEntity toUpdateEntity(MemberDetailDTO memberDetailDTO) {
        MemberEntity memberEntity = new MemberEntity(); // 기본 생성자 호출

        memberEntity.setId(memberDetailDTO.getMemberId());
        memberEntity.setMemberEmail(memberDetailDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDetailDTO.getMemberPassword());
        memberEntity.setMemberName(memberDetailDTO.getMemberName());
        memberEntity.setMemberNickName(memberDetailDTO.getMemberNickName());
        memberEntity.setMemberMemo(memberDetailDTO.getMemberMemo());
        memberEntity.setMemberPhone(memberDetailDTO.getMemberPhone());
        memberEntity.setMemberPhotoName(memberDetailDTO.getMemberPhotoName());
        memberEntity.setMemberAddress(memberDetailDTO.getMemberAddress());

        return memberEntity;
    }
}
