package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;

public class ModifierForAtomicPresentationOfToLinkInSurroundingContainerTest {
	private RecordStorageForPresentationContainersSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	private ModifierForAtomicPresentationOfToLinkInSurroundingContainer dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForPresentationContainersSpy();

		dataModifier = new ModifierForAtomicPresentationOfToLinkInSurroundingContainer();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(dataModifier.getLinkCollector());
		assertNotNull(dataModifier.getRecordStorage());
	}

	@Test
	public void testModifyPresentationsOfOnePresentationOf() {
		dataModifier.modifyByRecordType("presentationSurroundingContainer");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup presentationsOf = modifiedDataGroup
				.getFirstGroupWithNameInData("presentationsOf");
		assertCorrectFirstGroup(presentationsOf);
		assertEquals(presentationsOf.getChildren().size(), 1);
	}

	@Test
	public void testModifyPresentationsOfTwoPresentationOf() {
		dataModifier.modifyByRecordType("presentationSurroundingContainer");
		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(1);
		DataGroup presentationsOf = modifiedDataGroup
				.getFirstGroupWithNameInData("presentationsOf");
		assertCorrectFirstGroup(presentationsOf);

		assertCorrectSecondGroup(presentationsOf);
		assertEquals(presentationsOf.getChildren().size(), 2);
	}

	private void assertCorrectSecondGroup(DataGroup presentationsOf) {
		DataGroup presentationOf2 = presentationsOf.getAllGroupsWithNameInData("presentationOf")
				.get(1);
		assertEquals(presentationOf2.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadata");
		assertEquals(presentationOf2.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"secondGroup");
		assertEquals(presentationOf2.getRepeatId(), "1");
	}

	private void assertCorrectFirstGroup(DataGroup presentationsOf) {
		DataGroup presentationOf = presentationsOf.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadata");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"firstGroup");
		assertEquals(presentationOf.getRepeatId(), "0");
	}
}
