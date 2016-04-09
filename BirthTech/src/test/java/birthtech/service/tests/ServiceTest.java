package birthtech.service.tests;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import birthtech.entities.Checkup;
import birthtech.entities.Labour;
import birthtech.entities.Martenal;
import birthtech.services.CheckupService;
import birthtech.services.LabourService;
import birthtech.services.MartenalService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/applicationContext.xml")
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ServiceTest {

	@Autowired
	private MartenalService martenalService = null;
	@Autowired
	private CheckupService checkService = null;
	@Autowired
	private LabourService labourService = null;

	@Test
	public void should_create_martenal() {
		Date d = new Date();
		martenalService
				.addMartenal("Sisanda", "Masikisiki", 600, new Date(), "Lizo Masikisiki");
		 
	
	}

	@Test
	public void should_add_checkup() {
		 
		Martenal mat = martenalService.getMartenal().get(0);
		checkService.addCheckup(false, mat.getIdnumber(), "mabhule",
				"holy cross", "Petient is fine, the baby is fine");
		System.out.println("The size is :" + checkService.getCheckups().size());
	}

	@Test
	public void shoud_add_labour() {

		Labour labour = new Labour();
		Martenal parent=martenalService.getMartenal().get(0);
		labour.setBirthNo(123);
		labour.setBirthPlace("Flagstaff");
		labour.setNurse("Mabhulu");
		labour.setMartenal(parent);
		labour.setStatus(1);
		System.out.println("Parent found....."+parent.getNames());
		 labourService.addLabour(labour.getLabourDate(),
		 	labour.getBirthPlace(), labour.getStatus(),
		 	labour.getMartenal(), labour.getNurse(), labour.getBirthNo());
		 

	}

	@Test
	public void should_find_chechup_by_fullname() {
		Checkup c = checkService.getCheckup("lizo masikisiki");
		System.out.println(c == null ? "yes null" : c.getComments());
	}
}
