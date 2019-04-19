package com.techmust.helper;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ClientGroupData;
import com.techmust.facilitator.FacilitatorInformationData;
import com.techmust.generic.data.GenericData;
import com.techmust.inventory.challan.ChallanData;
import com.techmust.inventory.configuration.ConfigurationData;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.items.ItemCategoryData;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.ItemGroupData;
import com.techmust.inventory.items.StockEntriesData;
import com.techmust.inventory.location.LocationData;
import com.techmust.inventory.paymentsandreceipt.InvoiceReceiptData;
import com.techmust.inventory.paymentsandreceipt.PaymentData;
import com.techmust.inventory.paymentsandreceipt.PurchasePaymentData;
import com.techmust.inventory.paymentsandreceipt.ReceiptData;
import com.techmust.inventory.paymentsandreceipt.TransactionMode;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.inventory.purchase.PurchaseLineItem;
import com.techmust.inventory.purchaseorder.PurchaseOrderData;
import com.techmust.inventory.purchasereturned.PurchaseReturnedData;
import com.techmust.inventory.quotation.QuotationData;
import com.techmust.inventory.quotation.logs.QuotationLogData;
import com.techmust.inventory.returned.ReturnedData;
import com.techmust.inventory.sales.DiscountStructureData;
import com.techmust.inventory.sales.NonStockSalesLineItemData;
import com.techmust.inventory.sales.SalesData;
import com.techmust.inventory.sales.SalesLineItemData;
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.stocktransfer.ItemLocationData;
import com.techmust.inventory.stocktransfer.StockTransferData;
import com.techmust.inventory.stocktransfer.StockTransferMemoData;
import com.techmust.inventory.vendorpurchaseorder.VendorPurchaseOrderData;
import com.techmust.master.businesstype.BusinessTypeData;
import com.techmust.master.tax.ApplicableTax;
import com.techmust.master.tax.Tax;
import com.techmust.organization.OrganizationInformationData;
import com.techmust.usermanagement.action.ActionData;
import com.techmust.usermanagement.actionarea.ActionAreaData;
import com.techmust.usermanagement.role.RoleData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;
import com.techmust.vendormanagement.VendorGroupData;

public class TradeMustHelper extends GenericData {

	private static final long serialVersionUID = 1L;
	
	private UserInformationData m_oUserData;
	private OrganizationInformationData m_oOrganizationData;
	private FacilitatorInformationData m_oFacilitatorInformationData;
	private RoleData m_oRoleData;
	private ReceiptData m_oReceiptData;
	private ActionAreaData m_oActionArea;
	private ActionData m_oAction;
	private ItemData m_oItemData;
	private ItemGroupData m_oItemGroupData;
	private ItemCategoryData m_oItemCategoryData;
	private Tax m_oTaxData;
	private ApplicableTax m_oApplicablTax;
	private ConfigurationData m_oConfigurationData;
	private VendorData m_oVendorData;
	private ClientData m_oClientData;
	private BusinessTypeData m_oBusinessTypeData;
	private StockEntriesData m_oStockEntriesData;
	private StockTransferData m_oStockTransferData;
	private LocationData m_oLocationData;
	private TransactionMode m_oTransactionMode;
	private QuotationData m_oQuotationData;
	private SerialNumberData m_oSerialNumber;
	private ItemLocationData m_oItemLocation;
	private QuotationLogData m_oQuotationLogData;
	private ClientGroupData m_oClientGroup;
	private DiscountStructureData m_oDiscountStructureData;
	private StockTransferMemoData m_oStockTransferMemoData;
	private ChallanData m_oChallanData;
	private SalesData m_oSalesData;
	private PurchaseData m_oPurchaseData;
	private ReturnedData m_oReturnedData;
	private VendorGroupData m_oVendorGroupData;
	private PaymentData m_oPaymentData;
	private PurchasePaymentData m_oPurchasePaymentData;
	private VendorPurchaseOrderData m_oVPOrderData;
	private PurchaseReturnedData m_oPReturnedData;
	private PurchaseOrderData m_oPurchaseOrderData;
	private PurchaseLineItem m_oPurchaseLineItem;
	private InvoiceData m_oInvoiceData;
	private NonStockSalesLineItemData m_oNonSalesLineItemData;
	private SalesLineItemData m_oSalesLineItemData;
	private InvoiceReceiptData m_oInvoiceReceiptData;
	private String m_strColumn;
	private String m_strOrderBy;
	private int m_nPageNo;
	private int m_nPageSize;
	private int m_nBusinessId;
	private int m_nClientId;
	private String m_strArticleNumber;
	private int m_nUserId;
	private String m_strFromDate;
	private String m_strToDate;
	private boolean m_bIncludeZeroMovement;
	
	public TradeMustHelper ()
	{
		m_oUserData = new UserInformationData ();
		m_oOrganizationData = new OrganizationInformationData();
		m_oFacilitatorInformationData = new FacilitatorInformationData();
		m_oTaxData = new Tax ();
		m_oRoleData = new RoleData ();
		m_oReceiptData = new ReceiptData ();
		m_oActionArea = new ActionAreaData ();
		m_oAction = new ActionData ();
		m_oVendorData = new VendorData();
		setM_oInvoiceReceiptData(new InvoiceReceiptData());
		setM_oClientData(new ClientData ());
     	m_oClientData = new ClientData ();
		setM_oBusinessTypeData(new BusinessTypeData ()); 
		setM_oApplicablTax(new ApplicableTax ());
		setM_oItemData(new ItemData ());
		setM_oItemGroupData(new ItemGroupData ());
		setM_oItemCategoryData(new ItemCategoryData());
		setM_oConfigurationData(new ConfigurationData ());
		m_oStockTransferData = new StockTransferData();
		m_oLocationData = new LocationData ();
		m_oTransactionMode = new TransactionMode ();
		setM_oQuotationData(new QuotationData ());
		m_oItemLocation = new ItemLocationData ();
		m_oQuotationLogData = new QuotationLogData ();
		m_oClientGroup = new ClientGroupData ();
		m_oDiscountStructureData = new DiscountStructureData ();
		setM_oChallanData(new ChallanData ());
		m_oSalesData = new SalesData ();
		m_oSalesLineItemData = new SalesLineItemData ();
		setM_strArticleNumber("");
		m_strColumn = "";
		m_strOrderBy = "";
		m_nPageNo = 0;
		m_nPageSize = 10;
		setM_nBusinessId(-1);
		m_nClientId = -1;
	}
		
	public FacilitatorInformationData getM_oFacilitatorInformationData()
	{
		return m_oFacilitatorInformationData;
	}
	
	public void setM_oFacilitatorInformationData(FacilitatorInformationData m_oFacilitatorInformationData)
	{
		this.m_oFacilitatorInformationData = m_oFacilitatorInformationData;
	}

	public OrganizationInformationData getM_oOrganizationData()
	{
		return m_oOrganizationData;
	}

	public void setM_oOrganizationData(OrganizationInformationData m_oOrganizationData)
	{
		this.m_oOrganizationData = m_oOrganizationData;
	}

	public UserInformationData getM_oUserData() 
	{
		return m_oUserData;
	}

	public void setM_oUserData(UserInformationData oUserData) 
	{
		m_oUserData = oUserData;
	}

	public RoleData getM_oRoleData() 
	{
		return m_oRoleData;
	}

	public void setM_oRoleData(RoleData oRoleData) 
	{
		m_oRoleData = oRoleData;
	}

	public void setM_oReceiptData(ReceiptData m_oReceiptData) {
		this.m_oReceiptData = m_oReceiptData;
	}

	public ReceiptData getM_oReceiptData() {
		return m_oReceiptData;
	}

	public ActionAreaData getM_oActionArea() 
	{
		return m_oActionArea;
	}

	public void setM_oActionArea(ActionAreaData oActionArea) 
	{
		m_oActionArea = oActionArea;
	}

	public Tax getM_oTaxData() 
	{
		return m_oTaxData;
	}

	public void setM_oTaxData(Tax oTaxData) 
	{
		m_oTaxData = oTaxData;
	}

	public void setM_oAction(ActionData oAction) 
	{
		this.m_oAction = oAction;
	}

	public void setM_oItemData(ItemData oItemData) 
	{
		this.m_oItemData = oItemData;
	}

	public void setM_oItemGroupData(ItemGroupData m_oItemGroupData) 
	{
		this.m_oItemGroupData = m_oItemGroupData;
	}

	public ItemGroupData getM_oItemGroupData() 
	{
		return m_oItemGroupData;
	}

	public void setM_oItemCategoryData(ItemCategoryData oItemCategoryData) 
	{
		this.m_oItemCategoryData = oItemCategoryData;
	}

	public ItemCategoryData getM_oItemCategoryData() 
	{
		return m_oItemCategoryData;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oConfigurationData(ConfigurationData oConfigurationData) 
	{
		this.m_oConfigurationData = oConfigurationData;
	}

	public ConfigurationData getM_oConfigurationData()
	{
		return m_oConfigurationData;
	}

	public ActionData getM_oAction() 
	{
		return m_oAction;
	}

	public void setM_oApplicablTax(ApplicableTax oApplicablTax) 
	{
		this.m_oApplicablTax = oApplicablTax;
	}

	public void setM_oVendorData(VendorData oVendorData) 
	{
		this.m_oVendorData = oVendorData;
	}

	public VendorData getM_oVendorData()
	{
		return m_oVendorData;
	}

	public void setM_oClientData(ClientData oClientData)
	{
		this.m_oClientData = oClientData;
	}

	public ClientData getM_oClientData() 
	{
		return m_oClientData;
	}

	public void setM_oBusinessTypeData(BusinessTypeData oBusinessTypeData) 
	{
		this.m_oBusinessTypeData = oBusinessTypeData;
	}

	public BusinessTypeData getM_oBusinessTypeData()
	{
		return m_oBusinessTypeData;
	}

	public ApplicableTax getM_oApplicablTax() 
	{
		return m_oApplicablTax;
	}

	public String getM_strColumn() 
	{
		return m_strColumn;
	}

	public void setM_strColumn(String strColumn) 
	{
		m_strColumn = strColumn;
	}

	public String getM_strOrderBy() 
	{
		return m_strOrderBy;
	}

	public void setM_strOrderBy(String strOrderBy)
	{
		m_strOrderBy = strOrderBy;
	}

	public int getM_nPageNo()
	{
		return m_nPageNo;
	}

	public void setM_nPageNo(int nPageNo) 
	{
		m_nPageNo = nPageNo;
	}

	public int getM_nPageSize() 
	{
		return m_nPageSize;
	}

	public void setM_nPageSize(int nPageSize) 
	{
		m_nPageSize = nPageSize;
	}
	
	public void setM_nBusinessId(int nBusinessId) 
	{
		this.m_nBusinessId = nBusinessId;
	}

	public int getM_nBusinessId() 
	{
		return m_nBusinessId;
	}

	public void setM_oStockEntriesData(StockEntriesData oStockEntriesData) 
	{
		this.m_oStockEntriesData = oStockEntriesData;
	}

	public StockEntriesData getM_oStockEntriesData() 
	{
		return m_oStockEntriesData;
	}

	public void setM_oStockTransferData(StockTransferData m_oStockTransferData) 
	{
		this.m_oStockTransferData = m_oStockTransferData;
	}

	public StockTransferData getM_oStockTransferData() 
	{
		return m_oStockTransferData;
	}

	public LocationData getM_oLocationData() {
		return m_oLocationData;
	}

	public void setM_oLocationData(LocationData locationData) {
		m_oLocationData = locationData;
	}

	public TransactionMode getM_oTransactionMode() 
	{
		return m_oTransactionMode;
	}

	public void setM_oTransactionMode(TransactionMode oTransactionMode) 
	{
		m_oTransactionMode = oTransactionMode;
	}

	public void setM_oQuotationData(QuotationData oQuotationData) 
	{
		this.m_oQuotationData = oQuotationData;
	}

	public QuotationData getM_oQuotationData() 
	{
		return m_oQuotationData;
	}

	public int getM_nClientId() 
	{
		return m_nClientId;
	}

	public void setM_nClientId(int nClientId) 
	{
		m_nClientId = nClientId;
	}

	public SerialNumberData getM_oSerialNumber() {
		return m_oSerialNumber;
	}

	public void setM_oSerialNumber(SerialNumberData serialNumber) {
		m_oSerialNumber = serialNumber;
	}

	public void setM_oItemLocation(ItemLocationData m_oItemLocation) {
		this.m_oItemLocation = m_oItemLocation;
	}

	public ItemLocationData getM_oItemLocation() {
		return m_oItemLocation;
	}

	@Override
	public String generateXML() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setM_oQuotationLogData(QuotationLogData m_oQuotationLogData) {
		this.m_oQuotationLogData = m_oQuotationLogData;
	}

	public QuotationLogData getM_oQuotationLogData() {
		return m_oQuotationLogData;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void setM_oClientGroup(ClientGroupData m_oClientGroup) {
		this.m_oClientGroup = m_oClientGroup;
	}

	public ClientGroupData getM_oClientGroup() {
		return m_oClientGroup;
	}

	@Override
	protected Criteria listCriteria(Criteria arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setM_oDiscountStructureData(DiscountStructureData m_oDiscountStructureData) {
		this.m_oDiscountStructureData = m_oDiscountStructureData;
	}

	public DiscountStructureData getM_oDiscountStructureData() {
		return m_oDiscountStructureData;
	}

	public void setM_oSalesData(SalesData m_oSalesData) {
		this.m_oSalesData = m_oSalesData;
	}

	public SalesData getM_oSalesData() {
		return m_oSalesData;
	}

	public String getM_strFromDate() {
		return m_strFromDate;
	}

	public void setM_strFromDate(String fromDate) {
		m_strFromDate = fromDate;
	}

	public String getM_strToDate() {
		return m_strToDate;
	}

	public void setM_strToDate(String toDate) {
		m_strToDate = toDate;
	}

	public boolean isM_bIncludeZeroMovement() {
		return m_bIncludeZeroMovement;
	}

	public void setM_bIncludeZeroMovement(boolean includeZeroMovement) {
		m_bIncludeZeroMovement = includeZeroMovement;
	}

	public void setM_strArticleNumber(String m_strArticleNumber) {
		this.m_strArticleNumber = m_strArticleNumber;
	}

	public String getM_strArticleNumber() {
		return m_strArticleNumber;
	}

	public void setM_oStockTransferMemoData(StockTransferMemoData m_oStockTransferMemoData) {
		this.m_oStockTransferMemoData = m_oStockTransferMemoData;
	}

	public StockTransferMemoData getM_oStockTransferMemoData() {
		return m_oStockTransferMemoData;
	}

	public void setM_oChallanData(ChallanData m_oChallanData) {
		this.m_oChallanData = m_oChallanData;
	}

	public ChallanData getM_oChallanData() {
		return m_oChallanData;
	}

	public void setM_oPurchaseData(PurchaseData m_oPurchaseData) {
		this.m_oPurchaseData = m_oPurchaseData;
	}

	public PurchaseData getM_oPurchaseData() {
		return m_oPurchaseData;
	}

	public void setM_oReturnedData(ReturnedData m_oReturnedData) {
		this.m_oReturnedData = m_oReturnedData;
	}

	public ReturnedData getM_oReturnedData() {
		return m_oReturnedData;
	}

	public void setM_oVendorGroupData(VendorGroupData m_oVendorGroupData) {
		this.m_oVendorGroupData = m_oVendorGroupData;
	}

	public VendorGroupData getM_oVendorGroupData() {
		return m_oVendorGroupData;
	}

	public void setM_oPaymentData(PaymentData m_oPaymentData) {
		this.m_oPaymentData = m_oPaymentData;
	}

	public PaymentData getM_oPaymentData() {
		return m_oPaymentData;
	}

	public void setM_oPurchasePaymentData(PurchasePaymentData m_oPurchasePaymentData) {
		this.m_oPurchasePaymentData = m_oPurchasePaymentData;
	}

	public PurchasePaymentData getM_oPurchasePaymentData() {
		return m_oPurchasePaymentData;
	}

	public void setM_oVPOrderData(VendorPurchaseOrderData m_oVPOrderData) {
		this.m_oVPOrderData = m_oVPOrderData;
	}

	public VendorPurchaseOrderData getM_oVPOrderData() {
		return m_oVPOrderData;
	}

	public void setM_oPReturnedData(PurchaseReturnedData m_oPReturnedData) {
		this.m_oPReturnedData = m_oPReturnedData;
	}

	public PurchaseReturnedData getM_oPReturnedData() {
		return m_oPReturnedData;
	}

	public void setM_oPurchaseOrderData(PurchaseOrderData m_oPurchaseOrderData) {
		this.m_oPurchaseOrderData = m_oPurchaseOrderData;
	}

	public PurchaseOrderData getM_oPurchaseOrderData() {
		return m_oPurchaseOrderData;
	}

	public void setM_oPurchaseLineItem(PurchaseLineItem m_oPurchaseLineItem) {
		this.m_oPurchaseLineItem = m_oPurchaseLineItem;
	}

	public PurchaseLineItem getM_oPurchaseLineItem() {
		return m_oPurchaseLineItem;
	}

	public void setM_oInvoiceData(InvoiceData m_oInvoiceData) {
		this.m_oInvoiceData = m_oInvoiceData;
	}

	public InvoiceData getM_oInvoiceData() {
		return m_oInvoiceData;
	}

	public void setM_oNonSalesLineItemData(NonStockSalesLineItemData m_oNonSalesLineItemData) {
		this.m_oNonSalesLineItemData = m_oNonSalesLineItemData;
	}

	public void setM_oSalesLineItemData(SalesLineItemData m_oSalesLineItemData) {
		this.m_oSalesLineItemData = m_oSalesLineItemData;
	}

	public SalesLineItemData getM_oSalesLineItemData() {
		return m_oSalesLineItemData;
	}

	public NonStockSalesLineItemData getM_oNonSalesLineItemData() {
		return m_oNonSalesLineItemData;
	}

	public int getM_nUserId() 
	{
		return m_nUserId;
	}

	public void setM_nUserId(int nUserId)
	{
		m_nUserId = nUserId;
	}

	public InvoiceReceiptData getM_oInvoiceReceiptData() {
		return m_oInvoiceReceiptData;
	}

	public void setM_oInvoiceReceiptData(InvoiceReceiptData m_oInvoiceReceiptData) {
		this.m_oInvoiceReceiptData = m_oInvoiceReceiptData;
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

	@Override
	public EntityManager _getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}
}
