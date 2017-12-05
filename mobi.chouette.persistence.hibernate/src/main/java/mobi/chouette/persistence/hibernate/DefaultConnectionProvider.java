package mobi.chouette.persistence.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.engine.jndi.spi.JndiService;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Stoppable;

public class DefaultConnectionProvider
		implements MultiTenantConnectionProvider, ServiceRegistryAwareService, Stoppable {

	private static final long serialVersionUID = 1L;

	private transient DataSource datasource;

	@Override
	public Connection getAnyConnection() throws SQLException {
		return datasource.getConnection();
	}

	@Override
	public Connection getConnection(String identifier) throws SQLException {
		
		final Connection connection = getAnyConnection();
		try {
			if (identifier != null && !identifier.isEmpty()) {
				try (Statement stmt = connection.createStatement();) {
					stmt.execute("SET SCHEMA '" + identifier + "'");
				}
			}
		} catch (SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [" + identifier + "]", e);
		}
		return connection;
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		releaseAnyConnection(connection);
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return MultiTenantConnectionProvider.class.equals(unwrapType)
				|| AbstractMultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public <T> T unwrap(Class<T> unwrapType) {
		if (isUnwrappableAs(unwrapType)) {
			return (T) this;
		} else {
			throw new UnknownUnwrapTypeException(unwrapType);
		}
	}

	@Override
	public void injectServices(ServiceRegistryImplementor registry) {

		Map<?, ?> settings = getSettings(registry);
		String datasourceName = (String) settings.get(AvailableSettings.DATASOURCE);

		ContextHolder.setDefaultSchema(null);

		JndiService jndi = registry.getService(JndiService.class);
		if (jndi == null) {
			throw new HibernateException(
					"Could not locate JndiService from DataSourceBasedMultiTenantConnectionProviderImpl");
		}

		Object namedObject = jndi.locate(datasourceName);
		if (namedObject == null) {
			throw new HibernateException("JNDI name [" + datasourceName + "] could not be resolved");
		}

		if (DataSource.class.isInstance(namedObject)) {
			datasource = (DataSource) namedObject;
		} else {
			throw new HibernateException("Unknown object type [" + namedObject.getClass().getName()
					+ "] found in JNDI location [" + datasourceName + "]");
		}

	}

	private Map<?, ?> getSettings(ServiceRegistryImplementor registry) {
		Map<?, ?> result = registry.getService(ConfigurationService.class).getSettings();
		return result;
	}

	@Override
	public void stop() {
		datasource = null;
	}

}