package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.data.Data;
import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordTypePGroupsModifier implements DataModifier {
	protected RecordStorage recordStorage;
	protected DataRecordLinkCollector linkCollector;

	@Override
	public void modifyByRecordType(String recordType) {
		recordStorage.read("recordType", recordType);
		recordStorage.read("presentationGroup", recordType + "PGroup");
		DataGroup pGroup = DataGroup.withNameInData("");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", recordType+"PGroup"));
		pGroup.addChild(recordInfo);

//		recordStorage.create();
	}

	@Override
	public void setRecordStorage(RecordStorage recordStorage) {
		this.recordStorage = recordStorage;

	}

	@Override
	public void setLinkCollector(DataRecordLinkCollector linkCollector) {
		this.linkCollector = linkCollector;

	}

	public RecordStorage getRecordStorage() {
		return recordStorage;
	}

	public DataRecordLinkCollector getLinkCollector() {
		return linkCollector;
	}

}
