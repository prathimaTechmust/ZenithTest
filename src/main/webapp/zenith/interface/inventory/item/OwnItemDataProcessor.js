var OwnItemDataProcessor = (function __OwnItemDataProcessor ()
{
	function create (oItemData,callback)
	{
		ajaxCall(oItemData, "/ownItemDataCreate", callback);
	}
	
	return {
		create:create
	};
})();