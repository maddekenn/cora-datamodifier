package se.uu.ub.cora.datamodifier.metadata;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;

public class ModifierForLinkedRecordTypeInTextsTest {
	private RecordStorageForModifyingLinkedRecordTypeSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForLinkedRecordTypeInTexts dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForModifyingLinkedRecordTypeSpy();

		dataModifier = new ModifierForLinkedRecordTypeInTexts();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.getLinkCollector());
		assertNotNull(dataModifier.getRecordStorage());
	}

	@Test
	public void testModifyTextsInCollectionItem() {
		dataModifier.modifyByRecordType("metadataCollectionItem");

		assertCorrectModifiedGroupWithLinkedRecordTypeTextIdAndDefTextId("text", "someText",
				"someDefText");
		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

	private void assertCorrectModifiedGroupWithLinkedRecordTypeTextIdAndDefTextId(
			String linkedRecordType, String textId, String defTextId) {
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);

		DataGroup textIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				linkedRecordType);
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), textId);

		DataGroup defTextIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				linkedRecordType);
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), defTextId);
	}

	@Test
	public void testModifyTextsInItemCollection() {
		dataModifier.modifyByRecordType("metadataItemCollection");

		assertCorrectModifiedGroupWithLinkedRecordTypeTextIdAndDefTextId("text",
				"someCollectionText", "someCollectionDefText");
		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

	@Test
	public void testModifyTextsInRecordType() {
		dataModifier.modifyByRecordType("recordType");

		assertCorrectModifiedGroupWithLinkedRecordTypeTextIdAndDefTextId("text",
				"someRecordTypeText", "someRecordTypeDefText");
		assertEquals(linkCollector.noOfTimesCalled, 1);
	}

	@Test
	public void testModifyTextsInSearch() {
		dataModifier.modifyByRecordType("search");
		assertCorrectModifiedGroupWithLinkedRecordTypeTextIdAndDefTextId("text", "someSearchText",
				"someSearchDefText");
		assertEquals(linkCollector.noOfTimesCalled, 2);

	}

}
