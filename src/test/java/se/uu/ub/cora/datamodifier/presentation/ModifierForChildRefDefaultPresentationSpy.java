package se.uu.ub.cora.datamodifier.presentation;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class ModifierForChildRefDefaultPresentationSpy implements DataModifier {
	public List<String> recordTypes = new ArrayList<>();

	@Override
	public void modifyByRecordType(String recordType) {
		this.recordTypes.add(recordType);
	}

	@Override
	public void setRecordStorage(RecordStorage recordStorage) {

	}

	@Override
	public void setLinkCollector(DataRecordLinkCollector linkCollector) {

	}
}
