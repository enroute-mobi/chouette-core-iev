/**
 *  !!! Generated Code !!!
 *  Do not edit !
 *  Created by create_NetexStifImportFileSetTests.py
 */

package mobi.chouette.exchange.netex_stif.importer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.testng.annotations.Test;

public class NetexStifImportFileSetTests extends AbstractNetexStifImportFileSetTests {

	@Deployment
	public static EnterpriseArchive createDeployment() {
		return AbstractNetexStifImportFileSetTests.createDeployment(NetexStifImportFileSetTests.class);
	}

	@Test(groups = { "1726" }, testName = "1726-01", description = "Importer un fichier NeTEx par l'IHM", priority = 1)
	public void verifyCard1726_01() throws Exception {
		doImport("OFFRE_SNTYO_20170805011726.zip", "OK");
	}

	@Test(groups = { "1726" }, testName = "1726-02", description = "Importer un fichier NeTEx par l'IHM", priority = 2)
	public void verifyCard1726_02() throws Exception {
		doImport("OFFRE-SNTYO-20170805021726.zip", "OK");
	}

	@Test(groups = { "1726" }, testName = "1726-03", description = "Importer un fichier NeTEx par l'IHM", priority = 3)
	public void verifyCard1726_03() throws Exception {
		doImport("OFFRE-SNTYO-20170805031726.zip", "OK");
	}

	@Test(groups = { "1726" }, testName = "1726-04", description = "Importer un fichier NeTEx par l'IHM", priority = 4)
	public void verifyCard1726_04() throws Exception {
		doImport("OFFRE-SNTYO-20170805041726.zip", "OK");
	}

	@Test(groups = {
			"2113" }, testName = "2113-00", description = "[Netex] Organisation des frames du fichier commun.xml", priority = 5)
	public void verifyCard2113_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002113.zip", "NOK", "commun.xml:ERROR:2-NeTExSTIF-1:2_netexstif_1_1",
				"commun.xml:ERROR:2-NeTExSTIF-1:2_netexstif_1_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2114" }, testName = "2114-00", description = "[Netex] Organisation des frames du fichier calendriers.xml", priority = 6)
	public void verifyCard2114_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002114.zip", "NOK", "calendriers.xml:ERROR:2-NeTExSTIF-2:2_netexstif_2_1",
				"calendriers.xml:ERROR:2-NeTExSTIF-2:2_netexstif_2_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2115" }, testName = "2115-00", description = "[Netex] Organisation des frames des fichiers offre_xxx.xml", priority = 7)
	public void verifyCard2115_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002115.zip", "NOK", "offre_C00163_01.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_1",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_3",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_1",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_2",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_3",
				"offre_C00166_05.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00168_08.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00171_11.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2293" }, testName = "2293-00", description = "[Netex] Contrôle de la syntaxe des identifiants", priority = 8)
	public void verifyCard2293_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002293.zip", "NOK", "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2294" }, testName = "2294-00", description = "[Netex] Contrôle de l'attribut 'changed'", priority = 9)
	public void verifyCard2294_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002294.zip", "NOK", "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2295" }, testName = "2295-00", description = "[Netex] Contrôle de l'attribut 'modification'", priority = 10)
	public void verifyCard2295_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002295.zip", "NOK", "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

/*
	@Test(groups = {
			"2296" }, testName = "2296-00", description = "[Netex] Contrôle de la syntaxe des références", priority = 11)
	public void verifyCard2296_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002296.zip", "NOK", "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7",
				"offre_C00166_05.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00168_08.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}
	*/

	@Test(groups = {
			"2297" }, testName = "2297-00", description = "[Netex] Contrôle de la syntaxe des références internes", priority = 12)
	public void verifyCard2297_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002297.zip", "NOK", "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00165_04.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2298" }, testName = "2298-00", description = "[Netex] Contrôle de la syntaxe des références externes", priority = 13)
	public void verifyCard2298_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002298.zip", "NOK", "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2300" }, testName = "2300-00", description = "[Netex] Contrôle de la syntaxe des références externes", priority = 14)
	public void verifyCard2300_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002300.zip", "NOK", "offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2301" }, testName = "2301-00", description = "[Netex] Contrôle de l'objet Notice : présence de l'attribut Text", priority = 15)
	public void verifyCard2301_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002301.zip", "NOK", "commun.xml:ERROR:2-NeTExSTIF-Notice-1:2_netexstif_notice_1");
	}

	@Test(groups = {
			"2302" }, testName = "2302-00", description = "[Netex] Contrôle de l'objet Notice : TypeOfNoticeRef", priority = 16)
	public void verifyCard2302_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002302.zip", "NOK",
				"commun.xml:WARNING:2-NeTExSTIF-Notice-2:2_netexstif_notice_2",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2303" }, testName = "2303-00", description = "[Netex] Contrôle de l'objet DayTypeAssignment : OperatingDayRef", priority = 17)
	public void verifyCard2303_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002303.zip", "NOK",
				"calendriers.xml:ERROR:2-NeTExSTIF-DayTypeAssignment-1:2_netexstif_daytypeassignment_1");
	}

	@Test(groups = {
			"2304" }, testName = "2304-00", description = "[Netex] Contrôle de l'objet DayTypeAssignment : IsAvailable", priority = 18)
	public void verifyCard2304_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002304.zip", "NOK",
				"calendriers.xml:ERROR:2-NeTExSTIF-DayTypeAssignment-2:2_netexstif_daytypeassignment_2");
	}

	@Test(groups = {
			"2305" }, testName = "2305-00", description = "[Netex] Contrôle de l'objet DayType : complétude", priority = 19)
	public void verifyCard2305_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002305.zip", "OK",
				"calendriers.xml:WARNING:2-NeTExSTIF-DayType-1:2_netexstif_daytype_1");
	}

	@Test(groups = {
			"2306" }, testName = "2306-00", description = "[Netex] Contrôle de l'objet OperatingPeriod : chronologie", priority = 20)
	public void verifyCard2306_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002306.zip", "NOK",
				"calendriers.xml:ERROR:2-NeTExSTIF-OperatingPeriod-1:2_netexstif_operatingperiod_1");
	}

	@Test(groups = {
			"2307" }, testName = "2307-00", description = "[Netex] Contrôle de l'objet DayType : types de jour sur période", priority = 21)
	public void verifyCard2307_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002307.zip", "NOK",
				"calendriers.xml:ERROR:2-NeTExSTIF-DayType-2:2_netexstif_daytype_2",
				"calendriers.xml:ERROR:2-NeTExSTIF-DayTypeAssignment-2:2_netexstif_daytypeassignment_2");
	}

	@Test(groups = {
			"2308" }, testName = "2308-00", description = "[Netex] Contrôle de l'objet Route : DirectionType", priority = 22)
	public void verifyCard2308_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002308.zip", "NOK", "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2309" }, testName = "2309-00", description = "[Netex] Contrôle de l'objet Route : cohérence des routes inverses", priority = 23)
	public void verifyCard2309_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002309.zip", "OK",
				"offre_C00164_03.xml:WARNING:2-NeTExSTIF-Route-2:2_netexstif_route_2_2",
				"offre_C00171_11.xml:WARNING:2-NeTExSTIF-Route-2:2_netexstif_route_2_2");
	}

	@Test(groups = {
			"2310" }, testName = "2310-00", description = "[Netex] Contrôle de l'objet Route : Séquence des arrêts", priority = 24)
	public void verifyCard2310_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002310.zip", "OK");
	}

	@Test(groups = {
			"2311" }, testName = "2311-00", description = "[Netex] Contrôle de l'objet Direction : Name", priority = 25)
	public void verifyCard2311_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002311.zip", "OK");
	}

	@Test(groups = {
			"2312" }, testName = "2312-00", description = "[Netex] Contrôle de l'objet Direction : Attributs interdits", priority = 26)
	public void verifyCard2312_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002312.zip", "NOK", "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2313" }, testName = "2313-00", description = "[Netex] Contrôle de l'objet ServiceJourneyPattern : RouteRef", priority = 27)
	public void verifyCard2313_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002313.zip", "NOK", "offre_C00163_01.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-ServiceJourneyPattern-1:2_netexstif_servicejourneypattern_1",
				"offre_C00164_03.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2314" }, testName = "2314-00", description = "[Netex] Contrôle de l'objet ServiceJourneyPattern : pointsInSequence", priority = 28)
	public void verifyCard2314_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002314.zip", "NOK", "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00164_03.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2315" }, testName = "2315-00", description = "[Netex] Contrôle de l'objet ServiceJourneyPattern : ServiceJourneyPatternType", priority = 29)
	public void verifyCard2315_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002315.zip", "NOK", "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00164_03.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2316" }, testName = "2316-00", description = "[Netex] Contrôle de l'objet ServiceJourneyPattern : ordre des StopPointInJourneyPattern", priority = 30)
	public void verifyCard2316_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002316.zip", "NOK",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-ServiceJourneyPattern-4:2_netexstif_servicejourneypattern_4");
	}

	@Test(groups = {
			"2317" }, testName = "2317-00", description = "[Netex] Contrôle de l'objet Route : cohérence des ServiceJourneyPattern sur les interdictions de montée et descente", priority = 31)
	public void verifyCard2317_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002317.zip", "OK");
	}

	@Test(groups = {
			"2318" }, testName = "2318-00", description = "[Netex] Contrôle de l'objet PassengerStopAssignment : complétude", priority = 32)
	public void verifyCard2318_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002318.zip", "NOK", "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2319" }, testName = "2319-00", description = "[Netex] Contrôle de l'objet RoutingConstraintZone : complétude", priority = 33)
	public void verifyCard2319_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002319.zip", "NOK", "offre_C00163_01.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-RoutingConstraintZone-1:2_netexstif_routingconstraintzone_1",
				"offre_C00164_03.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2320" }, testName = "2320-00", description = "[Netex] Contrôle de l'objet RoutingConstraintZone : attribut ZoneUse", priority = 34)
	public void verifyCard2320_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002320.zip", "NOK", "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2321" }, testName = "2321-00", description = "[Netex] Contrôle de l'objet ServiceJourney : JourneyPatternRef", priority = 35)
	public void verifyCard2321_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002321.zip", "NOK", "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2322" }, testName = "2322-00", description = "[Netex] Contrôle de l'objet ServiceJourney : trainNumbers", priority = 36)
	public void verifyCard2322_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002322.zip", "OK");
	}

	@Test(groups = {
			"2323" }, testName = "2323-00", description = "[Netex] Contrôle de l'objet ServiceJourney : complétude des passingTimes", priority = 37)
	public void verifyCard2323_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002323.zip", "NOK",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-ServiceJourney-3:2_netexstif_servicejourney_3");
	}

	@Test(groups = {
			"2324" }, testName = "2324-00", description = "[Netex] Contrôle de l'objet ServiceJourney : chronologie des passingTimes", priority = 38)
	public void verifyCard2324_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002324.zip", "NOK",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-PassingTime-2:2_netexstif_passingtime_2",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-ServiceJourney-4:2_netexstif_servicejourney_4");
	}

	@Test(groups = {
			"2325" }, testName = "2325-00", description = "[Netex] Contrôle de l'objet PassingTime : complétude", priority = 39)
	public void verifyCard2325_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002325.zip", "NOK", "offre_C00165_04.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = {
			"2326" }, testName = "2326-00", description = "[Netex] Contrôle de l'objet PassingTime : chronologie", priority = 40)
	public void verifyCard2326_00() throws Exception {
		doImport("OFFRE_SNTYO_20170821002326.zip", "NOK",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-PassingTime-2:2_netexstif_passingtime_2",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-ServiceJourney-4:2_netexstif_servicejourney_4");
	}

	@Test(groups = {
			"2113" }, testName = "2113-01", description = "[Netex] Organisation des frames du fichier commun.xml", priority = 41)
	public void verifyCard2113_01() throws Exception {
		doImport("OFFRE_SNTYO_20170821012113.zip", "NOK", "commun.xml:ERROR:2-NeTExSTIF-1:2_netexstif_1_1",
				"commun.xml:ERROR:2-NeTExSTIF-1:2_netexstif_1_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2114" }, testName = "2114-01", description = "[Netex] Organisation des frames du fichier calendriers.xml", priority = 42)
	public void verifyCard2114_01() throws Exception {
		doImport("OFFRE_SNTYO_20170821012114.zip", "NOK", "calendriers.xml:ERROR:2-NeTExSTIF-2:2_netexstif_2_1",
				"calendriers.xml:ERROR:2-NeTExSTIF-2:2_netexstif_2_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2307" }, testName = "2307-01", description = "[Netex] Contrôle de l'objet DayType : types de jour sur période", priority = 43)
	public void verifyCard2307_01() throws Exception {
		doImport("OFFRE_SNTYO_20170821012307.zip", "NOK", "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2113" }, testName = "2113-02", description = "[Netex] Organisation des frames du fichier commun.xml", priority = 44)
	public void verifyCard2113_02() throws Exception {
		doImport("OFFRE_SNTYO_20170821022113.zip", "OK");
	}

	@Test(groups = {
			"2114" }, testName = "2114-02", description = "[Netex] Organisation des frames du fichier calendriers.xml", priority = 45)
	public void verifyCard2114_02() throws Exception {
		doImport("OFFRE_SNTYO_20170821022114.zip", "OK");
	}

	@Test(groups = {
			"2114" }, testName = "2114-03", description = "[Netex] Organisation des frames du fichier calendriers.xml", priority = 46)
	public void verifyCard2114_03() throws Exception {
		doImport("OFFRE_SNTYO_20170821032114.zip", "OK");
	}

	@Test(groups = {
			"2114" }, testName = "2114-04", description = "[Netex] Organisation des frames du fichier calendriers.xml", priority = 47)
	public void verifyCard2114_04() throws Exception {
		doImport("OFFRE_SNTYO_20170821042114.zip", "NOK", "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}
}
