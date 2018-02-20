package se.uu.ub.cora.datamodifier.create;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataAttribute;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;

public class MissingTextsCreatorTest {

	private DataRecordLinkCollectorSpy linkCollector;
	private RecordStorageForCreatingMissingTextsSpy recordStorage;
	private MissingTextsCreator textsCreator;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForCreatingMissingTextsSpy();
		textsCreator = new MissingTextsCreator();
		textsCreator.setLinkCollector(linkCollector);
		textsCreator.setRecordStorage(recordStorage);

	}

	@Test
	public void testGroupMissingTextAndDefTextToImplementingType() {
		textsCreator.modifyByRecordType("metadataGroup");

		DataGroup createdText = recordStorage.createdTexts.get(0);
		assertCorrectCreatedText(createdText, "coraText", "someNonExistingText", "testSystem");

		DataGroup createdDefText = recordStorage.createdTexts.get(1);
		assertCorrectCreatedText(createdDefText, "textSystemOne", "someNonExistingDefText",
				"testSystem");

		assertCorrectValuesSentToStorageForMissingTextAndDefText();
		assertCorrectValuesSentToLinkCollectorForMissingTextAndDefText(createdText, createdDefText);
	}

	private void assertCorrectCreatedText(DataGroup createdText, String textRecordType,
			String textId, String dataDivider) {
		DataGroup recordInfo = createdText.getFirstGroupWithNameInData("recordInfo");

		assertCorrectIdAndTypeAndDataDivider(recordInfo, textRecordType, textId, dataDivider);
		assertCorrectCreatedAndUpdated(recordInfo);
		assertCorrectTextParts(createdText, textId);
	}

	private void assertCorrectIdAndTypeAndDataDivider(DataGroup recordInfo, String linkedRecordType,
			String id, String dataDivider) {
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), id);
		assertCorrectLinkChildWithNameInDataTypeAndId(recordInfo, "type", "recordType",
				linkedRecordType);
		assertCorrectLinkChildWithNameInDataTypeAndId(recordInfo, "dataDivider", "system",
				dataDivider);

	}

	private void assertCorrectLinkChildWithNameInDataTypeAndId(DataGroup recordInfo,
			String nameInData, String linkedRecordType, String linkedRecordId) {
		DataGroup createdBy = recordInfo.getFirstGroupWithNameInData(nameInData);
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordType"),
				linkedRecordType);
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordId"), linkedRecordId);
	}

	private void assertCorrectCreatedAndUpdated(DataGroup recordInfo) {
		assertCorrectLinkChildWithNameInDataTypeAndId(recordInfo, "createdBy", "systemOneUser",
				"12345");
		assertCorrectLinkChildWithNameInDataTypeAndId(recordInfo, "updatedBy", "systemOneUser",
				"12345");

		String timeRegex = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}";

		String tsCreated = recordInfo.getFirstAtomicValueWithNameInData("tsCreated");
		assertTrue(tsCreated.matches(timeRegex));
		String tsUpdated = recordInfo.getFirstAtomicValueWithNameInData("tsUpdated");
		assertTrue(tsUpdated.matches(timeRegex));
	}

	private void assertCorrectTextParts(DataGroup createdText, String textId) {
		DataGroup svTextPart = getTextPartGroupWithTypeAndLang(createdText, "default", "sv");
		assertEquals(svTextPart.getFirstAtomicValueWithNameInData("text"), "Text for:" + textId);

		DataGroup enTextPart = getTextPartGroupWithTypeAndLang(createdText, "alternative", "en");
		assertEquals(enTextPart.getFirstAtomicValueWithNameInData("text"), "Text for:" + textId);
	}

	private DataGroup getTextPartGroupWithTypeAndLang(DataGroup createdText, String type,
			String lang) {
		DataAttribute typeAttribute = DataAttribute.withNameInDataAndValue("type", type);
		DataAttribute langAttribute = DataAttribute.withNameInDataAndValue("lang", lang);

		List<DataGroup> textParts = (List<DataGroup>) createdText
				.getAllGroupsWithNameInDataAndAttributes("textPart", typeAttribute, langAttribute);
		return textParts.get(0);
	}

	private void assertCorrectValuesSentToStorageForMissingTextAndDefText() {
		assertEquals(recordStorage.createdTextIds.get(0), "someNonExistingText");
		assertEquals(recordStorage.createdTextRecordTypes.get(0), "coraText");
		assertEquals(recordStorage.createdTextIds.get(1), "someNonExistingDefText");
		assertEquals(recordStorage.createdTextRecordTypes.get(1), "textSystemOne");
		assertEquals(recordStorage.createdTexts.size(), 2);
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 0);
	}

	private void assertCorrectValuesSentToLinkCollectorForMissingTextAndDefText(
			DataGroup createdText, DataGroup createdDefText) {
		assertEquals(linkCollector.metadataIds.get(0), "coraTextGroup");
		assertEquals(linkCollector.fromRecordTypes.get(0), "coraText");
		assertEquals(linkCollector.fromRecordIds.get(0), "someNonExistingText");
		assertEquals(linkCollector.dataGroups.get(0), createdText);

		assertEquals(linkCollector.metadataIds.get(1), "textSystemOneGroup");
		assertEquals(linkCollector.fromRecordTypes.get(1), "textSystemOne");
		assertEquals(linkCollector.fromRecordIds.get(1), "someNonExistingDefText");
		assertEquals(linkCollector.dataGroups.get(1), createdDefText);
		assertEquals(linkCollector.noOfTimesCalled, 2);

	}

	@Test
	public void testGroupMissingTextAbstractTypeDefTextExist() {
		textsCreator.modifyByRecordType("metadataTextVariable");

		DataGroup createdText = recordStorage.createdTexts.get(0);
		assertCorrectCreatedText(createdText, "coraText", "someNonExistingAbstractText",
				"someOtherSystem");

		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		assertCorrectLinkChildWithNameInDataTypeAndId(modifiedDataGroup, "textId", "coraText",
				"someNonExistingAbstractText");
		assertCorrectValuesSentToStorageForMissingAbstractText();
		assertCorrectValuesSentToLinkCollectorForMissingAbstractText(createdText);
	}

	private void assertCorrectValuesSentToStorageForMissingAbstractText() {
		assertEquals(recordStorage.createdTextIds.get(0), "someNonExistingAbstractText");
		assertEquals(recordStorage.createdTextRecordTypes.get(0), "coraText");
		assertEquals(recordStorage.createdTexts.size(), 1);

		assertEquals(recordStorage.createdTextIds.get(0), "someNonExistingAbstractText");
		assertEquals(recordStorage.createdTextRecordTypes.get(0), "coraText");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 1);
	}

	private void assertCorrectValuesSentToLinkCollectorForMissingAbstractText(
			DataGroup createdText) {
		assertEquals(linkCollector.metadataIds.get(0), "coraTextGroup");
		assertEquals(linkCollector.fromRecordTypes.get(0), "coraText");
		assertEquals(linkCollector.fromRecordIds.get(0), "someNonExistingAbstractText");
		assertEquals(linkCollector.metadataIds.get(1), "metadataTextVariableGroup");
		assertEquals(linkCollector.fromRecordTypes.get(1), "metadataTextVariable");
		assertEquals(linkCollector.fromRecordIds.get(1), "someTestTextVar");
		assertEquals(linkCollector.noOfTimesCalled, 2);

		assertEquals(linkCollector.dataGroups.get(0), createdText);
	}

	@Test
	public void testRecordTypeWithNoRecords() {
		textsCreator.modifyByRecordType("recordTypeWithNoData");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 0);
	}

	@Test
	public void testGroupWithNoTextOrDefText() {
		textsCreator.modifyByRecordType("recordTypeWithoutTexts");
		assertEquals(recordStorage.createdTexts.size(), 0);
		// DataGroup createdText = recordStorage.createdTexts.get(0);
		// assertCorrectCreatedText(createdText, "coraText",
		// "someNonExistingAbstractText",
		// "someOtherSystem");
		//
		// DataGroup modifiedDataGroup =
		// recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		// assertCorrectLinkChildWithNameInDataTypeAndId(modifiedDataGroup, "textId",
		// "coraText",
		// "someNonExistingAbstractText");
		// assertCorrectValuesSentToStorageForMissingAbstractText();
		// assertCorrectValuesSentToLinkCollectorForMissingAbstractText(createdText);
	}

}
