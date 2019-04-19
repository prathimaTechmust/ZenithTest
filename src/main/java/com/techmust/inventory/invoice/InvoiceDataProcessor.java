package com.techmust.inventory.invoice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.directwebremoting.io.FileTransfer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.dataexchange.DataExchangeResponse;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.exportimport.ExportImportProviderData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.challan.ChallanData;
import com.techmust.inventory.paymentsandreceipt.ReceiptData;
import com.techmust.inventory.sales.SalesData;
import com.techmust.inventory.sales.SalesDataProcessor;
import com.techmust.inventory.sales.SalesDataResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class InvoiceDataProcessor extends SalesDataProcessor
{
	@RequestMapping(value="/invoiceDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse create(@RequestBody SalesData oSalesData) throws Exception
    {
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oSalesData);
		InvoiceDataResponse oInvoiceDataResponse = new InvoiceDataResponse ();
		try
		{
			createLog(oSalesData, "InvoiceDataProcessor.create : ");
			SalesDataResponse oSalesDataResponse = (SalesDataResponse)super.create(oSalesData);
			oInvoiceDataResponse.m_bSuccess = oSalesDataResponse.m_bSuccess;
			oSalesData = oSalesDataResponse.m_arrSales.get(0);
			InvoiceData oInvoiceData = new InvoiceData ();
			oInvoiceDataResponse.m_arrInvoice.add(oInvoiceData.create(oSalesData, oSalesData.getM_nCreatedBy()));
			oSalesData.setM_strInvoiceNo(oInvoiceDataResponse.m_arrInvoice.get(0).getM_strInvoiceNumber());
			oSalesData.updateObject();
			oInvoiceDataResponse.m_strXMLData = oInvoiceDataResponse.m_arrInvoice.get(0).generateXML();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oInvoiceDataResponse.m_bSuccess [OUT] : " + oInvoiceDataResponse.m_bSuccess);
		return oInvoiceDataResponse;
    }
	@RequestMapping(value="/invoiceDatadeleteData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody InvoiceData oData) throws Exception
    {
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oData);
		InvoiceDataResponse oInvoiceDataResponse = new InvoiceDataResponse ();
		try
		{
			oInvoiceDataResponse.m_bSuccess = oData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oInvoiceDataResponse.m_bSuccess [OUT] : " + oInvoiceDataResponse.m_bSuccess);
		return oInvoiceDataResponse;
    }

	@RequestMapping(value="/invoiceDataget", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse get(@RequestBody InvoiceData oData) throws Exception
    {
		m_oLogger.info ("get");
		InvoiceDataResponse oInvoiceDataResponse = new InvoiceDataResponse ();
		try 
		{
			oData = (InvoiceData) populateObject (oData);
			oInvoiceDataResponse.m_arrInvoice.add(oData);
			oInvoiceDataResponse.m_arrInvoice = buildInvoiceData (oInvoiceDataResponse.m_arrInvoice);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oInvoiceDataResponse;
    }
	@RequestMapping(value="/invoiceDatagetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public String getXML(@RequestBody InvoiceData oData) throws Exception
    {
		oData = (InvoiceData) populateObject(oData);
	     return oData != null ? oData.generateXML () : "";
    }

    public FileTransfer exportToTally (InvoiceData oData) throws Exception
    {
    	HashMap<String, String> oOrderBy = new HashMap<String, String> ();
    	InvoiceDataResponse oInvoiceDataResponse = (InvoiceDataResponse) list(oData,oOrderBy);
    	String strTallyXml = "";
    	strTallyXml = GenericUtil.buildHtml(oData.generateTallyDataXML(oInvoiceDataResponse.m_arrInvoice), GenericUtil.getProperty("kTallyFormatXSLT"));
    	InputStream oInputStream = new ByteArrayInputStream(strTallyXml.getBytes());
    	FileTransfer oDwrXMLFile = new FileTransfer("tally_"+System.currentTimeMillis()+".xml", "application/xml", oInputStream);
    	return oDwrXMLFile;
    }
    
    @RequestMapping(value="/invoiceList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse list(@RequestBody TradeMustHelper oData)throws Exception
    {
    	HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
    	return list(oData.getM_oInvoiceData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
    }
    
	public GenericResponse list(InvoiceData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}
	
    @SuppressWarnings("unchecked")
	public GenericResponse list(InvoiceData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)throws Exception
    {
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " +oData);
		InvoiceDataResponse oInvoiceDataResponse = new InvoiceDataResponse ();
		try 
		{
			oInvoiceDataResponse.m_nRowCount = getRowCount(oData);
			oInvoiceDataResponse.m_arrInvoice = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
			oInvoiceDataResponse.m_arrInvoice = buildInvoiceData (oInvoiceDataResponse.m_arrInvoice);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oInvoiceDataResponse;
    }
    @RequestMapping(value="/invoiceDataupdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody SalesData oSalesData) throws Exception
    {
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData [IN] : " + oSalesData);
		InvoiceDataResponse oInvoiceDataResponse = new InvoiceDataResponse ();
		try
		{
			createLog(oSalesData, "InvoiceDataProcessor.update : ");
			SalesDataResponse oSalesDataResponse = (SalesDataResponse)super.update(oSalesData);
			oInvoiceDataResponse.m_bSuccess = oSalesDataResponse.m_bSuccess;
			
			oSalesData = (SalesData) populateObject(oSalesDataResponse.m_arrSales.get(0));
			try
			{
				oInvoiceDataResponse.m_arrInvoice.add(InvoiceData.getInstance(oSalesData.getM_nId()));
			}
			catch (Exception oException)
			{
				InvoiceData oInvoiceData = new InvoiceData ();
				oInvoiceDataResponse.m_arrInvoice.add(oInvoiceData.create(oSalesData, oSalesData.getM_nCreatedBy()));
				oSalesData.setM_strInvoiceNo(oInvoiceDataResponse.m_arrInvoice.get(0).getM_strInvoiceNumber());
				oSalesData.updateObject();
			}
			ChallanData oChallanData = null;
			try
			{
				oChallanData = ChallanData.getInstance(oSalesData);
			}
			catch(Exception oException)
			{
				
			}
			if (oChallanData != null)
			{
				oChallanData.setM_oInvoiceData(oInvoiceDataResponse.m_arrInvoice.get(0));
				oChallanData.updateObject();
			}
			oInvoiceDataResponse.m_strXMLData = oInvoiceDataResponse.m_arrInvoice.get(0).generateXML();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oInvoiceDataResponse.m_bSuccess [OUT] : " + oInvoiceDataResponse.m_bSuccess);
		return oInvoiceDataResponse;
    }
    
	public GenericResponse updateRemarks(InvoiceData oInvoiceData) throws Exception
    {
		m_oLogger.info ("updateRemarks");
		m_oLogger.debug ("updateRemarks - oData [IN] : " + oInvoiceData);
		InvoiceDataResponse oInvoiceDataResponse = new InvoiceDataResponse ();
		try
		{
			InvoiceData oData = new InvoiceData ();
			oData.setM_nInvoiceId(oInvoiceData.getM_nInvoiceId());
			oData = (InvoiceData) populateObject(oData);
			oData.setM_strRemarks(oInvoiceData.getM_strRemarks());
			oData.setM_strLRNumber(oInvoiceData.getM_strLRNumber());
			oData.setM_strESugamNumber(oInvoiceData.getM_strESugamNumber());
			oInvoiceDataResponse.m_bSuccess = oData.updateObject();
			oInvoiceDataResponse.m_strXMLData = oData.generateXML();
			oInvoiceDataResponse.m_arrInvoice.add(oData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateRemarks - oException : " + oException);
			throw oException;
		}
		return oInvoiceDataResponse;
    }
	
	@RequestMapping(value="/invoiceDatagetClientDetails", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getClientDetails (@RequestBody int nClientId)
	{
		String strXMLData = "<ClientDetails>";
		ClientData oClientData = new ClientData ();
		oClientData.setM_nClientId(nClientId);
		try 
		{
			oClientData = (ClientData) populateObject(oClientData);
			strXMLData += oClientData.generateXML();
			strXMLData += getClientBusinessDetails (oClientData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getClientDetails - oException : " + oException);
		}
		return strXMLData + "</ClientDetails>";
	}
	
	 @SuppressWarnings("unchecked")
	public FileTransfer exportInvoiceData (InvoiceData oInvoiceData, ExportImportProviderData oProviderData) throws Exception
	    {
	    	m_oLogger.info ("exportInvoiceData");
			m_oLogger.debug ("exportInvoiceData - oInvoiceData [IN] : " + oInvoiceData);
	    	FileTransfer oFile = null;
			InvoiceDataResponse oInvoiceDataResponse = new InvoiceDataResponse ();
			try
			{
				HashMap<String, String> oOrderBy = new HashMap<String, String> ();
				oInvoiceDataResponse = (InvoiceDataResponse) list(oInvoiceData, oOrderBy);
		    	oFile = oProviderData.export (oInvoiceDataResponse.m_arrInvoice);
			}
			catch (Exception oException)
			{
				m_oLogger.error ("exportItemData - oException : " + oException);
			}
	    	return oFile;
	    }
	    
	    @SuppressWarnings("unchecked")
		public GenericResponse importInvoiceData (InvoiceData oInvoiceData, ExportImportProviderData oProviderData, FileTransfer oFileTransfer) throws Exception
		{
			m_oLogger.info ("importInvoiceData");
			m_oLogger.debug ("importInvoiceData - oFileTransfer [IN] : " + oFileTransfer);
			DataExchangeResponse oDataExchangeResponse = new DataExchangeResponse ();
			try 
			{
				UserInformationData oUserInformationData = getUserData(oInvoiceData.getM_nCreatedBy());
				oDataExchangeResponse = (DataExchangeResponse) oProviderData.importData (oFileTransfer, oInvoiceData.getClass().getName(), oUserInformationData);
			}
			catch (Exception oException)
			{
				m_oLogger.error ("importInvoiceData - oException : " + oException);
				throw oException;
			}
			return oDataExchangeResponse;
		}
	
    private UserInformationData getUserData(int m_nCreatedBy) 
    {
			// TODO Auto-generated method stub
			return new UserInformationData();
	}
    
	@SuppressWarnings("unchecked")
	private String getClientBusinessDetails(ClientData oClientData) throws Exception
    {
    	InvoiceData oInvoiceData = new InvoiceData ();
    	oInvoiceData.setM_bIsForClientOutstanding(true);
    	oInvoiceData.setM_oClientData(oClientData);
    	double nInvoiceAmount = 0;
    	HashMap<String, String> oOrderBy = new HashMap<String, String> ();
    	ArrayList arrInvoice = new ArrayList (oInvoiceData.list(oOrderBy));
    	if(arrInvoice.size() > 0)
    	{
			Object[] arrInvoiceObject = (Object[])arrInvoice.get(0);
			nInvoiceAmount = (Double) arrInvoiceObject[1];
    	}
    	String strXMLData = "<BusinessDetails>";
    	strXMLData += "<nTotalBusiness>" + (float)nInvoiceAmount + "</nTotalBusiness>";
    	strXMLData += "<nTotalReceived>" + getTotalReceivedAmount (oClientData.getM_nClientId())+ "</nTotalReceived>";
		return strXMLData + "</BusinessDetails>";
	}

	public float getTotalReceivedAmount(int nClientId) 
	{
		float nReceivedAmount = 0;
		ReceiptData oReceiptData = new ReceiptData ();
		nReceivedAmount = oReceiptData.getClientReceiptsAmount (nClientId);
		return nReceivedAmount;
	}

	private ArrayList<InvoiceData> buildInvoiceData(ArrayList<InvoiceData> arrInvoiceData) 
	{
		m_oLogger.info("buildInvoiceDate");
		for (int nIndex=0; nIndex < arrInvoiceData.size(); nIndex++)
			arrInvoiceData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrInvoiceData.get(nIndex).getM_dCreatedOn()));
		return arrInvoiceData;
	}
    
    private void createLog (SalesData oSalesData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			ClientData oClientData = new ClientData ();
			oClientData.setM_nClientId(oSalesData.getM_oClientData().getM_nClientId());
			oClientData = (ClientData) populateObject(oClientData);
			oSalesData.setM_strTo(oClientData.getM_strCompanyName());
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oSalesData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oSalesData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName + oSalesData.getM_strTo());
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
    
    public GenericResponse updateInvoiceTable () 
    {
		m_oLogger.info ("updateInvoiceTable");
		InvoiceDataResponse oInvoiceDataResponse = new InvoiceDataResponse ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			InvoiceData oData = new InvoiceData ();
			oInvoiceDataResponse = (InvoiceDataResponse) list (oData, oOrderBy, 0, 0);
			for (int nIndex = 0; nIndex < oInvoiceDataResponse.m_arrInvoice.size(); nIndex++) 
			{
				InvoiceData oInvoiceData = oInvoiceDataResponse.m_arrInvoice.get(nIndex);
				oInvoiceData.setM_nInvoiceAmount(Math.round(SalesData.getInvoiceAmount (oInvoiceData)));
				oInvoiceData.setM_nBalanceAmount(oInvoiceData.getM_nInvoiceAmount() - oInvoiceData.getM_nReceiptAmount());
				oInvoiceData.updateObject();
				oInvoiceDataResponse.m_nRowCount++;
			}
			oInvoiceDataResponse.m_bSuccess = true;
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateInvoiceTable - oException : " + oException);
		}
		return oInvoiceDataResponse;
    }
    
    @RequestMapping(value="/invoiceDatagetAgeWiseInvoices", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public Object[] getAgeWiseInvoices (InvoiceData oData)
    {
    	m_oLogger.info ("getAgeWiseInvoices");
    	Object[] arrAgeWiseData = new Object [7];
    	try
		{
    		String[] arrPeriod = getAgeWiseArray ();
    		for (int nIndex = 0; nIndex < arrPeriod.length; nIndex++) 
    		{
    			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
    			Object[] arrAgeWise = new Object [3];
    			arrAgeWise[0] = arrPeriod[nIndex];
    			String[] arrDates = arrPeriod[nIndex].split("-");
    			InvoiceData oInvoiceData = new InvoiceData ();
    			String strFromDate = arrDates.length > 1 ? getDate(Integer.parseInt(arrDates[1])) : "";
    			oInvoiceData.setM_strFromDate(strFromDate);
    			oInvoiceData.setM_strToDate(getDate(Integer.parseInt(arrDates[0])));
    			oInvoiceData.m_bIsForAgeWise = true;
    			oInvoiceData.setM_oClientData(oData.getM_oClientData());
    			oInvoiceData.setM_nContactId(oData.getM_nContactId());
    			oInvoiceData.setM_nSiteId(oData.getM_nSiteId());
    			ArrayList arrInvoiceData = oInvoiceData.list(oOrderBy);
    			Object[] arrCountAndAmount = (Object[]) arrInvoiceData.get(0);
    			arrAgeWise[1] = arrCountAndAmount[0];
    			arrAgeWise[2] = arrCountAndAmount[1];
    			arrAgeWiseData[nIndex] = arrAgeWise;
    		}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getAgeWiseInvoices - oException : " + oException);
		}
    	return arrAgeWiseData;
    }

	private String[] getAgeWiseArray()
	{
		String[] arrAgeWise = new String[7];
		arrAgeWise[0] = "91-";
		arrAgeWise[1] = "61-90";
		arrAgeWise[2] = "46-60";
		arrAgeWise[3] = "31-45";
		arrAgeWise[4] = "21-30";
		arrAgeWise[5] = "11-20";
		arrAgeWise[6] = "1-10";
		return arrAgeWise;
	}
	
	private String getDate(int nNoOfDays) 
	{
		String strDate = "";
		if(nNoOfDays > 0)
		{
			Calendar oPreviousDate = Calendar.getInstance();
			oPreviousDate.add(Calendar.DATE, -nNoOfDays);
			strDate =  GenericIDataProcessor.getClientCompatibleFormat((oPreviousDate.getTime()));
		}
		return strDate;
	}
}
