package se.uu.ub.cora.recordstorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataCreator;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageForAddingMissingModeToPGroup implements RecordStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		if ("presentationGroup".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("presentationGroup",
					"presentationGroupGroup");
		}
		throw new RecordNotFoundException("Record of type " + type + " and id" + id + " not found");
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
		modifiedDataGroupsSentToUpdate.add(record);

	}

	@Override
	public Collection<DataGroup> readList(String type, DataGroup filter) {
		List<DataGroup> recordList = new ArrayList<>();
		if ("presentationGroup".equals(type)) {
			DataGroup outputWithNoModePGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("bookOutputPGroup",
							"presentation", "presentationGroup", "cora");
			recordList.add(outputWithNoModePGroup);

			DataGroup viewWithNoModePGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("bookViewPGroup",
							"presentation", "presentationGroup", "cora");
			DataGroup childReferences = createChildReferences();
			viewWithNoModePGroup.addChild(childReferences);
			recordList.add(viewWithNoModePGroup);

			DataGroup listWithNoModePGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("bookListPGroup",
							"presentation", "presentationGroup", "cora");
			recordList.add(listWithNoModePGroup);

			DataGroup menuWithNoModePGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("bookMenuPGroup",
							"presentation", "presentationGroup", "cora");
			recordList.add(menuWithNoModePGroup);

			DataGroup outputWithModePGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
							"bookWithModeOutputPGroup", "presentation", "presentationGroup",
							"cora");
			outputWithModePGroup.addChild(DataAtomic.withNameInDataAndValue("mode", "NOTOutput"));
			recordList.add(outputWithModePGroup);

			DataGroup formWithNoModePGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("bookFormPGroup",
							"presentation", "presentationGroup", "cora");
			recordList.add(formWithNoModePGroup);

			DataGroup inputWithNoModePGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("bookPGroup",
							"presentation", "presentationGroup", "cora");
			recordList.add(inputWithNoModePGroup);

			DataGroup intputWithModePGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("bookWithModePGroup",
							"presentation", "presentationGroup", "cora");
			intputWithModePGroup.addChild(DataAtomic.withNameInDataAndValue("mode", "NOTInput"));
			recordList.add(intputWithModePGroup);

		}
		return recordList;
	}

	private DataGroup createChildReferences() {
		DataGroup childReferences = DataGroup.withNameInData("childReferences");
		DataGroup childReference = DataGroup.withNameInData("childReference");
		childReference.addChild(DataAtomic.withNameInDataAndValue("default", "ref"));

		DataGroup refGroup = createRefGroupWithNameRecordTypeIdAndType("refGroup",
				"presentationGroup", "someOutputPGroup", "pGroup");
		refGroup.setRepeatId("1");
		refGroup.addChild(DataAtomic.withNameInDataAndValue("childStyle", "oneChildStyle"));
		refGroup.addChild(DataAtomic.withNameInDataAndValue("textStyle", "oneTextStyle"));
		childReference.addChild(refGroup);
		childReferences.addChild(childReference);
		return childReferences;
	}

	private DataGroup createRefGroupWithNameRecordTypeIdAndType(String nameInData,
			String recordType, String id, String type) {
		DataGroup refGroup = DataGroup.withNameInData(nameInData);
		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", recordType));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", id));
		ref.addAttributeByIdWithValue("type", type);
		refGroup.addChild(ref);
		return refGroup;
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

}
