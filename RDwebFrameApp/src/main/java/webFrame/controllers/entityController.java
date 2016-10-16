package webFrame.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.classfile.Attribute;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Attributes;

import webFrameApp.contentManagers.addContent;
import webFrameApp.contentManagers.contentLoader;
import webFrameApp.entites.Domain;
import webFrameApp.entites.OrgEntity;
import webFrameApp.entites.SqldataTypes;
import webFrameApp.enumerations.EntityAccessType;
import webFrameApp.enumerations.ListEnumerations;
import webFrameApp.serviceLgoic.DomainDAOImpl;
import webFrameApp.serviceLgoic.EntityDAOImpl;
import webFrameApp.serviceLgoic.TypeDAOImpl;

@Controller
public class entityController {

	@Autowired
	private DomainDAOImpl domainImp;
	@Autowired
	private EntityDAOImpl entityIMPL;

	@Autowired
	private TypeDAOImpl typeIMPL;

	@RequestMapping(value = "/web/requestDelete", method = RequestMethod.POST)
	public ModelAndView requestDelete() {
		ModelAndView model = new ModelAndView("entremove");
		List<OrgEntity> OrgEntity = entityIMPL.findAll();
		model.addObject("OrgEntity", OrgEntity);

		return model;
	}

	@RequestMapping(value = "/web/removeEntity", method = RequestMethod.POST)
	public ModelAndView removeEntity(HttpServletRequest request) {
		String name = request.getParameter("entity");
		entityIMPL.delete(entityIMPL.findByName(name).get(0));
		ModelAndView model = new ModelAndView("requestContent");
		model.addObject("AllDomains", entityIMPL.findAll());

		return model;

	}

	@RequestMapping(value = "web/showEntities", method = RequestMethod.GET)
	public @ResponseBody String showEntities(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String activedomain = request.getParameter("activeDomain");
		Domain dom = domainImp.findDomain(activedomain.trim());
		List<OrgEntity> ents = entityIMPL.findByDomain(dom.getDomainName());

		String arr = "<table><tr> <td>Select Entity</td><td><select onchange='showOptions()' name='activeEntity' id='activeEntity' >";

		for (OrgEntity e : ents) {
			arr = arr + "<option value='" + e.getName() + "'> " + e.getName()
					+ "</option>";

		}
		arr = arr + "</select></td></tr></table>";
		return arr;
	}

	@RequestMapping(value = "getentities", method = RequestMethod.GET)
	public @ResponseBody String[] getentities(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String domain = request.getParameter("domain");
		List<OrgEntity> ents = entityIMPL.findByDomain(domain);
		String[] ent = new String[ents.size()];
		int x = 0;
		for (OrgEntity e : ents) {
			ent[x] = e.getName();
			x++;
		}
		return ent;
	}

	@RequestMapping(value = "/web/loadEntities", method = RequestMethod.GET)
	public @ResponseBody String loadEntities(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String activedomain = request.getParameter("activeDomain");
		Domain dom = domainImp.findDomain(activedomain.trim());
		List<OrgEntity> ents = entityIMPL.findByDomain(dom.getDomainName());

		String arr = "<select onchange='showAttributes()' name='entity' id='entity' >";

		for (OrgEntity e : ents) {
			arr = arr + "<option value='" + e.getName() + "'> " + e.getName()
					+ "</option>";

		}
		arr = arr + "</select>";
		// <td><input type='button' value='Load' onclick='loadContent()'/></td>
		return arr;
	}

	@RequestMapping(value = "/web/loadTableAtrributes", method = RequestMethod.GET)
	public @ResponseBody String loadTableAtrributes(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String activeEntity = request.getParameter("entity");

		OrgEntity ents = entityIMPL.findByName(activeEntity).get(0);
		String[] attributes = ents.getAttributes();
		String arr = "<select onchange='loadAttributeType()' name='activeAttribute' id='activeAttribute' >";
		for (int x = 0; x < attributes.length; x++) {
			arr = arr + "<option value='" + attributes[x] + "'> "
					+ attributes[x] + "</option>";

		}
		arr = arr + "</select>";

		return arr;
	}

	@RequestMapping(value = "loadAttributeTypes", method = RequestMethod.GET)
	public @ResponseBody String loadTableTypes(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String activeAtt = request.getParameter("activeAttribute");
		String activeEntity = request.getParameter("entity");
		String type = "";
		OrgEntity ents = entityIMPL.findByName(activeEntity).get(0);
		String[] attributes = ents.getAttributes();
		String[] types = ents.getTypes();
		for (int x = 0; x < attributes.length; x++) {
			if (attributes[x].equals(activeAtt)) {
				type = types[x];
				break;
			}
		}

		String selectType = "<select id='attributeType' name='attributeType'>  <option  value='"
				+ type + "' >" + type + "</option></select>";

		return selectType;
	}

	@RequestMapping(value = "getjsoncontent", method = RequestMethod.GET)
	public @ResponseBody String viewContent(HttpServletRequest request) {

		contentLoader load = new contentLoader();
		String getDataFrom = request.getParameter("entity");
		List<OrgEntity> ents = entityIMPL.findByName(getDataFrom);
		OrgEntity ent = ents.get(0);
		String domain = ent.getDomain();
		String[] columns = load.getCoulumn(domain, getDataFrom);
		List<List<Object>> allData = load.getData(domain, getDataFrom);
		StringBuilder jsnStr = new StringBuilder("");
		jsnStr.append("{  \"data\" : [");
		for (int x = 0; x < allData.size(); x++) {
			jsnStr.append("{");
			for (int z = 0; z < columns.length; z++) {
				jsnStr.append(" \"" + columns[z] + "\":\""
						+ allData.get(x).get(z) + "\"");
				if (z < columns.length - 1) {
					jsnStr.append(",");
				}
			}
			jsnStr.append("}");
			if (x < allData.size() - 1) {
				jsnStr.append(",");
			}
		}
		jsnStr.append("]}");
		System.out.print(jsnStr.toString());
		return jsnStr.toString();
	}

	@RequestMapping(value = "getEntities", method = RequestMethod.POST)
	public ModelAndView getEntities(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("viewentities");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<Domain> doms = null;
		String user;
		if (auth != null) {
			String roles = auth.getAuthorities().toString();
			user = auth.getName();
			if (user != null && roles.contains("ROLE_Master")) {
				doms = domainImp.getAllDomains();

			} else {
				doms = domainImp.findByCreator(user);

			}

		}
		String domain = request.getParameter("domainName");
		Domain dom = domainImp.findDomain(domain);
		List<OrgEntity> ents = new ArrayList<OrgEntity>();
		if (dom != null) {
			ents = entityIMPL.findByDomain(dom.getDomainName());
		}
		model.addObject("domains", doms);
		model.addObject("OrgEntity", ents);
		return model;

	}

	@RequestMapping(value = "requestEntities", method = RequestMethod.POST)
	public ModelAndView requestEntities(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("viewentities");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<Domain> doms = null;
		String user;
		if (auth != null) {
			String roles = auth.getAuthorities().toString();
			user = auth.getName();
			if (user != null && roles.contains("ROLE_Master")) {
				doms = domainImp.getAllDomains();

			} else {
				doms = domainImp.findByCreator(user);

			}

		}
		model.addObject("domains", doms);

		return model;
	}

	@RequestMapping(value = "/web/showentities", method = RequestMethod.GET)
	public ModelAndView showentities(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView("enthome");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<Domain> doms = null;
		String user;
		if (auth != null) {
			String roles = auth.getAuthorities().toString();
			user = auth.getName();
			if (user != null && roles.contains("ROLE_Master")) {
				doms = domainImp.getAllDomains();

			} else {
				doms = domainImp.findByCreator(user);

			}

		}
		model.addObject("domains", doms);
		return model;
	}

	@RequestMapping(value = "/web/newEntityrequest", method = RequestMethod.GET)
	public ModelAndView newEntityrequest(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("createEntity");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<Domain> doms = new ArrayList<Domain>();
		String user;
		if (auth != null) {
			String roles = auth.getAuthorities().toString();
			user = auth.getName();
			if (user != null && roles.contains("ROLE_Master")) {
				doms = domainImp.getAllDomains();

			} else {
				doms = domainImp.findByCreator(user);

			}

		}
		model.addObject("domains", doms);

		return null;
	}

	@RequestMapping(value = "/web/newEntity", method = RequestMethod.GET)
	public ModelAndView newEntity(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("createEntity");

		String att_num = "1";
		// String domain = request.getParameter("domain");

		List<EntityAccessType> accessTypes = new ListEnumerations()
				.ListAccessType();
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<Domain> doms = new ArrayList<Domain>();
		String user;
		if (auth != null) {
			String roles = auth.getAuthorities().toString();
			user = auth.getName();
			if (user != null && roles.contains("ROLE_Master")) {
				doms = domainImp.getAllDomains();

			} else {
				doms = domainImp.findByCreator(user);

			}

		}
		model.addObject("domains", doms);
		List<SqldataTypes> types = typeIMPL.findAll();
		String[] dtype = new String[types.size()];
		int x = 0;
		for (SqldataTypes tp : types) {
			dtype[x] = tp.getName();
			x++;
		}

		model.addObject("att_number", att_num);
		// model.addObject("domain", domain);
		model.addObject("dtypes", dtype);
		model.addObject("allDomains", doms);
		model.addObject("accessTypes", accessTypes);

		return model;

	}

	@RequestMapping(value = "/web/customizeattribute", method = RequestMethod.GET)
	public ModelAndView customizeattribute(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView("customAttribute");

		return model;
	}

	@RequestMapping(value = "/web/newEntityData", method = RequestMethod.POST)
	public ModelAndView newEntityData(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView("showentity");
		int count_att = 0;
		String atts = request.getParameter("att_number");
		String[] attribute = request.getParameterValues("attributes");
		count_att = Integer.parseInt(atts);
		String[] type = request.getParameterValues("types");
		String[] lengthOv = request.getParameterValues("valueLength");
		String domain = request.getParameter("domain");
		String name = request.getParameter("entityName");
		String accessType = request.getParameter("accessType");
		String[] constraints = request.getParameterValues("constraints");
		int[] valueLegnth = new int[lengthOv.length];
		String[] CustomFields = request.getParameterValues("CustomField");
		for (int x = 0; x < lengthOv.length; x++) {
			valueLegnth[x] = Integer.parseInt(lengthOv[x]);

		}

		Domain dom = domainImp.findDomain(domain);
		System.out.println(dom.getDomainName() + " is the domain name");
		List<SqldataTypes> sqlTypes = typeIMPL.findAll();

		List<SqldataTypes> sqltypes = new ArrayList<SqldataTypes>(type.length);
		for (int x = 0; x < attribute.length; x++) {
			sqltypes.add(typeIMPL.findByName(type[x]));
		}

		List<String> CFields = new ArrayList<String>();
		// //String[] CustomFields = new String[count_att];
		// for (int v = 0; v < count_att; v++) {
		// int item = v + 1;
		// CustomFields[v] = request.getParameter("CustomField" + item);
		// // CFields.add(request.getParameter("CustomField" + v));
		// }
		System.out.println("\n \n" + attribute.length + "  : "
				+ constraints.length + " : " + lengthOv.length + " : "
				+ type.length);

		entityIMPL.CreateEntity(dom, name, accessType, attribute, type,
				sqltypes, valueLegnth, constraints, CustomFields, count_att);
		// System.out.println("Enity created successfully... :)");
		model.addObject("domain", dom.getDomainName());
		model.addObject("enity", name);
		model.addObject("access", accessType);
		model.addObject("attributes", Array_to_strText(attribute));
		model.addObject("customfields", Array_to_strText(CustomFields));
		model.addObject("types", Array_to_strText(type));
		for (String str : constraints) {
			System.out.println("\n \n");
			System.out.println(str);

		}
		return model;
	}

	@RequestMapping(value = "web/entityrelation", method = RequestMethod.GET)
	public ModelAndView entityRelation(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("addrelations");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<Domain> doms = new ArrayList<Domain>();
		if (auth != null) {
			String roles = auth.getAuthorities().toString();
			String user = auth.getName();
			if (user != null && roles.contains("ROLE_Master")) {
				doms = domainImp.getAllDomains();

			} else {
				doms = domainImp.findByCreator(user);

			}

		}
		System.out
				.println("\n There are " + doms.size() + " domain found for ");
		model.addObject("domains", doms);
		return model;

	}

	@RequestMapping(value = "web/entupdate", method = RequestMethod.GET)
	public @ResponseBody String entupdate(HttpServletRequest request) {
		String ents = "";
		ModelAndView model;
		try {
			model = new ModelAndView("requestContent");
			String domain = request.getParameter("domainName");
			System.out.println(domain + " was found");
			List<OrgEntity> ent = entityIMPL.findByDomain(domain);

			ents = "<select name='OrgEntity'  id='OrgEntity'  onchange='updateForm()'>";
			String str = "";
			for (int x = 0; x < ent.size(); x++) {
				str = str + "<option value=" + ent.get(x).getName() + "> "
						+ ent.get(x).getName() + "</option>";
			}
			ents = ents + str + "</select>";

			return ents;

		} catch (Exception e) {

			return "Sorry...Something went wrong. try again or contact the admin";

		}

	}

	@RequestMapping(value = "jsonlistetities", method = RequestMethod.GET)
	public @ResponseBody String JSONListEntities(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String activedomain = request.getParameter("activeDomain");
		Domain dom = domainImp.findDomain(activedomain.trim());
		List<OrgEntity> ents = entityIMPL.findByDomain(dom.getDomainName());
		StringBuffer entities = new StringBuffer("");
		int x = 0;
		int entitySize = ents.size();
		entities = entities.append("{\"OrgEntity\" : [");
		if (entitySize > 0) {
			for (OrgEntity ent : ents) {

				entities = entities.append(" {\"entityName\" : \""
						+ ent.getName() + "\"}");
				x++;
				if (x < entitySize) {
					entities = entities.append(",");
				}
			}
		}
		entities = entities.append("]}");
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		return entities.toString();
	}

	@RequestMapping(value = "jsonlistattributes", method = RequestMethod.GET)
	public @ResponseBody String JSONListAttributes(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String activeEntity = request.getParameter("activeEntity").trim();
		OrgEntity ents = entityIMPL.findByName(activeEntity).get(0);
		String[] attributes = ents.getAttributes();
		StringBuffer atributes = new StringBuffer("");
		if (attributes.length > 0) {
			int attSize = attributes.length;
			int x = 0;
			atributes.append("{ \"attributes\" : [ ");
			for (int ats = 0; ats < attributes.length; ats++) {
				atributes.append("{ \"attributeName\" : \"" + attributes[ats]
						+ "\" }");
				x++;
				if (x < attSize) {
					atributes.append(",");
				}
			}
			atributes.append("]}");
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		return atributes.toString();
	}

	@RequestMapping(value = "web/getAttributes", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String[] entityAttributes(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String activeEntity = request.getParameter("entity").trim();
		OrgEntity ents = entityIMPL.findByName(activeEntity).get(0);
		String[] attributes = ents.getAttributes();
		StringBuffer atributes = new StringBuffer("");

		return attributes;
	}

	@RequestMapping(value = "jsonlisttypes", method = RequestMethod.GET)
	public @ResponseBody String JSONListTypes(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String activeAtt = request.getParameter("activeAttribute");
		String activeEntity = request.getParameter("activeEntity");
		// String type = "";
		OrgEntity ents = entityIMPL.findByName(activeEntity).get(0);
		String[] attributes = ents.getAttributes();
		String[] types = ents.getTypes();
		StringBuffer attributeType = new StringBuffer("");
		int getPos = -1;
		for (int x = 0; x < attributes.length; x++) {
			if (attributes[x].equals(activeAtt.trim())) {
				getPos = x;
				break;
			}
		}
		if (getPos != -1) {
			attributeType.append(" { \"attributeType\" :[  { \"type\":  \""
					+ types[getPos] + "\"}]} ");
		} else {
			attributeType
					.append(" { \"attributeType\" :[  { \"type\": \"No Type Found\"}]} ");
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		return attributeType.toString();
	}

	@RequestMapping(value = "web/addrelationships", method = RequestMethod.GET)
	public ModelAndView relateEntities(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String domain = request.getParameter("domain");
		String entity = request.getParameter("entity");
		String attribute = request.getParameter("attribute");
		String refEntity = request.getParameter("referenceEntity");
		String refAttribute = request.getParameter("referenceAttribute");
		entityIMPL.newRelationship(domain, entity, attribute, refEntity,
				refAttribute);
		return null;// referenceAttribue

	}

	@RequestMapping(value = "web/getEntities", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getEntityList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		List<OrgEntity> entities = entityIMPL.findByDomain(request
				.getParameter("domain"));
		Gson gson = new Gson();
		System.out.println("inside get entities");
		String[] ents = new String[entities.size()];
		int x = 0;
		for (OrgEntity e : entities) {
			ents[x] = e.getName();
			x++;
		}
		System.out.println("Ajax request recieved...");
		return gson.toJson(ents);
	}

	@RequestMapping(value = "web/saveUpdates", method = RequestMethod.POST)
	public ModelAndView saveUpdate(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("requestupdate");
		System.out.println("get number of attributes");
		// int attrNumber = Integer.parseInt(request.getParameter("attrNumber")
		// .trim());
		String domainName = request.getParameter("domainName");
		String entity = request.getParameter("entity");
		String[] values = request.getParameterValues("attributes");
		int attrNumber = values.length;
		addContent cont = new addContent();
		ArrayList<String> tables = cont.getTables();
		ArrayList<String> attributes = cont.getTableAttributes(entityIMPL
				.findByName(entity).get(0));
		/*
		 * int[] typs = new int[attrNumber + 1]; typs[0] = Types.INTEGER; for
		 * (int x = 1; x < attrNumber + 1; x++) { typs[x] = Types.VARCHAR; }
		 */
		String variables = "";
		String Qmarks = "";
		if (attrNumber > 1) {
			variables = attributes.get(0);
			Qmarks = "?";
			for (int x = 1; x < attrNumber; x++) {
				variables = variables + "," + attributes.get(x);
				Qmarks = Qmarks + ", ?";
			}
		} else {
			variables = attributes.get(0);
			Qmarks = "?";

		}

		String sqlInsert = "INSERT INTO tbl_" + entity.trim() + "";
		String fullSqlquery = sqlInsert + " (" + variables + " ) VALUES ( "
				+ Qmarks + ")";
		boolean save = cont
				.saveContent(fullSqlquery, values, domainName, entity);

		model.addObject("tables", tables);
		model.addObject("domainName", domainName);/*	*/
		return model;
	}

	public String Array_to_strText(String[] array) {
		String strText = "";
		int c = 0;
		for (String str : array) {
			strText = strText + " " + str;
			if (c < array.length) {
				strText = strText + ",";
			}
			c++;
		}

		return strText;
	}
}
