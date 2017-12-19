package se.uu.ub.cora.datamodifier.presentation;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.bookkeeper.data.DataElement;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.recordstorage.RecordStorageForAddingMissingModeToPGroup;

public class ModifierForPresentationGroupModeTest {
	private RecordStorageForAddingMissingModeToPGroup recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	ModifierForPresentationGroupMode modifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageForAddingMissingModeToPGroup();
		modifier = new ModifierForPresentationGroupMode();
		modifier.setLinkCollector(linkCollector);
		modifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testModifyOutputPGroup() {
		modifier.modifyByRecordType("presentationGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 2);
		DataGroup modified = recordStorage.modifiedDataGroupsSentToUpdate.get(0);
		String mode = modified.getFirstAtomicValueWithNameInData("mode");
		assertEquals(mode, "output");

	}

	@Test
	public void testModifyOutputPGroupWhereModeIsAlreadySet() {
		modifier.modifyByRecordType("presentationGroup");
		assertEquals(recordStorage.modifiedDataGroupsSentToUpdate.size(), 2);
		DataGroup modified = recordStorage.modifiedDataGroupsSentToUpdate.get(1);
		String mode = modified.getFirstAtomicValueWithNameInData("mode");
		assertEquals(mode, "NOTOutput");
		int modeCounter = 0;
		for (DataElement dataElement : modified.getChildren()) {
			if ("mode".equals(dataElement.getNameInData())) {
				modeCounter++;
			}
		}
		assertEquals(modeCounter, 1);

	}
}
