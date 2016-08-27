package infosource.processors;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JsonBuilder {
	public String results;
	public String errorMessage;
	public boolean hasErrors = false;

	public String getResults() {
		return results;
	}

	public JsonBuilder(String exchange1, String exchange2) {
		JSONObject json1;
		json1 = extractData(exchange1);
		try {
			json1.append("document", extractData(exchange2));
		} catch (JSONException e) {
			hasErrors = true;
			errorMessage = e.getMessage() + "\n class:" + e.getClass();
		}

		results = json1.toString();

	}

	public JSONObject extractData(String message) {
		JSONObject extractedMessage = null;
		try {
			JSONObject jsonObject = new JSONObject(message);
			JSONObject data = jsonObject.getJSONObject(
					"org.apache.cxf.message.MessageContentsList")
					.getJSONObject("list");
			if (data.has("list")) {
				data = data.getJSONObject("list");
			}
			extractedMessage = data;

		} catch (JSONException e) {
			hasErrors = true;
			errorMessage = e.getMessage() + "\n class:" + e.getClass();
		}

		return extractedMessage;
	}
}
