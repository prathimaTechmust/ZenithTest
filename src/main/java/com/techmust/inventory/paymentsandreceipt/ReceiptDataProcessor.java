package com.techmust.inventory.paymentsandreceipt;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.directwebremoting.io.FileTransfer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.purchase.PurchaseDataResponse;
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.serial.SerialType;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class ReceiptDataProcessor extends GenericIDataProcessor<ReceiptData> 
{
	@RequestMapping(value="/ReceiptDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody ReceiptData oReceiptData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oReceiptData [IN] : " + oReceiptData);
		ReceiptDataResponse oReceiptDataResponse = new ReceiptDataResponse ();
		try
		{
			isvalidUser (oReceiptData);
			createLog(oReceiptData, "ReceiptDataProcessor.create : ");
			oReceiptData.prepareForSaveOrUpdate();
			oReceiptData.setM_strReceiptNumber(SerialNumberData.generateSerialNumber(SerialType.kReceiptNumber));
			oReceiptDataResponse.m_bSuccess = oReceiptData.saveObject();
			oReceiptData.updateReceiptInvoices(oReceiptData);
			oReceiptDataResponse.m_arrReceiptData.add(oReceiptData);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("create - oException : " + oException);
		}
		m_oLogger.debug("create - oReceiptDataResponse [OUT] : " + oReceiptDataResponse.m_bSuccess);
		return oReceiptDataResponse;
	}

	@RequestMapping(value="/ReceiptDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData( @RequestBody ReceiptData oReceiptData) throws Exception 
	{
		return null;
	}

	@RequestMapping(value="/ReceiptDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get( @RequestBody ReceiptData oReceiptData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oReceiptData.getM_nReceiptId() [IN] :" + oReceiptData.getM_nReceiptId());
		ReceiptDataResponse oReceiptDataResponse = new ReceiptDataResponse ();
		try 
		{
			isvalidUser (oReceiptData);
			oReceiptData =  (ReceiptData) populateObject (oReceiptData);
			oReceiptData.setM_strDate (getClientCompatibleFormat (oReceiptData.getM_dCreatedOn()));
			oReceiptData.setM_oInvoiceReceipts((buildInvoiceReceiptData (oReceiptData.getM_oInvoiceReceipts())));
			oReceiptDataResponse.m_arrReceiptData.add(oReceiptData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oReceiptDataResponse;
	}

	@RequestMapping(value="/ReceiptDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML( @RequestBody ReceiptData oReceiptData) throws Exception 
	{
		m_oLogger.debug("getXML - oReceiptData [IN] : " + oReceiptData);
		try
		{
			isvalidUser (oReceiptData);
			oReceiptData = (ReceiptData) populateObject(oReceiptData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " + oException);
		}
		return oReceiptData != null ?oReceiptData.generateXML () : ""; 
	}

	@RequestMapping(value="/ReceiptDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{ 
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		GenericResponse oReceiptDataResponse =list(oTradeMustHelper.getM_oReceiptData(), oOrderBy, oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
		return oReceiptDataResponse;
	}
	
	public GenericResponse list(ReceiptData oReceiptData,HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oReceiptData [IN] : " +oReceiptData);
		ReceiptDataResponse oResponse = new ReceiptDataResponse ();
		try 
		{
			isvalidUser (oReceiptData);
			ReceiptDataResponse.m_nRowCount = getRowCount(oReceiptData);
			oResponse.m_arrReceiptData = new ArrayList (oReceiptData.list (arrOrderBy, nPageNumber, nPageSize));
			oResponse.m_arrReceiptData = buildReceiptDate (oResponse.m_arrReceiptData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		m_oLogger.debug("list - oResponse.m_arrReceiptData.size () [OUT] : " + oResponse.m_arrReceiptData.size());
		return oResponse;
	}

	@RequestMapping(value="/ReceiptDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update( @RequestBody ReceiptData oReceiptData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oReceiptData [IN] : " + oReceiptData);
		ReceiptDataResponse oReceiptDataResponse = new ReceiptDataResponse ();
		try
		{
			isvalidUser (oReceiptData);
			createLog(oReceiptData, "ReceiptDataProcessor.update : ");
			removePreviousChildren (oReceiptData);
			oReceiptData.prepareForSaveOrUpdate();
			oReceiptDataResponse.m_bSuccess = oReceiptData.updateObject();
			oReceiptData.updateReceiptInvoices(oReceiptData);
			oReceiptDataResponse.m_arrReceiptData.add(oReceiptData);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		m_oLogger.debug("update - oReceiptDataResponse [OUT] : " + oReceiptDataResponse.m_bSuccess);
		return oReceiptDataResponse;
	}
	
	private void removePreviousChildren(ReceiptData oReceiptData) throws Exception 
	{
		m_oLogger.info ("removePreviousChildren");
		m_oLogger.debug ("removePreviousChildren - oReceiptData [IN] : " + oReceiptData);
		try
		{
			oReceiptData = (ReceiptData) populateObject(oReceiptData);
			Iterator<InvoiceReceiptData> oIterator = oReceiptData.getM_oInvoiceReceipts().iterator();
			while (oIterator.hasNext())
			{
				InvoiceReceiptData oInvoiceReceiptData = (InvoiceReceiptData) oIterator.next();
				oInvoiceReceiptData.getM_oInvoiceData().removeReceiptAmount (oInvoiceReceiptData);
				oInvoiceReceiptData.deleteObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("removePreviousChildren - oException : " , oException);
			throw oException;
		}
	}
	@RequestMapping(value="/ReceiptDatagetReports", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ReportReceiptDataResponse getReports (@RequestBody ReceiptData oReceiptData)
	{
		m_oLogger.info ("getReports");
		m_oLogger.debug ("getReports - oReceiptData [IN] : " + oReceiptData);
		ReportReceiptDataResponse oReportReceiptDataResponse = new ReportReceiptDataResponse ();
		ArrayList<ReportReceiptData> m_arrReportReceiptData = new ArrayList<ReportReceiptData> ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ReceiptDataResponse oReceiptDataResponse = (ReceiptDataResponse) list(oReceiptData,oOrderBy);
			for (int nIndex=0; nIndex < oReceiptDataResponse.m_arrReceiptData.size(); nIndex++)
			{
				if(IsReceiptDataExist (oReceiptDataResponse.m_arrReceiptData.get(nIndex), m_arrReportReceiptData))
					updateReceiptData(oReceiptDataResponse.m_arrReceiptData.get(nIndex), m_arrReportReceiptData);
				else
					addReceiptData (oReceiptDataResponse.m_arrReceiptData.get(nIndex), m_arrReportReceiptData);
			}
			oReportReceiptDataResponse.m_arrReportReceiptData = m_arrReportReceiptData;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getReports - oException : " + oException);
		}
		m_oLogger.debug("getReports - oReceiptDataResponse [OUT] : " + oReportReceiptDataResponse.m_bSuccess);
		return oReportReceiptDataResponse;
	}
	@RequestMapping(value="/ReceiptDataexportToTally", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public FileTransfer exportToTally (@RequestBody ReceiptData oReceiptData) throws Exception
    {
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
    	ReceiptDataResponse oReceiptDataResponse = (ReceiptDataResponse) list(oReceiptData, oOrderBy);
    	String strTallyXml = "";
    	strTallyXml = GenericUtil.buildHtml(oReceiptData.generateTallyDataXML(oReceiptDataResponse.m_arrReceiptData), GenericUtil.getProperty("kTallyReceiptFormatXSLT"));
    	InputStream oInputStream = new ByteArrayInputStream(strTallyXml.getBytes());
    	FileTransfer oDwrXMLFile = new FileTransfer("tally_rcpt_"+System.currentTimeMillis()+".xml", "application/xml", oInputStream);
    	return oDwrXMLFile;
    }
	@RequestMapping(value="/ReceiptDataSaveAndPrint", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse saveAndPrint(@RequestBody ReceiptData oReceiptData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oReceiptData [IN] : " + oReceiptData);
		ReceiptDataResponse oReceiptDataResponse = new ReceiptDataResponse ();
		try
		{
			oReceiptDataResponse = oReceiptData.getM_nReceiptId() > 0 ?(ReceiptDataResponse) update(oReceiptData) : (ReceiptDataResponse) create(oReceiptData);
			oReceiptDataResponse.m_strXMLData = getXML(oReceiptDataResponse.m_arrReceiptData.get(0));
		}
		
			catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oReceiptDataResponse.m_bSuccess [OUT] : " + oReceiptDataResponse.m_bSuccess);
		return oReceiptDataResponse;
	}
	
	protected void updateReceiptData(ReceiptData oReceiptData, ArrayList<ReportReceiptData> arrReportReceiptData) 
	{
		m_oLogger.info ("updateReceiptData");
		m_oLogger.debug ("updateReceiptData - oReceiptData [IN] : " + oReceiptData);
		try
		{
			for (int nIndex=0; nIndex < arrReportReceiptData.size(); nIndex++)
			{
				if (arrReportReceiptData.get(nIndex).getM_oClientData().getM_nClientId() == oReceiptData.getM_oClientData().getM_nClientId())
				{	
					arrReportReceiptData.get(nIndex).setReportData(oReceiptData);
					break;
				}
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("addReceiptData - oException : " + oException);
		}
	}

	protected void addReceiptData(ReceiptData oReceiptData, ArrayList<ReportReceiptData> arrReportReceiptData) 
	{
		m_oLogger.info ("addReceiptData");
		m_oLogger.debug ("addReceiptData - oReceiptData [IN] : " + oReceiptData);
		try
		{
			ReportReceiptData oReportReceiptData = new ReportReceiptData ();
			oReportReceiptData.setReportData(oReceiptData);
			arrReportReceiptData.add(oReportReceiptData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("addReceiptData - oException : " + oException);
		}
	}

	protected boolean IsReceiptDataExist(ReceiptData oReceiptData, ArrayList<ReportReceiptData> arrReportReceiptData) 
	{
		m_oLogger.info ("IsReceiptDataExist");
		m_oLogger.debug ("IsReceiptDataExist - oReceiptData [IN] : " + oReceiptData);
		boolean IsReceiptDataExist = false;
		try
		{
			for (int nIndex=0; nIndex < arrReportReceiptData.size(); nIndex++)
			{
				if (arrReportReceiptData.get(nIndex).getM_oClientData().getM_nClientId() == oReceiptData.getM_oClientData().getM_nClientId())
				{
					IsReceiptDataExist = true;
					break;
				}
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("addReceiptData - oException : " + oException);
		}
		return IsReceiptDataExist;
	}

	private Set<InvoiceReceiptData> buildInvoiceReceiptData(Set<InvoiceReceiptData> oInvoiceReceipts) 
	{
		m_oLogger.debug("buildInvoiceData - oInvoiceReceipts [IN] : " + oInvoiceReceipts);
		Set<InvoiceReceiptData> oInvoiceReceiptSet = new HashSet<InvoiceReceiptData> ();
		Iterator<InvoiceReceiptData> oIterator = oInvoiceReceipts.iterator();
		while(oIterator.hasNext())
		{
			InvoiceReceiptData oInvoiceReceiptData = oIterator.next();
			InvoiceData oInvoiceData = oInvoiceReceiptData.getM_oInvoiceData();
			oInvoiceData.setM_strDate(getClientCompatibleFormat(oInvoiceData.getM_dCreatedOn()));
			oInvoiceReceiptData.setM_oInvoiceData(oInvoiceData);
			oInvoiceReceiptSet.add(oInvoiceReceiptData);
		}
		return oInvoiceReceiptSet;
	}

	private void isvalidUser(ReceiptData oReceiptData) throws Exception 
	{
		m_oLogger.debug ("isvalidUser - oReceiptData [IN] :" + oReceiptData);
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oReceiptData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oReceiptData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}

	private ArrayList<ReceiptData> buildReceiptDate(ArrayList<ReceiptData> arrReceiptData) 
	{
		m_oLogger.debug("buildPurchaseData - arrReceiptData [IN] : " + arrReceiptData);
		for (int nIndex=0; nIndex < arrReceiptData.size(); nIndex++)
			arrReceiptData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrReceiptData.get(nIndex).getM_dCreatedOn())); 
		return arrReceiptData;
	}
	
	private void createLog (ReceiptData oReceiptData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			ClientData oClientData = new ClientData ();
			oClientData.setM_nClientId(oReceiptData.getM_oClientData().getM_nClientId());
			oClientData = (ClientData) populateObject(oClientData);
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oReceiptData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oReceiptData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName + oClientData.getM_strCompanyName());
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}

	@Override
	public GenericResponse list(ReceiptData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
