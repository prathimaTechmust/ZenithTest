package com.techmust.generic.email;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.email.template.TemplateData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.clientmanagement.ContactData;

@Entity
@Table(name = "tac27_emailmessage")
public class EmailMessageData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	
	// persistent data
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac27_emailmessage_id")
	private int m_nId;
	@Column(name = "ac27_subject")
	private String m_strSubject;
	@Column(name = "ac27_content")
	private String m_strContent;
	@ManyToMany
	@JoinTable(name = "tac28_email_contact", joinColumns = { @JoinColumn(name = "ac27_emailmessage_id") }, inverseJoinColumns = { @JoinColumn(name = "ac28_contact_id") })
	private Set<ContactData> m_oContact;
	@OneToMany
	@JoinColumn(name = "ac27_emailmessage_id")
	private Set<AttachmentData> m_oAttachment;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac27_created_on")
	private Date m_dCreatedOn;
	@ManyToOne
	@JoinColumn(name = "ac27_Template_Id")
	private TemplateData m_oTemplateData;
	@Transient
	private String m_strHTML;
	
	// non-persistent data used by client
	@Column(name = "ac27_Created_by")
	private int m_nCreatedBy;
	@Transient
	public ContactData [] m_arrContactData;
	@Transient
	public AttachmentData [] m_arrAttachmentData;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Transient
	private String m_strDate;
	@Transient
	private String m_strClientName;
	@Transient
	private String m_strFromDate = "";
	@Transient
    private String m_strToDate = "";
	
	public EmailMessageData ()
	{
		m_nId = -1;
		m_strSubject = "";
		m_strContent = "";
		m_oContact = new HashSet<ContactData> ();
		m_oAttachment = new HashSet<AttachmentData> ();
		m_nCreatedBy = -1;
		m_dCreatedOn = Calendar.getInstance().getTime ();
	}

	public int getM_nId () 
	{
		return m_nId;
	}

	public void setM_nId (int nId) 
	{
		m_nId = nId;
	}

	public String getM_strSubject () 
	{
		return m_strSubject;
	}

	public void setM_strSubject (String strSubject) 
	{
		m_strSubject = strSubject;
	}

	public String getM_strContent () 
	{
		return m_strContent;
	}

	public void setM_strContent (String strContent) 
	{
		m_strContent = strContent;
	}

	public Set<ContactData> getM_oContact () 
	{
		return m_oContact;
	}

	public void setM_oContact (Set<ContactData> oContact) 
	{
		m_oContact = oContact;
	}

	public Set<AttachmentData> getM_oAttachment () 
	{
		return m_oAttachment;
	}

	public void setM_oAttachment (Set<AttachmentData> oAttachment) 
	{
		m_oAttachment = oAttachment;
	}

	public int getM_nCreatedBy () 
	{
		return m_nCreatedBy;
	}

	public void setM_nCreatedBy (int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public Date getM_dCreatedOn () 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn (Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData)
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}

	public void setM_strDate(String m_strDate) 
	{
		this.m_strDate = m_strDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public String getM_strClientName() 
	{
		return m_strClientName;
	}

	public void setM_strClientName(String strClientName) 
	{
		m_strClientName = strClientName;
	}

	public void setM_oTemplateData(TemplateData oTemplateData) 
	{
		m_oTemplateData = oTemplateData;
	}

	public TemplateData getM_oTemplateData() 
	{
		return m_oTemplateData;
	}

	public void setM_strHTML(String strHTML) 
	{
		m_strHTML = strHTML;
	}

	public String getM_strHTML() 
	{
		return m_strHTML;
	}

	public void setM_strFromDate (String strFromDate) 
	{
		m_strFromDate = strFromDate;
	}

	public String getM_strFromDate () 
	{
		return m_strFromDate;
	}

	public void setM_strToDate (String strToDate)
	{
		m_strToDate = strToDate;
	}

	public String getM_strToDate ()
	{
		return m_strToDate;
	}

	@Override
	public String generateXML () 
	{
		String strEmailMessageDataXml = "";
		m_oLogger.info("generateXML");
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "EmailMessageData");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_strSubject", m_strSubject);
			addChild (oXmlDocument, oRootElement, "m_strContent", m_strContent);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
			Document oContactDataDoc = getXmlDocument ("<m_oContact>" + buildContactData () + "</m_oContact>");
			Node oContactNode = oXmlDocument.importNode (oContactDataDoc.getFirstChild (), true);
			oRootElement.appendChild (oContactNode);
			Document oAttachmentDataDoc = getXmlDocument ("<m_oAttachment>" + buildAttachmentData () + "</m_oAttachment>");
			Node oAttachmentNode = oXmlDocument.importNode (oAttachmentDataDoc.getFirstChild (), true);
			oRootElement.appendChild (oAttachmentNode);
			strEmailMessageDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strEmailMessageDataXml;
	}

	private String buildAttachmentData () 
	{
		String strXML = "";
		Object [] arrAttachment = m_oAttachment.toArray();
		for(int nIndex = 0; nIndex < arrAttachment.length; nIndex++)
		{
			AttachmentData oAttachmentData = (AttachmentData) arrAttachment [nIndex];
			strXML += oAttachmentData.generateXML();
		}
		return strXML;
	}

	private String buildContactData () 
	{
		String strXML = "";
		Object [] arrContact = m_oContact.toArray();
		for (int nIndex = 0; nIndex < arrContact.length; nIndex ++)
		{
			ContactData oContactData = (ContactData) arrContact [nIndex];
			strXML += oContactData.generateXML();
		}
		return strXML;
	}

	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_strSubject != null && !m_strSubject.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strSubject", m_strSubject.trim(),  MatchMode.ANYWHERE));
		if (m_nCreatedBy > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strDate != null && !m_strDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate, true)));
		if(m_strClientName != null && !m_strClientName.trim().isEmpty ())
		{
			Criteria oContactDataCriteria = oCriteria.createCriteria("m_oContact");
			Criteria oClientData = oContactDataCriteria.createCriteria ("m_oClientData");
			oClientData.add(Restrictions.ilike ("m_strCompanyName", m_strClientName.trim(), MatchMode.ANYWHERE));
		}
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nId");
		return oCriteria;
	}

	public Collection<AttachmentData> buildAttachmentList() 
	{
		m_oLogger.info("buildAttachmentList");
		ArrayList<AttachmentData> oArrayList = new ArrayList<AttachmentData> ();
		try
		{
			for (int nIndex = 0; nIndex < m_arrAttachmentData.length; nIndex++)
			{
				
				if (m_arrAttachmentData[nIndex].getM_oFile()!= null)// && m_arrAttachmentData[nIndex].getM_oFile().() > 0)
				{
					InputStream oInputStream = m_arrAttachmentData[nIndex].getM_oFile().getInputStream();
					m_arrAttachmentData[nIndex].setM_oAttachment(GenericIDataProcessor.getBlob (oInputStream));
				}
				else if(m_arrAttachmentData[nIndex].getM_buffImgPhoto() != null)
					m_arrAttachmentData[nIndex].setM_oAttachment(GenericIDataProcessor.getBlob(m_arrAttachmentData[nIndex].getM_buffImgPhoto()));
				else
				{
					AttachmentData oCollateralData = new AttachmentData ();
					oCollateralData.setM_nAttachmentId(m_arrAttachmentData[nIndex].getM_nAttachmentId());
					oCollateralData = (AttachmentData) GenericIDataProcessor.populateObject (oCollateralData);
					m_arrAttachmentData[nIndex].setM_oAttachment(oCollateralData.getM_oAttachment());
				}
				oArrayList.add(m_arrAttachmentData[nIndex]);
			}
		}
		catch(Exception oException)
		{
			m_oLogger.error("buildAttachmentList - oException : " +oException);
		}
		m_oLogger.debug("buildAttachmentList - oArrayList [OUT] : " +oArrayList);
		return oArrayList;
	}

	public Collection<ContactData> buildContacts()
	{
		m_oLogger.info("buildContacts");
		ArrayList<ContactData> oArrayList = new ArrayList<ContactData> ();
		try
		{
			for (int nIndex = 0; nIndex < m_arrContactData.length; nIndex++)
			{
				ContactData oContactData = new ContactData ();
				oContactData.setM_nContactId(m_arrContactData[nIndex].getM_nContactId());
				oArrayList.add(oContactData);
			}
		}
		catch(Exception oException)
		{
			m_oLogger.error("buildContacts - oException : " +oException);
		}
		m_oLogger.debug("buildContacts - oArrayList [OUT] : " +oArrayList);
		return oArrayList;
	}

	public void prepareData() 
	{
		HashSet<AttachmentData> oAttachments = new HashSet<AttachmentData> ();
		oAttachments.addAll(buildAttachmentList ());
		HashSet<ContactData> oContacts = new HashSet<ContactData> ();
		oContacts.addAll(buildContacts ());
		setM_oContact(oContacts);
		setM_oAttachment(oAttachments);
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
		if (m_strSubject != null && !m_strSubject.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strSubject")), m_strSubject.toLowerCase())); 
		if (m_nCreatedBy > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCreatedBy"), m_nCreatedBy)); 
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.ge(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false).toString())));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.le(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, false).toString())));
		if ((m_strDate != null && !m_strDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate, false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate, true).toString())));
/*
 		if(m_strClientName != null && !m_strClientName.trim().isEmpty ())
		{
			Criteria oContactDataCriteria = oCriteria.createCriteria("m_oContact");
			Criteria oClientData = oContactDataCriteria.createCriteria ("m_oClientData");
			oClientData.add(Restrictions.ilike ("m_strCompanyName", m_strClientName.trim(), MatchMode.ANYWHERE));
		}
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nId");
 */
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId));
		return oConjunct;
	}

}
