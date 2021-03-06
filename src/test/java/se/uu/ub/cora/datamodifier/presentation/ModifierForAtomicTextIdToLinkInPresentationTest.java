package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForAtomicTextsToLinksSpy;

public class ModifierForAtomicTextIdToLinkInPresentationTest {
	private RecordStorageForAtomicTextsToLinksSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForAtomicTextIdToLinkInPresentation dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForAtomicTextsToLinksSpy();

		dataModifier = new ModifierForAtomicTextIdToLinkInPresentation();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.getLinkCollector());
		assertNotNull(dataModifier.getRecordStorage());
	}

	@Test
	public void testModifyPVarWithEmptyTextId() {

		dataModifier.modifyByRecordType("presentationVar");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup emptyTextIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("emptyTextId");
		assertEquals(emptyTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"text");
		assertEquals(emptyTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someText");
	}

	@Test
	public void testModifyPVarWithNoEmptyTextId() {

		dataModifier.modifyByRecordType("presentationVar");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(1);
		assertFalse(modifiedDataGroup.containsChildWithNameInData("emptyTextId"));
	}

	@Test
	public void testModifyPCollVarWithEmptyTextId() {

		dataModifier.modifyByRecordType("presentationCollectionVar");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup emptyTextIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("emptyTextId");
		assertEquals(emptyTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"text");
		assertEquals(emptyTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"initialEmptyValueText");
	}

	@Test
	public void testModifyPCollVarWithNoEmptyTextId() {

		dataModifier.modifyByRecordType("presentationCollectionVar");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(1);
		assertFalse(modifiedDataGroup.containsChildWithNameInData("emptyTextId"));
	}
}
