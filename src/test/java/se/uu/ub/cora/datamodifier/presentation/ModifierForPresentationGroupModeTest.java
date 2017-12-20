package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForAddingMissingModeToPGroup;

public class ModifierForPresentationGroupModeTest {
	private RecordStorageForAddingMissingModeToPGroup recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	ModifierForPresentationGroupMode modifier;

	private int numberOfChildren = 8;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForAddingMissingModeToPGroup();
		modifier = new ModifierForPresentationGroupMode();
		modifier.setLinkCollector(linkCollector);
		modifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testModifyOutputPGroups() {
		modifier.modifyByRecordType("presentationGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), numberOfChildren);
		assertCorrectModeForChildWithIndex("output", 0);
		assertCorrectModeForChildWithIndex("output", 1);
		assertChildReferencesAreStillTheSame();

		assertCorrectModeForChildWithIndex("output", 2);
		assertCorrectModeForChildWithIndex("output", 3);

	}

	private void assertChildReferencesAreStillTheSame() {
		DataGroup modified = recordStorage.modifiedDataGroupsSentToUpdate.get(1);
		DataGroup childReferences = modified.getFirstGroupWithNameInData("childReferences");
		DataGroup childReference = childReferences.getFirstGroupWithNameInData("childReference");
		assertEquals(childReference.getChildren().size(), 2);
		assertTrue(childReference.containsChildWithNameInData("refGroup"));
		DataGroup refGroup = childReference.getFirstGroupWithNameInData("refGroup");
		assertEquals(refGroup.getChildren().size(), 3);
	}

	private void assertCorrectModeForChildWithIndex(String expectedMode, int index) {
		DataGroup modified = recordStorage.modifiedDataGroupsSentToUpdate.get(index);
		String mode = modified.getFirstAtomicValueWithNameInData("mode");
		assertEquals(mode, expectedMode);
	}

	@Test
	public void testModifyOutputPGroupWhereModeIsAlreadySet() {
		modifier.modifyByRecordType("presentationGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), numberOfChildren);
		DataGroup modified = recordStorage.modifiedDataGroupsSentToUpdate.get(4);
		String mode = modified.getFirstAtomicValueWithNameInData("mode");
		assertEquals(mode, "NOTOutput");
		int modeCounter = 0;
		for (DataElement dataElement : modified.getChildren()) {
			if ("mode".equals(dataElement.getNameInData())) {
				modeCounter++;
			}
		}
		assertEquals(modeCounter, 1);

	}

	@Test
	public void testModifyFormPGroup() {
		modifier.modifyByRecordType("presentationGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), numberOfChildren);
		assertCorrectModeForChildWithIndex("input", 5);

	}

	@Test
	public void testModifyInputPGroup() {
		modifier.modifyByRecordType("presentationGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), numberOfChildren);
		assertCorrectModeForChildWithIndex("input", 6);

	}

	@Test
	public void testModifyInputPGroupsWithMode() {
		modifier.modifyByRecordType("presentationGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), numberOfChildren);
		assertCorrectModeForChildWithIndex("NOTInput", 7);

	}
}
