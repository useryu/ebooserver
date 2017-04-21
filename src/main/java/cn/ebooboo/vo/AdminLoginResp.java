package cn.ebooboo.vo;

import cn.ebooboo.model.Admin;

public class AdminLoginResp {

	private Integer code;
	private String msg;
	private Admin user;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Admin getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "AdminLoginResp [code=" + code + ", msg=" + msg + ", user=" + user + "]";
	}

	public void setUser(Admin user) {
		this.user = user;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
