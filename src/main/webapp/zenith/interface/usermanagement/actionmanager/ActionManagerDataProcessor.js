var ActionManagerDataProcessor = (function __ActionManagerDataProcessor ()
{
	function changePassword (oActionManager,callback)
	{
		ajaxCall(oActionManager,"/actionManagerChangePassword",callback);
		
	}
	
	function get(oActionManager,callback)
	{
//		ajaxCall(oActionManager,"/actionManagerGet?tenant_id='sapna_book_stall'",callback);
		ajaxCall(oActionManager,"/actionManagerGet",callback);
		
	}
	
	function logOut(oUserinformationData)
	{
		ajaxCall("","/actionManagerLogout","");
		
	}
	
	function processForgotPassword ()
	{
		ajaxCall("","/actionManagerForgetPassword","");
		
	}
	return { 
		get : get,
		logOut: logOut,
		changePassword: changePassword,
		processForgotPassword: processForgotPassword
	};
})();
