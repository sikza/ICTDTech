package birthtech.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import birthtech.entities.Checkup;
import birthtech.interfaces.ICheckupService;
import birthtech.repositories.CheckupRepository;

@Service
public class CheckupService implements ICheckupService {
	@Autowired
	private CheckupRepository repo;

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
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	@Override
	public List<Checkup> getCheckups(int key) {
		// TODO Auto-generated method stub
		return null;
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
	public Checkup getCheckup(int key) {
		List<Checkup> checkups = repo.findAll();
		List<Checkup> checkupsFound = new ArrayList<Checkup>();
		if (checkups != null) {
			for (Checkup checkup : checkups) {
				if(checkup.getCheckupId()==key){
					checkupsFound.add(checkup);
				}
				
			}
		}
		return checkupsFound.get(0);
	}

	@Override
	public Checkup getCheckup(String fullname) {
		// TODO Auto-generated method stub
		return null;
	}
}
