package entities;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: FileInfo
 *
 */
@Entity
@XmlRootElement
public class FileInfo implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int fileId;
	private String fileName;
	private Date uploaded;
	private String uploadedBy;
	private String fileDir;

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getUploaded() {
		return uploaded;
	}

	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getFilePath() {
		return fileDir;
	}

	public void setFilePath(String filePath) {
		this.fileDir = filePath;
	}

	private static final long serialVersionUID = 1L;

}
