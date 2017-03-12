package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifierForRecordTypeSearchLink implements DataModifier {

	private static final String SEARCH_STRING = "search";
	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private static final String DATA_DIVIDER = "dataDivider";
	private static final String RECORD_INFO = "recordInfo";
	private static final String LINKED_RECORD_TYPE = "linkedRecordType";
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
		removeOldFields(dataGroup);
		DataGroup recordinfo = dataGroup.getFirstGroupWithNameInData(RECORD_INFO);
		String currentRecordType = recordinfo.getFirstAtomicValueWithNameInData("id");

		String dataDividerString = getDataDividerFromCurrentDataGroup(recordinfo);

		createSearchForRecordType(currentRecordType, dataDividerString);

		DataGroup search = createSearchGroup(currentRecordType);
		dataGroup.addChild(search);
	}

	private void removeOldFields(DataGroup dataGroup) {
		dataGroup.removeFirstChildWithNameInData("searchMetadataId");
		dataGroup.removeFirstChildWithNameInData("searchPresentationFormId");
	}

	private DataGroup createSearchGroup(String currentRecordType) {
		DataGroup search = DataGroup.withNameInData(SEARCH_STRING);
		search.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, SEARCH_STRING));
		search.addChild(
				DataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, currentRecordType + "Search"));
		return search;
	}

	private String getDataDividerFromCurrentDataGroup(DataGroup recordinfo) {
		DataGroup dataDivider = recordinfo.getFirstGroupWithNameInData(DATA_DIVIDER);
		return dataDivider.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
	}

	private void createSearchForRecordType(String currentRecordType, String currentDataDivider) {
		DataGroup search = DataGroup.withNameInData(SEARCH_STRING);
		String id = createRecordInfoWithDataDivider(currentRecordType, currentDataDivider, search);

		createMetadataPartOfSearch(search);

		createPresentationPartOfSearch(search);

		createRecordTypeToSearchInPartOfSearch(currentRecordType, search);

		DataGroup linkList = DataGroup.withNameInData("collectedDataLinks");
		System.out.println(currentRecordType + " " + id);
		recordStorage.create("search", id, search, linkList, currentDataDivider);
	}

	private String createRecordInfoWithDataDivider(String currentRecordType,
			String currentDataDivider, DataGroup search) {
		DataGroup recordInfo = DataGroup.withNameInData(RECORD_INFO);
		String id = currentRecordType + "Search";
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));

		createDataDividerUsingDataDividerStringAndAddToRecordInfo(currentDataDivider, recordInfo);
		search.addChild(recordInfo);
		return id;
	}

	private void createDataDividerUsingDataDividerStringAndAddToRecordInfo(
			String currentDataDivider, DataGroup recordInfo) {
		DataGroup dataDivider = DataGroup.withNameInData(DATA_DIVIDER);
		dataDivider.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, "system"));
		dataDivider
				.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, currentDataDivider));
		recordInfo.addChild(dataDivider);
	}

	private void createMetadataPartOfSearch(DataGroup search) {
		DataGroup metadataId = DataGroup.withNameInData("metadataId");
		metadataId.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, "metadataGroup"));
		metadataId.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordId", "autocompleteSearchGroup"));

		search.addChild(metadataId);
	}

	private void createPresentationPartOfSearch(DataGroup search) {
		DataGroup presentation = DataGroup.withNameInData("presentationId");
		presentation.addChild(
				DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, "presentationGroup"));
		presentation.addChild(
				DataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, "autocompleteSearchPGroup"));
		search.addChild(presentation);
	}

	private void createRecordTypeToSearchInPartOfSearch(String currentRecordType,
			DataGroup search) {
		DataGroup recordTypeToSearchIn = DataGroup.withNameInData("recordTypeToSearchIn");
		recordTypeToSearchIn
				.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, "recordType"));
		recordTypeToSearchIn
				.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, currentRecordType));
		search.addChild(recordTypeToSearchIn);
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

		// DataGroup collectedLinks =
		// linkCollector.collectLinks("recordTypeGroup", modified, type,
		// id);
		DataGroup collectedLinks = DataGroup.withNameInData("collectedDataLinks");

		recordStorage.update(type, id, modified, collectedLinks, dataDivider);
	}

	private String extractDataDivider(DataGroup recordInfo) {
		DataGroup dataDividerGroup = recordInfo.getFirstGroupWithNameInData(DATA_DIVIDER);
		return dataDividerGroup.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
	}

}
