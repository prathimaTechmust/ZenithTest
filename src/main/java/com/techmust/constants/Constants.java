package com.techmust.constants;

public class Constants 
{
	public static final String MESSAGES_BUNDLE_NAME = "MessagesBundle";
	public static final int DEFAULT_TENANT_ID = 1;
	public static final String DEFAULT_ROLE = "ROLE_USER";	
	public static final String CONNECTION_DRIVER_CLASS = "connection.driver_class";
	public static final String CONNECTION_DIALECT = "dialect";
	public static final String CONNECTION_URL = "hibernate.connection.url";
	public static final String CONNECTION_USERNAME = "hibernate.connection.username";
	public static final String CONNECTION_PASSWORD = "hibernate.connection.password";
	public static final String HBM2DDL = "hibernate.hbm2ddl.auto";
	public static final String SHOW_SQL = "show_sql";	
	public static final String TENANT_COOKIE_NAME = "TRADEMUST_TENANT";
	public static final String ADMIN_ROLE = "ROLE_ADMIN";
	public static final String IMAGE_DEFAULT_EXTENSION = ".jpeg";
	public static final String TENANT_ID = "tenant_id";
	public static final String kAdmin = "admin";
	public static final String kVendorRole = "Vendor";
	public static final String kClientRole = "client";
	public static final String kTenantAdminRole = "tenantadmin";
	public static final String kUserRole = "user";
	
	//connection URL for organization
	public static final String ORGANIZATION_CREATION_DB_URL = "jdbc:mysql://zenithdb.cdwear8cwwuw.ap-southeast-1.rds.amazonaws.com:3306/%s?useSSL=false&createDatabaseIfNotExist=TRUE";
	public static final String DBUSERNAME = "zenith";
	public static final String DBPASSWORD = "tmspl5629D!";
	//public static final String ORGANIZATION_CREATION_CON_URL = "jdbc:mysql://kubera.ckevpqhd0yy7.ap-southeast-1.rds.amazonaws.com:3306/%s?useSSL=false";
}
