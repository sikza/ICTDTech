package birthtech.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import birthtech.entities.Checkup;
import birthtech.entities.Martenal;
import birthtech.interfaces.ICheckupService;
import birthtech.repositories.CheckupRepository;

@Service
public class CheckupService implements ICheckupService {
	@Autowired
	private CheckupRepository repo;

	@Autowired
	private MartenalService MartenalService;

	@Override
	public boolean addCheckup(boolean delivered, int martenal, String nurse,
			String place, String comment) {
		Checkup checkup = new Checkup();
		checkup.setMartenalId(martenal);
		checkup.setDelivered(delivered);
		checkup.setNurse(nurse);
		checkup.setPlace(place);
		checkup.setComments(comment);
		repo.save(checkup);
		return false;
	}

	@Override
	public List<Checkup> getCheckups() {
		return repo.findAll();
	}

	@Override
	public List<Checkup> getCheckups(String fullname) {
		List<Checkup> checkups = repo.findAll();
		List<Checkup> checkupsFound = new ArrayList<Checkup>();
		if (checkups != null) {
			for (Checkup checkup : checkups) {
				checkupsFound.add(checkup);
			}
		}
		return checkupsFound;
	}

	@Override
	public Checkup getCheckupById(int key) {
		List<Checkup> checkups = repo.findAll();
		List<Checkup> checkupsFound = new ArrayList<Checkup>();
		if (checkups != null) {
			for (Checkup checkup : checkups) {
				if (checkup.getCheckupId() == key) {
					checkupsFound.add(checkup);
				}

			}
		}
		return checkupsFound.get(0);
	}

	@Override
	public Checkup getCheckup(String fullname) {
		int martenalId = 0;
		Checkup checkup = null;
		List<Martenal> martenal = MartenalService.getMartenal();
		for (Martenal mat : martenal) {
			if ((mat.getNames() + " " + mat.getSurname()).equals(fullname)
					|| (mat.getSurname() + " " + mat.getSurname())
							.equals(fullname)) {
				martenalId = mat.getMid();
				System.out.println("ID found :" + martenalId);
				checkup = repo.findOne(martenalId);
				break;
			}
		}
		return checkup;
	}
}
