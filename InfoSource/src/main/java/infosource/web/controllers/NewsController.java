package infosource.web.controllers;

import infosource.open.services.access.TransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NewsController {

	@RequestMapping(value = "news/getFeeds", method = RequestMethod.GET,produces="application/json")
	public String showFeeds() {
		TransactionManager transactionService = new TransactionManager();
		return "masikisiki";
	}
}
