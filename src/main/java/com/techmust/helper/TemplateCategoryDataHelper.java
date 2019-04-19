package com.techmust.helper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.email.template.TemplateCategoryData;
import com.techmust.generic.email.template.TemplateCategoryDataProcessor;
import com.techmust.generic.email.template.TemplateCategoryDataResponse;
import com.techmust.generic.response.GenericResponse;

@Controller 
public class TemplateCategoryDataHelper extends TemplateCategoryDataProcessor 
{
	Logger log=Logger.getLogger(TemplateCategoryDataHelper.class.getName());

	
//	@RequestMapping(value="/templateCategoryDatalist", method = RequestMethod.POST, headers = {"Content-type=application/json"})
//	@ResponseBody
//	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception 
//	{
//		GenericResponse	 oCategoryDataResponse=super.list(oData.getM_oTemplateCategoryData(), oData.getM_strColumn(), oData.getM_strOrderBy(),1,10);
//		return oCategoryDataResponse;
//	}
	
	@RequestMapping(value="/templateCategoryDatacreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody TemplateCategoryData oData) throws Exception 
	{
		GenericResponse oCategoryDataResponse=super.create(oData);
		return oCategoryDataResponse;
	}
	
	@RequestMapping(value="/templateCategoryDataget", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody TemplateCategoryData oData) throws Exception
	{
		GenericResponse oCategoryDataResponse=super.get(oData);
		return oCategoryDataResponse;
	}
	
	@RequestMapping(value="/templateCategoryDatagetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody TemplateCategoryData oData) throws Exception
	{
		oData = (TemplateCategoryData) populateObject(oData);
	    return oData != null ? oData.generateXML () : "";
	}
	
	@RequestMapping(value="/templateCategoryDataupdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody TemplateCategoryData oData)	throws Exception
	{
		GenericResponse oCategoryDataResponse=super.update(oData);
		return oCategoryDataResponse;
	}
	
	@RequestMapping(value="/templateCategoryDatadeleteData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody TemplateCategoryData oData) throws Exception 
	{
		GenericResponse oCategoryDataResponse=super.deleteData(oData);
		return oCategoryDataResponse;
	}



}
