package se.uu.ub.cora.datamodifier.presentation;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

public class ModifierForPresentationChildRefStyle extends DataModifierForPresentations {

	private static final String CHILD_STYLE = "childStyle";

//	protected void modifyDataGroup(DataGroup dataGroup) {
//		DataGroup childReferences = dataGroup.getFirstGroupWithNameInData("childReferences");
//		childReferences
//				.getAllGroupsWithNameInData("childReference").forEach(this::modifyChildReference);
//	}

	protected void modifyChildReference(DataGroup childReference) {
		possiblyModifyRefGroupInChildReferenceByNameInData(childReference, "refGroup");
		possiblyModifyRefGroupInChildReferenceByNameInData(childReference, "refMinGroup");
	}

	private void possiblyModifyRefGroupInChildReferenceByNameInData(DataGroup childReference,
			String nameInData) {
		if (childReference.containsChildWithNameInData(nameInData)) {
			modifyRefGroupInChildReferenceByNameInData(childReference, nameInData);
		}
	}

	private void modifyRefGroupInChildReferenceByNameInData(DataGroup childReference,
															String nameInData) {
		DataGroup refGroup = childReference.getFirstGroupWithNameInData(nameInData);
		moveStyleIfExist(childReference, refGroup, CHILD_STYLE);
		moveStyleIfExist(childReference, refGroup, "textStyle");
	}

	private void moveStyleIfExist(DataGroup childReference, DataGroup refGroup, String style) {
		if (refGroup.containsChildWithNameInData(style)) {
			String childStyleValue = refGroup.getFirstAtomicValueWithNameInData(style);
			refGroup.removeFirstChildWithNameInData(style);
			if (!childReference.containsChildWithNameInData(style)) {
				childReference.addChild(DataAtomic.withNameInDataAndValue(style, childStyleValue));
			}
		}
	}
}
