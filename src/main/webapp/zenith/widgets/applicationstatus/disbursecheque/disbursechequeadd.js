navigate ("disbursecheque", "widgets/applicationstatus/disbursecheque/disbursechequeInfo.js");
function disburseStudentChequeInfo_loaded ()
{
	m_oDisburseStudentChequeInfo_MemberData.m_oStudentInformationData = m_odisburseChequeList_Info_MemberData.m_oStudentInformationData;
	loadPage ("applicationstatus/disburse/disburseStudentCheque.html", "dialog", "disburseCheque_new ()");
}


