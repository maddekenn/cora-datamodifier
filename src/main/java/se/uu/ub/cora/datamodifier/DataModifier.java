package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifier {

	private RecordStorage recordStorage;

	private List<DataGroup> modifiedList = new ArrayList<>();

	private DataRecordLinkCollector linkCollector;

	public DataModifier(RecordStorage recordStorage, DataRecordLinkCollector linkCollector) {
		this.recordStorage = recordStorage;
		this.linkCollector = linkCollector;
	}

	public void modifyByRecordType(String recordType) {
		Collection<DataGroup> recordList = recordStorage.readList(recordType);
		for (DataGroup dataGroup : recordList) {
			modifyDataGroup(dataGroup);
			modifiedList.add(dataGroup);
		}
		updateRecords();
	}

	private void modifyDataGroup(DataGroup dataGroup) {
		DataGroup childReferences = dataGroup.getFirstGroupWithNameInData("childReferences");
		for (DataGroup childReference : childReferences
				.getAllGroupsWithNameInData("childReference")) {
			modifyChildReference(childReference);
		}
	}

	private void modifyChildReference(DataGroup childReference) {
		DataGroup ref = childReference.getFirstGroupWithNameInData("ref");
		ref.removeFirstChildWithNameInData("linkedRecordType");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadata"));
		ref.getAttributes().remove("type");
	}

	private void updateRecords() {
		for (DataGroup modified : modifiedList) {
			updateModifiedDataGroup(modified);
		}
	}

	private void updateModifiedDataGroup(DataGroup modified) {
		DataGroup recordInfo = modified.getFirstGroupWithNameInData("recordInfo");

		String id = recordInfo.getFirstAtomicValueWithNameInData("id");
		String type = recordInfo.getFirstAtomicValueWithNameInData("type");
		String dataDivider = extractDataDivider(recordInfo);

		DataGroup collectedLinks = linkCollector.collectLinks("metadataGroupGroup", modified, type,
				id);

		recordStorage.update(type, id, modified, collectedLinks, dataDivider);
	}

	private String extractDataDivider(DataGroup recordInfo) {
		DataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData("dataDivider");
		return dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

}
