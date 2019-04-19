package com.techmust.helper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ContactDataProcessor;
import com.techmust.clientmanagement.ContactDataResponse;

@Controller
public class ContactDataHelper extends ContactDataProcessor
{
	Logger log=Logger.getLogger(ContactDataHelper.class.getName());

	
//	@RequestMapping(value="/contactDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
//	@ResponseBody
//	public ContactDataResponse list (@RequestBody TradeMustHelper oData) 
//	{
//		ContactDataResponse oContactResponse=super.list(oData.getM_oContactData(), oData.getM_strColumn(), oData.getM_strOrderBy());
//		return oContactResponse;
//	}
	
}
