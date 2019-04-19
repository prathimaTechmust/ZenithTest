function ItemData ()
{
	this.m_nItemId = -1;
	this.m_oCreatedBy = new UserInformationData ();
	this.m_strArticleNumber = "";
	this.m_strItemName = ""; 
	this.m_strBrand = "";
	this.m_strDetail = "";
	this.m_strUnit = "No.";
	this.m_buffImgPhoto = null;
	this.m_nOpeningStock = 0;
	this.m_nReorderLevel = 0;
	this.m_nSellingPrice = 0;
	this.m_nCostPrice = 0;
	this.m_nReceived = 0;
	this.m_nIssued = 0;
	this.m_nCurrentStock = 0;
	this.m_oApplicableTax = new ApplicableTaxData ();
	this.m_oTaxWithCForm = new ApplicableTaxData ();
	this.m_strLocationCode = "";
	this.m_oUserCredentialsData = new UserInformationData ();
	this.m_bPublishOnline = false;
	this.m_arrCartItems = new Array ();
	this.m_arrItemGroups = new Array ();
	this.m_arrChildItems = new Array ();
	this.m_arrItemImages = new Array ();
	this.m_strImageName ="";
	this.m_bIsForReOrderList = false;
	
	this.generateXML = function generateXML(xmlDoc, strParentElement)
	{
		oElement = xmlDoc.createElement("ItemData");
		oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
		oRootElement.appendChild(oElement);
		addChild(xmlDoc, "ItemData", "m_strArticleNumber", this.m_strArticleNumber);
		addChild(xmlDoc, "ItemData", "m_strItemName", this.m_strItemName);
		addChild(xmlDoc, "ItemData", "m_strBrand", this.m_strBrand);
		addChild(xmlDoc, "ItemData", "m_strDetail", this.m_strDetail);
		addChild(xmlDoc, "ItemData", "m_nReorderLevel", this.m_nReorderLevel);
		addChild(xmlDoc, "ItemData", "m_nCurrentStock", this.m_nCurrentStock);
	}
}

dataObjectLoaded ();