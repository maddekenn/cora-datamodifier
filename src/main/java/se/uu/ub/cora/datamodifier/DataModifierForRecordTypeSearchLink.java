package se.uu.ub.cora.datamodifier;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

public class DataModifierForRecordTypeSearchLink extends DataModifierForRecordType {

	private static final String SEARCH_STRING = "search";
	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private static final String DATA_DIVIDER = "dataDivider";
	private static final String RECORD_INFO = "recordInfo";
	private static final String LINKED_RECORD_TYPE = "linkedRecordType";

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		removeOldFields(dataGroup);
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData(RECORD_INFO);
		String currentRecordType = recordInfo.getFirstAtomicValueWithNameInData("id");

		String dataDividerString = getDataDividerFromCurrentDataGroup(recordInfo);

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

		DataGroup linkList = linkCollector.collectLinks("searchGroup", search, SEARCH_STRING, id);
		recordStorage.create("search", id, search, linkList, currentDataDivider);
	}

	private String createRecordInfoWithDataDivider(String currentRecordType,
			String currentDataDivider, DataGroup search) {
		DataGroup recordInfo = DataGroup.withNameInData(RECORD_INFO);
		String id = currentRecordType + "Search";
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("type", SEARCH_STRING));
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("createdBy", "141414"));

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
}
