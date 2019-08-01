function zenith_MemberData ()
{
	var m_nUserId = -1;
	var m_nUID = -1;
	var m_nSelectedClientId = -1;
	var m_strUser = "";
	var m_strLoginId = "";
	var m_nOnlineUserId = -1;
	var m_arrObjects = [];
	var m_strTenantName = "";
	var m_strImageUrl = "";
}

var m_strLocationURL = window.location.origin;

var m_oZenithMemberData = new zenith_MemberData ();

var m_oPagesLookup = {};

var zenith_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js',
	'widgets/scholarshipmanagement/academicdetails/AcademicDetails.js'
];

includeDataObjects (zenith_includeDataObjects, "");

function navigate (tabName, pageName)
{
	loadWidget(pageName);
}

$.extend($.fn.validatebox.defaults.rules, 
		{
		    equals: 
		    {
		        validator: function(value,param)
		        {
		            return value == $(param[0]).val();
		        },
		        message: 'Password does not match.'
		    }
		});

$.extend($.fn.validatebox.defaults.rules, 
		{
    		maxLength: 
    		{
        		validator: function(value, param)
        		{
            		return value.length <= param[0];
        		},
        		message: 'Max. {0} characters.'
    		}
		});

$.extend($.fn.validatebox.defaults.rules, {
    minLength: {
        validator: function(value, param){
            return value.length >= param[0];
        },
        message: 'Please enter at least {0} characters.'
    }
});

function studentList_setPreview (m_strStudentImageUrl)
{
	m_oZenithMemberData.m_strImageUrl = m_strStudentImageUrl;
	loadPage ("scholarshipmanagement/student/studentImagePreview.html", "dialog", "studentList_showImagePreview ()");
}

function studentList_showImagePreview ()
{
	createPopup ('dialog', '', '', true);
	document.getElementById('dialog').style.position = "fixed";
	$(".imagePreview").attr('src', m_oZenithMemberData.m_strImageUrl);
}

function studentList_cancelImagePreview ()
{
	HideDialog ("dialog");
}
function isUrlValid(fieldId) {
	var url = $("#"+fieldId).val();
    if(!(/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i
           .test(url))) {
    	$("#"+fieldId).css({'border': '2px solid #ffa8a8', 'border-style': 'inset'});
    } else {
    	$("#"+fieldId).css({'border': '1px solid #ddd', 'border-style': 'groove'});
    }
}

// Using this method causes an extra label being added below the error field which disturbs the current layout.
/* 
 jQuery.validator.setDefaults({
  debug: true,
  success: "valid"
});
function isUrlValid(frmId) {
		$( "#"+frmId ).validate({
		  rules: {
		    field: {
		      email: true
		    }
		  }
		});
}
*/

function loadWidget(widgetName)
{
	var oScript = document.createElement('script')
	oScript.setAttribute("type","text/javascript")
	oScript.setAttribute("LANGUAGE","javascript")
	oScript.setAttribute("src", widgetName)
	document.getElementsByTagName("head")[0].appendChild(oScript)
}

function loadPage (toPage, container, onCompleteCallBack)
{
	var newDiv = document.createElement ('div'), strHtmlBody="";
	var bCachedPagePresence = m_oPagesLookup.hasOwnProperty(toPage);
//	bCachedPagePresence = false;
	if(!bCachedPagePresence) {
	    var client = getXMLHttpRequest ();
	    client.open('GET', toPage);
	    client.setRequestHeader ("Content-type", "text/html");
	    client.onreadystatechange = function() 
	    {
	        if (client.readyState == 4)
	        {
	        	strHtmlBody = client.responseText;
	        	m_oPagesLookup[toPage] = strHtmlBody;
	            newDiv.innerHTML = strHtmlBody;
	            var containerDiv = removeAllChildren (container);
	            containerDiv.appendChild (newDiv);
	            document.getElementById("status").innerHTML = "";
	            eval(onCompleteCallBack);
	        }
	    }
	    client.send();
	}
	else
	{
        newDiv.innerHTML = m_oPagesLookup[toPage];
        var containerDiv = removeAllChildren (container);
        containerDiv.appendChild (newDiv);
        document.getElementById("status").innerHTML = "";
        eval(onCompleteCallBack);
	}
    document.getElementById("status").innerHTML = "<p>loading "+toPage+"...in "+container+"</p>";
    
}

function viewStudentDocument(academicId) 
{
	var oAcademicDetails = new AcademicDetails ();
	oAcademicDetails.m_nAcademicId = academicId;
	AcademicDetailsDataProcessor.getStudentDocuments(oAcademicDetails,studentUploadedDocumentsResponse);
	
}

function studentUploadedDocumentsResponse (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		m_oZenithMemberData.m_oStudentDocuments = oResponse.m_oStudentDocuments;
		loadPage("applicationstatus/documentView/studentDocumentView.html","dialog","viewStudentDocumentDetails()");
	}
	else
		informUser("No Documents Uploaded!!","kError");
}

function viewStudentDocumentDetails ()
{
	viewStudentDocuments_init();
}

function viewStudentDocuments_init()
{	
	createPopup('dialog','','chequeRemarkInfo_button_cancel', true);
	setUploadedDocuments(m_oZenithMemberData.m_oStudentDocuments);
}

function setUploadedDocuments(oStudentDocuments)
{
	if(oStudentDocuments != null && oStudentDocuments.m_strStudentAadhar != null)
		$("#studentAadharId").attr('src',oStudentDocuments.m_strStudentAadhar);
	if(oStudentDocuments != null && oStudentDocuments.m_strFatherAadharImageId != null)
		$("#fatherAadharId").attr('src',oStudentDocuments.m_strFatherAadharImageId);
	if(oStudentDocuments != null && oStudentDocuments.m_strMotherAadharImageId !=null)
		$("#motherAadharId").attr('src',oStudentDocuments.m_strMotherAadharImageId);
	if(oStudentDocuments != null && oStudentDocuments.m_strStudentElectricityBill !=null)
		$("#electricityBillId").attr('src',oStudentDocuments.m_strStudentElectricityBill);
	if(oStudentDocuments != null && oStudentDocuments.m_strStudentMarksCard1 !=null)
		$("#marksCard1Id").attr('src',oStudentDocuments.m_strStudentMarksCard1);
	if(oStudentDocuments != null && oStudentDocuments.m_strStudentMarksCard2 !=null)
		$("#marksCard2Id").attr('src',oStudentDocuments.m_strStudentMarksCard2);
	if(oStudentDocuments != null && oStudentDocuments.m_strOtherDocuments !=null)
		$("#additionalDocumentId").attr('src',oStudentDocuments.m_strOtherDocuments);	
	if(oStudentDocuments != null && oStudentDocuments.m_strVerifyScanDocument !=null)
		$("#scanDocumentId").attr('src', oStudentDocuments.m_strVerifyScanDocument);
}

function viewStudentDocument_cancel() 
{
	HideDialog ("dialog");
}

function studentDocumentView_documentPreview (imageId)
{
	var imageSrc = document.getElementById(imageId.id).src;
	m_oZenithMemberData.m_strImagePreviewUrl = imageSrc;
	loadPage("applicationstatus/documentView/uploadDocumentView.html","secondDialog", "documentList_showImagePreview()");
}

function documentList_showImagePreview() 
{
	 createPopup ('secondDialog', '', '', true);
	 document.getElementById('secondDialog').style.position = "fixed";
	 $(".imagePreview").attr('src', m_oZenithMemberData.m_strImagePreviewUrl);    
}

function documentList_cancelImagePreview() 
{
	HideDialog ("secondDialog");	
}

function removeAllChildren (container)
{
    var containerDiv = document.getElementById (container);
    var children = containerDiv.childNodes;
    for (var nIndex = children.length - 1; nIndex >= 0; nIndex++)
    {
        try
        {
            containerDiv.removeChild (children.item (nIndex));
        }
        catch (exception)
        {
            break;
        }
    }
    return containerDiv;
}

function getXMLHttpRequest() 
{
    var client = null;
    if (window.XMLHttpRequest) 
    {
        client = new window.XMLHttpRequest;
    }
    else 
    {
        try 
        {
            client = new ActiveXObject("MSXML2.XMLHTTP.3.0");
        }
        catch(ex) 
        {
        }
    }
    return client;
}

function PopulateDD (strDDID, arrOptions)
{
	oDD = document.getElementById(strDDID);
	for (nIndex = oDD.length; nIndex >= 0; nIndex--)
		oDD.remove(nIndex);

	for (nIndex = 0; nIndex < arrOptions.length; nIndex++)
		oDD.appendChild (arrOptions [nIndex]);
}

function CreateOption (strID, strValue)
{	
	var oOption = document.createElement ("option");
	oOption.setAttribute ("id", strID);
	oOption.setAttribute ("value", strID);
	oOption.setAttribute ("title", strValue);
	oOption.appendChild (document.createTextNode (strValue));
	return oOption;
}

function createPopup (strPopupId, strCloseBtn, strSubmitBtn, bShow)
{
	document.getElementById(strPopupId).style.position = "absolute";
	
	$(document).ready(function ()
	{
      $(strCloseBtn).click(function (e)
      {
         e.preventDefault();
      });
      $(strSubmitBtn).click(function (e)
      {
         e.preventDefault();
      });
  });
	
	if(bShow)
	{
		ShowDialog(bShow, strPopupId);
	}
}

function ShowDialog (modal, strPopupId)
{
   $("#overlay").show();
   $("#"+strPopupId).fadeIn(300);
   if (modal)
   {
 	  $("#overlay").unbind("click");
   }
  else
  {
     $("#overlay").click(function (e)
      {
         HideDialog(strPopupId);
      });
  }
}

function HideDialog (strPopupId)
{
   $("#overlay").hide();
   $("#"+strPopupId).fadeOut(300);
}

function initMenu ()
{
	$('#cssmenu ul ul li:odd').addClass('odd');
	$('#cssmenu ul ul li:even').addClass('even');
	$('#cssmenu > ul > li > a').click(function() 
	{
	  $('#cssmenu li').removeClass('active');
	  $(this).closest('li').addClass('active');	
	  var checkElement = $(this).next();
	  if((checkElement.is('ul')) && (checkElement.is(':visible'))) 
	  {
	    $(this).closest('li').removeClass('active');
	    checkElement.slideUp('normal');
	  }
	  if((checkElement.is('ul')) && (!checkElement.is(':visible'))) 
	  {
	    $('#cssmenu ul ul:visible').slideUp('normal');
	    checkElement.slideDown('normal');
	  }
	  if($(this).closest('li').find('ul').children().length == 0)
	  {
	    return true;
	  } 
	  else 
	  {
	    return false;	
	  }		
	});
}

function includeDataObjects (arrObjects, callBack)
{
	m_oZenithMemberData.m_arrObjects = arrObjects;
	m_oZenithMemberData.m_nIncludeIndex = 0;
	m_oZenithMemberData.m_strIncludeCallback = callBack;
	navigate ('', arrObjects[0]);
}

function dataObjectLoaded ()
{
	m_oZenithMemberData.m_nIncludeIndex++;
	if (m_oZenithMemberData.m_nIncludeIndex >= m_oZenithMemberData.m_arrObjects.length)
		eval (m_oZenithMemberData.m_strIncludeCallback);
	else
		navigate ('', m_oZenithMemberData.m_arrObjects[m_oZenithMemberData.m_nIncludeIndex]);
}

function getImageName(oImagePath) 
{
	if (oImagePath)
	{
        var startIndex = (oImagePath.indexOf('\\') >= 0 ? oImagePath.lastIndexOf('\\') : oImagePath.lastIndexOf('/'));
        var strImageName = oImagePath.substring(startIndex);
        if (strImageName.indexOf('\\') === 0 || strImageName.indexOf('/') === 0) 
        	strImageName = strImageName.substring(1);
	}
	return strImageName;
}

function logout ()
{
	var oUserinformationData = new UserInformationData ();
	oUserinformationData.m_nUserId = m_oZenithMemberData.m_nUserId;
	ActionManagerDataProcessor.logOut (oUserinformationData);
	document.cookie ="zenithUserId=";
	document.cookie ="zenithPassword=";
	m_oZenithMemberData.m_nUserId = -1;
	index_div_login.style.visibility = "hidden";
	index_div_userName.textContent = "";
	window.location.href = window.location.href.split("?")[0];
	navigate ('','widgets/usermanagement/actionmanager/login.js');
}

function initializeDatePicker (elementId)
{
	$(elementId).datepicker({
	    changeMonth: true,
	    changeYear: true,
		yearRange: "-100:+100",
		dateFormat: "dd-mm-yy"});

}

function informUser (strMessage, strMessageType)
{
	jQuery.i18n.properties
	({
	    name:'Messages', 
	    path:'bundle/', 
	    mode:'map',
	    language:'en',
	    callback: function() 
	    {
			strMessage = (jQuery.i18n.map [strMessage] == undefined) ? strMessage : jQuery.i18n.map [strMessage];
			showMessage (strMessage, strMessageType)
	    }
	});
}

function showMessage (strMessage, strMessageType)
{
	document.getElementById('display-message').className = '';
	document.getElementById("display-message").innerHTML = strMessage;
	if (strMessageType == "kAlert")
		alert (strMessage);
	else if(strMessageType == "kSuccess")
		$("#display-message").addClass("display-success").show();
	else if (strMessageType == "kError")
		$("#display-message").addClass("display-error").show();
	else if (strMessageType == "kWarning")
		$("#display-message").addClass("display-warning").show();
	else
		$("#display-message").addClass("display-info").show();
	$("#display-message").delay(8000).slideUp();
	$("#display-message").click(function()
		{
			$("#display-message").hide();
		});
}

function getUserConfirmation (strMessage)
{
	var bConfirm = false;
	jQuery.i18n.properties
	({
	    name:'Messages', 
	    path:'bundle/', 
	    mode:'map',
	    language:'en',
	    callback: function() 
	    {
			bConfirm = confirm ((jQuery.i18n.map [strMessage] == undefined) ? strMessage : jQuery.i18n.map [strMessage]);
	    }
	});
	return bConfirm;
}

function validateNumber (data) 
{
	  var invalidChars = /[^0-9]/gi
	  if(invalidChars.test(data.value)) 
	  {
		  alert('Please Enter Numbers Only');
		  data.value = data.value.replace(invalidChars,"");		  
	  }
}

function validateFloatNumber (data) 
{
	  var invalidChars = /[^0-9.]/gi
	  if(invalidChars.test(data.value)) 
	  {
		  data.value = data.value.replace(invalidChars,"");
	  }
}

function validatePercentage(data)
{
	var x = parseFloat(data.value); 
	  if (isNaN(x) || x < 0 || x > 100) 
	  { 
	    data.value = data.value.replace(data.value,"");
	  }
}

function clearGridData (strDataGridId)
{
	try
	{
		$(strDataGridId).datagrid('loadData', {"total": 0, "rows":[]});
	}
	catch (oException)
	{	
		informUser ("clearGridData - oException : "+ oException, "kError");
	}
}

function loadXSLDoc(xslDoc)
{
	if (window.ActiveXObject || navigator.userAgent.search ('Trident') > 0) // internet explorer
	{
		xhttp = new ActiveXObject("MSXML2.DOMDocument");
		xhttp.async = false
		xhttp.load(xslDoc)
	}
	else
	{
		xhttp = new XMLHttpRequest();
		xhttp.open("GET",xslDoc,false);
		xhttp.send("");
		xhttp = xhttp.responseXML;
	}
	return xhttp;
}

function parseXmlDoc (strXML)
{
	var xmlDoc = null;
	if (window.DOMParser && navigator.userAgent.search ('Trident') < 0)
	{
		parser=new DOMParser();
		xmlDoc=parser.parseFromString(strXML,"text/xml");
	}
	else // Internet Explorer
	{
		xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async=false;
		xmlDoc.loadXML(strXML);
	}
	return xmlDoc;
}

function populateXMLData (strXML, strStyleSheet, oDivision)
{
	var xml = parseXmlDoc (strXML);
	var xsl = loadXSLDoc (strStyleSheet);
	if (window.ActiveXObject || navigator.userAgent.search ('Trident') > 0)// internet explorer
	{
		try
		{
			ex=xml.transformNode(xsl);
			document.getElementById(oDivision).innerHTML=ex;
		}
		catch (oException)
		{
            // possibly IE10
		}
	}
	else if (document.implementation && document.implementation.createDocument)
	{
		xsltProcessor=new XSLTProcessor();
		xsltProcessor.importStylesheet(xsl);
		resultDocument = xsltProcessor.transformToFragment(xml,document);
		var oElement = document.getElementById(oDivision)
		oElement.appendChild(resultDocument);
	}
}

function validateForm (strFormId)
{
	var bValid = true;
	var arrElements = $("#"+strFormId).children().find (".easyui-validatebox");
	for (var nIndex=0; nIndex < arrElements.length; nIndex++)
		if ((bValid = $('#'+arrElements[nIndex].id).validatebox ("isValid")) == false)
			break;
	return bValid;
}

function initFormValidateBoxes (strFormId)
{
	var arrElements = $("#"+strFormId).children().find (".easyui-validatebox");
	for (var nIndex=0; nIndex < arrElements.length; nIndex++)
		 $('#'+arrElements[nIndex].id).validatebox ();
}

function changePassword ()
{
	navigate ('','widgets/usermanagement/actionmanager/changePassword.js');
}

function setFocus(oItemId)
{
	document.getElementById(oItemId).focus();
}

function getSharedByList (arrConnections)
{
	var arrSharedByUsers = new Array ();
	for (var  nIndex = 0 ; nIndex < arrConnections.length ; nIndex++)
	{
		var oBusinessInformationData = arrConnections[nIndex].m_oSharedByUser;
		oBusinessInformationData.m_strRecommendedByDetail = arrConnections[nIndex].m_oInitiatedByUser.m_strUserName+"<br/>"+arrConnections[nIndex].m_oInitiatedByUser.m_strDesignation+"<br/>"+arrConnections[nIndex].m_oInitiatedByUser.m_strCompanyName;
		oBusinessInformationData.m_nInitiatedUserId = arrConnections[nIndex].m_oInitiatedByUser.m_nUserId;
		arrSharedByUsers.push(oBusinessInformationData);
	}
	return arrSharedByUsers;
}

function getSharedWithList (arrConnections)
{
	var arrSharedWithUsers = new Array ();
	for (var  nIndex = 0 ; nIndex < arrConnections.length ; nIndex++)
	{
		var oBusinessInformationData = arrConnections[nIndex].m_oSharedWithUser;
		oBusinessInformationData.m_strRecommendedByDetail = arrConnections[nIndex].m_oInitiatedByUser.m_strUserName+"<br/>"+arrConnections[nIndex].m_oInitiatedByUser.m_strDesignation+"<br/>"+arrConnections[nIndex].m_oInitiatedByUser.m_strCompanyName;
		arrSharedWithUsers.push(oBusinessInformationData);
	}
	return arrSharedWithUsers;
}

//StudentDetails Print Method
function printDocument ()
{
	var printContent = document.getElementById("printdetailsInfo");	
	 var openWindow = window.open("", "", "");	   
	    openWindow.document.write(printContent.innerHTML);
	    openWindow.document.close();
	    openWindow.focus();
	    openWindow.print();
	    openWindow.close();
	    document.getElementById("printdetailsInfo").innerHTML = "";
}

//Converting Date functions
function convertDateToTimeStamp(strDate)
{
    strDate= strDate.split("-");
    var newDate = strDate[0]+"/"+strDate[1]+"/"+strDate[2];
    value = new Date(newDate).getTime();
    return value;
}

function convertTimestampToDate(dTimeStamp)
{
    var timeStamp = new Date(dTimeStamp);
   var strDate = timeStamp.getFullYear() + "-" +("0" + (timeStamp.getMonth() + 1)).slice(-2) +  "-" +("0" + (timeStamp.getDate())).slice(-2);	
   return strDate; 
}

function FormatDate (strDate)
{
	var arrDate = strDate.split('/');
	var strFormatDate = strDate;
	if (arrDate.length > 2)
		strFormatDate = arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2];
	return strFormatDate;
}

function FormatDateToSet (strDate)
{
	var arrDate = strDate.split('-');
	var strFormatDate = arrDate[1] + "/" + arrDate[0] + "/" + arrDate[2];
	return strFormatDate;
}

function processConfirmation (strOkBtnName, strCancelBtnName, strMessage, strCallbackFunction)
{
	$.messager.defaults.ok = strOkBtnName;
	$.messager.defaults.cancel = strCancelBtnName;
	$.messager.confirm ('Confirm', strMessage, strCallbackFunction);
}

function getStyle(arrElement, cssprop)
{
	 if (arrElement.currentStyle) //IE
	  return arrElement.currentStyle[cssprop]
	 else if (document.defaultView && document.defaultView.getComputedStyle) //Firefox
	  return document.defaultView.getComputedStyle(arrElement, "")[cssprop]
	 else //try and get inline style
	  return arrElement.style[cssprop]
}

function getStyleHeight (strStyleName)
{
	return parseInt (getStyle(document.getElementById(strStyleName), 'height'));
}

function formatNumber (value,row,index)
{
	var nNumber= value;
	try
	{
		 var nlastThreeDigits = nNumber.substring(nNumber.length-6);
		 var nOtherDigits = nNumber.substring(0,nNumber.length-6);
		 if(nOtherDigits != '')
			 nlastThreeDigits = ',' + nlastThreeDigits;
		 var nFormattedDigits = nOtherDigits.replace(/\B(?=(\d{2})+(?!\d))/g, ",") + nlastThreeDigits;
		 return nFormattedDigits;	 
	}
	catch(oException){}
}

function progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
}

function refreshDataGrid (strDataGridID)
{
	var nLength = $(strDataGridID).datagrid('getData').rows.length;
	for (var nIndex = 0; nIndex < nLength; nIndex++)
		$(strDataGridID).datagrid('refreshRow', nIndex);
}

function handleFilter (strOnclickFunction, strFocusID)
{
		if(event.keyCode == 13)
		{
			event.preventDefault();
			eval(strOnclickFunction);
			$(strFocusID).focus();
		}
}

function generateXML (xmlDoc, arrData, strParentElement, strChildElement)
{
	oElement = xmlDoc.createElement(strChildElement);
	oRootElement = xmlDoc.getElementsByTagName(strParentElement)[0];
	oRootElement.appendChild(oElement);
	for(var nIndex = 0; nIndex < arrData.length; nIndex++)
		arrData [nIndex].generateXML (xmlDoc, strChildElement);
	return (new XMLSerializer()).serializeToString(xmlDoc);
}

function addChild(xmlDoc, strParent, strTagName, strValue)
{
	var nNodesLength = xmlDoc.getElementsByTagName(strParent).length;
	oElement = xmlDoc.getElementsByTagName(strParent)[nNodesLength -1];
	oNewElement = xmlDoc.createElement(strTagName);
	oTextNode = xmlDoc.createTextNode(strValue);
	oNewElement.appendChild(oTextNode);
	oElement.appendChild(oNewElement);
}

function loadXMLDoc(strXMLFile)
{
	if (window.XMLHttpRequest)
	{
		xhttp = new XMLHttpRequest();
	}
	else // code for IE5 and IE6
	{
		xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhttp.open("GET", strXMLFile, false);
	xhttp.send();
	return xhttp.responseXML;
}

function initUserCombobox (strComboboxID, strPlaceHolder, oMemberData)
{
	$(strComboboxID).combobox
	({
		valueField:'m_nUserId',
	    textField:'m_strUserName',
	    loader: getUserList,
		mode: 'remote',
		selectOnNavigation: false,
	    formatter: function(row)
	    {
			var opts = $(this).combobox('options');
			return row[opts.textField];
	    },
	    onSelect: function()
    	{
    		$(strComboboxID).combobox('textbox').focus();	
    		$(strComboboxID).combobox('textbox').bind('keydown', oMemberData);
    	}
	});
	var UserTextBox = $(strComboboxID).combobox('textbox');
	UserTextBox[0].placeholder = strPlaceHolder;
}

var getUserList = function(param, success, error)
{
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oUserInformationData = new UserInformationData ();
		oUserInformationData.m_strUserName = strQuery;
		oUserInformationData.m_nUserId = m_oZenithMemberData.m_nUserId;
		oUserInformationData.m_nUID = m_oZenithMemberData.m_nUID;
		UserInformationDataProcessor.getUserSuggesstions(oUserInformationData, "", "", function(oResponse)
				{
					var arrUserData = new Array();
					for(var nIndex=0; nIndex< oResponse.m_arrUserInformationData.length; nIndex++)
						arrUserData.push(oResponse.m_arrUserInformationData[nIndex]);
					success(arrUserData);
				});
	}
	else
		success(new Array ());
}

function initArticleNumberCombobox (strComboboxID, oMemberData)
{
	$(strComboboxID).combobox
	({
		valueField:'m_strArticleNumber',
	    textField:'m_strArticleNumber',
	    loader: getArticleNumberList,
		mode: 'remote',
		selectOnNavigation: false,
		onSelect: function()
    	{
    		$(strComboboxID).combobox('textbox').focus();	
    		$(strComboboxID).combobox('textbox').bind('keydown', oMemberData);
    	}
	});
	var ArticleTextBox = $(strComboboxID).combobox('textbox');
	ArticleTextBox[0].placeholder = "Article Number";
}

var getArticleNumberList = function(param, success, error)
{
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oItemData = new ItemData ();
		oItemData.m_strArticleNumber = strQuery;
		oItemData.m_oUserCredentialsData.m_nUserId = m_oZenithMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oZenithMemberData.m_nUID;
		ItemDataProcessor.getArticleSuggesstions(oItemData, "", "", function(oItemDataResponse)
				{
					var arrItemData = new Array();
					for(var nIndex=0; nIndex< oItemDataResponse.m_arrItems.length; nIndex++)
				    {
						arrItemData.push(oItemDataResponse.m_arrItems[nIndex]);
				    }
					success(arrItemData);
				});
	}
	else
		success(new Array ());
}

function disable (strElementId)
{
	document.getElementById (strElementId).disabled = true;
}

function enable (strElementId)
{
	document.getElementById (strElementId).disabled = false;
}

function getOrderedLineItems (arrLineItems)
{
	for(var nItemIndex = 0; nItemIndex < arrLineItems.length; nItemIndex++)
	{
		for(var nIndex = nItemIndex+1; nIndex < (arrLineItems.length); nIndex++)
		{
			if(arrLineItems[nItemIndex].m_nSerialNumber  > arrLineItems[nIndex].m_nSerialNumber)
			{
				var oLineItemData = arrLineItems[nItemIndex];
				arrLineItems[nItemIndex] = arrLineItems[nIndex];
				arrLineItems[nIndex] = oLineItemData;
			}
		}
	}
	return arrLineItems;
}

function loadDWRHeaders ()
{
	for (var nIndex = 0; nIndex < garrDWRHeaders.length; nIndex++)
		loadWidget (garrDWRHeaders[nIndex]);
}

function logoutOnlineUser ()
{
	document.cookie ="OnlineUser=";
	document.cookie ="OnlineUserId=";
	document.cookie ="OnlineUserPassword=";
	m_oZenithMemberData.m_nUserId = -1;
	document.getElementById("index_td_userName").innerHTML = onlineApp_showSignIn ();
	document.getElementById("index_div_rightMenuBar").innerHTML = "";
	$('#verticalSplitter').jqxSplitter({
		panels: [
		           { size: "100%"},
		           { size: '0%'}]
		});
	navigate('onlineApp', 'widgets/onlinemanagement/onlineApp.js');
}

function initHorizontalSplitter (strHorizontalSplitterId, strDataGridId)
{
	initSplitter (strHorizontalSplitterId);
    $(strHorizontalSplitterId).on('resize', function (event) {
    	$(strDataGridId).datagrid('resize');
    });
    
    $('#verticalSplitter').on('resize', function (event) {
    	$(strDataGridId).datagrid('resize');
    });
    
    $(window).unbind ('resize');
    $(window).resize(function()
			{
		    	$(strHorizontalSplitterId).jqxSplitter({
		    		height: this.innerHeight - getOffsetHeight(),
		            });
		    	$(strDataGridId).datagrid('resize');
			});
}

function initHorizontalSplitterWithTabs (strHorizontalSplitterId, strPanelId)
{
	initSplitter (strHorizontalSplitterId);
    $(strHorizontalSplitterId).on('resize', function (event) {
    	$(strPanelId).tabs('resize');
    });
    
    $('#verticalSplitter').on('resize', function (event) {
    	$(strPanelId).tabs('resize');
    });
    
    $(window).unbind ('resize');
    $(window).resize(function()
			{
		    	$(strHorizontalSplitterId).jqxSplitter({
		    		height: this.innerHeight - getOffsetHeight(),
		            });
		    	$(strPanelId).tabs('resize');
			});
}

function initPanelWithoutSplitter (strPanelId, strDataGridId)
{
    $(strPanelId).jqxPanel({ width: '100%', height: this.innerHeight - getOffsetHeight(), autoUpdate: true, scrollBarSize:10});
    $('#verticalSplitter').on('resize', function (event) {
    	$(strDataGridId).datagrid('resize');
    });
    $(window).unbind ('resize');
    $(window).resize(function()
			{
		    	$(strPanelId).jqxPanel({
		    		height: this.innerHeight - getOffsetHeight()
		            });
		    	$(strDataGridId).datagrid('resize');
			});
}

function getOffsetHeight ()
{
	return getStyleHeight ("ZenithLogo") + getStyleHeight ("footer") + 25;
}

function initSplitter (strSplitterId)
{
	$(strSplitterId).jqxSplitter({
        width: '100%',
        height: this.innerHeight - getOffsetHeight(),
        orientation: 'horizontal',
        panels: [
           { size: "50%", min: "20%", collapsible: false },
           { size: '50%', min: "20%"}]
    });
}

function getDateFromDays (nNoOfDays) // To get previous date, send negative value to parameter 'nNoOfDays'.
{
	var strDate = "";
	if(isFinite(nNoOfDays))
	{
		var oCurrentDate = new Date ();
		var nPreviousDate = oCurrentDate.getTime() + (1000*60*60*24*nNoOfDays);
		oCurrentDate.setTime(nPreviousDate);
		var nMonth = oCurrentDate.getMonth() + 1 ;
		strDate = oCurrentDate.getDate() +'-'+ nMonth +'-'+ oCurrentDate.getFullYear();
	}
	return strDate;
}

function initOnlineSplitter ()
{
	$('#verticalSplitter').jqxSplitter({
        width: '100%',
        height: this.innerHeight,
        orientation: 'vertical',
        panels: [
           { size: "85%", min: "65%", collapsible: false},
           { size: '15%', min: "5%", collapsible: true}]
    });
}

function validateImageFile(file, id) 
{
    var sFileName = file[0].name;
    var sFileExtension = sFileName.split('.')[sFileName.split('.').length - 1].toLowerCase();
    var iFileSize = file[0].size;
    var iConvert = ((file[0].size / 10485760).toFixed(2))*10;
    if (!(sFileExtension === "png" || sFileExtension === "jpeg" || sFileExtension === "gif" || sFileExtension === "jpg") || iConvert > 2) 
    {
	    txt="File type:" + 	sFileName + "\n\n";
	    txt+="Size:" + iConvert + "MB\n\n";
	    txt+="Please make sure that your file is in jpeg/jpg/png/gif format and size is less than 2MB";
	    alert(txt);
	    $(id).val('');
    }
}

function ajaxCall(oData, strURL, callback)
{
	var strTenantName = document.cookie.split("=")[1];
	$.ajax({
	    url: m_strLocationURL + strURL,
	    beforeSend: 
	    	function(xhr)
	    	{
	    		xhr.setRequestHeader('zenithTenantName', strTenantName);
	    		xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
	    	},
	    data: JSON.stringify(oData),
	    dataType: "json",
	    type: "POST",
	    async:false,
	    contentType: "application/json",
	    success: function(oResponse){
			fn = eval(callback);
			fn(oResponse);
		    },
	    error:function(xhr, textStatus, errorThrown){
	    	console.log(xhr );
	    	console.log(textStatus);
	    	console.log(errorThrown);
	    	}
	    });
}

function multipartAjaxCall(oData, strURL, callback)
{
	$.ajax({
	    url: m_strLocationURL + strURL,
	    data:oData ,
	    enctype: 'multipart/form-data',
	    type:"POST",
	    async:false,
	    processData: false,
	    contentType: false,
	    cache: false,	    
	    success: function(oResponse){
			fn = eval(callback);
			fn(oResponse);
		    },
	    error:function(xhr, textStatus, errorThrown){
	    	console.log(xhr );
	    	console.log(textStatus);
	    	console.log(errorThrown);
	    	showError(xhr);
	    	}
	    });
}

function ajaxXMLCall(oData, strURL, callback)
{
	var strTenantName = document.cookie.split("=")[1];
	$.ajax({
	    url: m_strLocationURL + strURL,
	    beforeSend: 
	    	function(xhr)
	    	{
	    		xhr.setRequestHeader('zenithTenantName', strTenantName);
	    		xhr.setRequestHeader("Content-Encoding", "gzip, deflate");
	    	},
	    data: JSON.stringify(oData),
	    dataType: "text",
	    type: "POST",
	    async:false,
	    contentType: "application/json",
	    success: function(oResponse){
			fn = eval(callback);
			fn(oResponse);
		    },
	    error:function(xhr, textStatus, errorThrown){
	    	console.log(xhr );
	    	console.log(textStatus);
	    	console.log(errorThrown);
	    	}
	    });
}

