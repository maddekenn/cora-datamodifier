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
package se.uu.ub.cora.datamodifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.storage.MetadataStorage;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageSpy implements RecordStorage, MetadataStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	public List<DataGroup> createdData = new ArrayList<>();
	public List<String> createdType = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		if ("presentationGroup".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("presentationGroup",
					"presentationGroupGroup");
		}
		if ("presentationSurroundingContainer".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("presentationSurroundingContainer",
					"presentationSurroundingContainerGroup");
		}
		if ("presentationRepeatingContainer".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("presentationRepeatingContainer",
					"presentationRepeatingContainerGroup");
		}
		if ("presentationResourceLink".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("presentationResourceLink",
					"presentationResourceLinkGroup");
		}
		if ("recordType".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("recordType", "recordTypeGroup");
		}
		if ("metadataRecordLink".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataRecordLink", "metadataRecordLinkGroup");
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
		if ("metadataGroup".equals(type)) {
			DataGroup bookGroup = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"bookGroup", "metadata", "metadataGroup", "cora");
			DataGroup bookChildReferences = addChildReferencesForBook();
			bookGroup.addChild(bookChildReferences);
			recordList.add(bookGroup);

			DataGroup personGroup = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"personGroup", "metadata", "metadataGroup", "systemone");
			DataGroup personChildReferences = addChildReferencesForPerson();
			personGroup.addChild(personChildReferences);
			recordList.add(personGroup);

		}
		if ("recordType".equals(type)) {
			DataGroup book = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider("book",
					"recordType", "recordType", "cora");
			book.addChild(DataAtomic.withNameInDataAndValue("abstract", "false"));
			book.addChild(
					DataAtomic.withNameInDataAndValue("searchMetadataId", "someUnimportantId"));
			book.addChild(DataAtomic.withNameInDataAndValue("searchPresentationFormId",
					"someUnimportantId"));
			recordList.add(book);
			DataGroup place = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider("place",
					"recordType", "recordType", "cora");
			place.addChild(DataAtomic.withNameInDataAndValue("abstract", "false"));
			place.addChild(DataAtomic.withNameInDataAndValue("searchMetadataId",
					"somePlaceUnimportantId"));
			place.addChild(DataAtomic.withNameInDataAndValue("searchPresentationFormId",
					"somePlaceUnimportantId"));
			recordList.add(place);
			DataGroup authority = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"authority", "recordType", "recordType", "cora");
			authority.addChild(DataAtomic.withNameInDataAndValue("abstract", "true"));
			authority.addChild(DataAtomic.withNameInDataAndValue("searchMetadataId",
					"somePlaceUnimportantId"));
			authority.addChild(DataAtomic.withNameInDataAndValue("searchPresentationFormId",
					"somePlaceUnimportantId"));
			recordList.add(authority);
		}
		if ("presentationGroup".equals(type)) {
			DataGroup bookPGroup = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"bookPGroup", "presentation", "presentationGroup", "cora");

			DataGroup childReferences = addPresentationChildReferences(false);
			bookPGroup.addChild(childReferences);
			recordList.add(bookPGroup);

			DataGroup personPGroup = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"personPGroup", "presentation", "presentationGroup", "cora");

			personPGroup.addChild(addPresentationChildReferences(true));
			recordList.add(personPGroup);
		}

		if ("presentationSurroundingContainer".equals(type)) {
			DataGroup textPart = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"textPartEnOutputSContainer", "presentation",
					"presentationSurroundingContainer", "cora");
			textPart.addAttributeByIdWithValue("repeat", "children");
			textPart.addAttributeByIdWithValue("type", "container");

			DataGroup childReferences = addPresentationChildReferences(false);
			textPart.addChild(childReferences);
			recordList.add(textPart);
		}
		if ("presentationRepeatingContainer".equals(type)) {
			DataGroup textPart = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"textPartEnOutputRContainer", "presentation", "presentationRepeatingContainer",
					"cora");
			textPart.addAttributeByIdWithValue("repeat", "this");
			textPart.addAttributeByIdWithValue("type", "container");

			DataGroup childReferences = addPresentationChildReferences(false);
			textPart.addChild(childReferences);
			recordList.add(textPart);
		}
		if ("presentationResourceLink".equals(type)) {
			DataGroup textPart = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"masterInfoPResLink", "presentation", "presentationResourceLink", "cora");
			textPart.addAttributeByIdWithValue("type", "pResourceLink");

			DataGroup childReferences = addPresentationChildReferences(false);
			textPart.addChild(childReferences);
			recordList.add(textPart);

			DataGroup textPartWithNoChildren = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"masterInfoPResLinkNoChildren", "presentation", "presentationResourceLink",
					"cora");
			textPart.addAttributeByIdWithValue("type", "pResourceLink");
			recordList.add(textPartWithNoChildren);
		}
		if ("metadataRecordLink".equals(type)) {
			DataGroup someLink = createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"someLink", "metadata", "metadataRecordLink", "cora");
			someLink.addAttributeByIdWithValue("type", "recordLink");
			someLink.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "book"));
			recordList.add(someLink);
		}
		return recordList;
	}

	private DataGroup addChildReferencesForPerson() {
		DataGroup personChildReferences = DataGroup.withNameInData("childReferences");
		DataGroup childReference3 = createChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType(
				"0", "metadataGroup", "recordInfoGroup", "group");
		personChildReferences.addChild(childReference3);
		DataGroup childReference4 = createChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType(
				"1", "metadataTextVariable", "lastNameTextVar", "textVariable");
		personChildReferences.addChild(childReference4);
		return personChildReferences;
	}

	private DataGroup addChildReferencesForBook() {
		DataGroup bookChildReferences = DataGroup.withNameInData("childReferences");
		DataGroup childReference = createChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType("0",
				"metadataGroup", "recordInfoGroup", "group");
		bookChildReferences.addChild(childReference);
		DataGroup childReference1 = createChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType(
				"1", "metadataTextVariable", "bookTitleTextVar", "textVariable");
		bookChildReferences.addChild(childReference1);
		DataGroup childReference2 = createChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType(
				"1", "metadataRecordLink", "bookCoverLink", "recordLink");
		bookChildReferences.addChild(childReference2);
		return bookChildReferences;
	}

	private DataGroup createChildRefWithRepeatIdAndLinkedTypeAndIdAndAttributeType(String repeatId,
			String linkedRecordType, String linkedRecordId, String attributeType) {
		DataGroup childReference = DataGroup.withNameInData("childReference");
		childReference.setRepeatId(repeatId);
		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		ref.addAttributeByIdWithValue("type", attributeType);
		childReference.addChild(ref);
		return childReference;
	}

	private DataGroup createMetadataGroupWithIdAndNameInDataAndTypeAndDataDivider(String id,
			String nameInData, String type, String dataDividerId) {
		return DataCreator.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(id,
				nameInData, type, dataDividerId);
	}
	//
	// private DataGroup createDataDivider(String dataDividerId) {
	// DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
	// dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType",
	// "system"));
	// dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId",
	// dataDividerId));
	// return dataDivider;
	// }

	private DataGroup addPresentationChildReferences(boolean includeRefMinGroup) {
		DataGroup childReferences = DataGroup.withNameInData("childReferences");

		DataGroup childReference = DataGroup.withNameInData("childReference");
		childReference.addChild(DataAtomic.withNameInDataAndValue("default", "ref"));

		DataGroup refGroup = createRefGroupWithNameRecordTypeIdAndType("refGroup",
				"presentationGroup", "permissionRulePartOrganisationOutputPGroup", "pGroup");
		refGroup.setRepeatId("1");
		refGroup.addChild(DataAtomic.withNameInDataAndValue("childStyle", "oneChildStyle"));
		refGroup.addChild(DataAtomic.withNameInDataAndValue("textStyle", "oneTextStyle"));
		childReference.addChild(refGroup);

		if (includeRefMinGroup) {
			DataGroup refMinGroup = createRefGroupWithNameRecordTypeIdAndType("refMinGroup",
					"presentationRecordLink", "userRoleMinimizedOutputPLink", "pRecordLink");
			refMinGroup.setRepeatId("3");
			refMinGroup.addChild(DataAtomic.withNameInDataAndValue("childStyle", "zeroChildStyle"));
			refMinGroup.addChild(DataAtomic.withNameInDataAndValue("textStyle", "zeroTextStyle"));
			childReference.addChild(refMinGroup);
		}
		childReferences.addChild(childReference);

		DataGroup childReference2 = DataGroup.withNameInData("childReference");
		childReference2.addChild(DataAtomic.withNameInDataAndValue("default", "ref"));
		DataGroup refGroup2 = createRefGroupWithNameRecordTypeIdAndType("refGroup", "text",
				"someText", "text");
		refGroup2.setRepeatId("2");
		childReference2.addChild(refGroup2);
		childReferences.addChild(childReference2);

		DataGroup childReference3 = DataGroup.withNameInData("childReference");
		childReference3.addChild(DataAtomic.withNameInDataAndValue("default", "ref"));

		DataGroup refGroup3 = createRefGroupWithNameRecordTypeIdAndType("refGroup",
				"presentationGroup", "anotherOutputPGroup", "pGroup");
		refGroup.setRepeatId("1");
		childReference3.addChild(refGroup3);

		if (includeRefMinGroup) {
			DataGroup refMinGroup = createRefGroupWithNameRecordTypeIdAndType("refMinGroup",
					"presentationGroup", "anotherMinimizedOutputPGroup", "pGroup");
			refMinGroup.setRepeatId("3");
			refMinGroup.addChild(DataAtomic.withNameInDataAndValue("childStyle", "zeroChildStyle"));
			refMinGroup.addChild(DataAtomic.withNameInDataAndValue("textStyle", "zeroTextStyle"));
			childReference3.addChild(refMinGroup);
		}
		childReferences.addChild(childReference3);
		return childReferences;

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

	@Override
	public Collection<DataGroup> getMetadataElements() {
		return null;
	}

	@Override
	public Collection<DataGroup> getPresentationElements() {
		return null;
	}

	@Override
	public Collection<DataGroup> getTexts() {
		return null;
	}

	@Override
	public Collection<DataGroup> getRecordTypes() {
		return null;
	}
}
