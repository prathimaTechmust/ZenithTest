package com.techmust.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ClientGroupData;
import com.techmust.clientmanagement.ContactData;
import com.techmust.clientmanagement.DemographyData;
import com.techmust.clientmanagement.SiteData;
import com.techmust.constants.Constants;
import com.techmust.generic.email.AttachmentData;
import com.techmust.generic.email.EmailMessageData;
import com.techmust.generic.email.MessageData;
import com.techmust.generic.email.template.TemplateCategoryData;
import com.techmust.generic.email.template.TemplateData;
import com.techmust.generic.exportimport.DataExchangeClasses;
import com.techmust.generic.exportimport.ExportImportProviderData;
import com.techmust.generic.log.LogData;
import com.techmust.inventory.challan.ChallanData;
import com.techmust.inventory.configuration.ConfigurationData;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.invoice.OnlineVendorInvoiceData;
import com.techmust.inventory.items.ChildItemData;
import com.techmust.inventory.items.ItemCategoryData;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.ItemGroupData;
import com.techmust.inventory.items.ItemImagesData;
import com.techmust.inventory.items.VendorItemData;
import com.techmust.inventory.location.LocationData;
import com.techmust.inventory.paymentsandreceipt.InvoiceReceiptData;
import com.techmust.inventory.paymentsandreceipt.OnlineReceiptData;
import com.techmust.inventory.paymentsandreceipt.PaymentData;
import com.techmust.inventory.paymentsandreceipt.PurchasePaymentData;
import com.techmust.inventory.paymentsandreceipt.ReceiptData;
import com.techmust.inventory.paymentsandreceipt.TransactionMode;
import com.techmust.inventory.purchase.NonStockPurchaseLineItem;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.inventory.purchase.PurchaseLineItem;
import com.techmust.inventory.purchaseorder.PurchaseOrderData;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemData;
import com.techmust.inventory.purchaseorder.PurchaseOrderStockLineItemData;
import com.techmust.inventory.purchasereturned.PurchaseReturnedData;
import com.techmust.inventory.purchasereturned.PurchaseReturnedLineItemData;
import com.techmust.inventory.quotation.QuotationAttachmentData;
import com.techmust.inventory.quotation.QuotationData;
import com.techmust.inventory.quotation.QuotationLineItemData;
import com.techmust.inventory.quotation.logs.QuotationLogData;
import com.techmust.inventory.returned.NonStockReturnedLineItemData;
import com.techmust.inventory.returned.ReturnedData;
import com.techmust.inventory.returned.ReturnedLineItemData;
import com.techmust.inventory.sales.CustomizedItemData;
import com.techmust.inventory.sales.DiscountStructureData;
import com.techmust.inventory.sales.NonStockSalesLineItemData;
import com.techmust.inventory.sales.SalesData;
import com.techmust.inventory.sales.SalesLineItemData;
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.stocktransfer.ItemLocationData;
import com.techmust.inventory.stocktransfer.StockTransferData;
import com.techmust.inventory.stocktransfer.StockTransferMemoData;
import com.techmust.inventory.supply.SupplyData;
import com.techmust.inventory.utility.TallyTransformData;
import com.techmust.inventory.utility.TallyTransformKeyData;
import com.techmust.inventory.vendorpurchaseorder.VendorPOLineItemData;
import com.techmust.inventory.vendorpurchaseorder.VendorPurchaseOrderData;
import com.techmust.master.model.tenant.MasterTenant;
import com.techmust.property.PropertyData;
import com.techmust.property.PropertyDetailData;
import com.techmust.property.PropertyPhotoData;
import com.techmust.property.propertytype.PropertyTypeData;
import com.techmust.vendormanagement.VendorCounterData;
import com.techmust.vendormanagement.VendorData;
import com.techmust.vendormanagement.VendorGroupData;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Utility class for DataSource
 * 
 */
public final class DBUtil
{
	private static final Logger LOG = LoggerFactory.getLogger(DBUtil.class);	
	public static DataSource createAndConfigureDataSource(MasterTenant oMasterTenant)
	{
		HikariDataSource oHikariDataSource = new HikariDataSource();
		oHikariDataSource.setUsername(oMasterTenant.getM_strConnectionUsername());
		oHikariDataSource.setPassword(oMasterTenant.getM_strConnectionPassword());
		oHikariDataSource.setJdbcUrl(oMasterTenant.getM_strUrl());
		oHikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		// HikariCP settings - could come from the master_tenant table but        
		// hardcoded here for brevity
		// Maximum waiting time for a connection from the pool
		oHikariDataSource.setConnectionTimeout(20000);
		// Minimum number of idle connections in the pool
		oHikariDataSource.setMinimumIdle(1);
		// Maximum number of actual connection in the pool
		oHikariDataSource.setMaximumPoolSize(20);
		// Maximum time that a connection is allowed to sit idle in the pool
		oHikariDataSource.setIdleTimeout(10000);
		oHikariDataSource.setMaxLifetime(60000);
		// Setting up a pool name for each tenant datasource
		int tenantId = oMasterTenant.getM_nTenantId();
		String tenantConnectionPoolName = tenantId + "-connection-pool";
		oHikariDataSource.setPoolName(tenantConnectionPoolName);
		LOG.info("Configured datasource:" + oMasterTenant.getM_nTenantId()+ ". Connection poolname:" + tenantConnectionPoolName);
		return oHikariDataSource;
	}
	
	public static void  createTable(String strDBUrl)
	{
		MetadataSources oMetadataSources = getMetadataSources(strDBUrl);		
		oMetadataSources.addAnnotatedClass(ClientData.class);
		oMetadataSources.addAnnotatedClass(ClientGroupData.class);
		oMetadataSources.addAnnotatedClass(ContactData.class);
		oMetadataSources.addAnnotatedClass(DemographyData.class);
		oMetadataSources.addAnnotatedClass(SiteData.class);
		oMetadataSources.addAnnotatedClass(ChallanData.class);
		oMetadataSources.addAnnotatedClass(ConfigurationData.class);
		oMetadataSources.addAnnotatedClass(InvoiceData.class);
		oMetadataSources.addAnnotatedClass(OnlineVendorInvoiceData.class);
		oMetadataSources.addAnnotatedClass(ChildItemData.class);
		oMetadataSources.addAnnotatedClass(ItemCategoryData.class);
		oMetadataSources.addAnnotatedClass(ItemData.class);
		oMetadataSources.addAnnotatedClass(ItemGroupData.class);
		oMetadataSources.addAnnotatedClass(ItemImagesData.class);
		oMetadataSources.addAnnotatedClass(VendorItemData.class);
		oMetadataSources.addAnnotatedClass(LocationData.class);
		oMetadataSources.addAnnotatedClass(InvoiceReceiptData.class);
		oMetadataSources.addAnnotatedClass(OnlineReceiptData.class);
		oMetadataSources.addAnnotatedClass(PaymentData.class);
		oMetadataSources.addAnnotatedClass(PurchasePaymentData.class);
		oMetadataSources.addAnnotatedClass(ReceiptData.class);
		oMetadataSources.addAnnotatedClass(TransactionMode.class);
		oMetadataSources.addAnnotatedClass(NonStockPurchaseLineItem.class);
		oMetadataSources.addAnnotatedClass(PurchaseData.class);
		oMetadataSources.addAnnotatedClass(PurchaseLineItem.class);
		oMetadataSources.addAnnotatedClass(PurchaseOrderData.class);
		oMetadataSources.addAnnotatedClass(PurchaseOrderLineItemData.class);
		oMetadataSources.addAnnotatedClass(PurchaseOrderStockLineItemData.class);
		oMetadataSources.addAnnotatedClass(PurchaseReturnedData.class);
		oMetadataSources.addAnnotatedClass(PurchaseReturnedLineItemData.class);
		oMetadataSources.addAnnotatedClass(QuotationLogData.class);
		oMetadataSources.addAnnotatedClass(QuotationAttachmentData.class);
		oMetadataSources.addAnnotatedClass(QuotationData.class);
		oMetadataSources.addAnnotatedClass(QuotationLineItemData.class);
		oMetadataSources.addAnnotatedClass(NonStockReturnedLineItemData.class);
		oMetadataSources.addAnnotatedClass(ReturnedData.class);
		oMetadataSources.addAnnotatedClass(ReturnedLineItemData.class);
		oMetadataSources.addAnnotatedClass(CustomizedItemData.class);
		oMetadataSources.addAnnotatedClass(DiscountStructureData.class);
		oMetadataSources.addAnnotatedClass(NonStockSalesLineItemData.class);
		oMetadataSources.addAnnotatedClass(SalesData.class);
		oMetadataSources.addAnnotatedClass(SalesLineItemData.class);
		oMetadataSources.addAnnotatedClass(SerialNumberData.class);
		oMetadataSources.addAnnotatedClass(ItemLocationData.class);
		oMetadataSources.addAnnotatedClass(StockTransferData.class);
		oMetadataSources.addAnnotatedClass(StockTransferMemoData.class);
		oMetadataSources.addAnnotatedClass(SupplyData.class);
		oMetadataSources.addAnnotatedClass(TallyTransformData.class);
		oMetadataSources.addAnnotatedClass(TallyTransformKeyData.class);
		oMetadataSources.addAnnotatedClass(VendorPOLineItemData.class);
		oMetadataSources.addAnnotatedClass(VendorPurchaseOrderData.class);
		oMetadataSources.addAnnotatedClass(TemplateCategoryData.class);
		oMetadataSources.addAnnotatedClass(TemplateData.class);
		oMetadataSources.addAnnotatedClass(AttachmentData.class);
		oMetadataSources.addAnnotatedClass(EmailMessageData.class);
		oMetadataSources.addAnnotatedClass(MessageData.class);
		oMetadataSources.addAnnotatedClass(DataExchangeClasses.class);
		oMetadataSources.addAnnotatedClass(ExportImportProviderData.class);
		oMetadataSources.addAnnotatedClass(LogData.class);
		oMetadataSources.addAnnotatedClass(VendorCounterData.class);
		oMetadataSources.addAnnotatedClass(VendorData.class);
		oMetadataSources.addAnnotatedClass(VendorGroupData.class);
		oMetadataSources.addAnnotatedClass(PropertyTypeData.class);
		oMetadataSources.addAnnotatedClass(PropertyData.class);
		oMetadataSources.addAnnotatedClass(PropertyDetailData.class);
		oMetadataSources.addAnnotatedClass(PropertyPhotoData.class);
        SchemaExport oSchemaExport = new SchemaExport ();
        oSchemaExport.execute(EnumSet.of(TargetType.DATABASE), SchemaExport.Action.CREATE, oMetadataSources.buildMetadata());
	}
	private static MetadataSources getMetadataSources(String strDBUrl)
	{
		Map<String, String> arrsettings = getConnectionSettings(strDBUrl);		
		MetadataSources oMetadataSources = new MetadataSources(
	    new StandardServiceRegistryBuilder()
	    .applySettings (arrsettings)
	    .build());		
		return oMetadataSources;
	}
	
	private static Map<String, String> getConnectionSettings(String strDBUrl)
	{
		Map<String, String> arrsettings = new HashMap<>();
		arrsettings.put(Constants.CONNECTION_DRIVER_CLASS, "com.mysql.cj.jdbc.Driver" );
		arrsettings.put(Constants.CONNECTION_DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
		arrsettings.put(Constants.CONNECTION_URL, strDBUrl);
		arrsettings.put(Constants.CONNECTION_USERNAME, Constants.DBUSERNAME);
		arrsettings.put(Constants.CONNECTION_PASSWORD, Constants.DBPASSWORD);
		arrsettings.put(Constants.HBM2DDL, "create");
		arrsettings.put(Constants.SHOW_SQL, "true");
		return arrsettings;
	}
}