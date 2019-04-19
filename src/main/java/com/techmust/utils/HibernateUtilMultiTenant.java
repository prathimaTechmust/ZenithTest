package com.techmust.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtilMultiTenant
{
	static String kURL = "";
	static String kDRIVER = "";
	static String kUSERNAME = "";
	static String kPASSWORD = "";
	static String kDIALECT = "";

	@Value("${multitenancy.mtapp.master.datasource.url}")
	public void setkURL(String kURL)
	{
		HibernateUtilMultiTenant.kURL = kURL;
	}

	@Value("${multitenancy.mtapp.master.datasource.driverClassName}")
	public void setkDRIVER(String kDRIVER)
	{
		HibernateUtilMultiTenant.kDRIVER = kDRIVER;
	}

	@Value("${multitenancy.mtapp.master.datasource.username}")
	public void setkUSER(String kUSERNAME)
	{
		HibernateUtilMultiTenant.kUSERNAME = kUSERNAME;
	}

	@Value("${multitenancy.mtapp.master.datasource.password}")
	public void setkPASSWORD(String kPASSWORD)
	{
		HibernateUtilMultiTenant.kPASSWORD = kPASSWORD;
	}

	@Value("${multitenancy.mtapp.master.datasource.dialect}")
	public void setkDIALECT(String kDIALECT)
	{
		HibernateUtilMultiTenant.kDIALECT = kDIALECT;
	}

	public static String getkURL()
	{
		return kURL;
	}

	public static String getkDRIVER()
	{
		return kDRIVER;
	}

	public static String getkUSERNAME()
	{
		return kUSERNAME;
	}

	public static String getkPASSWORD()
	{
		return kPASSWORD;
	}

	public static String getkDIALECT()
	{
		return kDIALECT;
	}
}