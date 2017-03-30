package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

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

		RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);
		DataGroup modifiedDataGroup = recordStorageSpy.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup childReferences = modifiedDataGroup
				.getFirstGroupWithNameInData("childReferences");
		List<DataGroup> children = childReferences.getAllGroupsWithNameInData("childReference");
		DataGroup childReference = children.get(0);

		DataGroup refGroup = childReference.getFirstGroupWithNameInData("refGroup");

		DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");

		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
		assertEquals(ref.getAttributes().get("type"), "presentation");

		DataGroup childReference2 = children.get(1);
		DataGroup refGroup2 = childReference2.getFirstGroupWithNameInData("refGroup");

		DataGroup ref2 = refGroup2.getFirstGroupWithNameInData("ref");

		assertEquals(ref2.getFirstAtomicValueWithNameInData("linkedRecordType"), "text");
		assertEquals(ref2.getAttributes().get("type"), "text");

		assertTrue(linkCollector.collectLinksWasCalled);
	}

	@Test
	public void testModifyChildReferenceCheckRefMinGroup() {
		dataModifier.modifyByRecordType("presentationGroup");

		RecordStorageSpy recordStorageSpy = ((RecordStorageSpy) recordStorage);
		DataGroup modifiedDataGroup = recordStorageSpy.modifiedDataGroupsSentToUpdate.get(1);
		DataGroup childReferences = modifiedDataGroup
				.getFirstGroupWithNameInData("childReferences");
		DataGroup childReference = childReferences.getFirstGroupWithNameInData("childReference");

		DataGroup refGroup = childReference.getFirstGroupWithNameInData("refGroup");

		DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");

		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
        Map<String, String> attributes = ref.getAttributes();
        assertEquals(attributes.get("type"), "presentation");

		DataGroup refMinGroup = childReference.getFirstGroupWithNameInData("refMinGroup");
		DataGroup refMin = refMinGroup.getFirstGroupWithNameInData("ref");
		assertEquals(refMin.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
		assertEquals(refMin.getAttributes().get("type"), "presentation");

		assertTrue(linkCollector.collectLinksWasCalled);
	}
}
