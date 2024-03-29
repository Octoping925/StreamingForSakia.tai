package com.octoping.boardproject_spring.repository;

import com.octoping.boardproject_spring.domain.Member;
import com.octoping.boardproject_spring.service.MainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemoryMemberRepositoryTest {
    MemberRepository memberRepository = new MemoryMemberRepository();

    @Test
    public void isMemberValidTest() {
        memberRepository.save(new Member("myc", "123"));
        boolean resultTrue = memberRepository.isUserInfoValid("myc", "123");
        boolean resultFalse = memberRepository.isUserInfoValid("myc", "NotCorrectPassword");
        boolean resultFalse2 = memberRepository.isUserInfoValid("testFail!", "");

        Assertions.assertTrue(resultTrue);
        Assertions.assertFalse(resultFalse);
        Assertions.assertFalse(resultFalse2);
    }
}
