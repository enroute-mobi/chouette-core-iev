package mobi.chouette.model.type;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.postgresql.util.PGobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class DateRange implements Serializable {
	private static final long serialVersionUID = -4771648000594466515L;
	@Getter
	@Setter
	private Date first;
	@Getter
	@Setter
	private Date last;

	public DateRange(PGobject obj) {
		if (obj.getType().equals("daterange")) {
			String value = obj.getValue().substring(1, obj.getValue().length() - 1);
			String[] tok = value.split(",");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				first = new Date(format.parse(tok[0]).getTime());
			} catch (ParseException e) {
			}
			try {
				last = new Date(format.parse(tok[1]).getTime());
			} catch (ParseException e) {
			}

		}
	}

	public PGobject toPGObject() throws SQLException {
		PGobject obj = new PGobject();
		obj.setType("daterange");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		obj.setValue("[" + format.format(first) + "," + format.format(last) + "]");
		return obj;
	}

	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return "DateRange [" + format.format(first) + "," + format.format(last) + "]";
	}

}
