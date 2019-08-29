/**
 *  !!! Generated Code (2017-11-20 11:20:50)!!!
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

	@Test(groups = { "2111" }, testName = "2111-00", description = "[Netex] Conformité XML", priority = 1)
	public void verifyCard2111_00() throws Exception {
		doImport("OFFRE_SNTYO_2111_00.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00164_03.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00166_05.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00171_11.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = { "2111" }, testName = "2111-01", description = "[Netex] Conformité XML", priority = 2)
	public void verifyCard2111_01() throws Exception {
		doImport("OFFRE_SNTYO_2111_01.zip", "NOK", 1, 8, 6, "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = { "2111" }, testName = "2111-02", description = "[Netex] Conformité XML", priority = 3)
	public void verifyCard2111_02() throws Exception {
		doImport("OFFRE_SNTYO_2111_02.zip", "NOK", 1, 8, 6, "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = { "2111" }, testName = "2111-03", description = "[Netex] Conformité XML", priority = 4)
	public void verifyCard2111_03() throws Exception {
		doImport("OFFRE_SNTYO_2111_03.zip", "NOK", 1, 8, 6, "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = { "2111" }, testName = "2111-04", description = "[Netex] Conformité XML", priority = 5)
	public void verifyCard2111_04() throws Exception {
		doImport("OFFRE_SNTYO_2111_04.zip", "OK", 1, 8, 6);
	}

	@Test(groups = { "2111" }, testName = "2111-05", description = "[Netex] Conformité XML", priority = 6)
	public void verifyCard2111_05() throws Exception {
		doImport("OFFRE_SNTYO_2111_05.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-4:2_netexstif_4",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7",
				"offre_C00165_04.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-4:2_netexstif_4",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7");
	}

	@Test(groups = { "2111" }, testName = "2111-06", description = "[Netex] Conformité XML", priority = 7)
	public void verifyCard2111_06() throws Exception {
		doImport("OFFRE_SNTYO_2111_06.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00164_03.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00165_04.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00166_05.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00168_08.xml:ERROR:1-NeTExStif-2:1_netexstif_2");
	}

	@Test(groups = { "2111" }, testName = "2111-07", description = "[Netex] Conformité XML", priority = 8)
	public void verifyCard2111_07() throws Exception {
		doImport("OFFRE_SNTYO_2111_07.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00164_03.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00165_04.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00166_05.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00168_08.xml:WARNING:2-NeTExSTIF-Route-2:2_netexstif_route_2_1");
	}

	@Test(groups = {
			"2113" }, testName = "2113-00", description = "[Netex] Organisation des frames du fichier commun.xml", priority = 9)
	public void verifyCard2113_00() throws Exception {
		doImport("OFFRE_SNTYO_2113_00.zip", "NOK", 1, 8, 6, "commun.xml:ERROR:2-NeTExSTIF-1:2_netexstif_1_1",
				"commun.xml:ERROR:2-NeTExSTIF-1:2_netexstif_1_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2114" }, testName = "2114-00", description = "[Netex] Organisation des frames du fichier calendriers.xml", priority = 10)
	public void verifyCard2114_00() throws Exception {
		doImport("OFFRE_SNTYO_2114_00.zip", "NOK", 1, 8, 6,
				"calendriers.xml:ERROR:2-NeTExSTIF-2:2_netexstif_2_1",
				"calendriers.xml:ERROR:2-NeTExSTIF-2:2_netexstif_2_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2115" }, testName = "2115-00", description = "[Netex] Organisation des frames des fichiers offre_xxx.xml", priority = 11)
	public void verifyCard2115_00() throws Exception {
		doImport("OFFRE_SNTYO_2115_00.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_1",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_2",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_3",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_4",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-ServiceJourney-3:2_netexstif_servicejourney_3",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_3",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-3:2_netexstif_3_4");
	}

	@Test(groups = {
			"2293" }, testName = "2293-00", description = "[Netex] Contrôle de la syntaxe des identifiants", priority = 12)
	public void verifyCard2293_00() throws Exception {
		doImport("OFFRE_SNTYO_2293-00.zip", "NOK", 1, 8, 6, "calendriers.xml:ERROR:2-NeTExSTIF-4:2_netexstif_4",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-4:2_netexstif_4",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7");
	}

	/* @Test(groups = {
			"2294" }, testName = "2294-00", description = "[Netex] Contrôle de l'attribut 'changed'", priority = 13)
	public void verifyCard2294_00() throws Exception {
		doImport("OFFRE_SNTYO_2294_00.zip", "OK", 1, 8, 6, "calendriers.xml:WARNING:2-NeTExSTIF-5:2_netexstif_5");
	}

	@Test(groups = {
			"2294" }, testName = "2294-01", description = "[Netex] Contrôle de l'attribut 'changed'", priority = 14)
	public void verifyCard2294_01() throws Exception {
		doImport("OFFRE_SNTYO_2294_01.zip", "OK", 1, 8, 6, "commun.xml:WARNING:2-NeTExSTIF-5:2_netexstif_5");
	}
	@Test(groups = {
		"2294" }, testName = "2294-02", description = "[Netex] Contrôle de l'attribut 'changed'", priority = 50)
	public void verifyCard2294_02() throws Exception {
	doImport("OFFRE_SNTYO_2294_02.zip", "OK", 1, 8, 6, "offre_C00171_11.xml:WARNING:2-NeTExSTIF-5:2_netexstif_5");
	} */


	@Test(groups = {
			"2295" }, testName = "2295-00", description = "[Netex] Contrôle de l'attribut 'modification'", priority = 15)
	public void verifyCard2295_00() throws Exception {
		doImport("OFFRE_SNTYO_2295_00.zip", "NOK", 1, 8, 6, "calendriers.xml:ERROR:2-NeTExSTIF-6:2_netexstif_6",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-6:2_netexstif_6");
	}

	@Test(groups = {
			"2296" }, testName = "2296-00", description = "[Netex] Contrôle de la syntaxe des références", priority = 16)
	public void verifyCard2296_00() throws Exception {
		doImport("OFFRE_SNTYO_2296-00.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:2-NeTExSTIF-4:2_netexstif_4",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-4:2_netexstif_4",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7");
	}

	@Test(groups = {
			"2296" }, testName = "2296-01", description = "[Netex] Contrôle de la syntaxe des références", priority = 17)
	public void verifyCard2296_01() throws Exception {
		doImport("OFFRE_SNTYO_2296-01.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:2-NeTExSTIF-4:2_netexstif_4",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-4:2_netexstif_4",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-7:2_netexstif_7");
	}

	@Test(groups = {
			"2297" }, testName = "2297-00", description = "[Netex] Contrôle de la syntaxe des références internes", priority = 18)
	public void verifyCard2297_00() throws Exception {
		doImport("OFFRE_SNTYO_2297_00.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:2-NeTExSTIF-8:2_netexstif_8_1",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-8:2_netexstif_8_2");
	}

	@Test(groups = {
			"2297" }, testName = "2297-01", description = "[Netex] Contrôle de la syntaxe des références internes", priority = 19)
	public void verifyCard2297_01() throws Exception {
		doImport("OFFRE_SNTYO_2297_01.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:2-NeTExSTIF-8:2_netexstif_8_1",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-8:2_netexstif_8_2");
	}

	@Test(groups = {
			"2298" }, testName = "2298-00", description = "[Netex] Contrôle de la syntaxe des références externes", priority = 20)
	public void verifyCard2298_00() throws Exception {
		doImport("OFFRE_SNTYO_2298_00.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:2-NeTExSTIF-9:2_netexstif_9_1",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-9:2_netexstif_9_2");
	}

	@Test(groups = {
			"2298" }, testName = "2298-01", description = "[Netex] Contrôle de la syntaxe des références externes", priority = 21)
	public void verifyCard2298_01() throws Exception {
		doImport("OFFRE_SNTYO_2298_01.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:2-NeTExSTIF-9:2_netexstif_9_1",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-9:2_netexstif_9_2");
	}

	@Test(groups = {
			"2300" }, testName = "2300-00", description = "[Netex] Contrôle de la syntaxe des références externes", priority = 22)
	public void verifyCard2300_00() throws Exception {
		doImport("OFFRE_SNTYO_2300_00.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2301" }, testName = "2301-00", description = "[Netex] Contrôle de l'objet Notice : présence de l'attribut Text", priority = 23)
	public void verifyCard2301_00() throws Exception {
		doImport("OFFRE_SNTYO_2301_00.zip", "NOK", 1, 8, 6,
				"commun.xml:ERROR:2-NeTExSTIF-Notice-1:2_netexstif_notice_1",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2302" }, testName = "2302-00", description = "[Netex] Contrôle de l'objet Notice : TypeOfNoticeRef", priority = 24)
	public void verifyCard2302_00() throws Exception {
		doImport("OFFRE_SNTYO_2302_00.zip", "NOK", 1, 8, 6,
				"commun.xml:WARNING:2-NeTExSTIF-Notice-2:2_netexstif_notice_2",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2303" }, testName = "2303-00", description = "[Netex] Contrôle de l'objet DayTypeAssignment : OperatingDayRef", priority = 25)
	public void verifyCard2303_00() throws Exception {
		doImport("OFFRE_SNTYO_2303_00.zip", "NOK", 1, 8, 6,
				"calendriers.xml:ERROR:2-NeTExSTIF-DayTypeAssignment-1:2_netexstif_daytypeassignment_1");
	}

	@Test(groups = {
			"2304" }, testName = "2304-00", description = "[Netex] Contrôle de l'objet DayTypeAssignment : IsAvailable", priority = 26)
	public void verifyCard2304_00() throws Exception {
		doImport("OFFRE_SNTYO_2304_00.zip", "NOK", 1, 8, 6,
				"calendriers.xml:ERROR:2-NeTExSTIF-DayTypeAssignment-2:2_netexstif_daytypeassignment_2");
	}

	@Test(groups = {
			"2305" }, testName = "2305-00", description = "[Netex] Contrôle de l'objet DayType : complétude", priority = 27)
	public void verifyCard2305_00() throws Exception {
		doImport("OFFRE_SNTYO_2305_00.zip", "OK", 1, 8, 6,
				"calendriers.xml:WARNING:2-NeTExSTIF-DayType-1:2_netexstif_daytype_1");
	}

	@Test(groups = {
			"2306" }, testName = "2306-00", description = "[Netex] Contrôle de l'objet OperatingPeriod : chronologie", priority = 28)
	public void verifyCard2306_00() throws Exception {
		doImport("OFFRE_SNTYO_2306_00.zip", "NOK", 1, 8, 6,
				"calendriers.xml:ERROR:2-NeTExSTIF-OperatingPeriod-1:2_netexstif_operatingperiod_1");
	}

	@Test(groups = {
			"2307" }, testName = "2307-00", description = "[Netex] Contrôle de l'objet DayType : types de jour sur période", priority = 29)
	public void verifyCard2307_00() throws Exception {
		doImport("OFFRE_SNTYO_2307_00.zip", "NOK", 1, 8, 6,
				"calendriers.xml:ERROR:2-NeTExSTIF-DayType-2:2_netexstif_daytype_2");
	}

	@Test(groups = {
			"2307" }, testName = "2307-01", description = "[Netex] Contrôle de l'objet DayType : types de jour sur période", priority = 30)
	public void verifyCard2307_01() throws Exception {
		doImport("OFFRE_SNTYO_2307_01.zip", "NOK", 1, 8, 6, "calendriers.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00166_05.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00168_08.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10",
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-10:2_netexstif_10");
	}

	@Test(groups = {
			"2308" }, testName = "2308-00", description = "[Netex] Contrôle de l'objet Route : DirectionType", priority = 31)
	public void verifyCard2308_00() throws Exception {
		doImport("OFFRE_SNTYO_2308_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-Route-1:2_netexstif_route_1");
	}

	@Test(groups = {
			"2309" }, testName = "2309-00", description = "[Netex] Contrôle de l'objet Route : cohérence des routes inverses", priority = 32)
	public void verifyCard2309_00() throws Exception {
		doImport("OFFRE_SNTYO_2309_00.zip", "OK", 1, 8, 6,
				"offre_C00163_01.xml:WARNING:2-NeTExSTIF-Route-2:2_netexstif_route_2_1",
				"offre_C00164_03.xml:WARNING:2-NeTExSTIF-Route-2:2_netexstif_route_2_2",
				"offre_C00171_11.xml:WARNING:2-NeTExSTIF-Route-2:2_netexstif_route_2_2");
	}

	@Test(groups = {
			"2310" }, testName = "2310-00", description = "[Netex] Contrôle de l'objet Route : Séquence des arrêts", priority = 33)
	public void verifyCard2310_00() throws Exception {
		doImport("OFFRE_SNTYO_2310_00.zip", "NOK", 1, 8, 6,
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-Route-3:2_netexstif_route_3");
	}

	@Test(groups = {
			"2311" }, testName = "2311-00", description = "[Netex] Contrôle de l'objet Direction : Name", priority = 34)
	public void verifyCard2311_00() throws Exception {
		doImport("OFFRE_SNTYO_2311_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-Direction-1:2_netexstif_direction_1",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-Direction-1:2_netexstif_direction_1");
	}

	@Test(groups = {
			"2312" }, testName = "2312-00", description = "[Netex] Contrôle de l'objet Direction : Attributs interdits", priority = 35)
	public void verifyCard2312_00() throws Exception {
		doImport("OFFRE_SNTYO_2312_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-Direction-2:2_netexstif_direction_2");
	}

	@Test(groups = {
			"2313" }, testName = "2313-00", description = "[Netex] Contrôle de l'objet ServiceJourneyPattern : RouteRef", priority = 36)
	public void verifyCard2313_00() throws Exception {
		doImport("OFFRE_SNTYO_2313_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-ServiceJourneyPattern-1:2_netexstif_servicejourneypattern_1");
	}

	@Test(groups = {
			"2314" }, testName = "2314-00", description = "[Netex] Contrôle de l'objet ServiceJourneyPattern : pointsInSequence", priority = 37)
	public void verifyCard2314_00() throws Exception {
		doImport("OFFRE_SNTYO_2314_00.zip", "NOK", 1, 8, 6, "offre_C00163_01.xml:ERROR:1-NeTExStif-2:1_netexstif_2",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-ServiceJourney-3:2_netexstif_servicejourney_3",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-ServiceJourneyPattern-2:2_netexstif_servicejourneypattern_2");
	}

	@Test(groups = {
			"2315" }, testName = "2315-00", description = "[Netex] Contrôle de l'objet ServiceJourneyPattern : ServiceJourneyPatternType", priority = 38)
	public void verifyCard2315_00() throws Exception {
		doImport("OFFRE_SNTYO_2315_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-ServiceJourneyPattern-3:2_netexstif_servicejourneypattern_3_1",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-ServiceJourneyPattern-3:2_netexstif_servicejourneypattern_3_2");
	}

	@Test(groups = {
			"2316" }, testName = "2316-00", description = "[Netex] Contrôle de l'objet ServiceJourneyPattern : ordre des StopPointInJourneyPattern", priority = 39)
	public void verifyCard2316_00() throws Exception {
		doImport("OFFRE_SNTYO_2316_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-ServiceJourneyPattern-4:2_netexstif_servicejourneypattern_4",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-Route-3:2_netexstif_route_3",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-ServiceJourney-3:2_netexstif_servicejourney_3",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-ServiceJourneyPattern-4:2_netexstif_servicejourneypattern_4");
	}

	@Test(groups = {
			"2317" }, testName = "2317-00", description = "[Netex] Contrôle de l'objet Route : cohérence des ServiceJourneyPattern sur les interdictions de montée et descente", priority = 40)
	public void verifyCard2317_00() throws Exception {
		doImport("OFFRE_SNTYO_2317_00.zip", "OK", 1, 8, 6,
				"offre_C00171_11.xml:WARNING:2-NeTExSTIF-Route-4:2_netexstif_route_4");
	}

	@Test(groups = {
			"2318" }, testName = "2318-00", description = "[Netex] Contrôle de l'objet PassengerStopAssignment : complétude", priority = 41)
	public void verifyCard2318_00() throws Exception {
		doImport("OFFRE_SNTYO_2318_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-PassengerStopAssignment-1:2_netexstif_passengerstopassignment_1");
	}

	@Test(groups = {
			"2319" }, testName = "2319-00", description = "[Netex] Contrôle de l'objet RoutingConstraintZone : complétude", priority = 42)
	public void verifyCard2319_00() throws Exception {
		doImport("OFFRE_SNTYO_2319_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-RoutingConstraintZone-1:2_netexstif_routingconstraintzone_1",
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-RoutingConstraintZone-1:2_netexstif_routingconstraintzone_1");
	}

	@Test(groups = {
			"2320" }, testName = "2320-00", description = "[Netex] Contrôle de l'objet RoutingConstraintZone : attribut ZoneUse", priority = 43)
	public void verifyCard2320_00() throws Exception {
		doImport("OFFRE_SNTYO_2320_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-RoutingConstraintZone-2:2_netexstif_routingconstraintzone_2");
	}

	@Test(groups = {
			"2321" }, testName = "2321-00", description = "[Netex] Contrôle de l'objet ServiceJourney : JourneyPatternRef", priority = 44)
	public void verifyCard2321_00() throws Exception {
		doImport("OFFRE_SNTYO_2321_00.zip", "NOK", 1, 8, 6,
				"offre_C00163_01.xml:ERROR:2-NeTExSTIF-ServiceJourney-1:2_netexstif_servicejourney_1");
	}

	@Test(groups = {
			"2322" }, testName = "2322-00", description = "[Netex] Contrôle de l'objet ServiceJourney : trainNumbers", priority = 45)
	public void verifyCard2322_00() throws Exception {
		doImport("OFFRE_SNTYO_2322_00.zip", "NOK", 1, 8, 6,
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-ServiceJourney-2:2_netexstif_servicejourney_2");
	}

	@Test(groups = {
			"2323" }, testName = "2323-00", description = "[Netex] Contrôle de l'objet ServiceJourney : complétude des passingTimes", priority = 46)
	public void verifyCard2323_00() throws Exception {
		doImport("OFFRE_SNTYO_2323_00.zip", "NOK", 1, 8, 6,
				"offre_C00164_03.xml:ERROR:2-NeTExSTIF-ServiceJourney-3:2_netexstif_servicejourney_3");
	}

	@Test(groups = {
			"2324" }, testName = "2324-00", description = "[Netex] Contrôle de l'objet ServiceJourney : chronologie des passingTimes", priority = 47)
	public void verifyCard2324_00() throws Exception {
		doImport("OFFRE_SNTYO_2324_00.zip", "NOK", 1, 8, 6,
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-PassingTime-2:2_netexstif_passingtime_2",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-ServiceJourney-4:2_netexstif_servicejourney_4");
	}

	@Test(groups = {
			"2325" }, testName = "2325-00", description = "[Netex] Contrôle de l'objet PassingTime : complétude", priority = 48)
	public void verifyCard2325_00() throws Exception {
		doImport("OFFRE_SNTYO_2325_00.zip", "NOK", 1, 8, 6,
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-PassingTime-1:2_netexstif_passingtime_1",
				"offre_C00165_04.xml:ERROR:2-NeTExSTIF-ServiceJourney-4:2_netexstif_servicejourney_4");
	}

	@Test(groups = {
			"2326" }, testName = "2326-00", description = "[Netex] Contrôle de l'objet PassingTime : chronologie", priority = 49)
	public void verifyCard2326_00() throws Exception {
		doImport("OFFRE_SNTYO_2326_00.zip", "NOK", 1, 8, 6,
				"offre_C00171_11.xml:ERROR:2-NeTExSTIF-PassingTime-2:2_netexstif_passingtime_2");
	}
}
