package com.techmust.inventory.invoice;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.vendormanagement.VendorData;

public class OnlineVendorInvoiceDataProcessor extends GenericIDataProcessor<OnlineVendorInvoiceData> 
{
	@Override
	public GenericResponse create(OnlineVendorInvoiceData oOnlineVendorInvoiceData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oOnlineVendorInvoiceData [IN] : " + oOnlineVendorInvoiceData);
		OnlineVendorInvoiceDataResponse oOnlineVendorInvoiceDataResponse = new OnlineVendorInvoiceDataResponse ();
		try
		{
			oOnlineVendorInvoiceDataResponse.m_bSuccess = oOnlineVendorInvoiceData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oOnlineVendorInvoiceDataResponse.m_bSuccess [OUT] : " + oOnlineVendorInvoiceDataResponse.m_bSuccess);
		return oOnlineVendorInvoiceDataResponse;
	}

	@Override
	public GenericResponse deleteData(OnlineVendorInvoiceData oOnlineVendorInvoiceData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oOnlineVendorInvoiceData.getM_nId() [IN] : " + oOnlineVendorInvoiceData.getM_nId());
		OnlineVendorInvoiceDataResponse oOnlineVendorInvoiceDataResponse = new OnlineVendorInvoiceDataResponse ();
		try
		{
			oOnlineVendorInvoiceDataResponse.m_bSuccess = oOnlineVendorInvoiceData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oOnlineVendorInvoiceDataResponse.m_bSuccess [OUT] : " + oOnlineVendorInvoiceDataResponse.m_bSuccess);
		return oOnlineVendorInvoiceDataResponse;
	}

	@Override
	public GenericResponse get(OnlineVendorInvoiceData oOnlineVendorInvoiceData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oOnlineVendorInvoiceData.getM_nId() [IN] :" +oOnlineVendorInvoiceData.getM_nId());
		OnlineVendorInvoiceDataResponse oOnlineVendorInvoiceDataResponse = new OnlineVendorInvoiceDataResponse ();
		try 
		{
			oOnlineVendorInvoiceData = (OnlineVendorInvoiceData) populateObject (oOnlineVendorInvoiceData);
			oOnlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice.add (oOnlineVendorInvoiceData);
			oOnlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice = buildVendorInvoiceData (oOnlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oOnlineVendorInvoiceDataResponse;
	}

	@Override
	public String getXML(OnlineVendorInvoiceData oOnlineVendorInvoiceData) throws Exception 
	{
		OnlineVendorInvoiceDataResponse oOnlineVendorInvoiceDataResponse = new OnlineVendorInvoiceDataResponse ();
		oOnlineVendorInvoiceData = (OnlineVendorInvoiceData) populateObject(oOnlineVendorInvoiceData);
		oOnlineVendorInvoiceDataResponse.m_strXMLData = oOnlineVendorInvoiceData.getM_oInvoiceData().generateXML();
		Document oXmlDocument = convertStringToDocument(oOnlineVendorInvoiceDataResponse.m_strXMLData);
		VendorData oVendorData = (VendorData) populateObject(oOnlineVendorInvoiceData.getM_oVendorData());
		oOnlineVendorInvoiceDataResponse.m_strXMLData = oOnlineVendorInvoiceData.addVendorToXML (oXmlDocument,oVendorData);
		return oOnlineVendorInvoiceDataResponse.m_strXMLData;
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
	public GenericResponse list(OnlineVendorInvoiceData oOnlineVendorInvoiceData,HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oOnlineVendorInvoiceData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked") 
	public GenericResponse list(OnlineVendorInvoiceData oOnlineVendorInvoiceData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oOnlineVendorInvoiceData [IN] : " +oOnlineVendorInvoiceData);
		OnlineVendorInvoiceDataResponse oOnlineVendorInvoiceDataResponse = new OnlineVendorInvoiceDataResponse ();
		try 
		{
			oOnlineVendorInvoiceDataResponse.m_nRowCount = getRowCount(oOnlineVendorInvoiceData);
			oOnlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice = new ArrayList (oOnlineVendorInvoiceData.list (arrOrderBy, nPageNumber, nPageSize));
			oOnlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice = buildVendorInvoiceData (oOnlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oOnlineVendorInvoiceDataResponse;
	}

	private ArrayList<OnlineVendorInvoiceData> buildVendorInvoiceData(ArrayList<OnlineVendorInvoiceData> arrOnlineVendorInvoice) 
	{
		m_oLogger.info("buildVendorInvoiceData");
		for (int nIndex=0; nIndex < arrOnlineVendorInvoice.size(); nIndex++)
			arrOnlineVendorInvoice.get(nIndex).getM_oInvoiceData().setM_strDate(getClientCompatibleFormat(arrOnlineVendorInvoice.get(nIndex).getM_oInvoiceData().getM_dCreatedOn()));
		return arrOnlineVendorInvoice;
	}

	@Override
	public GenericResponse update(OnlineVendorInvoiceData oOnlineVendorInvoiceData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oOnlineVendorInvoiceData.getM_nId() [IN] : " + oOnlineVendorInvoiceData.getM_nId());
		OnlineVendorInvoiceDataResponse oOnlineVendorInvoiceDataResponse = new OnlineVendorInvoiceDataResponse ();
		try
		{
			oOnlineVendorInvoiceDataResponse.m_bSuccess = oOnlineVendorInvoiceData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oOnlineVendorInvoiceDataResponse.m_bSuccess [OUT] : " + oOnlineVendorInvoiceDataResponse.m_bSuccess);
		return oOnlineVendorInvoiceDataResponse;
	}

	public GenericResponse getClientInvoiceXml (InvoiceData oInvoiceData) throws Exception
	{
		m_oLogger.debug ("getClientInvoiceXml");
		OnlineVendorInvoiceDataResponse oOnlineVendorInvoiceDataResponse = new OnlineVendorInvoiceDataResponse ();
		OnlineVendorInvoiceData oOnlineVendorInvoiceData = new OnlineVendorInvoiceData ();
		InvoiceDataProcessor oDataProcessor = new InvoiceDataProcessor ();
		try
		{
			oOnlineVendorInvoiceData.getM_oInvoiceData().setM_nInvoiceId(oInvoiceData.getM_nInvoiceId());
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOnlineVendorInvoiceDataResponse = (OnlineVendorInvoiceDataResponse) list(oOnlineVendorInvoiceData, oOrderBy);
			OnlineVendorInvoiceData oData = new OnlineVendorInvoiceData ();
			if(oOnlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice.size()>0)
			{
				oData.setM_nId(oOnlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice.get(0).getM_nId());
				oOnlineVendorInvoiceDataResponse.m_strXMLData = getXML(oData);
			}
			else
				oOnlineVendorInvoiceDataResponse.m_strXMLData = oDataProcessor.getXML(oInvoiceData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getClientInvoiceXml - oException : " + oException);
			throw oException;
		}
		return oOnlineVendorInvoiceDataResponse;
	}
	
}
