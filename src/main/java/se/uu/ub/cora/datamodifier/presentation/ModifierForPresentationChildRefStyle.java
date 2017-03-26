package se.uu.ub.cora.datamodifier.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class ModifierForPresentationChildRefStyle implements DataModifier {

	private static final String CHILD_STYLE = "childStyle";
	RecordStorage recordStorage;
	DataRecordLinkCollector linkCollector;
	private String recordType;
	private List<DataGroup> modifiedList = new ArrayList<>();

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
		DataGroup childReferences = dataGroup.getFirstGroupWithNameInData("childReferences");
		for (DataGroup childReference : childReferences
				.getAllGroupsWithNameInData("childReference")) {
			DataGroup refGroup = childReference.getFirstGroupWithNameInData("refGroup");
			moveStyleIfExist(childReference, refGroup, CHILD_STYLE);
			moveStyleIfExist(childReference, refGroup, "textStyle");

			if (childReference.containsChildWithNameInData("refMinGroup")) {
				DataGroup refMinGroup = childReference.getFirstGroupWithNameInData("refMinGroup");
				moveStyleIfExist(childReference, refMinGroup, CHILD_STYLE);
				moveStyleIfExist(childReference, refMinGroup, "textStyle");
			}
		}

	}

	private void moveStyleIfExist(DataGroup childReference, DataGroup refGroup, String style) {
		if (refGroup.containsChildWithNameInData(style)) {
			String childStyleValue = refGroup.getFirstAtomicValueWithNameInData(style);
			refGroup.removeFirstChildWithNameInData(style);
			if (!childReference.containsChildWithNameInData(style)) {
				childReference.addChild(DataAtomic.withNameInDataAndValue(style, childStyleValue));
			}
		}
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
		// String metadataId = getMetadataId();
		// DataGroup collectedLinks = linkCollector.collectLinks(metadataId,
		// modified, type, id);
		DataGroup collectedLinks = DataGroup.withNameInData("collectedlinks");
		String dataDivider = extractDataDivider(recordInfo);
		recordStorage.update(type, id, modified, collectedLinks, dataDivider);
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
