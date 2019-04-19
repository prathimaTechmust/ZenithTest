package com.techmust.generic.email.template;

import java.sql.Blob;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.directwebremoting.io.FileTransfer;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac42_templates")
public class TemplateData extends TenantData
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac42_template_id")
	private int m_nTemplateId;
	@Column(name = "ac42_template_name")
	private String m_strTemplateName;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac42_created_on")
	private Date m_dCreatedOn;
	@Column(name = "ac42_Created_by")
	private int m_nCreatedBy;
	@Column(name = "ac42_template_file")
	@Lob
	private Blob m_oTemplateFile;
	@Column(name = "ac42_template_file_name")
	private String m_strTemplateFileName;
	@Transient
	private FileTransfer m_oFile;
	@ManyToOne
	@JoinColumn(name = "ac42_category_id")
	private TemplateCategoryData m_oCategoryData;

	public TemplateData ()
	{
		m_nTemplateId = -1;
		m_strTemplateName = "";
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_oTemplateFile = null;
		m_nCreatedBy = -1;
		m_strTemplateFileName = "";
		m_oFile = null;
		m_oCategoryData = new TemplateCategoryData ();
	}
	
	public int getM_nTemplateId () 
	{
		return m_nTemplateId;
	}

	public void setM_nTemplateId (int nTemplateId)
	{
		m_nTemplateId = nTemplateId;
	}

	public String getM_strTemplateName () 
	{
		return m_strTemplateName;
	}

	public void setM_strTemplateName (String strTemplateName) 
	{
		m_strTemplateName = strTemplateName;
	}

	public Date getM_dCreatedOn () 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn (Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public int getM_nCreatedBy () 
	{
		return m_nCreatedBy;
	}

	public void setM_oCreatedBy (int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public Blob getM_oTemplateFile () 
	{
		return m_oTemplateFile;
	}

	public void setM_oTemplateFile (Blob oTemplateFile) 
	{
		m_oTemplateFile = oTemplateFile;
	}

	public String getM_strTemplateFileName () 
	{
		return m_strTemplateFileName;
	}

	public void setM_strTemplateFileName (String strTemplateFileName)
	{
		m_strTemplateFileName = strTemplateFileName;
	}

	public FileTransfer getM_oFile () 
	{
		return m_oFile;
	}

	public void setM_oFile (FileTransfer oFile) 
	{
		m_oFile = oFile;
	}

	public TemplateCategoryData getM_oCategoryData ()
	{
		return m_oCategoryData;
	}

	public void setM_oCategoryData(TemplateCategoryData oCategoryData) 
	{
		m_oCategoryData = oCategoryData;
	}

	@Override
	public String generateXML() 
	{
		String strTemplateDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "TemplateData");
			addChild (oXmlDocument, oRootElement, "m_nTemplateId", m_nTemplateId);
			addChild (oXmlDocument, oRootElement, "m_strTemplateName", m_strTemplateName);
			addChild (oXmlDocument, oRootElement, "m_strTemplateFileName", m_strTemplateFileName);
			Document oCategoryDoc = getXmlDocument (m_oCategoryData.generateXML());
			Node oCategoryNode = oXmlDocument.importNode (oCategoryDoc.getFirstChild (), true);
			oRootElement.appendChild (oCategoryNode);
			strTemplateDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strTemplateDataXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oCategoryCriteria = oCriteria.createCriteria("m_oCategoryData");
		if (m_nTemplateId > 0)
			oCriteria.add (Restrictions.eq ("m_nTemplateId", m_nTemplateId));
		if (m_nCreatedBy  > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if (!m_strTemplateName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strTemplateName", m_strTemplateName.trim (), MatchMode.ANYWHERE));
		if (m_oCategoryData.getM_strCategoryName() != null && !m_oCategoryData.getM_strCategoryName().trim().isEmpty())
			oCategoryCriteria.add(Restrictions.eq ("m_strCategoryName", m_oCategoryData.getM_strCategoryName().trim()));
		if (strColumn.contains("m_strCategoryName"))
			oCategoryCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nTemplateId");
		return oCriteria;
	}
	

	@Override
	public GenericData getInstanceData(String strXML,
			UserInformationData oCredentials) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nTemplateId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nTemplateId"), m_nTemplateId)); 
		if (m_nCreatedBy > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCreatedBy"), m_nCreatedBy)); 
		if (!m_strTemplateName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strTemplateName")), m_strTemplateName.toLowerCase())); 
		if (m_oCategoryData.getM_strCategoryName() != null && !m_oCategoryData.getM_strCategoryName().trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strCategoryName"), m_oCategoryData.getM_strCategoryName().trim())); 
//		if (strColumn.contains("m_strCategoryName"))
//			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCategoryName")), m_oCategoryData.getM_strCategoryName().toLowerCase())); 
//		else
//			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nTemplateId");

		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nTemplateId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nTemplateId"), m_nTemplateId));
		return oConjunct;
	}
}
