package mobi.chouette.exchange;

import mobi.chouette.common.JobData.ACTION;
import mobi.chouette.model.ActionTask;

public class DummyActionTask extends ActionTask  {
	private static final long serialVersionUID = -43627369741783584L;

	@Override
	public Long getId() {
		return 1L;
	}

	@Override
	public ACTION getAction() {
		return ACTION.importer;
	}

	@Override
	public String getFormat() {
		return null;
	}

}
