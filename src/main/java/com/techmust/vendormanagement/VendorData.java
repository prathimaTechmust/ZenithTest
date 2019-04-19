package com.techmust.vendormanagement;

import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.DemographyData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.dataexchange.ChildKey;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@DiscriminatorValue("Vendor")
public class VendorData extends ClientData 
{
	private static final long serialVersionUID = 1L;
	public static String kVendor = "Vendor";
	@Column(name = "CL01_shope_name")
	protected String m_strShopeName;
	@Transient
	protected String m_strShopeDesc;
	@Column(name = "CL01_remarks")
	protected String m_strRemarks;
	@Column(name = "CL01_vendor_logo")
	@Lob
	private Blob m_oLogo;
	@Transient
	private BufferedImage m_buffImgLogo;
	
	public VendorData ()
	{
		m_strShopeName = "";
		m_strRemarks = "";
	}
	
	public String getM_strShopeName ()
	{
		return m_strShopeName;
	}

	public void setM_strShopeName (String strShopeName)
	{
		m_strShopeName = strShopeName;
	}

	public String getM_strShopeDesc () 
	{
		return m_strShopeDesc;
	}

	public void setM_strShopeDesc (String strShopeDesc) 
	{
		m_strShopeDesc = strShopeDesc;
	}

	public String getM_strRemarks () 
	{
		return m_strRemarks;
	}

	public void setM_strRemarks (String strRemarks) 
	{
		m_strRemarks = strRemarks;
	}

	public BufferedImage getM_buffImgLogo () 
	{
		return m_buffImgLogo;
	}

	public void setM_buffImgLogo (BufferedImage buffImgLogo) 
	{
		m_buffImgLogo = buffImgLogo;
	}

	public Blob getM_oLogo() 
	{
		return m_oLogo;
	}

	public void setM_oLogo(Blob logo) 
	{
		m_oLogo = logo;
	}

	public String generateXML()
	{
		String strXML = "";
		m_oLogger.info("generateXML");
	    try 
	    {
			Document oXmlDocument = createNewXMLDocument ();
			Document oDemographyXmlDoc = getXmlDocument ("<m_oDemography>"+m_oDemography.generateXML()+"</m_oDemography>");
			Document oContactsXmlDoc = getXmlDocument ("<m_oContacts>"+buildContactList (m_oContacts)+"</m_oContacts>");
			Element oRootElement = createRootElement (oXmlDocument, "VendorData");
			Node oDemographyNode = oXmlDocument.importNode(oDemographyXmlDoc.getFirstChild(), true);
			Node oContactsNode = oXmlDocument.importNode(oContactsXmlDoc.getFirstChild(), true);
			oRootElement.appendChild(oDemographyNode);
			oRootElement.appendChild(oContactsNode);
			addChild (oXmlDocument, oRootElement, "m_nVendorId", m_nClientId);
			addChild (oXmlDocument, oRootElement, "m_strVendorCompanyName", m_strCompanyName);
			addChild (oXmlDocument, oRootElement, "m_strVendorAddress", m_strAddress);
			addChild (oXmlDocument, oRootElement, "m_strVendorCity", m_strCity);
			addChild (oXmlDocument, oRootElement, "m_strVendorPinCode", m_strPinCode);
			addChild (oXmlDocument, oRootElement, "m_strVendorState", m_strState);
			addChild (oXmlDocument, oRootElement, "m_strVendorCountry", m_strCountry);
			addChild (oXmlDocument, oRootElement, "m_strVendorTelephone", m_strTelephone);
			addChild (oXmlDocument, oRootElement, "m_strVendorMobileNumber", m_strMobileNumber);
			addChild (oXmlDocument, oRootElement, "m_strVendorEmail", m_strEmail);
			addChild (oXmlDocument, oRootElement, "m_strVendorWebAddress", m_strWebAddress);
			addChild (oXmlDocument, oRootElement, "m_strVendorTinNumber", m_strTinNumber);
			addChild (oXmlDocument, oRootElement, "m_strVendorVatNumber", m_strVatNumber);
			addChild (oXmlDocument, oRootElement, "m_strVendorCSTNumber", m_strCSTNumber);
			addChild (oXmlDocument, oRootElement, "m_bAllowOnlineAccess", m_bAllowOnlineAccess ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_nVendorOpeningBalance", m_nOpeningBalance);
			addChild (oXmlDocument, oRootElement, "m_bClientLock", m_bClientLock ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_bOutstationClient", m_bOutstationClient ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_nCreditLimit", m_nCreditLimit);
			addChild (oXmlDocument, oRootElement, "m_bVerified", m_bVerified ? "Yes" : "No");
			addChild (oXmlDocument, oRootElement, "m_bAllowAutomaticPublishing", m_bAllowAutomaticPublishing ? "Yes" : "No");
			strXML = getXmlString (oXmlDocument);
	    }
	    catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
	    return strXML;
    }
	
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) throws Exception
	{
		m_oLogger.info("getInstance");
		VendorData oVendorData = new VendorData ();
		try
		{
			strXML = strXML.replaceAll(">\\s+<", "><").trim();
			Document oXMLDocument = getXmlDocument(strXML);
			NodeList oVendorNode = oXMLDocument.getChildNodes();;
			for(int nIndex = 0; nIndex < oVendorNode.getLength(); nIndex++)
			{
				Node oVendorNodeList = oVendorNode.item(nIndex);
				oVendorData.setM_oDemography((DemographyData) m_oDemography.getInstanceData((getDemographyData(oVendorNodeList)), oCredentials));
				buildContacts (oVendorNodeList, oVendorData, oCredentials);
				oVendorData = buildVendorData (oVendorNodeList, oVendorData);
			}
			if(isVendorExists(oVendorData))
				throw new Exception ("Duplicate");
		}
		catch(Exception oException)
		{
			m_oLogger.error("generateXML - oException : " + oException);
			throw oException;
		}
		return oVendorData;
	}
	
	public boolean isVendorExists(VendorData oVendorData) 
	{
		boolean bIsVendorExists = false;
		   m_oLogger.info("isVendorExists");
		   try
		   {
			   VendorData oData = new VendorData ();
			   VendorDataProcessor oVendorDataProcessor = new VendorDataProcessor ();
			   oData.setM_strCompanyName(oVendorData.getM_strCompanyName());
			   VendorDataResponse oResponse =(VendorDataResponse) oVendorDataProcessor.listVendor(oData, "", "");
			   bIsVendorExists = (oResponse.m_arrVendorData.size() > 0);
		   }
		   catch (Exception oException)
		   {
			   m_oLogger.error("isVendorExists - oException : " + oException);
		   }
		   m_oLogger.debug("isVendorExists - bIsVendorExists [OUT] : " + bIsVendorExists);
		   return bIsVendorExists;
	}
	
	private VendorData buildVendorData(Node oVendorNodeList, VendorData oVendorData) throws Exception 
	{
		oVendorData.setM_strCompanyName(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorCompanyName"));
		oVendorData.setM_strAddress(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorAddress"));
		oVendorData.setM_strCity(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorCity"));
		oVendorData.setM_strPinCode(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorPinCode"));
		oVendorData.setM_strState(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorState "));
		oVendorData.setM_strCountry(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorCountry "));
		oVendorData.setM_strTelephone(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorTelephone"));
		oVendorData.setM_strMobileNumber(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorMobileNumber "));
		oVendorData.setM_strEmail(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorEmail"));
		oVendorData.setM_strWebAddress(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorWebAddress"));
		oVendorData.setM_strTinNumber(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorTinNumber"));
		oVendorData.setM_strVatNumber(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorVatNumber"));
		oVendorData.setM_strCSTNumber(UserManagementInitializer.getValue(oVendorNodeList, "m_strVendorCSTNumber"));
		oVendorData.setM_bAllowOnlineAccess(UserManagementInitializer.getValue(oVendorNodeList,"m_bAllowOnlineAccess").equalsIgnoreCase("yes") ? true : false);
		oVendorData.setM_nOpeningBalance(Float.parseFloat(UserManagementInitializer.getValue(oVendorNodeList, "m_nVendorOpeningBalance")));
		oVendorData.setM_bClientLock(UserManagementInitializer.getValue(oVendorNodeList,"m_bClientLock").equalsIgnoreCase("yes") ? true : false);
		oVendorData.setM_bOutstationClient(UserManagementInitializer.getValue(oVendorNodeList,"m_bOutstationClient").equalsIgnoreCase("yes") ? true : false);
		oVendorData.setM_nCreditLimit(Float.parseFloat(UserManagementInitializer.getValue(oVendorNodeList, "m_nCreditLimit")));
		return oVendorData;
	}
	
	private String getDemographyData(Node oVendorNode) throws  Exception
	{
		String strXML = "";
		Document oDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		NodeList oNodeList = oVendorNode.getChildNodes();
		for(int nIndex = 0; nIndex < ((NodeList) oNodeList).getLength(); nIndex++)
		{
			if(oNodeList.item(nIndex).getNodeName().equalsIgnoreCase("m_oDemography"))
			{
				Node oDemographyNode = oNodeList.item(nIndex).getFirstChild();
				oDocument.appendChild(oDocument.importNode(oDemographyNode, true));
				strXML = getXmlString(oDocument);
				break;
			}
		}
		return strXML;
	}

	protected void addClassCriteria (Criteria oCriteria) 
	{	
		oCriteria.add((Restrictions.eq("class", kVendor)));
	}

	public String generateVendorDataXML(ArrayList<VendorData> arrVendor) 
	{
		String strVendorDataXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Document oVendorXmlDoc = getXmlDocument ("<VendorList>" + buildVendorXml (arrVendor) + "</VendorList>");
			Node oVendorNode = oXmlDocument.importNode (oVendorXmlDoc.getFirstChild (), true);
			oXmlDocument.appendChild(oVendorNode);
			strVendorDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateVendorDataXML - oException" + oException);
		}
		m_oLogger.debug("generateVendorDataXML - strVendorDataXml [OUT] : " + strVendorDataXml);
		return strVendorDataXml;
	}

	private String buildVendorXml(ArrayList<VendorData> arrVendor) 
	{
		m_oLogger.debug("buildVendorXml");
		String strXml = "";
		for(int nIndex = 0; nIndex < arrVendor.size(); nIndex++)
		{
			strXml += arrVendor.get(nIndex).generateXML();
		}
	    return strXml;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap getHeaderKeys() 
	{
		HashMap oHeaderKey = new LinkedHashMap ();
		oHeaderKey.put("Company Name", "m_strVendorCompanyName");
		oHeaderKey.put("Address", "m_strVendorAddress");
		oHeaderKey.put("City", "m_strVendorCity");
		oHeaderKey.put("PinCode", "m_strVendorPinCode");
		oHeaderKey.put("State", "m_strVendorState");
		oHeaderKey.put("Country", "m_strVendorCountry");
		oHeaderKey.put("Telephone", "m_strVendorTelephone");
		oHeaderKey.put("Mobile Number", "m_strVendorMobileNumber");
		oHeaderKey.put("Email", "m_strVendorEmail");
		oHeaderKey.put("Web Address", "m_strVendorWebAddress");
		oHeaderKey.put("Tin Number", "m_strVendorTinNumber");
		oHeaderKey.put("Vat Number", "m_strVendorVatNumber");
		oHeaderKey.put("CSTNumber", "m_strVendorCSTNumber");
		oHeaderKey.put("Allow Online Access", "m_bAllowOnlineAccess");
		oHeaderKey.put("Opening Balance", "m_nVendorOpeningBalance");
		oHeaderKey.put("Client Lock", "m_bClientLock");
		oHeaderKey.put("Out Station Client", "m_bOutstationClient");
		oHeaderKey.put("Credit Limit", "m_nCreditLimit");
		return oHeaderKey;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ChildKey> getChildKeys ()
	{
		ArrayList<ChildKey> arrChildKeys = new ArrayList<ChildKey>();
		ChildKey oDemographyKey = new ChildKey ();
		oDemographyKey.m_oChildKey.put("Demography" ,"m_oDemography");
		oDemographyKey.m_oChildKey.put("BusinessTypeData" , "BusinessTypeData");
		oDemographyKey.m_oChildKey.put("BusinessTypeId" , "m_nBusinessTypeId");
		oDemographyKey.m_oChildKey.put("BusinessType Name" , "m_strBusinessName");
		ChildKey oContactsChildKey = new ChildKey ();
		oContactsChildKey.m_oChildKey.put("Contacts", "m_oContacts");
		oContactsChildKey.m_oChildKey.put("ContactData", "ContactData");
		oContactsChildKey.m_oChildKey.put("ContactName", "m_strContactName");
		oContactsChildKey.m_oChildKey.put("PhoneNumber", "m_strPhoneNumber");
		oContactsChildKey.m_oChildKey.put("Email", "m_strEmail");
		oContactsChildKey.m_oChildKey.put("Department", "m_strDepartment");
		oContactsChildKey.m_oChildKey.put("Designation", "m_strDesignation");
		arrChildKeys.add(oContactsChildKey);
		arrChildKeys.add(oDemographyKey);
		return arrChildKeys;
	}
}
