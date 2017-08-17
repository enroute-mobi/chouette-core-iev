package mobi.chouette.exchange.validator.checkpoints;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;
import mobi.chouette.model.RoutingConstraint;

public class RoutingConstraintValidator extends GenericValidator<RoutingConstraint> implements CheckPointConstant {

	private static final String[] codes = {L3_ITL_1,L3_ITL_2,L3_ITL_3};

	@Override
	public void validate(Context context, RoutingConstraint object, ValidateParameters parameters, String transportMode) {
		super.validate( context,  object,  parameters,transportMode,codes);
	}

  
	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-ITL-1
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :   Les arrêts d'une ITL ne doivent pas être désactivés 
 	 * <p>
 	 * <b>Message</b> :   L'ITL {objectId} référence un arrêt (ZDEp) désactivé {nomArrêt} ({idArret}) 
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3ITL1(Context context, RoutingConstraint object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-ITL-2
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Une ITL ne peut pas couvrir l'ensemble des arrêts de l'itinéraire
 	 * <p>
 	 * <b>Message</b> :  L'ITL {objectId} couvre tous les arrêts de l'itinéraire {ObjectId}.
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3ITL2(Context context, RoutingConstraint object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-ITL-3
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Une ITL doit référencer au moins 2 arrêts
 	 * <p>
 	 * <b>Message</b> :  L'ITL {ObjectId} n'a pas suffisament d'arrêts (minimum 2 arrêts requis)
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3ITL3(Context context, RoutingConstraint object, CheckpointParameters parameters)
	{
	     // TODO 	
	}

}

