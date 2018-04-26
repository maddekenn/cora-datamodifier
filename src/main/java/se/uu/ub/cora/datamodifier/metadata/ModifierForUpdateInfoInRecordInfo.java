package se.uu.ub.cora.datamodifier.metadata;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForUpdateInfoInRecordInfo extends DataModifierForRecordType {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		DataGroup updatedGroup = createUpdatedGroupUsingRecordInfo(recordInfo);

		removeOldStructure(recordInfo);
		recordInfo.addChild(updatedGroup);

	}

	private DataGroup createUpdatedGroupUsingRecordInfo(DataGroup recordInfo) {
		DataGroup updatedBy = recordInfo.getFirstGroupWithNameInData("updatedBy");
		String tsUpdated = recordInfo.getFirstAtomicValueWithNameInData("tsUpdated");
		return createUpdatedGroup(updatedBy, tsUpdated);
	}

	private void removeOldStructure(DataGroup recordInfo) {
		recordInfo.removeFirstChildWithNameInData("updatedBy");
		recordInfo.removeFirstChildWithNameInData("tsUpdated");
	}

	private DataGroup createUpdatedGroup(DataGroup updatedBy, String tsUpdated) {
		DataGroup updatedGroup = DataGroup.withNameInData("updated");
		updatedGroup.addChild(updatedBy);
		updatedGroup.addChild(DataAtomic.withNameInDataAndValue("tsUpdated", tsUpdated));
		updatedGroup.setRepeatId("0");
		return updatedGroup;
	}

}
