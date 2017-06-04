package se.uu.ub.cora.datamodifier.metadata;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAtomicParentIdToLink extends DataModifierForRecordType {

	private static final String PARENT_ID = "parentId";

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		String parentIdValue = dataGroup.getFirstAtomicValueWithNameInData(PARENT_ID);
		dataGroup.removeFirstChildWithNameInData(PARENT_ID);
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		DataGroup typeGroup = recordInfo.getFirstGroupWithNameInData("type");
		String type = typeGroup.getFirstAtomicValueWithNameInData("linkedRecordId");

		DataGroup parentIdGroup = DataGroup.withNameInData(PARENT_ID);
		parentIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", type));
		parentIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", parentIdValue));
		dataGroup.addChild(parentIdGroup);

	}

}
