navigate("preparecheque","widgets/applicationstatus/preparecheque/prepareChequeInfo.js");

function prepareChequeInfo_Loaded ()
{
	m_oPrepareChequeInfoMemberData.m_oStudentInformationData = m_oPrepareChequeListMemberData.m_oStudentInformationData;	
	loadPage("applicationstatus/preparecheque/preparechequeInfo.html","dialog","prepareChequeInfo_new ()");
}