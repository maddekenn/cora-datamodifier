package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForAtomicLinkedPresentationsToLinksSpy;

public class ModifierForAtomicLinkedPresentationToLinkInPLinkTest {
	private RecordStorageForAtomicLinkedPresentationsToLinksSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForAtomicLinkedPresentationToLinkInPLink dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForAtomicLinkedPresentationsToLinksSpy();

		dataModifier = new ModifierForAtomicLinkedPresentationToLinkInPLink();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.getLinkCollector());
		assertNotNull(dataModifier.getRecordStorage());
	}

	@Test
	public void testModifyPLinkWithNoLinkedPresentations() {
		dataModifier.modifyByRecordType("presentationRecordLink");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		assertFalse(modifiedDataGroup.containsChildWithNameInData("linkedRecordPresentations"));

	}

	@Test
	public void testModifyPLinkWithOneLinkedPresentations() {
		dataModifier.modifyByRecordType("presentationRecordLink");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(1);
		assertTrue(modifiedDataGroup.containsChildWithNameInData("linkedRecordPresentations"));
		List<DataGroup> linkedPresentationList = getListOfLinkedPresentations(modifiedDataGroup);
		assertEquals(linkedPresentationList.size(), 1);

		DataGroup linkedPresentation = linkedPresentationList.get(0);
		assertCorrectTypeAndIdInDataGroup("someRecordType", "someRecordId", linkedPresentation);

	}

	private void assertCorrectTypeAndIdInDataGroup(String recordType, String recordId,
			DataGroup linkedPresentation) {
		assertEquals(linkedPresentation.getFirstAtomicValueWithNameInData("linkedRecordType"),
				recordType);
		assertEquals(linkedPresentation.getFirstAtomicValueWithNameInData("linkedRecordId"),
				recordId);
	}

	private List<DataGroup> getListOfLinkedPresentations(DataGroup modifiedDataGroup) {
		DataGroup linkedPresentations = modifiedDataGroup
				.getFirstGroupWithNameInData("linkedRecordPresentations");
		List<DataGroup> linkedPresentationList = linkedPresentations
				.getAllGroupsWithNameInData("linkedRecordPresentation");
		return linkedPresentationList;
	}

	@Test
	public void testModifyPLinkWithTwoLinkedPresentations() {
		dataModifier.modifyByRecordType("presentationRecordLink");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(2);
		List<DataGroup> linkedPresentationList = getListOfLinkedPresentations(modifiedDataGroup);
		assertEquals(linkedPresentationList.size(), 2);

		DataGroup linkedPresentation = linkedPresentationList.get(0);
		assertCorrectTypeAndIdInDataGroup("someRecordType", "someRecordId", linkedPresentation);

		DataGroup linkedPresentation2 = linkedPresentationList.get(1);
		assertCorrectTypeAndIdInDataGroup("someOtherRecordType", "someOtherRecordId",
				linkedPresentation2);
	}

}
