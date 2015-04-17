package mobi.chouette.model.util;

import com.google.common.collect.Iterables;

import mobi.chouette.model.api.Job;
import mobi.chouette.model.api.Link;
import com.google.common.base.Predicate;

public class JobUtil {
	
	public static void updateLink(Job job, Link link)
	{
		final String rel = link.getRel();
		Iterables.removeIf(job.getLinks(), new Predicate<Link>() {
			@Override
			public boolean apply(Link link) {
				return link.getRel().equals(rel)  ;
			}		});
		job.getLinks().add(link);
	}

}
