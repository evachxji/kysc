package com.kysc.utils;


public enum ErrorMsg {

	ERROR_MSG(1001,"请填写用户名"),

    ERROR_MSG1(1002,"用户名不符合规范"),

    ERROR_MSG2(1003,"用户名已存在"),

    ERROR_MSG3(1004,"用户名或密码错误"),

    ERROR_MSG4(1005,"该手机号已注册"),

    ERROR_MSG5(1006,"密码不符合规范"),

    ERROR_MSG6(1007,"手机号不符合规范"),

    ERROR_MSG7(1008,"请上传正确的图片格式"),

    ERROR_MSG8(1009,"图片文件大小不符合规范"),
;
	/** 错误码 */
	private Integer code;
	/** 错误信息 */
	private String msg;

	ErrorMsg(final Integer code, final String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
