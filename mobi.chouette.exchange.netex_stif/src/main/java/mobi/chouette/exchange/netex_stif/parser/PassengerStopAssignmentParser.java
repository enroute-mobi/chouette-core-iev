package mobi.chouette.exchange.netex_stif.parser;

import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.Constant;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.model.PassengerStopAssignment;
import mobi.chouette.exchange.netex_stif.validator.PassengerStopAssignmentValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.StopAreaLite;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.Referential;

@Log4j
public class PassengerStopAssignmentParser implements Parser, Constant {

	@Override
	public void parse(Context context) throws Exception {
		XmlPullParser xpp = (XmlPullParser) context.get(PARSER);
		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();
		NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NETEX_STIF_OBJECT_FACTORY);
		Referential referential = (Referential) context.get(REFERENTIAL);

		PassengerStopAssignmentValidator validator = (PassengerStopAssignmentValidator) ValidatorFactory.getValidator(context, PassengerStopAssignmentValidator.class);

		String id = xpp.getAttributeValue(null, ID);
		PassengerStopAssignment stopAssignment = factory.getPassengerStopAssignment(id);
		
		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			// log.info("PassengerStopAssignmentParser : " + xpp.getName());
			if (xpp.getName().equals(SCHEDULED_STOP_POINT_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				String attr_version = xpp.getAttributeValue(null, VERSION);
				String content = xpp.nextText();
				// check internal reference
				boolean checked = validator.checkNetexRef(context, stopAssignment, SCHEDULED_STOP_POINT_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, stopAssignment, SCHEDULED_STOP_POINT_REF, ref,
							attr_version, content, lineNumber, columnNumber);
				stopAssignment.setScheduledStopPointRef(ref);
			} else if (xpp.getName().equals(QUAY_REF)) {
				String ref = xpp.getAttributeValue(null, REF);
				String attr_version = xpp.getAttributeValue(null, VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, stopAssignment, QUAY_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkExternalRef(context, stopAssignment, QUAY_REF, ref,
							attr_version, content, lineNumber, columnNumber);
				if (checked) 
					checked = validator.checkExistsRef(context, stopAssignment, QUAY_REF, ref, attr_version, content, lineNumber, columnNumber);
				stopAssignment.setQuayRef(ref);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		
		if (validator.validate(context, stopAssignment, lineNumber, columnNumber)) {
			List<StopPoint> list = factory.getStopPoints(stopAssignment.getScheduledStopPointRef());
			// log.info(Color.CYAN+"id :" + id + " list : " + list+Color.NORMAL);
			if (list != null) {
				StopAreaLite stopArea = referential.getSharedReadOnlyStopAreas().get(stopAssignment.getQuayRef());
				if (stopArea != null) {
					for (StopPoint stopPoint : list) {
						stopPoint.setStopAreaId(stopArea.getId());
					}
				}
				else
				{
					log.info(Color.RED+"quay " + stopAssignment.getQuayRef()+ " not found"+Color.NORMAL);
				}
			}
		}

	}

	static {
		ParserFactory.register(PassengerStopAssignmentParser.class.getName(), new ParserFactory() {
			private PassengerStopAssignmentParser instance = new PassengerStopAssignmentParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
