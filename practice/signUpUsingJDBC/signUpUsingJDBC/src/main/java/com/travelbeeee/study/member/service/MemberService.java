package com.travelbeeee.study.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbeeee.study.member.Member;
import com.travelbeeee.study.member.dao.MemberDao;


@Service
public class MemberService implements IMemberService{

	@Autowired
	MemberDao memberDao;
	
	@Override
	public Boolean registerMember(Member member) {
		int result = memberDao.insertMember(member);
		if(result == 0)
			return false;
		return true;
	}

	@Override
	public int modifyMember(Member member) {
		int result = memberDao.updateMember(member);
		System.out.println("수정결과  : " + result);
		return result;
	}

	@Override
	public int removeMember(Member member) {
		int result = memberDao.deleteMember(member.getId());
		
		return result;
	}

	@Override
	public Member findMember(Member member) {
		if(memberDao.selectMember(member) != null)
			return memberDao.selectMember(member);
		return null;
	}
}
