package infosource.health.ematclinic.services;

import infosource.health.ematclinic.enums.LabourStatusEnum;
import infosource.health.ematclinic.interfaces.ILabourService;
import infosource.health.ematclinic.messaging.MessageSender;
import infosource.health.ematclinic.messaging.UniversalMarshaller;
import infosource.health.ematclinic.models.Child;
import infosource.health.ematclinic.models.Labour;
import infosource.health.ematclinic.models.Maternity;
import infosource.health.ematclinic.repositories.LabourRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class LabourService implements ILabourService {
	@Autowired
	private LabourRepository labourRepo;

	@Override
	public boolean addLabour(Date date, String place, int status,
			Maternity parent, String nurse, int birthnumber) {
		Labour labour = new Labour();
		labour.setBirthNo(birthnumber);
		labour.setLabourDate(date);
		labour.setMaternal(parent);
		labour.setStatus(status);

		Labour lab = labourRepo.save(labour);
		if (lab != null) {
			MessageSender sender = new MessageSender();
			Gson jsonMapper = new Gson();
			sender.sendMessage(jsonMapper.toJson(lab));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Labour getLabour(int lid) {
		List<Labour> listLabour = new ArrayList<Labour>();
		for (Labour labour : labourRepo.findAll()) {
			if (labour.getLabourId() == lid) {
				listLabour.add(labour);
			}
		}
		return listLabour.get(0);
	}

	@Override
	public List<Labour> getLabour() {
		System.out.println("Labour service method- list invoked...");
		return labourRepo.findAll();
	}

	@Override
	public List<Labour> getLabour(Maternity martenal) {
		List<Labour> listLabour = new ArrayList<Labour>();
		for (Labour labour : labourRepo.findAll()) {
			if (labour.getMaternal() == martenal) {
				listLabour.add(labour);
			}
		}
		return listLabour;
	}

	@Override
	public List<Labour> getLabour(Date date) {
		List<Labour> listLabour = new ArrayList<Labour>();
		for (Labour labour : labourRepo.findAll()) {
			if (labour.getLabourDate() == date) {
				listLabour.add(labour);
			}
		}
		return listLabour;
	}

	@Override
	public Labour add(Labour lab) {
		Labour labour = labourRepo.save(lab);
	//	if(labour.getChild()!=null){
			MessageSender sender = new MessageSender();
					UniversalMarshaller marshal = new UniversalMarshaller();
					sender.sendMessage(marshal.toXML(labour));
					System.out.println("\n \n \n \n \n done !!! \n \n \n");
		//	}
	//f (labour != null) {
	//	Gson jsonMapper = new Gson();
	//	System.out.println("\n \n \n  sending jms message "
	//			+ jsonMapper.toJson(labour) + " \n \n");
	//	MessageSender sender = new MessageSender();
	//	UniversalMarshaller marshal = new UniversalMarshaller();
	//	sender.sendMessage(marshal.toXML(lab));
    //
	//
		return labour;
	}

}
