package com.techmust.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import com.techmust.constants.Constants;

/**
 * Utility class for DataSource
 * 
 */
public final class DBUtil
{
	
	public static void  createTable(String strDBUrl)
	{
		MetadataSources oMetadataSources = getMetadataSources(strDBUrl);		
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
		arrsettings.put(Constants.HBM2DDL, "create");
		arrsettings.put(Constants.SHOW_SQL, "true");
		return arrsettings;
	}
}