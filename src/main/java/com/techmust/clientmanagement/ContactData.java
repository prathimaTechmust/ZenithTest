package com.techmust.clientmanagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="TCL03_Contact")
public class ContactData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="CL03_contact_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int m_nContactId;
	@Column(name="CL03_name")
	private String m_strContactName;
	@Column(name="CL03_telephone")
	private String m_strPhoneNumber;
	@Column(name="CL03_email")
	private String m_strEmail;
	@Column(name="CL03_department")
	private String m_strDepartment;
	@Column(name="CL03_designation")
	private String m_strDesignation;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="CL01_client_id")
	private ClientData m_oClientData;
	@Column(name="CL03_creation_date")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date m_dCreationDate;
	@Column(name="CL03_updation_date")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date m_dUpdationDate;
	
	public ContactData ()
	{
		m_nContactId = -1;
		m_strContactName = "";
		m_strPhoneNumber = "";
		m_strEmail = "";
		m_strDepartment = "";
		m_strDesignation = "";
		m_dCreationDate = Calendar.getInstance ().getTime ();
		m_dUpdationDate =  Calendar.getInstance ().getTime ();
		m_oClientData = new ClientData ();
	}
	
	public void setM_nContactId (int nContactId) 
	{
		m_nContactId = nContactId;
	}

	public int getM_nContactId ()
	{
		return m_nContactId;
	}
		
	public void setM_strContactName (String strContactName) 
	{
		m_strContactName = strContactName;
	}

	public String getM_strContactName () 
	{
		return m_strContactName;
	}
	
	public void setM_strPhoneNumber (String strPhoneNumber) 
	{
		m_strPhoneNumber = strPhoneNumber;
	}

	public String getM_strPhoneNumber () 
	{
		return m_strPhoneNumber;
	}
	
	public void setM_strEmail (String strEmail) 
	{
		m_strEmail = strEmail;
	}

	public String getM_strEmail () 
	{
		return m_strEmail;
	}
	
	public void setM_strDepartment (String strDepartment) 
	{
		m_strDepartment = strDepartment;
	}

	public String getM_strDepartment () 
	{
		return m_strDepartment;
	}

	public void setM_strDesignation (String strDesignation) 
	{
		m_strDesignation = strDesignation;
	}

	public String getM_strDesignation () 
	{
		return m_strDesignation;
	}
	
	public void setM_oClientData (ClientData oClientData) 
	{
		m_oClientData = oClientData;
	}

	public ClientData getM_oClientData () 
	{
		return m_oClientData;
	}
	
	public void setM_dCreationDate (Date dCreationDate)
	{
		m_dCreationDate = dCreationDate;
	}

	public Date getM_dCreationDate () 
	{
		return m_dCreationDate;
	}

	public void setM_dUpdationDate (Date dUpdationDate) 
	{
		m_dUpdationDate = dUpdationDate;
	}

	public Date getM_dUpdationDate () 
	{
		return m_dUpdationDate;
	}

	
	@Override
    public String generateXML ()
    {
		String strContactXml = "";
		m_oLogger.info ("generateXML");
	    try 
	    {
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "ContactData");
			addChild (oXmlDocument, oRootElement, "m_nContactId", m_nContactId);
			addChild (oXmlDocument, oRootElement, "m_strContactName", m_strContactName);
			addChild (oXmlDocument, oRootElement, "m_strPhoneNumber", m_strPhoneNumber);
			addChild (oXmlDocument, oRootElement, "m_strEmail", m_strEmail);
			addChild (oXmlDocument, oRootElement, "m_strDepartment", m_strDepartment);
			addChild (oXmlDocument, oRootElement, "m_strDesignation", m_strDesignation);
			strContactXml = getXmlString (oXmlDocument);
	    }
	    catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
	    return strContactXml;
    }
	
	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) throws Exception
	{
		ContactData oContactData = new ContactData ();
		try
		{
			Document oXMLDocument = getXmlDocument(strXML);
			Node oContactNode = oXMLDocument.getFirstChild();
			oContactData.setM_strContactName(UserManagementInitializer.getValue(oContactNode,"m_strContactName"));
			oContactData.setM_strPhoneNumber(UserManagementInitializer.getValue(oContactNode,"m_strPhoneNumber"));
			oContactData.setM_strEmail(UserManagementInitializer.getValue(oContactNode,"m_strEmail"));
			oContactData.setM_strDepartment(UserManagementInitializer.getValue(oContactNode,"m_strDepartment"));
			oContactData.setM_strDesignation(UserManagementInitializer.getValue(oContactNode,"m_strDesignation"));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getInstance - oException : " + oException);
			throw oException;
		}
		return oContactData;
	}
	
	public String buildMailXML(String strSubject, String strContent) 
	{
		String strMailXML = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "ContactMail");
			addChild (oXmlDocument, oRootElement, "strContent", strContent);
			addChild (oXmlDocument, oRootElement, "subject", strSubject);
			Document oContactDoc = getXmlDocument (generateXML ());
			Node oContactNode = oXmlDocument.importNode (oContactDoc.getFirstChild (), true);
			oRootElement.appendChild (oContactNode);
			strMailXML = getXmlString(oXmlDocument);
		} 
		catch (Exception oException)
		{
			GenericData.m_oLogger.error("buildMailXML - oException" + oException);
		}
		return strMailXML;
	}
	
	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{		
		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		Criteria oDemographyDataCriteria = oClientDataCriteria.createCriteria("m_oDemography");
		ArrayList<Criterion> arrCriterion = new ArrayList<Criterion> ();
		if (!m_strContactName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strContactName", m_strContactName, MatchMode.ANYWHERE));
		if (!m_strPhoneNumber.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strPhoneNumber", m_strPhoneNumber, MatchMode.ANYWHERE));
		if (!m_strEmail.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strEmail", m_strEmail, MatchMode.ANYWHERE));
		if (!m_strDepartment.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strDepartment", m_strDepartment, MatchMode.ANYWHERE));
		if (!m_strDesignation.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strDesignation", m_strDesignation, MatchMode.ANYWHERE));
		if(m_oClientData != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
			oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if (m_oClientData != null && m_oClientData.getM_nClientId () > 0)
			oCriteria.add (Restrictions.eq ("m_oClientData", m_oClientData));
		if (strColumn.contains("m_strCompanyName"))
			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nContactId");
		return oCriteria;
	}

	private void addDisjunctionCriteria(Criteria oDemographyDataCriteria, ArrayList<Criterion> arrCriterion) 
	{
		Disjunction oDisjunction = Restrictions.disjunction();
	    for (int nIndex = 0; nIndex < arrCriterion.size(); nIndex++)
	    	oDisjunction.add(arrCriterion.get(nIndex));
	    oDemographyDataCriteria.add(oDisjunction);
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*
 		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
 		Criteria oDemographyDataCriteria = oClientDataCriteria.createCriteria("m_oDemography");
		ArrayList<Criterion> arrCriterion = new ArrayList<Criterion> ();
		if(m_arrBusinessTypeData != null && m_arrBusinessTypeData.length > 0)
		{
			for (int nIndex = 0; nIndex < m_arrBusinessTypeData.length; nIndex ++)
				arrCriterion.add(Restrictions.eq ("m_oBusinessType", m_arrBusinessTypeData[nIndex]));
			addDisjunctionCriteria (oDemographyDataCriteria, arrCriterion);
		}
*/		
		if (!m_strContactName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strContactName")), m_strContactName.toLowerCase())); 
		if (!m_strPhoneNumber.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strPhoneNumber")), m_strPhoneNumber.toLowerCase())); 
		if (!m_strEmail.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strEmail")), m_strEmail.toLowerCase())); 
		if (!m_strDepartment.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strDepartment")), m_strDepartment.toLowerCase())); 
		if (!m_strDesignation.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strDesignation")), m_strDesignation.toLowerCase())); 
		if(m_oClientData != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("ClientData.m_strCompanyName")), m_oClientData.getM_strCompanyName().toLowerCase())); 
		if (m_oClientData != null && m_oClientData.getM_nClientId () > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("ClientData.m_nClientId"), m_oClientData.getM_nClientId())); 
//		if (strColumn.contains("m_strCompanyName"))
//			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
//		else
//			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nContactId");

		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nContactId"), m_nContactId));
		return oConjunct;
	}
}