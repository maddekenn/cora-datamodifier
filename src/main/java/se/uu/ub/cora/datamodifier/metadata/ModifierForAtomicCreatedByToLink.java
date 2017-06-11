package se.uu.ub.cora.datamodifier.metadata;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAtomicCreatedByToLink extends DataModifierForRecordType {

	private static final String CREATED_BY = "createdBy";

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		String createdByValue = extractCreatedByValue(recordInfo);
		removeAtomicCreatedByFromRecordInfo(recordInfo);
		DataGroup createdByGroup = createCreatedByGroup(createdByValue);
		recordInfo.addChild(createdByGroup);
	}

	private void removeAtomicCreatedByFromRecordInfo(DataGroup recordInfo) {
		recordInfo.removeFirstChildWithNameInData(CREATED_BY);
	}

	private String extractCreatedByValue(DataGroup recordInfo) {
		return recordInfo.getFirstAtomicValueWithNameInData(CREATED_BY);
	}

	private DataGroup createCreatedByGroup(String createdByValue) {
		DataGroup createdByGroup = DataGroup.withNameInData(CREATED_BY);
		createdByGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "user"));
		createdByGroup
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", createdByValue));
		return createdByGroup;
	}

}
