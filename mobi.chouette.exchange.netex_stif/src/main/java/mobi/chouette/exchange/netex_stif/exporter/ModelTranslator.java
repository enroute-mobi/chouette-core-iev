/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobi.chouette.exchange.netex_stif.exporter;

import mobi.chouette.model.ChouetteIdentifiedObject;


/**
 * 
 * @author marc
 */
public class ModelTranslator
{
   public String convertToNMTOKEN(String s)
   {
      return s.replaceAll(" ", "-");
   }

   public String netexMockId(ChouetteIdentifiedObject model, String mock)
   {
      if (model == null)
         return null;
      return model.objectIdPrefix() + ":" + mock + ":" + model.objectIdSuffix();
   }



   public String firstLetterUpcase(String word)
   {
      StringBuilder sb = new StringBuilder(word); // Puts the first caracter
      // upcase
      sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
      return sb.toString();
   }




}
