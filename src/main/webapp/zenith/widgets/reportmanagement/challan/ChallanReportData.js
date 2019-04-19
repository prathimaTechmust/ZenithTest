function ChallanReportData()
{
	this.m_strSoldBy = "";
	this.m_strSoldTo = "";
	this.m_strChallanNumber = "";
	this.m_strDate = "";
	this.m_nAmount = 0;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		
		oElement = xmlDoc.createElement("ChallanReportData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "ChallanReportData", "m_strSoldBy", this.m_strSoldBy);
		addChild(xmlDoc, "ChallanReportData", "m_strSoldTo", this.m_strSoldTo);
		addChild(xmlDoc, "ChallanReportData", "m_strChallanNumber", this.m_strChallanNumber);
		addChild(xmlDoc, "ChallanReportData", "m_strDate", this.m_strDate);
		addChild(xmlDoc, "ChallanReportData", "m_nAmount", this.m_nAmount);
	}
}

dataObjectLoaded ();

