package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


@Singleton
public class CSVUtil {
	
	public List<Map<String,String>> readCsv(InputStream input){
		Reader reader = new InputStreamReader(input);
		return readCsv(reader);
	}
	
	public List<Map<String,String>> readCsv(Reader reader){
		
		List<Map<String,String>> output = new ArrayList<>();
		CSVParser csvParser = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader().withDelimiter(';').withSkipHeaderRecord().withIgnoreSurroundingSpaces();
		try {
			//initialize CSVParser object
			csvParser = new CSVParser(reader, csvFileFormat);
			//Get a list of CSV file records
			List<CSVRecord> csvRecords = csvParser.getRecords(); 
			//Read the CSV file records starting from the second record to skip the header
			for (CSVRecord record : csvRecords) {
				 output.add(record.toMap());
			}
		return output;
		
		} 
		catch (Exception e) {
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();

		} finally {
			try {
				reader.close();
				csvParser.close();
			} catch (IOException e) {
				System.out.println("Error while closing fileReader/csvFileParser !!!");
				e.printStackTrace();
			}
		}
		return null;

}
}
