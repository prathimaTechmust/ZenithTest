package com.techmust.inventory.quotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.serial.SerialType;
import com.techmust.traderp.util.TraderpUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class QuotationDataProcessor extends GenericIDataProcessor<QuotationData> 
{
	
	@RequestMapping(value="/quotationDatacreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create (@RequestBody QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oQuotationData [IN] : " + oQuotationData);
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try
		{
			createLog(oQuotationData, "QuotationDataProcessor.create : ");
			isvalidUser (oQuotationData);
			isCreditLimitExceeded(oQuotationData);
			oQuotationData.prepareQuotationData ();
			oQuotationData.setM_dDate(getDBCompatibleDateFormat(oQuotationData.getM_strDate()));
			oQuotationData.setM_strQuotationNumber(SerialNumberData.generateSerialNumber(SerialType.kQuotationNumber));
			oQuotationDataResponse.m_bSuccess = oQuotationData.saveObject();
			oQuotationData = (QuotationData) populateObject(oQuotationData);
			oQuotationDataResponse.m_arrQuotations.add(oQuotationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - QuotationDataResponse.m_bSuccess [OUT] : " + oQuotationDataResponse.m_bSuccess);
		return oQuotationDataResponse;
	}

	@RequestMapping(value="/quotationDatadeleteData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData (@RequestBody QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oQuotationData.getM_nQuotationId() [IN] : " + oQuotationData.getM_nQuotationId());
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try
		{
			oQuotationData.prepareAttachmentData ();
			oQuotationDataResponse.m_bSuccess = oQuotationData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oQuotationDataResponse.m_bSuccess [OUT] : " + oQuotationDataResponse.m_bSuccess);
		return oQuotationDataResponse;
	}

	@RequestMapping(value="/quotationDataget", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get (@RequestBody QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oQuotationData.getM_nQuotationId() [IN] :" +oQuotationData.getM_nQuotationId());
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try 
		{
			isvalidUser (oQuotationData);
			oQuotationData = (QuotationData) populateObject (oQuotationData);
			oQuotationData.customizeQuotationLineItems ();
			oQuotationData.setM_strDate (getClientCompatibleFormat (oQuotationData.getM_dDate()));
			oQuotationDataResponse.m_arrQuotations.add (oQuotationData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oQuotationDataResponse;
	}

	@RequestMapping(value="/quotationDatagetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML (@RequestBody QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.debug ("getXML - oQuotationData [IN] : "+ oQuotationData);
		try
		{
		isvalidUser (oQuotationData);
		oQuotationData = (QuotationData) populateObject(oQuotationData);
		oQuotationData.setM_strDate(getClientCompatibleFormat(oQuotationData.getM_dDate()));
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " , oException);
		}
		return oQuotationData != null ?oQuotationData.generateXML () : "";
	}

	@RequestMapping(value="/quotationDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oQuotationData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse list (QuotationData oQuotationData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)throws Exception
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oQuotationData [IN] : " +oQuotationData);
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try 
		{
			isvalidUser (oQuotationData);
			oQuotationDataResponse.m_nRowCount = getRowCount(oQuotationData);
			oQuotationDataResponse.m_arrQuotations = new ArrayList (oQuotationData.list (arrOrderBy, nPageNumber, nPageSize));
			oQuotationDataResponse.m_arrQuotations = buildQuotationData (oQuotationDataResponse.m_arrQuotations);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oQuotationDataResponse;
	}
	
	@RequestMapping(value="/quotationDataupdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oQuotationData.getM_nQuotationId() [IN] : " + oQuotationData.getM_nQuotationId());
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try
		{
			createLog(oQuotationData, "QuotationDataProcessor.update : ");
			isvalidUser (oQuotationData);
			isCreditLimitExceeded(oQuotationData);
			removePreviousChildren (oQuotationData);
			oQuotationData.deleteAttachmentData();
			oQuotationData.prepareQuotationData();
			oQuotationData.setM_dDate(getDBCompatibleDateFormat(oQuotationData.getM_strDate()));
			oQuotationDataResponse.m_bSuccess = oQuotationData.updateObject();
			oQuotationData = (QuotationData) populateObject(oQuotationData);
			oQuotationDataResponse.m_arrQuotations.add(oQuotationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oQuotationDataResponse.m_bSuccess [OUT] : " + oQuotationDataResponse.m_bSuccess);
		return oQuotationDataResponse;
	}
	
	@RequestMapping(value="/quotationDataarchive", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse archive (@RequestBody QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.info ("archive");
		m_oLogger.debug ("archive - oQuotationData.getM_nQuotationId() [IN] : " + oQuotationData.getM_nQuotationId());
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try
		{
			oQuotationData = (QuotationData) populateObject(oQuotationData);
			oQuotationData.setM_bIsArchived(true);
			oQuotationDataResponse.m_bSuccess = oQuotationData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("archive - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("archive - oQuotationDataResponse.m_bSuccess [OUT] : " + oQuotationDataResponse.m_bSuccess);
		return oQuotationDataResponse;
	}
	
	@RequestMapping(value="/quotationDataunArchive", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse unArchive (@RequestBody QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.info ("unArchive");
		m_oLogger.debug ("unArchive - oQuotationData.getM_nQuotationId() [IN] : " + oQuotationData.getM_nQuotationId());
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try
		{
			oQuotationData = (QuotationData) populateObject(oQuotationData);
			oQuotationData.setM_bIsArchived(false);
			oQuotationDataResponse.m_bSuccess = oQuotationData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("unArchive - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("unArchive - oQuotationDataResponse.m_bSuccess [OUT] : " + oQuotationDataResponse.m_bSuccess);
		return oQuotationDataResponse;
	}
	
	@RequestMapping(value="/quotationDatasaveAndSendMail", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse saveAndSendMail (@RequestBody QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.info ("saveAndSendMail");
		m_oLogger.debug ("saveAndSendMail - oQuotationData [IN] : " + oQuotationData);
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try
		{
			oQuotationDataResponse = oQuotationData.getM_nQuotationId() > 0 ? (QuotationDataResponse) update(oQuotationData) : (QuotationDataResponse) create(oQuotationData);
			oQuotationDataResponse = (QuotationDataResponse) sendMail (oQuotationDataResponse.m_arrQuotations.get(0));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("saveAndSendMail - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("saveAndSendMail - QuotationDataResponse.m_bSuccess [OUT] : " + oQuotationDataResponse.m_bSuccess);
		return oQuotationDataResponse;
	}
	
	@RequestMapping(value="/quotationDatasendMail", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse sendMail (@RequestBody QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.info ("SendMail");
		m_oLogger.debug ("SendMail - oQuotationData [IN] : " + oQuotationData);
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		String strXMLContent = "";
		try
		{
			QuotationData oData = new QuotationData ();
			oData.setM_nQuotationId(oQuotationData.getM_nQuotationId());
			oQuotationData = (QuotationData) populateObject (oData);
			strXMLContent = oQuotationData.generateXML();
			oQuotationDataResponse.m_bSuccess = TraderpUtil.sendEmail(oQuotationData.getM_oContactData().getM_strEmail(), "Quotation Details", strXMLContent, "printQuotations.xslt");
		}
		catch (Exception oException)
		{
			m_oLogger.error ("SendMail - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("SendMail - QuotationDataResponse.m_bSuccess [OUT] : " + oQuotationDataResponse.m_bSuccess);
		return oQuotationDataResponse;
	}
	
	@RequestMapping(value="/quotationDatasaveAndPrint", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse saveAndPrint(@RequestBody QuotationData oQuotationData) throws Exception
	{
		m_oLogger.info("saveAndPrint");
		m_oLogger.debug("saveAndPrint - oQuotationData [IN] : " + oQuotationData);
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try
		{
			oQuotationDataResponse = prepareForPrint((QuotationDataResponse) create(oQuotationData));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("saveAndPrint - oException : " + oException);
			throw oException;
		}
		return oQuotationDataResponse;
	}
	
	@RequestMapping(value="/quotationDataupdateAndPrin", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updateAndPrint(@RequestBody QuotationData oQuotationData) throws Exception
	{
		m_oLogger.info("updateAndPrint");
		m_oLogger.debug("updateAndPrint - oQuotationData [IN] : " + oQuotationData);
		QuotationDataResponse oQuotationDataResponse = new QuotationDataResponse ();
		try
		{
			oQuotationDataResponse = prepareForPrint((QuotationDataResponse) update(oQuotationData));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateAndPrint - oException : " + oException);
			throw oException;
		}
		return oQuotationDataResponse;
	}
	
	@RequestMapping(value="/quotationDataprepareForPrint", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	protected QuotationDataResponse prepareForPrint(@RequestBody QuotationDataResponse oQuotationDataResponse) throws Exception
    {
		QuotationData oQuotationData = oQuotationDataResponse.m_arrQuotations.get(0);
		ClientData oClientData = (ClientData) populateObject(oQuotationData.getM_oClientData());
		oQuotationData.setM_oClientData(oClientData);
		oQuotationDataResponse.m_strXMLData = oQuotationData.generateXML();	    
		return oQuotationDataResponse;
    }
	
	private void isvalidUser (QuotationData oQuotationData) throws Exception
	{
		m_oLogger.info("isvalidUser");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oQuotationData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oQuotationData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
	
	private void isCreditLimitExceeded(QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.debug("isCreditLimitExceeded - oQuotationData : " + oQuotationData);
		InvoiceData oInvoiceData = new InvoiceData ();
		oInvoiceData.setM_oClientData(oQuotationData.getM_oClientData());
		oInvoiceData.setM_bIsForClientOutstanding(true);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		ArrayList arrInvoice = new ArrayList (oInvoiceData.list (oOrderBy));
		m_oLogger.debug("isCreditLimitExceeded - arrInvoice.size() : " + arrInvoice.size());
		if (arrInvoice.size() > 0)
		  checkCreditExceeded (arrInvoice, oQuotationData);
	}
	
	private void checkCreditExceeded(ArrayList arrInvoice,QuotationData oQuotationData) throws Exception 
	{
		m_oLogger.info("checkCreditExceeded");
		Object[] arrInvoiceObject = (Object[])arrInvoice.get(0);
		ClientData oClientData = (ClientData) arrInvoiceObject[0];
		if(oClientData.getM_nCreditLimit() > 0)
		{
			double nBalanceAmount = (Double) arrInvoiceObject[3];
			double nQuotationLineItemAmount = oQuotationData.calculateQuotationLineItemTotal(oQuotationData.m_arrQuotationLineItems);
			double nExceededAmount = (nBalanceAmount + nQuotationLineItemAmount) - oClientData.getM_nCreditLimit();
			m_oLogger.debug("checkCreditExceeded - nExceededAmount : " + nExceededAmount);
			if(nExceededAmount > 0)
						throw new Exception ("Client Credit Limit Exceeded! \n" +
						"Client Credit limit - " + oClientData.getM_nCreditLimit()+ "\n" +
 						"Client Balance Amount - " + nBalanceAmount + "\n" +
						"This Quotation Amount - " + nQuotationLineItemAmount + "\n" +
						"Exceeded Amount - " + nExceededAmount);
		}
	}

	private void createLog (QuotationData oQuotationData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			ClientData oClientData = new ClientData ();
			oClientData.setM_nClientId(oQuotationData.getM_oClientData().getM_nClientId());
			oClientData = (ClientData) populateObject(oClientData);
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oQuotationData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oQuotationData.getM_oUserCredentialsData().getM_nUserId());
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}

	private void removePreviousChildren (QuotationData oQuotationData) throws Exception
    {
		m_oLogger.info ("removePreviousChilds");
		m_oLogger.debug ("removePreviousChilds - oSalesData [IN] : " + oQuotationData);
		try
		{
			oQuotationData = (QuotationData) populateObject(oQuotationData);
			Iterator<QuotationLineItemData> oIterator = oQuotationData.getM_oQuotationLineItems().iterator();
			while (oIterator.hasNext())
			{
				QuotationLineItemData oQuotationLineItemData = (QuotationLineItemData) oIterator.next();
				oQuotationLineItemData.deleteObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("removePreviousChilds - oException : " , oException);
			throw oException;
		}
    }
	
	private ArrayList<QuotationData> buildQuotationData(ArrayList<QuotationData> arrQuotationData) 
	{
		m_oLogger.info("buildQuotationData");
		for (int nIndex=0; nIndex < arrQuotationData.size(); nIndex++)
			arrQuotationData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrQuotationData.get(nIndex).getM_dDate())); 
		return arrQuotationData;
	}

	@RequestMapping(value="/createFromJsonquotationData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public QuotationDataResponse createFromJSON (@RequestBody QuotationData oData)
	{
		QuotationDataResponse oResponse = new QuotationDataResponse ();
		return oResponse;
	}

	@Override
	public GenericResponse list(QuotationData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
