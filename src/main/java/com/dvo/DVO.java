package com.dvo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class DVO {
	
	@NotEmpty(message="用户不能为空")
	@Length(min=6,max=20,message="长度为6-20位")
	private String username;
	
	@NotEmpty(message="用户不能为空")
	@Length(min=6,max=20,message="长度为6-20位")
	@Pattern(regexp="[a-zA-Z0-9] {6-20}")
	private String password;
	
	@NotEmpty(message="用户不能为空")
	@Length(min=6,max=20,message="长度为6-20位")
	@Pattern(regexp="[a-zA-Z0-9] {6-20}")
	private String confirm;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	
	
	
}
