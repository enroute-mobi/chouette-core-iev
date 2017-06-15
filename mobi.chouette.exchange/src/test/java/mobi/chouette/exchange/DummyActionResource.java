package mobi.chouette.exchange;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.common.JobData.ACTION;
import mobi.chouette.model.ActionResource;

public class DummyActionResource extends ActionResource  {

	private static final long serialVersionUID = -5016923228893616787L;
	
	@Getter
	@Setter
	private Long taskId;
	
	@Getter
	@Setter
	private Long id;

	public DummyActionResource(long l) {
		id = l;
	}

	@Override
	public ACTION getAction() {
		// TODO Auto-generated method stub
		return ACTION.importer;
	}

}
