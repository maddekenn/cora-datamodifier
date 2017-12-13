package se.uu.ub.cora.datamodifier.implementing;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;

import static org.testng.Assert.*;

public class AbstractLinkToImplementingLinkModifierTest {

	private DataRecordLinkCollectorSpy linkCollector;
	private RecordStorageForChangingAbstractLinkToImplementingSpy recordStorage;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForChangingAbstractLinkToImplementingSpy();

	}

	@Test
	public void testMetadataGroupWithOneAbstractLink() {
		AbstractLinkToImplementingLinkModifier dataModifier = new AbstractLinkToImplementingLinkModifier();
		dataModifier.setLinkCollector(linkCollector);
		dataModifier.setRecordStorage(recordStorage);
		dataModifier.modifyByRecordType("testBook");

		DataGroup modifiedDataGroup = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		DataGroup location = modifiedDataGroup.getFirstGroupWithNameInData("location");
		String linkedRecordType = location.getFirstAtomicValueWithNameInData("linkedRecordType");
		assertEquals(linkedRecordType, "locationOrganisation");

	}

}
