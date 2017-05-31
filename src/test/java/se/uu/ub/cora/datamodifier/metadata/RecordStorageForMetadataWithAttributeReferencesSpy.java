/*
 * Copyright 2017 Uppsala University Library
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
package se.uu.ub.cora.datamodifier.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.datamodifier.DataCreator;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageForMetadataWithAttributeReferencesSpy implements RecordStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	public List<DataGroup> createdData = new ArrayList<>();
	public List<String> createdType = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		if ("metadataGroup".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataGroup",
					"metadataGroupGroup");
		}
		if ("metadataRecordLink".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataRecordLink",
					"metadataRecordLinkGroup");
		}
		return null;
	}

	@Override
	public void create(String type, String id, DataGroup record, DataGroup linkList,
			String dataDivider) {
		createdData.add(record);
		createdType.add(type);

	}

	@Override
	public void deleteByTypeAndId(String type, String id) {
		// TODO Auto-generated method stubLink

	}

	@Override
	public boolean linksExistForRecord(String type, String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(String type, String id, DataGroup record, DataGroup linkList,
			String dataDivider) {
		modifiedDataGroupsSentToUpdate.add(record);

	}

	@Override
	public Collection<DataGroup> readList(String type) {

		List<DataGroup> recordList = new ArrayList<>();
		if ("metadataGroup".equals(type)) {
			DataGroup group = DataCreator.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"someGroup", "metadata", "metadataGroup", "testSystem");
			group.addAttributeByIdWithValue("type", "group");

			DataGroup attributeReferences = DataGroup.withNameInData("attributeReferences");
			createAndAddRefWithLinkedIdAndRepeatId(attributeReferences, "namePartGivenNameTypeCollectionVar", "0");
			createAndAddRefWithLinkedIdAndRepeatId(attributeReferences, "someOtherTypeCollectionVar", "1");
			group.addChild(attributeReferences);
			recordList.add(group);

			DataGroup withNoAttributeReferences = DataCreator.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"withoutAttributeReferencesGroup", "metadata", "metadataGroup", "testSystem");
			group.addAttributeByIdWithValue("type", "group");
			createAndAddChildReferences(withNoAttributeReferences);
			recordList.add(withNoAttributeReferences);
		}

		return recordList;
	}

	private void createAndAddChildReferences(DataGroup withNoAttributeReferences) {
		DataGroup childReferences = DataGroup.withNameInData("childReferences");
		DataGroup childReference = DataGroup.withNameInData("childReference");
		createAndAddRefGroup(childReference);
		childReferences.addChild(childReference);
		withNoAttributeReferences.addChild(childReferences);
	}

	private void createAndAddRefGroup(DataGroup childReference) {
		DataGroup refGroup = DataGroup.withNameInData("refGroup");
		refGroup.setRepeatId("0");
		createAndAddRef(refGroup);
		childReference.addChild(refGroup);
	}

	private void createAndAddRef(DataGroup refGroup) {
		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addAttributeByIdWithValue("type", "text");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "text"));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "firstNameTextVarText"));
		refGroup.addChild(ref);
	}

	private void createAndAddRefWithLinkedIdAndRepeatId(DataGroup attributeReferences, String linkedRecordId, String repeatId) {
		DataAtomic ref = DataAtomic.withNameInDataAndValue("ref",
                linkedRecordId);
		ref.setRepeatId(repeatId);
		attributeReferences.addChild(ref);
	}

	@Override
	public Collection<DataGroup> readAbstractList(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGroup readLinkList(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> generateLinkCollectionPointingToRecord(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean recordsExistForRecordType(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean recordExistsForAbstractOrImplementingRecordTypeAndRecordId(String type,
			String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
