package birthtech.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import birthtech.entities.Child;
import birthtech.entities.Labour;
import birthtech.entities.Martenal;
import birthtech.interfaces.IChildService;
import birthtech.repositories.ChildRepository;

@Service
public class ChildService implements IChildService {
	@Autowired
	private ChildRepository repo;

	@Override
	public Child addChild(String gender, String name, String surname,
			Martenal mother, Labour labour) {
		Child child = new Child();
		child.setGender(gender);
		child.setSurname(surname);
		child.setName(name);
		child.setLabour(labour);
		child.setMartenal(mother);
		Child savedChild = repo.save(child);
		return savedChild;
	}

	@Override
	public List<Child> getChildren() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	@Override
	public List<Child> getChildren(Martenal mother) {
		 
		
		return null;
	}

	@Override
	public Child getChil(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
