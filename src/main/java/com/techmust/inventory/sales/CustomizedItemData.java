package com.techmust.inventory.sales;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.inventory.items.ItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac50_article_customize")
public class CustomizedItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private CustomizedItemDataEmmbedable compositeId;
	@Transient
	private int m_nCustomizeId;
	@ManyToOne
	@JoinColumn(name = "ac5X_Client_id")
	private ClientData m_oClientData;
	@ManyToOne
	@JoinColumn(name = "ac5X_item_id")
	private ItemData m_oItemData;
	@Column(name = "ac50_client_article_number")
	private String m_strClientArticleNumber; 
	@Column(name = "ac50_client_article_description")
	private String m_strClientArticleDescription;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac50_created_on")
	private Date m_dCreatedOn;
	@Column(name = "ac50_Created_by")
	private int m_nCreatedBy;
	@Transient
	private String m_strItemArticleNumber; 
	
	public CustomizedItemData()
	{
		m_strClientArticleNumber = "";
		m_strClientArticleDescription = "";
		m_strItemArticleNumber = "";
		m_oClientData = new ClientData ();
		m_oItemData = new ItemData ();
		m_dCreatedOn = Calendar.getInstance ().getTime ();
	}
	
	

	public CustomizedItemDataEmmbedable getCompositeId()
	{
		return compositeId;
	}



	public void setCompositeId(CustomizedItemDataEmmbedable compositeId) 
	{
		this.compositeId = compositeId;
	}

	public int getM_nCustomizeId() 
	{
		return m_nCustomizeId;
	}

	public void setM_nCustomizeId(int nCustomizeId) 
	{
		m_nCustomizeId = nCustomizeId;
	}

	public ClientData getM_oClientData() 
	{
		return m_oClientData;
	}

	public void setM_oClientData(ClientData oClientData) 
	{
		m_oClientData = oClientData;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oItemData(ItemData oItemData)
	{
		m_oItemData = oItemData;
	}

	public String getM_strClientArticleNumber() 
	{
		return m_strClientArticleNumber;
	}

	public void setM_strClientArticleNumber(String strClientArticleNumber)
	{
		m_strClientArticleNumber = strClientArticleNumber;
	}

	public String getM_strClientArticleDescription() 
	{
		return m_strClientArticleDescription;
	}

	public void setM_strClientArticleDescription(String strClientArticleDescription)
	{
		m_strClientArticleDescription = strClientArticleDescription;
	}

	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public int getM_nCreatedBy()
	{
		return m_nCreatedBy;
	}

	public void setM_nCreatedBy(int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public String getM_strItemArticleNumber() 
	{
		return m_strItemArticleNumber;
	}

	public void setM_strItemArticleNumber(String strItemArticleNumber)
	{
		m_strItemArticleNumber = strItemArticleNumber;
	}

	@Override
	public String generateXML() 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oItemCriteria = oCriteria.createCriteria("m_oItemData");
		if (!m_strClientArticleNumber.trim().isEmpty ())
			oCriteria.add (Restrictions.eq ("m_strClientArticleNumber", m_strClientArticleNumber.trim()));
		if (m_strClientArticleDescription != null && !m_strClientArticleDescription.trim().isEmpty ())
			oCriteria.add (Restrictions.eq ("m_strArticleNumber", m_strClientArticleDescription.trim()));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientData", m_oClientData));
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oCriteria.add (Restrictions.eq ("m_oItemData", m_oItemData));
		if (m_oItemData != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemCriteria.add(Restrictions.eq("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim()));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strItemName");
		return oCriteria;
	}
	
	public void prepareCustomizedData() throws Exception 
	{
		ItemData oItemData = ItemData.getInstance(m_strItemArticleNumber, null);
		m_oItemData = oItemData;
	}

	@SuppressWarnings("unchecked")
	public static CustomizedItemData getItemInstance(String strClientArticleNumber, int nClientId, int nItemId) throws Exception 
	{
		CustomizedItemData oCustomizedItemData = new CustomizedItemData ();
		oCustomizedItemData.getM_oClientData().setM_nClientId(nClientId);
		oCustomizedItemData.setM_strClientArticleNumber(strClientArticleNumber);
		oCustomizedItemData.getM_oItemData().setM_nItemId(nItemId);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		ArrayList<CustomizedItemData> arrCustomizedItemData = new ArrayList(oCustomizedItemData.list(oOrderBy));
		oCustomizedItemData = arrCustomizedItemData.size() > 0 ? arrCustomizedItemData.get(0) : null;
		return oCustomizedItemData;
	}
	
	public static String getArticleDescription(String strArticleDesc, int nIndex, String strDesc) 
	{
		String [] arrDesc = strArticleDesc.split("\\|");
		return arrDesc.length > nIndex ? arrDesc[nIndex] : strDesc;
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
//		Criteria oItemCriteria = oCriteria.createCriteria("m_oItemData");
		if (!m_strClientArticleNumber.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strClientArticleNumber"), m_strClientArticleNumber)); 
		if (m_strClientArticleDescription != null && !m_strClientArticleDescription.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strClientArticleDescription"), m_strClientArticleDescription.trim())); 
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oClientData"), m_oClientData)); 
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oItemData"), m_oItemData)); 
/*	
 		if (m_oItemData != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemCriteria.add(Restrictions.eq("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim()));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strItemName");
*/
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
}
