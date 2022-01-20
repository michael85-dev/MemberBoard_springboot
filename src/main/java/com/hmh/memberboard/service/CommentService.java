package com.hmh.memberboard.service;

import com.hmh.memberboard.dto.CommentDetailDTO;
import com.hmh.memberboard.dto.CommentPagingDTO;
import com.hmh.memberboard.dto.CommentSaveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    Long save(CommentSaveDTO commentSaveDTO);

    List<CommentDetailDTO> findAll(Long boardId);

    Page<CommentPagingDTO> paging(Pageable pageable);
}
