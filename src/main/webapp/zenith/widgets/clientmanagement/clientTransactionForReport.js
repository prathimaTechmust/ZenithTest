navigate ("clientTransaction", "widgets/clientmanagement/clientTransaction.js");

function clientTransaction_loaded ()
{
	m_oClientTransactionMemeberData.m_oClientData = m_oClientOutstandingMemberData.m_oClientData;
	m_oClientTransactionMemeberData.m_oSelectedClientId = m_oClientOutstandingMemberData.m_oSelectedClientId;
	loadPage ("clientmanagement/clientTransaction.html", "dialog", "clientTransaction_init()");
}