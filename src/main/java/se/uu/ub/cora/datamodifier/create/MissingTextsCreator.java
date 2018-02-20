package se.uu.ub.cora.datamodifier.create;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataModifierForRecordType;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class MissingTextsCreator extends DataModifierForRecordType {

	private static final String LINKED_RECORD_TYPE = "linkedRecordType";
	private static final String RECORD_INFO = "recordInfo";
	private static final String LINKED_RECORD_ID = "linkedRecordId";
	private boolean dataGroupWasModified = false;
	private DataGroup currentDataGroup;

	@Override
	public void modifyByRecordType(String recordType) {
		this.recordType = recordType;
		modifiedList = new ArrayList<>();
		try {
			DataGroup emptyFilter = DataGroup.withNameInData("filter");
			Collection<DataGroup> recordList = recordStorage.readList(recordType, emptyFilter);
			for (DataGroup dataGroup : recordList) {
				modifyDataGroup(dataGroup);
				possiblyAddToModifiedList(dataGroup);
			}
		} catch (RecordNotFoundException e) {
			// do nothing
			// will end up here if the recordType has no records
		}
		updateRecords();
	}

	private void possiblyAddToModifiedList(DataGroup dataGroup) {
		if (dataGroupWasModified) {
			modifiedList.add(dataGroup);
		}
	}

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		this.currentDataGroup = dataGroup;
		if (dataGroup.containsChildWithNameInData("textId")) {
			possiblyCreateTextUsingTextNameInData("textId");
		}
		if (dataGroup.containsChildWithNameInData("defTextId")) {
			possiblyCreateTextUsingTextNameInData("defTextId");
		}
	}

	private void possiblyCreateTextUsingTextNameInData(String textNameInData) {
		DataGroup textIdGroup = currentDataGroup.getFirstGroupWithNameInData(textNameInData);
		String textId = textIdGroup.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
		String textRecordType = textIdGroup.getFirstAtomicValueWithNameInData(LINKED_RECORD_TYPE);
		String recordTypeToCreate = textRecordType;
		if (textMissingInStorage(textRecordType, textId)) {
			recordTypeToCreate = changeRecordTypeToImplementingIfAbstract(textRecordType);
			createTextFromInfoInDataGroupUsingTextNameInData(recordTypeToCreate, textId);
		}

		possiblyChangeLinkedRecordTypeInDataGroup(textIdGroup, textRecordType, recordTypeToCreate);
	}

	private boolean textMissingInStorage(String recordType, String recordId) {
		try {
			recordStorage.read(recordType, recordId);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private void createTextFromInfoInDataGroupUsingTextNameInData(String recordTypeToCreate,
			String textId) {

		String dataDivider = extractDataDividerFromDataGroup();
		DataGroup text = createTextDataGroupWithIdAndTypeAndDataDivider(textId, recordTypeToCreate,
				dataDivider);

		createTextParts(textId, text);
		DataGroup linkList = linkCollector.collectLinks(recordTypeToCreate + "Group", text,
				recordTypeToCreate, textId);
		DataGroup emptyCollectedData = DataGroup.withNameInData("collectedData");

		recordStorage.create(recordTypeToCreate, textId, text, emptyCollectedData, linkList,
				"cora");
	}

	private String changeRecordTypeToImplementingIfAbstract(String textRecordType) {
		String recordTypeToCreate = textRecordType;
		String abstractValue = getAbstractValue(textRecordType);
		if ("true".equals(abstractValue)) {
			recordTypeToCreate = "coraText";
		}
		return recordTypeToCreate;
	}

	private String getAbstractValue(String textRecordType) {
		DataGroup recordTypeDefinition = recordStorage.read("recordType", textRecordType);
		return recordTypeDefinition.getFirstAtomicValueWithNameInData("abstract");
	}

	private String extractDataDividerFromDataGroup() {
		DataGroup dataGroupRecordInfo = currentDataGroup.getFirstGroupWithNameInData(RECORD_INFO);
		DataGroup dataDividerGroup = dataGroupRecordInfo.getFirstGroupWithNameInData("dataDivider");

		return dataDividerGroup.getFirstAtomicValueWithNameInData(LINKED_RECORD_ID);
	}

	private DataGroup createTextDataGroupWithIdAndTypeAndDataDivider(String textId,
			String textRecordType, String dataDivider) {
		DataGroup text = DataGroup.withNameInData("text");
		DataGroup recordInfo = createRecordInfoWithId(textId);
		addType(recordInfo, textRecordType);
		addDataDivider(recordInfo, dataDivider);
		addCreatedAndUpdatedInfo(recordInfo);

		text.addChild(recordInfo);
		return text;
	}

	private DataGroup createRecordInfoWithId(String textId) {
		DataGroup recordInfo = DataGroup.withNameInData(RECORD_INFO);
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", textId));
		return recordInfo;
	}

	private void addType(DataGroup recordInfo, String recordType) {
		DataGroup typeGroup = createLinkWithNameInDataLinkedTypeAndLinkedId("type", "recordType",
				recordType);
		recordInfo.addChild(typeGroup);
	}

	private DataGroup createLinkWithNameInDataLinkedTypeAndLinkedId(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		DataGroup createdBy = DataGroup.withNameInData(nameInData);
		createdBy.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, linkedRecordType));
		createdBy.addChild(DataAtomic.withNameInDataAndValue(LINKED_RECORD_ID, linkedRecordId));
		return createdBy;
	}

	private void addDataDivider(DataGroup recordInfo, String dataDivider) {
		DataGroup dataDividerGroup = createLinkWithNameInDataLinkedTypeAndLinkedId("dataDivider",
				"system", dataDivider);
		recordInfo.addChild(dataDividerGroup);
	}

	private void addCreatedAndUpdatedInfo(DataGroup recordInfo) {
		DataGroup createdBy = createLinkWithNameInDataLinkedTypeAndLinkedId("createdBy",
				"systemOneUser", "12345");
		recordInfo.addChild(createdBy);
		DataGroup updatedBy = createLinkWithNameInDataLinkedTypeAndLinkedId("updatedBy",
				"systemOneUser", "12345");
		recordInfo.addChild(updatedBy);

		String currentLocalDateTime = getLocalTimeDateAsString(LocalDateTime.now());
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("tsCreated", currentLocalDateTime));
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("tsUpdated", currentLocalDateTime));
	}

	protected String getLocalTimeDateAsString(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		return localDateTime.format(formatter);
	}

	private void createTextParts(String textId, DataGroup text) {
		DataGroup svTextPart = createTextPartWithTypeAndIdUsingTextId("default", "sv", textId);
		text.addChild(svTextPart);
		DataGroup enTextPart = createTextPartWithTypeAndIdUsingTextId("alternative", "en", textId);
		text.addChild(enTextPart);
	}

	private DataGroup createTextPartWithTypeAndIdUsingTextId(String type, String lang,
			String textId) {
		DataGroup textPart = DataGroup.withNameInData("textPart");
		textPart.addAttributeByIdWithValue("type", type);
		textPart.addAttributeByIdWithValue("lang", lang);
		textPart.addChild(DataAtomic.withNameInDataAndValue("text", "Text for:" + textId));
		return textPart;
	}

	private void possiblyChangeLinkedRecordTypeInDataGroup(DataGroup textIdGroup,
			String textRecordType, String recordTypeToCreate) {
		if (recordTypeWasAbstractAndWasChanged(textRecordType, recordTypeToCreate)) {
			textIdGroup.removeFirstChildWithNameInData(LINKED_RECORD_TYPE);
			textIdGroup.addChild(
					DataAtomic.withNameInDataAndValue(LINKED_RECORD_TYPE, recordTypeToCreate));
			dataGroupWasModified = true;
		}
	}

	private boolean recordTypeWasAbstractAndWasChanged(String textRecordType,
			String recordTypeToCreate) {
		return !textRecordType.equals(recordTypeToCreate);
	}
}
