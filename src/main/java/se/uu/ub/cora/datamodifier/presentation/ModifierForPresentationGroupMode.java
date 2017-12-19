package se.uu.ub.cora.datamodifier.presentation;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForPresentationGroupMode extends DataModifierForRecordType {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		if (!dataGroup.containsChildWithNameInData("mode")) {
			dataGroup.addChild(DataAtomic.withNameInDataAndValue("mode", "output"));
		}
	}

}
