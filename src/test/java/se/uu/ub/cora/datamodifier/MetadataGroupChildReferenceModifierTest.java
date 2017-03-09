package se.uu.ub.cora.datamodifier;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MetadataGroupChildReferenceModifierTest {

	@Test
	public void testMainMethod() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		MetadataGroupChildReferenceModifier modifier = new MetadataGroupChildReferenceModifier();
		String args[] = new String[] {"/home/madde/workspace/modify/", "se.uu.ub.cora.datamodifier.DataModifierForChildReferenceSpy"};

		modifier.main(args);
		DataModifierForChildReferenceSpy childRefModifier = (DataModifierForChildReferenceSpy)modifier.dataModifier;
		assertEquals(childRefModifier.recordType, "metadataGroup");
	}
}
