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
package se.uu.ub.cora.datamodifier.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageForStyleSpy implements RecordStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	public List<DataGroup> createdData = new ArrayList<>();
	public List<String> createdType = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		if ("presentationGroup".equals(id)) {
			readRecordTypes.add(id);
			return createRecordTypeWithMetadataId("presentationGroup", "presentationGroupGroup");
		}
		return null;
	}

	private DataGroup createRecordTypeWithMetadataId(String recordId, String metadataId) {
		DataGroup surrounding = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
				recordId, "recordType", "recordType", "cora");
		DataGroup metadataIdGroup = DataGroup.withNameInData("metadataId");
		metadataIdGroup
				.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
		metadataIdGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", metadataId));
		surrounding.addChild(metadataIdGroup);
		return surrounding;
	}

	@Override
	public void create(String type, String id, DataGroup record, DataGroup linkList,
			String dataDivider) {
		createdData.add(record);
		createdType.add(type);

	}

	@Override
	public void deleteByTypeAndId(String type, String id) {
		// TODO Auto-generated method stub

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
		if ("presentationGroup".equals(type)) {
			DataGroup bookPGroup = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"bookPGroup", "presentation", "presentationGroup", "cora");
			DataGroup childReferences = DataGroup.withNameInData("childReferences");

			DataGroup firstReference = createChildWithRefGroupWithStyle();
			childReferences.addChild(firstReference);

			DataGroup secondReference = createChildWithRefGroupWithoutStyle();
			childReferences.addChild(secondReference);
			bookPGroup.addChild(childReferences);

			bookPGroup.addChild(childReferences);
			recordList.add(bookPGroup);

			DataGroup personPGroup = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"personPGroup", "presentation", "presentationGroup", "cora");
			DataGroup childReferences2 = DataGroup.withNameInData("childReferences");

			DataGroup firstReference2 = createChildRefWithRefGroupWithStyleAndRefMinGroupWithStyle();
			childReferences2.addChild(firstReference2);

			DataGroup secondReference2 = createChildRefWithRefGroupWithoutStyleAndRefMinGroupWithStyle();
			childReferences2.addChild(secondReference2);

			personPGroup.addChild(childReferences2);
			recordList.add(personPGroup);
		}
		return recordList;
	}

	private DataGroup createChildRefWithRefGroupWithStyleAndRefMinGroupWithStyle() {
		DataGroup firstReference2 = DataGroup.withNameInData("childReference");
		createAndAddRefGroupWithStyle(firstReference2);
		DataGroup refMinGroup = createRefGroupWithNameRecordTypeIdAndType("refMinGroup",
				"presentationRecordLink", "userRoleMinimizedOutputPLink", "pRecordLink");
		refMinGroup.setRepeatId("3");
		refMinGroup.addChild(DataAtomic.withNameInDataAndValue("childStyle", "zeroChildStyle"));
		refMinGroup.addChild(DataAtomic.withNameInDataAndValue("textStyle", "zeroTextStyle"));
		firstReference2.addChild(refMinGroup);
		return firstReference2;
	}

	private DataGroup createChildRefWithRefGroupWithoutStyleAndRefMinGroupWithStyle() {
		DataGroup firstReference2 = createChildWithRefGroupWithoutStyle();
		DataGroup refMinGroup = createRefGroupWithNameRecordTypeIdAndType("refMinGroup",
				"presentationRecordLink", "userRoleMinimizedOutputPLink", "pRecordLink");
		refMinGroup.setRepeatId("3");
		refMinGroup.addChild(DataAtomic.withNameInDataAndValue("childStyle", "zeroChildStyle"));
		refMinGroup.addChild(DataAtomic.withNameInDataAndValue("textStyle", "zeroTextStyle"));
		firstReference2.addChild(refMinGroup);
		return firstReference2;
	}

	private DataGroup createChildWithRefGroupWithoutStyle() {
		DataGroup childReference2 = DataGroup.withNameInData("childReference");
		childReference2.addChild(DataAtomic.withNameInDataAndValue("default", "ref"));
		DataGroup refGroup2 = createRefGroupWithNameRecordTypeIdAndType("refGroup", "text",
				"someText", "text");
		refGroup2.setRepeatId("2");
		childReference2.addChild(refGroup2);
		return childReference2;
	}

	private DataGroup createChildWithRefGroupWithStyle() {
		DataGroup childReference = DataGroup.withNameInData("childReference");
		childReference.addChild(DataAtomic.withNameInDataAndValue("default", "ref"));

		createAndAddRefGroupWithStyle(childReference);
		return childReference;
	}

	private void createAndAddRefGroupWithStyle(DataGroup childReference) {
		DataGroup refGroup = createRefGroupWithNameRecordTypeIdAndType("refGroup",
				"presentationGroup", "permissionRulePartOrganisationOutputPGroup", "pGroup");
		refGroup.setRepeatId("1");
		refGroup.addChild(DataAtomic.withNameInDataAndValue("childStyle", "oneChildStyle"));
		refGroup.addChild(DataAtomic.withNameInDataAndValue("textStyle", "oneTextStyle"));
		childReference.addChild(refGroup);
	}

	private DataGroup createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(String id,
			String nameInData, String type, String dataDividerId) {
		DataGroup metadataGroup = DataGroup.withNameInData(nameInData);
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("type", type));

		recordInfo.addChild(createDataDivider(dataDividerId));
		metadataGroup.addChild(recordInfo);
		return metadataGroup;
	}

	private DataGroup createDataDivider(String dataDividerId) {
		DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", dataDividerId));
		return dataDivider;
	}

	private DataGroup createRefGroupWithNameRecordTypeIdAndType(String nameInData,
			String recordType, String id, String type) {
		DataGroup refGroup = DataGroup.withNameInData(nameInData);
		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", recordType));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", id));
		ref.addAttributeByIdWithValue("type", type);
		refGroup.addChild(ref);
		return refGroup;
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
