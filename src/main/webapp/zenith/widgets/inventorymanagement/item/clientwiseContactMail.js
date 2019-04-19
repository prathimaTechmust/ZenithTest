navigate ("newmessage", "widgets/crmanagement/emailMessage.js");
function emailMessage_loaded ()
{
	m_oEmailMessageMemberData.m_arrClientwiseData = m_oItemTransactionData.m_arrSelectedData;
	loadPage ("crmanagement/emailMessage.html", "secondDialog", "emailMessage_initContacts ()");
}