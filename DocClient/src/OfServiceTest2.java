
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.swing.JFrame;

import sessionBeans.FileService;
import sessionBeans.FileServiceRemote;
import entities.FileInfo;

public class OfServiceTest2 {

	public static void main(String[] args) throws NamingException, IOException {
		FileServiceRemote fs = (FileServiceRemote) InitialContext
				.doLookup("DocProject/DocMan/FileService!sessionBeans.FileServiceRemote");
		FileInfo fi = fs.getFileById("user3", 22);
		File f = new File(fi.getFileName()+".pdf");
		FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
		System.out.println(f.getAbsolutePath());
		boolean dwn = fs.downloadFile(fi.getFilePath(), f.getAbsolutePath());
		fos.close();
		if (dwn) {

			System.out.println("File downloaded successfully...");
		} else {
			System.out.println("oopps.");

		}

		try {
			Thread.sleep(Integer.MAX_VALUE);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
