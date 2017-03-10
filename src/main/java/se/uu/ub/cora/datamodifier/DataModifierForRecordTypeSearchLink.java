package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifierForRecordTypeSearchLink implements DataModifier {

	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private static final String DATA_DIVIDER = "dataDivider";
	private static final String RECORD_INFO = "recordInfo";
	RecordStorage recordStorage;
	DataRecordLinkCollector linkCollector;
	private List<DataGroup> modifiedList = new ArrayList<>();

	@Override
	public void modifyByRecordType(String recordType) {
		Collection<DataGroup> recordList = recordStorage.readList(recordType);
		for (DataGroup dataGroup : recordList) {
			modifyDataGroup(dataGroup);
			modifiedList.add(dataGroup);
		}
		updateRecords();
	}

	private void modifyDataGroup(DataGroup dataGroup) {
		// searchMetadataId ska tas bort
		dataGroup.removeFirstChildWithNameInData("searchMetadataId");
		// searchPresentationFormId ska tas bort
		dataGroup.removeFirstChildWithNameInData("searchPresentationFormId");
		// search för den här recordTypen ska skapas - searchen ska peka på
		// metadatagruppen autocompleteSearchGroup
		DataGroup recordinfo = dataGroup.getFirstGroupWithNameInData(RECORD_INFO);
		String currentRecordType = recordinfo.getFirstAtomicValueWithNameInData("id");

		DataGroup dataDivider = recordinfo.getFirstGroupWithNameInData(DATA_DIVIDER);
		String dataDividerString = dataDivider.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);

		createSearchForRecordType(currentRecordType, dataDividerString);
		// och motsvarande presentationsgrupp autocompleteSearchPGroup
		// länken till den skapade searchen ska läggas till som länk i
		// recordType
	}

	private void createSearchForRecordType(String currentRecordType, String currentDataDivider) {
		DataGroup search = DataGroup.withNameInData("search");
		DataGroup recordInfo = DataGroup.withNameInData(RECORD_INFO);
		String id = currentRecordType + "Search";
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));

		DataGroup dataDivider = DataGroup.withNameInData(DATA_DIVIDER);
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider
				.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, currentDataDivider));
		recordInfo.addChild(dataDivider);
		search.addChild(recordInfo);

		DataGroup presentation = DataGroup.withNameInData("presentationId");
		presentation.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordType", "presentationGroup"));
		presentation.addChild(
				DataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, "autocompleteSearchPGroup"));
		search.addChild(presentation);
		// DataGroup metadataId = DataGroup.withNameInData("metadataId");
		// metadataId.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType",
		// "metadataGroup"));
		// metadataId.addChild(
		// DataAtomic.withNameInDataAndValue("linkedRecordId",
		// "autocompleteSearchGroup"));

		// search.addChild(metadataId);
		DataGroup linkList = DataGroup.withNameInData("collectedDataLinks");
		recordStorage.create(currentRecordType, id, search, linkList, currentDataDivider);
	}

	@Override
	public void setRecordStorage(RecordStorage recordStorage) {
		this.recordStorage = recordStorage;

	}

	@Override
	public void setLinkCollector(DataRecordLinkCollector linkCollector) {
		this.linkCollector = linkCollector;

	}

	private void updateRecords() {
		for (DataGroup modified : modifiedList) {
			updateModifiedDataGroup(modified);
		}
	}

	private void updateModifiedDataGroup(DataGroup modified) {
		DataGroup recordInfo = modified.getFirstGroupWithNameInData(RECORD_INFO);

		String id = recordInfo.getFirstAtomicValueWithNameInData("id");
		String type = recordInfo.getFirstAtomicValueWithNameInData("type");
		String dataDivider = extractDataDivider(recordInfo);

		DataGroup collectedLinks = linkCollector.collectLinks("metadataGroupGroup", modified, type,
				id);

		recordStorage.update(type, id, modified, collectedLinks, dataDivider);
	}

	private String extractDataDivider(DataGroup recordInfo) {
		DataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData(DATA_DIVIDER);
		return dataDividerGroup.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
	}

}
