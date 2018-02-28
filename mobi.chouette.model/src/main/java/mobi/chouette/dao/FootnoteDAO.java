package mobi.chouette.dao;

import java.util.List;

import mobi.chouette.model.Footnote;

public interface FootnoteDAO extends GenericDAO<Footnote> {
	public List<Footnote> findByLineId(final long lineId);

}
