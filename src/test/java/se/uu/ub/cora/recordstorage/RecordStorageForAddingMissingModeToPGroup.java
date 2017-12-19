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
			// DataGroup childReferences =
			// addPresentationChildReferences(false);
			// bookPGroup.addChild(childReferences);
			recordList.add(outputWithNoModePGroup);

			DataGroup outputWithModePGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
							"bookWithModeOutputPGroup", "presentation", "presentationGroup",
							"cora");
			outputWithModePGroup.addChild(DataAtomic.withNameInDataAndValue("mode", "NOTOutput"));
			recordList.add(outputWithModePGroup);
			//
			// DataGroup personPGroup =
			// createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
			// "personPGroup", "presentation", "presentationGroup", "cora");
			//
			// personPGroup.addChild(addPresentationChildReferences(true));
			// recordList.add(personPGroup);
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

}
