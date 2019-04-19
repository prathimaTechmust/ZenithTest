package com.techmust.usermanagement.actionmanager;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import com.techmust.generic.response.GenericResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.generic.util.GenericUtil;

public class ActionManagerResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	private static final String kAdminMenuXmlPath = "com/techmust/traderp/resource/adminmenu.defaults.xml";
	public String m_strUser;
	public String m_strLoginId;
	public String m_strActionsXml;
	public String m_strMenuHTML;
	public int m_nUserId;
	public long m_nUID;
	public ArrayList<ActionManagerData> m_arrActionManagerData;
	
	Logger m_oLogger = Logger.getLogger (ActionManagerResponse.class);
	
	public ActionManagerResponse ()
	{
		m_strUser = "";
		m_strLoginId = "";
		m_strActionsXml = "";
		m_nUserId = -1;
		m_nUID = -1;
		m_arrActionManagerData = new ArrayList<ActionManagerData> ();
	}

	public void set (UserInformationData oData)
	{
		m_strUser = oData.getM_strUserName ();
		m_strLoginId = oData.getM_strLoginId ();
		m_strActionsXml = oData.getActionsAsXML();//getAdminActionsAsXML ();
		m_nUserId = oData.getM_nUserId ();
		m_nUID = oData.getM_nUID();
		m_strMenuHTML = GenericUtil.buildHtml (m_strActionsXml, GenericUtil.getProjectProperty ("MENU_XSLT_FILE"));
		m_bSuccess = true;
	}

	private String getAdminActionsAsXML() 
	{
		String strAdminXML = "";
		try
		{
			InputStream oDefaultXmlStream = ActionManagerResponse.class.getClassLoader().getResourceAsStream (kAdminMenuXmlPath);
			StringWriter oWriter = new StringWriter();
			IOUtils.copy(oDefaultXmlStream, oWriter, "UTF-8");
			strAdminXML = oWriter.toString();
		}
		catch(Exception oException)
		{
			
		}
		return strAdminXML;
	}
	
}