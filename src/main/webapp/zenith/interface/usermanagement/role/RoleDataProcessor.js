var RoleDataProcessor = (function __RoleDataProcessor ()
{
	function create (oRoleData,callback)
	{
		ajaxCall(oRoleData,"/roleCreate",callback);
		
	}
	
	function get(oRoleData,callback)
	{
		ajaxCall(oRoleData,"/roleGet",callback);
		
	}
	
	function listActionData(callback)
	{
		ajaxCall("","/roleListActionData",callback);
	}
	
	function list(oRoleData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oRoleData:oRoleData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper,"/roleList",callback);
		 
	}
	
	function update (oRoleData, callback)
	{
		ajaxCall(oRoleData,"/roleUpdate",callback);
		
	}
	
	function deleteData (oRoleData, callback)
	{
		ajaxCall(oRoleData,"/roleDelete",callback);
		
	}
	
	function getXML (oRoleData, callback)
	{
		ajaxXMLCall(oRoleData,"/roleGetXML",callback);

	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		listActionData:listActionData,
		deleteData : deleteData, 
		getXML : getXML
	};
})();