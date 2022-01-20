package com.hmh.memberboard.service;

import com.hmh.memberboard.common.PagingConst;
import com.hmh.memberboard.dto.CommentDetailDTO;
import com.hmh.memberboard.dto.CommentPagingDTO;
import com.hmh.memberboard.dto.CommentSaveDTO;
import com.hmh.memberboard.entity.BoardEntity;
import com.hmh.memberboard.entity.CommentEntity;
import com.hmh.memberboard.entity.MemberEntity;
import com.hmh.memberboard.repository.BoardRepository;
import com.hmh.memberboard.repository.CommentRepository;
import com.hmh.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final BoardRepository br;
    private final MemberRepository mr;
    private final CommentRepository cr;

    @Override
    public Long save(CommentSaveDTO commentSaveDTO) {
        Optional<BoardEntity> boardEntityOptional = br.findById(commentSaveDTO.getBoardId());
        BoardEntity boardEntity = boardEntityOptional.get();

        MemberEntity memberEntity = mr.findByMemberId(commentSaveDTO.getMemberId());
        CommentEntity commentEntity = CommentEntity.toSave(commentSaveDTO, boardEntity, memberEntity);

        Long commentId = cr.save(commentEntity).getId();

        return commentId;
    }

    @Override
    public List<CommentDetailDTO> findAll(Long boardId) {
        Optional<BoardEntity> boardEntityOptional = br.findById(boardId);
        BoardEntity boardEntity = boardEntityOptional.get();

        List<CommentEntity> commentEntityList = boardEntity.getCommentEntityList();

        List<CommentDetailDTO> commentList =new ArrayList<>();
        for (CommentEntity c: commentEntityList) {
            CommentDetailDTO commentDetailDTO = CommentDetailDTO.toMove(c);
            commentList.add(commentDetailDTO);
        }

        return commentList;
    }

    @Override
    public Page<CommentPagingDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber();
        page = (page == 1) ? 0 : (page - 1);

        Page<CommentEntity> commentEntityList = cr.findAll(PageRequest.of(page, PagingConst.C_PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));

        Page<CommentPagingDTO> commentPaging = commentEntityList.map(
                comment -> new CommentPagingDTO(comment.getId(),
                        comment.getCommentWriter(),
                        comment.getCommentContents(),
                        comment.getBoardEntity().getId(),
                        comment.getMemberEntity().getId())
        );

        return commentPaging;
    }
}
