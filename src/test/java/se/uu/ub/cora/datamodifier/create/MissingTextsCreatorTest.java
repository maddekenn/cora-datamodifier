package se.uu.ub.cora.datamodifier.create;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.Data;
import se.uu.ub.cora.bookkeeper.data.DataAttribute;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;

import java.util.List;

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
		assertEquals(recordStorage.createdTextIds.get(0), "someNonExistingText");
		assertEquals(recordStorage.createdTextRecordTypes.get(0), "coraText");

		DataGroup createdDefText = recordStorage.createdTexts.get(1);
		assertCorrectCreatedDefText(createdDefText);
		assertEquals(recordStorage.createdTexts.size(), 2);
		assertEquals(recordStorage.createdTextIds.get(1), "someNonExistingDefText");
		assertEquals(recordStorage.createdTextRecordTypes.get(1), "textSystemOne");

		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 0);
	}

	private void assertCorrectCreatedText(DataGroup createdText, String textRecordType, String textId, String dataDivider) {
		DataGroup recordInfo = createdText.getFirstGroupWithNameInData("recordInfo");
		assertCorrectIdAndTypeAndDataDivider(recordInfo, textRecordType, textId, dataDivider);
		assertCorrectCreatedAndUpdated(recordInfo);

		DataGroup svTextPart = getTextPartGroupWIthTypeAndLang(createdText, "default", "sv");
		assertEquals(svTextPart.getFirstAtomicValueWithNameInData("text"), "Text for:"+textId);

		DataGroup enTextPart = getTextPartGroupWIthTypeAndLang(createdText, "alternative", "en");
		assertEquals(enTextPart.getFirstAtomicValueWithNameInData("text"), "Text for:"+textId);
	}

	private DataGroup getTextPartGroupWIthTypeAndLang(DataGroup createdText, String type, String lang) {
		DataAttribute typeAttribute = DataAttribute.withNameInDataAndValue("type", type);
		DataAttribute langAttribute = DataAttribute.withNameInDataAndValue("lang", lang);

		List<DataGroup> textParts = (List<DataGroup>) createdText.getAllGroupsWithNameInDataAndAttributes("textPart", typeAttribute, langAttribute);
		return textParts.get(0);
	}

	private void assertCorrectIdAndTypeAndDataDivider(DataGroup recordInfo, String linkedRecordType, String id, String dataDivider) {
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), id);
		assertCorrectLinkChildWithNameInDataTypeAndId(recordInfo, "type", "recordType", linkedRecordType);
		assertCorrectLinkChildWithNameInDataTypeAndId(recordInfo, "dataDivider", "system", dataDivider);

	}

	private void assertCorrectLinkChildWithNameInDataTypeAndId(DataGroup recordInfo, String nameInData, String linkedRecordType, String linkedRecordId) {
		DataGroup createdBy = recordInfo.getFirstGroupWithNameInData(nameInData);
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordType"), linkedRecordType);
		assertEquals(createdBy.getFirstAtomicValueWithNameInData("linkedRecordId"), linkedRecordId);
	}

	private void assertCorrectCreatedAndUpdated(DataGroup recordInfo) {
		assertCorrectLinkChildWithNameInDataTypeAndId(recordInfo, "createdBy", "systemOneUser", "12345");

		assertCorrectLinkChildWithNameInDataTypeAndId(recordInfo, "updatedBy", "systemOneUser", "12345");

		String tsCreated = recordInfo.getFirstAtomicValueWithNameInData("tsCreated");
		assertTrue(tsCreated.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));

		String tsUpdated = recordInfo.getFirstAtomicValueWithNameInData("tsUpdated");
		assertTrue(tsUpdated.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
	}

	private void assertCorrectCreatedDefText(DataGroup createdText) {
		DataGroup recordInfo = createdText.getFirstGroupWithNameInData("recordInfo");
		assertCorrectIdAndTypeAndDataDivider(recordInfo, "textSystemOne", "someNonExistingDefText", "testSystem");
		assertCorrectCreatedAndUpdated(recordInfo);

		DataGroup svTextPart = getTextPartGroupWIthTypeAndLang(createdText, "default", "sv");
		assertEquals(svTextPart.getFirstAtomicValueWithNameInData("text"), "Text for:someNonExistingDefText");

		DataGroup enTextPart = getTextPartGroupWIthTypeAndLang(createdText, "alternative", "en");
		assertEquals(enTextPart.getFirstAtomicValueWithNameInData("text"), "Text for:someNonExistingDefText");
	}

	@Test
	public void testGroupMissingTextAbstractTypeDefTextExist() {
		textsCreator.modifyByRecordType("metadataTextVariable");

		DataGroup createdText = recordStorage.createdTexts.get(0);
		assertCorrectCreatedText(createdText, "coraText", "someNonExistingAbstractText", "someOtherSystem ");
		assertNotNull(createdText);
//		assertCorrectCreatedText(createdText);
//		assertEquals(recordStorage.createdTextIds.get(0), "someNonExistingText");
//		assertEquals(recordStorage.createdTextRecordTypes.get(0), "coraText");
//
//		DataGroup createdDefText = recordStorage.createdTexts.get(1);
//		assertCorrectCreatedDefText(createdDefText);
//		assertEquals(recordStorage.createdTexts.size(), 2);
//		assertEquals(recordStorage.createdTextIds.get(1), "someNonExistingDefText");
//		assertEquals(recordStorage.createdTextRecordTypes.get(1), "textSystemOne");

//		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 1);
	}
}
