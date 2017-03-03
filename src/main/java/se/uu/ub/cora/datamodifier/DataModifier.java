package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifier {

	private RecordStorage recordStorage;

	private List<DataGroup> modifiedList = new ArrayList<>();

	public DataModifier(RecordStorage recordStorage) {
		this.recordStorage = recordStorage;
	}

	public void modifyByRecordType(String recordType) {
		Collection<DataGroup> recordList = recordStorage.readList(recordType);
		for (DataGroup dataGroup : recordList) {
			DataGroup childReferences = dataGroup.getFirstGroupWithNameInData("childReferences");
			for (DataGroup childReference : childReferences
					.getAllGroupsWithNameInData("childReference")) {
				DataGroup ref = childReference.getFirstGroupWithNameInData("ref");
				ref.removeFirstChildWithNameInData("linkedRecordType");
				ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
			}
			modifiedList.add(dataGroup);
		}
		DataGroup emptyLinkList = DataGroup.withNameInData("collectedDataLinks");
		for (DataGroup modified : modifiedList) {
			DataGroup recordInfo = modified.getFirstGroupWithNameInData("recordInfo");
			String id = recordInfo.getFirstAtomicValueWithNameInData("id");
			String type = recordInfo.getFirstAtomicValueWithNameInData("type");
			DataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData("dataDivider");
			String dataDivider = dataDividerGroup
					.getFirstAtomicValueWithNameInData("linkedRecordId");

			recordStorage.update(type, id, modified, emptyLinkList, dataDivider);
		}
	}

}
