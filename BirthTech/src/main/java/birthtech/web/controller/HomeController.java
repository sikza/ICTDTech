package birthtech.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.MatrixParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import birthtech.entities.Checkup;
import birthtech.entities.Child;
import birthtech.entities.Labour;
import birthtech.entities.Maternity;
import birthtech.services.CheckupService;
import birthtech.services.ChildService;
import birthtech.services.LabourService;
import birthtech.services.MaternityService;

@Controller
public class HomeController {
	@Autowired
	private MaternityService matService;
	@Autowired
	private CheckupService checkService;
	@Autowired
	private LabourService labService;
	@Autowired
	private ChildService childService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView homepage() {
		return new ModelAndView("home");
	}

	// CRUD starts
	@RequestMapping(value = "maternity/getall", method = RequestMethod.GET)
	public ModelAndView findAll() {
		ModelAndView model = new ModelAndView("getallmartenals");
		List<Maternity> getAll = matService.getMartenal();
		model.addObject("results", getAll);
		return model;
	}

	@RequestMapping(value = "maternity/new", method = RequestMethod.GET)
	public ModelAndView newmartenal() {
		return new ModelAndView("newmartenalform");
	}

	@RequestMapping(value = "maternity/create", method = RequestMethod.GET)
	public ModelAndView newrecord(ModelMap modelmap,
			HttpServletRequest request, Maternity mat) {
		ModelAndView model = new ModelAndView("viewmaternal");
		modelmap.addAttribute(mat.getNames(), "names");
		modelmap.addAttribute(mat.getNurse(), "nurse");
		modelmap.addAttribute(mat.getSurname(), "surname");
		modelmap.addAttribute(mat.getIdnumber());
		int idNumber = Integer.parseInt(request.getParameter("idNumber"));
		Date date = new Date();
		mat.setIdnumber(idNumber);
		mat.setRegistration(date);
		boolean saved = matService.addMaternity(mat);
		model.addObject("maternal", mat);
		return model;
	}

	@RequestMapping(value = "maternity/current", method = RequestMethod.GET)
	public ModelAndView maternity(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("viewcurrent");
		String id = request.getParameter("idnumber");
		int intId = Integer.parseInt(id);
		Maternity patient = matService.getMaternityById(intId);
		model.addObject("maternity", patient);
		return model;
	}

	@RequestMapping(value = "maternity/view/{idnumber}", method = RequestMethod.GET)
	public ModelAndView viewmaternal(
			@RequestParam(value = "idnumber", required = false) String idnumber) {
		ModelAndView model = new ModelAndView("viewmaternal");
		Maternity mat = null;
		mat = matService.getMaternityById(Integer.parseInt(idnumber));
		model.addObject("maternal", mat);
		return model;
	}

	@RequestMapping(value = "maternity/edit/{idnumber}", method = RequestMethod.GET)
	public ModelAndView editmaternal(
			@PathVariable(value = "idnumber") String idnumber) {
		ModelAndView model = new ModelAndView("editmaternalform");
		Maternity mat = null;
		mat = matService.getMaternityById(40);
		System.out.println("\n" + mat.getNames() + " " + mat.getSurname()
				+ " with ID " + mat.getIdnumber() + " is being edited");
		model.addObject("maternal", mat);
		return model;
	}

	@RequestMapping(value = "maternity/search", method = RequestMethod.GET)
	public ModelAndView search() {

		return new ModelAndView("current");
	}

	@RequestMapping(value = "maternity/checkup/all", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Checkup> current(String idnumber) {
		int intId = Integer.parseInt(idnumber);
		List<Checkup> checkups = checkService.getCheckups(intId);

		return checkups;
	}

	// POST CHECKUP
	@RequestMapping(value = "maternity/checkup/adds", method = RequestMethod.POST)
	public boolean updatecheckup(@RequestBody Checkup checkup) {

		return false;
	}

	// CRUD ends
	@RequestMapping(value = "maternity/checkup/add", method = RequestMethod.GET)
	public ModelAndView addCheckup(ModelMap map, Checkup checkup,
			HttpServletRequest request) {
		String id = request.getParameter("maternity");
		map.addAttribute("maternity", checkup.getMartenalId());
		map.addAttribute("nurse", checkup.getNurse());
		map.addAttribute("place", checkup.getPlace());
		map.addAttribute("comments", checkup.getComments());
		checkup.setMartenalId(Integer.parseInt(id));
		checkup.setDate(new Date());
		checkup.setDelivered(false);
		String delivery = request.getParameter("delivered");
	
		Maternity maternity = matService.getMaternityById(Integer.parseInt(id));
		if (delivery.trim().equals("yes")) {
			String labourstatus= request.getParameter("labourstatus");
			
			checkup.setDelivered(true);
			Labour lab = new Labour();
			lab.setMaternal(maternity);
			lab.setBirthNo(1862);
			lab.setBirthPlace(checkup.getPlace());
		 if(labourstatus.trim().equals("success")){lab.setStatus(1);}else{lab.setStatus(0);}
			lab.setLabourDate(new Date());
			lab.setNurse(checkup.getNurse());
			checkService.addCheckup(checkup);
			
			if(labourstatus.trim().equals("success")){
			Child child = new Child();
			child.setGender(request.getParameter("gender"));
			child.setName(request.getParameter("childNames"));
			child.setSurname(request.getParameter("childSurname"));
			Child chld = childService.save(child);
			lab.setChild(chld);
			}
			lab = labService.add(lab);
		} else {
			checkService.addCheckup(checkup);

		}
		return new ModelAndView("current");
	}
}