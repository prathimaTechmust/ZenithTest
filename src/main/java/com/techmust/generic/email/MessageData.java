package com.techmust.generic.email;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.techmust.generic.email.EMailStatus;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tg01_send_mail")
public class MessageData extends TenantData
{
	private static final long serialVersionUID = 1L;
	@Column(name = "g01_subject")
	private String m_strSubject;
	@Column(name = "g01_content")
	private String m_strContent;
	@Transient
	private ArrayList<String> m_arrAttachments;
	@Transient
	private ArrayList<String> m_arrRecipient;
	@Column(name = "g01_reply_to")
	private String m_strMessageFrom;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "g01_mail_id")
	private int m_nId;
	@Column(name = "g01_send_to")
	private String m_strSendTo;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "g01_created_on")
	private Date m_dCreationDate;
	@Column(name = "g01_status")
	@Enumerated(EnumType.ORDINAL)
	private EMailStatus m_nStatus;
	@Column(name = "g01_attachments")
	private String m_strAttachments;
	@ManyToOne
	@JoinColumn(name = "g01_email_message")
	private EmailMessageData m_oEmailMessageData;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "g01_send_date")
	private Date m_dSendDate;

	public MessageData ()
	{
		m_arrAttachments = new ArrayList<String> ();
		setM_strAttachments("");
		m_nStatus = EMailStatus.kToSend;
		setM_dCreationDate(Calendar.getInstance().getTime ());
		setM_dSendDate(null);
	}

	public void setM_strSubject(String strSubject)
	{
		this.m_strSubject = strSubject;
	}

	public String getM_strSubject()
	{
		return m_strSubject;
	}

	public void setM_strContent(String strContent) 
	{
		this.m_strContent = strContent;
	}

	public String getM_strContent() 
	{
		return m_strContent;
	}

	public void setM_arrAttachments(ArrayList<String> arrAttachments)
	{
		this.m_arrAttachments = arrAttachments;
	}

	public ArrayList<String> getM_arrAttachments()
	{
		return m_arrAttachments;
	}

	public void setM_arrRecipient(ArrayList<String> arrRecipient) 
	{
		this.m_arrRecipient = arrRecipient;
	}

	public ArrayList<String> getM_arrRecipient()
	{
		return m_arrRecipient;
	}

	@Override
	public String generateXML() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		oCriteria.add(Restrictions.eq("m_nStatus", m_nStatus));
		Calendar oCalendar = Calendar.getInstance();
		oCalendar.add(Calendar.DATE, -1); 
		oCriteria.add(Restrictions.disjunction().add(Restrictions.isNull("m_dSendDate"))
												.add (Restrictions.le("m_dSendDate", oCalendar.getTime())));
		return oCriteria;
	}	

	public void setM_nId(int nId) 
	{
		this.m_nId = nId;
	}

	public int getM_nId() 
	{
		return m_nId;
	}

	public void setM_strSendTo(String strSendTo) 
	{
		this.m_strSendTo = strSendTo;
	}

	public String getM_strSendTo() 
	{
		return m_strSendTo;
	}

	public void setM_nStatus(EMailStatus nStatus) 
	{
		this.m_nStatus = nStatus;
	}

	public void setM_dCreationDate(Date dCreationDate)
    {
	    this.m_dCreationDate = dCreationDate;
    }

	public Date getM_dCreationDate()
    {
	    return m_dCreationDate;
    }

	public EMailStatus getM_nStatus() 
	{
		return m_nStatus;
	}

	public void setM_strMessageFrom(String strMessageFrom) 
	{
		this.m_strMessageFrom = strMessageFrom;
	}

	public String getM_strMessageFrom() 
	{
		return m_strMessageFrom;
	}

	public void setM_strAttachments(String strAttachments) 
	{
		this.m_strAttachments = strAttachments;
	}

	public String getM_strAttachments() 
	{
		return m_strAttachments;
	}

	public void setM_oEmailMessageData(EmailMessageData oEmailMessageData) 
	{
		m_oEmailMessageData = oEmailMessageData;
	}

	public EmailMessageData getM_oEmailMessageData() 
	{
		return m_oEmailMessageData;
	}

	public void setM_dSendDate(Date dSendDate) 
	{
		this.m_dSendDate = dSendDate;
	}

	public Date getM_dSendDate() 
	{
		return m_dSendDate;
	}

	@Override
	public GenericData getInstanceData(String strXML,UserInformationData oCredentials) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nStatus"), m_nStatus)); 
		Calendar oCalendar = Calendar.getInstance();
		oCalendar.add(Calendar.DATE, -1); 
//		oCriteria.add(Restrictions.disjunction().add(Restrictions.isNull("m_dSendDate")).add (Restrictions.le("m_dSendDate", oCalendar.getTime())));
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
}