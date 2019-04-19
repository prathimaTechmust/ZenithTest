package com.techmust.master.tax;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;

@Controller
public class TaxDataProcessor extends GenericIDataProcessor<Tax>
{

	@RequestMapping(value="/taxCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse create(@RequestBody Tax oTax) throws Exception
    {
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oTax [IN] : " + oTax);
		TaxDataResponse oTaxDataResponse = new TaxDataResponse ();
		try
		{
			oTaxDataResponse.m_bSuccess = oTax.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oTaxDataResponse.m_bSuccess [OUT] : " + oTaxDataResponse.m_bSuccess);
		return oTaxDataResponse;
    }

	@RequestMapping(value="/taxDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse deleteData(@RequestBody Tax oTax) throws Exception
    {
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oTax.getM_nTaxId() [IN] : " + oTax.getM_nTaxId());
		TaxDataResponse oTaxDataResponse = new TaxDataResponse ();
		try
		{
			oTaxDataResponse.m_bSuccess = oTax.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oTaxDataResponse.m_bSuccess [OUT] : " + oTaxDataResponse.m_bSuccess);
		return oTaxDataResponse;
    }

	@RequestMapping(value="/taxGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse get(@RequestBody Tax oTax) throws Exception
    {
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oTax.getM_nTaxId() [IN] :" +oTax.getM_nTaxId());
		TaxDataResponse oTaxDataResponse = new TaxDataResponse ();
		try 
		{
			oTax = (Tax) populateObject (oTax);
			oTaxDataResponse.m_arrTax.add (oTax);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oTaxDataResponse;
    }

	@RequestMapping(value="/taxGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public String getXML(@RequestBody Tax oTax) throws Exception
    {
		oTax = (Tax) populateObject(oTax);
	    return oTax != null ? oTax.generateXML () : "";
    }

	@RequestMapping(value="/taxList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oTaxData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
    public GenericResponse list(Tax oTax, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
            throws Exception
    {
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oTax [IN] : " +oTax);
		TaxDataResponse oTaxDataResponse = new TaxDataResponse ();
		try 
		{
			oTaxDataResponse.m_nRowCount = getRowCount(oTax);
			oTaxDataResponse.m_arrTax = new ArrayList (oTax.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oTaxDataResponse;
    }

	@RequestMapping(value="/taxUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse update(@RequestBody Tax oTax) throws Exception
    {
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oTax.getM_nTaxId() [IN] : " + oTax.getM_nTaxId());
		TaxDataResponse oTaxDataResponse = new TaxDataResponse ();
		try
		{
			oTaxDataResponse.m_bSuccess = oTax.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oTaxDataResponse.m_bSuccess [OUT] : " + oTaxDataResponse.m_bSuccess);
		return oTaxDataResponse;
    }
	
	@RequestMapping(value="/getUniqueTaxNames", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getUniqueTaxNames ()
	{
		m_oLogger.info ("getUniqueTaxNames");
		TaxDataResponse oTaxDataResponse = new TaxDataResponse ();
		Tax oTax = new Tax ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oTaxDataResponse = (TaxDataResponse) list(oTax,oOrderBy);
			oTaxDataResponse = buildUniqueTaxes(oTaxDataResponse);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getUniqueTaxNames - oException : " + oException );
		}
		return oTaxDataResponse;
	}

	private TaxDataResponse buildUniqueTaxes(TaxDataResponse oTaxList) 
	{
		TaxDataResponse oTaxDataResponse = new TaxDataResponse ();
		for(int nIndex = 0; nIndex < oTaxList.m_arrTax.size(); nIndex++)
		{
			if(!isTaxExists(oTaxDataResponse, oTaxList.m_arrTax.get(nIndex)))
				oTaxDataResponse.m_arrTax.add(oTaxList.m_arrTax.get(nIndex));
		}
		return oTaxDataResponse;
	}

	private boolean isTaxExists(TaxDataResponse oTaxList, Tax oTax) 
	{
		boolean bTaxExists = false;
		for(int nIndex = 0; !bTaxExists && nIndex < oTaxList.m_arrTax.size(); nIndex++)
			bTaxExists = oTaxList.m_arrTax.get(nIndex).getM_strTaxName().equalsIgnoreCase(oTax.getM_strTaxName());
		return bTaxExists;
	}

	@Override
	public GenericResponse list(Tax oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
