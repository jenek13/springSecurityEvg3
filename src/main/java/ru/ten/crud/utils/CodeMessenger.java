package ru.ten.crud.utils;


public class CodeMessenger {

	private static spring.app.utils.ErrorCode code;

	public static spring.app.utils.ErrorCode getCode(){
		spring.app.utils.ErrorCode cod = code;
		code = null;
		return cod;
	}

	public static void setCode(spring.app.utils.ErrorCode cod) {
		code = cod;
	}
}
