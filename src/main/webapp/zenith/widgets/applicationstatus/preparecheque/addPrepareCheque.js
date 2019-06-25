navigate("preparecheque","widgets/applicationstatus/preparecheque/prepareChequeInfo.js");

function prepareChequeInfo_Loaded ()
{
	m_oPrepareChequeInfoMemberData.m_nUID = m_oPrepareChequeListMemberData.m_nUID;
	m_oPrepareChequeInfoMemberData.m_StudentName = m_oPrepareChequeListMemberData.m_strStudentName; 
	m_oPrepareChequeInfoMemberData.m_nStudentId = m_oPrepareChequeListMemberData.m_nStudentId;
	loadPage("applicationstatus/preparecheque/preparechequeInfo.html","dialog","prepareChequeInfo_new ()");
}