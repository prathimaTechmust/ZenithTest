function InventoryReportData()
{
	this.m_strArticleNumber = "";
	this.m_strItemName = "";
	this.m_strBrand = "";
	this.m_strDetail = "";
	this.m_nCurrentStock = 0;
	this.m_nSellingPrice = 0;
	this.m_nAmount = 0;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("InventoryReportData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "InventoryReportData", "m_strArticleNumber", this.m_strArticleNumber);
		addChild(xmlDoc, "InventoryReportData", "m_strItemName", this.m_strItemName);
		addChild(xmlDoc, "InventoryReportData", "m_strBrand", this.m_strBrand);
		addChild(xmlDoc, "InventoryReportData", "m_strDetail", this.m_strDetail);
		addChild(xmlDoc, "InventoryReportData", "m_nCurrentStock", this.m_nCurrentStock);
		addChild(xmlDoc, "InventoryReportData", "m_nSellingPrice", this.m_nSellingPrice);
		addChild(xmlDoc, "InventoryReportData", "m_nAmount", this.m_nAmount);
	}
}

dataObjectLoaded ();