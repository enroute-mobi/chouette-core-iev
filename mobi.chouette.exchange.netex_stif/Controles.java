public class Controles {
	class Context {
	}

	/**
	 * <b>Titre</b> :[Netex] Conformité du zip importé
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2110">Cartes #2110</a>
	 * <p>
	 * <b>Code</b> : 1-NeTExStif-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : l'archive importée doit respecter l'organisation dossiers et en fichiers définie dans la NT44
	 * <p>
	 * <b>Message</b> : Le fichier importé n'est pas une archive ZIP valide. L'archive importée ne respecte pas
	 * l'organisation attendue : fichier calendriers.xml manquant. L'archive importée ne respecte pas l'organisation
	 * attendue : aucun fichier d'offre trouvé
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check1NeTExStif1(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 1-NeTExStif-1 : [Netex] Conformité du zip importé
		boolean result = true;
		return result;
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Conformité XML
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2111">Cartes #2111</a>
	 * <p>
	 * <b>Code</b> : 1-NeTExStif-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : les fichiers xml doivent respecter la syntaxe XML du W3C
	 * <p>
	 * <b>Message</b> : Le fichier {nomFichier} ne respecte pas la syntaxe XML : code W3C {code erreur} rencontré
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check1NeTExStif2(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 1-NeTExStif-2 : [Netex] Conformité XML
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Netex] Conformité XSD NeTEx
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2112">Cartes #2112</a>
	 * <p>
	 * <b>Code</b> : 1-NeTExStif-3
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : les fichiers xml doivent respecter la XSD NeTEx
	 * <p>
	 * <b>Message</b> : Le fichier {nomFichier} ne respecte pas la XSD NeTEx : code W3C {code erreur} rencontré
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check1NeTExStif3(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 1-NeTExStif-3 : [Netex] Conformité XSD NeTEx
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Génériques] Contrôle du contenu selon un pattern
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2192">Cartes #2192</a>
	 * <p>
	 * <b>Code</b> : 3-Générique-1
	 * <p>
	 * <b>Variables</b> : * attribut. * expression régulière
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : l'attribut de l'objet doit respecter un motif (expression régulière)
	 * <p>
	 * <b>Message</b> : {objet} : l'attribut {nom attribut} à une valeur {valeur} qui ne respecte pas le motif
	 * {expression régulière}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Générique1(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Générique-1 : [Génériques] Contrôle du contenu selon un pattern
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Génériques] Valeur min
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2193">Cartes #2193</a>
	 * <p>
	 * <b>Code</b> : 3-Générique-2
	 * <p>
	 * <b>Variables</b> : * attribut de type numérique. * valeur minimale (optionnelle). * valeur maximale (optionnelle)
	 * <p>
	 * <b>Prérequis</b> : Néant
	 * <p>
	 * <b>Prédicat</b> : la valeur numérique de l'attribut doit rester comprise entre 2 valeurs
	 * <p>
	 * <b>Message</b> : {objet} : l'attribut {nom attribut} à une valeur {valeur} supérieure à la valeur maximale
	 * autorisée {max}. {objet} : l'attribut {nom attribut} à une valeur {valeur} inférieure à la valeur minimale
	 * autorisée {min}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Générique2(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Générique-2 : [Génériques] Valeur min
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Génériques] Unicité d'un attribut d'un objet dans une ligne
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2194">Cartes #2194</a>
	 * <p>
	 * <b>Code</b> : 3-Générique-3
	 * <p>
	 * <b>Variables</b> : * attribut
	 * <p>
	 * <b>Prérequis</b> : Néant
	 * <p>
	 * <b>Prédicat</b> : la valeur de l'attribut doit être unique au sein des objets de la ligne
	 * <p>
	 * <b>Message</b> : {objet} : l'attribut {nom attribut} de {ref X} à une valeur {valeur} en conflit avec {ref Y}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Générique3(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Générique-3 : [Génériques] Unicité d'un attribut d'un objet dans une
		// ligne
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[ITL] ITL & arret désactivé
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2109">Cartes #2109</a>
	 * <p>
	 * <b>Code</b> : 3-ITL-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les arrêts d'une ITL ne doivent pas être désactivés
	 * <p>
	 * <b>Message</b> : L'ITL {objectId} référence un arrêt (ZDEp) désactivé {nomArrêt} ({idArret})
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3ITL1(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-ITL-1 : [ITL] ITL & arret désactivé
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[ITL] Couverture de l'itinéraire
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2510">Cartes #2510</a>
	 * <p>
	 * <b>Code</b> : 3-ITL-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une ITL ne peut pas couvrir l'ensemble des arrêts de l'itinéraire
	 * <p>
	 * <b>Message</b> : L'ITL {objectId} couvre tous les arrêts de l'itinéraire {ObjectId}.
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3ITL2(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-ITL-2 : [ITL] Couverture de l'itinéraire
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[ITL] Définition minimale d'une ITL
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2511">Cartes #2511</a>
	 * <p>
	 * <b>Code</b> : 3-ITL-3
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une ITL doit référencer au moins 2 arrêts
	 * <p>
	 * <b>Message</b> : L'ITL {ObjectId} n'a pas suffisament d'arrêts (minimum 2 arrêts requis)
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3ITL3(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-ITL-3 : [ITL] Définition minimale d'une ITL
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Mission] Doublon de missions dans une ligne
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2102">Cartes #2102</a>
	 * <p>
	 * <b>Code</b> : 3-JourneyPattern-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Deux missions de la même ligne ne doivent pas desservir les mêmes arrêts dans le même ordre
	 * <p>
	 * <b>Message</b> : La mission {objectId} est identique à la mission {objectId}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3JourneyPattern1(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-JourneyPattern-1 : [Mission] Doublon de missions dans une ligne
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Mission] Présence de courses
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2625">Cartes #2625</a>
	 * <p>
	 * <b>Code</b> : 3-JourneyPattern-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une mission doit avoir au moins une course
	 * <p>
	 * <b>Message</b> : La mission {objectId} n'a pas de course
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3JourneyPattern2(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-JourneyPattern-2 : [Mission] Présence de courses
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Ligne] Appariement des itinéraires
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2121">Cartes #2121</a>
	 * <p>
	 * <b>Code</b> : 3-Line-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : Ligne disposant de plusieurs itinéraires
	 * <p>
	 * <b>Prédicat</b> : Les itinéraires d'une ligne doivent être associés en aller/retour
	 * <p>
	 * <b>Message</b> : Sur la ligne {nomLigne} ({objectId}), aucun itinéraire n'a d'itinéraire inverse
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Line1(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Line-1 : [Ligne] Appariement des itinéraires
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Deux arrêts d'une même ZDL ne peuvent pas se succéder dans un itinéraire
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2092">Cartes #2092</a>
	 * <p>
	 * <b>Code</b> : 3-Route-1
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Deux arrêts d'une même ZDL ne peuvent pas se succéder dans un itinéraire
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} dessert successivement les arrêts {nomArrêt1} ({idArrêt1}) et
	 * {nomArrêt2} ({idArrêt2}) de la même zone de lieu
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route1(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-1 : [Itinéraire] Deux arrêts d'une même ZDL ne peuvent pas se
		// succéder dans un itinéraire
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Vérification de l'itinéraire inverse
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2093">Cartes #2093</a>
	 * <p>
	 * <b>Code</b> : 3-Route-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : présence d'itinéraire référençant un itinéraire inverse
	 * <p>
	 * <b>Prédicat</b> : Si l'itinéraire référence un itinéraire inverse, celui-ci doit :. * référencer l'itinéraire
	 * inverse. * avoir un sens opposé à l'itinéraire testé
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} référence un itinéraire retour {objectId} incohérent
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route2(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-2 : [Itinéraire] Vérification de l'itinéraire inverse
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Présence de missions
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2624">Cartes #2624</a>
	 * <p>
	 * <b>Code</b> : 3-Route-3
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : Néant
	 * <p>
	 * <b>Prédicat</b> : Un itinéraire doit avoir au moins une mission
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} n'a pas de mission
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route3(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-3 : [Itinéraire] Présence de missions
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Détection de double définition d'itinéraire
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2095">Cartes #2095</a>
	 * <p>
	 * <b>Code</b> : 3-Route-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : 2 itinéraires ne doivent pas desservir strictement les mêmes arrêts dans le même ordre avec les
	 * mêmes critères de monté/descente
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} est identique à l'itinéraire {objectid}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route4(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-4 : [Itinéraire] Détection de double définition d'itinéraire
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Vérification des terminus de l'itinéraire inverse
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2120">Cartes #2120</a>
	 * <p>
	 * <b>Code</b> : 3-Route-5
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : présence d'itinéraire référençant un itinéraire inverse
	 * <p>
	 * <b>Prédicat</b> : Deux itinéraires en aller/retour doivent desservir les mêmes terminus
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} dessert au départ un arrêt de la ZDL {nomZDL1} alors que l'itinéraire
	 * inverse dessert à l'arrivée un arrêt de la ZDL {nomZDL2}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * Note : le test à l'arrivée est équivalent au test au départ sur l'itinéraire inverse, il est inutile de le faire
	 * 2 fois.
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route5(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-5 : [Itinéraire] Vérification des terminus de l'itinéraire
		// inverse
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Un itinéraire doit contenir au moins 2 arrêts
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2097">Cartes #2097</a>
	 * <p>
	 * <b>Code</b> : 3-Route-6
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Un itinéraire doit référencer au moins 2 arrêts
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} ne dessert pas assez d'arrêts (minimum 2 requis)
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route6(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-6 : [Itinéraire] Un itinéraire doit contenir au moins 2 arrêts
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Un itinéraire doit contenir au moins 1 mission
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2098">Cartes #2098</a>
	 * <p>
	 * <b>Code</b> : 3-Route-7
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Un itinéraire doit posséder au moins une mission
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} n'a aucune mission
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route7(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-7 : [Itinéraire] Un itinéraire doit contenir au moins 1 mission
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Utilisation des arrêts par les missions
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2099">Cartes #2099</a>
	 * <p>
	 * <b>Code</b> : 3-Route-8
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les arrêts de l'itinéraire doivent être desservis par au moins une mission
	 * <p>
	 * <b>Message</b> : l'arrêt {nomArrêt} ({idArrêt}) de l'itinéraire {objectId} n'est desservi par aucune mission
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route8(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-8 : [Itinéraire] Utilisation des arrêts par les missions
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Existence d'une mission passant par tous les arrêts de l'itinéraire
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2100">Cartes #2100</a>
	 * <p>
	 * <b>Code</b> : 3-Route-9
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une mission de l'itinéraire devrait desservir l'ensemble des arrêts de celui-ci
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} n'a aucune mission desservant l'ensemble de ses arrêts
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route9(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-9 : [Itinéraire] Existence d'une mission passant par tous les
		// arrêts de l'itinéraire
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Itinéraire & arrêt désactivé
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2101">Cartes #2101</a>
	 * <p>
	 * <b>Code</b> : 3-Route-10
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les arrêts d'un itinéraire ne doivent pas être désactivés
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} référence un arrêt (ZDEp) désactivé {nomArrêt} ({idArret})
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route10(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-10 : [Itinéraire] Itinéraire & arrêt désactivé
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Itinéraire] Présence de tracé
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2419">Cartes #2419</a>
	 * <p>
	 * <b>Code</b> : 3-Route-11
	 * <p>
	 * <b>Variables</b> : un ou plusieurs mode(s) de transport
	 * <p>
	 * <b>Prérequis</b> : Le mode de transport de la ligne correspond à celui demandé par le contrôle
	 * <p>
	 * <b>Prédicat</b> : Un itinéraire doit avoir un tracé
	 * <p>
	 * <b>Message</b> : L'itinéraire {objectId} n'a pas de tracé associé
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Route11(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Route-11 : [Itinéraire] Présence de tracé
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Tracé] Proximité d'un tracé avec l'itinéraire associé
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2421">Cartes #2421</a>
	 * <p>
	 * <b>Code</b> : 3-Shape-1
	 * <p>
	 * <b>Variables</b> : * distance maximale en mètre
	 * <p>
	 * <b>Prérequis</b> : mode de transport
	 * <p>
	 * <b>Prédicat</b> : Le tracé d'un itinéraire doit passer à proximité des arrêts de celui-ci : la distance entre le
	 * ZDEp et son point projeté initial doit être inférieure à une distance maximale (m).
	 * <p>
	 * <b>Message</b> : Tracé {objectId} : le tracé passe trop loin de l'arrêt {nom arrêt} ({identifiant arrêt}) :
	 * {distance} > {distance max}
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Shape1(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Shape-1 : [Tracé] Proximité d'un tracé avec l'itinéraire associé
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Tracé] Cohérence d'un tracé avec l'itinéraire associé
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2562">Cartes #2562</a>
	 * <p>
	 * <b>Code</b> : 3-Shape-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : le tracé d'un itinéraire doit être composé de sections entre chaque arrêt de celui-ci
	 * <p>
	 * <b>Message</b> : Tracé {objectId} : le tracé n'est pas défini entre les arrêts {nom arrêt1} ({identifiant
	 * arrêt1}) et {nom arrêt2} ({identifiant arrêt2})
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Shape2(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Shape-2 : [Tracé] Cohérence d'un tracé avec l'itinéraire associé
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Tracés] Impact lors de la mise à jour de la BD-TOPO
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2565">Cartes #2565</a>
	 * <p>
	 * <b>Code</b> : 3-Shape-3
	 * <p>
	 * <b>Variables</b> : note minimale acceptée
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les sections de tracé construits en mode RI ne doivent pas s'écarter d'une nouvelle RI sur la
	 * BD-TOPO
	 * <p>
	 * <b>Message</b> : Le tracé de l'itinéraire {ObjectId} est en écart avec la voirie sur {nombre sections}/{nombre
	 * total de sections} sections.
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3Shape3(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-Shape-3 : [Tracés] Impact lors de la mise à jour de la BD-TOPO
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Course] La durée d'attente à un arrêt ne doit pas être trop grande
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2103">Cartes #2103</a>
	 * <p>
	 * <b>Code</b> : 3-VehicleJourney-1
	 * <p>
	 * <b>Variables</b> : * durée d'attente maximale à un arrêt en minutes, .
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : La durée d?attente à un arrêt ne doit pas être trop grande
	 * <p>
	 * <b>Message</b> : Sur la course {ObjectId}, le temps d'attente {valeur} à l'arrêt {nomArrêt} ({objectId}) est
	 * supérieur au seuil toléré ({tempsMax})
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3VehicleJourney1(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-VehicleJourney-1 : [Course] La durée d'attente à un arrêt ne doit pas
		// être trop grande
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Course] La vitesse entre deux arrêts doit être dans une fourchette paramétrable
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2104">Cartes #2104</a>
	 * <p>
	 * <b>Code</b> : 3-VehicleJourney-2
	 * <p>
	 * <b>Variables</b> : * vitesse minimale (km/h), . * vitesse maximale (km/h),.
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : La vitesse entre deux arrêts doit être dans une fourchette paramétrable
	 * <p>
	 * <b>Message</b> : Sur la course {ObjectId}, la vitesse calculée {valeur} entre les arrêts {nomArrêt1} ({objectId})
	 * et {nomArrêt2} ({objectId}) est supérieur au seuil toléré ({vitesseMax}). Sur la course {ObjectId}, la vitesse
	 * calculée {valeur} entre les arrêts {nomArrêt1} ({objectId}) et {nomArrêt2} ({objectId}) est inférieure au seuil
	 * toléré ({vitesseMin})
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3VehicleJourney2(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-VehicleJourney-2 : [Course] La vitesse entre deux arrêts doit être
		// dans une fourchette paramétrable
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Course] Les vitesses entre 2 arrêts doivent être similaires pour toutes les courses d'une même
	 * mission
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2105">Cartes #2105</a>
	 * <p>
	 * <b>Code</b> : 3-VehicleJourney-3
	 * <p>
	 * <b>Variables</b> : * écart maximal en minutes
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les temps de parcours entre 2 arrêts successifs doivent être similaires pour toutes les courses
	 * d?une même mission
	 * <p>
	 * <b>Message</b> : Le temps de parcours sur la course {objectId} entre les arrêts {nomArrêt1} ({objectId}) et
	 * {nomArrêt2} ({objectId}) s'écarte de {écart} du temps moyen constaté sur la mission
	 * <p>
	 * <b>Criticité</b> : warning
	 * <p>
	 * Note : Le calcul se fait à l'aide d'un temps de parcours moyen calculé sur toutes les courses de la mission puis
	 * d'une mesure de l'écart entre les temps de parcours de chaque course avec ce temps moyen
	 *
	 * @param context
	 * @return
	 */
	public boolean check3VehicleJourney3(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-VehicleJourney-3 : [Course] Les vitesses entre 2 arrêts doivent être
		// similaires pour toutes les courses d'une même mission
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Course] Une course doit avoir au moins un calendrier d'application
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2106">Cartes #2106</a>
	 * <p>
	 * <b>Code</b> : 3-VehicleJourney-4
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Une course doit avoir au moins un calendrier d?application
	 * <p>
	 * <b>Message</b> : La course {objecId} n'a pas de calendrier d'application
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3VehicleJourney4(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-VehicleJourney-4 : [Course] Une course doit avoir au moins un
		// calendrier d'application
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :[Course] Chronologie croissante des horaires
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2107">Cartes #2107</a>
	 * <p>
	 * <b>Code</b> : 3-VehicleJourney-5
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : L'horaire d'arrivée à un arrêt doit être antérieur à l'horaire de départ de cet arrêt ET les
	 * horaires de départ aux arrêts doivent être dans l'ordre chronologique croissant.
	 * <p>
	 * <b>Message</b> : La course {objectId} a un horaire d'arrivé {hh:mm} supérieur à l'horaire de départ {hh:mm} à
	 * l'arrêt {nomArrêt} ({objectId}) . La course {objectId} a un horaire de départ {hh:mm} à l'arrêt {nomArrêt}
	 * ({objectId}) supérieur à l'horaire d'arrivé {hh:mm} à l'arrêt suivant
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check3VehicleJourney5(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 3-VehicleJourney-5 : [Course] Chronologie croissante des horaires
		boolean result = true;
		return result;
	}

	/**
	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de
	 * l'Organisation
	 * <p>
	 * <b>Référence Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
	 * <p>
	 * <b>Code</b> :2-NeTExSTIF-PassengerStopAssignment-2
	 * <p>
	 * <b>Variables</b> : néant
	 * <p>
	 * <b>Prérequis</b> : néant
	 * <p>
	 * <b>Prédicat</b> : Les arrêts utilisés dans une offre doivent faire partie de la liste des arrêts autorisés pour
	 * l'organisation (i.e site de saisie)
	 * <p>
	 * <b>Message</b> : L'arrêt {objectId} ne fait pas partie des arrêts disponibles pour votre organisation.
	 * <p>
	 * <b>Criticité</b> : error
	 * <p>
	 * 
	 *
	 * @param context
	 * @return
	 */
	public boolean check2NeTExSTIFPassengerStopAssignment2(Context context, int lineNumber, int columnNumber) {
		// TODO : [STIF] Implementation Controle 2-NeTExSTIF-PassengerStopAssignment-2 : Validation de l'appartenance de
		// l'arrêt à une Organisation donc à un site de saisie de l'Organisation
		boolean result = true;
		return result;
	}

}