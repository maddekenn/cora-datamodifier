package se.uu.ub.cora.recordstorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataCreator;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageForParentIdToLinksSpy implements RecordStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	public List<DataGroup> createdData = new ArrayList<>();
	public List<String> createdType = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		if ("metadataGroup".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataGroup",
					"metadataGroupGroup");
		}
		if ("metadataTextVariable".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataTextVariable",
					"metadataTextVariableGroup");
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
		if ("metadataGroup".equals(type)) {
			DataGroup metadataGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("someTestGroup",
							"metadata", "metadataGroup", "testSystem");
			metadataGroup.addAttributeByIdWithValue("type", "group");
			metadataGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData", "some"));
			metadataGroup
					.addChild(DataAtomic.withNameInDataAndValue("parentId", "someParentGroup"));
			recordList.add(metadataGroup);

		}
		if ("metadataTextVariable".equals(type)) {
			DataGroup metadataGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("someTestTextVar",
							"metadata", "metadataTextVariable", "testSystem");
			metadataGroup.addAttributeByIdWithValue("type", "textVariable");
			metadataGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData", "some"));
			metadataGroup
					.addChild(DataAtomic.withNameInDataAndValue("parentId", "someParentTextVar"));
			recordList.add(metadataGroup);

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
