package mobi.chouette.exchange;

import java.util.List;

import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;

/**
 * 
 * give processing elementary commands for line oriented actions <br/>
 * on import process, getLineProcessingCommands should return a command instance for each line to process <br>
 * on export process, getLineProcessingCommands should return a single command instance reusable for each line <br>
 * <ul><li>line should be provided in context on LINE key</li></ul>
 * 
 * @author michel
 *
 */
public interface ProcessingCommands {

	List<Command> getPreProcessingCommands(Context context,boolean withDao);
	List<Command> getLineProcessingCommands(Context context,boolean withDao);
	List<Command> getStopAreaProcessingCommands(Context context,boolean withDao);
	List<Command> getPostProcessingCommands(Context context,boolean withDao);
	List<Command> getDisposeCommands(Context context,boolean withDao);
}
