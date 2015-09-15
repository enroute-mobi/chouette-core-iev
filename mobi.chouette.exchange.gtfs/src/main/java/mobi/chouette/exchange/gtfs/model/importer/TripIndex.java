package mobi.chouette.exchange.gtfs.model.importer;

import java.io.IOException;
import java.util.Map;

import mobi.chouette.common.HTMLTagValidator;
import mobi.chouette.exchange.gtfs.model.GtfsTrip;
import mobi.chouette.exchange.gtfs.model.importer.GtfsException.ERROR;
import mobi.chouette.exchange.gtfs.model.importer.StopTimeByTrip.FIELDS;

public abstract class TripIndex extends IndexImpl<GtfsTrip> implements
		GtfsConverter {

	public static enum FIELDS {
		route_id, service_id, trip_id, trip_headsign, trip_short_name, direction_id, block_id, shape_id, wheelchair_accessible, bikes_allowed;
	};

	public static final String FILENAME = "trips.txt";

	protected GtfsTrip bean = new GtfsTrip();
	protected String[] array = new String[FIELDS.values().length];
	protected String _routeId = null;
	protected String _serviceId = null;

	public TripIndex(String name, String id, boolean unique) throws IOException {
		super(name, id, unique);
	}
	
	@Override
	protected void checkRequiredFields(Map<String, Integer> fields) {
		for (String fieldName : fields.keySet()) {
			if (fieldName != null) {
				if (!fieldName.equals(fieldName.trim())) {
					// extra spaces in end fields are tolerated : 1-GTFS-CSV-7 warning
					getErrors().add(new GtfsException(_path, 1, fieldName, GtfsException.ERROR.EXTRA_SPACE_IN_HEADER_FIELD, null, null));
				}
				
				if (HTMLTagValidator.validate(fieldName.trim())) {
					getErrors().add(new GtfsException(_path, 1, fieldName.trim(), GtfsException.ERROR.HTML_TAG_IN_HEADER_FIELD, null, null));
				}
				
				boolean fieldNameIsExtra = true;
				for (FIELDS field : FIELDS.values()) {
					if (fieldName.trim().equals(field.name())) {
						fieldNameIsExtra = false;
						break;
					}
				}
				if (fieldNameIsExtra) {
					// extra fields are tolerated : 1-GTFS-Trip-8 warning
					getErrors().add(new GtfsException(_path, 1, fieldName, GtfsException.ERROR.EXTRA_HEADER_FIELD, null, null));
				}
			}
		}

		// checks for ubiquitous header fields : 1-GTFS-Trip-2 error
		if ( fields.get(FIELDS.trip_id.name()) == null ||
				fields.get(FIELDS.route_id.name()) == null ||
				fields.get(FIELDS.service_id.name()) == null) {
			
			String name = "";
			if (fields.get(FIELDS.trip_id.name()) == null)
				name = FIELDS.trip_id.name();
			else if (fields.get(FIELDS.route_id.name()) == null)
				name = FIELDS.route_id.name();
			else if (fields.get(FIELDS.service_id.name()) == null)
				name = FIELDS.service_id.name();
			
			throw new GtfsException(_path, 1, name, GtfsException.ERROR.MISSING_REQUIRED_FIELDS, null, null);
		}
	}

	@Override
	protected GtfsTrip build(GtfsIterator reader, Context context) {
		int i = 0;

		for (FIELDS field : FIELDS.values()) {
			array[i++] = getField(reader, field.name());
		}

		i = 0;
		String value = null;
		int id = (int) context.get(Context.ID);
		bean.setId(id);
		bean.getErrors().clear();
		
		value = array[i++]; testExtraSpace(FIELDS.route_id.name(), value, bean);
		if (value == null || value.trim().isEmpty()) {
			bean.getErrors().add(new GtfsException(_path, id, FIELDS.route_id.name(), GtfsException.ERROR.MISSING_REQUIRED_VALUES, null, null));
		} else {
			bean.setRouteId(STRING_CONVERTER.from(context, FIELDS.route_id, value, true));
		}
		
		value = array[i++]; testExtraSpace(FIELDS.service_id.name(), value, bean);
		if (value == null || value.trim().isEmpty()) {
			bean.getErrors().add(new GtfsException(_path, id, FIELDS.service_id.name(), GtfsException.ERROR.MISSING_REQUIRED_VALUES, null, null));
		} else {
			bean.setServiceId(STRING_CONVERTER.from(context, FIELDS.service_id, value, true));
		}
		
		value = array[i++]; testExtraSpace(FIELDS.trip_id.name(), value, bean);
		if (value == null || value.trim().isEmpty()) {
			bean.getErrors().add(new GtfsException(_path, id, FIELDS.trip_id.name(), GtfsException.ERROR.MISSING_REQUIRED_VALUES, null, null));
		} else {
			bean.setTripId(STRING_CONVERTER.from(context, FIELDS.trip_id, value, true));
		}
		
		value = array[i++]; testExtraSpace(FIELDS.trip_headsign.name(), value, bean);
		bean.setTripHeadSign(STRING_CONVERTER.from(context, FIELDS.trip_headsign, value, false));
		
		value = array[i++]; testExtraSpace(FIELDS.trip_short_name.name(), value, bean);
		bean.setTripShortName(STRING_CONVERTER.from(context, FIELDS.trip_short_name, value, false));
		
		value = array[i++]; testExtraSpace(FIELDS.direction_id.name(), value, bean);
		if (value != null && !value.trim().isEmpty()) {
			try {
				bean.setDirectionId(DIRECTIONTYPE_CONVERTER.from(context, FIELDS.direction_id, value, GtfsTrip.DirectionType.Outbound, false));
			} catch (GtfsException ex) {
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.direction_id.name(), GtfsException.ERROR.INVALID_DIRECTION, null, null));
			}
		}
		
		value = array[i++]; testExtraSpace(FIELDS.block_id.name(), value, bean);
		bean.setBlockId(STRING_CONVERTER.from(context, FIELDS.block_id, value, false));
		
		value = array[i++]; testExtraSpace(FIELDS.shape_id.name(), value, bean);
		bean.setShapeId(STRING_CONVERTER.from(context, FIELDS.shape_id, value, false));
		
		value = array[i++]; testExtraSpace(FIELDS.wheelchair_accessible.name(), value, bean);
		if (value != null && !value.trim().isEmpty()) {
			try {
				bean.setWheelchairAccessible(WHEELCHAIRACCESSIBLETYPE_CONVERTER.from(context, FIELDS.wheelchair_accessible, value, false));
			} catch (GtfsException ex) {
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.wheelchair_accessible.name(), GtfsException.ERROR.INVALID_WHEELCHAIR_TYPE, null, null));
			}
		}
		
		value = array[i++]; testExtraSpace(FIELDS.bikes_allowed.name(), value, bean);
		if (value != null && !value.trim().isEmpty()) {
			try {
				bean.setBikesAllowed(BIKESALLOWEDTYPE_CONVERTER.from(context, FIELDS.bikes_allowed, value, false));
			} catch (GtfsException ex) {
				bean.getErrors().add(new GtfsException(_path, id, FIELDS.bikes_allowed.name(), GtfsException.ERROR.INVALID_BYKE_TYPE, null, null));
			}
		}

		return bean;
	}

	@Override
	public boolean validate(GtfsTrip bean, GtfsImporter dao) {
		boolean result = true;
//		String routeId = bean.getRouteId();
//		if (!routeId.equals(_routeId)) {
//			if (!dao.getRouteById().containsKey(routeId)) {
//				throw new GtfsException(getPath(), bean.getId(),
//						FIELDS.route_id.name(), ERROR.MISSING_FOREIGN_KEY,
//						"TODO", bean.getRouteId());
//			}
//			_routeId = routeId;
//		}
//
//		String serviceId = bean.getServiceId();
//		if (!serviceId.equals(_serviceId)) {
//			boolean okCalendar = (dao.hasCalendarImporter() && dao.getCalendarByService().containsKey(serviceId)) ;
//			boolean okCalendarDate = (dao.hasCalendarDateImporter() && dao.getCalendarDateByService().containsKey(serviceId)) ;
//			if (!okCalendar
//					&& !okCalendarDate) {
//				throw new GtfsException(getPath(), bean.getId(),
//						FIELDS.service_id.name(), ERROR.MISSING_FOREIGN_KEY,
//						"TODO", bean.getServiceId());
//			}
//
//			_serviceId = serviceId;
//		}

		return result;
	}

}
