package mobi.chouette.exchange.importer.inserter;

import java.util.Date;

import javax.ejb.Stateless;

import mobi.chouette.common.Context;
import mobi.chouette.model.Footnote;

@Stateless(name = FootnoteInserter.BEAN_NAME)
public class FootnoteInserter implements Inserter<Footnote> {

	public static final String BEAN_NAME = "FootnoteInserter";

	@Override
	public void insert(Context context, Footnote oldValue, Footnote newValue) {

		if (newValue.getCode() != null
				&& !newValue.getCode().equals(oldValue.getCode())) {
			oldValue.setCode(newValue.getCode());
		}

		if (newValue.getKey() != null
				&& !newValue.getKey().equals(oldValue.getKey())) {
			oldValue.setKey(newValue.getKey());
		}

		if (newValue.getLabel() != null
				&& !newValue.getLabel().equals(oldValue.getLabel())) {
			oldValue.setLabel(newValue.getLabel());
		}

		// Updated now anyhow
		oldValue.setUpdatedTime(new Date());
		
	}

}
