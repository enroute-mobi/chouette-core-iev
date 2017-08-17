package mobi.chouette.exchange.validator.checkpoints;

import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.ChouetteIdentifiedObject;

public abstract class GenericValidator<T extends ChouetteIdentifiedObject> implements CheckPointConstant {

	private static final String[] genericCodes = {L3_Generique_1,L3_Generique_2,L3_Generique_3};
	
	public abstract void validate(Context context, T object, ValidateParameters parameters, String transportMode) ;

	public void validate(Context context, T object, ValidateParameters parameters, String transportMode,
			String[] codes) {
		
		// first check generic codes for class
		for (String code : genericCodes) {
			// find checkpoint for code
			// in transportModes
			// else in global
			
			
		}
		
		
		// then check specific codes 
		for (String code : codes) {
			// find checkpoint for code
			// in transportModes
			// else in global
		
			
		}
		
	}
	
	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Generique-1
 	 * <p>
 	 * <b>Variables</b> :  * attribut<br>
	 *  * expression régulière
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  l'attribut de l'objet doit respecter un motif (expression régulière)
 	 * <p>
 	 * <b>Message</b> :  {objet} : l'attribut {nom attribut} à une valeur {valeur} qui ne respecte pas le motif {expression régulière}
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Generique1(Context context, T object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Generique-2
 	 * <p>
 	 * <b>Variables</b> :  * attribut de type numérique<br>
	 *  * valeur minimale (optionnelle)<br>
	 *  * valeur maximale (optionnelle)
 	 * <p>
 	 * <b>Prérequis</b> :  Néant
 	 * <p>
 	 * <b>Prédicat</b> :  la valeur numérique de l'attribut doit rester comprise entre 2 valeurs
 	 * <p>
 	 * <b>Message</b> :  {objet} : l'attribut {nom attribut} à une valeur {valeur} supérieure à la valeur maximale autorisée {max}<br>
	 *   {objet} : l'attribut {nom attribut} à une valeur {valeur} inférieure à la valeur minimale autorisée {min}
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Generique2(Context context, T object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Generique-3
 	 * <p>
 	 * <b>Variables</b> :  * attribut
 	 * <p>
 	 * <b>Prérequis</b> :  Néant
 	 * <p>
 	 * <b>Prédicat</b> :  la valeur de l'attribut doit être unique au sein des objets de la ligne
 	 * <p>
 	 * <b>Message</b> :  {objet} : l'attribut {nom attribut} de {ref X} à une valeur {valeur} en conflit avec {ref Y}
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Generique3(Context context, T object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


}
