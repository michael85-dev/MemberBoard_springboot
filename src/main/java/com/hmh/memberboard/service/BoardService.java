package com.hmh.memberboard.service;

import com.hmh.memberboard.dto.BoardDetailDTO;
import com.hmh.memberboard.dto.BoardPagingDTO;
import com.hmh.memberboard.dto.BoardSaveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    Long save(BoardSaveDTO boardSaveDTO) throws IOException;

    List<BoardDetailDTO> findAll();

    BoardDetailDTO findById(Long boardId);

    Long update(BoardDetailDTO boardDetailDTO);

    Page<BoardPagingDTO> paging(Pageable pageable);
}
