navigate ("reIssueCheque", "widgets/applicationstatus/disbursecheque/disbursechequeInfo.js");
function disburseStudentChequeInfo_loaded ()
{
	m_oDisburseStudentChequeInfo_MemberData.m_oStudentInformationData = m_odisburseChequeList_Info_MemberData.m_oStudentInformationData;
	loadPage ("applicationstatus/disburse/reIssueChequeRemark.html", "dialog", "reIssueCheque_new ()");
}

