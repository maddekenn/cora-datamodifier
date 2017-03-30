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
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class ModifierForPresentationChildRefStyleTest {
	private RecordStorage recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	protected ModifierForPresentationChildRefStyle dataModifier;

	@BeforeMethod
	public void setUp() {
		dataModifier = new ModifierForPresentationChildRefStyle();
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForStyleAndDefaultPresentationSpy();
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
		assertEquals(linkCollector.metadataId, "presentationGroupGroup");

		RecordStorageForStyleAndDefaultPresentationSpy recordStorageSpy = ((RecordStorageForStyleAndDefaultPresentationSpy) recordStorage);
		// bookPGroup
		assertCorrectChildrenInFirstModifiedDataGroup(recordStorageSpy);
		assertTrue(linkCollector.collectLinksWasCalled);

		// personPGroup
		assertCorrectChildrenInSecondModifiedDataGroup(recordStorageSpy);
		assertTrue(linkCollector.collectLinksWasCalled);

	}

	private void assertCorrectChildrenInFirstModifiedDataGroup(
			RecordStorageForStyleAndDefaultPresentationSpy recordStorageSpy) {
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
			RecordStorageForStyleAndDefaultPresentationSpy recordStorageSpy, int index) {
		DataGroup modifiedDataGroup = recordStorageSpy.modifiedDataGroupsSentToUpdate.get(index);
		DataGroup childReferences = modifiedDataGroup
				.getFirstGroupWithNameInData("childReferences");

		List<DataGroup> children = childReferences.getAllGroupsWithNameInData("childReference");
		return children;
	}

	private void assertCorrectChildrenInSecondModifiedDataGroup(
			RecordStorageForStyleAndDefaultPresentationSpy recordStorageSpy) {
		List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 1);
		DataGroup firstChildReference = children.get(0);
		assertEquals(firstChildReference.getFirstAtomicValueWithNameInData("childStyle"),
				"oneChildStyle");
		assertEquals(firstChildReference.getFirstAtomicValueWithNameInData("textStyle"),
				"oneTextStyle");

		assertEquals(firstChildReference.getChildren().size(), 5);
		DataGroup refGroup = firstChildReference.getFirstGroupWithNameInData("refGroup");
		assertFalse(refGroup.containsChildWithNameInData("childStyle"));
		assertFalse(refGroup.containsChildWithNameInData("textStyle"));

		DataGroup secondChildReference = children.get(1);

		assertEquals(secondChildReference.getFirstAtomicValueWithNameInData("childStyle"),
				"zeroChildStyle");
		assertEquals(secondChildReference.getFirstAtomicValueWithNameInData("textStyle"),
				"zeroTextStyle");
		DataGroup refMinGroup = secondChildReference.getFirstGroupWithNameInData("refMinGroup");
		assertFalse(refMinGroup.containsChildWithNameInData("childStyle"));
		assertFalse(refMinGroup.containsChildWithNameInData("textStyle"));
	}

}
