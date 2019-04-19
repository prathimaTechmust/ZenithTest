package com.techmust.inventory.quotation;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.directwebremoting.io.FileTransfer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac47_quotation_attachment")
public class QuotationAttachmentData extends TenantData 
{
	private static final long serialVersionUID = 1L; 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac47_attachment_id")
	private int m_nAttachmentId;
	@Column(name = "ac47_attachment")
	@Lob
	private Blob m_oAttachment;
	@Column(name = "ac47_file_name")
	private String m_strFileName;
	@Transient
	private FileTransfer m_oFile;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac47_quotationId",nullable = false)
	private QuotationData m_oQuotationData;
	
	public QuotationAttachmentData () 
	{
		m_nAttachmentId = -1;
		m_oAttachment = null;
		m_strFileName = "";
		m_oFile = null;
		setM_oQuotationData(new QuotationData ());
	}
	
	public int getM_nAttachmentId () 
	{
		return m_nAttachmentId;
	}
	
	public void setM_nAttachmentId (int nAttachmentId) 
	{
		m_nAttachmentId = nAttachmentId;
	}
	
	public Blob getM_oAttachment () 
	{
		return m_oAttachment;
	}
	
	public void setM_oAttachment (Blob oAttachment) 
	{
		m_oAttachment = oAttachment;
	}
	
	public String getM_strFileName () 
	{
		return m_strFileName;
	}
	
	public void setM_strFileName (String strFileName) 
	{
		m_strFileName = strFileName;
	}
	
	public FileTransfer getM_oFile ()
	{
		return m_oFile;
	}
	public void setM_oFile (FileTransfer oFile) 
	{
		m_oFile = oFile;
	}
	
	public void setM_oQuotationData (QuotationData oQuotationData) 
	{
		m_oQuotationData = oQuotationData;
	}

	public QuotationData getM_oQuotationData () 
	{
		return m_oQuotationData;
	}

	@Override
	public String generateXML () 
	{
		String strXml = "";
		m_oLogger.info("generateXML");
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "QuotationAttachmentData");
			addChild (oXmlDocument, oRootElement, "m_nAttachmentId", m_nAttachmentId);
			addChild (oXmlDocument, oRootElement, "m_strFileName", m_strFileName);
			strXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strXml;
	}

	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String atrOrderBy)
	{
		oCriteria.add(Restrictions.eq("m_nAttachmentId", m_nAttachmentId));
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAttachmentId"), m_nAttachmentId)); 
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(m_nAttachmentId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAttachmentId"), m_nAttachmentId));
		return oConjunct;
	}
}
