function LocalPurchaseData()
{
	this.m_strArticleDescription = "";
	this.m_nQty = 0;
	this.m_nShippedQty = 0;
	this.m_nBalance = 0;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("LocalPurchaseData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "LocalPurchaseData", "m_strArticleDescription", this.m_strArticleDescription);
		addChild(xmlDoc, "LocalPurchaseData", "m_nQty", this.m_nQty);
		addChild(xmlDoc, "LocalPurchaseData", "m_nShippedQty", this.m_nShippedQty);
		addChild(xmlDoc, "LocalPurchaseData", "m_nBalance", this.m_nBalance);
	}
}

dataObjectLoaded ();
