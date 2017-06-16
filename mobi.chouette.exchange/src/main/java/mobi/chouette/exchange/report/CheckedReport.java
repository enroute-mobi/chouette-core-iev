package mobi.chouette.exchange.report;

import java.util.List;

public interface CheckedReport {

	List<Integer> getCheckPointErrorKeys();
	
	List<Integer> getCheckPointWarningKeys();

	int getCheckPointErrorCount();

	int getCheckPointWarningCount();

}
