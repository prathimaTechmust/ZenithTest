package com.techmust.master.tax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;

@Controller
public class ApplicableTaxDataProcessor extends GenericIDataProcessor<ApplicableTax>
{

	@RequestMapping(value="/applicableTaxCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse create(@RequestBody ApplicableTax oApplicableTax) throws Exception
    {
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oApplicableTax [IN] : " + oApplicableTax);
		ApplicableTaxResponse oApplicableTaxResponse = new ApplicableTaxResponse ();
		try
		{
			HashSet<Tax> oTax = new HashSet<Tax> ();
			oTax.addAll (buildTaxList (oApplicableTax.m_arrTax));
			oTax.remove (null);
			oApplicableTax.setM_oTaxes(oTax);
			oApplicableTaxResponse.m_bSuccess = oApplicableTax.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oApplicableTaxResponse.m_bSuccess [OUT] : " + oApplicableTaxResponse.m_bSuccess);
		return oApplicableTaxResponse;
    }

	@Override
	@RequestMapping(value="/applicableTaxDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse deleteData(@RequestBody ApplicableTax oApplicableTax) throws Exception
    {
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oApplicableTax.getM_nId() [IN] : " + oApplicableTax.getM_nId());
		ApplicableTaxResponse oApplicableTaxResponse = new ApplicableTaxResponse ();
		try
		{
			oApplicableTaxResponse.m_bSuccess = oApplicableTax.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oApplicableTaxResponse.m_bSuccess [OUT] : " + oApplicableTaxResponse.m_bSuccess);
		return oApplicableTaxResponse;
    }

	@Override
	@RequestMapping(value="/applicableTaxGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse get(@RequestBody ApplicableTax oApplicableTax) throws Exception
    {
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oItemData.getM_nId() [IN] :" +oApplicableTax.getM_nId());
		ApplicableTaxResponse oApplicableTaxResponse = new ApplicableTaxResponse ();
		try 
		{
			oApplicableTax = (ApplicableTax) populateObject (oApplicableTax);
			oApplicableTaxResponse.m_arrApplicableTax.add (oApplicableTax);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oApplicableTaxResponse;
    }

	@Override
	@RequestMapping(value="/applicableTaxGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public String getXML(@RequestBody ApplicableTax oApplicableTax) throws Exception
    {
		oApplicableTax = (ApplicableTax) populateObject(oApplicableTax);
	    return oApplicableTax != null ? oApplicableTax.generateXML () : "";
    }

	@RequestMapping(value="/applicableTaxList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oApplicablTax(), oOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
    public GenericResponse list(ApplicableTax oApplicableTax, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize )
            throws Exception
    {
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oApplicableTax [IN] : " +oApplicableTax);
		ApplicableTaxResponse oApplicableTaxResponse = new ApplicableTaxResponse ();
		try 
		{
			oApplicableTaxResponse.m_nRowCount = getRowCount(oApplicableTax);
			oApplicableTaxResponse.m_arrApplicableTax = new ArrayList (oApplicableTax.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oApplicableTaxResponse;
    }

	@Override
	@RequestMapping(value="/applicableTaxUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse update(@RequestBody ApplicableTax oApplicableTax) throws Exception
    {
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oApplicableTax.getM_nId() [IN] : " + oApplicableTax.getM_nId());
		ApplicableTaxResponse oApplicableTaxResponse = new ApplicableTaxResponse ();
		try
		{
			HashSet<Tax> oTax = new HashSet<Tax> ();
			oTax.addAll (buildTaxList (oApplicableTax.m_arrTax));
			oTax.remove (null);
			oApplicableTax.setM_oTaxes(oTax);
			oApplicableTaxResponse.m_bSuccess = oApplicableTax.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oItemDataResponse.m_bSuccess [OUT] : " + oApplicableTaxResponse.m_bSuccess);
		return oApplicableTaxResponse;
    }
	
	@RequestMapping(value="/listApplicableTax", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ApplicableTaxResponse listApplicableTaxData () 
	{
		m_oLogger.info ("listApplicableTaxData");
		ApplicableTaxResponse oApplicableTaxResponse = new ApplicableTaxResponse ();
		Tax oTax = new Tax ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oApplicableTaxResponse.m_arrTax = new ArrayList (oTax.list (oOrderBy));
			oApplicableTaxResponse.m_bSuccess = true;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("listApplicableTaxData - oException : " +oException);
		}
		m_oLogger.debug ("listApplicableTaxData - oApplicableTaxResponse.m_arrTax.size [OUT] : "
							+oApplicableTaxResponse.m_arrTax.size ());
		return oApplicableTaxResponse;
	}
	
	private Collection<Tax> buildTaxList (Tax [] arrTaxData)
    {
		m_oLogger.info ("buildTaxList");
		m_oLogger.debug ("buildTaxList - arrTaxData.length [IN] : " + arrTaxData != null ? arrTaxData.length : 0);
		ArrayList<Tax> oArrayList = new ArrayList<Tax> ();
		try
		{
			for (int nIndex = 0; arrTaxData != null && nIndex < arrTaxData.length; nIndex++)
				oArrayList.add(arrTaxData [nIndex]);
		}
		catch(Exception oException)
		{
			m_oLogger.error ("buildTaxList - oException : " +oException);
		}
		return oArrayList;
    }

	@Override
	public GenericResponse list(ApplicableTax oApplicableTax, HashMap<String, String> arrOrderBy)
			throws Exception {
		// TODO Auto-generated method stub
		return list (oApplicableTax,arrOrderBy, 0, 0);
	}

}
