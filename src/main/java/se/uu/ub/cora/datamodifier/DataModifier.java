package se.uu.ub.cora.datamodifier;

import java.util.Collection;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifier {

	private RecordStorage recordStorage;

	public DataModifier(RecordStorage recordStorage) {
		this.recordStorage = recordStorage;
	}

	public void modifyByRecordType(String recordType) {
		Collection<DataGroup> recordList = recordStorage.readList(recordType);
	}

}
