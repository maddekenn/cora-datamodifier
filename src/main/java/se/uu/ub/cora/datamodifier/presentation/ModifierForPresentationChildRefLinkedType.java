package se.uu.ub.cora.datamodifier.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.linkcollector.DataRecordLinkCollector;
import se.uu.ub.cora.datamodifier.DataModifier;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class ModifierForPresentationChildRefLinkedType extends DataModifierImp {
	private static final String PRESENTATION = "presentation";
	private static final String LINKED_RECORD_TYPE = "linkedRecordType";

	protected void modifyDataGroup(DataGroup dataGroup) {
		if (dataGroup.containsChildWithNameInData("childReferences")) {
			modifyChildReferences(dataGroup);
		}
	}

	private void modifyChildReferences(DataGroup dataGroup) {
		DataGroup childReferences = dataGroup.getFirstGroupWithNameInData("childReferences");
		childReferences
				.getAllGroupsWithNameInData("childReference").forEach(this::modifyRefGroupsInChildReference);
	}

	private void modifyRefGroupsInChildReference(DataGroup childReference) {
		modifyPresentationChildReference(childReference.getFirstGroupWithNameInData("refGroup"));
		if (childReference.containsChildWithNameInData("refMinGroup")) {
			modifyPresentationChildReference(
					childReference.getFirstGroupWithNameInData("refMinGroup"));
		}
	}

	private void modifyPresentationChildReference(DataGroup refGroup) {
		DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");
		String linkedRecordType = ref.getFirstAtomicValueWithNameInData(LINKED_RECORD_TYPE);
		if (linkedRecordType.startsWith(PRESENTATION)) {
			modifyChildReference(ref);
		}
	}

	protected void modifyChildReference(DataGroup ref) {
		removeAndAddLinkedRecordType(ref);
		removeAndAddAttribute(ref);
	}

	private void removeAndAddLinkedRecordType(DataGroup ref) {
		ref.removeFirstChildWithNameInData(LINKED_RECORD_TYPE);
		ref.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, PRESENTATION));
	}

	private void removeAndAddAttribute(DataGroup ref) {
		ref.getAttributes().remove("type");
		ref.addAttributeByIdWithValue("type", PRESENTATION);
	}
}
