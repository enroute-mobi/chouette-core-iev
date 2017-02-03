/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */

package mobi.chouette.exchange.neptune.exporter.producer;

import mobi.chouette.model.ChouetteIdentifiedObject;

import org.trident.schema.trident.TridentObjectType;

/**
 * @author michel
 * 
 */
public interface IJaxbNeptuneProducer<T extends TridentObjectType, U extends ChouetteIdentifiedObject>
{
   T produce(U o, boolean addExtension);
}
