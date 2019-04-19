navigate ("propertyType", "widgets/propertymanagement/propertytype/propertyType.js");

function propertyType_loaded ()
{
	m_oPropertyTypeMemberData.m_nPropertyId = m_oPropertyTypeListMemberData.m_nPropertyTypeId;
	loadPage ("propertymanagement/propertytype/propertyType.html", "dialog", "propertyType_edit ()");
}

