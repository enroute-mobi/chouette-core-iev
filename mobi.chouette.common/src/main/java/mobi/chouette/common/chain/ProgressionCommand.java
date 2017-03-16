package mobi.chouette.common.chain;

import javax.ejb.Local;

import mobi.chouette.common.Context;

@Local
public interface ProgressionCommand extends Command {

	void initialize(Context context, int stepCount);
	void start(Context context, int stepCount);
	void terminate(Context context, int stepCount);
	void dispose(Context context);
}
