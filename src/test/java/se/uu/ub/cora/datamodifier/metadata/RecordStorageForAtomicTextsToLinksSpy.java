package se.uu.ub.cora.datamodifier.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataCreator;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageForAtomicTextsToLinksSpy implements RecordStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	public List<DataGroup> createdData = new ArrayList<>();
	public List<String> createdType = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		if ("metadataCollectionItem".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataCollectionItem",
					"metadataCollectionItemGroup");
		}
		if ("metadataGroup".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataGroup",
					"metadataGroupGroup");
		}
		if ("presentationVar".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("presentationVar",
					"presentationVarGroup");
		}
		return null;
	}

	@Override
	public void create(String s, String s1, DataGroup dataGroup, DataGroup dataGroup1, String s2) {

	}

	@Override
	public void deleteByTypeAndId(String s, String s1) {

	}

	@Override
	public boolean linksExistForRecord(String s, String s1) {
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup record, DataGroup linkList,
			String dataDivider) {
		modifiedDataGroupsSentToUpdate.add(record);
	}

	@Override
	public Collection<DataGroup> readList(String type) {
		List<DataGroup> recordList = new ArrayList<>();
		if ("metadataCollectionItem".equals(type)) {
			DataGroup collectionItem = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("someItem", "metadata",
							"collectionItem", "testSystem");
			collectionItem.addChild(DataAtomic.withNameInDataAndValue("nameInData", "some"));
			collectionItem.addChild(DataAtomic.withNameInDataAndValue("textId", "someText"));
			collectionItem.addChild(DataAtomic.withNameInDataAndValue("defTextId", "someDefText"));
			recordList.add(collectionItem);

		}
		if ("metadataGroup".equals(type)) {
			DataGroup metadataGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("someTestGroup",
							"metadata", "metadataGroup", "testSystem");
			metadataGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData", "some"));
			metadataGroup
					.addChild(DataAtomic.withNameInDataAndValue("textId", "someTestGroupText"));
			metadataGroup.addChild(
					DataAtomic.withNameInDataAndValue("defTextId", "someTestGroupDefText"));
			recordList.add(metadataGroup);

		}
		if ("presentationVar".equals(type)) {
			DataGroup pVar = DataCreator.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"presentationRefPVar", "presentation", "presentationVar", "testSystem");
			pVar.addChild(DataAtomic.withNameInDataAndValue("emptyTextId", "someTextVar"));
			pVar.addAttributeByIdWithValue("type", "pVar");
			recordList.add(pVar);

			DataGroup pVarWithNoEmptyTextId = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
							"anotherPresentationRefPVar", "presentation", "presentationVar",
							"testSystem");
			pVarWithNoEmptyTextId.addAttributeByIdWithValue("type", "pVar");
			recordList.add(pVarWithNoEmptyTextId);
		}
		return recordList;
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

	@Override
	public Collection<DataGroup> readAbstractList(String type) {
		// TODO Auto-generated method stub
		return null;
	}
}
