package com.org.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import java.io.IOException;

public class ACT {

	public static void init(Context context, Class<?> name) throws NotExistException, WrongTypeException, IOException {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler((Ability) context, name));
	}

}
