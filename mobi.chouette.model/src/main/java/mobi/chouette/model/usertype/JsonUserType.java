package mobi.chouette.model.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import lombok.extern.log4j.Log4j;

@Log4j
public class JsonUserType implements UserType {

	private final int[] jsonTypes = new int[] { Types.JAVA_OBJECT };

	@Override
	public Object assemble(Serializable cached, Object value) throws HibernateException {
		return cached;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		try {
			return value == null ? null : new JSONObject(value.toString());
		} catch (JSONException e) {
			throw new HibernateException(e.getMessage());
		}
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return x == null ? y == null : x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x == null ? 0 : x.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		// get the first column names
		if (names != null && names.length > 0 && rs != null && rs.getArray(names[0]) != null) {
			String data = rs.getString(names[0]);
			if (data != null) {
				try {
					return new JSONObject(data);
				} catch (JSONException e) {
					log.error("unable to parse " + data);
					return new JSONObject();
				}
			}
		}
		return new JSONObject();
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		// setting the column with json string data
		if (value != null && st != null) {
			String data = value.toString();
			PGobject pGobject = new PGobject();
            pGobject.setType("json");
            pGobject.setValue(data);
            st.setObject(index, pGobject, Types.OTHER);
            return;
		} else if (st != null) {
			st.setObject(index, null);
		}

	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return JSONObject.class;
	}

	@Override
	public int[] sqlTypes() {
		return jsonTypes;
	}

}
