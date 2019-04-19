package com.techmust.inventory.stocktransfer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.location.LocationData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name="tac09_itemlocation")
public class ItemLocationData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ac09_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int m_nId;
	@ManyToOne
	@JoinColumn(name="ac09_item_id")
	private ItemData m_oItemData;
	@ManyToOne
	@JoinColumn(name="ac09_locationId")
	private LocationData m_oLocationData;
	@Column(name="ac09_received")
	private int m_nReceived;
	@Column(name="ac09_issued")
	private int m_nIssued;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	
	public ItemLocationData ()
	{
		m_nId = -1;
		m_oItemData = new ItemData ();
		m_oLocationData = new LocationData ();
		m_nReceived = 0;
		m_nIssued = 0;
	}

	public void setM_nId(int nId) 
	{
		m_nId = nId;
	}

	public int getM_nId()
	{
		return m_nId;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oItemData(ItemData oItemData) 
	{
		m_oItemData = oItemData;
	}

	public LocationData getM_oLocationData() 
	{
		return m_oLocationData;
	}

	public void setM_oLocationData(LocationData oLocationData) 
	{
		m_oLocationData = oLocationData;
	}

	public int getM_nReceived() 
	{
		return m_nReceived;
	}

	public void setM_nReceived(int nReceived) 
	{
		m_nReceived = nReceived;
	}

	public int getM_nIssued() 
	{
		return m_nIssued;
	}

	public void setM_nIssued(int nIssued)
	{
		m_nIssued = nIssued;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData)
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData()
	{
		return m_oUserCredentialsData;
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strItemLocationXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ItemLocationData");
			addChild (oXmlDocument, oRootElement, "m_nReceived", m_nReceived);
			addChild (oXmlDocument, oRootElement, "m_nIssued", m_nIssued);
			strItemLocationXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strItemLocationXML;
	}

	public void received (float nQuantity)
	{
		m_nReceived += nQuantity;
	}
	
	public void issued (float nQuantity)
	{
		m_nIssued += nQuantity;
	}
	
	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oItemDataCriteria = oCriteria.createCriteria ("m_oItemData");
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oCriteria.add (Restrictions.eq ("m_oItemData", m_oItemData));
		if (m_oItemData.getM_strArticleNumber() != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemDataCriteria.add (Restrictions.eq ("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim()));
		if (m_oItemData.getM_strItemName() != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemDataCriteria.add (Restrictions.ilike ("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oItemData.getM_strBrand() != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemDataCriteria.add (Restrictions.ilike ("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
		if (m_oLocationData != null && m_oLocationData.getM_nLocationId() > 0)
			oCriteria.add (Restrictions.eq ("m_oLocationData", m_oLocationData));
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
//		Criteria oItemDataCriteria = oCriteria.createCriteria ("m_oItemData");
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oItemData"), m_oItemData)); 

/*	
 		if (m_oItemData.getM_strArticleNumber() != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemDataCriteria.add (Restrictions.eq ("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim()));
		if (m_oItemData.getM_strItemName() != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemDataCriteria.add (Restrictions.ilike ("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oItemData.getM_strBrand() != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemDataCriteria.add (Restrictions.ilike ("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
*/
		if (m_oLocationData != null && m_oLocationData.getM_nLocationId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oLocationData"), m_oLocationData)); 
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oItemData"), m_oItemData)); 
		if (m_oLocationData != null && m_oLocationData.getM_nLocationId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oLocationData"), m_oLocationData)); 
		return oConjunct;
	}
}
