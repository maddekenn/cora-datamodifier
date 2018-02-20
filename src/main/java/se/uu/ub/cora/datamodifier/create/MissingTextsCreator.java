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


	@Override
	public void modifyByRecordType(String recordType) {
		this.recordType = recordType;
		modifiedList = new ArrayList<>();
		try {
			DataGroup emptyFilter = DataGroup.withNameInData("filter");
			Collection<DataGroup> recordList = recordStorage.readList(recordType, emptyFilter);
			for (DataGroup dataGroup : recordList) {
				modifyDataGroup(dataGroup);
				// TODO: inte säkert att gruppen ska uppdateras, bara om en abstrakt typ har
				// bytts mot en
				// implementerande
//				modifiedList.add(dataGroup);
			}
		} catch (RecordNotFoundException e) {
			// do nothing
			// will end up here if the recordType has no records
		}
		updateRecords();
	}

	@Override
	protected void modifyDataGroup(DataGroup dataGroup) {
		createTextFromInfoInDataGroupUsingTextNameInData(dataGroup, "textId");
		createTextFromInfoInDataGroupUsingTextNameInData(dataGroup, "defTextId");
	}

	private void createTextFromInfoInDataGroupUsingTextNameInData(DataGroup dataGroup, String textNameInData) {
		DataGroup textIdGroup = dataGroup.getFirstGroupWithNameInData(textNameInData);
		String textId = textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
		String textRecordType = textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType");

		String dataDivider = extractDataDividerFromDataGroup(dataGroup);

		DataGroup text = createTextDataGroupWithIdAndTypeAndDataDivider(textId, textRecordType, dataDivider);

		DataGroup recordInfo = text.getFirstGroupWithNameInData("recordInfo");

		addCreatedAndUpdatedInfo(recordInfo);

		createTextParts(textId, text);

		DataGroup linkList = linkCollector.collectLinks("coraTextGroup", text, "coraText", "someId");
		DataGroup emptyCollectedData = DataGroup.withNameInData("collectedData");

		recordStorage.create(textRecordType, textId, text, emptyCollectedData, linkList, "cora");
	}

	private void createTextParts(String textId, DataGroup text) {
		DataGroup svTextPart = createTextPartWithTypeAndIdUsingTextId("default", "sv", textId);
		text.addChild(svTextPart);
		DataGroup enTextPart = createTextPartWithTypeAndIdUsingTextId("alternative", "en", textId);
		text.addChild(enTextPart);
	}

	private DataGroup createTextPartWithTypeAndIdUsingTextId(String type, String lang, String textId) {
		DataGroup textPart = DataGroup.withNameInData("textPart");
		textPart.addAttributeByIdWithValue("type", type);
		textPart.addAttributeByIdWithValue("lang", lang);
		textPart.addChild(DataAtomic.withNameInDataAndValue("text", "Text for:"+textId));
		return textPart;
	}

	private String extractDataDividerFromDataGroup(DataGroup dataGroup) {
		DataGroup dataGroupRecordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		DataGroup dataDividerGroup = dataGroupRecordInfo.getFirstGroupWithNameInData("dataDivider");

		return dataDividerGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	private DataGroup createTextDataGroupWithIdAndTypeAndDataDivider(String textId, String textRecordType, String dataDivider) {
		DataGroup text = DataGroup.withNameInData("text");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", textId));
		addType(recordInfo, textRecordType);
		addDataDivider(recordInfo, dataDivider);

		text.addChild(recordInfo);
		return text;
	}

	private void addType(DataGroup recordInfo, String textRecordType) {
		DataGroup typeGroup = createLinkWithNameInDataLinkedTypeAndLinkedId("type", "recordType", textRecordType);
		recordInfo.addChild(typeGroup);
	}

	private void addDataDivider(DataGroup recordInfo, String dataDivider) {
		DataGroup dataDividerGroup = createLinkWithNameInDataLinkedTypeAndLinkedId("dataDivider", "system", dataDivider);
		recordInfo.addChild(dataDividerGroup);
	}


	private void addCreatedAndUpdatedInfo(DataGroup recordInfo) {
		DataGroup createdBy = createLinkWithNameInDataLinkedTypeAndLinkedId("createdBy", "systemOneUser", "12345");
		recordInfo.addChild(createdBy);
		DataGroup updatedBy = createLinkWithNameInDataLinkedTypeAndLinkedId("updatedBy", "systemOneUser", "12345");
		recordInfo.addChild(updatedBy);

		String currentLocalDateTime = getLocalTimeDateAsString(LocalDateTime.now());
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("tsCreated", currentLocalDateTime));
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("tsUpdated", currentLocalDateTime));
	}

	private DataGroup createLinkWithNameInDataLinkedTypeAndLinkedId(String nameInData, String linkedRecordType, String linkedRecordId) {
		DataGroup createdBy = DataGroup.withNameInData(nameInData);
		createdBy.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		createdBy.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		return createdBy;
	}


	protected String getLocalTimeDateAsString(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		return localDateTime.format(formatter);
	}
}
