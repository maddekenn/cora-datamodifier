package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.datamodifier.RecordStorageSpy;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class DataModifierForPresentationChildReferenceTest {
	private RecordStorage recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	protected DataModifierForPresentationChildReference dataModifier;

	@BeforeMethod
	public void setUp() {
		dataModifier = new DataModifierForPresentationChildReference();
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageSpy();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.recordStorage);
		assertNotNull(dataModifier.linkCollector);
	}

	@Test
	public void testModifyChildReference() {
		dataModifier.modifyByRecordType("presentationGroup");

		assertEquals(linkCollector.metadataId, "presentationGroupGroup");

		RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);
		List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 0);

		DataGroup ref = extractRefByIndexAndRefGroupName(children, 0, "refGroup");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
		assertEquals(ref.getAttributes().get("type"), "presentation");

		DataGroup ref2 = extractRefByIndexAndRefGroupName(children, 1, "refGroup");
		assertEquals(ref2.getFirstAtomicValueWithNameInData("linkedRecordType"), "text");
		assertEquals(ref2.getAttributes().get("type"), "text");

		assertTrue(linkCollector.collectLinksWasCalled);
	}

	private List<DataGroup> extractChildrenFromModifiedByIndex(RecordStorageSpy recordStorageSpy,
			int index) {
		DataGroup modifiedDataGroup = recordStorageSpy.modifiedDataGroupsSentToUpdate.get(index);
		DataGroup childReferences = modifiedDataGroup
				.getFirstGroupWithNameInData("childReferences");

		List<DataGroup> children = childReferences.getAllGroupsWithNameInData("childReference");
		return children;
	}

	private DataGroup extractRefByIndexAndRefGroupName(List<DataGroup> children, int index,
			String refGroupName) {
		DataGroup childReference = children.get(index);
		DataGroup refGroup = childReference.getFirstGroupWithNameInData(refGroupName);
		DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");
		return ref;
	}

	@Test
	public void testModifyChildReferenceCheckRefMinGroup() {
		dataModifier.modifyByRecordType("presentationGroup");
		RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);

		assertEquals(linkCollector.metadataId, "presentationGroupGroup");
		assertEquals(recordStorageSpy.readRecordTypes.get(0), "presentationGroup");

		List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 1);

		DataGroup ref = extractRefByIndexAndRefGroupName(children, 0, "refGroup");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
		assertEquals(ref.getAttributes().get("type"), "presentation");

		DataGroup refMin = extractRefByIndexAndRefGroupName(children, 0, "refMinGroup");
		assertEquals(refMin.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
		assertEquals(refMin.getAttributes().get("type"), "presentation");

		assertTrue(linkCollector.collectLinksWasCalled);
	}

	@Test
	public void testModifyChildReferenceSurroundingContainer() {
		dataModifier.modifyByRecordType("presentationSurroundingContainer");
		RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);

		assertEquals(linkCollector.metadataId, "presentationSurroundingContainerGroup");
		assertEquals(recordStorageSpy.readRecordTypes.get(0), "presentationSurroundingContainer");

		List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 0);

		DataGroup ref = extractRefByIndexAndRefGroupName(children, 0, "refGroup");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
		assertEquals(ref.getAttributes().get("type"), "presentation");

		DataGroup ref2 = extractRefByIndexAndRefGroupName(children, 1, "refGroup");
		assertEquals(ref2.getFirstAtomicValueWithNameInData("linkedRecordType"), "text");
		assertEquals(ref2.getAttributes().get("type"), "text");

		assertTrue(linkCollector.collectLinksWasCalled);
	}

	@Test
	public void testModifyChildReferenceRepeatingContainer() {
		dataModifier.modifyByRecordType("presentationRepeatingContainer");
		RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);

		assertEquals(linkCollector.metadataId, "presentationRepeatingContainerGroup");
		assertEquals(recordStorageSpy.readRecordTypes.get(0), "presentationRepeatingContainer");

		List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 0);

		DataGroup ref = extractRefByIndexAndRefGroupName(children, 0, "refGroup");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
		assertEquals(ref.getAttributes().get("type"), "presentation");

		DataGroup ref2 = extractRefByIndexAndRefGroupName(children, 1, "refGroup");
		assertEquals(ref2.getFirstAtomicValueWithNameInData("linkedRecordType"), "text");
		assertEquals(ref2.getAttributes().get("type"), "text");

		assertTrue(linkCollector.collectLinksWasCalled);
	}

	@Test
	public void testModifyChildReferenceResourceLink() {
		dataModifier.modifyByRecordType("presentationResourceLink");
		RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);

		assertEquals(linkCollector.metadataId, "presentationResourceLinkGroup");
		assertEquals(recordStorageSpy.readRecordTypes.get(0), "presentationResourceLink");

		List<DataGroup> children = extractChildrenFromModifiedByIndex(recordStorageSpy, 0);

		DataGroup ref = extractRefByIndexAndRefGroupName(children, 0, "refGroup");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
		assertEquals(ref.getAttributes().get("type"), "presentation");

		DataGroup ref2 = extractRefByIndexAndRefGroupName(children, 1, "refGroup");
		assertEquals(ref2.getFirstAtomicValueWithNameInData("linkedRecordType"), "text");
		assertEquals(ref2.getAttributes().get("type"), "text");

		assertTrue(linkCollector.collectLinksWasCalled);
	}
}
