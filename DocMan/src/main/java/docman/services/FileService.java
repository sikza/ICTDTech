package docman.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import useraccount.soap.client.UAServiceClient;
import entities.FileInfo;
import entities.FileRequest;
import entities.FileShare;
import ftp.FTPConnection;

/**
 * Session Bean implementation class FileService
 */
@Stateless
@LocalBean
@WebService(endpointInterface = "docman.services.FileServiceRemote", portName = "SOAPFileService", serviceName = "FileService")
public class FileService implements FileServiceRemote, FileServiceLocal {

	@PersistenceContext(unitName = "DocMan")
	private EntityManager em;

	public FileService() {

	}

	@Override
	public boolean addFile(FileInfo file, String fileSource, InputStream is) {
		FTPClient ftpClient = new FTPConnection().connect();
		InputStream ins = is;
		boolean done = false;
		if (ftpClient.isConnected()) {
			System.out.println("Connection successfully established...");
			if (ins != null) {
				try {
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
					ftpClient.makeDirectory(file.getUploadedBy().replace(" ",
							"_"));
					String filePath = file.getFilePath() + ".pdf";
					file.setFilePath(filePath);
					done = ftpClient.storeFile(file.getFilePath(), ins);
					if (done) {
						em.persist(file);

					}
				} catch (FileNotFoundException e) {
					System.out.println("File not file...start over");
				} catch (IOException e) {
					System.out.println("IO exception");
				} finally {
					try {
						ins.close();
						ftpClient.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (done) {
					System.out.println("File was successfully uploaded!!!");
				} else {
					System.out.println("Something went wrong :( ");
				}

			}

		} else {
			System.out.println("ERROR. could not connect to the ftp server");
		}

		return false;
	}

	@Override
	public FileInfo findByName(String fileOnwer, String fileName) {
		String filePath = fileOnwer + "/" + fileName;
		FTPClient ftpClient = new FTPConnection().connect();
		String remoteFile = fileOnwer + "/" + fileName + "";
		File download = new File("/home/Desktop/" + fileName + ".pdf");
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					download));
			Boolean success = ftpClient.retrieveFile(remoteFile, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateFile(int key, FileInfo info) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeFile(int key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<FileInfo> getFiles() {
		System.out.println("Start get file one");

		FileInfo f2 = em.find(FileInfo.class, 2);
		System.out.println("file 1 done");

		FileInfo f3 = em.find(FileInfo.class, 3);
		List<FileInfo> files = new ArrayList<FileInfo>();

		files.add(f2);
		files.add(f3);
		return files;
	}

	@Override
	public List<FileInfo> getFilesByOwner(String fileOwner) {

		List<FileInfo> files = em
				.createQuery(
						"SELECT e From FileInfo e WHERE UploadedBy='"
								+ fileOwner + "'").getResultList();

		return files;
	}

	public List<FileInfo> getUserDocuments(String requester, String owner) {
		UAServiceClient client = new UAServiceClient();
		List<FileInfo> files = null;
		if (client.isUserMemberOf(requester, "SAGOV")) {
			files = em
					.createQuery(
							"SELECT e From FileInfo e WHERE UploadedBy='"
									+ owner + "'").getResultList();
		}
		return files;
	}

	@Override
	public boolean downloadFile(String remotePath, String localDest) {
		FTPClient ftpClient = new FTPConnection().connect();
		boolean downloaded = false;
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			FileOutputStream fos = new FileOutputStream(localDest);
			downloaded = ftpClient.retrieveFile(remotePath, fos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return downloaded;
	}

	@Override
	public FileInfo getFileById(String username, int Id) {
		FileInfo fi = em.find(FileInfo.class, Id);
		return fi;
	}

	@Override
	public FileInfo getFileByUsername(String username) {
		return null;
	}

	@Override
	public boolean saveRequest(FileRequest request) {
		em.persist(request);
		return false;
	}

	@Override
	public List<FileRequest> getRequests(String forUser) {
		List<FileRequest> listReq = em.createQuery(
				"SELECT e FROM FileRequest e WHERE ReqTo='" + forUser + "'")
				.getResultList();
		return listReq;
	}

	@Override
	public boolean shareFile(FileInfo fi, String shareWith) {
		boolean shared = false;
		FileShare share = new FileShare();
		share.setSharedWith(shareWith);
		share.setFile(fi);
		em.persist(share);
		if (!em.contains(share)) {
			shared = false;
			System.out.println("\n Something went wrong :(");

		} else {
			shared = true;
		}

		return shared;
	}

	@Override
	public boolean removeRequest(FileRequest request) {
		em.remove(em.merge(request));
		if (em.contains(request)) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public FileRequest findRequestById(int reqId) {
		FileRequest req = new FileRequest();
		req = em.find(FileRequest.class, reqId);
		return req;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileShare> getSahres(String useredWith) {
		List<FileShare> listShares = null;
		listShares = em.createQuery(
				"SELECT e FROM FileShare e WHERE SharedWith='" + useredWith
						+ "'").getResultList();
		return listShares;
	}

}
