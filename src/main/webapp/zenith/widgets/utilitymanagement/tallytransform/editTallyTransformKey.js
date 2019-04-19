navigate ("tallyTranformKey", "widgets/utilitymanagement/tallytransform/tallyTransformKey.js");

function tallyTransformKey_loaded ()
{
	m_oTallyTransformKeyMemberData.m_nTallyTranformKeyId = m_oTallyTransformKeyListMemberData.m_nTallyTranformKeyId;
	loadPage ("utilitymanagement/tallytransform/tallyTransformKey.html", "dialog", "tallyTransformKey_edit ()");
}