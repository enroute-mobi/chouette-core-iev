package mobi.chouette.exchange;

import mobi.chouette.common.JobData.ACTION;
import mobi.chouette.model.ActionTask;

public class DummyActionTask extends ActionTask  {
	private static final long serialVersionUID = -43627369741783584L;

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return 1L;
	}

	@Override
	public ACTION getAction() {
		// TODO Auto-generated method stub
		return ACTION.importer;
	}

}
