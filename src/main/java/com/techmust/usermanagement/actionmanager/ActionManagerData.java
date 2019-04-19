package com.techmust.usermanagement.actionmanager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ActionManagerData extends MasterData 
{
	private static final long serialVersionUID = 1L;
	private String m_strUserName ;
	private String m_strLoginId ;
	private String m_strPassword ;
	private String m_strNewPassword;
	private int m_nUserId;
	
	public ActionManagerData ()
	{
		m_strUserName = "";
		m_strLoginId = "";
		m_strPassword = "";
		m_strNewPassword = "";
	}

	public void setM_strPassword (String strPassword)
	{
		m_strPassword = strPassword;
	}

	public String getM_strPassword () 
	{
		return m_strPassword;
	}

	public void setM_strUserName (String strUserName)
	{
		m_strUserName = strUserName;
	}

	public String getM_strUserName ()
	{
		return m_strUserName;
	}

	public void setM_strNewPassword (String strNewPassword) 
	{
		m_strNewPassword = strNewPassword;
	}

	public String getM_strNewPassword () 
	{
		return m_strNewPassword;
	}
	
	public void setM_strLoginId (String strLoginId) 
	{
		m_strLoginId = strLoginId;
	}

	public String getM_strLoginId () 
	{
		return m_strLoginId;
	}

	@Override
    public String generateXML ()
    {	
		m_oLogger.info ("generateXML");
		String strXML = "";
		try
		{
		Document oXmlDocument = createNewXMLDocument ();
		Element oRootElement = createRootElement (oXmlDocument, "ActionManagerData");
		addChild (oXmlDocument, oRootElement, "m_strUserName", m_strUserName);
		addChild (oXmlDocument, oRootElement, "m_strLoginId", m_strLoginId);
		addChild (oXmlDocument, oRootElement, "m_strPassword", m_strPassword);
		addChild (oXmlDocument, oRootElement, "m_strNewPassword", m_strNewPassword);
		strXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
	    m_oLogger.debug( "generateXML - strXML [OUT] : " + strXML);
	    return strXML ;
    }
	
	public void setM_nUserId(int nUserId)
    {
	    this.m_nUserId = nUserId;
    }

	public int getM_nUserId()
    {
	    return m_nUserId;
    }
	
	@Override
	protected Criteria listCriteria (Criteria criteria, String strColumn, String strOrderBy) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericData getInstanceData(String strXML,UserInformationData oCredentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
}