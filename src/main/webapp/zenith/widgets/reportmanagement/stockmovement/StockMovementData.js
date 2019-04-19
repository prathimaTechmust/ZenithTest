function StockMovementData()
{
	this.m_strArticleNumber = "";
	this.m_strItemName = "";
	this.m_strBrand = "";
	this.m_strDetail = "";
	this.m_nReceivedQuantity = 0;
	this.m_nIssuedQuantity = 0;
	this.m_nBalanceQuantity = 0;
	this.m_nCurrentStock = 0;
	this.m_nCurrentValue = 0;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("StockMovementData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "StockMovementData", "m_strArticleNumber", this.m_strArticleNumber);
		addChild(xmlDoc, "StockMovementData", "m_strItemName", this.m_strItemName);
		addChild(xmlDoc, "StockMovementData", "m_strBrand", this.m_strBrand);
		addChild(xmlDoc, "StockMovementData", "m_strDetail", this.m_strDetail);
		addChild(xmlDoc, "StockMovementData", "m_nReceived", this.m_nReceivedQuantity);
		addChild(xmlDoc, "StockMovementData", "m_nIssued", this.m_nIssuedQuantity);
		addChild(xmlDoc, "StockMovementData", "m_nBalanceQuantity", this.m_nBalanceQuantity);
		addChild(xmlDoc, "StockMovementData", "m_nCurrentStock", this.m_nCurrentStock);
		addChild(xmlDoc, "StockMovementData", "m_nCurrentValue", this.m_nCurrentValue);
	}
}

dataObjectLoaded ();