package mobi.chouette.exchange.validator.checkpoints;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.Route;

public class RouteValidator extends GenericValidator<Route> implements CheckPointConstant {

	private static final String[] codes = {L3_Route_1,L3_Route_2,L3_Route_3,L3_Route_4,L3_Route_5,L3_Route_6,L3_Route_7,L3_Route_8,L3_Route_9,L3_Route_10,L3_Route_11};

	@Override
	public void validate(Context context, Route object, ValidateParameters parameters, String transportMode) {
		super.validate( context,  object,  parameters,transportMode,codes);
	}

 
	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-1
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Deux arrêts d'une même ZDL ne peuvent pas se succéder dans un itinéraire
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} dessert successivement les arrêts {nomArrêt1} ({idArrêt1}) et {nomArrêt2} ({idArrêt2}) de la même zone de lieu
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route1(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-2
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  présence d'itinéraire référençant un itinéraire inverse
 	 * <p>
 	 * <b>Prédicat</b> :  Si l'itinéraire référence un itinéraire inverse, celui-ci doit :<br>
	 *   * référencer l'itinéraire inverse<br>
	 *  * avoir un sens opposé à l'itinéraire testé
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} référence un itinéraire retour {objectId} incohérent
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route2(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-3
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  Néant
 	 * <p>
 	 * <b>Prédicat</b> :  Un itinéraire doit avoir au moins une mission
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} n'a pas de mission 
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route3(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-4
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  2 itinéraires ne doivent pas desservir strictement les mêmes arrêts dans le même ordre avec les mêmes critères de monté/descente
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} est identique à l'itinéraire {objectid}
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route4(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-5
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> : présence d'itinéraire référençant un itinéraire inverse 
 	 * <p>
 	 * <b>Prédicat</b> :  Deux itinéraires en aller/retour doivent desservir les mêmes terminus
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} dessert au départ un arrêt de la ZDL {nomZDL1} alors que l'itinéraire inverse dessert à l'arrivée un arrêt de la ZDL {nomZDL2} 
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route5(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-6
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Un itinéraire doit référencer au moins 2 arrêts
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} ne dessert pas assez d'arrêts (minimum 2 requis)
 	 * <p>
 	 * <b>Criticité</b> :  error
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route6(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-7
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Un itinéraire doit posséder au moins une mission
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} n'a aucune mission
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route7(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-8
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Les arrêts de l'itinéraire doivent être desservis par au moins une mission
 	 * <p>
 	 * <b>Message</b> :  l'arrêt {nomArrêt} ({idArrêt}) de l'itinéraire {objectId} n'est desservi par aucune mission
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route8(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-9
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Une mission de l'itinéraire devrait desservir l'ensemble des arrêts de celui-ci
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} n'a aucune mission desservant l'ensemble de ses arrêts
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route9(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-10
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Les arrêts d'un itinéraire ne doivent pas être désactivés 
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} référence un arrêt (ZDEp) désactivé {nomArrêt} ({idArret})  
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route10(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Route-11
 	 * <p>
 	 * <b>Variables</b> :  un ou plusieurs mode(s) de transport
 	 * <p>
 	 * <b>Prérequis</b> :  Le mode de transport de la ligne correspond à celui demandé par le contrôle 
 	 * <p>
 	 * <b>Prédicat</b> :  Un itinéraire doit avoir un tracé
 	 * <p>
 	 * <b>Message</b> :  L'itinéraire {objectId} n'a pas de tracé associé
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Route11(Context context, Route object, CheckpointParameters parameters)
	{
	     // TODO 	
	}

}

