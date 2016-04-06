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
	public boolean addMartenal(String fullname, Date date, String nurse) {
		Martenal mat= new Martenal();
		mat.setPid(2);
		mat.setNames(fullname);
		mat.setIdnumber(600);
		mat.setSurname("masiksiki");
		repo.save(mat);
		System.out.println("New Martenal was successfully added...");
		return true;
	}

	@Override
	public List<Martenal> getMartenal() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}



}