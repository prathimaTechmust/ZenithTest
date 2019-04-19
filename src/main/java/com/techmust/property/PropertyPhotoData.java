package com.techmust.property;

import java.awt.image.BufferedImage;
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

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;
@Entity
@Table(name = "tac46_property_photo")
public class PropertyPhotoData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac46_image_id")
	private int m_nImageId;
	@Column(name = "ac46_file_name")
	private String m_strFileName;
	@Column(name = "ac46_description")
	private String m_strDescription;
	@Transient
	private BufferedImage m_buffImage;
	@Column(name = "ac46_blob")
	@Lob
	private Blob m_oBlob;
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "ac46_creation_date")
	private Date m_dCreationDate;
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "ac46_updation_date")
	private Date m_dUpdationDate;
	@ManyToOne
	@JoinColumn(name = "ac46_property_id")
	private PropertyData m_oPropertyData;
	
	public PropertyPhotoData () 
	{
		m_nImageId = -1;
		m_strFileName = "";
		m_strDescription = "";
		m_oBlob = null;
		m_dCreationDate = Calendar.getInstance().getTime();
	}
	
	public int getM_nImageId () 
	{
		return m_nImageId;
	}

	public void setM_nImageId (int nImageId) 
	{
		m_nImageId = nImageId;
	}

	public String getM_strFileName () 
	{
		return m_strFileName;
	}

	public void setM_strFileName (String strFileName) 
	{
		m_strFileName = strFileName;
	}

	public String getM_strDescription () 
	{
		return m_strDescription;
	}

	public void setM_strDescription (String strDescription)
	{
		m_strDescription = strDescription;
	}

	public BufferedImage getM_buffImage ()
	{
		return m_buffImage;
	}

	public void setM_buffImage (BufferedImage buffImage)
	{
		m_buffImage = buffImage;
	}

	public Blob getM_oBlob () 
	{
		return m_oBlob;
	}

	public void setM_oBlob (Blob oBlob)
	{
		m_oBlob = oBlob;
	}

	public Date getM_dCreationDate () 
	{
		return m_dCreationDate;
	}

	public void setM_dCreationDate (Date dCreationDate)
	{
		m_dCreationDate = dCreationDate;
	}

	public Date getM_dUpdationDate ()
	{
		return m_dUpdationDate;
	}

	public void setM_dUpdationDate (Date dUpdationDate) 
	{
		m_dUpdationDate = dUpdationDate;
	}

	public void setM_oPropertyData (PropertyData oPropertyData)
	{
		m_oPropertyData = oPropertyData;
	}

	public PropertyData getM_oPropertyData () 
	{
		return m_oPropertyData;
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strXML = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "PropertyPhotoData");
			addChild (oXmlDocument, oRootElement, "m_nImageId", m_nImageId);
			addChild (oXmlDocument, oRootElement, "m_strDescription", m_strDescription);
			addChild (oXmlDocument, oRootElement, "m_strFileName", m_strFileName);
			addChild (oXmlDocument, oRootElement, "m_dCreationDate", m_dCreationDate.toString());
			strXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		m_oLogger.debug("generateXML - strXML [OUT] : " + strXML);
	    return strXML;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if(!m_strDescription.isEmpty())
			oCriteria.add(Restrictions.ilike("m_strDescription", m_strDescription, MatchMode.ANYWHERE));
		if(m_dCreationDate != null)
			oCriteria.add(Restrictions.ilike("m_dCreationDate", m_dCreationDate));
		addSortByCondition(oCriteria, strColumn, strOrderBy, "m_nImageId");
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
		if(!m_strDescription.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strDescription")), m_strDescription));
		/*if(m_dCreationDate != null)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_dCreationDate")), m_dCreationDate));*/
		//addSortByCondition(oCriteria, strColumn, strOrderBy, "m_nImageId");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nImageId"), m_nImageId)); 
		return oConjunct;
	}
}
