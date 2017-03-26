package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class ModifierForPresentationChildRefStyleTest {
	private RecordStorage recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	protected ModifierForPresentationChildRefStyle dataModifier;

	@BeforeMethod
	public void setUp() {
		dataModifier = new ModifierForPresentationChildRefStyle();
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForStyleSpy();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.recordStorage);
		assertNotNull(dataModifier.linkCollector);
	}

	@Test
	public void testStyle() {
		dataModifier.modifyByRecordType("presentationGroup");

		RecordStorageForStyleSpy recordStorageSpy = ((RecordStorageForStyleSpy) recordStorage);
		assertCorrectChildrenInFirstModifiedDataGroup(recordStorageSpy);

		// personPGroup
		List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 1);
		DataGroup secondChildReference = children.get(0);
		assertEquals(secondChildReference.getFirstAtomicValueWithNameInData("childStyle"),
				"oneChildStyle");
		assertEquals(secondChildReference.getFirstAtomicValueWithNameInData("textStyle"),
				"oneTextStyle");

		assertEquals(secondChildReference.getChildren().size(), 4);

		// TODO: kolla andra childreference i person, ska bli det värdet som är
		// i refMinGroup eftersom refGroup inte har style

	}

	private void assertCorrectChildrenInFirstModifiedDataGroup(
			RecordStorageForStyleSpy recordStorageSpy) {
		List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 0);
		DataGroup firstChildReference = children.get(0);
		assertEquals(firstChildReference.getFirstAtomicValueWithNameInData("childStyle"),
				"oneChildStyle");
		assertEquals(firstChildReference.getFirstAtomicValueWithNameInData("textStyle"),
				"oneTextStyle");

		DataGroup refGroup = firstChildReference.getFirstGroupWithNameInData("refGroup");
		assertFalse(refGroup.containsChildWithNameInData("childStyle"));
		assertFalse(refGroup.containsChildWithNameInData("textStyle"));

		DataGroup secondChildReference = children.get(1);
		assertFalse(secondChildReference.containsChildWithNameInData("childStyle"));
		assertFalse(secondChildReference.containsChildWithNameInData("textStyle"));

		DataGroup refGroup2 = secondChildReference.getFirstGroupWithNameInData("refGroup");
		assertFalse(refGroup2.containsChildWithNameInData("childStyle"));
		assertFalse(refGroup2.containsChildWithNameInData("textStyle"));
	}

	private List<DataGroup> extractChildrenFromModifiedByIndex(
			RecordStorageForStyleSpy recordStorageSpy, int index) {
		DataGroup modifiedDataGroup = recordStorageSpy.modifiedDataGroupsSentToUpdate.get(index);
		DataGroup childReferences = modifiedDataGroup
				.getFirstGroupWithNameInData("childReferences");

		List<DataGroup> children = childReferences.getAllGroupsWithNameInData("childReference");
		return children;
	}

	// private DataGroup extractRefByIndexAndRefGroupName(List<DataGroup>
	// children, int index,
	// String refGroupName) {
	// DataGroup childReference = children.get(index);
	// DataGroup refGroup =
	// childReference.getFirstGroupWithNameInData(refGroupName);
	// DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");
	// return ref;
	// }

}
