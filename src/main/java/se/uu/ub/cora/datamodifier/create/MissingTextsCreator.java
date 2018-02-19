package se.uu.ub.cora.datamodifier.create;

import java.util.ArrayList;
import java.util.Collection;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class MissingTextsCreator extends DataModifierForRecordType {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyByRecordType(String recordType) {
		this.recordType = recordType;
		modifiedList = new ArrayList<>();
		try {
			DataGroup emptyFilter = DataGroup.withNameInData("filter");
			Collection<DataGroup> recordList = recordStorage.readList(recordType, emptyFilter);
			for (DataGroup dataGroup : recordList) {
				modifyDataGroup(dataGroup);
				// TODO: inte säkert att gruppen ska uppdateras, bara om en abstrakt typ har
				// bytts mot en
				// implementerande
				modifiedList.add(dataGroup);
			}
		} catch (RecordNotFoundException e) {
			// do nothing
			// will end up here if the recordType has no records
		}
		updateRecords();
	}
}
