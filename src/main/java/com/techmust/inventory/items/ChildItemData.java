package com.techmust.inventory.items;

import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.Criteria;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac51_child_items")
public class ChildItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac51_id")
	private int m_nChildItemId;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac51_child_item_id")
	private ItemData m_oItemData;
	@Column(name = "ac51_child_quantity")
	private float m_nQuantity;
	
	public ChildItemData ()
	{
		m_nChildItemId = -1;
		m_nQuantity = 0;
		m_oItemData = new ItemData ();
	}
	
	public int getM_nChildItemId() 
	{
		return m_nChildItemId;
	}

	public void setM_nChildItemId(int nChildItemId) 
	{
		m_nChildItemId = nChildItemId;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oItemData(ItemData oItemData) 
	{
		m_oItemData = oItemData;
	}

	public float getM_nQuantity() 
	{
		return m_nQuantity;
	}

	public void setM_nQuantity(float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	@Override
	public String generateXML() 
	{
		String strChildItemXml = "";
		m_oLogger.info ("generateXML");
	    try 
	    {
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "ChildItemData");
			addChild (oXmlDocument, oRootElement, "m_nChildItemId", m_oItemData.getM_nItemId());
			addChild (oXmlDocument, oRootElement, "m_nQuantity", m_nQuantity);
			strChildItemXml = getXmlString (oXmlDocument);
	    }
	    catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
	    return strChildItemXml;
	}

	@Override
	protected Criteria listCriteria(Criteria criteria, String strColumn,
			String strOrderBy) {
		// TODO Auto-generated method stub
		return null;
	}
	

	public static void removeFromChild(ItemData oItemData) throws Exception 
	{
		ItemData oData = (ItemData) GenericIDataProcessor.populateObject(oItemData);
		Iterator<ChildItemData> oIterator = oData.getM_oChildItems().iterator();
		while (oIterator.hasNext())
		{
			ChildItemData oChildItemData = oIterator.next();
			oChildItemData.m_oItemData.deleteObject();
			oChildItemData.updateObject();
		}
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) throws Exception 
	{
		ChildItemData oChildItemData = new ChildItemData ();
		try
		{
			Document oXMLDocument = getXmlDocument(strXML);
			Node oChildItemNode = oXMLDocument.getFirstChild();
			oChildItemData.m_oItemData.setM_nItemId((Integer.parseInt(UserManagementInitializer.getValue(oChildItemNode,"m_nChildItemId"))));
			oChildItemData.setM_nQuantity(Float.parseFloat(UserManagementInitializer.getValue(oChildItemNode,"m_nQuantity")));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getInstanceData - oException : " + oException);
			throw oException;
		}
		return oChildItemData;
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
		return oConjunct;
	}
}
