navigate ("newTallyTranform", "widgets/utilitymanagement/tallytransform/tallyTransform.js");

function tallyTransform_loaded ()
{
	m_oTallyTransformMemberData.m_nTallyTranformId = m_oTallyTransformListMemberData.m_nTallyTranformId;
	loadPage ("utilitymanagement/tallytransform/tallyTransform.html", "dialog", "tallyTransform_edit ()");
}