package se.uu.ub.cora.datamodifier.create;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.storage.MetadataStorage;
import se.uu.ub.cora.datamodifier.DataCreator;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageForCreatingMissingTextsSpy implements RecordStorage, MetadataStorage {
	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		// if ("metadataGroup".equals(id)) {
		// readRecordTypes.add(id);
		// return DataCreator.createRecordTypeWithMetadataId("metadataGroup",
		// "metadataGroupGroup");
		// }
		// if ("coraText".equals(type)) {
		// readRecordTypes.add(id);
		// return DataCreator.createRecordTypeWithMetadataId("metadataGroup",
		// "metadataGroupGroup");
		// }
		throw new RecordNotFoundException("no record found with type: " + type + " and id: " + id);
	}

	@Override
	public void create(String type, String id, DataGroup record, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByTypeAndId(String type, String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean linksExistForRecord(String type, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup record, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<DataGroup> readList(String type, DataGroup filter) {
		List<DataGroup> recordList = new ArrayList<>();
		if ("metadataGroup".equals(type)) {
			DataGroup metadataGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("someTestGroup",
							"metadata", "metadataGroup", "testSystem");
			metadataGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData", "some"));

			DataGroup text = DataGroup.withNameInData("textId");
			text.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
			text.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "someTestGroupText"));
			metadataGroup.addChild(text);

			DataGroup defText = DataGroup.withNameInData("defTextId");
			defText.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "coraText"));
			defText.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordId", "someTestGroupDefText"));
			metadataGroup.addChild(defText);
			recordList.add(metadataGroup);

		}

		return recordList;
	}

	@Override
	public Collection<DataGroup> readAbstractList(String type, DataGroup filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGroup readLinkList(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> generateLinkCollectionPointingToRecord(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean recordsExistForRecordType(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String type,
			String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<DataGroup> getMetadataElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> getPresentationElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> getTexts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> getRecordTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> getCollectTerms() {
		// TODO Auto-generated method stub
		return null;
	}

}
