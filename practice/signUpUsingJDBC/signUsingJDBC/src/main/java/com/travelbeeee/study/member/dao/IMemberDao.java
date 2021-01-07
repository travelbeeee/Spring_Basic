package com.travelbeeee.study.member.dao;

import com.travelbeeee.study.member.Member;

public interface IMemberDao {
	public int insertMember(Member member);
	public int updateMember(Member member);
	public int deleteMember(int id);
	public Member selectMember(Member member);
}
