function PurchaseReportData()
{
	this.m_strPurchasedBy = "";
	this.m_strPurchasedFrom = "";
	this.m_strInvoiceNo = "";
	this.m_strDate = "";
	this.m_nAmount = 0;
	this.m_strCompanyName = "";
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("PurchaseReportData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "PurchaseReportData", "m_strPurchasedBy", this.m_strPurchasedBy);
		addChild(xmlDoc, "PurchaseReportData", "m_strPurchasedFrom", this.m_strPurchasedFrom);
		addChild(xmlDoc, "PurchaseReportData", "m_strInvoiceNo", this.m_strInvoiceNo);
		addChild(xmlDoc, "PurchaseReportData", "m_strDate", this.m_strDate);
		addChild(xmlDoc, "PurchaseReportData", "m_nAmount", this.m_nAmount);
		addChild(xmlDoc, "PurchaseReportData", "m_strCompanyName", this.m_strCompanyName);
	}
}

dataObjectLoaded ();