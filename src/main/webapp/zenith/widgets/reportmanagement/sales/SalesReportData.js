function SalesReportData()
{
	this.m_strSoldBy = "";
	this.m_strSoldTo = "";
	this.m_strInvoiceNo = "";
	this.m_strDate = "";
	this.m_nAmount = 0;
	this.m_strCompanyName = "";
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("SalesReportData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "SalesReportData", "m_strSoldBy", this.m_strSoldBy);
		addChild(xmlDoc, "SalesReportData", "m_strSoldTo", this.m_strSoldTo);
		addChild(xmlDoc, "SalesReportData", "m_strInvoiceNo", this.m_strInvoiceNo);
		addChild(xmlDoc, "SalesReportData", "m_strDate", this.m_strDate);
		addChild(xmlDoc, "SalesReportData", "m_nAmount", this.m_nAmount);
		addChild(xmlDoc, "SalesReportData", "m_strCompanyName", this.m_strCompanyName);
	}
}

dataObjectLoaded ();

