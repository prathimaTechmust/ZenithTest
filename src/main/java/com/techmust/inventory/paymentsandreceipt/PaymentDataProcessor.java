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

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.serial.SerialType;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class PaymentDataProcessor extends GenericIDataProcessor<PaymentData>
{
	@RequestMapping(value="/paymentDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody PaymentData oPaymentData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oPaymentData [IN] : " + oPaymentData);
		PaymentDataResponse oPaymentDataResponse = new PaymentDataResponse();
		try
		{
			isvalidUser (oPaymentData);
			oPaymentData.prepareForSaveOrUpdate();
			oPaymentData.setM_strPaymentNumber(SerialNumberData.generateSerialNumber(SerialType.kPaymentNumber));
			oPaymentDataResponse.m_bSuccess = oPaymentData.saveObject();
			oPaymentData.updatePaymentInvoices(oPaymentData);
			oPaymentDataResponse.m_arrPaymentData.add(oPaymentData);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("create - oException : " + oException);
		}
		m_oLogger.debug("create - oPaymentDataResponse [OUT] : " + oPaymentDataResponse.m_bSuccess);
		return oPaymentDataResponse;
	}

	@RequestMapping(value="/paymentDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData( @RequestBody PaymentData oPaymentData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oPaymentData [IN] : " + oPaymentData);
		PaymentDataResponse oPaymentDataResponse = new PaymentDataResponse();
		try
		{
			isvalidUser (oPaymentData);
			oPaymentDataResponse.m_bSuccess = oPaymentData.deleteObject();
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		}
		m_oLogger.debug("deleteData - oPaymentDataResponse [OUT] : " + oPaymentDataResponse.m_bSuccess);
		return oPaymentDataResponse;
	}

	@RequestMapping(value="/paymentDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody PaymentData oPaymentData) throws Exception 
	{
		m_oLogger.info ("get");
		PaymentDataResponse oPaymentDataResponse = new PaymentDataResponse();
		try 
		{
			isvalidUser (oPaymentData);
			oPaymentData =  (PaymentData) populateObject (oPaymentData);
			oPaymentData.setM_strDate (getClientCompatibleFormat (oPaymentData.getM_dCreatedOn()));
			oPaymentData.setM_oPurchases((buildPurchasePaymentData (oPaymentData.getM_oPurchases())));
			oPaymentDataResponse.m_arrPaymentData.add(oPaymentData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oPaymentDataResponse;
	}
	
	private Set<PurchasePaymentData> buildPurchasePaymentData(Set<PurchasePaymentData> oPurchasePayment) 
	{
		m_oLogger.debug("buildPurchasePaymentData - oInvoiceReceipts [IN] : " + oPurchasePayment);
		Set<PurchasePaymentData> oPurchasePaymentSet = new HashSet<PurchasePaymentData> ();
		Iterator<PurchasePaymentData> oIterator = oPurchasePayment.iterator();
		while(oIterator.hasNext())
		{
			PurchasePaymentData oPurchasePaymentData = oIterator.next();
			PurchaseData oPurchaseData = oPurchasePaymentData.getM_oPurchaseData();
			oPurchaseData.setM_strDate(getClientCompatibleFormat(oPurchaseData.getM_dCreatedOn()));
			oPurchasePaymentData.setM_oPurchaseData(oPurchaseData);
			oPurchasePaymentSet.add(oPurchasePaymentData);
		}
		return oPurchasePaymentSet;
	}

	@RequestMapping(value="/paymentDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody PaymentData oPaymentData) throws Exception 
	{
		m_oLogger.debug("getXML - oPaymentData [IN] : " + oPaymentData);
		try
		{
			isvalidUser (oPaymentData);
			oPaymentData = (PaymentData) populateObject(oPaymentData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " + oException);
		}
		return oPaymentData != null ?oPaymentData.generateXML () : ""; 
	}

	@RequestMapping(value="/paymentDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		GenericResponse oPaymentDataResponse = list(oTradeMustHelper.getM_oPaymentData(),oOrderBy, oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
		return oPaymentDataResponse;
	}
	
	public GenericResponse list(PaymentData oPaymentData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oPaymentData [IN] : " + oPaymentData);
		PaymentDataResponse oPaymentDataResponse = new PaymentDataResponse();
		try 
		{               
			isvalidUser (oPaymentData);
			oPaymentDataResponse.m_nRowCount = getRowCount(oPaymentData);
			oPaymentDataResponse.m_arrPaymentData = new ArrayList (oPaymentData.list (arrOrderBy, nPageNumber, nPageSize));
			oPaymentDataResponse.m_arrPaymentData = buildPurchaseData (oPaymentDataResponse.m_arrPaymentData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		m_oLogger.debug("list - oResponse.m_arrPaymentData.size () [OUT] : " + oPaymentDataResponse.m_arrPaymentData.size());
		return oPaymentDataResponse;
	}
	
	private ArrayList<PaymentData> buildPurchaseData(ArrayList<PaymentData> arrPaymentData) 
	{
		m_oLogger.debug("buildPurchaseData - arrPaymentData [IN] : " + arrPaymentData);
		for (int nIndex=0; nIndex < arrPaymentData.size(); nIndex++)
			arrPaymentData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrPaymentData.get(nIndex).getM_dCreatedOn())); 
		return arrPaymentData;
	}

	@RequestMapping(value="/paymentDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update( @RequestBody PaymentData oPaymentData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oPaymentData [IN] : " + oPaymentData);
		PaymentDataResponse oPaymentDataResponse = new PaymentDataResponse();
		try
		{
			isvalidUser (oPaymentData);
			removePreviousChildren (oPaymentData);
			oPaymentData.prepareForSaveOrUpdate();
			oPaymentDataResponse.m_bSuccess = oPaymentData.updateObject();
			oPaymentData.updatePaymentInvoices(oPaymentData);
			oPaymentDataResponse.m_arrPaymentData.add(oPaymentData);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		m_oLogger.debug("update - oReceiptDataResponse [OUT] : " + oPaymentDataResponse.m_bSuccess);
		return oPaymentDataResponse;
	}
	
	private void removePreviousChildren(PaymentData oPaymentData) throws Exception 
	{
		m_oLogger.info ("removePreviousChildren");
		m_oLogger.debug ("removePreviousChildren - oPaymentData [IN] : " + oPaymentData);
		try
		{
			oPaymentData = (PaymentData) populateObject(oPaymentData);
			Iterator<PurchasePaymentData> oIterator = oPaymentData.getM_oPurchases().iterator();
			while (oIterator.hasNext())
			{
				PurchasePaymentData oPurchasePaymentData = (PurchasePaymentData) oIterator.next();
				oPurchasePaymentData.getM_oPurchaseData().removePaymentAmount(oPurchasePaymentData);
				oPurchasePaymentData.deleteObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("removePreviousChildren - oException : " , oException);
			throw oException;
		}
	}
	@RequestMapping(value="/paymentDataGetReports", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ReportPaymentDataResponse getReports ( @RequestBody PaymentData oPaymentData)
	{
		m_oLogger.info ("getReports");
		m_oLogger.debug ("getReports - oPaymentData [IN] : " + oPaymentData);
		ReportPaymentDataResponse oReportPaymentDataResponse = new ReportPaymentDataResponse ();
		ArrayList<ReportPaymentData> m_arrReportPaymentData = new ArrayList<ReportPaymentData> ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			PaymentDataResponse oPaymentDataResponse = (PaymentDataResponse) list(oPaymentData, oOrderBy);
			for (int nIndex=0; nIndex < oPaymentDataResponse.m_arrPaymentData.size(); nIndex++)
			{
				if(IsPaymentDataExist (oPaymentDataResponse.m_arrPaymentData.get(nIndex), m_arrReportPaymentData))
					updatePaymentData(oPaymentDataResponse.m_arrPaymentData.get(nIndex), m_arrReportPaymentData);
				else
					addPaymentData (oPaymentDataResponse.m_arrPaymentData.get(nIndex), m_arrReportPaymentData);
			}
			oReportPaymentDataResponse.m_arrReportPaymentData = m_arrReportPaymentData;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getReports - oException : " + oException);
		}
		m_oLogger.debug("getReports - oPaymentDataResponse [OUT] : " + oReportPaymentDataResponse.m_bSuccess);
		return oReportPaymentDataResponse;
	}
	@RequestMapping(value="/paymentDataSaveAndPrint", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse saveAndPrint(@RequestBody PaymentData oPaymentData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oPaymentData);
		PaymentDataResponse oPaymentDataResponse = new PaymentDataResponse ();
		try
		{
			oPaymentDataResponse = oPaymentData.getM_nPaymentId() > 0 ?(PaymentDataResponse) update(oPaymentData) : (PaymentDataResponse) create(oPaymentData);
			oPaymentData = (PaymentData) populateObject (oPaymentDataResponse.m_arrPaymentData.get(0));
			oPaymentDataResponse.m_arrPaymentData.add(oPaymentData);
			oPaymentDataResponse.m_strXMLData = getXML(oPaymentData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oPurchaseOrderResponse.m_bSuccess [OUT] : " + oPaymentDataResponse.m_bSuccess);
		return oPaymentDataResponse;
	}
	
	public FileTransfer exportToTally (PaymentData oPaymentData) throws Exception
    {
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
    	PaymentDataResponse oPaymentDataResponse = (PaymentDataResponse) list(oPaymentData, oOrderBy);
    	String strTallyXml = "";
    	strTallyXml = GenericUtil.buildHtml(oPaymentData.generateTallyDataXML(oPaymentDataResponse.m_arrPaymentData), GenericUtil.getProperty("kTallyPaymentFormatXSLT"));
    	InputStream oInputStream = new ByteArrayInputStream(strTallyXml.getBytes());
    	FileTransfer oDwrXMLFile = new FileTransfer("tally_pymt_"+System.currentTimeMillis()+".xml", "application/xml", oInputStream);
    	return oDwrXMLFile;
    }
	
	private void updatePaymentData(PaymentData oPaymentData, ArrayList<ReportPaymentData> arrReportPaymentData) 
	{
		m_oLogger.info ("updatePaymentData");
		m_oLogger.debug ("updatePaymentData - oPaymentData [IN] : " + oPaymentData);
		try
		{
			for (int nIndex=0; nIndex < arrReportPaymentData.size(); nIndex++)
			{
				if (arrReportPaymentData.get(nIndex).getM_oVendorData().getM_nClientId() == oPaymentData.getM_oVendorData().getM_nClientId())
				{	
					arrReportPaymentData.get(nIndex).setReportData(oPaymentData);
					break;
				}
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("addPaymentData - oException : " + oException);
		}
	}

	private void addPaymentData(PaymentData oPaymentData, ArrayList<ReportPaymentData> arrReportPaymentData) 
	{
		m_oLogger.info ("addPaymentData");
		m_oLogger.debug ("addPaymentData - oPaymentData [IN] : " + oPaymentData);
		try
		{
			ReportPaymentData oReportPaymentData = new ReportPaymentData ();
			oReportPaymentData.setReportData(oPaymentData);
			arrReportPaymentData.add(oReportPaymentData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("addPaymentData - oException : " + oException);
		}
	}

	private boolean IsPaymentDataExist(PaymentData oPaymentData, ArrayList<ReportPaymentData> arrReportPaymentData) 
	{
		m_oLogger.info ("IsPaymentDataExist");
		m_oLogger.debug ("IsPaymentDataExist - oPaymentData [IN] : " + oPaymentData);
		boolean IsPaymentDataExist = false;
		try
		{
			for (int nIndex=0; nIndex < arrReportPaymentData.size(); nIndex++)
			{
				if (arrReportPaymentData.get(nIndex).getM_oVendorData().getM_nClientId() == oPaymentData.getM_oVendorData().getM_nClientId())
				{
					IsPaymentDataExist = true;
					break;
				}
			}
		}
		catch (Exception oException)   
		{
			m_oLogger.error("addPaymentData - oException : " + oException);
		}
		return IsPaymentDataExist;
	}

	
	private void isvalidUser(PaymentData oPaymentData) throws Exception 
	{
		m_oLogger.debug ("isvalidUser - oPaymentData [IN] :" + oPaymentData);
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oPaymentData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oPaymentData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}

	@Override
	public GenericResponse list(PaymentData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
