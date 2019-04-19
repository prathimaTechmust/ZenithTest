function ClientData ()
{
	this.m_nClientId = -1;
	this.m_strCompanyName = "";
	this.m_strAddress = "";
	this.m_strCity = "";
	this.m_strPinCode = "";
	this.m_strState = "";
	this.m_strCountry = "";
	this.m_strTelephone = "";
	this.m_strMobileNumber = "";
	this.m_strEmail = "";
	this.m_strWebAddress = "";
	this.m_strTinNumber = "";
	this.m_strVatNumber = "";
	this.m_strCSTNumber = "";
	this.m_strPassword = "";
	this.m_strNewPassword = "";
	this.m_bAllowOnlineAccess = false;
	this.m_bClientLock = false;
	this.m_bOutstationClient = false;
	this.m_nOpeningBalance = 0;
	this.m_nCreditLimit = 0;
	this.m_arrContactData = [];
	this.m_arrSiteData = [];
	}
dataObjectLoaded ();