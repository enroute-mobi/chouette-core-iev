package mobi.chouette.exchange;

import lombok.Data;
import mobi.chouette.common.JobData;

@Data
public class TestJobData implements JobData {

	private Long id;

	private String inputFilename;

	private String outputFilename;

	private JobData.ACTION action;
	
	private String type;
	
	private String referential;
	
	private String pathName;
}
