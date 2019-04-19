package com.techmust.helper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.email.template.TemplateDataProcessor;
import com.techmust.generic.response.GenericResponse;

@Controller
public class TemplateHelper extends TemplateDataProcessor 
{
	
//	@RequestMapping(value="/templateDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
//	@ResponseBody
//	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception
//	{
//		GenericResponse oGenericResponse = super.list(oTradeMustHelper.getM_oTemplateData(), oTradeMustHelper.getM_strColumn(),oTradeMustHelper.getM_strOrderBy(), oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
//		return oGenericResponse;
//	}

}
