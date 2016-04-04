package birthtech.services;

import java.util.Date;
import java.util.List;

import birthtech.entities.Parent;

public interface IParentService {
	public Parent getParent(String fullname);
	public boolean saveLocalParent(Parent parent);
public List<Parent> listParents();
	public void addChild(String newbornName, String newbornSurname,
			Date dateofbith, String birhnumber, String gender, String status);
}
