package com.techmust.vendormanagement;

import java.util.ArrayList;
import com.techmust.clientmanagement.ClientDataResponse;

public class VendorDataResponse extends ClientDataResponse
{

    private static final long serialVersionUID = 1L;
	public ArrayList<VendorData> m_arrVendorData;
	public String m_strOffcanvasMenu;
	public VendorDataResponse ()
	{
		m_arrVendorData = new ArrayList<VendorData> ();
		m_strOffcanvasMenu = "";
	}
}
