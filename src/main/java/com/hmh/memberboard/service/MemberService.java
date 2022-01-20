package com.hmh.memberboard.service;

import com.hmh.memberboard.dto.MemberDetailDTO;
import com.hmh.memberboard.dto.MemberLoginDTO;
import com.hmh.memberboard.dto.MemberPagingDTO;
import com.hmh.memberboard.dto.MemberSaveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface MemberService {
    Long save(MemberSaveDTO memberSaveDTO) throws IOException;

    boolean login(MemberLoginDTO memberLoginDTO);

    String findByNickName(MemberLoginDTO memberLoginDTO);

    MemberDetailDTO findByMemberId(MemberLoginDTO memberLoginDTO);

    MemberDetailDTO findById(Long memberId);

    void update(MemberDetailDTO memberDetailDTO);

    List<MemberDetailDTO> findAll();

    void deleteById(Long memberId);

    Page<MemberPagingDTO> paging(Pageable pageable);
}
