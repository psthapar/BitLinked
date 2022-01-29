package com.parvinder.thapar.bitlinked.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalUtil implements GlobalConstants {
	
	public static Date getTodayWithTimeStamp() {
		Date todayWithStamp;
		SimpleDateFormat formatter = new SimpleDateFormat(GlobalConstants.BLOCK_DATE_FORMAT);
		String strDate = formatter.format(new Date());
		try {
			todayWithStamp =  formatter.parse(strDate);
		}catch (Exception ex) {
			ex.printStackTrace();
			todayWithStamp = new Date();
		}
		return todayWithStamp;
	}

}
