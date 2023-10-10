package com.anandhuarjunan.imagetools.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Utils {

	
	public static Iterable<CSVRecord> readCsv(String filePath) throws IOException {
		Reader in = new FileReader(Utils.class.getResource(filePath).getPath());
		return CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
	}
	
	public static Iterable<CSVRecord> readAlgorithmsDataCsv() throws IOException {
		return readCsv("/data/algorithms.csv");
	}
	
	

}
