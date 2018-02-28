package mobi.chouette.exchange.importer.updater;

import javax.ejb.Stateless;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validation.ValidationData;
import mobi.chouette.exchange.validation.report.ValidationReporter;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.util.ChouetteModelUtil;
import mobi.chouette.model.util.Referential;

@Stateless(name = StopPointUpdater.BEAN_NAME)
public class StopPointUpdater implements Updater<StopPoint> {

	public static final String BEAN_NAME = "StopPointUpdater";

//	@EJB
//	private StopAreaDAO stopAreaDAO;
//
//	@EJB(beanName = StopAreaUpdater.BEAN_NAME)
//	private Updater<StopArea> stopAreaUpdater;
//
	@Override
	public void update(Context context, StopPoint oldValue, StopPoint newValue) throws Exception {

		if (newValue.isSaved()) {
			return;
		}
		newValue.setSaved(true);

//		Monitor monitor = MonitorFactory.start(BEAN_NAME);
		Referential cache = (Referential) context.get(Constant.CACHE);
		cache.getStopPoints().put(oldValue.getObjectId(), oldValue);
		
		// Database test init
		ValidationReporter validationReporter = ValidationReporter.Factory.getInstance();
		validationReporter.addItemToValidationReport(context, ValidationConstant.DATABASE_STOP_POINT_2, "E");
		validationReporter.addItemToValidationReport(context, ValidationConstant.DATABASE_STOP_POINT_3, "W");
		ValidationData data = (ValidationData) context.get(Constant.VALIDATION_DATA);
		
		if (oldValue.isDetached()) {
			// object does not exist in database
			oldValue.setObjectId(newValue.getObjectId());
			oldValue.setObjectVersion(newValue.getObjectVersion());
			oldValue.setCreationTime(newValue.getCreationTime());
			oldValue.setForAlighting(newValue.getForAlighting());
			oldValue.setForBoarding(newValue.getForBoarding());
			oldValue.setStopAreaId(newValue.getStopAreaId());
			oldValue.setDetached(false);
		} else {
			twoDatabaseStopPointTwoTest(validationReporter, context, oldValue, newValue, data);
			twoDatabaseStopPointThreeTest(validationReporter, context, oldValue.getContainedInStopArea(), newValue.getContainedInStopArea(), data);
			if (newValue.getObjectId() != null && !newValue.getObjectId().equals(oldValue.getObjectId())) {
				oldValue.setObjectId(newValue.getObjectId());
			}
			if (newValue.getObjectVersion() != null && !newValue.getObjectVersion().equals(oldValue.getObjectVersion())) {
				oldValue.setObjectVersion(newValue.getObjectVersion());
			}
			if (newValue.getCreationTime() != null && !newValue.getCreationTime().equals(oldValue.getCreationTime())) {
				oldValue.setCreationTime(newValue.getCreationTime());
			}

			// Boarding and alighting
			if (newValue.getForAlighting() != null && !newValue.getForAlighting().equals(oldValue.getForAlighting())) {
				oldValue.setForAlighting(newValue.getForAlighting());
			}

			if (newValue.getForBoarding() != null && !newValue.getForBoarding().equals(oldValue.getForBoarding())) {
				oldValue.setForBoarding(newValue.getForBoarding());
			}
		}

		// StopArea
		
//		if (newValue.getContainedInStopArea() == null) {
//			oldValue.setContainedInStopArea(null);
//		} else {
//			String objectId = newValue.getContainedInStopArea().getObjectId();
//			StopArea stopArea = cache.getStopAreas().get(objectId);
//			if (stopArea == null) {
//				stopArea = stopAreaDAO.findByObjectId(objectId);
//				if (stopArea != null) {
//					cache.getStopAreas().put(objectId, stopArea);
//				}
//			}
//
//			if (stopArea == null) {
//				stopArea = ObjectFactory.getStopArea(cache, objectId);
//			}
//			
//			oldValue.setContainedInStopArea(stopArea);
//
//			if (!context.containsKey(Constant.AREA_BLOC))
//			   stopAreaUpdater.update(context, oldValue.getContainedInStopArea(), newValue.getContainedInStopArea());
//		}
//		monitor.stop();

	}
	
	
	/**
	 * Test 2-DATABASE-StopPoint-2
	 * @param validationReporter
	 * @param context
	 * @param oldSp
	 * @param newSp
	 */
	private void twoDatabaseStopPointTwoTest(ValidationReporter validationReporter, Context context, StopPoint oldSp, StopPoint newSp, ValidationData data) {
		if(oldSp !=null && newSp != null) {
			if(oldSp.getPosition() != null && newSp.getPosition() != null) {
				if(!oldSp.getPosition().equals(newSp.getPosition()))
					validationReporter.addCheckPointReportError(context, null, ValidationConstant.DATABASE_STOP_POINT_2,ValidationConstant.DATABASE_STOP_POINT_2, data.getDataLocations().get(newSp.getObjectId()));
				else
					validationReporter.reportSuccess(context, ValidationConstant.DATABASE_STOP_POINT_2);
			}
		}
	}
	
	
	/**
	 * Test 2-DATABASE-StopPoint-3
	 * @param validationReporter
	 * @param context
	 * @param oldSp
	 * @param newSp
	 */
	private void twoDatabaseStopPointThreeTest(ValidationReporter validationReporter, Context context, StopArea oldSA, StopArea newSA, ValidationData data) {
		if(!ChouetteModelUtil.sameValue(oldSA, newSA))
			validationReporter.addCheckPointReportError(context, null, ValidationConstant.DATABASE_STOP_POINT_3,ValidationConstant.DATABASE_STOP_POINT_3, data.getDataLocations().get(newSA.getObjectId()));
		else
			validationReporter.reportSuccess(context, ValidationConstant.DATABASE_STOP_POINT_3);
	}
}
