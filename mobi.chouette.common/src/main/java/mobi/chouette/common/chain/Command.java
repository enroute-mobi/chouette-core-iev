package mobi.chouette.common.chain;

import javax.ejb.Local;

import mobi.chouette.common.Context;

@Local
public interface Command {

	boolean execute(Context context) throws Exception;
}
