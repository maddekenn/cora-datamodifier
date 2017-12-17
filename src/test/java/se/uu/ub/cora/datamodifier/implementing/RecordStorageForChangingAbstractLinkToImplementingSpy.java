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
package se.uu.ub.cora.datamodifier.implementing;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.bookkeeper.data.DataAtomic;
import se.uu.ub.cora.bookkeeper.data.DataGroup;
import se.uu.ub.cora.bookkeeper.storage.MetadataStorage;
import se.uu.ub.cora.datamodifier.DataCreator;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;
import se.uu.ub.cora.spider.record.storage.RecordStorage;

public class RecordStorageForChangingAbstractLinkToImplementingSpy
		implements RecordStorage, MetadataStorage {

	public List<DataGroup> modifiedDataGroupsSentToUpdate = new ArrayList<>();
	public List<String> readRecordTypes = new ArrayList<>();

	public List<DataGroup> createdData = new ArrayList<>();
	public List<String> createdType = new ArrayList<>();

	@Override
	public DataGroup read(String type, String id) {
		if ("testBook".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("testBook", "testBookGroup");
		}
		if ("testArticle".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("testArticle", "testArticleGroup");
		}
		if ("metadataGroup".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("metadataGroup",
					"metadataGroupGroup");
		}
		if ("locationOrganisation".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("locationOrganisation",
					"locationOrganisationGroup");
		}
		if ("locationOrganisation".equals(type) && "org:001".equals(id)) {
			readRecordTypes.add(id);
			return DataGroup.withNameInData("organisation");
		}
		if ("testBookLinkWithoutChildren".equals(id)) {
			readRecordTypes.add(id);
			return DataCreator.createRecordTypeWithMetadataId("testBookLinkWithoutChildren",
					"testtestBookLinkWithoutChildrenGroup");
		}
		throw new RecordNotFoundException("no record found with type: " + type + " and id: " + id);
	}

	@Override
	public void create(String type, String id, DataGroup record, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
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
	public void update(String type, String id, DataGroup record, DataGroup collectedTerms,
			DataGroup linkList, String dataDivider) {
		modifiedDataGroupsSentToUpdate.add(record);

	}

	@Override
	public Collection<DataGroup> readList(String type, DataGroup filter) {

		List<DataGroup> recordList = new ArrayList<>();
		if ("testBook".equals(type)) {
			DataGroup dataGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("testBookGroup",
							"testBook", "testBook", "testSystem");

			DataGroup abstractChildLink = DataGroup.withNameInData("location");
			abstractChildLink.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordType", "organisation"));
			abstractChildLink
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "org:001"));
			dataGroup.addChild(abstractChildLink);

			DataGroup implementingChildLink = DataGroup.withNameInData("author");
			implementingChildLink
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "person"));
			implementingChildLink
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "person:001"));
			dataGroup.addChild(implementingChildLink);
			recordList.add(dataGroup);

		}

		if ("testArticle".equals(type)) {
			DataGroup dataGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("testArticleGroup",
							"testArticle", "testArticle", "testSystem");

			DataGroup childDataGroup = DataGroup.withNameInData("middleGroup");
			DataGroup abstractChildLink = DataGroup.withNameInData("location");
			abstractChildLink.addChild(
					DataAtomic.withNameInDataAndValue("linkedRecordType", "organisation"));
			abstractChildLink
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "org:001"));
			childDataGroup.addChild(abstractChildLink);

			DataGroup implementingChildLink = DataGroup.withNameInData("author");
			implementingChildLink
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "person"));
			implementingChildLink
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "person:001"));
			childDataGroup.addChild(implementingChildLink);
			dataGroup.addChild(childDataGroup);
			recordList.add(dataGroup);

		}
		if ("recordType".equals(type)) {
			DataGroup organisation = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("organisation",
							"recordType", "recordType", "testSystem");
			organisation.addChild(DataAtomic.withNameInDataAndValue("abstract", "true"));
			recordList.add(organisation);

			DataGroup locationOrganisation = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider("locationOrganisation",
							"recordType", "recordType", "testSystem");
			DataGroup parent = DataGroup.withNameInData("parentId");
			parent.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
			parent.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "organisation"));
			locationOrganisation.addChild(parent);
			recordList.add(locationOrganisation);

			DataGroup permissionOrganisation = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
							"permissionOrganisation", "recordType", "recordType", "testSystem");
			DataGroup permissionOrgParent = DataGroup.withNameInData("parentId");
			permissionOrgParent
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
			permissionOrgParent
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "organisation"));
			permissionOrganisation.addChild(parent);
			recordList.add(permissionOrganisation);

			DataGroup person = DataCreator.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
					"person", "recordType", "recordType", "testSystem");
			organisation.addChild(DataAtomic.withNameInDataAndValue("abstract", "false"));
			recordList.add(person);

			DataGroup organisationNoImplementingChild = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
							"organisationNoImplementingChild", "recordType", "recordType",
							"testSystem");
			organisation.addChild(DataAtomic.withNameInDataAndValue("abstract", "true"));
			recordList.add(organisation);

		}

		if ("testBookLinkWithoutChildren".equals(type)) {
			DataGroup dataGroup = DataCreator
					.createDataGroupWithIdAndNameInDataAndTypeAndDataDivider(
							"testtestBookLinkWithoutChildrenGroup", "testBookLinkWithoutChildren",
							"testBookLinkWithoutChildren", "testSystem");

			DataGroup abstractChildLink = DataGroup.withNameInData("location");
			abstractChildLink.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType",
					"organisationNoImplementingChild"));
			abstractChildLink
					.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "org:001"));
			dataGroup.addChild(abstractChildLink);
			recordList.add(dataGroup);
		}

		return recordList;
	}

	public static void addCreateAndUpdateInfoToRecordInfoInDataGroup(String userId,
			DataGroup dataGroup) {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		addUserIdToRecordInfoByNameInData(userId, recordInfo, "createdBy");
		LocalDateTime timestampCreated = LocalDateTime.of(2017, 10, 01, 00, 00, 00);
		addTimestampToRecordInfoByNameInData(timestampCreated, recordInfo, "tsCreated");
		addUserIdToRecordInfoByNameInData(userId, recordInfo, "updatedBy");
		LocalDateTime timestampUpdated = LocalDateTime.of(2017, 11, 03, 07, 25, 36);
		addTimestampToRecordInfoByNameInData(timestampUpdated, recordInfo, "tsUpdated");
	}

	private static void addTimestampToRecordInfoByNameInData(LocalDateTime timestamp,
			DataGroup recordInfo, String nameInData) {
		String dateTimeString = getLocalTimeDateAsString(timestamp);

		recordInfo.addChild(DataAtomic.withNameInDataAndValue(nameInData, dateTimeString));
	}

	private static void addUserIdToRecordInfoByNameInData(String userId, DataGroup recordInfo,
			String nameInData) {
		DataGroup createdOrUpdatedBy = DataGroup.withNameInData(nameInData);
		createdOrUpdatedBy.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "user"));
		createdOrUpdatedBy.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", userId));
		recordInfo.addChild(createdOrUpdatedBy);
	}

	private static String getLocalTimeDateAsString(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return localDateTime.format(formatter);
	}

	@Override
	public Collection<DataGroup> readAbstractList(String type, DataGroup filter) {
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

	@Override
	public Collection<DataGroup> getCollectTerms() {
		// TODO Auto-generated method stub
		return null;
	}
}
