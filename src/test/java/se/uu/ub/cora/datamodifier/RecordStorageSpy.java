package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageSpy implements RecordStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(String type, String id, DataGroup record, DataGroup linkList,
			String dataDivider) {
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
	public void update(String type, String id, DataGroup record, DataGroup linkList,
			String dataDivider) {
		modifiedDataGroupsSentToUpdate.add(record);

	}

	@Override
	public Collection<DataGroup> readList(String type) {
		List<DataGroup> recordList = new ArrayList<>();
		DataGroup bookGroup = createMetadataGroupWithIdAndDataDivider("bookGroup", "cora");
		DataGroup bookChildReferences = addChildReferencesForBook();
		bookGroup.addChild(bookChildReferences);
		recordList.add(bookGroup);

		DataGroup personGroup = createMetadataGroupWithIdAndDataDivider("personGroup", "systemone");
		DataGroup personChildReferences = addChildReferencesForPerson();
		personGroup.addChild(personChildReferences);
		recordList.add(personGroup);

		// TODO Auto-generated method stub
		return recordList;
	}

	private DataGroup addChildReferencesForPerson() {
		DataGroup personChildReferences = DataGroup.withNameInData("childReferences");
		DataGroup childReference3 = addChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType("0",
				"metadataGroup", "recordInfoGroup", "group");
		personChildReferences.addChild(childReference3);
		DataGroup childReference4 = addChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType("1",
				"metadataTextVariable", "lastNameTextVar", "textVariable");
		personChildReferences.addChild(childReference4);
		return personChildReferences;
	}

	private DataGroup addChildReferencesForBook() {
		DataGroup bookChildReferences = DataGroup.withNameInData("childReferences");
		DataGroup childReference = addChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType("0",
				"metadataGroup", "recordInfoGroup", "group");
		bookChildReferences.addChild(childReference);
		DataGroup childReference1 = addChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType("1",
				"metadataTextVariable", "bookTitleTextVar", "textVariable");
		bookChildReferences.addChild(childReference1);
		DataGroup childReference2 = addChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType("1",
				"metadataRecordLink", "bookCoverLink", "recordLink");
		bookChildReferences.addChild(childReference2);
		return bookChildReferences;
	}

	private DataGroup addChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType(String repeatId,
			String linkedRecordType, String linkedrecordId, String attributeType) {
		DataGroup childReference = DataGroup.withNameInData("childReference");
		childReference.setRepeatId(repeatId);
		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedrecordId));
		ref.addAttributeByIdWithValue("type", attributeType);
		childReference.addChild(ref);
		return childReference;
	}

	private DataGroup createMetadataGroupWithIdAndDataDivider(String id, String dataDividerId) {
		DataGroup metadataGroup = DataGroup.withNameInData("metadata");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("type", "metadataGroup"));

		DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", dataDividerId));
		recordInfo.addChild(dataDivider);
		metadataGroup.addChild(recordInfo);
		return metadataGroup;
	}

	@Override
	public Collection<DataGroup> readAbstractList(String type) {
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

}
