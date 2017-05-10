package se.uu.ub.cora.datamodifier.metadata;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForMetadata;

public class ModifierForLinkedTypeInRecordInfo extends DataModifierForMetadata {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		String type = recordInfo.getFirstAtomicValueWithNameInData("type");

		recordInfo.removeFirstChildWithNameInData("type");

		DataGroup typeGroup = createTypeAsDataGroup(type);
		recordInfo.addChild(typeGroup);
	}

	private DataGroup createTypeAsDataGroup(String type) {
		DataGroup typeGroup = DataGroup.withNameInData("type");
		typeGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		typeGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", type));
		return typeGroup;
	}

}
