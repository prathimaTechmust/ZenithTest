package com.techmust.inventory.items;

import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
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
import javax.xml.parsers.DocumentBuilderFactory;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.Criteria;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.clientmanagement.ContactData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataexchange.ChildKey;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.crm.PubliciseData;
import com.techmust.inventory.sales.PubliciseToNotBoughtClients;
import com.techmust.inventory.sales.SalesData;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;
@Entity
@Table(name = "tac01_items")
@DiscriminatorValue("items")
public class ItemData extends TenantData
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ac01_item_id")
	private int m_nItemId;
	@Column (name = "ac01_Created_by")
	private int m_nCreatedBy;
	@Column (name = "ac01_article_number")
//	@Index(name = "idx_article_number")
	private String m_strArticleNumber; 
	@Column (name = "ac01_item_name")
	private String m_strItemName; 
	@Column(name = "ac01_brand")
	private String m_strBrand;
	@Column(name="ac01_detail")
	private String m_strDetail;
	@Column( name = "ac01_unit")
	private String m_strUnit;
	@Transient
	private BufferedImage m_buffImgPhoto;
	@Column (name = "ac01_item_photo")
	private Blob m_oItemPhoto;
	@Column (name = "ac01_compressed_photo")
	private Blob m_oCompressedItemPhoto;
	@Column(name="ac01_image_name")
	private String m_strImageName;
	@Column(name = "ac01_opening_stock")
	private float m_nOpeningStock;
	@Column(name="ac01_reorder_level")
	private float m_nReorderLevel;
	@Column(name="ac01_selling_price")
	private float m_nSellingPrice;
	@Column(name="ac01_received_qty")
	private float m_nReceived;
	@Column(name="ac01_issued_qty")
	private float m_nIssued; 
	@Column(name="ac01_cost_price")
	@ColumnDefault("0")
	private float m_nCostPrice;
	@Column(name="ac01_created_on")
	@Basic
	@Temporal(TemporalType.TIME)
	private Date m_dCreatedOn;
	@Column(name="ac01_updated_on")
	@Basic
	@Temporal(TemporalType.TIME)
	private Date m_dUpdatedOn;
	@Column (name = "ac01_applicable_tax")
	private int m_nApplicableTaxID;
	@Column (name = "ac01_TaxWithCForm")
	private int m_nTaxWithCFormID;
	@Column(name = "ac01_hsnNumber")
	private String m_strHsnNumber;
	@Column(name="ac01_location_code")
	private String m_strLocationCode;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Column(name="ac01_Publish_Online")
	@ColumnDefault("0")
	private boolean m_bPublishOnline;
	@Transient
	private boolean m_bIsForReOrderList;
	@Transient
	private boolean m_bIsForBrandList;
	@Transient
	private String m_strKeyword;
	@Transient
	private boolean m_bIsForSearch;
	@Transient
	private int [] m_arrCartItems;
	@Transient
	private boolean m_bIsForCartList;
	@Transient
	public ItemGroupData [] m_arrItemGroups;
	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name = "tac36_group_items", joinColumns = { @JoinColumn(name = "ac36_item_id") }, inverseJoinColumns = { @JoinColumn(name = "ac36_group_id") })
	private Set<ItemGroupData> m_oItemGroups;
	@JsonManagedReference
	@OneToMany(fetch=FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name="ac51_parent_item_id")
	private Set<ChildItemData> m_oChildItems;
	@Transient
	public ChildItemData [] m_arrChildItems;
	@Formula("(select ac01_opening_stock + ac01_received_qty - ac01_issued_qty)")
	private float m_nCurrentStock;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ac01_ItemCategory")
	private ItemCategoryData m_oItemCategoryData;
	@Transient
	private String m_strFromDate;
	@Transient
	private String m_strToDate;
	@OneToMany(fetch=FetchType.EAGER, orphanRemoval = true)
	@JoinColumn( name="ac65_id")
	private Set<ItemImagesData> m_oItemImage;
	@Transient
	public ItemImagesData [] m_arrItemImages;
	
	public ItemData ()
	{
		m_nItemId = -1;
		m_dCreatedOn = Calendar.getInstance ().getTime ();
		m_dUpdatedOn = Calendar.getInstance ().getTime ();
		m_strArticleNumber = "";
		m_strItemName = "";
		m_strBrand = "";
		m_strDetail = "";
		m_strUnit = "No.";
		m_nOpeningStock = 0;
		m_nReorderLevel = 0;
		m_nSellingPrice = 0;
		m_nReceived = 0;
		m_nCostPrice = 0;
		m_nIssued = 0;
		setM_nCreatedBy(-1);
		m_bIsForReOrderList = false;
		m_bIsForBrandList = false;
		m_bIsForCartList = false;
		m_strLocationCode = "";
		setM_oItemGroups(new HashSet<ItemGroupData> ());
		m_oChildItems = new HashSet<ChildItemData> ();
		m_arrChildItems = new ChildItemData [0];
		m_nCurrentStock = 0;
		m_nApplicableTaxID = 0;
		m_strKeyword = "";
		m_oItemCategoryData = new ItemCategoryData ();
		setM_oItemImage(new HashSet<ItemImagesData> ());
		m_strImageName = "";
		m_nTaxWithCFormID = 0;
		m_strHsnNumber = "";
	}
	
	public ItemData (ItemData oItemData)
	{
		m_nItemId = oItemData.m_nItemId;
		m_dCreatedOn = oItemData.m_dCreatedOn;
		m_dUpdatedOn = oItemData.m_dUpdatedOn;
		m_strArticleNumber = oItemData.m_strArticleNumber;
		m_strItemName = oItemData.m_strItemName;
		m_strBrand = oItemData.m_strBrand;
		m_strDetail = oItemData.m_strDetail;
		m_strUnit = oItemData.m_strUnit;
		m_nOpeningStock = oItemData.m_nOpeningStock;
		m_nReorderLevel = oItemData.m_nReorderLevel;
		m_nSellingPrice = oItemData.m_nSellingPrice;
		m_nReceived = oItemData.m_nReceived;
		m_nCostPrice = oItemData.m_nCostPrice;
		m_nIssued = oItemData.m_nIssued;
		m_nCreatedBy = oItemData.m_nCreatedBy;
		m_strLocationCode = oItemData.m_strLocationCode;
		m_oItemGroups = oItemData.m_oItemGroups;
		m_buffImgPhoto = oItemData.m_buffImgPhoto;
		m_oItemPhoto = oItemData.m_oItemPhoto;
		m_oCompressedItemPhoto = oItemData.m_oCompressedItemPhoto;
		m_nApplicableTaxID = oItemData.m_nApplicableTaxID;
		m_nTaxWithCFormID = oItemData.m_nTaxWithCFormID;
		m_bIsForReOrderList = oItemData.m_bIsForReOrderList;
		m_bIsForBrandList = oItemData.m_bIsForBrandList;
		m_bIsForCartList = oItemData.m_bIsForCartList;
		m_oUserCredentialsData = oItemData.m_oUserCredentialsData;
		m_bPublishOnline = oItemData.m_bPublishOnline;
		m_strKeyword = oItemData.m_strKeyword;
		m_bIsForSearch = oItemData.m_bIsForSearch;
		m_arrCartItems = oItemData.m_arrCartItems;
		m_arrItemGroups = oItemData.m_arrItemGroups;
		m_oChildItems = oItemData.m_oChildItems;
		m_arrChildItems = oItemData.m_arrChildItems;
		m_nCurrentStock = oItemData.m_nCurrentStock;
		m_oItemCategoryData = oItemData.m_oItemCategoryData;
		m_strHsnNumber = oItemData.m_strHsnNumber;
	}

	public String getM_strHsnNumber() 
	{
		return m_strHsnNumber;
	}

	public void setM_strHsnNumber(String strHsnNumber)
	{
		this.m_strHsnNumber = strHsnNumber;
	}
	public void setM_nItemId(int m_nItemId)
    {
	    this.m_nItemId = m_nItemId;
    }

	public int getM_nItemId()
    {
	    return m_nItemId;
    }

	public void setM_strArticleNumber(String strArticleNumber)
	{
		this.m_strArticleNumber = strArticleNumber;
	}

	public String getM_strArticleNumber() 
	{
		return m_strArticleNumber;
	}

	public void setM_strItemName(String m_strItemName)
    {
	    this.m_strItemName = m_strItemName;
    }

	public String getM_strItemName()
    {
	    return m_strItemName;
    }

	public void setM_strBrand(String m_strBrand)
    {
	    this.m_strBrand = m_strBrand;
    }

	public String getM_strBrand()
    {
	    return m_strBrand;
    }

	public String getM_strDetail() 
	{
		return m_strDetail;
	}

	public void setM_strDetail(String strDetail) 
	{
		m_strDetail = strDetail;
	}

	public void setM_strUnit(String strUnit) 
	{
		this.m_strUnit = strUnit;
	}

	public String getM_strUnit() 
	{
		return m_strUnit;
	}

	public void setM_nOpeningStock(float m_nOpeningStock)
    {
	    this.m_nOpeningStock = m_nOpeningStock;
    }

	public float getM_nOpeningStock()
    {
	    return m_nOpeningStock;
    }

	public void setM_nReorderLevel(float m_nReorderLevel)
    {
	    this.m_nReorderLevel = m_nReorderLevel;
    }

	public float getM_nReorderLevel()
    {
	    return m_nReorderLevel;
    }

	public BufferedImage getM_buffImgPhoto() 
	{
		return m_buffImgPhoto;
	}

	public void setM_buffImgPhoto(BufferedImage buffImgPhoto)
	{
		m_buffImgPhoto = buffImgPhoto;
	}

	public void setM_oItemPhoto(Blob oItemPhoto) 
	{
		this.m_oItemPhoto = oItemPhoto;
	}

	public Blob getM_oItemPhoto()
	{
		return m_oItemPhoto;
	}

	public void setM_oCompressedItemPhoto(Blob oCompressedItemPhoto) 
	{
		m_oCompressedItemPhoto = oCompressedItemPhoto;
	}

	public Blob getM_oCompressedItemPhoto() 
	{
		return m_oCompressedItemPhoto;
	}

	public String getM_strImageName () 
	{
		return m_strImageName;
	}

	public void setM_strImageName (String strImageName) 
	{
		m_strImageName = strImageName;
	}

	public float getM_nSellingPrice() 
	{
		return m_nSellingPrice;
	}

	public void setM_nSellingPrice(float nSellingPrice) 
	{
		m_nSellingPrice = nSellingPrice;
	}

	public void setM_nCostPrice(float nCostPrice) 
	{
		m_nCostPrice = nCostPrice;
	}

	public float getM_nCostPrice() 
	{
		return m_nCostPrice;
	}

	public void setM_nCreatedBy(int nCreatedBy) 
	{
		this.m_nCreatedBy = nCreatedBy;
	}

	public int getM_nCreatedBy() 
	{
		return m_nCreatedBy;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		this.m_dCreatedOn = dCreatedOn;
	}

	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}

	public void setM_dUpdatedOn(Date dUpdatedOn) 
	{
		this.m_dUpdatedOn = dUpdatedOn;
	}

	public Date getM_dUpdatedOn() 
	{
		return m_dUpdatedOn;
	}

	public void setM_oApplicableTax(int nApplicableTaxID) 
	{
		m_nApplicableTaxID = nApplicableTaxID;
	}

	public int getM_oApplicableTax() 
	{
		return m_nApplicableTaxID;
	}

	public void setM_oTaxWithCForm(int nTaxWithCForm) 
	{
		m_nTaxWithCFormID = nTaxWithCForm;
	}

	public int getM_oTaxWithCForm() 
	{
		return m_nTaxWithCFormID;
	}

	public void setM_strLocationCode(String strLocationCode) 
	{
		this.m_strLocationCode = strLocationCode;
	}

	public String getM_strLocationCode() 
	{
		return m_strLocationCode;
	}

	public void setM_nReceived(float m_nReceived)
    {
	    this.m_nReceived = m_nReceived;
    }

	public float getM_nReceived()
    {
	    return m_nReceived;
    }

	public void setM_nIssued(float m_nIssued)
    {
	    this.m_nIssued = m_nIssued;
    }

	public float getM_nIssued()
    {
	    return m_nIssued;
    }

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		this.m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}

	public void setM_bPublishOnline(boolean m_bPublishOnline) 
	{
		this.m_bPublishOnline = m_bPublishOnline;
	}

	public void setM_strKeyword(String m_strKeyword) 
	{
		this.m_strKeyword = m_strKeyword;
	}

	public String getM_strKeyword() 
	{
		return m_strKeyword;
	}

	public void setM_bIsForSearch(boolean bIsForSearch) 
	{
		m_bIsForSearch = bIsForSearch;
	}

	public boolean isM_bIsForSearch() 
	{
		return m_bIsForSearch;
	}

	public boolean isM_bPublishOnline() 
	{
		return m_bPublishOnline;
	}

	public void setM_oItemCategoryData(ItemCategoryData m_oItemCategoryData) 
	{
		this.m_oItemCategoryData = m_oItemCategoryData;
	}

	public ItemCategoryData getM_oItemCategoryData() 
	{
		return m_oItemCategoryData;
	}

	public void setM_arrCartItems(int [] m_arrCartItems) 
	{
		this.m_arrCartItems = m_arrCartItems;
	}

	public int [] getM_arrCartItems() 
	{
		return m_arrCartItems;
	}

	public void setM_oItemGroups (Set<ItemGroupData> m_oItemGroups)
	{
		this.m_oItemGroups = m_oItemGroups;
	}

	public Set<ItemGroupData> getM_oItemGroups () 
	{
		return m_oItemGroups;
	}

	public Set<ChildItemData> getM_oChildItems() 
	{
		return m_oChildItems;
	}

	public void setM_oChildItems(Set<ChildItemData> oChildItems) 
	{
		m_oChildItems = oChildItems;
	}

	public float getM_nCurrentStock() 
	{
		return m_nCurrentStock;
	}

	public void setM_nCurrentStock(float nCurrentStock) 
	{
		m_nCurrentStock = nCurrentStock;
	}

	public String getM_strFromDate () 
	{
		return m_strFromDate;
	}

	public void setM_strFromDate (String strFromDate) 
	{
		m_strFromDate = strFromDate;
	}

	public String getM_strToDate () 
	{
		return m_strToDate;
	}

	public void setM_strToDate (String strToDate) 
	{
		m_strToDate = strToDate;
	}

	public Set<ItemImagesData> getM_oItemImage ()
	{
		return m_oItemImage;
	}

	public void setM_oItemImage (Set<ItemImagesData> oItemImage) 
	{
		m_oItemImage = oItemImage;
	}

	public boolean isM_bIsForReOrderList()
	{
		return m_bIsForReOrderList;
	}

	public void setM_bIsForReOrderList(boolean bIsForReOrderList) 
	{
		m_bIsForReOrderList = bIsForReOrderList;
	}

	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strItemInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ItemData");
			Document oChildItemXmlDoc = getXmlDocument ("<m_oChildItems>"+buildChildItemsList (m_oChildItems)+"</m_oChildItems>");
			Node oChildItemNode = oXmlDocument.importNode (oChildItemXmlDoc.getFirstChild(), true);
			oRootElement.appendChild (oChildItemNode);
			if( m_oItemGroups != null)
				buildItemsGroupList (m_oItemGroups, oXmlDocument, oRootElement);
			addChild (oXmlDocument, oRootElement, "m_nItemId", m_nItemId);
			addChild (oXmlDocument, oRootElement, "m_strArticleNumber", m_strArticleNumber);
			addChild (oXmlDocument, oRootElement, "m_strItemName", m_strItemName);
			addChild (oXmlDocument, oRootElement, "m_strBrand", m_strBrand);
			addChild (oXmlDocument, oRootElement, "m_strDetail",m_strDetail );
			addChild (oXmlDocument, oRootElement, "m_strUnit",m_strUnit );
			addChild (oXmlDocument, oRootElement, "m_strLocationCode",m_strLocationCode );
			addChild (oXmlDocument, oRootElement, "m_nSellingPrice", m_nSellingPrice);
			addChild (oXmlDocument, oRootElement, "m_nOpeningStock", m_nOpeningStock);
			addChild (oXmlDocument, oRootElement, "m_nReorderLevel", m_nReorderLevel);
			addChild (oXmlDocument, oRootElement, "m_nReceived", m_nReceived);
			addChild (oXmlDocument, oRootElement, "m_nIssued", m_nIssued);
//			addChild (oXmlDocument, oRootElement, "m_strUserName", getM_oCreatedBy() != null ? getM_oCreatedBy().getM_strUserName() : "");
			addChild (oXmlDocument, oRootElement, "m_nCreatedByUserId", m_nCreatedBy);
			addChild (oXmlDocument, oRootElement, "m_bPublishOnline", m_bPublishOnline ? 1 : 0);
			addChild (oXmlDocument, oRootElement, "m_nApplicableTaxID", m_nApplicableTaxID);
			addChild (oXmlDocument, oRootElement, "m_nTaxWithCFormID", m_nTaxWithCFormID);
			strItemInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
	    m_oLogger.debug( "generateXML - strItemInfoXML [OUT] : " + strItemInfoXML);
	    return strItemInfoXML;
	}

	private void buildItemsGroupList(Set<ItemGroupData> oItemGroups, Document oXmlDocument, Element oRootElement) 
	{
		Object [] arrItemGroupData = oItemGroups.toArray ();
		Document oDocument = null;
		for (int nIndex = 0; nIndex < arrItemGroupData.length; nIndex ++)
		{
			oDocument = createNewXMLDocument ();
			Element oElement = createRootElement(oDocument, "m_oItemGroups");
			ItemGroupData oItemGroupData = (ItemGroupData) arrItemGroupData [nIndex];
			addChild (oDocument, oElement, "m_strGroupName", oItemGroupData.getM_strGroupName());
			Node oChildNode = oXmlDocument.importNode (oDocument.getFirstChild (), true);
			oRootElement.appendChild (oChildNode);
		}
	}

	private String buildChildItemsList(Set<ChildItemData> oChildItems) 
	{
		String strXML = "";
		Object [] arrChildItemData = oChildItems.toArray ();
		for (int nIndex = 0; nIndex < arrChildItemData.length; nIndex ++)
		{
			ChildItemData oChildItemData = (ChildItemData) arrChildItemData [nIndex];
			strXML += oChildItemData.generateXML ();
		}
		return strXML;
	}

	
	public void received (float nQuantity)
	{
		m_nReceived += nQuantity;
	}
	
	public void issued (float nQuantity)
	{
		m_nIssued += nQuantity;
	}
	
	public void updateIssuedForChildItems(float nQuantity) throws Exception 
	{
		Iterator<ChildItemData> oIterator = m_oChildItems.iterator();
		while (oIterator.hasNext())
		{
			ChildItemData oChildItemData = (ChildItemData) oIterator.next();
			ItemData oItemData = oChildItemData.getM_oItemData();
			oItemData.issued(oChildItemData.getM_nQuantity() * nQuantity);
			oItemData.updateObject();
		}
	}
	
	public String getTaxName(boolean bIsOutOfStateClient) 
	{
		String strTaxName = ""; 
		if(bIsOutOfStateClient)
			strTaxName = "CST";
		else
		{
		}
		return strTaxName;
	}

	public static ItemData getInstance (String strArticleNumber, UserInformationData oCredentials) throws Exception
	{
		if (strArticleNumber == null || strArticleNumber.isEmpty())
			throw new IllegalArgumentException ("ItemData.getInstance - ArticleNumber is Mandatory!");
		ItemData oItemData = new ItemData ();
		oItemData.setM_strArticleNumber(strArticleNumber);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return (ItemData) oItemData.list(oOrderBy).get(0);
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (m_bIsForCartList)
			buildCartItemsCriteria (m_arrCartItems, oCriteria);
		else
			buildItemsCriteria (oCriteria);

		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nItemId");
		return oCriteria;
	}

	private void buildCartItemsCriteria(int[] arrCartItems, Criteria oCriteria) 
	{
		ArrayList<Criterion> arrCriterion = new ArrayList<Criterion> ();
		if (m_arrCartItems != null && m_arrCartItems.length > 0)
		{
			for(int nIndex=0; nIndex < arrCartItems.length; nIndex++)
				arrCriterion.add(Restrictions.eq("m_nItemId", arrCartItems[nIndex]));
		}
		else
			oCriteria.add(Restrictions.eq("m_nItemId", -1));
		addCartItemsCriteria (oCriteria , arrCriterion);
	}

	private void addCartItemsCriteria(Criteria oCriteria, ArrayList<Criterion> arrCriterion) 
	{
		Disjunction oDisjunction = Restrictions.disjunction();
	    for (int nIndex = 0; nIndex < arrCriterion.size(); nIndex++)
	    	oDisjunction.add(arrCriterion.get(nIndex));
	    oCriteria.add(oDisjunction);
	}

	private void buildItemsCriteria(Criteria oCriteria) 
	{
		if (!m_strItemName.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strItemName", m_strItemName.trim(), MatchMode.ANYWHERE));
		if (m_strArticleNumber != null && !m_strArticleNumber.trim().isEmpty ())
			oCriteria.add (Restrictions.eq ("m_strArticleNumber", m_strArticleNumber.trim()));
		if (m_nItemId > 0)
			oCriteria.add (Restrictions.eq ("m_nItemId", m_nItemId));
		if (!m_strBrand.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strBrand", m_strBrand.trim(), MatchMode.ANYWHERE));
		if (m_oItemCategoryData != null && m_oItemCategoryData.getM_nCategoryId() > 0)
			oCriteria.add (Restrictions.eq ("m_oItemCategoryData", m_oItemCategoryData));
		if (m_bPublishOnline)
			oCriteria.add (Restrictions.like ("m_bPublishOnline", m_bPublishOnline));
		if (m_bIsForReOrderList)
			oCriteria.add(Restrictions.geProperty("m_nReorderLevel", "m_nCurrentStock"));
		if (m_bIsForSearch && !m_strKeyword.isEmpty() && (m_strKeyword.toLowerCase().contains(m_strItemName.toLowerCase())
														|| m_strKeyword.toLowerCase().contains(m_strBrand.toLowerCase())
														|| m_strKeyword.toLowerCase().contains(m_strArticleNumber.toLowerCase())
														|| m_strKeyword.toLowerCase().contains(m_strDetail.toLowerCase())))
			oCriteria.add(Restrictions.disjunction().add(Restrictions.ilike("m_strItemName", m_strKeyword, MatchMode.ANYWHERE))
													.add(Restrictions.ilike("m_strBrand", m_strKeyword, MatchMode.ANYWHERE))
													.add(Restrictions.ilike("m_strArticleNumber", m_strKeyword, MatchMode.ANYWHERE))
													.add(Restrictions.ilike("m_strDetail", m_strKeyword, MatchMode.ANYWHERE)));
		if (m_bIsForBrandList)
			oCriteria.setProjection( Projections.projectionList().add( Projections.groupProperty("m_strBrand")));
	}

	public void prepareItemData() throws Exception 
	{
		for (int nIndex = 0; m_arrItemGroups != null && nIndex < m_arrItemGroups.length; nIndex++)
		{
			ItemGroupData oItemGroupData = (ItemGroupData) GenericIDataProcessor.populateObject(m_arrItemGroups[nIndex]);
			oItemGroupData.getM_oGroupItems().add(this);
			m_oItemGroups.add(oItemGroupData);
		}
	}
	
	public void prepareChildItemSet() 
	{
		for (int nIndex = 0; m_arrChildItems != null && nIndex < m_arrChildItems.length; nIndex++)
		{
			m_arrChildItems[nIndex].setM_oItemData(m_arrChildItems[nIndex].getM_oItemData());
			m_oChildItems.add(m_arrChildItems[nIndex]);
		}
	}	

	public String generateItemDataXML(ArrayList<ItemData> arrItemData) 
	{
		String strItemListDataXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Document oInvoiceXmlDoc = getXmlDocument ("<ItemList>" + buildItemXml (arrItemData) + "</ItemList>");
			Node oInvoiceNode = oXmlDocument.importNode (oInvoiceXmlDoc.getFirstChild (), true);
			oXmlDocument.appendChild(oInvoiceNode);
			strItemListDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateTallyDataXML - oException" + oException);
		}
		m_oLogger.debug("generateTallyDataXML - strTallyDataXml [OUT] : " + strItemListDataXml);
		return strItemListDataXml;
	}

	private String buildItemXml(ArrayList<ItemData> arrItemData) 
	{
		m_oLogger.debug("buildItemXml");
		String strXml = "";
		for(int nIndex = 0; nIndex < arrItemData.size(); nIndex++)
			strXml += arrItemData.get(nIndex).generateXML();
	    return strXml;
	}

	private ItemData buildItemData(ItemData oItemData, Node oChildNode) throws Exception 
	{
		oItemData.setM_strArticleNumber(UserManagementInitializer.getValue(oChildNode, "m_strArticleNumber"));
		oItemData.setM_strItemName(UserManagementInitializer.getValue(oChildNode, "m_strItemName"));
		oItemData.setM_strBrand(UserManagementInitializer.getValue(oChildNode, "m_strBrand"));
		oItemData.setM_strDetail(UserManagementInitializer.getValue(oChildNode, "m_strDetail"));
		oItemData.setM_strUnit(UserManagementInitializer.getValue(oChildNode, "m_strUnit"));
		oItemData.setM_strLocationCode(UserManagementInitializer.getValue(oChildNode, "m_strLocationCode "));
		oItemData.setM_nSellingPrice(Float.parseFloat(UserManagementInitializer.getValue(oChildNode, "m_nSellingPrice")));
		oItemData.setM_nOpeningStock(Float.parseFloat(UserManagementInitializer.getValue(oChildNode, "m_nOpeningStock")));
		oItemData.setM_nReorderLevel(Float.parseFloat(UserManagementInitializer.getValue(oChildNode, "m_nReorderLevel")));
		oItemData.setM_nReceived(Float.parseFloat(UserManagementInitializer.getValue(oChildNode, "m_nReceived")));
		oItemData.setM_nIssued(Float.parseFloat(UserManagementInitializer.getValue(oChildNode, "m_nIssued")));
		oItemData.setM_bPublishOnline(UserManagementInitializer.getValue(oChildNode, "m_bPublishOnline").equalsIgnoreCase("yes") ? true : false);
		return oItemData;
	}

	private String getApplicableTaxXml(Node oItemNode, String strTaxName) throws  Exception
	{
		String strXml = "";
		NodeList oNodeList = oItemNode.getChildNodes();
		Document oDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		for(int nIndex = 0; nIndex < ((NodeList) oNodeList).getLength(); nIndex++)
		{
			if(oNodeList.item(nIndex).getNodeName().equalsIgnoreCase(strTaxName))
			{
				Node oApplicaleTaxNode = oNodeList.item(nIndex).getFirstChild();
				oDocument.appendChild(oDocument.importNode(oApplicaleTaxNode, true));
				strXml = getXmlString(oDocument);
				break;
			}
		}
		return strXml;
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) throws Exception
	{
		ItemData oItemData = new ItemData ();
		strXML = strXML.replaceAll(">\\s+<", "><").trim();
		Document oXMLDocument = getXmlDocument(strXML);
		NodeList oChildNodes = oXMLDocument.getChildNodes();
		for (int nIndex = 0; nIndex < oChildNodes.getLength(); nIndex++)
	    {
			Node oChildNode = oChildNodes.item(nIndex);
    		oItemData = buildItemData (oItemData, oChildNode);
    		buildChildItems (oChildNode, oItemData, oCredentials);
    		oItemData.setM_nCreatedBy(oCredentials.getM_nUserId());
	    }
		if(isArticleExists(oItemData))
			throw new Exception ("Duplicate");
		return oItemData;
	}
	
	public boolean isArticleExists(ItemData oItemData)
    {
	   boolean bIsArticleExists = false;
	   m_oLogger.info("isArticleExists");
	   try
	   {
		   ItemData oData = new ItemData ();
		   ItemDataProcessor oDataProcessor = new ItemDataProcessor ();
		   oData.setM_strArticleNumber(oItemData.getM_strArticleNumber());
		   oData.setM_oUserCredentialsData(oItemData.getM_oUserCredentialsData());
		   HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		   ItemDataResponse oItemDataResponse =(ItemDataResponse) oDataProcessor.list(oData, oOrderBy);
		   bIsArticleExists = (oItemDataResponse.m_arrItems.size() > 0);
	   }
	   catch (Exception oException)
	   {
		   m_oLogger.error("isArticleExists - oException : " + oException);
	   }
	   m_oLogger.debug("isArticleExists - bIsArticleExists [OUT] : " + bIsArticleExists);
	   return bIsArticleExists;
    }
	
	private void buildChildItems(Node oChildItemNode, ItemData oItemData, UserInformationData oCredentials) throws Exception 
	{
		NodeList oChildItemList = oChildItemNode.getChildNodes();
		for(int nIndex = 0; nIndex < ((NodeList) oChildItemList).getLength(); nIndex++)
		{
			if(oChildItemList.item(nIndex).getNodeName().equalsIgnoreCase("m_oChildItems"))
			{
				NodeList oNodeList = oChildItemList.item(nIndex).getChildNodes();
				buildChildItemSet(oNodeList, oItemData, oCredentials);
				break;
			}
		}
	}

	private void buildChildItemSet(NodeList oNodeList, ItemData oItemData, UserInformationData oCredentials) throws Exception
	{
		for(int nIndex = 0; nIndex < ((NodeList) oNodeList).getLength(); nIndex++)
		{
			if(oNodeList.item(nIndex).getNodeName().equalsIgnoreCase("ChildItemData"))
			{
				Document oDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Node oChildNode = oNodeList.item(nIndex);
				oDocument.appendChild(oDocument.importNode(oChildNode, true));
				String strXML = getXmlString(oDocument);
				ChildItemData oChildItemData = new ChildItemData ();
				oChildItemData = (ChildItemData)oChildItemData.getInstanceData(strXML, oCredentials);
				oItemData.m_oChildItems.add(oChildItemData);
			}
		}
	}

	public static ContactData[] getClientContacts(PubliciseData oPubliciseData) throws Exception 
	{
		if(oPubliciseData.isM_bIsBought())
		{
			SalesData oSalesData = new SalesData ();
			oSalesData.setM_bIsForClientwise(true);
			oSalesData.setM_nItemId(oPubliciseData.getM_nItemId());
			return SalesData.getContactsToPublicise (oPubliciseData, oSalesData);
		}
		else
		{
			PubliciseToNotBoughtClients oPubliciseToNotBoughtClients = new PubliciseToNotBoughtClients ();
			oPubliciseToNotBoughtClients.setM_nItemId(oPubliciseData.getM_nItemId());
			return oPubliciseToNotBoughtClients.getContactsToPublicise (oPubliciseData);
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap getHeaderKeys() 
	{
		HashMap oHeaderKey = new LinkedHashMap ();
		oHeaderKey.put("ItemId", "m_nItemId");
		oHeaderKey.put("ArticleNumber", "m_strArticleNumber");
		oHeaderKey.put("ItemName", "m_strItemName");
		oHeaderKey.put("Brand", "m_strBrand");
		oHeaderKey.put("Detail", "m_strDetail");
		oHeaderKey.put("Unit", "m_strUnit");
		oHeaderKey.put("LocationCode", "m_strLocationCode");
		oHeaderKey.put("SellingPrice", "m_nSellingPrice");
		oHeaderKey.put("OpeningStock", "m_nOpeningStock");
		oHeaderKey.put("ReorderLevel", "m_nReorderLevel");
		oHeaderKey.put("Received", "m_nReceived");
		oHeaderKey.put("Issued", "m_nIssued");
		oHeaderKey.put("UserName", "m_strUserName");
		oHeaderKey.put("CreatedBy", "m_nCreatedByUserId");
		oHeaderKey.put("PublishOnline", "m_bPublishOnline");
		return oHeaderKey;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ChildKey> getChildKeys ()
	{
		ArrayList<ChildKey> arrChildKeys = new ArrayList<ChildKey>();
		ChildKey oApplicableTaxChildKey = new ChildKey ();
		oApplicableTaxChildKey.m_oChildKey.put("Applicable Tax", "m_oApplicableTax");
		oApplicableTaxChildKey.m_oChildKey.put("Applicable Tax Data", "Applicabletax");
		oApplicableTaxChildKey.m_oChildKey.put("ApplicableTaxId", "m_nId");
		oApplicableTaxChildKey.m_oChildKey.put("ApplicableTaxName", "m_strApplicableTaxName");
		ChildKey oTaxWithCForm = new ChildKey ();
		oTaxWithCForm.m_oChildKey.put("Tax With C Form", "m_oTaxWithCForm");
		oTaxWithCForm.m_oChildKey.put("Applicable Tax Data", "Applicabletax");
		oTaxWithCForm.m_oChildKey.put("ApplicableTaxId", "m_nId");
		oTaxWithCForm.m_oChildKey.put("ApplicableTaxName", "m_strApplicableTaxName");
		ChildKey oChildItems = new ChildKey ();
		oChildItems.m_oChildKey.put("Child Items" ,"m_oChildItems");
		oChildItems.m_oChildKey.put("Child Items Data" ,"ChildItemData");
		oChildItems.m_oChildKey.put("ChildId" ,"m_nChildItemId");
		oChildItems.m_oChildKey.put("Quantity" ,"m_nQuantity");
		arrChildKeys.add(oApplicableTaxChildKey);
		arrChildKeys.add(oTaxWithCForm);
		arrChildKeys.add(oChildItems);
		return arrChildKeys;
	}

	public void buildItemImages() 
	{
		HashSet<ItemImagesData> oItemImages = new HashSet<ItemImagesData> ();
		oItemImages.addAll(buildImageList ());
		setM_oItemImage(oItemImages);
	}

	private Collection<ItemImagesData> buildImageList() 
	{
		m_oLogger.info("buildImageList");
		ArrayList<ItemImagesData> oArrayList = new ArrayList<ItemImagesData> ();
		try
		{
			for (int nIndex = 0; nIndex < m_arrItemImages.length; nIndex++)
			{
				m_arrItemImages[nIndex] = setImageList(m_arrItemImages[nIndex]);
				if((m_arrItemImages[nIndex]!=null)&&(m_arrItemImages[nIndex].getM_oImages()!=null))
				oArrayList.add(m_arrItemImages[nIndex]);
			}
		}
		catch(Exception oException)
		{
			m_oLogger.error("buildImageList - oException : " +oException);;;
		}
		m_oLogger.debug("buildImageList - oArrayList [OUT] : " +oArrayList);
		return oArrayList;
	}

	private ItemImagesData setImageList(ItemImagesData oItemImagesData) throws Exception 
	{
		ItemDataProcessor oDataProcessor = new ItemDataProcessor ();
		if((oItemImagesData.getM_buffImgPhoto() != null)||(oItemImagesData.getM_nId()==-1))
		{
			oItemImagesData.setM_oImages(GenericIDataProcessor.getBlob(oItemImagesData.getM_buffImgPhoto()));
			oItemImagesData.setM_oCompressedPhoto(GenericIDataProcessor.getBlob (oDataProcessor.resizeImage(oDataProcessor.compressImage(oItemImagesData.getM_buffImgPhoto()))));
		}
		else
		{
			ItemImagesData oImagesData = new ItemImagesData ();
			oImagesData.setM_nId(oItemImagesData.getM_nId());;
			oImagesData = (ItemImagesData) GenericIDataProcessor.populateObject (oImagesData);
			if(oImagesData!=null)
			{
				oItemImagesData.setM_oImages(oImagesData.getM_oImages());
				oItemImagesData.setM_buffImgPhoto(GenericIDataProcessor.getBufferedImage(oImagesData.getM_oImages()));
				oItemImagesData.setM_oCompressedPhoto(GenericIDataProcessor.getBlob (oDataProcessor.resizeImage(oDataProcessor.compressImage(oItemImagesData.getM_buffImgPhoto()))));
				oItemImagesData.setM_strFileName(oImagesData.getM_strFileName());
			}
		}
		return oItemImagesData;
	}

	public void setDefaultChildImage(ItemData oData)
	{
		oData.m_arrItemImages = new ItemImagesData [1];
		ItemImagesData oImagesData = new ItemImagesData ();
		oImagesData.setM_buffImgPhoto(m_buffImgPhoto);
		oImagesData.setM_strFileName(m_strImageName);
		oData.m_arrItemImages[0]= oImagesData;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*		
  		if (m_bIsForCartList)
			buildCartItemsCriteria (m_arrCartItems, oCriteria);
		else
			buildItemsCriteria (oCriteria);

		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nItemId");
*/
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nItemId()> 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nItemId"), m_nItemId));
		return oConjunct;
	}
}
