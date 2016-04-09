package birthtech.services.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import birthtech.entities.Child;
import birthtech.entities.Martenal;
import birthtech.services.MartenalService;

 
@RestController
public class RestServices {
	@Autowired
	private MartenalService matSer;
	
	@RequestMapping( value="/home", method=RequestMethod.GET)
	public List<Martenal> serviceTest(@PathVariable String name){
		System.out.println("Masikisiki Lizo Printed from a service");
	return matSer.getMartenal();
	}

}
