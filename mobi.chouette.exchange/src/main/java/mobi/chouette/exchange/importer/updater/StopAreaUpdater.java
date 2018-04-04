package mobi.chouette.exchange.importer.updater;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.CollectionUtil;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.Pair;
import mobi.chouette.dao.AccessLinkDAO;
import mobi.chouette.dao.AccessPointDAO;
import mobi.chouette.dao.StopAreaDAO;
import mobi.chouette.exchange.parameters.AbstractParameter;
import mobi.chouette.exchange.validation.ValidationData;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.AccessLink;
import mobi.chouette.model.AccessPoint;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.util.ChouetteModelUtil;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Stateless(name = StopAreaUpdater.BEAN_NAME)
@Log4j
public class StopAreaUpdater implements Updater<StopArea> {

	public static final String BEAN_NAME = "StopAreaUpdater";

	@EJB
	private StopAreaDAO stopAreaDAO;

	@EJB(beanName = StopAreaUpdater.BEAN_NAME)
	private Updater<StopArea> stopAreaUpdater;

	@EJB
	private AccessPointDAO accessPointDAO;

	@EJB(beanName = AccessPointUpdater.BEAN_NAME)
	private Updater<AccessPoint> accessPointUpdater;

	@EJB
	private AccessLinkDAO accessLinkDAO;

	@EJB(beanName = AccessLinkUpdater.BEAN_NAME)
	private Updater<AccessLink> accessLinkUpdater;

	@Override
	public void update(Context context, StopArea oldValue, StopArea newValue) throws Exception {

		if (newValue.isSaved()) {
			return;
		}
		newValue.setSaved(true);

		Monitor monitor = MonitorFactory.start(BEAN_NAME);
		Referential cache = (Referential) context.get(Constant.CACHE);

		AbstractParameter params = (AbstractParameter) context.get(Constant.CONFIGURATION);
		// Database test init
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.addItemToValidationReport(context, "2-DATABASE-", "StopArea", 2, "W", "E");
		validationReporter.addItemToValidationReport(context, ValidationConstant.DATABASE_ACCESS_POINT_1, "E");
		ValidationData data = (ValidationData) context.get(Constant.VALIDATION_DATA);

		if (newValue.getAreaType() == null) {
			log.error("stoparea without mandatory areatype " + newValue.getObjectId());
			throw new IllegalArgumentException("area type null");
		}

		if (oldValue.isDetached()) {
			oldValue.setObjectId(newValue.getObjectId());
			oldValue.setObjectVersion(newValue.getObjectVersion());
			oldValue.setCreationTime(newValue.getCreationTime());
			oldValue.setName(newValue.getName());
			oldValue.setComment(newValue.getComment());
			oldValue.setAreaType(newValue.getAreaType());
			oldValue.setRegistrationNumber(newValue.getRegistrationNumber());
			oldValue.setNearestTopicName(newValue.getNearestTopicName());
			oldValue.setUrl(newValue.getUrl());
			oldValue.setTimeZone(newValue.getTimeZone());
			oldValue.setFareCode(newValue.getFareCode());
			oldValue.setLiftAvailable(newValue.getLiftAvailable());
			oldValue.setMobilityRestrictedSuitable(newValue.getMobilityRestrictedSuitable());
			oldValue.setStairsAvailable(newValue.getStairsAvailable());
			oldValue.setIntUserNeeds(newValue.getIntUserNeeds());
			oldValue.setLongitude(newValue.getLongitude());
			oldValue.setLatitude(newValue.getLatitude());
			oldValue.setLongLatType(newValue.getLongLatType());
			oldValue.setCountryCode(newValue.getCountryCode());
			oldValue.setZipCode(newValue.getZipCode());
			oldValue.setCityName(newValue.getCityName());
			oldValue.setStreetName(newValue.getStreetName());
			oldValue.setDetached(false);
		} else {
			twoDatabaseStopAreaTwoTest(validationReporter, context, oldValue, newValue, data);
			twoDatabaseStopAreaOneTest(validationReporter, context, oldValue, newValue, data);
			if (newValue.getObjectId() != null && !newValue.getObjectId().equals(oldValue.getObjectId())) {
				oldValue.setObjectId(newValue.getObjectId());
			}
			if (newValue.getObjectVersion() != null && !newValue.getObjectVersion().equals(oldValue.getObjectVersion())) {
				oldValue.setObjectVersion(newValue.getObjectVersion());
			}
			if (newValue.getCreationTime() != null && !newValue.getCreationTime().equals(oldValue.getCreationTime())) {
				oldValue.setCreationTime(newValue.getCreationTime());
			}
			if (newValue.getName() != null && !newValue.getName().equals(oldValue.getName())) {
				oldValue.setName(newValue.getName());
			}
			if (newValue.getComment() != null && !newValue.getComment().equals(oldValue.getComment())) {
				oldValue.setComment(newValue.getComment());
			}
			if (newValue.getAreaType() != null && !newValue.getAreaType().equals(oldValue.getAreaType())) {
				oldValue.setAreaType(newValue.getAreaType());
			}
			if (newValue.getRegistrationNumber() != null
					&& !newValue.getRegistrationNumber().equals(oldValue.getRegistrationNumber())) {
				oldValue.setRegistrationNumber(newValue.getRegistrationNumber());
			}
			if (newValue.getNearestTopicName() != null
					&& !newValue.getNearestTopicName().equals(oldValue.getNearestTopicName())) {
				oldValue.setNearestTopicName(newValue.getNearestTopicName());
			}
			if (newValue.getUrl() != null && !newValue.getUrl().equals(oldValue.getUrl())) {
				oldValue.setUrl(newValue.getUrl());
			}
			if (newValue.getTimeZone() != null && !newValue.getTimeZone().equals(oldValue.getTimeZone())) {
				oldValue.setTimeZone(newValue.getTimeZone());
			}
			if (newValue.getFareCode() != null && !newValue.getFareCode().equals(oldValue.getFareCode())) {
				oldValue.setFareCode(newValue.getFareCode());
			}
			if (newValue.getLiftAvailable() != null && !newValue.getLiftAvailable().equals(oldValue.getLiftAvailable())) {
				oldValue.setLiftAvailable(newValue.getLiftAvailable());
			}
			if (newValue.getMobilityRestrictedSuitable() != null
					&& !newValue.getMobilityRestrictedSuitable().equals(oldValue.getMobilityRestrictedSuitable())) {
				oldValue.setMobilityRestrictedSuitable(newValue.getMobilityRestrictedSuitable());
			}
			if (newValue.getStairsAvailable() != null
					&& !newValue.getStairsAvailable().equals(oldValue.getStairsAvailable())) {
				oldValue.setStairsAvailable(newValue.getStairsAvailable());
			}
			if (newValue.getIntUserNeeds() != null && !newValue.getIntUserNeeds().equals(oldValue.getIntUserNeeds())) {
				oldValue.setIntUserNeeds(newValue.getIntUserNeeds());
			}

			if (newValue.getLongitude() != null && !newValue.getLongitude().equals(oldValue.getLongitude())) {
				oldValue.setLongitude(newValue.getLongitude());
			}
			if (newValue.getLatitude() != null && !newValue.getLatitude().equals(oldValue.getLatitude())) {
				oldValue.setLatitude(newValue.getLatitude());
			}
			if (newValue.getLongLatType() != null && !newValue.getLongLatType().equals(oldValue.getLongLatType())) {
				oldValue.setLongLatType(newValue.getLongLatType());
			}
			if (newValue.getCountryCode() != null && !newValue.getCountryCode().equals(oldValue.getCountryCode())) {
				oldValue.setCountryCode(newValue.getCountryCode());
			}
			if (newValue.getZipCode() != null && !newValue.getZipCode().equals(oldValue.getZipCode())) {
				oldValue.setZipCode(newValue.getZipCode());
			}
			if (newValue.getCityName() != null && !newValue.getCityName().equals(oldValue.getCityName())) {
				oldValue.setCityName(newValue.getCityName());
			}
			if (newValue.getStreetName() != null && !newValue.getStreetName().equals(oldValue.getStreetName())) {
				oldValue.setStreetName(newValue.getStreetName());
			}
		}

		// StopArea Parent
		if (newValue.getParent() == null) {
			oldValue.setParent(null);
		} else {
			String objectId = newValue.getParent().getObjectId();
			StopArea stopArea = cache.getStopAreas().get(objectId);
			if (stopArea == null) {
				stopArea = stopAreaDAO.findByObjectId(params.getStopAreaReferentialId(),objectId);
				if (stopArea != null) {
					cache.getStopAreas().put(objectId, stopArea);
				}
			}

			if (stopArea == null) {
				stopArea = ObjectFactory.getStopArea(cache, objectId);
			}
			if (context.containsKey(Constant.AREA_BLOC))
				oldValue.forceParent(stopArea);
			else
				oldValue.setParent(stopArea);
			stopAreaUpdater.update(context, oldValue.getParent(), newValue.getParent());
		}

		// AccessPoint
		Collection<AccessPoint> addedAccessPoint = CollectionUtil.substract(newValue.getAccessPoints(),
				oldValue.getAccessPoints(), NeptuneIdentifiedObjectComparator.INSTANCE);

		List<AccessPoint> accessPoints = null;
		for (AccessPoint item : addedAccessPoint) {
			AccessPoint accessPoint = cache.getAccessPoints().get(item.getObjectId());
			if (accessPoint == null) {
				if (accessPoints == null) {
					accessPoints = accessPointDAO.findByObjectId(UpdaterUtils.getObjectIds(addedAccessPoint));
					for (AccessPoint object : accessPoints) {
						cache.getAccessPoints().put(object.getObjectId(), object);
					}
				}
				accessPoint = cache.getAccessPoints().get(item.getObjectId());
			}

			if (accessPoint == null) {
				accessPoint = ObjectFactory.getAccessPoint(cache, item.getObjectId());
			} else {
				twoDatabaseAccessPointOneTest(validationReporter, context, accessPoint, item, data);
			}
			accessPoint.setContainedIn(oldValue);
		}

		Collection<Pair<AccessPoint, AccessPoint>> modifiedAccessPoint = CollectionUtil.intersection(
				oldValue.getAccessPoints(), newValue.getAccessPoints(), NeptuneIdentifiedObjectComparator.INSTANCE);
		for (Pair<AccessPoint, AccessPoint> pair : modifiedAccessPoint) {
			accessPointUpdater.update(context, pair.getLeft(), pair.getRight());
		}

		// AccessLink
		Collection<AccessLink> addedAccessLink = CollectionUtil.substract(newValue.getAccessLinks(),
				oldValue.getAccessLinks(), NeptuneIdentifiedObjectComparator.INSTANCE);

		List<AccessLink> accessLinks = null;
		for (AccessLink item : addedAccessLink) {

			AccessLink accessLink = cache.getAccessLinks().get(item.getObjectId());
			if (accessLink == null) {
				if (accessLinks == null) {
					accessLinks = accessLinkDAO.findByObjectId(UpdaterUtils.getObjectIds(addedAccessLink));
					for (AccessLink object : accessLinks) {
						cache.getAccessLinks().put(object.getObjectId(), object);
					}
				}
				accessLink = cache.getAccessLinks().get(item.getObjectId());
			}

			if (accessLink == null) {
				accessLink = ObjectFactory.getAccessLink(cache, item.getObjectId());
			}
			accessLink.setStopArea(oldValue);
		}

		Collection<Pair<AccessLink, AccessLink>> modifiedAccessLink = CollectionUtil.intersection(
				oldValue.getAccessLinks(), newValue.getAccessLinks(), NeptuneIdentifiedObjectComparator.INSTANCE);
		for (Pair<AccessLink, AccessLink> pair : modifiedAccessLink) {
			accessLinkUpdater.update(context, pair.getLeft(), pair.getRight());
		}


		monitor.stop();

	}

	/**
	 * Test 2-DATABASE-StopArea-1
	 * 
	 * @param validationReporter
	 * @param context
	 * @param oldParent
	 * @param newParent
	 */
	private void twoDatabaseStopAreaOneTest(ValidationReporter validationReporter, Context context, StopArea oldValue,
			StopArea newValue, ValidationData data) {
		if (!ChouetteModelUtil.sameValue(oldValue.getParent(), newValue.getParent()))
			validationReporter.addCheckPointReportError(context, null, ValidationConstant.DATABASE_STOP_AREA_1,ValidationConstant.DATABASE_STOP_AREA_1,
					data.getDataLocations().get(newValue.getObjectId()));
		else
			validationReporter.reportSuccess(context, ValidationConstant.DATABASE_STOP_AREA_1);
	}

	/**
	 * Test 2-DATABASE-StopArea-2
	 * 
	 * @param validationReporter
	 * @param context
	 * @param oldSA
	 * @param newSA
	 */
	private void twoDatabaseStopAreaTwoTest(ValidationReporter validationReporter, Context context, StopArea oldSA,
			StopArea newSA, ValidationData data) {
		if (oldSA != null && newSA != null) {
			if (!ChouetteModelUtil.sameValue(oldSA.getAreaType(), newSA.getAreaType()))
				validationReporter.addCheckPointReportError(context, null, ValidationConstant.DATABASE_STOP_AREA_2,ValidationConstant.DATABASE_STOP_AREA_2,
						data.getDataLocations().get(newSA.getObjectId()));
			else
				validationReporter.reportSuccess(context, ValidationConstant.DATABASE_STOP_AREA_2);
		}
	}

	/**
	 * Test 2-DATABASE-Access-Point-1
	 * 
	 * @param validationReporter
	 * @param context
	 * @param oldAP
	 * @param newAP
	 * @param data
	 */
	private void twoDatabaseAccessPointOneTest(ValidationReporter validationReporter, Context context,
			AccessPoint oldAP, AccessPoint newAP, ValidationData data) {
		if (!ChouetteModelUtil.sameValue(oldAP.getContainedIn(), newAP.getContainedIn()))
			validationReporter.addCheckPointReportError(context, null, ValidationConstant.DATABASE_ACCESS_POINT_1, ValidationConstant.DATABASE_ACCESS_POINT_1,
					data.getDataLocations().get(newAP.getObjectId()));
		else
			validationReporter.reportSuccess(context, ValidationConstant.DATABASE_ACCESS_POINT_1);
	}

}
