package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.sql.Date;

import javax.naming.InitialContext;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.exporter.SharedDataKeys;
import mobi.chouette.exchange.report.ActionReporter;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.model.Line;
import mobi.chouette.model.util.NamingUtil;

@Log4j
public class NetexStifLineProducerCommand implements Command {
	private static final String MERGED = "merged";
	public static final String COMMAND = "NetexStifLineProducerCommand";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);
		ActionReporter reporter = ActionReporter.Factory.getInstance();

		try {

			Line line = (Line) context.get(Constant.LINE);
			log.info("procesing line " + NamingUtil.getName(line));
			NetexStifExportParameters configuration = (NetexStifExportParameters) context.get(Constant.CONFIGURATION);

			ExportableData collection = (ExportableData) context.get(Constant.EXPORTABLE_DATA);
			if (collection == null)
			{
				collection = new  ExportableData();
				context.put(Constant.EXPORTABLE_DATA, collection);
			}
			else
			{
				collection.clear();
			}

			SharedDataKeys sharedData = (SharedDataKeys) context.get(Constant.SHARED_DATA_KEYS);
			if (sharedData == null) {
				sharedData = new SharedDataKeys();
				context.put(Constant.SHARED_DATA_KEYS, sharedData);
			}

			Date startDate = null;
			if (configuration.getStartDate() != null) {
				startDate = new Date(configuration.getStartDate().getTime());
			}

			Date endDate = null;
			if (configuration.getEndDate() != null) {
				endDate = new Date(configuration.getEndDate().getTime());
			}

			NetexStifDataCollector collector = new NetexStifDataCollector();
			boolean cont = (collector.collect(collection, line, startDate, endDate));
			reporter.addObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, NamingUtil.getName(line), OBJECT_STATE.OK, IO_TYPE.INPUT);
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.LINE, 0);
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.JOURNEY_PATTERN, collection.getJourneyPatterns().size());
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.ROUTE, collection.getRoutes().size());
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.VEHICLE_JOURNEY, collection.getVehicleJourneys().size());
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.CONNECTION_LINK, collection.getConnectionLinks().size());
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.TIME_TABLE, collection.getTimetables().size());
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.ACCESS_POINT, collection.getAccessPoints().size());
			reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.STOP_AREA, collection.getStopAreas().size());
			if (cont) {

				NetexStifLineProducer producer = new NetexStifLineProducer();
				producer.produce(context);

				reporter.setStatToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, OBJECT_TYPE.LINE, 1);
				// merge refresh shared data
				
				reporter.addObjectReport(context, MERGED, OBJECT_TYPE.NETWORK, "networks", OBJECT_STATE.OK, IO_TYPE.INPUT);
				reporter.setStatToObjectReport(context, MERGED, OBJECT_TYPE.NETWORK, OBJECT_TYPE.NETWORK, sharedData.getNetworkIds().size());
				reporter.addObjectReport(context, MERGED, OBJECT_TYPE.COMPANY, "companies", OBJECT_STATE.OK, IO_TYPE.INPUT);
				reporter.setStatToObjectReport(context, MERGED, OBJECT_TYPE.COMPANY, OBJECT_TYPE.COMPANY, sharedData.getCompanyIds().size());
				reporter.addObjectReport(context, MERGED, OBJECT_TYPE.CONNECTION_LINK, "connection links", OBJECT_STATE.OK, IO_TYPE.INPUT);
				reporter.setStatToObjectReport(context, MERGED, OBJECT_TYPE.CONNECTION_LINK, OBJECT_TYPE.CONNECTION_LINK, sharedData.getConnectionLinkIds().size());
				reporter.addObjectReport(context, MERGED, OBJECT_TYPE.ACCESS_POINT, "access points", OBJECT_STATE.OK, IO_TYPE.INPUT);
				reporter.setStatToObjectReport(context, MERGED, OBJECT_TYPE.ACCESS_POINT, OBJECT_TYPE.ACCESS_POINT, sharedData.getAccessPointIds().size());
				reporter.addObjectReport(context, MERGED, OBJECT_TYPE.STOP_AREA, "stop areas", OBJECT_STATE.OK, IO_TYPE.INPUT);
				reporter.setStatToObjectReport(context, MERGED, OBJECT_TYPE.STOP_AREA, OBJECT_TYPE.STOP_AREA, sharedData.getStopAreaIds().size());
				reporter.addObjectReport(context, MERGED, OBJECT_TYPE.TIME_TABLE, "calendars", OBJECT_STATE.OK, IO_TYPE.INPUT);
				reporter.setStatToObjectReport(context, MERGED, OBJECT_TYPE.TIME_TABLE, OBJECT_TYPE.TIME_TABLE, sharedData.getTimetableIds().size());
				result = Constant.SUCCESS;
			} else {
				reporter.addErrorToObjectReport(context, line.getObjectId(), OBJECT_TYPE.LINE, ActionReporter.ERROR_CODE.NO_DATA_ON_PERIOD, "no data on period");
				result = Constant.ERROR;
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new NetexStifLineProducerCommand();
			return result;
		}
	}

	static {
		CommandFactory.register(NetexStifLineProducerCommand.class.getName(), new DefaultCommandFactory());
	}

}
