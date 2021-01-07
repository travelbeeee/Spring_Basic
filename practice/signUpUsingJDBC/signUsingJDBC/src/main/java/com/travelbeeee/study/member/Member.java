package com.travelbeeee.study.member;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Member {
	private int id;
	private String username;
	private String email;
	private String pwd;
}
