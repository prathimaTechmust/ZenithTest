package com.techmust.generic.email;

import java.awt.image.BufferedImage;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.directwebremoting.io.FileTransfer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac30_attachment")
public class AttachmentData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ac30_attachment_id")
	private int m_nAttachmentId;
	@Column(name = "ac30_attachment")
	@Lob
	private Blob m_oAttachment;
	@Column(name = "ac30_file_name")
	private String m_strFileName;
	@Transient
	private BufferedImage m_buffImgPhoto;
	@Transient
	private FileTransfer m_oFile;
	
	public AttachmentData ()
	{
		m_nAttachmentId = -1;
		m_oAttachment = null;
		m_strFileName = "";
		setM_oFile(null);
	}

	public void setM_nAttachmentId (int nAttachmentId) 
	{
		m_nAttachmentId = nAttachmentId;
	}

	public int getM_nAttachmentId () 
	{
		return m_nAttachmentId;
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

	public void setM_buffImgPhoto(BufferedImage m_buffImgPhoto) 
	{
		this.m_buffImgPhoto = m_buffImgPhoto;
	}

	public BufferedImage getM_buffImgPhoto() 
	{
		return m_buffImgPhoto;
	}

	public void setM_oFile(FileTransfer oFile) 
	{
		m_oFile = oFile;
	}

	public FileTransfer getM_oFile()
	{
		return m_oFile;
	}

	@Override
	public String generateXML () 
	{
		String strAttachmentDataXML = "";
		m_oLogger.info("generateXML");
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "AttachmentData");
			addChild (oXmlDocument, oRootElement, "m_nAttachmentId", m_nAttachmentId);
			addChild (oXmlDocument, oRootElement, "m_strFileName", m_strFileName);
			strAttachmentDataXML = getXmlString(oXmlDocument);
		}
		catch (Exception oException)
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strAttachmentDataXML;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String atrOrderBy) 
	{
		if(m_nAttachmentId > 0)
			oCriteria.add(Restrictions.eq("m_nAttachmentId", m_nAttachmentId));
		return oCriteria;
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
		if(m_nAttachmentId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAttachmentId"), m_nAttachmentId)); 
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAttachmentId"), m_nAttachmentId));
		return oConjunct;
	}
}
