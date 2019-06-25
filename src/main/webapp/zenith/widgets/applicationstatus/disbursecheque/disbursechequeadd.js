navigate ("disbursecheque", "widgets/applicationstatus/disbursecheque/disbursechequeInfo.js");
function disburseStudentChequeInfo_loaded ()
{
	m_oDisburseStudentChequeInfo_MemberData.m_nStudentId = m_odisburseChequeList_Info_MemberData.m_nStudentId;
	m_oDisburseStudentChequeInfo_MemberData.m_nUID = m_odisburseChequeList_Info_MemberData.m_nUID;
	m_oDisburseStudentChequeInfo_MemberData.m_strStudentName = m_odisburseChequeList_Info_MemberData.m_strStudentName;
	m_oDisburseStudentChequeInfo_MemberData.m_nChequeNumber = m_odisburseChequeList_Info_MemberData.m_nChequeNumber;
	loadPage ("applicationstatus/disburse/disburseStudentCheque.html", "dialog", "disburseCheque_new ()");
}
