package mobi.chouette.exchange.validation.report;

import java.io.PrintStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import mobi.chouette.exchange.validation.report.ObjectReference.TYPE;
import mobi.chouette.model.JourneyPattern;

public class ObjectReferenceTest {
	@Test(groups = { "JsonGeneration" }, description = "Json generated", priority = 104)
	public void verifyJsonGeneration() throws Exception {
	
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(oStream);
		JourneyPattern jp = new JourneyPattern();
		jp.setObjectId("TEST:JourneyPattern:1234");
		ObjectReference objectRef = new ObjectReference(jp);
		objectRef.print(stream, new StringBuilder(), 1, true);
		String text = oStream.toString();
		JSONObject res = null;
		try {
			res = new JSONObject(text);
			
			Assert.assertEquals(res.getString("type") , TYPE.JOURNEY_PATTERN.name().toLowerCase(), "wrong object reference type");
			Assert.assertEquals(res.getString("objectId") , jp.getObjectId(), "wrong object referenced id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
