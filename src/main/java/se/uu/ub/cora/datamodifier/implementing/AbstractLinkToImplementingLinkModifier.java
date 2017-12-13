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

	@Override
	public void modifyByRecordType(String recordType) {
		recordTypes = recordStorage.readList("recordType", DataGroup.withNameInData("filter"));
		Map<String, List<String>> parentRecordTypesWithChildHolder = getParentRecordTypes();
		addChildRecordTypesToParents(parentRecordTypesWithChildHolder);
		super.modifyByRecordType(recordType);
	}

	private void addChildRecordTypesToParents(
			Map<String, List<String>> parentRecordTypesWithChildHolder) {
		for (DataGroup readRecordType : recordTypes) {
			possiblyAddRecordTypeAsChildToParent(parentRecordTypesWithChildHolder, readRecordType);
		}
	}

	private void possiblyAddRecordTypeAsChildToParent(
			Map<String, List<String>> parentRecordTypesWithChildHolder, DataGroup readRecordType) {
		if (recordTypeHasParent(readRecordType)) {
			DataGroup parent = readRecordType.getFirstGroupWithNameInData(PARENT_ID);
			String parentId = parent.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
			String id = extractidFromRecordInfoInDataGroup(readRecordType);
			parentRecordTypesWithChildHolder.get(parentId).add(id);
		}
	}

	private boolean recordTypeHasParent(DataGroup readRecordType) {
		return readRecordType.containsChildWithNameInData(PARENT_ID);
	}

	private String extractidFromRecordInfoInDataGroup(DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		return recordInfo.getFirstAtomicValueWithNameInData("id");
	}

	private Map<String, List<String>> getParentRecordTypes() {
		Map<String, List<String>> parentRecordTypesWithChildren = new HashMap<>();
		for (DataGroup readRecordType : recordTypes) {
			if (recordTypeIsAbstract(readRecordType)) {
				String id = extractidFromRecordInfoInDataGroup(readRecordType);
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
			if (dataElement instanceof DataGroup) {
				DataGroup child = (DataGroup) dataElement;
				if (childIsLink(child)) {
					modifyLink(child);
				}
			}
		// TODO: iterate if child is group
	}

	private void modifyLink(DataGroup child) {
		String linkedRecordType = child.getFirstAtomicValueWithNameInData(LINKED_RECORD_TYPE);
		String linkedRecordId = child.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);

		List<String> implementingRecordTypes = getImplementingRecordTypes(linkedRecordType,
				recordTypes);
		String foundImplementingRecordType = null;
		for (String implementingRecordType : implementingRecordTypes) {
			try {
				recordStorage.read(implementingRecordType, linkedRecordId);
				foundImplementingRecordType = implementingRecordType;
			} catch (RecordNotFoundException e) {
				// do nothing
			}
		}
		if (foundImplementingRecordType != null) {
			child.removeFirstChildWithNameInData(LINKED_RECORD_TYPE);
			child.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE,
					foundImplementingRecordType));
		}
	}

	private boolean childIsLink(DataGroup child) {
		return child.containsChildWithNameInData(LINKED_RECORD_TYPE);
	}

	private List<String> getImplementingRecordTypes(String linkedRecordType,
			Collection<DataGroup> recordTypes) {
		List<String> implementingRecordTypes = new ArrayList<>();
		for (DataGroup recordType : recordTypes) {
			if (recordTypeHasParent(recordType)) {
				DataGroup parentGroup = recordType.getFirstGroupWithNameInData(PARENT_ID);
				String parentId = parentGroup.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
				if (parentId.equals(linkedRecordType)) {
					DataGroup recordInfo = recordType.getFirstGroupWithNameInData("recordInfo");

					implementingRecordTypes.add(recordInfo.getFirstAtomicValueWithNameInData("id"));
				}
			}
		}
		return implementingRecordTypes;
	}

}
