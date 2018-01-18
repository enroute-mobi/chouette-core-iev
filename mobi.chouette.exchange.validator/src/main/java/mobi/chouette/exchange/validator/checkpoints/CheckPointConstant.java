package mobi.chouette.exchange.validator.checkpoints;

import java.util.Arrays;

public class CheckPointConstant {

	// Check [Ligne] Appariement des itinéraires
	public static final String L3_Line_1 = "3-Line-1";

	// Check [Itinéraire] Deux arrêts d'une même ZDL ne peuvent pas se succéder
	// dans un itinéraire
	public static final String L3_Route_1 = "3-Route-1";

	// Check [Itinéraire] Vérification de l'itinéraire inverse
	public static final String L3_Route_2 = "3-Route-2";

	// Check [Itinéraire] Présence de missions
	public static final String L3_Route_3 = "3-Route-3";

	// Check [Itinéraire] Détection de double définition d'itinéraire
	public static final String L3_Route_4 = "3-Route-4";

	// Check [Itinéraire] Vérification des terminus de l'itinéraire inverse
	public static final String L3_Route_5 = "3-Route-5";

	// Check [Itinéraire] Un itinéraire doit contenir au moins 2 arrêts
	public static final String L3_Route_6 = "3-Route-6";


	// Check [Itinéraire] Utilisation des arrêts par les missions
	public static final String L3_Route_8 = "3-Route-8";

	// Check [Itinéraire] Existence d'une mission passant par tous les arrêts de
	// l'itinéraire
	public static final String L3_Route_9 = "3-Route-9";

	// Check [Itinéraire] Itinéraire & arrêt désactivé
	public static final String L3_Route_10 = "3-Route-10";

	// Check [Itinéraire] Présence de tracé
	public static final String L3_Route_11 = "3-Route-11";

	// Check [Mission] Doublon de missions dans une ligne
	public static final String L3_JourneyPattern_1 = "3-JourneyPattern-1";

	// Check [Mission] Présence de courses
	public static final String L3_JourneyPattern_2 = "3-JourneyPattern-2";

	// Check [Course] La durée d'attente à un arrêt ne doit pas être trop grande
	public static final String L3_VehicleJourney_1 = "3-VehicleJourney-1";

	// Check [Course] La vitesse entre deux arrêts doit être dans une fourchette
	// paramétrable
	public static final String L3_VehicleJourney_2 = "3-VehicleJourney-2";

	// Check [Course] Les vitesses entre 2 arrêts doivent être similaires pour
	// toutes les courses d'une même mission
	public static final String L3_VehicleJourney_3 = "3-VehicleJourney-3";

	// Check [Course] Une course doit avoir au moins un calendrier d'application
	public static final String L3_VehicleJourney_4 = "3-VehicleJourney-4";

	// Check [Course] Chronologie croissante des horaires
	public static final String L3_VehicleJourney_5 = "3-VehicleJourney-5";

	// Check [ITL] ITL & arret désactivé
	public static final String L3_RoutingConstraint_1 = "3-RoutingConstraint-1";

	// Check [ITL] Couverture de l'itinéraire
	public static final String L3_RoutingConstraint_2 = "3-RoutingConstraint-2";

	// Check [ITL] Définition minimale d'une ITL
	public static final String L3_RoutingConstraint_3 = "3-RoutingConstraint-3";

	// Check [Tracé] Proximité d'un tracé avec l'itinéraire associé
	public static final String L3_Shape_1 = "3-Shape-1";

	// Check [Tracés] Impact lors de la mise à jour de la BD-TOPO
	public static final String L3_Shape_3 = "3-Shape-3";

	// Check [Tracé] Cohérence d'un tracé avec l'itinéraire associé
	public static final String L3_Shape_2 = "3-Shape-2";

	// Check [Génériques] Contrôle du contenu selon un pattern
	public static final String L3_Generic_1 = "3-Generic-1";

	// Check [Génériques] Valeur min
	public static final String L3_Generic_2 = "3-Generic-2";

	// Check [Génériques] Unicité d'un attribut d'un objet dans une ligne
	public static final String L3_Generic_3 = "3-Generic-3";

	private CheckPointConstant() {
	}
	
	public static final boolean exists(String code)
	{
		String constant = "L"+code.replaceAll("-", "_");
		return Arrays.stream(CheckPointConstant.class.getDeclaredFields()).anyMatch(f -> f.getName().equals(constant));
	}

}
