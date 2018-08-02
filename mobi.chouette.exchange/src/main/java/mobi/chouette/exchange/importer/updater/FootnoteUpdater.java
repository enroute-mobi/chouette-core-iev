package mobi.chouette.exchange.importer.updater;

import java.util.Date;

import javax.ejb.Stateless;

import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.model.Footnote;
import mobi.chouette.model.util.ChecksumUtil;
import lombok.extern.log4j.Log4j;

@Log4j
@Stateless(name = FootnoteUpdater.BEAN_NAME)
public class FootnoteUpdater implements Updater<Footnote> {

	public static final String BEAN_NAME = "FootnoteUpdater";

	@Override
	public void update(Context context, Footnote oldValue, Footnote newValue) {
		
		if (newValue.getLineId() != null && !newValue.getLineId().equals(oldValue.getLineId())) {
			oldValue.setLineId(newValue.getLineId());
		}
		
		if (newValue.getCode() != null && !newValue.getCode().equals(oldValue.getCode())) {
			oldValue.setCode(newValue.getCode());
		}

		if (newValue.getKey() != null && !newValue.getKey().equals(oldValue.getKey())) {
			oldValue.setKey(newValue.getKey());
		}

		if (newValue.getLabel() != null && !newValue.getLabel().equals(oldValue.getLabel())) {
			oldValue.setLabel(newValue.getLabel());
		}
		if (newValue.getDataSourceRef() != null && !newValue.getDataSourceRef().equals(oldValue.getDataSourceRef())) {
			oldValue.setDataSourceRef(newValue.getDataSourceRef());
		}

		// Updated now anyhow
		oldValue.setUpdatedTime(new Date());
		ChecksumUtil.checksum(context, oldValue);
		log.info(Color.LIGHT_GREEN +"FootnoteUpdater : oldValue :"+oldValue.toString()+ Color.NORMAL);
	}

}
