package infosource.messaging.soap;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.jetty.util.ajax.JSON;

import com.google.gson.JsonObject;

import birthcertificate.ews.soap.BatchApplication;

public class PrepareSoapProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String jsnString = in.getBody(String.class);
		MessageContentsList messageContentList = new MessageContentsList();
		JSONObject jObject = new JSONObject(jsnString).getJSONObject("list");
		String jsonStr = "";
		JSONArray jArray;
		String str= jObject.getString("infosource.messaging.models.Labour");
	//if (jObject.length() > 1) {
	//	jArray = jObject.getJSONArray("infosource.messaging.models.Labour");
	//	jsonStr = jArray.toString();
	//} else {
	//	JSONObject obj = jObject
	//			.getJSONObject("infosource.messaging.models.Labour");
	//	jsonStr = obj.toString();
	//}

		messageContentList.add(str);
		exchange.getOut().setHeader("operationName", "BatchApplication");
		exchange.getOut().setBody(messageContentList);
	}

}