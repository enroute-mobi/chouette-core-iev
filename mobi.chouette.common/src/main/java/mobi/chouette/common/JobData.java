package mobi.chouette.common;

public interface JobData {

	public enum ACTION { 
		importer,exporter,validator
	}
	
	Long getId();
	String getInputFilename();
	void setInputFilename(String filename); 
	String getOutputFilename();
	void setOutputFilename(String filename); 
	String getReferential();
	ACTION getAction();
	String getType();
	String getPathName();

}
