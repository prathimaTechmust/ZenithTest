package com.techmust.inventory.invoice;

//import com.techmust.vendormanagement.VendorCounterData;
import com.techmust.vendormanagement.VendorData;

public class VendorInvoiceData extends InvoiceData 
{
	private static final long serialVersionUID = 1L;
	private static VendorData m_oVendorData;
	
	public VendorInvoiceData() 
	{
		setM_oVendorData(new VendorData ());
	}
	
	public void setM_oVendorData(VendorData oVendorData) 
	{
		m_oVendorData = oVendorData;
	}
	
	public VendorData getM_oVendorData() 
	{
		return m_oVendorData;
	}
	
	@Override
	protected String generateInvoiceNumber () throws Exception
	{
		//return VendorCounterData.generateSerialNumber(m_oVendorData.getM_nClientId());
		return null;
	}
}
