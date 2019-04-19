package com.techmust.generic.data;

import java.util.Collection;
import java.util.HashMap;

public interface IGenericData
{
	boolean saveObject() throws Exception;
	boolean updateObject() throws Exception;
	boolean deleteObject() throws Exception;
	@SuppressWarnings("unchecked")
    Collection list(HashMap<String, String> arrOrderBy) throws Exception;
}
