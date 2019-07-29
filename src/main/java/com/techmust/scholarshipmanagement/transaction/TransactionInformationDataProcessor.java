package com.techmust.scholarshipmanagement.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.constants.Constants;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.ZenithHelper;
import com.techmust.scholarshipmanagement.student.StudentDataResponse;

public class TransactionInformationDataProcessor  extends GenericIDataProcessor<Transaction>
{

	@Override
	public GenericResponse create(Transaction oGenericData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse deleteData(Transaction oGenericData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(Transaction oGenericData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/transactionInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return List(oData.getM_oTransaction(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	@SuppressWarnings("unchecked")
	private GenericResponse List(Transaction oTransaction, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize ) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oTransaction [IN] : " + oTransaction);
		TransactionResponse oTransactionResponse = new TransactionResponse();
		try {
			
			oTransactionResponse.m_nRowCount = getRowCount(oTransaction);
			oTransactionResponse.m_arrTransaction = new ArrayList(oTransaction.list (arrOrderBy, nPageNumber, nPageSize));
		}
		catch (Exception oException)
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oTransactionResponse;
	}

	@Override
	public GenericResponse update(Transaction oGenericData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXML(Transaction oGenericData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse list(Transaction oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
