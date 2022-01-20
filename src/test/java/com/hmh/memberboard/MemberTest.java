package com.hmh.memberboard;

import com.hmh.memberboard.dto.MemberSaveDTO;
import com.hmh.memberboard.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.stream.IntStream;

public class MemberTest {
    @Autowired
    private MemberService ms;

    @Test
    public void memberSave() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            try {
                ms.save(new MemberSaveDTO("아이디" + i, "비밀번호" + i, "이름" + i, "닉네임" + i, "전화번호" + i, "주소" + i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
