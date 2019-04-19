package com.techmust.inventory.paymentsandreceipt;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.vendormanagement.VendorData;

public class OnlineReceiptDataProcessor extends GenericIDataProcessor<OnlineReceiptData> 
{
	@Override
	public GenericResponse create(OnlineReceiptData oOnlineReceiptData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oOnlineVendorInvoiceData [IN] : " + oOnlineReceiptData);
		OnlineReceiptDataResponse oOnlineReceiptDataResponse = new OnlineReceiptDataResponse ();
		try
		{
			oOnlineReceiptDataResponse.m_bSuccess = oOnlineReceiptData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oOnlineReceiptDataResponse.m_bSuccess [OUT] : " + oOnlineReceiptDataResponse.m_bSuccess);
		return oOnlineReceiptDataResponse;
	}

	@Override
	public GenericResponse deleteData(OnlineReceiptData oOnlineReceiptData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oOnlineReceiptData.getM_nId() [IN] : " + oOnlineReceiptData.getM_nId());
		OnlineReceiptDataResponse oOnlineReceiptDataResponse = new OnlineReceiptDataResponse ();
		try
		{
			oOnlineReceiptDataResponse.m_bSuccess = oOnlineReceiptData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oOnlineReceiptDataResponse.m_bSuccess [OUT] : " + oOnlineReceiptDataResponse.m_bSuccess);
		return oOnlineReceiptDataResponse;
	}

	@Override
	public GenericResponse get(OnlineReceiptData oOnlineReceiptData) throws Exception
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oOnlineReceiptData.getM_nId() [IN] :" +oOnlineReceiptData.getM_nId());
		OnlineReceiptDataResponse oOnlineReceiptDataResponse = new OnlineReceiptDataResponse ();
		try 
		{
			oOnlineReceiptData = (OnlineReceiptData) populateObject (oOnlineReceiptData);
			oOnlineReceiptDataResponse.m_arrOnlineReceipts.add (oOnlineReceiptData);
			oOnlineReceiptDataResponse.m_arrOnlineReceipts = buildVendorReceiptData (oOnlineReceiptDataResponse.m_arrOnlineReceipts);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oOnlineReceiptDataResponse;
	}

	private ArrayList<OnlineReceiptData> buildVendorReceiptData(ArrayList<OnlineReceiptData> arrOnlineReceipts) 
	{
		m_oLogger.info("buildVendorReceiptData");
		for (int nIndex=0; nIndex < arrOnlineReceipts.size(); nIndex++)
			arrOnlineReceipts.get(nIndex).getM_oReceiptData().setM_strDate(getClientCompatibleFormat(arrOnlineReceipts.get(nIndex).getM_oReceiptData().getM_dCreatedOn()));
		return arrOnlineReceipts;
	}

	@Override
	public String getXML(OnlineReceiptData oOnlineReceiptData) throws Exception 
	{
		OnlineReceiptDataResponse oOnlineReceiptDataResponse = new OnlineReceiptDataResponse ();
		oOnlineReceiptData = (OnlineReceiptData) populateObject(oOnlineReceiptData);
		oOnlineReceiptDataResponse.m_strXMLData = oOnlineReceiptData.getM_oReceiptData().generateXML();
		Document oXmlDocument = convertStringToDocument(oOnlineReceiptDataResponse.m_strXMLData);
		VendorData oVendorData = (VendorData) populateObject(oOnlineReceiptData.getM_oVendorData());
		oOnlineReceiptDataResponse.m_strXMLData = oOnlineReceiptData.addVendorToXML (oXmlDocument,oVendorData);
		return oOnlineReceiptDataResponse.m_strXMLData;
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
	

	@Override
	public GenericResponse list(OnlineReceiptData oOnlineReceiptData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oOnlineReceiptData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked") 
	public GenericResponse list(OnlineReceiptData oOnlineReceiptData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oOnlineReceiptData [IN] : " +oOnlineReceiptData);
		OnlineReceiptDataResponse oOnlineReceiptDataResponse = new OnlineReceiptDataResponse ();
		try 
		{
			oOnlineReceiptDataResponse.m_nRowCount = getRowCount(oOnlineReceiptData);
			oOnlineReceiptDataResponse.m_arrOnlineReceipts = new ArrayList (oOnlineReceiptData.list (arrOrderBy, nPageNumber, nPageSize));
			oOnlineReceiptDataResponse.m_arrOnlineReceipts = buildVendorReceiptData (oOnlineReceiptDataResponse.m_arrOnlineReceipts);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oOnlineReceiptDataResponse;
	}

	@Override
	public GenericResponse update(OnlineReceiptData oOnlineReceiptData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oOnlineReceiptData.getM_nId() [IN] : " + oOnlineReceiptData.getM_nId());
		OnlineReceiptDataResponse oOnlineReceiptDataResponse = new OnlineReceiptDataResponse ();
		try
		{
			oOnlineReceiptDataResponse.m_bSuccess = oOnlineReceiptData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oOnlineVendorInvoiceDataResponse.m_bSuccess [OUT] : " + oOnlineReceiptDataResponse.m_bSuccess);
		return oOnlineReceiptDataResponse;
	}

	public ReportReceiptDataResponse getReportData (OnlineReceiptData oOnlineReceiptData)
	{
		m_oLogger.info ("getReportData");
		m_oLogger.debug ("getReportData - oOnlineReceiptData [IN] : " + oOnlineReceiptData);
		OnlineReceiptDataResponse oOnlineReceiptDataResponse = new OnlineReceiptDataResponse ();
		ReportReceiptDataResponse oReportReceiptDataResponse = new ReportReceiptDataResponse ();
		ArrayList<ReportReceiptData> m_arrReportReceiptData = new ArrayList<ReportReceiptData> ();
		ReceiptDataProcessor oReceiptDataProcessor = new ReceiptDataProcessor ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOnlineReceiptDataResponse=(OnlineReceiptDataResponse) list(oOnlineReceiptData,oOrderBy);
			for (int nIndex=0; nIndex < oOnlineReceiptDataResponse.m_arrOnlineReceipts.size(); nIndex++)
			{
				if(oReceiptDataProcessor.IsReceiptDataExist (oOnlineReceiptDataResponse.m_arrOnlineReceipts.get(nIndex).getM_oReceiptData(), m_arrReportReceiptData))
					oReceiptDataProcessor.updateReceiptData(oOnlineReceiptDataResponse.m_arrOnlineReceipts.get(nIndex).getM_oReceiptData(), m_arrReportReceiptData);
				else
					oReceiptDataProcessor.addReceiptData (oOnlineReceiptDataResponse.m_arrOnlineReceipts.get(nIndex).getM_oReceiptData(), m_arrReportReceiptData);
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
}
