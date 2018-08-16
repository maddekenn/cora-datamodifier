/*
 * Copyright 2018 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.datamodifier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.bookkeeper.data.DataGroup;

public class RecordTypePGroupsModifierTest {

	private RecordStorageSpy recordStorage;
	private DataRecordLinkCollectorSpy linkCollector;
	RecordTypePGroupsModifier modifier;

	@BeforeMethod
	public void setUp() {
		linkCollector = new DataRecordLinkCollectorSpy();
		recordStorage = new RecordStorageSpy();

		modifier = new RecordTypePGroupsModifier();
		modifier.setLinkCollector(linkCollector);
		modifier.setRecordStorage(recordStorage);
	}

	@Test
	public void testInit() {
		assertNotNull(modifier.getLinkCollector());
		assertNotNull(modifier.getRecordStorage());
	}

	@Test
	public void testModify() {
		modifier.modifyByRecordType("myRecordType");
		assertEquals(recordStorage.readRecordTypeIds.get(0), "myRecordType");
		assertEquals(recordStorage.readRecordTypeTypes.get(0), "recordType");
		assertEquals(recordStorage.readRecordTypeIds.get(1), "myRecordTypePGroup");
		assertEquals(recordStorage.readRecordTypeTypes.get(1), "presentationGroup");

		DataGroup createdPGroup = recordStorage.createdData.get(0);
		DataGroup recordInfo = createdPGroup.getFirstGroupWithNameInData("recordInfo");
		String id = recordInfo.getFirstAtomicValueWithNameInData("id");
		assertEquals("myRecordTypePGroup", id);
	}
}
