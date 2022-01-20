package com.hmh.memberboard.service;

import com.hmh.memberboard.common.PagingConst;
import com.hmh.memberboard.dto.MemberDetailDTO;
import com.hmh.memberboard.dto.MemberLoginDTO;
import com.hmh.memberboard.dto.MemberPagingDTO;
import com.hmh.memberboard.dto.MemberSaveDTO;
import com.hmh.memberboard.entity.MemberEntity;
import com.hmh.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository mr;

    @Override
    public Long save(MemberSaveDTO memberSaveDTO) throws IOException {
        boolean checkEmail = mr.findByMemberEmail(memberSaveDTO.getMemberEmail());

        if (checkEmail) {
            Long memberId = null;

            return memberId;

        } else {
            MemberEntity memberEntity = MemberEntity.toSaveEntity(memberSaveDTO);

            MultipartFile memberPhoto = memberSaveDTO.getMemberPhoto();
            String memberPhotoName = memberPhoto.getOriginalFilename();

            memberPhotoName = System.currentTimeMillis() + "-" + memberPhotoName;

            String savePath = "";

            if (!memberPhoto.isEmpty()) {
                memberPhoto.transferTo(new File(savePath));
                memberSaveDTO.setMemberPhotoName(memberPhotoName);
            }

            Long memberId = mr.save(memberEntity).getId();

            return memberId;
        }
    }

    @Override
    public boolean login(MemberLoginDTO memberLoginDTO) {
        MemberEntity memberEntity = mr.findByEmail(memberLoginDTO.getMemberEmail());

        if (memberEntity != null) {
            if(memberLoginDTO.getMemberPassword().equals(memberEntity.getMemberPassword())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String findByNickName(MemberLoginDTO memberLoginDTO) {
        MemberEntity memberEntity = mr.findByEmail(memberLoginDTO.getMemberEmail());

        String nickName = memberEntity.getMemberNickName();

        return nickName;
    }

    @Override
    public MemberDetailDTO findByMemberId(MemberLoginDTO memberLoginDTO) {
        MemberEntity memberEntity = mr.findByEmail(memberLoginDTO.getMemberEmail());

        MemberDetailDTO memberDetailDTO = MemberDetailDTO.injectInfo(memberEntity);

        return memberDetailDTO;
    }

    @Override
    public MemberDetailDTO findById(Long memberId) {
        MemberEntity memberEntity = mr.findByMemberId(memberId);

        MemberDetailDTO memberDetailDTO = MemberDetailDTO.injectInfo(memberEntity);

        return memberDetailDTO;
    }

    @Override
    public void update(MemberDetailDTO memberDetailDTO) {
        MemberEntity memberEntity = MemberEntity.toUpdateEntity(memberDetailDTO);

        mr.save(memberEntity);
    }

    @Override
    public List<MemberDetailDTO> findAll() {
        List<MemberEntity> memberEntityList = mr.findAll();
        List<MemberDetailDTO> memberDetailDTOList = new ArrayList<>();

        for (MemberEntity m:memberEntityList) {
            memberDetailDTOList.add(MemberDetailDTO.injectInfo(m));
        }

        return memberDetailDTOList;
    }

    @Override
    public void deleteById(Long memberId) {
        mr.deleteById(memberId);
    }

    @Override
    public Page<MemberPagingDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber();
        page = (page == 1) ? 0 : (page - 1);

        Page<MemberEntity> memberEntities = mr.findAll(PageRequest.of(page, PagingConst.M_PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));

        Page<MemberPagingDTO> memberPaging = memberEntities.map(
                member -> new MemberPagingDTO(member.getId(),
                        member.getMemberEmail(),
                        member.getMemberPassword(),
                        member.getMemberName(),
                        member.getMemberNickName(),
                        member.getMemberMemo(),
                        member.getMemberPhone(),
                        member.getMemberAddress(),
                        member.getMemberPhotoName())
        );

        return memberPaging;
    }
}
