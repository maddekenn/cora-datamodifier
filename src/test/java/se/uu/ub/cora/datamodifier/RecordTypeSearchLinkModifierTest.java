package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.Test;

public class RecordTypeSearchLinkModifierTest {

	@Test
	public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Constructor<RecordTypeSearchLinkModifier> constructor = RecordTypeSearchLinkModifier.class.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

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
