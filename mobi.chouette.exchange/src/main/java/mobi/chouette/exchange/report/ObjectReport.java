package mobi.chouette.exchange.report;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.validation.report.CheckPointReport.SEVERITY;

@ToString
public class ObjectReport extends AbstractReport implements CheckedReport {

	@XmlElement(name = "type", required = true)
	@Getter
	private ActionReporter.OBJECT_TYPE type;

	@XmlElement(name = "description", required = true)
	@Getter
	@Setter
	private String description;

	@XmlElement(name = "status", required = true)
	@Getter
	private OBJECT_STATE status = OBJECT_STATE.OK;

	@XmlElement(name = "stats", required = true)
	@Getter
	private Map<OBJECT_TYPE, Integer> stats = new HashMap<>();

	@XmlElement(name = "io_type")
	@Getter
	private IO_TYPE ioType;

	@XmlElement(name = "errors")
	@Getter
	private List<ObjectError> errors = new ArrayList<>();

	@XmlElement(name = "checkpoint_errors")
	@Getter
	private List<Integer> checkPointErrorKeys = new ArrayList<>();

	@XmlElement(name = "checkpoint_warnings")
	@Getter
	private List<Integer> checkPointWarningKeys = new ArrayList<>();

	@XmlElement(name = "checkpoint_error_count")
	@Getter
	private int checkPointErrorCount = 0;

	@XmlElement(name = "checkpoint_warning_count")
	@Getter
	private int checkPointWarningCount = 0;

	@XmlElement(name = "objectid")
	@Getter
	private String objectId;

	public ObjectReport(String objectId, OBJECT_TYPE type, String description, OBJECT_STATE status, IO_TYPE ioType) {
		this.objectId = objectId;
		this.type = type;
		this.description = description;
		this.status = status;
		this.ioType = ioType;
	}

	/**
	 * add an error; status will be set to ERROR
	 * 
	 * @param error
	 */
	protected void addError(ObjectError error) {
		status = OBJECT_STATE.ERROR;
		errors.add(error);
	}

	/**
	 * set status if not worst
	 */
	public void setStatus(OBJECT_STATE newStatus) {
		if (newStatus.ordinal() > status.ordinal()
				|| (newStatus.equals(OBJECT_STATE.IGNORED) && !status.equals(OBJECT_STATE.ERROR)))
			status = newStatus;
	}

	/**
	 * 
	 * @param checkPointErrorId
	 */
	protected boolean addCheckPointError(int checkPointErrorId, SEVERITY severity) {
		boolean ret = false;

		if (severity.equals(SEVERITY.WARNING)) {
			if (checkPointWarningCount < MAX_ERRORS) {
				checkPointWarningKeys.add(Integer.valueOf(checkPointErrorId));
				ret = true;
			}
			checkPointWarningCount++;
			if (status.ordinal() < OBJECT_STATE.WARNING.ordinal())
				status = OBJECT_STATE.WARNING;
		} else {
			if (checkPointErrorCount < MAX_ERRORS) {
				checkPointErrorKeys.add(Integer.valueOf(checkPointErrorId));
				ret = true;
			}
			checkPointErrorCount++;
			status = OBJECT_STATE.ERROR;
		}
		return ret;
	}

	/**
	 * Add stat to data type
	 * 
	 * @param type
	 * @param count
	 */
	public void addStatTypeToObject(OBJECT_TYPE type, int count) {

		if (stats.containsKey(type)) {
			stats.put(type, Integer.valueOf(stats.get(type).intValue() + count));
		} else {
			stats.put(type, Integer.valueOf(count));
		}

	}

	/**
	 * set stat to data type
	 * 
	 * @param type
	 * @param count
	 * @return previous value
	 */
	public int setStatTypeToObject(OBJECT_TYPE type, int count) {

		int oldvalue = 0;
		if (stats.containsKey(type)) {
			oldvalue = stats.get(type).intValue();
		}
		stats.put(type, Integer.valueOf(count));
		return oldvalue;

	}

	public JSONObject toJson() throws JSONException {
		JSONObject object = new JSONObject();
		object.put("type", type.toString().toLowerCase());
		object.put("description", description);
		object.put("objectid", objectId);
		object.put("status", status);
		if (ioType != null) {
			object.put("io_type", ioType);
		}
		if (!stats.isEmpty()) {
			JSONObject map = new JSONObject();
			object.put("stats", map);
			for (Entry<OBJECT_TYPE, Integer> entry : stats.entrySet()) {

				map.put(entry.getKey().toString().toLowerCase(), entry.getValue());
			}
		}

		if (!errors.isEmpty()) {
			JSONArray array = new JSONArray();
			object.put("errors", array);
			for (ObjectError error : errors) {
				array.put(error.toJson());
			}
		}

		if (!checkPointErrorKeys.isEmpty()) {
			JSONArray array = new JSONArray();
			object.put("check_point_errors", array);
			for (Integer value : checkPointErrorKeys) {
				array.put(value);
			}
		}

		object.put("check_point_error_count", checkPointErrorCount);
		object.put("check_point_warning_count", checkPointWarningCount);
		return object;
	}

	@Override
	public void print(PrintStream out, StringBuilder ret, int level, boolean first) {
		ret.setLength(0);
		out.print(addLevel(ret, level).append('{'));
		out.print(toJsonString(ret, level + 1, "type", type.toString().toLowerCase(), true));
		out.print(toJsonString(ret, level + 1, "description", description, false));
		out.print(toJsonString(ret, level + 1, "objectid", objectId, false));
		out.print(toJsonString(ret, level + 1, "status", status, false));
		if (ioType != null)
			out.print(toJsonString(ret, level + 1, "io_type", ioType, false));
		if (!stats.isEmpty()) {
			printMap(out, ret, level + 1, "stats", stats, false);
		}
		if (!errors.isEmpty()) {
			printArray(out, ret, level + 1, "errors", errors, false);
		}
		List<Integer> lstErrorKeys = new ArrayList<>();
		for (Integer numError : checkPointErrorKeys) {
			if (lstErrorKeys.size() < MAX_ERRORS)
				lstErrorKeys.add(numError);
			else
				break;
		}
		for (Integer numWarning : checkPointWarningKeys) {
			if (lstErrorKeys.size() < MAX_ERRORS)
				lstErrorKeys.add(numWarning);
			else
				break;
		}
		if (!lstErrorKeys.isEmpty())
			printIntArray(out, ret, level + 1, "check_point_errors", lstErrorKeys, false);

		out.print(toJsonString(ret, level + 1, "check_point_error_count", checkPointErrorCount, false));
		out.print(toJsonString(ret, level + 1, "check_point_warning_count", checkPointWarningCount, false));

		ret.setLength(0);
		out.print(addLevel(ret.append('\n'), level).append('}'));

	}

	public void clear() {
		errors.clear();
		checkPointErrorKeys.clear();
		checkPointWarningKeys.clear();
	}
}
