package mongo.exercise.app;

import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mongo.exercise.document.MongoPojo;
import mongo.exercise.service.MongoService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	public MongoService mongoService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/ajax/index")
	@ResponseBody
	public String index(HttpServletRequest request, @RequestParam("param") String param) {

		JsonObject json = new JsonObject();

		System.out.println("############# request json: ");
		System.out.println(request.getParameter("start"));
		System.out.println("############# end");

		int start = Integer.parseInt((String) request.getParameter("start"));
		int end = Integer.parseInt((String) request.getParameter("length"));
		String sortCol = request.getParameter("order[0][column]");
		String sortDir = (String) request.getParameter("order[0][dir]");

		MultiValueMap<String, String> search = UriComponentsBuilder.fromUriString("http://aaa.com?" +param).build().getQueryParams();

		Enumeration params = request.getParameterNames();
		System.out.println("----------------------------");
		while (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			System.out.println(name + " : " + request.getParameter(name));
		}
		System.out.println("----------------------------");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("sortCol", sortCol);
		map.put("sortDir", sortDir);

		if (search.get("f_job") != null) {
			map.put("job", search.get("f_job").get(0));

			System.out.println(map.get("job"));
		}

//		String sortCol= arrs[0].column;
//		int sortDir = arrs[0].dir;

		HashMap<Object, Object> result = mongoService.getPageList(map);

		json.addProperty("draw", (String) request.getParameter("draw"));
		json.addProperty("recordsTotal", Long.parseLong(result.get("total").toString()));
		json.addProperty("recordsFiltered", Long.parseLong(result.get("total").toString()));

		json.add("data", (JsonElement) result.get("list"));

		System.out.println(json.toString());
		return json.toString();
	}

}
