package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import org.testng.annotations.Test;

public class RecordTypeSearchLinkModifierTest {

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		String args[] = new String[] { "/home/madde/workspace/modify/",
				"se.uu.ub.cora.datamodifier.DataModifierForRecordTypeSearchLinkSpy" };

		RecordTypeSearchLinkModifier.main(args);
		DataModifierForRecordTypeSearchLinkSpy recordTypeModifier = (DataModifierForRecordTypeSearchLinkSpy) RecordTypeSearchLinkModifier.dataModifier;
		assertEquals(recordTypeModifier.recordType, "recordType");
	}
}
