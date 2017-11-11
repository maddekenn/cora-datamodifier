package se.uu.ub.cora.datamodifier.metadata;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForChangingTimestampFormatInRecordInfo extends DataModifierForRecordType {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		addNanosecondToTimestampInRecordInfoByNameInData(recordInfo, "tsCreated");
		addNanosecondToTimestampInRecordInfoByNameInData(recordInfo, "tsUpdated");
	}

	private void addNanosecondToTimestampInRecordInfoByNameInData(DataGroup recordInfo,
			String nameInData) {
		String timestamp = recordInfo.getFirstAtomicValueWithNameInData(nameInData);
		String newTimestampString = timestamp + ".0";

		recordInfo.removeFirstChildWithNameInData(nameInData);
		recordInfo.addChild(DataAtomic.withNameInDataAndValue(nameInData, newTimestampString));
	}

}
