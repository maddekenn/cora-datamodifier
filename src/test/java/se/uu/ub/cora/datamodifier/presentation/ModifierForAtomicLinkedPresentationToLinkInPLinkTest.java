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
	public void testModifyPLinkWithOneLinkedPresentation() {
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
		assertNotNull(linkedPresentation.getRepeatId());

		assertCorrectPresentedRecordType(recordType, linkedPresentation);

		assertCorrectPresentation(recordId, linkedPresentation);
	}

	private void assertCorrectPresentedRecordType(String recordType, DataGroup linkedPresentation) {
		DataGroup presentedRecordType = linkedPresentation
				.getFirstGroupWithNameInData("presentedRecordType");
		assertEquals(presentedRecordType.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"recordType");
		assertEquals(presentedRecordType.getFirstAtomicValueWithNameInData("linkedRecordId"),
				recordType);
	}

	private void assertCorrectPresentation(String recordId, DataGroup linkedPresentation) {
		DataGroup presentation = linkedPresentation.getFirstGroupWithNameInData("presentation");
		assertEquals(presentation.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"presentation");
		assertEquals(presentation.getFirstAtomicValueWithNameInData("linkedRecordId"), recordId);
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

	// {
	// "name": "presentation",
	// "children": [
	// {
	// "name": "recordInfo",
	// "children": [
	// {
	// "name": "id",
	// "value": "somePLink"
	// },
	// {
	// "name": "dataDivider",
	// "children": [
	// {
	// "name": "linkedRecordType",
	// "value": "system"
	// },
	// {
	// "name": "linkedRecordId",
	// "value": "testsSystem"
	// }
	// ]
	// }
	// ]
	// },
	// {
	// "name": "presentationOf",
	// "children": [
	// {
	// "name": "linkedRecordType",
	// "value": "metadataRecordLink"
	// },
	// {
	// "name": "linkedRecordId",
	// "value": "someLink"
	// }
	// ]
	// },
	// {
	// "name": "mode",
	// "value": "input"
	// },
	// {
	// "name": "linkedRecordPresentations",
	// "children": [
	// {
	// "name": "linkedRecordPresentation",
	// "children": [
	// {
	// "name": "presentedRecordType",
	// "children": [
	// {
	// "name": "linkedRecordType",
	// "value": "recordType"
	// },
	// {
	// "name": "linkedRecordId",
	// "value": "text"
	// }
	// ]
	// },
	// {
	// "name": "presentation",
	// "children": [
	// {
	// "name": "linkedRecordType",
	// "value": "presentation"
	// },
	// {
	// "name": "linkedRecordId",
	// "value": "somePresentationPGroup"
	// }
	// ]
	// }
	// ],
	// "repeatId": "0"
	// },
	// {
	// "name": "linkedRecordPresentation",
	// "children": [
	// {
	// "name": "presentedRecordType",
	// "children": [
	// {
	// "name": "linkedRecordType",
	// "value": "recordType"
	// },
	// {
	// "name": "linkedRecordId",
	// "value": "coraText"
	// }
	// ]
	// },
	// {
	// "name": "presentation",
	// "children": [
	// {
	// "name": "linkedRecordType",
	// "value": "presentation"
	// },
	// {
	// "name": "linkedRecordId",
	// "value": "somePresentationRContainer"
	// }
	// ]
	// }
	// ],
	// "repeatId": "1"
	// }
	// ]
	// }
	// ],
	// "attributes": {
	// "type": "pRecordLink"
	// }
	// }

}
