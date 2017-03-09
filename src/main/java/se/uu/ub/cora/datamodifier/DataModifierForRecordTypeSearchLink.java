package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifierForRecordTypeSearchLink implements DataModifier {

	RecordStorage recordStorage;
	DataRecordLinkCollector linkCollector;

	@Override
	public void modifyByRecordType(String recordType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRecordStorage(RecordStorage recordStorage) {
		this.recordStorage = recordStorage;

	}

	@Override
	public void setLinkCollector(DataRecordLinkCollector linkCollector) {
		this.linkCollector = linkCollector;

	}

}
