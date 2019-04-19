package com.techmust.clientmanagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.parsers.DocumentBuilderFactory;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataexchange.ChildKey;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "TCL01_Client")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name="CL01_clientType")
@DiscriminatorValue("client")
public class ClientData extends TenantData 
{
	@Id
	@Column(name="CL01_client_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int m_nClientId;
	@Column(name="CL01_name")
	protected String m_strCompanyName;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="CL02_demography_id")
	protected DemographyData m_oDemography;
	@Column(name = "CL01_address")
	protected String m_strAddress;
	@Column(name = "CL01_city")
	protected String m_strCity;
	@Column(name = "CL01_pincode")
	protected String m_strPinCode;
	@Column(name = "CL01_state")
	protected String m_strState;
	@Column(name = "CL01_country")
	protected String m_strCountry;
	@Column(name = "CL01_telephone")
	protected String m_strTelephone;
	@Column(name = "CL01_mobile_number")
	protected String m_strMobileNumber;
	@Column(name = "CL01_email_address")
	protected String m_strEmail;
	@Column(name = "CL01_web_address")
	protected String m_strWebAddress;
	@Column(name = "CL01_tin_number")
	protected String m_strTinNumber;
	@Column(name = "CL01_vat_number")
	protected String m_strVatNumber;
	@Column(name="CL01_cst_number")
	protected String m_strCSTNumber;
	@Transient
	protected Date m_dCreationDate;
	@Transient
	protected Date m_dUpdationDate;
	@Column(name = "CL01_PASSWORD")
	protected String m_strPassword;
	@Transient
	protected String m_strNewPassword;
	@Column(name="CL01_Allow_Online_Access")
	@ColumnDefault("0")
	protected boolean m_bAllowOnlineAccess;
	@Column(name="CL01_Client_Lock")
	@ColumnDefault("0")
	protected boolean m_bClientLock;
	@Column(name="CL01_OutstationClient")
	@ColumnDefault("0")
	protected boolean m_bOutstationClient;
	@Column(name="CL01_Verified")
	@ColumnDefault("0")
	protected boolean m_bVerified;
	@Column(name="CL01_opening_balance")
	@ColumnDefault("0")
	protected float m_nOpeningBalance;
	@Column(name="CL01_credit_limit")
	@ColumnDefault("0")
	protected float m_nCreditLimit;
	@Transient
	public ClientData [] m_arrClientBalanceData;
	@Column(name="CL01_AllowAutomaticPublishing")
	@ColumnDefault("0")
	protected boolean m_bAllowAutomaticPublishing;
	
	@JsonManagedReference
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="CL01_contact_id")  
	protected Set<ContactData> m_oContacts;
	@JsonManagedReference
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn( name="CL05_site_id")
	protected Set<SiteData> m_oSites;
	private static final long serialVersionUID = 1L;
	@Transient
	public static String kClient = "client";
	@Transient
	public ContactData [] m_arrContactData;
	@Transient
	public SiteData [] m_arrSiteData;

	
	public ClientData ()
	{
		m_nClientId = -1;
		m_strCompanyName = "";
		m_strAddress = "";
		m_strCity = "";
		m_strPinCode = "";
		m_strState = "";
		m_strCountry = "";
		m_strTelephone = "";
		m_strMobileNumber = "";
		m_strEmail = "";
		m_strWebAddress = "";
		m_strTinNumber = "";
		m_strVatNumber = "";
		m_strCSTNumber = "";
		m_dCreationDate = Calendar.getInstance ().getTime ();
		m_dUpdationDate = Calendar.getInstance ().getTime ();
		m_nOpeningBalance = 0;
		m_nCreditLimit = 0;
		m_oContacts = new HashSet<ContactData> ();
		m_oSites = new HashSet<SiteData> ();
		m_oDemography = new DemographyData ();
		m_bVerified = false;
		m_bAllowOnlineAccess = false;
		m_bClientLock = false;
		m_bAllowAutomaticPublishing = false;
	}

	public void setM_nClientId (int nClientId) 
	{
		m_nClientId = nClientId;
	}

	public int getM_nClientId () 
	{
		return m_nClientId;
	}
	
	public void setM_strCompanyName (String strCompanyName) 
	{
		m_strCompanyName = strCompanyName;
	}

	public String getM_strCompanyName () 
	{
		return m_strCompanyName;
	}
	
	public void setM_oDemography (DemographyData oDemography) 
	{
		m_oDemography = oDemography;
	}

	public DemographyData getM_oDemography () 
	{
		return m_oDemography;
	}

	public void setM_strAddress (String strAddress) 
	{
		m_strAddress = strAddress;
	}

	public String getM_strAddress () 
	{
		return m_strAddress;
	}

	public void setM_strCity (String strCity) 
	{
		m_strCity = strCity;
	}

	public String getM_strCity () 
	{
		return m_strCity;
	}

	public void setM_strPinCode (String strPinCode) 
	{
		m_strPinCode = strPinCode;
	}

	public String getM_strPinCode () 
	{
		return m_strPinCode;
	}

	public void setM_strState (String strState) 
	{
		m_strState = strState;
	}

	public String getM_strState () 
	{
		return m_strState;
	}

	public void setM_strCountry (String strCountry) 
	{
		m_strCountry = strCountry;
	}

	public String getM_strCountry () 
	{
		return m_strCountry;
	}

	public void setM_strTelephone (String strTelephone) 
	{
		m_strTelephone = strTelephone;
	}

	public String getM_strTelephone () 
	{
		return m_strTelephone;
	}
	
	public void setM_strMobileNumber (String strMobileNumber) 
	{
		m_strMobileNumber = strMobileNumber;
	}

	public String getM_strMobileNumber () 
	{
		return m_strMobileNumber;
	}

	public void setM_strEmail (String strEmail) 
	{
		m_strEmail = strEmail;
	}

	public String getM_strEmail () 
	{
		return m_strEmail;
	}

	public void setM_strWebAddress (String strWebAddress) 
	{
		m_strWebAddress = strWebAddress;
	}

	public String getM_strWebAddress () 
	{
		return m_strWebAddress;
	}
	
	public void setM_dCreationDate (Date dCreationDate) 
	{
		m_dCreationDate = dCreationDate;
	}

	public Date getM_dCreationDate ()
	{
		return m_dCreationDate;
	}
	
	public void setM_dUpdationDate (Date dUpdationDate) 
	{
		m_dUpdationDate = dUpdationDate;
	}

	public Date getM_dUpdationDate () 
	{
		return m_dUpdationDate;
	}
	
	public void setM_oContacts (Set<ContactData> oContacts) 
	{
		m_oContacts = oContacts;
	}

	public Set<ContactData> getM_oContacts () 
	{
		return m_oContacts;
	}
	
	public Set<SiteData> getM_oSites () 
	{
		return m_oSites;
	}

	public void setM_oSites (Set<SiteData> oSite) 
	{
		m_oSites = oSite;
	}
	
	public String getM_strPassword() 
	{
		return m_strPassword;
	}

	public void setM_strPassword(String strPassword) 
	{
		m_strPassword = strPassword;
	}

	public String getM_strNewPassword () 
	{
		return m_strNewPassword;
	}

	public void setM_strNewPassword (String strNewPassword) 
	{
		m_strNewPassword = strNewPassword;
	}

	public boolean isM_bAllowOnlineAccess() 
	{
		return m_bAllowOnlineAccess;
	}

	public void setM_bAllowOnlineAccess(boolean bAllowOnlineAccess) 
	{
		m_bAllowOnlineAccess = bAllowOnlineAccess;
	}

	public boolean isM_bVerified() 
	{
		return m_bVerified;
	}

	public void setM_bVerified(boolean bVerified) 
	{
		m_bVerified = bVerified;
	}

	public boolean isM_bClientLock() 
	{
		return m_bClientLock;
	}

	public void setM_bClientLock(boolean bClientLock) 
	{
		m_bClientLock = bClientLock;
	}

	public boolean isM_bOutstationClient() 
	{
		return m_bOutstationClient;
	}

	public void setM_bOutstationClient(boolean bOutstationClient) 
	{
		m_bOutstationClient = bOutstationClient;
	}

	public boolean isM_bAllowAutomaticPublishing()
	{
		return m_bAllowAutomaticPublishing;
	}

	public void setM_bAllowAutomaticPublishing(boolean bAllowAutomaticPublishing) 
	{
		m_bAllowAutomaticPublishing = bAllowAutomaticPublishing;
	}

	public float getM_nOpeningBalance() 
	{
		return m_nOpeningBalance;
	}

	public void setM_nOpeningBalance(float nOpeningBalance) 
	{
		m_nOpeningBalance = nOpeningBalance;
	}

	public float getM_nCreditLimit() 
	{
		return m_nCreditLimit;
	}

	public void setM_nCreditLimit(float nCreditLimit) 
	{
		m_nCreditLimit = nCreditLimit;
	}
	
	@Override
    public String generateXML ()
    {
		String strXML = "";
		m_oLogger.info ("generateXML");
	    try 
	    {
			Document oXmlDocument = createNewXMLDocument ();
			Document oDemographyXmlDoc = getXmlDocument ("<m_oDemography>"+m_oDemography.generateXML()+"</m_oDemography>");
			Document oContactsXmlDoc = getXmlDocument ("<m_oContacts>"+buildContactList (m_oContacts)+"</m_oContacts>");
			Document oSitesXmlDoc = getXmlDocument ("<m_oSites>"+buildSiteList(m_oSites)+"</m_oSites>");
			Element oRootElement = createRootElement (oXmlDocument, "ClientData");
			Node oDemographyNode = oXmlDocument.importNode (oDemographyXmlDoc.getFirstChild(), true);
			Node oContactsNode = oXmlDocument.importNode (oContactsXmlDoc.getFirstChild(), true);
			Node oSitesNode = oXmlDocument.importNode(oSitesXmlDoc.getFirstChild(), true);
			oRootElement.appendChild (oDemographyNode);
			oRootElement.appendChild (oContactsNode);
			oRootElement.appendChild(oSitesNode);
			addChild (oXmlDocument, oRootElement, "m_nClientId", m_nClientId);
			addChild (oXmlDocument, oRootElement, "m_strCompanyName", m_strCompanyName);
			addChild (oXmlDocument, oRootElement, "m_strAddress", m_strAddress);
			addChild (oXmlDocument, oRootElement, "m_strCity", m_strCity);
			addChild (oXmlDocument, oRootElement, "m_strPinCode", m_strPinCode);
			addChild (oXmlDocument, oRootElement, "m_strState", m_strState);
			addChild (oXmlDocument, oRootElement, "m_strCountry", m_strCountry);
			addChild (oXmlDocument, oRootElement, "m_strTelephone", m_strTelephone);
			addChild (oXmlDocument, oRootElement, "m_strMobileNumber", m_strMobileNumber);
			addChild (oXmlDocument, oRootElement, "m_strEmail", m_strEmail);
			addChild (oXmlDocument, oRootElement, "m_strWebAddress", m_strWebAddress);
			addChild (oXmlDocument, oRootElement, "m_strTinNumber", m_strTinNumber);
			addChild (oXmlDocument, oRootElement, "m_strVatNumber", m_strVatNumber);
			addChild (oXmlDocument, oRootElement, "m_strCSTNumber", m_strCSTNumber);
			addChild (oXmlDocument, oRootElement, "m_nOpeningBalance", m_nOpeningBalance);
			addChild (oXmlDocument, oRootElement, "m_nCreditLimit", m_nCreditLimit);
			addChild (oXmlDocument, oRootElement, "m_strPassword", m_strPassword);
			addChild (oXmlDocument, oRootElement, "m_bAllowOnlineAccess", m_bAllowOnlineAccess ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_bClientLock", m_bClientLock ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_bOutstationClient", m_bOutstationClient ? "Yes" : "No");
			strXML = getXmlString (oXmlDocument);
	    }
	    catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
	    return strXML;
    }

	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{	
		addClassCriteria (oCriteria);
		if (!m_strCompanyName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strCompanyName", m_strCompanyName.trim (), MatchMode.ANYWHERE));
		if (m_oDemography != null && m_oDemography.getM_nDemographyId () > 0)
			oCriteria.add (Restrictions.eq ("m_oDemography", m_oDemography));
		if (!m_strAddress.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strAddress", m_strAddress, MatchMode.ANYWHERE));
		if (!m_strAddress.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strAddress", m_strAddress, MatchMode.ANYWHERE));
		if (!m_strCity.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strCity", m_strCity.trim (), MatchMode.ANYWHERE));
		if (!m_strPinCode.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strPinCode", m_strPinCode, MatchMode.ANYWHERE));
		if (!m_strState.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strState", m_strState, MatchMode.ANYWHERE));
		if (!m_strCountry.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strCountry", m_strCountry, MatchMode.ANYWHERE));
		if (!m_strTelephone.isEmpty ())
			oCriteria.add(Restrictions.ilike ("m_strTelephone", m_strTelephone.trim (), MatchMode.ANYWHERE));
		if (!m_strEmail.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strEmail", m_strEmail.trim (), MatchMode.EXACT));
		if (!m_strWebAddress.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strWebAddress", m_strWebAddress, MatchMode.ANYWHERE));
		if (m_bOutstationClient)
			oCriteria.add (Restrictions.like ("m_bOutstationClient", m_bOutstationClient));
		if (m_bVerified)
			oCriteria.add (Restrictions.like ("m_bVerified", m_bVerified));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nClientId");
		return oCriteria;
	}
	
	protected void addClassCriteria (Criteria oCriteria)
	{
		oCriteria.add ((Restrictions.eq ("class", kClient)));
	}
	
	protected String buildContactList (Set<ContactData> oContacts) 
	{
		String strXML = "";
		Object [] arrContactData = oContacts.toArray ();
		for (int nIndex = 0; nIndex < arrContactData.length; nIndex ++)
		{
			ContactData oContactData = (ContactData) arrContactData [nIndex];
			strXML += oContactData.generateXML ();
		}
		return strXML;
	}
	
	protected String buildSiteList(Set<SiteData> oSites)
	{
		String strXML = "";
		Object [] arrSiteData = oSites.toArray ();
		for (int nIndex = 0; nIndex < arrSiteData.length; nIndex ++)
		{
			SiteData oSiteData = (SiteData) arrSiteData [nIndex];
			strXML += oSiteData.generateXML ();
		}
		return strXML;
	}

	public void setM_strTinNumber(String strTinNumber)
    {
	    this.m_strTinNumber = strTinNumber;
    }

	public String getM_strTinNumber()
    {
	    return m_strTinNumber;
    }

	public void setM_strVatNumber(String strVatNumber)
    {
	    this.m_strVatNumber = strVatNumber;
    }

	public String getM_strVatNumber()
    {
	    return m_strVatNumber;
    }

	public void setM_strCSTNumber(String strCSTNumber)
    {
	    this.m_strCSTNumber = strCSTNumber;
    }

	public String getM_strCSTNumber()
    {
	    return m_strCSTNumber;
    }

	public boolean delete() throws Exception 
	{
		boolean bDeleted = false;
		deleteContacts ();
		deleteSites ();
		ClientData oClientData = new ClientData ();
		oClientData.setM_nClientId(m_nClientId);
		bDeleted = oClientData.deleteObject();
		return bDeleted;
	}

	private void deleteSites() throws Exception 
	{
		Iterator<SiteData> oSites = m_oSites.iterator();
		while (oSites.hasNext())
			oSites.next().deleteObject();
	}

	private void deleteContacts() throws Exception 
	{
		Iterator<ContactData> oContacts = m_oContacts.iterator();
		while (oContacts.hasNext())
			oContacts.next().deleteObject();
	}

	public String generateLoginDetailsXML (String strPassword)
	{
		m_oLogger.info ("generateLoginDetailsXML");
		String strXML = "";
		try
		{
			Document oXMLDoc = createNewXMLDocument();
			Element oRootElement = createRootElement(oXMLDoc, "LoginDetails");
			addChild (oXMLDoc, oRootElement, "m_strEmail", m_strEmail);
			addChild (oXMLDoc, oRootElement, "m_strPassword", strPassword);
			strXML = getXmlString (oXMLDoc);
		}
		catch (Exception oException)
		{
			m_oLogger.error("generateLoginDetailsXML - oException : " + oException);
		}
	    m_oLogger.debug( "generateLoginDetailsXML - strXML [OUT] : " + strXML);
		return strXML;
	}

	public String generateForgotPasswordXML(String strPassword)
	{
		m_oLogger.info ("generateForgotPasswordXML");
		String strXML = "";
		try
		{
			Document oXMLDoc = createNewXMLDocument();
			Element oRootElement = createRootElement(oXMLDoc, "ForgotPasswordData");
			addChild (oXMLDoc, oRootElement, "m_strEmail", m_strEmail);
			addChild (oXMLDoc, oRootElement, "m_strPassword", strPassword);
			strXML = getXmlString (oXMLDoc);
		}
		catch (Exception oException)
		{
			m_oLogger.error("generateForgotPasswordXML - oException : " + oException);
		}
	    m_oLogger.debug( "generateForgotPasswordXML - strXML [OUT] : " + strXML);
		return strXML;
	} 

	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) throws Exception
	{
		m_oLogger.info ("getInstanceData");
		ClientData oClientData = new ClientData ();
		strXML = strXML.replaceAll(">\\s+<", "><").trim();
		Document oXMLDocument = getXmlDocument(strXML);
		NodeList oNodeList	= oXMLDocument.getChildNodes();
		 for (int nIndex = 0; nIndex < oNodeList.getLength(); nIndex++)
		    {
		    	Node oClientNode = oNodeList.item(nIndex);
	    		buildContacts (oClientNode, oClientData, oCredentials);
	    		oClientData.setM_oDemography((DemographyData) m_oDemography.getInstanceData(getDemographyData(oClientNode), oCredentials));
		    	buildSites (oClientNode, oClientData, oCredentials);
		    	buildClientData (oClientNode, oClientData);
		    }
		 if(isClientExists(oClientData))
				throw new Exception ("Duplicate");
		return oClientData;
	}
	
	public boolean isClientExists(ClientData oClientData) 
	{
		boolean bIsClientExists = false;
		   m_oLogger.info("isClientExists");
		   try
		   {
			   ClientData oData = new ClientData ();
			   ClientDataProcessor oClientDataProcessor = new ClientDataProcessor ();
			   oData.setM_strCompanyName(oClientData.getM_strCompanyName());
			   HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			   ClientDataResponse oResponse =(ClientDataResponse) oClientDataProcessor.list(oData, oOrderBy);
			   bIsClientExists = (oResponse.m_arrClientData.size() > 0);
		   }
		   catch (Exception oException)
		   {
			   m_oLogger.error("isClientExists - oException : " + oException);
		   }
		   m_oLogger.debug("isClientExists - bIsClientExists [OUT] : " + bIsClientExists);
		   return bIsClientExists;
	}

	private void buildSites(Node oClientNode, ClientData oClientData, UserInformationData oCredentials) throws Exception 
	{
		NodeList oNodeList = oClientNode.getChildNodes();
		for(int nIndex = 0; nIndex < ((NodeList) oNodeList).getLength(); nIndex++)
		{
			if(oNodeList.item(nIndex).getNodeName().equalsIgnoreCase("m_oSites"))
			{
				NodeList oSitesList = oNodeList.item(nIndex).getChildNodes();
				buildSitesSet(oSitesList, oClientData, oCredentials);
				break;
			}
		}
	}

	private void buildSitesSet(NodeList oSitesNodeList, ClientData oClientData, UserInformationData oCredentials) throws Exception 
	{
		for(int nIndex = 0; nIndex < ((NodeList) oSitesNodeList).getLength(); nIndex++)
		{
			if(oSitesNodeList.item(nIndex).getNodeName().equalsIgnoreCase("SiteData"))
			{
				Document oDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Node oSiteNode = oSitesNodeList.item(nIndex);
				oDocument.appendChild(oDocument.importNode(oSiteNode, true));
				String strXML = getXmlString(oDocument);
				SiteData oSiteData = new SiteData ();
				oClientData.m_oSites.add((SiteData)oSiteData.getInstanceData(strXML, oCredentials));
			}
		}
	}

	protected void buildContacts(Node oClientNode, ClientData oClientData, UserInformationData oCredentials) throws Exception 
	{
		NodeList oNodeList = oClientNode.getChildNodes();
		for(int nIndex = 0; nIndex < ((NodeList) oNodeList).getLength(); nIndex++)
		{
			if(oNodeList.item(nIndex).getNodeName().equalsIgnoreCase("m_oContacts"))
			{
				NodeList oContactsList = oNodeList.item(nIndex).getChildNodes();
				buildContactsSet(oContactsList, oClientData, oCredentials);
				break;
			}
		}
	}

	private void buildContactsSet(NodeList oContactsNodeList, ClientData oClientData, UserInformationData oCredentials) throws Exception 
	{
		for(int nIndex = 0; nIndex < ((NodeList) oContactsNodeList).getLength(); nIndex++)
		{
			if(oContactsNodeList.item(nIndex).getNodeName().equalsIgnoreCase("ContactData"))
			{
				Document oDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Node oContactNode = oContactsNodeList.item(nIndex);
				oDocument.appendChild(oDocument.importNode(oContactNode, true));
				String strXML = getXmlString(oDocument);
				ContactData oContactData = new ContactData ();
				oContactData = (ContactData)oContactData.getInstanceData(strXML, oCredentials);
				oContactData.setM_oClientData(oClientData);
				oClientData.m_oContacts.add(oContactData);
			}
		}
	}
	
	private String getDemographyData(Node oVendorNode) throws  Exception
	{
		String strXML = "";
		NodeList oNodeList = oVendorNode.getChildNodes();
		for(int nIndex = 0; nIndex < ((NodeList) oNodeList).getLength(); nIndex++)
		{
			if(oNodeList.item(nIndex).getNodeName().equalsIgnoreCase("m_oDemography"))
			{
				Document oDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Node oDemographyNode = oNodeList.item(nIndex).getFirstChild();
				oDocument.appendChild(oDocument.importNode(oDemographyNode, true));
				strXML = getXmlString(oDocument);
				break;
			}
		}
		return strXML;
	}
	
	private void buildClientData(Node oClientNode, ClientData oClientData) throws Exception 
	{
		oClientData.setM_strCompanyName(UserManagementInitializer.getValue(oClientNode, "m_strCompanyName"));
    	oClientData.setM_strAddress(UserManagementInitializer.getValue(oClientNode,"m_strAddress"));
    	oClientData.setM_strCity(UserManagementInitializer.getValue(oClientNode,"m_strCity"));
    	oClientData.setM_strPinCode(UserManagementInitializer.getValue(oClientNode,"m_strPinCode"));
    	oClientData.setM_strState(UserManagementInitializer.getValue(oClientNode,"m_strState"));
    	oClientData.setM_strCountry(UserManagementInitializer.getValue(oClientNode,"m_strCountry"));
    	oClientData.setM_strTelephone(UserManagementInitializer.getValue(oClientNode,"m_strTelephone"));
    	oClientData.setM_strMobileNumber (UserManagementInitializer.getValue(oClientNode,"m_strMobileNumber"));
    	oClientData.setM_strEmail(UserManagementInitializer.getValue(oClientNode,"m_strEmail"));
    	oClientData.setM_strWebAddress(UserManagementInitializer.getValue(oClientNode,"m_strWebAddress"));
    	oClientData.setM_strTinNumber (UserManagementInitializer.getValue(oClientNode,"m_strTinNumber"));
    	oClientData.setM_strVatNumber (UserManagementInitializer.getValue(oClientNode,"m_strVatNumber"));
    	oClientData.setM_strCSTNumber(UserManagementInitializer.getValue(oClientNode,"m_strCSTNumber"));
    	oClientData.setM_nOpeningBalance(Float.parseFloat(UserManagementInitializer.getValue(oClientNode,"m_nOpeningBalance")));
    	oClientData.setM_nCreditLimit(Float.parseFloat(UserManagementInitializer.getValue(oClientNode,"m_nCreditLimit")));
    	oClientData.setM_strPassword(UserManagementInitializer.getValue(oClientNode, "m_strPassword"));
    	oClientData.setM_bAllowOnlineAccess(UserManagementInitializer.getValue(oClientNode,"m_bAllowOnlineAccess").equalsIgnoreCase("yes") ? true : false);
    	oClientData.setM_bClientLock(UserManagementInitializer.getValue(oClientNode,"m_bClientLock").equalsIgnoreCase("yes") ? true : false);
    	oClientData.setM_bOutstationClient(UserManagementInitializer.getValue(oClientNode,"m_bOutstationClient").equalsIgnoreCase("yes") ? true : false);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap getHeaderKeys() 
	{
		HashMap oHeaderKey = new LinkedHashMap ();
		oHeaderKey.put("Company Name", "m_strCompanyName");
		oHeaderKey.put("Address", "m_strAddress");
		oHeaderKey.put("City", "m_strCity");
		oHeaderKey.put("PinCode", "m_strPinCode");
		oHeaderKey.put("State", "m_strState");
		oHeaderKey.put("Country", "m_strCountry");
		oHeaderKey.put("Telephone", "m_strTelephone");
		oHeaderKey.put("Mobile Number", "m_strMobileNumber");
		oHeaderKey.put("Email", "m_strEmail");
		oHeaderKey.put("Web Address", "m_strWebAddress");
		oHeaderKey.put("Tin Number", "m_strTinNumber");
		oHeaderKey.put("Vat Number", "m_strVatNumber");
		oHeaderKey.put("CSTNumber", "m_strCSTNumber");
		oHeaderKey.put("Opening Balance", "m_nOpeningBalance");
		oHeaderKey.put("Credit Limit", "m_nCreditLimit");
		oHeaderKey.put("Password", "m_strPassword");
		oHeaderKey.put("Allow Online Access", "m_bAllowOnlineAccess");
		oHeaderKey.put("Client Lock", "m_bClientLock");
		oHeaderKey.put("Out Station Client", "m_bOutstationClient");
		return oHeaderKey;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ChildKey> getChildKeys ()
	{
		ArrayList<ChildKey> arrChildKeys = new ArrayList<ChildKey>();
		ChildKey oDemographyKey = new ChildKey ();
		oDemographyKey.m_oChildKey.put("Demography" ,"m_oDemography");
		oDemographyKey.m_oChildKey.put("BusinessTypeData" ,"BusinessTypeData");
		oDemographyKey.m_oChildKey.put("BusinessTypeId" , "m_nBusinessTypeId");
		oDemographyKey.m_oChildKey.put("BusinessType Name" , "m_strBusinessName");
		ChildKey oContactsChildKey = new ChildKey ();
		oContactsChildKey.m_oChildKey.put("Contact", "m_oContacts");
		oContactsChildKey.m_oChildKey.put("ContactData", "ContactData");
		oContactsChildKey.m_oChildKey.put("ContactName", "m_strContactName");
		oContactsChildKey.m_oChildKey.put("PhoneNumber", "m_strPhoneNumber");
		oContactsChildKey.m_oChildKey.put("Email", "m_strEmail");
		oContactsChildKey.m_oChildKey.put("Department", "m_strDepartment");
		oContactsChildKey.m_oChildKey.put("Designation", "m_strDesignation");
		ChildKey oSitesKey = new ChildKey ();
		oSitesKey.m_oChildKey.put("Sites", "m_oSites");
		oSitesKey.m_oChildKey.put("SiteData", "SiteData");
		oSitesKey.m_oChildKey.put("SiteName", "m_strSiteName");
		oSitesKey.m_oChildKey.put("SiteAddress", "m_strSiteAddress");
		oSitesKey.m_oChildKey.put("SiteStatus", "m_nSiteStatus");
		arrChildKeys.add(oContactsChildKey);
		arrChildKeys.add(oSitesKey);
		arrChildKeys.add(oDemographyKey);
		return arrChildKeys;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
//		addClassCriteria (oCriteria);
		if (!m_strCompanyName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCompanyName")), m_strCompanyName.toLowerCase())); 
		if (m_oDemography != null && m_oDemography.getM_nDemographyId () > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nDemographyId"), m_oDemography.getM_nDemographyId ())); 
		if (!m_strAddress.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strAddress")), m_strAddress.toLowerCase())); 
		if (!m_strCity.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCity")), m_strCity.toLowerCase())); 
		if (!m_strPinCode.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strPinCode")), m_strPinCode.toLowerCase())); 
		if (!m_strState.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strState")), m_strState.toLowerCase())); 
		if (!m_strCountry.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCountry")), m_strCountry.toLowerCase())); 
		if (!m_strTelephone.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strTelephone")), m_strTelephone.toLowerCase())); 
		if (!m_strEmail.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strEmail")), m_strEmail.toLowerCase())); 
		if (!m_strWebAddress.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strWebAddress")), m_strWebAddress.toLowerCase())); 
		if (m_bOutstationClient)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_bOutstationClient"), m_bOutstationClient)); 
		if (m_bVerified)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_bVerified"), m_bVerified)); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nClientId");

		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_strPassword != null && !m_strPassword.isEmpty ())
		    oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strPassword"), m_strPassword));
		if (m_strEmail != null && !m_strEmail.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strEmail"), m_strEmail));
		if (m_nClientId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nClientId"), m_nClientId));
		return oConjunct;
	}
}