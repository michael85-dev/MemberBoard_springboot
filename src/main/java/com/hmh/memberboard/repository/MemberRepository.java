package com.hmh.memberboard.repository;

import com.hmh.memberboard.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    MemberEntity findByEmail(String memberEmail);

    boolean findByMemberEmail(String memberEmail);


}
