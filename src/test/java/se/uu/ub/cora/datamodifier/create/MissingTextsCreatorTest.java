package se.uu.ub.cora.datamodifier.create;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;

public class MissingTextsCreatorTest {

	private DataRecordLinkCollectorSpy linkCollector;
	private RecordStorageForCreatingMissingTextsSpy recordStorage;
	private MissingTextsCreator textsCreator;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForCreatingMissingTextsSpy();
		textsCreator = new MissingTextsCreator();
		textsCreator.setLinkCollector(linkCollector);
		textsCreator.setRecordStorage(recordStorage);

	}

	@Test
	public void testInit() {
		textsCreator.modifyByRecordType("metadataGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 1);
	}

}
