package mobi.chouette.exchange.importer;

import java.io.StringWriter;
import java.sql.Time;

import javax.naming.InitialContext;

import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.model.StopPoint;
import mobi.chouette.model.VehicleJourney;
import mobi.chouette.model.VehicleJourneyAtStop;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LineRegisterCommandTest {
	
	
	private LineRegisterCommand lineRegister = null;
	
	
	
	@SuppressWarnings("deprecation")
	@Test (groups = { "write" }, description = "write command")
	public void testLineRegisterWrite() throws Exception 
	{
		InitialContext initialContext = new InitialContext();
		Context context = new Context();
		context.put(Constant.INITIAL_CONTEXT, initialContext);
		
		StringWriter buffer = new StringWriter(); 
		VehicleJourney neptuneObject = new VehicleJourney();
	    neptuneObject.setId(4321L);
	    StopPoint sp = new StopPoint();
	    sp.setId(1001L);
	    
	    VehicleJourneyAtStop vjas = new VehicleJourneyAtStop();
        vjas.setStopPoint(sp);
	    
        vjas.setArrivalTime(new Time(23,59,0));
        
        vjas.setDepartureTime(new Time(0,5,0));
        vjas.setArrivalDayOffset(0);
        
        vjas.setDepartureDayOffset(1);
        
		lineRegister = new LineRegisterCommand();
		
		lineRegister.write(buffer, neptuneObject, sp, vjas);
		
		
		Assert.assertEquals(buffer.toString(), "4321"+Constant.COPY_SEP+"1001"+Constant.COPY_SEP+"23:59:00"+Constant.COPY_SEP+"00:05:00"+Constant.COPY_SEP+"0"+Constant.COPY_SEP+"1\n", "Invalid data entry for buffer");
	
		
	}
}
