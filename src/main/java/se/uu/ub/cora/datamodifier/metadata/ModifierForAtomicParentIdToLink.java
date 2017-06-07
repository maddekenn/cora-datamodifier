package se.uu.ub.cora.datamodifier.metadata;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForAtomicParentIdToLink extends DataModifierForRecordType {

	private static final String REF_PARENT_ID = "refParentId";

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		if (dataGroup.containsChildWithNameInData(REF_PARENT_ID)) {
			replaceAtomicParentIdWithLink(dataGroup);
		}
	}

	private void replaceAtomicParentIdWithLink(DataGroup dataGroup) {
		String parentIdValue = dataGroup.getFirstAtomicValueWithNameInData(REF_PARENT_ID);
		dataGroup.removeFirstChildWithNameInData(REF_PARENT_ID);
		DataGroup parentIdGroup = createParentGroup(dataGroup, parentIdValue);
		dataGroup.addChild(parentIdGroup);
	}

	private DataGroup createParentGroup(DataGroup dataGroup, String parentIdValue) {
		DataGroup parentIdGroup = DataGroup.withNameInData(REF_PARENT_ID);
		addLinkedRecordType(dataGroup, parentIdGroup);
		parentIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", parentIdValue));
		return parentIdGroup;
	}

	private void addLinkedRecordType(DataGroup dataGroup, DataGroup parentIdGroup) {
		String type = extractType(dataGroup);
		parentIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", type));
	}

	private String extractType(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		DataGroup typeGroup = recordInfo.getFirstGroupWithNameInData("type");
		return typeGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

}
