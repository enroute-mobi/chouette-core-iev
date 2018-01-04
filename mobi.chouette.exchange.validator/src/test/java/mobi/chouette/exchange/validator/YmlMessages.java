package mobi.chouette.exchange.validator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import lombok.extern.log4j.Log4j;

@Log4j
public class YmlMessages {

	private static final Map<String, String> messages;

	static {
		messages = new HashMap<String, String>();
		File f = new File("src/test/data/validation.yml");
		try {
			List<String> lines = FileUtils.readLines(f, Charset.forName("UTF-8"));
			lines.stream().filter(l -> !l.trim().isEmpty()).forEach(l -> {
				if (l.trim().startsWith("3_")) {
					String[] tokens = l.split(":");
					if (tokens.length > 1) {
						int pos = l.indexOf(':');
						String key = l.substring(0, pos).trim();
						String message = l.substring(pos + 1).trim();
						message = message.substring(1, message.length() - 1);
						messages.put(key, message);
					}
				}
			});

		} catch (IOException e) {
			log.error("cannot read message file", e);
		}

	}

	public static String getMessage(String key) {
		return messages.get(key);
	}

	public static List<String> getMessageKeys(String key) {
		List<String> keys = new ArrayList<>();
		String message = getMessage(key);
		if (message != null) {
			String[] tokens = message.split("\\%\\{");
			for (String token : tokens) {
				int pos = token.indexOf('}');
				if (pos >= 0) {
					keys.add(token.substring(0, pos));
				}
			}
		}
		return keys;
	}

	public static String populateMessage(String key, Map<String, String> values) {
		String message = getMessage(key);
		log.info(message);
		if (message == null)
			return message;
		for (Entry<String, String> e : values.entrySet()) {
			try {
				log.info(e);
				message = message.replace("%{" + e.getKey() + "}", e.getValue());
				log.info(message);
			} catch (IllegalArgumentException iae) {
				log.error("IAE : message=" + message + ", key=" + e.getKey() + ", value=" + e.getValue()
						+ " => exception=" + iae.getMessage());
			}
		}
		return message;
	}

	public static List<String> missingKeys(String key, Map<String, String> values) {
		List<String> keys = getMessageKeys(key);
		keys.removeAll(values.keySet());
		return keys;
	}

}
