package webFrame.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import webFrameApp.entites.Domain;
import webFrameApp.entites.FeedComment;
import webFrameApp.entites.FeedPost;
import webFrameApp.enumerations.DomainCategory;
import webFrameApp.serviceLgoic.DomainDAOImpl;
import webFrameApp.serviceLgoic.EntityDAOImpl;
import webFrameApp.serviceLgoic.FeedCommentImpl;
import webFrameApp.serviceLgoic.FeedPostImpl;
import webFrameApp.serviceLgoic.TypeDAOImpl;
 
@Controller
public class domainController {
	@Autowired
	private DomainDAOImpl domainImp;
	@Autowired
	private EntityDAOImpl entityIMPL;

	@Autowired
	private TypeDAOImpl typeIMPL;

	@Autowired
	private FeedPostImpl feed;
	@Autowired
	private FeedCommentImpl comment;

	@RequestMapping(value = "web/admin", method = RequestMethod.GET)
	public ModelAndView login(ModelMap model, HttpServletRequest request) {
		ModelAndView mod;
		boolean isUser = false, isMaster = false, isAdmin = false;
		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		Collection<? extends GrantedAuthority> roles = SecurityContextHolder
				.getContext().getAuthentication().getAuthorities();

		for (GrantedAuthority grantedAuthority : roles) {
			if (grantedAuthority.getAuthority().equals("ROLE_User")) {
				isUser = true;

			} else if (grantedAuthority.getAuthority().equals("ROLE_Admin")) {
				isAdmin = true;

			} else if (grantedAuthority.getAuthority().equals("ROLE_Master")) {
				isMaster = true;

			}

		}

		if (isMaster) {
			List<Domain> domain = domainImp.getAllDomains();
			mod = new ModelAndView("masterpage");
			mod.addObject("domains", domain);
		} else if (isAdmin == true) {

			mod = new ModelAndView("adminhome");
			String creator = user;

			if (creator != null) {

				List<Domain> found = domainImp.findByCreator(creator);

				try {
					if (found.size() != 0) {
						String[] domains = new String[found.size()];
						int x = 0;
						for (Domain dm : found) {
							domains[x] = dm.getDomainName();
							x++;
						}

						mod.addObject("domains", domains);
					} else {
						mod = new ModelAndView("errorpage");
						String linkHome = "<a href='http://www.google.com'>Go home</a>";
						mod.addObject("errorMessage",
								"<b>No Domain Found For You Sorry, Are you sure you are the admin </b> <br/>"
										+ linkHome);
						return mod;

					}
				} catch (Exception ex) {
					System.out.println("you have an exception :  \n \n \n "
							+ ex.getClass());
				}

			}

		} else {

			mod = new ModelAndView("userhome");
		}

		return mod;

	}

	@RequestMapping(value = "attributeForm", method = RequestMethod.GET)
	public ModelAndView attForm(@ModelAttribute("webFrame") ModelMap modelpm,
			Domain dmain, HttpServletRequest request) {
		ModelAndView model = new ModelAndView("attForm");
		int numOfAtt = Integer.parseInt(request.getParameter("att_num").trim());
		String[] dataType = { "VARCHAR", "INT", "DOUBLE", "TEXT", "ENUM" };
		// String [] jspTypes={"text","text","text","textarea","select"};
		String dname = request.getParameter("domainName");
		String creator = request.getParameter("creator");

		model.addObject("domainName", dname);
		model.addObject("creator", creator);
		model.addObject("numOfAtt", numOfAtt);
		model.addObject("dtypes", dataType);

		return model;
	}
	 
	@RequestMapping(value = "/web/createdomainrequest", method = RequestMethod.GET)
	public ModelAndView newDomain() {

		List<DomainCategory> domainCategories = new ArrayList<DomainCategory>();
		for (DomainCategory c : DomainCategory.values()) {
			domainCategories.add(c);
		}
	 
		
		ModelAndView model = new ModelAndView("createdomainform");
		model.addObject("categories", domainCategories);
		return model;
	}

	@RequestMapping(value = "/web/createDomain", method = RequestMethod.GET)
	public ModelAndView createDomain(
			@ModelAttribute("webFrame") ModelMap modelmp, Domain dmain,
			HttpServletRequest request) throws SQLException {
		ModelAndView model = new ModelAndView("showdomain");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		String domainCreator = request.getParameter("domainCreator");
		String domainName = request.getParameter("domainName");
		String domainOrg = request.getParameter("domainOrg");
		String contact = request.getParameter("contact");
		String category = request.getParameter("category");
		String description = request.getParameter("description");

		if (auth != null) {
			domainCreator = auth.getName();

		}
		dmain.setCategory(category);
		dmain.setContactDetails(contact);
		dmain.setDescription(description);
		dmain.setDomainName(domainName);
		dmain.setOrgName(domainOrg);
		dmain.setCreator(domainCreator);

		domainImp.createDomain(domainCreator, domainName, category, domainOrg,
				contact, description);
		model.addObject("domain", dmain);

		return model;
	}

	//

	@RequestMapping(value = "/web/viewAll", method = RequestMethod.GET)
	public ModelAndView viewAll(@ModelAttribute("webFrame") ModelMap modelpm,
			Domain dmain, HttpServletRequest request) throws SQLException {
		ModelAndView model = new ModelAndView("viewAll");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String roles = auth.getAuthorities().toString();
		System.out.println("\n \n \n  Look Chap... These are the roles.."
				+ roles);
		String user;
		if (auth != null) {
			user = auth.getName();
			if (user != null && roles.contains("ROLE_Master")) {
				List<Domain> dmn = domainImp.getAllDomains();
				model.addObject("domains", dmn);
			} else {
				List<Domain> dmn = domainImp.findByCreator(user);
				model.addObject("domains", dmn);
			}

		}

		return model;

	}

	@RequestMapping(value = "/web/purgeDomain", method = RequestMethod.GET)
	public ModelAndView purgeDomain(HttpServletRequest request) {
		String name = request.getParameter("domainName");
		Domain dom = domainImp.findDomain(name);
		domainImp.purgeDomain(dom);
		ModelAndView model = new ModelAndView("requestContent");
		// // this needs to be changed, it show all domains even to people with
		// restricted access
		model.addObject("AllDomains", domainImp.getAllDomains());
		return model;

	}

	@RequestMapping(value = "/web/domainLoginRequest", method = RequestMethod.GET)
	public ModelAndView domainLoginRequest(HttpServletRequest request) {
		// ModelAndView model = new ModelAndView("domainLogin");
		// personDAOimpl personImpl=new personDAOimpl();
		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		// person p=personImpl.findByUsername(user);

		// Collection<? extends GrantedAuthority> authorities =
		// authentication.getAuthorities();

		ModelAndView model = new ModelAndView("domainHome");
		HttpSession session = request.getSession();
		session.setAttribute("user", user);

		System.out.println();

		model.addObject("currentUser", "lizo"/*
											 * p.getFirst_name()+" "+p.getLast_name
											 * ()
											 */);
		String creator = "Lizo";
		if (!creator.equals("") || (creator != null)) {
			List<Domain> found = domainImp.findByCreator(creator);
			try {
				if (found.size() != 0) {
					String[] domains = new String[found.size()];
					int x = 0;
					for (Domain dm : found) {
						domains[x] = dm.getDomainName();
						x++;
					}

					model.addObject("domains", domains);
				} else {
					ModelAndView mod = new ModelAndView("errorpage");
					String linkHome = "<a href='http://www.google.com'>Go home</a>";
					mod.addObject("errorMessage",
							"<b>No Domain Found For You Sorry, Are you sure you are the admin </b> <br/>"
									+ linkHome);
					return mod;

				}
			} catch (Exception ex) {
				System.out.println(ex.getClass());

			}
			;
		}

		return model;
		// return model;

	}

	@RequestMapping(value = "/web/domainLogin", method = RequestMethod.GET)
	public ModelAndView domainLogin(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		ModelAndView model = new ModelAndView("domainHome");
		String creator = request.getParameter("creatorName").trim();
		if (!creator.equals("") || (creator != null)) {
			List<Domain> found = domainImp.findByCreator(creator);
			try {
				if (found.size() != 0) {
					String[] domains = new String[found.size()];
					int x = 0;
					for (Domain dm : found) {
						domains[x] = dm.getDomainName();
						x++;
					}

					model.addObject("domains", domains);
				} else {
					ModelAndView mod = new ModelAndView("errorpage");
					String linkHome = "<a href='http://www.google.com'>Go home</a>";
					mod.addObject("errorMessage",
							"<b>No Domain Found For You Sorry, Are you sure you are the admin </b> <br/>"
									+ linkHome);
					return mod;

				}
			} catch (Exception ex) {
				System.out.println(ex.getClass());

			}
			;
		}

		return model;

	}

}
