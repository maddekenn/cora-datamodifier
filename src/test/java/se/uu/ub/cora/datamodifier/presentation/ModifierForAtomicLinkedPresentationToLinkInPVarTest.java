package se.uu.ub.cora.datamodifier.presentation;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForAtomicLinkedPresentationsToLinksSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForAtomicTextsToLinksSpy;

import static org.testng.Assert.*;

public class ModifierForAtomicLinkedPresentationToLinkInPVarTest {
	private RecordStorageForAtomicLinkedPresentationsToLinksSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForAtomicLinkedPresentationToLinkInPVar dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForAtomicLinkedPresentationsToLinksSpy();

		dataModifier = new ModifierForAtomicLinkedPresentationToLinkInPVar();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.getLinkCollector());
		assertNotNull(dataModifier.getRecordStorage());
	}

	@Test
	public void testModifyPVarWithNoLinkedPresentations() {
		dataModifier.modifyByRecordType("presentationRecordLink");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		assertFalse(modifiedDataGroup.containsChildWithNameInData("linkedRecordPresentations"));

	}

	@Test
	public void testModifyPVarWithOneLinkedPresentations() {
		dataModifier.modifyByRecordType("presentationRecordLink");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(1);
		assertTrue(modifiedDataGroup.containsChildWithNameInData("linkedRecordPresentations"));

	}

//	@Test
//	public void testModifyPVarWithNoEmptyTextId() {
//
//		dataModifier.modifyByRecordType("presentationVar");
//	}
//
//	@Test
//	public void testModifyPCollVarWithEmptyTextId() {
//
//		dataModifier.modifyByRecordType("presentationCollectionVar");
//		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
//		DataGroup emptyTextIdGroup = modifiedDataGroup.getFirstGroupWithNameInData("emptyTextId");
//		assertEquals(emptyTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
//				"text");
//		assertEquals(emptyTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
//				"initialEmptyValueText");
//	}
//
//	@Test
//	public void testModifyPCollVarWithNoEmptyTextId() {
//
//		dataModifier.modifyByRecordType("presentationCollectionVar");
//		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(1);
//		assertFalse(modifiedDataGroup.containsChildWithNameInData("emptyTextId"));
//	}
}
