package se.uu.ub.cora.datamodifier.presentation;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;

public class ModifierForPresentationGroupMode extends DataModifierForRecordType {

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		if (modeIsMissing(dataGroup)) {
			addMode(dataGroup);
		}
	}

	private boolean modeIsMissing(DataGroup dataGroup) {
		return !dataGroup.containsChildWithNameInData("mode");
	}

	private void addMode(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		String id = recordInfo.getFirstAtomicValueWithNameInData("id");
		String mode = getMode(id);
		dataGroup.addChild(DataAtomic.withNameInDataAndValue("mode", mode));
	}

	private String getMode(String id) {
		if (idIndicatesOutputMode(id)) {
			return "output";
		}
		return "input";
	}

	private boolean idIndicatesOutputMode(String id) {
		return id.endsWith("ViewPGroup") || id.endsWith("ListPGroup") || id.endsWith("MenuPGroup")
				|| id.endsWith("OutputPGroup");
	}

}
