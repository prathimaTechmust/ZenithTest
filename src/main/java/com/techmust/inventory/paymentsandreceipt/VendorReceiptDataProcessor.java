package com.techmust.inventory.paymentsandreceipt;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.VendorItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

public class VendorReceiptDataProcessor extends ReceiptDataProcessor 
{
	private VendorData m_oVendorData;
	
	public GenericResponse createVendorReceipt (ReceiptData oReceiptData, VendorData oVendorData) throws Exception
	{
		m_oLogger.info ("createVendorReceipt");
		m_oLogger.debug ("createVendorReceipt - oReceiptData [IN] :: " + oReceiptData);
		ReceiptDataResponse oReceiptDataResponse = new ReceiptDataResponse ();
		OnlineReceiptData oOnlineReceiptData = new OnlineReceiptData ();
		OnlineReceiptDataProcessor oOnlineReceiptDataProcessor = new OnlineReceiptDataProcessor ();
		try
		{
			m_oVendorData = oVendorData;
			oReceiptData.setM_oUserCredentialsData(getAdminUserInfo ());
			oReceiptData.setM_nCreatedBy(oReceiptData.getM_oUserCredentialsData().getM_nUserId());
			oReceiptDataResponse = (ReceiptDataResponse) super.create(oReceiptData);
			oOnlineReceiptData.getM_oVendorData().setM_nClientId(oVendorData.getM_nClientId());
			oOnlineReceiptData.getM_oReceiptData().setM_nReceiptId(oReceiptDataResponse.m_arrReceiptData.get(0).getM_nReceiptId());
			oOnlineReceiptDataProcessor.create(oOnlineReceiptData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createVendorReceipt - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("createVendorReceipt - oReceiptDataResponse.m_bSuccess [OUT] : " + oReceiptDataResponse.m_bSuccess);
		return oReceiptDataResponse;
	}
	
	private UserInformationData getAdminUserInfo ()
	{
		m_oLogger.info ("getAdminUserInfo");
		UserInformationData oUserData = new UserInformationData ();
		try
		{	
			oUserData.setM_strUserName("admin");
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oUserData = (UserInformationData) oUserData.list(oOrderBy).get(0);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getAdminUserInfo - oException : " + oException);
		}
		return oUserData;
	}

	public GenericResponse saveAndPrintReceipt (ReceiptData oReceiptData, VendorData oVendorData) throws Exception
	{
		m_oLogger.info ("makeReceipt");
		m_oLogger.debug ("makeReceipt - oReceiptData [IN] :: " + oReceiptData);
		ReceiptDataResponse oReceiptDataResponse = new ReceiptDataResponse ();
		OnlineReceiptData oOnlineReceiptData = new OnlineReceiptData ();
		OnlineReceiptDataProcessor oOnlineReceiptDataProcessor = new OnlineReceiptDataProcessor ();
		try
		{
			m_oVendorData = oVendorData;
			oReceiptData.setM_oUserCredentialsData(getAdminUserInfo ());
			oReceiptData.setM_nCreatedBy(oReceiptData.getM_oUserCredentialsData().getM_nUserId());
			oReceiptDataResponse = (ReceiptDataResponse) super.saveAndPrint(oReceiptData);
			oOnlineReceiptData.getM_oVendorData().setM_nClientId(oVendorData.getM_nClientId());
			oOnlineReceiptData.getM_oReceiptData().setM_nReceiptId(oReceiptDataResponse.m_arrReceiptData.get(0).getM_nReceiptId());
			oOnlineReceiptDataProcessor.create(oOnlineReceiptData);
			Document oXmlDocument = convertStringToDocument(oReceiptDataResponse.m_strXMLData);
			oVendorData = (VendorData) populateObject(oVendorData);
			oReceiptDataResponse.m_strXMLData = (String) oReceiptData.addVendorToXML (oXmlDocument,oVendorData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("makeInvoice - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("makeInvoice - oReceiptDataResponse.m_bSuccess [OUT] : " + oReceiptDataResponse.m_bSuccess);
		return oReceiptDataResponse;
	}

	private Document convertStringToDocument(String strXMLData)
	{
		m_oLogger.info ("convertStringToDocument");
		m_oLogger.debug ("convertStringToDocument - strXMLData [IN] :: " + strXMLData);
		Document oDocument = null;
		DocumentBuilderFactory oDocFactory = DocumentBuilderFactory.newInstance();
        try 
        {  
        	DocumentBuilder oDocBuilder = oDocFactory.newDocumentBuilder();
            oDocument = oDocBuilder.parse(new InputSource(new StringReader(strXMLData))); 
        } 
        catch (Exception oException) 
        {  
        	m_oLogger.error("convertStringToDocument - oException : " + oException);
        } 
        return oDocument;
	}
	
	public ReportReceiptDataResponse getReportData (VendorData oVendorData)
	{
		m_oLogger.info ("getReportData");
		m_oLogger.debug ("getReportData - oVendorData [IN] : " + oVendorData);
		OnlineReceiptData oOnlineReceiptData = new OnlineReceiptData ();
		OnlineReceiptDataProcessor oOnlineReceiptDataProcessor = new OnlineReceiptDataProcessor ();
		OnlineReceiptDataResponse oOnlineReceiptDataResponse = new OnlineReceiptDataResponse ();
		ReportReceiptDataResponse oReportReceiptDataResponse = new ReportReceiptDataResponse ();
		ArrayList<ReportReceiptData> m_arrReportReceiptData = new ArrayList<ReportReceiptData> ();
		try
		{
			oOnlineReceiptData.getM_oVendorData().setM_nClientId(oVendorData.getM_nClientId());
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOnlineReceiptDataResponse=(OnlineReceiptDataResponse) oOnlineReceiptDataProcessor.list(oOnlineReceiptData, oOrderBy);
			for (int nIndex=0; nIndex < oOnlineReceiptDataResponse.m_arrOnlineReceipts.size(); nIndex++)
			{
				if(IsReceiptDataExist (oOnlineReceiptDataResponse.m_arrOnlineReceipts.get(nIndex).getM_oReceiptData(), m_arrReportReceiptData))
					updateReceiptData(oOnlineReceiptDataResponse.m_arrOnlineReceipts.get(nIndex).getM_oReceiptData(), m_arrReportReceiptData);
				else
					addReceiptData (oOnlineReceiptDataResponse.m_arrOnlineReceipts.get(nIndex).getM_oReceiptData(), m_arrReportReceiptData);
			}
			oReportReceiptDataResponse.m_arrReportReceiptData = m_arrReportReceiptData;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getReportData - oException : " + oException);
		}
		m_oLogger.debug("getReportData - oReceiptDataResponse [OUT] : " + oReportReceiptDataResponse.m_bSuccess);
		return oReportReceiptDataResponse;
	}
	
	public String getXML(ReceiptData oReceiptData) throws Exception
    {
		m_oLogger.info ("getXML : ");
		m_oLogger.debug ("getXML - oReceiptData [IN] : " +oReceiptData);
		String strXml = "";
		try 
		{
			oReceiptData.setM_oUserCredentialsData(getAdminUserInfo ());
			oReceiptData.setM_nCreatedBy(oReceiptData.getM_oUserCredentialsData().getM_nUserId());
			strXml = super.getXML(oReceiptData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
    }
	
	public GenericResponse get(ReceiptData oReceiptData) throws Exception
    {
		m_oLogger.info ("getXML : ");
		m_oLogger.debug ("getXML - oReceiptData [IN] : " +oReceiptData);
		ReceiptDataResponse oReceiptDataResponse = new ReceiptDataResponse ();
		try 
		{
			oReceiptData.setM_oUserCredentialsData(getAdminUserInfo ());
			oReceiptData.setM_nCreatedBy(oReceiptData.getM_oUserCredentialsData().getM_nUserId());
			oReceiptDataResponse = (ReceiptDataResponse) super.get(oReceiptData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +oReceiptDataResponse);
		return oReceiptDataResponse;
    }
	
}
