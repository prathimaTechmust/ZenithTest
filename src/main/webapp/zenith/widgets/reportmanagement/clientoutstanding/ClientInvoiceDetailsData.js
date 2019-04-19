function ClientInvoiceDetailsData()
{
	this.m_strDate = "";
	this.m_strInvoiceNumber = "";
	this.m_nInvoiceAmount = 0;
	this.m_nReceiptAmount = 0;
	this.m_nBalanceAmount = 0;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("ClientInvoiceDetailsData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "ClientInvoiceDetailsData", "m_strDate", this.m_strDate);
		addChild(xmlDoc, "ClientInvoiceDetailsData", "m_strInvoiceNumber", this.m_strInvoiceNumber);
		addChild(xmlDoc, "ClientInvoiceDetailsData", "m_nInvoiceAmount", this.m_nInvoiceAmount);
		addChild(xmlDoc, "ClientInvoiceDetailsData", "m_nReceiptAmount", this.m_nReceiptAmount);
		addChild(xmlDoc, "ClientInvoiceDetailsData", "m_nBalanceAmount", this.m_nBalanceAmount);
	}
}

dataObjectLoaded ();