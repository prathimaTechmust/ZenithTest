package com.techmust.inventory.items;

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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac65_item_images")
public class ItemImagesData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac65_id")
	private int m_nId;
	@Column(name = "ac65_image")
	@Lob
	private Blob m_oImages;
	@Column(name = "ac65_file_name")
	private String m_strFileName;
	@Transient
	private BufferedImage m_buffImgPhoto;
	@Column(name = "ac65_compressed_image")
	@Lob
	private Blob m_oCompressedPhoto;

	public ItemImagesData ()
	{
		m_nId = -1;
		m_oImages = null;
		m_strFileName = "";
		m_oCompressedPhoto = null;
	}
	
	public int getM_nId () 
	{
		return m_nId;
	}

	public void setM_nId (int nId)
	{
		m_nId = nId;
	}

	public Blob getM_oImages ()
	{
		return m_oImages;
	}

	public void setM_oImages (Blob oImages) 
	{
		m_oImages = oImages;
	}

	public String getM_strFileName () 
	{
		return m_strFileName;
	}

	public void setM_strFileName (String strFileName) 
	{
		m_strFileName = strFileName;
	}

	public BufferedImage getM_buffImgPhoto ()
	{
		return m_buffImgPhoto;
	}

	public void setM_buffImgPhoto(BufferedImage buffImgPhoto) 
	{
		m_buffImgPhoto = buffImgPhoto;
	}

	public Blob getM_oCompressedPhoto () 
	{
		return m_oCompressedPhoto;
	}

	public void setM_oCompressedPhoto (Blob oCompressedPhoto) 
	{
		m_oCompressedPhoto = oCompressedPhoto;
	}

	@Override
	public String generateXML() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericData getInstanceData(String strXML,
			UserInformationData credentials) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria criteria, String strColumn,
			String strOrderBy) {
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
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId));
		return oConjunct;
	}
}
