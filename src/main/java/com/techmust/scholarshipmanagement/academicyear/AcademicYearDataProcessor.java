package com.techmust.scholarshipmanagement.academicyear;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.ZenithHelper;
import com.techmust.utils.Utils;

@Controller
public class AcademicYearDataProcessor extends GenericIDataProcessor<AcademicYear>
{

	@Override
	@RequestMapping(value="/academicyearcreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody	
	public GenericResponse create(@RequestBody AcademicYear oAcademicYear) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oAcademicYear [IN] : " + oAcademicYear);
		AcademicYearResponse oAcademicYearResponse = new AcademicYearResponse();
		try
		{
			oAcademicYearResponse.m_bSuccess = oAcademicYear.saveObject();
			Utils.createActivityLog("AcademicYearDataProcessor::create", oAcademicYear);
			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oAcademicYearResponse;
	}

	
	@RequestMapping(value="/academicyearInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oZenithHelper) throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String,String>();
		oOrderBy.put(oZenithHelper.getM_strSortColumn(), oZenithHelper.getM_strOrderBy());
		return list(oZenithHelper.getM_oAcademicYear(),oOrderBy,oZenithHelper.getM_nPageNo(),oZenithHelper.getM_nPageSize());
	}

	@SuppressWarnings("unchecked")
	private GenericResponse list(AcademicYear oAcademicYear, HashMap<String, String> arrOrderBy, int nPageNumber,	int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oAcademicYear [IN] : " + oAcademicYear);
		AcademicYearResponse oAcademicYearResponse = new AcademicYearResponse();
		try 
		{
			oAcademicYearResponse.m_nRowCount = getRowCount(oAcademicYear);
			oAcademicYearResponse.m_arrAcademicYear = new ArrayList (oAcademicYear.listCustomData(this));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oAcademicYearResponse;
	}

	@RequestMapping(value = "/academicyearcreateandupdate",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse createAndUpdate (@RequestBody AcademicYear oAcademicYear)
	{
		m_oLogger.info("createAndUpdate");
		m_oLogger.debug("createAndUpdate - AcademicYear"+oAcademicYear);
		AcademicYearResponse oAcademicYearResponse = new AcademicYearResponse();
		try 
		{
			if(checkAcademicExists(oAcademicYear))
			{
				oAcademicYearResponse.m_bSuccess = updateAcademicYearData(oAcademicYear);
			}				
			else
			{
				updateOldAcademicValue(oAcademicYear);
				oAcademicYearResponse.m_bSuccess = oAcademicYear.saveObject();
			}
				
		}
		catch (Exception oException)
		{
			m_oLogger.error("createAndUpdate - oException"+oException);
		}
		return oAcademicYearResponse;		
	}

	private boolean updateAcademicYearData(AcademicYear oData)
	{
		m_oLogger.info("updateAcademicYearData");
		m_oLogger.debug("updateAcademicYearData - AcademicYear"+oData);
		boolean bIsUpdate = false;
		AcademicYear oAcademicYear = new AcademicYear();
		oAcademicYear.setM_strAcademicYear(oData.getM_strAcademicYear());
		try
		{
			AcademicYear oYear = (AcademicYear) populateObject(oAcademicYear);			
			oYear.setM_bDefaultYear(oData.isM_bDefaultYear());
			oYear.setM_dUpdatedOn(Calendar.getInstance().getTime());
			oYear.setM_oUserUpdatedBy(oData.getM_oUserUpdatedBy());
			updateOldAcademicValue(oData);
			bIsUpdate = oYear.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateAcademicYearData - oException"+oException);
		}
		return bIsUpdate;		
	}

	private void updateOldAcademicValue(AcademicYear oData)
	{
		AcademicYear oYear = new AcademicYear();
		oYear.setM_bDefaultYear(true);
		try
		{
			oYear = (AcademicYear) populateObject(oYear);
			oYear.setM_bDefaultYear(false);
			oYear.setM_dUpdatedOn(Calendar.getInstance().getTime());
			oYear.setM_oUserUpdatedBy(oData.getM_oUserUpdatedBy());
			boolean isUpdate = oYear.updateObject();			
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateOldAcademicValue - oException"+oException);
		}		
	}


	private boolean checkAcademicExists(AcademicYear oAcademicYearData)
	{
		boolean bIsExists = false;
		m_oLogger.info("checkAcademicExists");
		m_oLogger.debug("checkAcademicExists - AcademicYear"+oAcademicYearData);
		AcademicYear oAcademicYear = new AcademicYear();
		oAcademicYear.setM_strAcademicYear(oAcademicYearData.getM_strAcademicYear());
		try
		{			
			AcademicYear oYear = (AcademicYear) populateObject(oAcademicYear);
			if(oYear != null)
				bIsExists = true;
		} 
		catch (Exception oException)
		{
			m_oLogger.error("checkAcademicExists - oException"+oException);
		}		
		return bIsExists;		
	}


	@RequestMapping(value = "/academicyearupdate",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody	
	public GenericResponse update(@RequestBody ZenithHelper oZenithHelper) throws Exception
	{		
		return null;
	}
	
	@Override
	public GenericResponse deleteData(AcademicYear oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(AcademicYear oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXML(AcademicYear oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse list(AcademicYear oGenericData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse update(AcademicYear oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
}
