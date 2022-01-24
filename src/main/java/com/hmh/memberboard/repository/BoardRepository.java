package com.hmh.memberboard.repository;

import com.hmh.memberboard.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    BoardEntity findByBoardWriter(String commentWriter);

    // native query (알고 있는 것을 그대로 쓰는것 - 실제 쓰는 것 ? )
    // jpql 이라고 함.(java persistence query language)
    // 반드시 테이블에 대한 약칭을 써야함. (BoardEntity b) -> BoardEntity를 b라고 여기서는 명명하겠다.
                                    // BoardEntity as b (DB에서는 이렇게 씀)
    // query dsl
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id = :boardId")
                                                                        // DB에서는 #{}을 사용했으나. 여기선 :을 사용함
    int boardHits(@Param("boardId") Long boardId);
}
