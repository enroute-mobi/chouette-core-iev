// @@LICENCE@@
//----------------------------------------------------------------------------
/**
 * Socit DRYADE
 *
 * Projet chouette : dfinition des types de donnes spcifiques 
 * 					 mis  disposition du mapping Base <-> Objet 
 * 					(package fr.certu.chouette.customtypes)
 *
 * DServiceStatusValueType.java : Type de donne bas sur l'numration ServiceStatusValue
 *
 * Historique des modifications :
 * Date        | Auteur         | Libll
 * ------------+----------------+-----------------------------------------------
 * 10 nov. 2004|Chao ZHANG		| Cration
 * ------------+----------------+-----------------------------------------------
 */
//----------------------------------------------------------------------------
package fr.certu.chouette.dao.hibernate.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import lombok.extern.log4j.Log4j;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.json.JSONObject;



//----------------------------------------------------------------------------
/**
 * Type de donnee base sur l'enumration ServiceStatusValue
 * 
 * La structure implemente l'interface des definitions de type UserType.
 *  
 */
@Log4j
public class JsonTextUserType implements UserType 
{
	//------------------------------------------------------------
	/**************** Variables d'instance **********************/
	private static final int[] SQL_TYPES = {Types.CLOB};
	
	//-------------------------------------------------------------------
	/************** Methodes de l'interface UserType *******************/

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	public Object deepCopy(Object arg0) throws HibernateException {
		return arg0;
	}
	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(Object arg0, Object arg1) throws HibernateException {
		return arg0 == arg1;
	}
	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	public boolean isMutable() {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
			throws HibernateException, SQLException 
	{
		String name = resultSet.getString(names[0]);
		log.warn(" json get "+names[0]+" = "+name);
		if (resultSet.wasNull()  || name.isEmpty() ) return null; 
		if (name.equals("null")) name = "{}" ;
		return new JSONObject(name);	
	}
	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet(PreparedStatement statement, Object value, int index)
			throws HibernateException, SQLException 
	{
		log.warn(" json set "+index+" = "+value);
		if (value == null) 
		{
			statement.setNull(index, Types.VARCHAR);
		} else {
			statement.setString(index, value.toString());
		}
	}
    /* (non-Javadoc)
    * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
    */
   public Object assemble(Serializable cached, Object owner) throws HibernateException {
		log.warn(" json assemble ");
       return cached;
   }

   /* (non-Javadoc)
    * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
    */
   public Serializable disassemble(Object value) throws HibernateException {
		log.warn(" json disassemble ");
       return (Serializable)value;
   }

   /* (non-Javadoc)
    * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
    */
   public Object replace(Object original, Object target, Object owner) throws HibernateException {
		log.warn(" json replace ");
       return original;
   }
   /* (non-Javadoc)
    * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
    */
   public int hashCode(Object x) throws HibernateException {
		log.warn(" json hashCode ");
       return x.toString().hashCode();
   }
	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	@SuppressWarnings("rawtypes")
	public Class returnedClass() {
		return JSONObject.class;
	}
	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	public int[] sqlTypes() {
		return SQL_TYPES;
	}
}