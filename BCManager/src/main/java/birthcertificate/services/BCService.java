package birthcertificate.services;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import docman.services.FileInfo;
import bcmanager.system.MessageType;
import bcmanager.system.SystemMessage;
import birthcertificate.cxf.clients.DocManClient;
import birthcertificate.cxf.clients.UAServiceClient;
import birthcertificate.entities.BCertificate;
import birthcertificate.entities.Labour;
import birthcertificate.processors.Person;
import birthcertificate.repositories.BCRepository;
import birthcertificate.routebuilders.UniversalMarshaller;

@Service
public class BCService {

	@Autowired
	private BCRepository repo;

	public int createCerticate(int bcNumber, Date creationDate,
			String birthNumber, String childNames, String surname,
			String gender, String maternalId) {

		BCertificate certificate = new BCertificate();
		certificate.setBcNumber(bcNumber);
		certificate.setCreationDate(creationDate);
		certificate.setBirthNumber(birthNumber);
		certificate.setChildNames(childNames);
		certificate.setSurname(surname);
		certificate.setGender(gender);
		certificate.setMaternalId(maternalId);
		// repo.save(certificate);
		return 0;
	}

	public List<BCertificate> getAll() {

		return repo.findAll();
	}

	public List<BCertificate> getCollectReady() {
		List<BCertificate> collectReady = new ArrayList<BCertificate>();
		for (BCertificate bc : repo.findAll()) {
			if (bc.isCollectReady()) {
				collectReady.add(bc);
			}
		}
		return collectReady;
	}

	public BCertificate getCertificateById(String Id) {
		BCertificate certifice = repo.findOne(Integer.parseInt(Id));
		return certifice;
		// TODO Auto-generated method stub

	}

	public BCertificate getCertificateByParentId(String idNumber) {
		for (BCertificate bc : repo.findAll()) {
			if (bc.getMaternalId().equals(idNumber)) {
				return bc;
			}
		}
		return null;
	}

	public int generateBCNumber(Labour lab) {
		System.out.println("Trying to generate a Birth Certicate Id for:"
				+ lab.getChild().getSurname());
		SimpleDateFormat fmt = new SimpleDateFormat("yy-MM-dd");
		String date = fmt.format(new Date());
		String[] dateParts = date.split("-");
		String gen = lab.getChild().getGender().toLowerCase().equals("male") ? "0"
				: "1";
		String bcnumStr = dateParts[0] + dateParts[1] + dateParts[2] + gen
				+ 999;
		int bcNumber = Integer.parseInt(bcnumStr);
		return bcNumber;
	}

	public SystemMessage batchApplications(Labour[] applications) {
		SystemMessage message = new SystemMessage();
		int countValid = -1;
		try {
			for (Labour lab : applications) {
				if (lab.getChild() != null) {
					countValid++;
					if (!exists(lab)) {
						message = createCerticate(lab);
					}
				}
			}
			System.out.println(countValid + "  entities were processed!!!");
		} catch (Exception e) {
			System.out.println("Something went wrong! >> 'batchApplication'");
			message.setMessage(MessageType.Exception);
			message.setMessage(e.getMessage());
		}
		return message;
	}

	public SystemMessage createCerticate(Labour labour) {
		SystemMessage message = new SystemMessage();
		BCertificate application = new BCertificate();
		try {
			String fullName = labour.getMaternal().getSurname() + " "
					+ labour.getMaternal().getNames();
			application.setBcNumber(generateBCNumber(labour));
			application.setBcNumber(labour.getBirthNo());
			application.setBirthNumber("" + labour.getBirthNo());
			application.setChildNames(labour.getChild().getName());
			application.setSurname(labour.getChild().getSurname());
			application.setGender(labour.getChild().getGender());
			application.setParentFullNames(fullName);
			application.setMaternalId(labour.getMaternal().getPid() + "");
			application.setCollectReady(false);
			application.setCreationDate(new Date());
			BCertificate certificate = repo.save(application);
			message.setMessage("Completed :" + certificate.getBirthNumber());
		} catch (Exception e) {
			message.setMessage(MessageType.Exception);
			message.setMessage(e.getMessage());
		}
		return message;
	}

	public useraccount.soap.services.Person findParent(String fullName) {
		UAServiceClient client = new UAServiceClient();
		useraccount.soap.services.Person person = client
				.findPersonByFullName(fullName);
		return person;
	}

	public List<FileInfo> getUserDocument(String requester, String docOwner) {
		DocManClient client = new DocManClient();
		return client.getDocument(requester, docOwner);
	}

	@Transactional
	public int updateCollectionStatus(boolean status, String parent) {
		return repo.updateCollectionStatus(status, parent);

	}

	@Transactional
	public int updateCollectionStatus(boolean status, int certificateNo) {
		return repo.updateCollectionStatus(status, certificateNo);

	}

	// TODO
	public boolean exists(Labour lab) {
		for (BCertificate cert : repo.findAll()) {
			if ((Integer.parseInt(cert.getBirthNumber())) == (lab.getBirthNo())) {
				return true;
			}
		}
		return false;
	}
}
