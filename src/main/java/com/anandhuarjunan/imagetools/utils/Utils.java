package com.anandhuarjunan.imagetools.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Utils {

	
	public static Iterable<CSVRecord> readCsv(String filePath) throws IOException {
        InputStream inputStream = Utils.class.getResourceAsStream(filePath);
		Reader in =  new InputStreamReader(inputStream);
		return CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
	}
	
	public static Iterable<CSVRecord> readAlgorithmsDataCsv() throws IOException {
		return readCsv("/data/algorithms.csv");
	}
	
	

}
