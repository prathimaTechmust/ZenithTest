package com.techmust.helper;

import org.apache.log4j.Logger;
import org.directwebremoting.io.FileTransfer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.email.template.TemplateData;
import com.techmust.generic.email.template.TemplateDataProcessor;
import com.techmust.generic.response.GenericResponse;

@Controller
public class TemplateDataHelper extends TemplateDataProcessor
{
	
	Logger log=Logger.getLogger(TemplateDataHelper.class.getName());

	
	@RequestMapping(value="/templateDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse createTemplate (@RequestBody TemplateData oTemplateData, FileTransfer oFileTransfer) throws Exception 
	{
		GenericResponse oTemplateDataResponse=super.createTemplate(oTemplateData , oFileTransfer);
		return oTemplateDataResponse;
	}

//	@RequestMapping(value="/templateDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
//	@ResponseBody
//	public GenericResponse list (@RequestBody TradeMustHelper oData )throws Exception 
//	{
//		GenericResponse oTemplateDataResponse=super.list(oData.getM_oTemplateData(), oData.getM_strColumn(), oData.getM_strOrderBy(),oData.getM_nPageNo(),oData.getM_nPageSize());
//		return oTemplateDataResponse;
//	}
	
	@RequestMapping(value="/templateDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData (@RequestBody TemplateData oTemplateData) throws Exception 
	{
		GenericResponse oTemplateDataResponse=super.deleteData(oTemplateData);
		return oTemplateDataResponse;
	}

	@RequestMapping(value="/templateDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get (@RequestBody TemplateData oTemplateData) throws Exception 
	{
		GenericResponse oTemplateDataResponse=super.get(oTemplateData);
		return oTemplateDataResponse;
	}

	
	@RequestMapping(value="/templateDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody TemplateData oData) throws Exception
	{
		oData = (TemplateData) populateObject(oData);
	    return oData != null ? oData.generateXML () : "";
	}
	
	@RequestMapping(value="/templateDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updateTemplate (@RequestBody TemplateData oTemplateData, FileTransfer oFileTransfer) throws Exception 
	{
		GenericResponse oTemplateDataResponse=super.updateTemplate(oTemplateData, oFileTransfer);
		return oTemplateDataResponse;
	}	
}
