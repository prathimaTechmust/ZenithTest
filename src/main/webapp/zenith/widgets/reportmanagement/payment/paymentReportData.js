function PaymentReportData()
{
	this.m_strVendorName = "";
	this.m_nAmount = 0;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("PaymentReportData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "PaymentReportData", "m_strVendorName", this.m_strVendorName);
		addChild(xmlDoc, "PaymentReportData", "m_nAmount", this.m_nAmount);
	}
}

dataObjectLoaded ();