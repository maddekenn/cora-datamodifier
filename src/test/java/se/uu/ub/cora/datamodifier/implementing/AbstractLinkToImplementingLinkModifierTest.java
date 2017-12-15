package se.uu.ub.cora.datamodifier.implementing;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;

public class AbstractLinkToImplementingLinkModifierTest {

	private DataRecordLinkCollectorSpy linkCollector;
	private RecordStorageForChangingAbstractLinkToImplementingSpy recordStorage;
	private AbstractLinkToImplementingLinkModifier dataModifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForChangingAbstractLinkToImplementingSpy();
		dataModifier = new AbstractLinkToImplementingLinkModifier();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);

	}

	@Test
	public void testMetadataGroupWithOneAbstractLinkFirstLevel() {
		dataModifier.modifyByRecordType("testBook");

		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup location = modifiedDataGroup.getFirstGroupWithNameInData("location");
		String linkedRecordType = location.getFirstAtomicValueWithNameInData("linkedRecordType");
		assertEquals(linkedRecordType, "locationOrganisation");

	}

	@Test
	public void testMetadataGroupWithOneImplementingtLinkFirstLevel() {
		dataModifier.modifyByRecordType("testBook");

		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup author = modifiedDataGroup.getFirstGroupWithNameInData("author");
		String linkedRecordType = author.getFirstAtomicValueWithNameInData("linkedRecordType");
		assertEquals(linkedRecordType, "person");

	}

}
