package com.purbon.pdfwriter.chart.Impl.dicts;

import com.purbon.pdfwriter.chart.Translator;

public class DefaultTranslator implements Translator {

	public String translateWithUnits(String code) {
		if ("U".equalsIgnoreCase(code)) {
			return "Voltage in Volts";
		} else if ("P".equalsIgnoreCase(code)) {
			return "Active Power in Watt";
		} else if ("I".equalsIgnoreCase(code)) {
			return "Current in Ampere";
		} else if ("Q".equalsIgnoreCase(code)) {
			return "Reactive Power in VAr";
		}
		return code;
	}
	
	public String translate(String code) {
		if ("I".equalsIgnoreCase(code)) {
			return "Voltage";
		} else if ("Q".equalsIgnoreCase(code)) {
			return "Active Power";
		} else if ("U".equalsIgnoreCase(code)) {
			return "Current";
		} else if ("P".equalsIgnoreCase(code)) {
			return "Reactive Power";
		}
		return "";
	}
}
