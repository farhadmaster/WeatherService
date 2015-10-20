package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;




import play.Play;

@Singleton
public class WeatherFTPReader {

	@Inject CSVUtil csvUtil; 
	@Inject ZipUtil zipUtil;

	public  List<String> listFileName(){
		FTPClient ftpclient = null;
		try {
			ftpclient = openFTPConnection();
			FTPFile[] ftpFiles = ftpclient.listFiles();
			List<String> filenames = new ArrayList<String>();
            for (FTPFile ftpFile : ftpFiles) {
                // Check if FTPFile is a regular file
                if (ftpFile.getType() == FTPFile.FILE_TYPE) {
                	filenames.add(ftpFile.getName());
                }
            }
            return filenames ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if (ftpclient != null ){
				try {
					ftpclient.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	} 
	private FTPClient openFTPConnection() {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(Play.application().configuration().getString("ftp.server.address"));
			ftpClient.login(Play.application().configuration().getString("ftp.server.username"), Play.application().configuration().getString("ftp.server.password"));
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			boolean directoryChanged = ftpClient.changeWorkingDirectory("incoming");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return ftpClient;
	}

	public List<Map<String,String>> readCSVFileToMap(String filename){
		FTPClient ftpClient =null;
		try {
			ftpClient = openFTPConnection();
			InputStream inputstream = ftpClient.retrieveFileStream(filename);
			boolean check = ftpClient.completePendingCommand();
			if(check){
				InputStream input = null;
				input = new CompressorStreamFactory().createCompressorInputStream("bzip2",inputstream);
				if (ftpClient != null ){
					ftpClient.disconnect();
				}
				return(csvUtil.readCsv(input));
			}

		} catch (CompressorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

