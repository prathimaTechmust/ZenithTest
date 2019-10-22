package com.techmust.constants;

public class Constants 
{
	//DB CONFIGURATION DETAILS	
	public static final String CONNECTION_DRIVER_CLASS = "connection.driver_class";
	public static final String CONNECTION_DIALECT = "dialect";
	public static final String CONNECTION_URL = "hibernate.connection.url";
	public static final String CONNECTION_USERNAME = "hibernate.connection.username";
	public static final String CONNECTION_PASSWORD = "hibernate.connection.password";
	public static final String HBM2DDL = "hibernate.hbm2ddl.auto";
	public static final String SHOW_SQL = "show_sql";	
	public static final String TENANT_COOKIE_NAME = "ZENITH_TENANT";
	public static final String ADMIN_ROLE = "ROLE_ADMIN";	
	public static final String TENANT_ID = "tenant_id";
	public static final int DEFAULT_TENANT_ID = 1;
	public static final String DEFAULT_ROLE = "ROLE_USER";	
	public static final String kAdmin = "admin";
	public static final String kTenantAdminRole = "tenantadmin";
	public static final String kUserRole = "user";
	public static final String MESSAGES_BUNDLE_NAME = "MessagesBundle";
	
	//S3 BUCKET DETAILS
	public static final String CLIENTREGION = "ap-southeast-1";
	public static final String BUCKETNAME = "zenithfoundationbucket";	
	public static final String STUDENTIMAGEURL = "https://s3-ap-southeast-1.amazonaws.com/zenithfoundationbucket/";	
	public static final String USERIMAGEURL = "https://s3-ap-southeast-1.amazonaws.com/zenithfoundationbucket/";
	
	//STUDENT APPLICATION STATUS
	public static final String STUDENTVERIFIED = "verified";
	public static final String STUDENTAPPROVED = "approved";
	public static final String STUDENTREJECTED = "rejected";
	public static final String CHEQUEPREPARED = "cheque prepared";
    public static final String  REISSUECHEQUE = "approved";
	public static final String CHEQUEDISBURSED = "cheque disbursed";
	public static final String CHEQUECLAIMED = "cheque claimed";
	public static final String APPLICATIONCOUNSELING = "counseling";
	public static final String APPLICATIONREVERIFICATION = "pending";
	public static final String COUNSELINGSTUDENTVERIFIED = "verified";
	public static final String CHEQUESTATUS = "Active";
	
	//STUDENT DOCUMENTS AND IMAGES S3 BUCKET URL
	public static final String USERIMAGEFOLDER = "images/user/";
	public static final String STUDENTIMAGEFOLDER = "images/student/";
	public static final String STUDENTAADHARDOCUMENTFOLDER = "images/studentaadhar/";
	public static final String STUDENTELECTRICITYBILLDOCUMENTFOLDER = "images/studentelectricitybill/";
	public static final String S3BUCKETURL = "https://s3-ap-southeast-1.amazonaws.com/zenithfoundationbucket/";
	public static final String STUDENTFATHERAADHAR = "images/studentfatheraadhar/";
	public static final String STUDENTMOTHERAADHAR = "images/studentmotheraadhar/";
	public static final String STUDENTMARKSCARD1 = "images/studentmarkscard1/";
	public static final String STUDENTMARKSCARD2 = "images/studentmarkscard2/";
	public static final String STUDENTOTHERDOCUMENTS = "images/studentotherdocuments/";
	public static final String VERIFIEDAPPLICATION = "images/verifiedapplication/";
	
	//DEFAULT FILE EXTENSIONS
	public static final String IMAGE_DEFAULT_EXTENSION = ".jpeg";
	public static final String PDF_DEFAULT_EXTENSION = ".pdf";
	
	//STUDENT SMS AND DOWNLOAD CONSTANTS
	public static final String NUMBERPREFIX = "+91";	
	public static final String REPORTFILENAME = "StudentReports";
	public static final String STUDENTREPORTSFOLDER = "reports/";
	
	//LOGIN USER COOKIE CONSTANTS
	public static final String LOGINUSERNAME = "login_username";	
	public static final String LOGINUSERID = "LoginUserId";
	
	//ERROR DESCRIPTION MESSAGES
	public static final String DELETEERRORDESC = "Student Application status is in Progress hence Cannot be Deleted  ";
	public static final String COURSEDELETEDESC = "Students are part of this course";
	
}
