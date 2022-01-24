package com.hmh.memberboard.service;

import com.hmh.memberboard.common.PagingConst;
import com.hmh.memberboard.dto.BoardDetailDTO;
import com.hmh.memberboard.dto.BoardPagingDTO;
import com.hmh.memberboard.dto.BoardSaveDTO;
import com.hmh.memberboard.entity.BoardEntity;
import com.hmh.memberboard.entity.MemberEntity;
import com.hmh.memberboard.repository.BoardRepository;
import com.hmh.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository br;
    private final MemberRepository mr;

    @Override
    public Long save(BoardSaveDTO boardSaveDTO) throws IOException {
        // 파일이 있으면.
        MultipartFile boardPhoto = boardSaveDTO.getBoardPhoto();
        String boardPhotoName = boardPhoto.getOriginalFilename();

        boardPhotoName = System.currentTimeMillis() + "-" + boardPhotoName;
        String savePath = "D:\\GitHub\\Test\\MemberBoard\\src\\main\\resources\\static\\photo" + boardPhotoName;

        if (!boardPhoto.isEmpty()) {
            boardPhoto.transferTo(new File(savePath));
            boardSaveDTO.setBoardPhotoName(boardPhotoName);
        }

        int hits = boardSaveDTO.getBoardHits();
        hits = 0;
        boardSaveDTO.setBoardHits(hits);
        // boardSave의 데이터를 Entity에 담아서 보내줘야함.
        // MemberEntity와 연관관계가 있으므로 MemberEntity를 호출하고 그것을 가지고 와야함.
        MemberEntity memberEntity = mr.findById(boardSaveDTO.getMemberId()).get();
        BoardEntity boardEntity = BoardEntity.toSave(boardSaveDTO, memberEntity);


        Long boardId = br.save(boardEntity).getId();

        return boardId;
    }

    @Override
    public List<BoardDetailDTO> findAll() {
        List<BoardEntity> boardEntityList = br.findAll();
        List<BoardDetailDTO> boardDetailDTOList = new ArrayList<>();

        for (BoardEntity b: boardEntityList) {
            boardDetailDTOList.add(BoardDetailDTO.toMoveData(b));
        }

        return boardDetailDTOList;
    }

    @Override
    @Transactional // jpql 메서드를 호출 하려면 붙여야함.
    public BoardDetailDTO findById(Long boardId) {
        Optional<BoardEntity> boardEntityOptional = br.findById(boardId);
        BoardEntity boardEntity = boardEntityOptional.get();

//        int hits = boardEntity.getBoardHits();
//        hits = hits + 1;
//        boardEntity.setBoardHits(hits);

//        int hits = br.boardHits(boardId);

        BoardDetailDTO boardDetailDTO = BoardDetailDTO.toMoveData(boardEntity);

        return boardDetailDTO;
    }

    @Override
    public Long update(BoardDetailDTO boardDetailDTO) {
        // 사진 정보 업데이트를 해줘야할까?

        // 데이터 보내기
//        MemberEntity memberEntity = mr.findById(boardDetailDTO.getMemberId()); -> 어떻게 Entity를 적합하게 꺼내올 수 있을까?
        MemberEntity memberEntity = mr.findByMemberNickName(boardDetailDTO.getBoardWriter()); // 이거로 되나?
        BoardEntity boardEntity = BoardEntity.toUpdate(boardDetailDTO, memberEntity);

        Long boardId = br.save(boardEntity).getId();

        return boardId;
    }

    @Override
    public Page<BoardPagingDTO> paging(Pageable pageable) {
        // 아직 이부분 미숙함
        int page = pageable.getPageNumber(); // 페이지 를 가져오는 것으로 며ㅑㅇ명
        page = (page == 1) ? 0 : (page - 1);

        Page<BoardEntity> boardEntityPage = br.findAll(PageRequest.of(page, PagingConst.B_PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        Page<BoardPagingDTO> boardPage = boardEntityPage.map(
                board -> new BoardPagingDTO(board.getId(),
                        board.getBoardTitle(),
                        board.getBoardContents(),
                        board.getBoardPassword(),
                        board.getBoardWriter(),
                        board.getBoardPhotoName(),
                        board.getMemberEntity().getId())
        );

        return boardPage;
    }

    @Override
    public Long find(String nickName) {
        BoardEntity boardEntity = br.findByBoardWriter(nickName);
        BoardDetailDTO boardDetailDTO = BoardDetailDTO.toMoveData(boardEntity);
        Long boardId = boardDetailDTO.getBoardId();

        return boardId;
    }
}
