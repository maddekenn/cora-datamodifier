package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.datamodifier.RecordStorageSpy;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class ModifierForPresentationChildRefStyleTest {
	private RecordStorage recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	protected ModifierForPresentationChildRefStyle dataModifier;

	@BeforeMethod
	public void setUp() {
		dataModifier = new ModifierForPresentationChildRefStyle();
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
	public void init() {
		// skapa en presentationsgrupp med ett barn som har
		// childstyle i både ref och min ref
		// och ett barn som har både childstyle och textstyle
		// kör modifiering
		// kolla att det är childReference och inte childRefGroup och
		// childRefMinGroup som
		// har childstyle och textstyle
		// om både ref och refMin har så "vinner" ref

	}

}
