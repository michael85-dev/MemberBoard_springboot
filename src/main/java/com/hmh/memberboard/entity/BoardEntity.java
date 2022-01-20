package com.hmh.memberboard.entity;

import com.hmh.memberboard.dto.BoardDetailDTO;
import com.hmh.memberboard.dto.BoardSaveDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    @Column
    @NotBlank
    private String boardWriter; // 게시글 작성자, 로그인시 닉네임 받음, 비로그인시 임의 작성
    @Column
    @Length(min = 2, max = 50)
    private String boardTitle; // 게시글 제목
    @Column
    private String boardPassword;
    @Column
    @Length(min = 10, max = 1000)
    private String boardContents; // 게시글 내용
    @Column
    private String boardPhotoName; // 사진 첨부 (글 내부는 아님.) 가능하면 후에 추가 가능

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "boardEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    public static BoardEntity toSave(BoardSaveDTO boardSaveDTO, MemberEntity memberEntity) {
        BoardEntity boardEntity = new BoardEntity();

        boardEntity.setBoardTitle(boardSaveDTO.getBoardTitle());
        boardEntity.setBoardWriter(boardSaveDTO.getBoardWriter());
        boardEntity.setBoardContents(boardSaveDTO.getBoardContents());
        boardEntity.setBoardPhotoName(boardSaveDTO.getBoardPhotoName());
        boardEntity.setMemberEntity(memberEntity);

        return boardEntity;
    }

    public static BoardEntity toUpdate(BoardDetailDTO boardDetailDTO, MemberEntity memberEntity) {
        BoardEntity boardEntity = new BoardEntity();

        boardEntity.setId(boardDetailDTO.getBoardId());
        boardEntity.setBoardWriter(boardDetailDTO.getBoardWriter());
        boardEntity.setBoardPassword(boardDetailDTO.getBoardPassword());
        boardEntity.setBoardContents(boardDetailDTO.getBoardContents());
        boardEntity.setBoardTitle(boardDetailDTO.getBoardTitle());
        boardEntity.setMemberEntity(memberEntity);

        return boardEntity;
    }
}
