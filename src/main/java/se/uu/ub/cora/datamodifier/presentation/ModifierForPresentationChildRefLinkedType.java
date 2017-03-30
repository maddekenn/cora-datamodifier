package se.uu.ub.cora.datamodifier.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class ModifierForPresentationChildRefLinkedType implements DataModifier {
	private static final String PRESENTATION = "presentation";
	private static final String LINKED_RECORD_TYPE = "linkedRecordType";
	RecordStorage recordStorage;
	DataRecordLinkCollector linkCollector;
	private List<DataGroup> modifiedList = new ArrayList<>();
	private String recordType;

	@Override
	public void modifyByRecordType(String recordType) {
		this.recordType = recordType;
		modifiedList = new ArrayList<>();
		Collection<DataGroup> recordList = recordStorage.readList(recordType);
		for (DataGroup dataGroup : recordList) {
			modifyDataGroup(dataGroup);
			modifiedList.add(dataGroup);
		}
		updateRecords();
	}

	private void modifyDataGroup(DataGroup dataGroup) {
		if (dataGroup.containsChildWithNameInData("childReferences")) {
			modifyChildReferences(dataGroup);
		}
	}

	private void modifyChildReferences(DataGroup dataGroup) {
		DataGroup childReferences = dataGroup.getFirstGroupWithNameInData("childReferences");
		for (DataGroup childReference : childReferences
				.getAllGroupsWithNameInData("childReference")) {
			modifyRefGroupsInChildReference(childReference);
		}
	}

	private void modifyRefGroupsInChildReference(DataGroup childReference) {
		modifyPresentationChildReference(childReference.getFirstGroupWithNameInData("refGroup"));
		if (childReference.containsChildWithNameInData("refMinGroup")) {
			modifyPresentationChildReference(
					childReference.getFirstGroupWithNameInData("refMinGroup"));

		}
	}

	private void modifyPresentationChildReference(DataGroup refGroup) {
		DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");
		String linkedRecordType = ref.getFirstAtomicValueWithNameInData(LINKED_RECORD_TYPE);
		if (linkedRecordType.startsWith(PRESENTATION)) {
			modifyChildReference(ref);
		}
	}

	private void modifyChildReference(DataGroup ref) {
		removeAndAddLinkedRecordType(ref);
		removeAndAddAttribute(ref);
	}

	private void removeAndAddLinkedRecordType(DataGroup ref) {
		ref.removeFirstChildWithNameInData(LINKED_RECORD_TYPE);
		ref.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, PRESENTATION));
	}

	private void removeAndAddAttribute(DataGroup ref) {
		ref.getAttributes().remove("type");
		ref.addAttributeByIdWithValue("type", PRESENTATION);
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
		String metadataId = getMetadataId();
		DataGroup collectedLinks = linkCollector.collectLinks(metadataId, modified, type, id);

		String dataDivider = extractDataDivider(recordInfo);
		recordStorage.update(type, id, modified, collectedLinks, dataDivider);
	}

	private String getMetadataId() {
		DataGroup recordTypeDataGroup = recordStorage.read("recordType", recordType);
		DataGroup metadataIdGroup = recordTypeDataGroup.getFirstGroupWithNameInData("metadataId");
		return metadataIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
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
