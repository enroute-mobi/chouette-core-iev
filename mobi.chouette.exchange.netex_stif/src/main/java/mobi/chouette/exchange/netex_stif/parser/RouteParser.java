package mobi.chouette.exchange.netex_stif.parser;

import org.xmlpull.v1.XmlPullParser;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.XPPUtil;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.model.Direction;
import mobi.chouette.exchange.netex_stif.model.NetexStifObjectFactory;
import mobi.chouette.exchange.netex_stif.validator.RouteValidator;
import mobi.chouette.exchange.netex_stif.validator.ValidatorFactory;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.Route;
import mobi.chouette.model.type.PTDirectionEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class RouteParser implements Parser {

	@Override
	public void parse(Context context) throws Exception {

		XmlPullParser xpp = (XmlPullParser) context.get(Constant.PARSER);
		Referential referential = (Referential) context.get(Constant.REFERENTIAL);
		Long version = (Long) context.get(NetexStifConstant.VERSION);
		xpp.require(XmlPullParser.START_TAG, null, NetexStifConstant.ROUTE);

		int columnNumber = xpp.getColumnNumber();
		int lineNumber = xpp.getLineNumber();

		String id = xpp.getAttributeValue(null, NetexStifConstant.ID);
		
		RouteValidator validator = (RouteValidator) ValidatorFactory.getValidator(context, RouteValidator.class);
		validator.checkNetexId(context, NetexStifConstant.ROUTE, id, lineNumber, columnNumber);
		
		Route route = ObjectFactory.getRoute(referential, id);
		route.setObjectVersion(version);
		String changed = xpp.getAttributeValue(null, NetexStifConstant.CHANGED);
		if (changed != null) {
			route.setCreationTime(NetexStifUtils.getDate(changed));
		}
		String modification = xpp.getAttributeValue(null, NetexStifConstant.MODIFICATION);
		LineLite line = (LineLite) context.get(Constant.LINE);
		if (line != null) {
			route.setLineId(line.getId());
			route.setLineLite(line);
		}
		else
		{
			throw new NullPointerException("LINE not set in context");
		}

		while (xpp.nextTag() == XmlPullParser.START_TAG) {
			// log.info("RouteParser tag: " + xpp.getName());
			if (xpp.getName().equals(NetexStifConstant.NAME)) {
				route.setName(xpp.nextText());
			} else if (xpp.getName().equals(NetexStifConstant.LINE_REF)) {
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String att_version = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check external reference
				boolean checked = validator.checkNetexRef(context, route, NetexStifConstant.LINE_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkExternalRef(context, route, NetexStifConstant.LINE_REF, ref, att_version,
							content, lineNumber, columnNumber);
				if (checked)
					checked = validator.checkExistsRef(context, route, NetexStifConstant.LINE_REF, ref, att_version,
							content, lineNumber, columnNumber);
				// TODO : check version with line of file
			} else if (xpp.getName().equals(NetexStifConstant.DIRECTION_TYPE)) {

				String tmpDirType = xpp.nextText();
				route.setWayBack(tmpDirType);
				// log.warn("Route "+ id+ " : DirectionType = "+ tmpDirType);
				if (tmpDirType.equals(NetexStifConstant.DIRECTION_INBOUND)) {
					route.setDirection(PTDirectionEnum.R);
				} else if (tmpDirType.equals(NetexStifConstant.DIRECTION_OUTBOUND)) {
					route.setDirection(PTDirectionEnum.A);
				} else {
					// error will be returned by validator
				}
			} else if (xpp.getName().equals(NetexStifConstant.DIRECTION_REF)) {
				NetexStifObjectFactory factory = (NetexStifObjectFactory) context.get(NetexStifConstant.NETEX_STIF_OBJECT_FACTORY);
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String att_version = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check internal reference
				boolean checked = validator.checkNetexRef(context, route, NetexStifConstant.DIRECTION_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, route, NetexStifConstant.DIRECTION_REF, ref, att_version, content,
							lineNumber, columnNumber);
				Direction direction = factory.getDirection(ref);
				if (direction.isFilled()) {
					route.setPublishedName(direction.getName());
				} else {
					factory.addRouteDirection(id, ref);
				}
			} else if (xpp.getName().equals(NetexStifConstant.INVERSE_ROUTE_REF)) {
				String ref = xpp.getAttributeValue(null, NetexStifConstant.REF);
				String att_version = xpp.getAttributeValue(null, NetexStifConstant.VERSION);
				String content = xpp.nextText();
				// check internal reference
				boolean checked = validator.checkNetexRef(context, route, NetexStifConstant.ROUTE_REF, ref, lineNumber,
						columnNumber);
				if (checked)
					checked = validator.checkInternalRef(context, route, NetexStifConstant.ROUTE_REF, ref, att_version, content,
							lineNumber, columnNumber);
				validator.addInverseRouteRef(context, route.getObjectId(), ref);
				Route wayBackRoute = ObjectFactory.getRoute(referential, ref);
				if (wayBackRoute != null) {
					wayBackRoute.setOppositeRoute(route);
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
		
		validator.addModification(context, route.getObjectId(), modification);
		validator.validate(context, route, lineNumber, columnNumber);
		
		route.setFilled(true);
	}

	static {
		ParserFactory.register(RouteParser.class.getName(), new ParserFactory() {
			private RouteParser instance = new RouteParser();

			@Override
			protected Parser create() {
				return instance;
			}
		});
	}

}
