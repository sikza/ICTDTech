package birthtech.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import birthtech.entities.Martenal;
import birthtech.interfaces.IMartenalService;
import birthtech.repositories.MartenalRepository;

@Service
public class MartenalService implements IMartenalService {
	
@Autowired	
private MartenalRepository repo;
	@Override
	public boolean addMartenal(String names,String surname,int idnumber, Date registration,String nurse) {
		Martenal mat= new Martenal();
		mat.setNames(names);
		mat.setSurname(surname);
		mat.setRegistration(new Date());
		mat.setNurse(nurse);
		mat.setIdnumber(idnumber);
		repo.save(mat);
		System.out.println("New Martenal was successfully added...");
		return true;
	}

	@Override
	public List<Martenal> getMartenal() {
				return repo.findAll();
	}



}
