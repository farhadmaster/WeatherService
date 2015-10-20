package model;

import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class FileModel {
	
	private String fileDate;
	private String fileSeq ;
	private String filename;
	private Boolean fileReaded;
	private int orderId;
	
	@Inject
	public FileModel (){
	
	}
	
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	public String getFileSeq() {
		return fileSeq;
	}
	public void setFileSeq(String fileSeq) {
		this.fileSeq = fileSeq;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Boolean getFileReaded() {
		return fileReaded;
	}
	public void setFileReaded(Boolean fileReaded) {
		this.fileReaded = fileReaded;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	

}
