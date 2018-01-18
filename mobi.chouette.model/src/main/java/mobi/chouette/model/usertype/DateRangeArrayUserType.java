package mobi.chouette.model.usertype;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import mobi.chouette.model.type.DateRange;

public class DateRangeArrayUserType implements UserType {

	private final int[] arrayTypes = new int[] { Types.ARRAY };

	@Override
	public Object assemble(Serializable cached, Object value) throws HibernateException {
		return cached;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value == null ? null : ((DateRange[]) value).clone();
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
			Object[] results = (Object[]) rs.getArray(names[0]).getArray();
			DateRange[] castObject = new DateRange[results.length];
			for (int i = 0; i < results.length; i++) {
				castObject[i] = new DateRange((PGobject) results[i]);
			}
			return castObject;
		}
		return null;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		// setting the column with string array
		if (value != null && st != null) {
			DateRange[] castObject = (DateRange[]) value;
			PGobject[] pgObject = new PGobject[castObject.length];
			for (int i = 0; i < castObject.length; i++) {
				pgObject[i] = castObject[i].toPGObject();
			}
			Array array = session.connection().createArrayOf("daterange", pgObject);
			st.setArray(index, array);
		} else if (st != null) {
			st.setNull(index, arrayTypes[0]);
		}

	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return DateRange[].class;
	}

	@Override
	public int[] sqlTypes() {
		return arrayTypes;
	}

}
