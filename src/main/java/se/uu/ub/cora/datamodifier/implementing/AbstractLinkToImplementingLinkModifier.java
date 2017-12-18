package se.uu.ub.cora.datamodifier.implementing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class AbstractLinkToImplementingLinkModifier extends DataModifierForRecordType {

	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private static final String PARENT_ID = "parentId";
	private static final String LINKED_RECORD_TYPE = "linkedRecordType";
	private Collection<DataGroup> recordTypes;
	private Map<String, List<String>> parentRecordTypesWithChildHolder;

	@Override
	public void modifyByRecordType(String recordType) {
		recordTypes = recordStorage.readList("recordType", DataGroup.withNameInData("filter"));
		parentRecordTypesWithChildHolder = getParentRecordTypes();
		addChildRecordTypesToParents();
		super.modifyByRecordType(recordType);
	}

	private void addChildRecordTypesToParents() {
		for (DataGroup readRecordType : recordTypes) {
			possiblyAddRecordTypeAsChildToParent(parentRecordTypesWithChildHolder, readRecordType);
		}
	}

	private void possiblyAddRecordTypeAsChildToParent(
			Map<String, List<String>> parentRecordTypesWithChildHolder, DataGroup readRecordType) {
		if (recordTypeHasParent(readRecordType)) {
			addRecordTypeAsChildToParent(parentRecordTypesWithChildHolder, readRecordType);
		}
	}

	private void addRecordTypeAsChildToParent(
			Map<String, List<String>> parentRecordTypesWithChildHolder, DataGroup readRecordType) {
		String parentId = extractParentId(readRecordType);
		String id = extractIdFromRecordInfoInDataGroup(readRecordType);
		parentRecordTypesWithChildHolder.get(parentId).add(id);
	}

	private String extractParentId(DataGroup readRecordType) {
		DataGroup parent = readRecordType.getFirstGroupWithNameInData(PARENT_ID);
		return parent.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
	}

	private boolean recordTypeHasParent(DataGroup readRecordType) {
		return readRecordType.containsChildWithNameInData(PARENT_ID);
	}

	private String extractIdFromRecordInfoInDataGroup(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		return recordInfo.getFirstAtomicValueWithNameInData("id");
	}

	private Map<String, List<String>> getParentRecordTypes() {
		Map<String, List<String>> parentRecordTypesWithChildren = new HashMap<>();
		for (DataGroup readRecordType : recordTypes) {
			if (recordTypeIsAbstract(readRecordType)) {
				String id = extractIdFromRecordInfoInDataGroup(readRecordType);
				parentRecordTypesWithChildren.put(id, new ArrayList<>());
			}
		}
		return parentRecordTypesWithChildren;
	}

	private boolean recordTypeIsAbstract(DataGroup readRecordType) {
		return readRecordType.containsChildWithNameInData("abstract")
				&& "true".equals(readRecordType.getFirstAtomicValueWithNameInData("abstract"));
	}

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		for (DataElement dataElement : dataGroup.getChildren())
			findAndModifyLinksInDataGroupChild(dataElement);
	}

	private void findAndModifyLinksInDataGroupChild(DataElement dataElement) {
		if (dataElement instanceof DataGroup) {
			DataGroup child = (DataGroup) dataElement;
			findAndModifyLink(child);
		}
	}

	private void findAndModifyLink(DataGroup child) {
		if (childIsLink(child)) {
			possiblyModifyLink(child);
		} else {
			modifyDataGroup(child);
		}
	}

	private void possiblyModifyLink(DataGroup child) {
		String linkedRecordType = child.getFirstAtomicValueWithNameInData(LINKED_RECORD_TYPE);
		if (linkedRecordTypeIsAbstract(linkedRecordType)) {
			changeAbstractLinkToImplementing(child, linkedRecordType);
		}
	}

	private void changeAbstractLinkToImplementing(DataGroup child, String linkedRecordType) {
		List<String> implementingRecordTypes = getImplementingRecordTypes(linkedRecordType);
		String linkedRecordId = child.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
		String foundImplementingRecordType = null;
		for (String implementingRecordType : implementingRecordTypes) {
			foundImplementingRecordType = tryToReadRecordByImplementingRecordTypeAndId(
					implementingRecordType, linkedRecordId, foundImplementingRecordType);
		}
		possiblyModifyChild(child, foundImplementingRecordType);
	}

	private List<String> getImplementingRecordTypes(String linkedRecordType) {
		return parentRecordTypesWithChildHolder.get(linkedRecordType);
	}

	private String tryToReadRecordByImplementingRecordTypeAndId(String implementingRecordType,
			String linkedRecordId, String foundImplementingRecordType) {
		String foundImplementingType = foundImplementingRecordType;
		try {
			recordStorage.read(implementingRecordType, linkedRecordId);
			foundImplementingType = implementingRecordType;
		} catch (RecordNotFoundException e) {
			// do nothing
		}
		return foundImplementingType;
	}

	private void possiblyModifyChild(DataGroup child, String foundImplementingRecordType) {
		if (foundImplementingRecordType != null) {
			modifyChild(child, foundImplementingRecordType);
		}
	}

	private void modifyChild(DataGroup child, String foundImplementingRecordType) {
		child.removeFirstChildWithNameInData(LINKED_RECORD_TYPE);
		child.addChild(
				DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, foundImplementingRecordType));
	}

	private boolean linkedRecordTypeIsAbstract(String linkedRecordType) {
		return parentRecordTypesWithChildHolder.containsKey(linkedRecordType);
	}

	private boolean childIsLink(DataGroup child) {
		return child.containsChildWithNameInData(LINKED_RECORD_TYPE);
	}

}
