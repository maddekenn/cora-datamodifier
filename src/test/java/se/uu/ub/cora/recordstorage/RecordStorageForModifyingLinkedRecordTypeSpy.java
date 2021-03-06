package se.uu.ub.cora.recordstorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataCreator;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageForModifyingLinkedRecordTypeSpy implements RecordStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	public List<DataGroup> createdData = new ArrayList<>();
	public List<String> createdType = new ArrayList<>();
	public List<String> tsUpdates = new ArrayList<>();
	public List<String> updatedBys = new ArrayList<>();
	public List<String> updatedByUserTypes = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		if ("metadataCollectionItem".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataCollectionItem",
					"metadataCollectionItemGroup");
		}
		if ("metadataItemCollection".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataItemCollection",
					"metadataItemCollectionGroup");
		}
		if ("recordType".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("recordType", "recordTypeGroup");
		}
		if ("search".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("search", "searchGroup");
		}
		if ("recordTypeForChangingUpdateStructure".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId(
					"recordTypeForChangingUpdateStructure",
					"recordTypeForChangingUpdateStructureGroup");
		}
		return null;
	}

	@Override
	public void create(String s, String s1, DataGroup dataGroup, DataGroup collectedTerms,
			DataGroup dataGroup1, String s2) {

	}

	@Override
	public void deleteByTypeAndId(String s, String s1) {

	}

	@Override
	public boolean linksExistForRecord(String s, String s1) {
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup record, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		modifiedDataGroupsSentToUpdate.add(record);
	}

	@Override
	public Collection<DataGroup> readList(String type, DataGroup filter) {
		List<DataGroup> recordList = new ArrayList<>();
		if ("metadataCollectionItem".equals(type)) {
			DataGroup collectionItem = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("someItem", "metadata",
							"metadataCollectionItem", "testSystem");
			// type should be atomic here since this is used for the code that
			// tests changing atomic data to link
			DataGroup recordInfo = collectionItem.getFirstGroupWithNameInData("recordInfo");
			recordInfo.removeFirstChildWithNameInData("type");
			recordInfo
					.addChild(DataAtomic.withNameInDataAndValue("type", "metadataCollectionItem"));

			collectionItem.addChild(DataAtomic.withNameInDataAndValue("nameInData", "some"));

			DataGroup textId = createTextGroupWithNameInDataLinkedRecordTypeAndLInkedRecordId(
					"textId", "coraText", "someText");
			collectionItem.addChild(textId);
			DataGroup defTextId = createTextGroupWithNameInDataLinkedRecordTypeAndLInkedRecordId(
					"defTextId", "coraText", "someDefText");
			collectionItem.addChild(defTextId);
			recordList.add(collectionItem);

		}
		if ("metadataItemCollection".equals(type)) {
			DataGroup itemCollection = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("modeCollection",
							"metadata", "metadataItemCollection", "testSystem");
			itemCollection.addChild(DataAtomic.withNameInDataAndValue("nameInData", "mode"));

			DataGroup textId = createTextGroupWithNameInDataLinkedRecordTypeAndLInkedRecordId(
					"textId", "coraText", "someCollectionText");
			itemCollection.addChild(textId);
			DataGroup defTextId = createTextGroupWithNameInDataLinkedRecordTypeAndLInkedRecordId(
					"defTextId", "coraText", "someCollectionDefText");
			itemCollection.addChild(defTextId);

			DataGroup itemReferences = DataGroup.withNameInData("collectionitemReferences");
			DataGroup ref = DataGroup.withNameInData("ref");
			ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType",
					"metadataCollectionItem"));
			ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "inputItem"));
			itemReferences.addChild(ref);
			itemCollection.addChild(itemReferences);
			recordList.add(itemCollection);

		}

		if ("recordType".equals(type)) {
			DataGroup recordType = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("book", "recordType",
							"recordType", "testSystem");
			recordType.addChild(DataAtomic.withNameInDataAndValue("abstract", "false"));

			DataGroup textId = createTextGroupWithNameInDataLinkedRecordTypeAndLInkedRecordId(
					"textId", "coraText", "someRecordTypeText");
			recordType.addChild(textId);
			DataGroup defTextId = createTextGroupWithNameInDataLinkedRecordTypeAndLInkedRecordId(
					"defTextId", "coraText", "someRecordTypeDefText");
			recordType.addChild(defTextId);

			DataGroup metadataId = DataGroup.withNameInData("metadataId");
			metadataId.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
			metadataId.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "bookGroup"));
			recordType.addChild(metadataId);
			recordList.add(recordType);

		}
		if ("search".equals(type)) {
			DataGroup search = DataCreator.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"bookSearch", "search", "search", "testSystem");
			search.addChild(DataAtomic.withNameInDataAndValue("searchGroup", "someGroup"));

			DataGroup textId = createTextGroupWithNameInDataLinkedRecordTypeAndLInkedRecordId(
					"textId", "coraText", "someSearchText");
			search.addChild(textId);
			DataGroup defTextId = createTextGroupWithNameInDataLinkedRecordTypeAndLInkedRecordId(
					"defTextId", "coraText", "someSearchDefText");
			search.addChild(defTextId);

			DataGroup metadataId = DataGroup.withNameInData("metadataId");
			metadataId.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
			metadataId.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordId", "bookSearchGroup"));
			search.addChild(metadataId);
			recordList.add(search);

			DataGroup search2 = DataCreator.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"placeSearch", "search", "search", "testSystem");
			search2.addChild(DataAtomic.withNameInDataAndValue("searchGroup", "someGroup"));

			DataGroup metadataId2 = DataGroup.withNameInData("metadataId");
			metadataId2.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
			metadataId2.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordId", "placeSearchGroup"));
			search2.addChild(metadataId2);
			recordList.add(search2);

		}
		if ("place".equals(type)) {
			throw new RecordNotFoundException("No records exists with recordType place");
		}
		if ("recordTypeForChangingUpdateStructure".equals(type)) {
			DataGroup dataGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("someId",
							"updateStructure", "changingUpdateStructure\"", "testSystem");
			dataGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData", "mode"));

			DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
			String tsUpdatedString = "2018-04-03 11:36:38.789";
			String updatedByString = "someUser";
			String userType = "systemOneUser";

			tsUpdates.add(tsUpdatedString);
			updatedBys.add(updatedByString);
			updatedByUserTypes.add(userType);
			recordInfo.addChild(
					DataAtomic.withNameInDataAndValue("tsUpdated", tsUpdatedString));

			DataGroup updatedBy = DataGroup.withNameInData("updatedBy");
			updatedBy.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordType", userType));
			updatedBy.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", updatedByString));
			recordInfo.addChild(updatedBy);
			recordList.add(dataGroup);
		}

		return recordList;
	}

	private DataGroup createTextGroupWithNameInDataLinkedRecordTypeAndLInkedRecordId(
			String textIdNameInData, String linkedRecordType, String linkedRecordId) {
		DataGroup textId = DataGroup.withNameInData(textIdNameInData);
		textId.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		textId.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		return textId;
	}

	@Override
	public Collection<DataGroup> readAbstractList(String s, DataGroup filter) {
		return null;
	}

	@Override
	public DataGroup readLinkList(String s, String s1) {
		return null;
	}

	@Override
	public Collection<DataGroup> generateLinkCollectionPointingToRecord(String s, String s1) {
		return null;
	}

	@Override
	public boolean recordsExistForRecordType(String s) {
		return false;
	}

	@Override
	public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String s, String s1) {
		return false;
	}
}
