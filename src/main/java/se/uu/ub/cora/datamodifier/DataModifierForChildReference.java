package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifierForChildReference implements DataModifier {

	RecordStorage recordStorage;
	DataRecordLinkCollector linkCollector;
	private List<DataGroup> modifiedList = new ArrayList<>();

	public DataModifierForChildReference() {
	}

	@Override
	public void modifyByRecordType(String recordType) {
		DataGroup emptyFilter = DataGroup.withNameInData("filter");
		Collection<DataGroup> recordList = recordStorage.readList(recordType, emptyFilter);
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
		DataGroup typeGroup = recordInfo.getFirstGroupWithNameInData("type");
		String type = typeGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
		String dataDivider = extractDataDivider(recordInfo);

		DataGroup collectedLinks = linkCollector.collectLinks("metadataGroupGroup", modified, type,
				id);
		// emptyCollectedData was added after the data already had been modified
		DataGroup emptyCollectedData = DataGroup.withNameInData("collectedData");
		recordStorage.update(type, id, modified, emptyCollectedData, collectedLinks, dataDivider);
	}

	private String extractDataDivider(DataGroup recordInfo) {
		DataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData("dataDivider");
		return dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
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
