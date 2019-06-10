package br.com.caelum.leilao.util;

import java.util.Calendar;

public class DataUtil {

	public static Calendar primeiroDiaUtil(Calendar data) {

		int diaDaSemana = data.get(Calendar.DAY_OF_WEEK);

		if (diaDaSemana == Calendar.SATURDAY)
			data.add(Calendar.DAY_OF_MONTH, 2);
		else if (diaDaSemana == Calendar.SUNDAY)
			data.add(Calendar.DAY_OF_MONTH, 1);

		return data;
	}
}
