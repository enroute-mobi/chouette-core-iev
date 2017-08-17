package mobi.chouette.exchange.validator.checkpoints;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.validator.ValidateParameters;

public class ShapeValidator extends GenericValidator<Shape> implements CheckPointConstant {

	private static final String[] codes = {L3_Shape_1,L3_Shape_2,L3_Shape_3};

	@Override
	public void validate(Context context, Shape object, ValidateParameters parameters, String transportMode) {
		super.validate( context,  object,  parameters,transportMode,codes);
	}

 
	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Shape-1
 	 * <p>
 	 * <b>Variables</b> :  * distance maximale en mètre
 	 * <p>
 	 * <b>Prérequis</b> : specific
 	 * <p>
 	 * <b>Prédicat</b> :  Le tracé d'un itinéraire doit passer à proximité des arrêts de celui-ci : la distance entre le ZDEp et son point projeté initial doit être inférieure à une distance maximale (m).
 	 * <p>
 	 * <b>Message</b> :  Tracé {objectId} : le tracé passe trop loin de l'arrêt {nom arrêt} ({identifiant arrêt}) :  {distance} > {distance max} 
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Shape1(Context context, Shape object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Shape-2
 	 * <p>
 	 * <b>Variables</b> :  néant
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  le tracé d'un itinéraire doit être composé de sections entre chaque arrêt de celui-ci
 	 * <p>
 	 * <b>Message</b> :  Tracé {objectId} : le tracé n'est pas défini entre les arrêts {nom arrêt1} ({identifiant arrêt1}) et {nom arrêt2} ({identifiant arrêt2})
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Shape2(Context context, Shape object, CheckpointParameters parameters)
	{
	     // TODO 	
	}


	/** 
 	 * <b>Titre</b> :Validation de l'appartenance de l'arrêt à une Organisation donc à un site de saisie de l'Organisation
 	 * <p>
 	 * <b>R&eacute;ference Redmine</b> : <a target="_blank" href="https://projects.af83.io/issues/2838">Cartes #2838</a>
 	 * <p>
 	 * <b>Code</b> :3-Shape-3
 	 * <p>
 	 * <b>Variables</b> :  note minimale acceptée
 	 * <p>
 	 * <b>Prérequis</b> :  néant
 	 * <p>
 	 * <b>Prédicat</b> :  Les sections de tracé construits en mode RI ne doivent pas s'écarter d'une nouvelle RI sur la BD-TOPO
 	 * <p>
 	 * <b>Message</b> :  Le tracé de l'itinéraire {ObjectId} est en écart avec la voirie sur {nombre sections}/{nombre total de sections} sections.
 	 * <p>
 	 * <b>Criticité</b> :  warning
 	 * <p>
 	 * Note :  
 	 *
 	 * @param context
 	 * @return
 	 */
	private void check3Shape3(Context context, Shape object, CheckpointParameters parameters)
	{
	     // TODO 	
	}

}

