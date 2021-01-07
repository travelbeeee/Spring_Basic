package com.travelbeeee.study.member.service;

import com.travelbeeee.study.member.Member;

public interface IMemberService {
	Boolean registerMember(Member member);
	int modifyMember(Member member);
	int removeMember(Member member);
	Member findMember(Member member);
}
