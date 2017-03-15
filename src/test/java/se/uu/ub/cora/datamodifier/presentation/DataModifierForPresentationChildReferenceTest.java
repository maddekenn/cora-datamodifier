package se.uu.ub.cora.datamodifier.presentation;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataRecordLinkCollectorSpy;
import se.uu.ub.cora.datamodifier.RecordStorageSpy;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class DataModifierForPresentationChildReferenceTest {
    private RecordStorage recordStorage;
    private DataRecordLinkCollectorSpy linkCollector;
    protected DataModifierForPresentationChildReference dataModifier;

    @BeforeMethod
    public void setUp(){
        dataModifier = new DataModifierForPresentationChildReference();
        linkCollector = new DataRecordLinkCollectorSpy();
        recordStorage = new RecordStorageSpy();
        dataModifier.setLinkCollector(linkCollector);
        dataModifier.setRecordStorage(recordStorage);
    }

    @Test
    public void testInit(){
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
        DataGroup childReference = childReferences.getFirstGroupWithNameInData("childReference");

        DataGroup refGroup = childReference.getFirstGroupWithNameInData("refGroup");

        DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");

        assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "presentation");
        assertEquals(ref.getAttributes().size(), 0);
        assertTrue(linkCollector.collectLinksWasCalled);
    }
}
