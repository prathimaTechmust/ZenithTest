var quotation_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/quotationmanagement/quotation/QuotationData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/quotationmanagement/quotation/QuotationLineItemData.js',
	'widgets/quotationmanagement/quotation/QuotationAttachmentData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderData.js'
];

 includeDataObjects (quotation_includeDataObjects, "quotation_loaded ()");

function quotation_memberData ()
{
	this.m_nEditIndex = undefined;
	this.m_arrArticleDetails = new Array ();
	this.m_arrToDetails = new Array();
	this.m_strXMLData = "";
//	this.m_oQuotationData = new QuotationData ();
	this.m_nSalesId = -1;
	this.m_bIsForSite = true;
	this.m_arrPriceDetail = new Array ();
	this.m_arrAttachment = new Array ();
	this.m_arrUniqueTaxNames = new Array();
	this.m_bIsOutOfStateClient = false;
	this.m_nSelectedClientId = -1;
	this.m_strClientArticleNumber = "";
	this.m_bRoleAdmin = false;
}
var m_oQuotationMemberData = new quotation_memberData ();

function quotation_initAdmin ()
{
	quotation_new ();
	quotation_updateVisibility ();
}

function quotation_updateVisibility ()
{
	document.getElementById ("quotation_img_add").style.visibility="visible";
	document.getElementById("quotation_img_addSite").setAttribute ("enable", "true");
	document.getElementById("quotation_img_addContact").setAttribute ("enable", "true");
	document.getElementById ("quotation_img_addItem").style.visibility="visible";
	m_oQuotationMemberData.m_bRoleAdmin = true;
}

function quotation_initUser ()
{
	quotation_new ();
}

function quotation_new ()
{
	createPopup("dialog", "#quotation_button_save", "#quotation_button_cancel", true);
	quotation_init ();
	quotation_appendRow ();
}

function quotation_getUniqueTaxNames()
{
	TaxDataProcessor.getUniqueTaxNames ( quotation_gotUniqueNames );
}

function quotation_gotUniqueNames (oResponse)
{
	m_oQuotationMemberData.m_arrUniqueTaxNames = oResponse.m_arrTax;
}

function quotation_edit ()
{
	createPopup('ProcessDialog', '', '', true);
	createPopup ("dialog", "#quotation_button_save", "#quotation_button_cancel", true);
	quotation_init ();
	quotation_updateVisibility ();
	var oQuotationData = new QuotationData ();
	oQuotationData.m_nId = m_oQuotationMemberData.m_nQuotationId;
	oQuotationData.m_nQuotationId = m_oQuotationListMemberData.m_nSelectedId;
	oQuotationData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oQuotationData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	document.getElementById("quotation_button_save").setAttribute('update', true);
	document.getElementById("quotation_button_save").innerHTML = "Update";
	document.getElementById("quotation_button_saveAndPrint").setAttribute('update', true);
	document.getElementById("quotation_button_saveAndPrint").innerHTML = "Update & Print";
	document.getElementById("quotation_button_saveAndSendMail").setAttribute('update', true);
	document.getElementById("quotation_button_saveAndSendMail").innerHTML = "Update & Send Mail";
	QuotationDataProcessor.get (oQuotationData, quotation_gotData);
}

function quotation_gotData (oResponse)
{
	HideDialog ("ProcessDialog");
	var oQuotationData = oResponse.m_arrQuotations[0];
	var m_oQuotationData = new QuotationData ();
	quotation_populateToCombobox(oQuotationData);	
	m_oQuotationMemberData.m_oQuotationData =  oResponse.m_arrQuotations[0];
	quotation_setClientInfo(oQuotationData.m_oClientData);
	$('#quotation_input_to').combobox('select', oQuotationData.m_oClientData.m_strCompanyName);	
	$('#quotation_input_siteName').combobox('select', oQuotationData.m_oSiteData.m_nSiteId);	
	$("#quotation_textarea_terms").val(oQuotationData.m_strTermsAndCondition);
	if(oQuotationData.m_oContactData != null)
		$('#quotation_input_contactName').combobox('select', oQuotationData.m_oContactData.m_nContactId);
//	$('#quotation_input_date').datebox('setValue', oQuotationData.m_strDate);
	$('#quotation_input_date').datepicker('setDate', oQuotationData.m_strDate);
	$("#quotation_input_quotationNumber").val(oQuotationData.m_strQuotationNumber);
	$('#quotation_table_quotationAttachmentsDG').datagrid('loadData', oQuotationData.m_oQuotationAttachmentData);
	document.getElementById("quotation_input_CFormProvided").checked = oQuotationData.m_bIsCFormProvided;
	initFormValidateBoxes ("quotation_form_id");
	var arrOrderedLineItem = getOrderedLineItems (oQuotationData.m_oQuotationLineItems);
	for (var nIndex = 0; nIndex < arrOrderedLineItem.length; nIndex++)
	{
		try
		{
			arrOrderedLineItem[nIndex].m_strArticleNumber = arrOrderedLineItem[nIndex].m_oItemData.m_strArticleNumber;
		}catch(oException){}
		arrOrderedLineItem[nIndex].m_nQuantity = Number(arrOrderedLineItem[nIndex].m_nQuantity).toFixed(2);
		arrOrderedLineItem[nIndex].m_nPrice = Number(arrOrderedLineItem[nIndex].m_nPrice).toFixed(2);
		var nDiscountPrice = (arrOrderedLineItem[nIndex].m_nPrice - (arrOrderedLineItem[nIndex].m_nPrice * (arrOrderedLineItem[nIndex].m_nDiscount/100))).toFixed(2);
		arrOrderedLineItem[nIndex].m_nDiscountPrice = Math.round(nDiscountPrice)
		arrOrderedLineItem[nIndex].m_nAmount =  (arrOrderedLineItem[nIndex].m_nDiscountPrice * arrOrderedLineItem[nIndex].m_nQuantity).toFixed(2);
		arrOrderedLineItem[nIndex].m_nLineItemId = arrOrderedLineItem[nIndex].m_nLineItemId;
		arrOrderedLineItem[nIndex].m_nItemId = arrOrderedLineItem[nIndex].m_oItemData.m_nItemId;
		$('#quotation_table_quotationDG').datagrid('appendRow',arrOrderedLineItem[nIndex]);
	}
	quotation_loadFooterDG (true);
	quotation_appendRow ();
}

function quotation_populateToCombobox(oQuotationData)
{
	assert.isObject(oQuotationData, 'oQuotationData is expected to be an object');
	assert( Object.keys(oQuotationData).length >0 , "oQuotationData cannot be an empty .");// checks for non emptyness 
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = oQuotationData.m_oClientData.m_strCompanyName;
	ClientDataProcessor.getClientSuggesstions (oClientData, "", "", 1, 10, quotation_gotClientSuggesstions );
}	

function quotation_gotClientSuggesstions(oResponse)
{
	var arrClientInfo = new Array ();
	for(var nIndex=0; nIndex< oResponse.m_arrClientData.length; nIndex++)
	{
		arrClientInfo.push(oResponse.m_arrClientData[nIndex]);
		arrClientInfo[nIndex].m_strTo = encodeURIComponent(oResponse.m_arrClientData[nIndex].m_strCompanyName + " "+"|" +" "+oResponse.m_arrClientData[nIndex].m_strTinNumber);
	}
	$('#quotation_input_to').combobox('loadData',arrClientInfo)
}

function quotation_init ()
{
	initFormValidateBoxes ("quotation_form_id");
//	$( "#quotation_input_date" ).datebox({required:true});
	$( "#quotation_input_date" ).datepicker();
	var dDate = new Date();
	var dCurrentDate = (dDate.getMonth()+1) +'/'+  dDate.getDate() +'/'+ dDate.getFullYear();
//	$('#quotation_input_date').datebox('setValue', dCurrentDate);
	$('#quotation_input_date').datepicker('setDate', dCurrentDate);
	quotation_initToCombobox ();
	quotation_getUniqueTaxNames ();
	quotation_initializeDataGrid ();
	quotation_initAttachmentDG ();
}

function quotation_cancel ()
{
	HideDialog ("dialog");
}

function clientInfo_handleAfterSave ()
{
	// Handler Function for Client Save.
}

function quotation_initToCombobox ()
{
	$('#quotation_input_to').combobox
	({
		valueField:'m_nClientId',
	    textField:'m_strTo',
	    selectOnNavigation: false,
	    loader: getFilteredClientData,
		mode: 'remote',
	    formatter:function(row)
    	{
        	var opts = $(this).combobox('options');
        	return decodeURIComponent(row[opts.textField]);
    	},
		onSelect:function(row)
	    {
    		quotation_updateDGTaxBasedOnCForm ();
    		m_oQuotationMemberData.m_arrToDetails = row;
			quotation_setClientInfo(m_oQuotationMemberData.m_arrToDetails);
			$('#quotation_input_siteName').combobox('textbox').focus ();
	    },
    	onChange:function(row)
	    {
	    	document.getElementById ("quotation_img_addSite").style.visibility="hidden";
	    	document.getElementById ("quotation_img_addContact").style.visibility="hidden";
	    	document.getElementById("quotation_input_CFormProvided").checked = false;
	    	document.getElementById("quotation_td_CformProvided").style.visibility="hidden";
	    	var nClientId = m_oQuotationMemberData.m_nSelectedClientId > 0 ? m_oQuotationMemberData.m_nSelectedClientId : -1;
	    	var arrArticles = $('#quotation_table_quotationDG').datagrid('getRows');
	    	if(arrArticles.length > 1 && nClientId > 0)
	    		quotation_getItems (nClientId, arrArticles);
	    }
	});
	var toTextBox = $('#quotation_input_to').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Client Name";
	toTextBox.focus ();
	toTextBox.bind('keydown', function (e)
		    {
		      	quotation_handleKeyboardNavigation (e);
		    });
}

function quotation_getItems (nClientId, arrArticles)
{
	assert.isArray(arrArticles, "arrArticles is expected to be a type of Array");
	for(var nIndex = 0; nIndex< arrArticles.length; nIndex++)
	{
		if(arrArticles[nIndex].m_nItemId > 0)
		{
			var oItemData  = new ItemData ();
			oItemData.m_nItemId = arrArticles[nIndex].m_nItemId;
			ItemDataProcessor.getItemData(oItemData, nClientId, quotation_gotItems);
		}
	}
	quotation_loadFooterDG (true);
}

function quotation_gotItems(oData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	m_oQuotationMemberData.m_oArticleDetails = oData;
	m_oQuotationMemberData.m_nEditIndex = nIndex;
	quotation_getDiscount (oData);
}

function quotation_updateArticleTableRow ( oArticleData, nDGRowIndex)
{
	assert.isObject(oArticleData, "oArticleData expected to be an Object.");
	assert( Object.keys(oArticleData).length >0 , "oArticleData cannot be an empty .");// checks for non emptyness 
	$('#quotation_table_quotationDG').datagrid('updateRow',{
		index: nDGRowIndex,
		row: 
		{
			m_strArticleNumber : oArticleData.m_strArticleNumber,
			m_strArticleDescription : oArticleData.m_strItemName,
			m_strDetail : oArticleData.m_strDetail,
			m_nPrice : oArticleData.m_nSellingPrice
		}
	});
}

var getFilteredClientData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined)
	{
		var strQuery = param.q.trim();
		var oClientData = new ClientData ();
		oClientData.m_strCompanyName = strQuery;
		ClientDataProcessor.getClientSuggesstions (oClientData, "", "", "", "", function(oResponse){
			var arrClientInfo = new Array ();
			for(var nIndex=0; nIndex< oResponse.m_arrClientData.length; nIndex++)
		    {
				arrClientInfo.push(oResponse.m_arrClientData[nIndex]);
				arrClientInfo[nIndex].m_strTo = encodeURIComponent(oResponse.m_arrClientData[nIndex].m_strCompanyName + " "+"|" +" "+
		    											oResponse.m_arrClientData[nIndex].m_strTinNumber);
		    }
			success(arrClientInfo);
		});
	}
	else
		success(new Array ());
}

function quotation_handleKeyboardNavigation (e)
{
	assert.isObject(e, "e expected to be an Object.");
	assert( Object.keys(e).length >0 , "e cannot be an empty .");// checks for non emptyness 
	if(e.keyCode == 13)
	{
		quotation_setClientInfo(m_oQuotationMemberData.m_arrToDetails);
		$('#quotation_input_siteName').combobox('textbox').focus ();
	}
}

function quotation_setClientInfo(oClientData)
{
	assert.isObject(oClientData, 'oClientData is expected to be an object');
	assert( Object.keys(oClientData).length >0 , "oClientData cannot be an empty .");// checks for non emptyness  commented bcoz during 1at run there wont be any data 
	m_oTrademustMemberData.m_nSelectedClientId = oClientData.m_nClientId;
	m_oQuotationMemberData.m_nSelectedClientId = oClientData.m_nClientId;
	$("#quotation_input_to").combobox('setText',oClientData.m_strCompanyName);
	$("#quotation_label_address").text(oClientData.m_strAddress);
	$("#quotation_label_city").text(oClientData.m_strCity + "-" + oClientData.m_strPinCode);
	$("#quotation_label_phoneNumber").val(oClientData.m_strMobileNumber);
	$("#quotation_label_email").text(oClientData.m_strEmail);
	$("#quotation_label_tinNo").text(oClientData.m_strTinNumber);
	$("#quotation_label_vatNo").text(oClientData.m_strVatNumber);
	$("#quotation_label_cstNo").text(oClientData.m_strCSTNumber);
	if(document.getElementById("quotation_img_addSite").getAttribute("enable") == "true")
		document.getElementById ("quotation_img_addSite").style.visibility="visible";
	if(document.getElementById("quotation_img_addContact").getAttribute("enable") == "true")
		document.getElementById ("quotation_img_addContact").style.visibility="visible";
	quotation_initSiteCombobox (quotation_getActiveSites(oClientData.m_oSites));
	quotation_initContactsCombobox(oClientData.m_oContacts);
	quotation_showOrHideCformChkBox (oClientData.m_bOutstationClient);
}

function quotation_getActiveSites (arrSites)
{
	assert.isArray(arrSites, "arrSites is expected to be a type of Array");
	var arrActiveSites = new Array ();
	for(var nIndex = 0; nIndex < arrSites.length; nIndex++)
	{
		if(arrSites[nIndex].m_nSiteStatus == "kActive")
			arrActiveSites.push(arrSites[nIndex]);
	}
	return arrActiveSites;
}

function quotation_showOrHideCformChkBox (bIsOutstationClient)
{
	assert.isBoolean(bIsOutstationClient, 'bIsOutstationClient is expected to be of type boolean');
	m_oQuotationMemberData.m_bIsOutOfStateClient = bIsOutstationClient;
	if(bIsOutstationClient)
		document.getElementById ("quotation_td_CformProvided").style.visibility="visible";
	else
		document.getElementById ("quotation_td_CformProvided").style.visibility="hidden";
}

function quotation_initSiteCombobox (arrClientSites)
{
	assert.isArray(arrClientSites, "arrClientSites expected to be a Array.");
	$('#quotation_input_siteName').combobox
	({
		data:arrClientSites,
		valueField:'m_nSiteId',
	    textField:'m_strSiteName',
	    selectOnNavigation: false,
	    filter:function(q,row)
    	{
    		var opts = $(this).combobox('options');
    		return row[opts.textField].toUpperCase().indexOf(q.trim().toUpperCase()) >= 0;
    	},
    	onSelect:function(row)
	    {
    		$('#quotation_input_contactName').combobox('textbox').focus ();
	    }
	});
}

function quotation_initContactsCombobox(arrClientContacts)
{
	assert.isArray(arrClientContacts, "arrClientContacts expected to be a Array.");
	$('#quotation_input_contactName').combobox
	({
		data:arrClientContacts,
		valueField:'m_nContactId',
	    textField:'m_strContactName',
	    selectOnNavigation: false,
	    filter:function(q,row)
    	{
    		var opts = $(this).combobox('options');
    		return row[opts.textField].toUpperCase().indexOf(q.trim().toUpperCase()) >= 0;
    	},
    	onSelect:function(row)
	    {
    		if(row.m_strEmail != "")
    			document.getElementById ("quotation_button_saveAndSendMail").style.visibility="visible";
    		var oUpdateButton = document.getElementById("quotation_button_save");
    		if(oUpdateButton.getAttribute('update') == "false")
    		{
    			var rowIndex = $('#quotation_table_quotationDG').datagrid('getRows').length-1;
    			var editors = $('#quotation_table_quotationDG').datagrid('getEditors', rowIndex);
    			//var articleNumberEditor = editors[0];
    			//var articleNoTextbox =  $(articleNumberEditor.target).combobox('textbox');
    		    //articleNoTextbox.focus ();
    		}
	    }
	});
}

function quotation_initAttachmentDG ()
{
	$('#quotation_table_quotationAttachmentsDG').datagrid ({
	    columns:[[  
	        {field:'m_strFileName',title:'Description', width:80},
	        {field:'Actions',title:'Action',align:'center',
				formatter:function(value,row,index)
				{
        			return quotation_displayAttachmentDGAction (row, index);
				}
			}
	]]
	});
}

function quotation_displayAttachmentDGAction (nRowId, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="quotation_deleteAttachment ('+nIndex+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function quotation_deleteAttachment (nIndex)
{
	assert.isNumber(nIndex, "nIndex is expected to be of type number");
	$('#quotation_table_quotationAttachmentsDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#quotation_table_quotationAttachmentsDG');
}

function quotation_addItem ()
{
	navigate ("newItem", "widgets/inventorymanagement/item/itemAdmin.js");
}

function quotation_initializeDataGrid ()
{
	$('#quotation_table_quotationDG').datagrid ({
	    columns:[[  
	        {field:'m_strArticleNumber',title:'Article# <img title="Add" src="images/add.PNG" align="right" style="visibility:hidden;" id="quotation_img_addItem" onClick="quotation_addItem ()"/>', width:80,
	        	editor:
	        		{
	        			type:'combobox',
		                options:
		                {
			                valueField:'m_strArticleDetail',
			                textField:'m_strArticleNumber',
			                hasDownArrow: false,
			                panelWidth: 400,
			                selectOnNavigation: false,
			                loader: getFilteredItemData,
			        		mode: 'remote',
			                formatter:function(row)
			            	{
				            	var opts = $(this).combobox('options');
				            	return decodeURIComponent(row[opts.valueField]);
			            	},
				        	onSelect:function(row)
				            {
				        		var opts = $(this).combobox('options');
				        		m_oQuotationMemberData.m_oArticleDetails = row;
				        		var strDecodeDetails = decodeURIComponent (row[opts.valueField]);
				        		m_oQuotationMemberData.m_arrArticleDetails = strDecodeDetails.split(" | ");
				        		quotation_articleDetails();
				        		quotation_getDiscount (row);
				            }
		        	    }
	        		},
	        },
	        {field:'itemCustomizeAction',title:'',align:'center',width:25,
				formatter:function(value,row,index)
				{
        			return quotation_initItemCustomizeAction (row, index);
				}
			},
	        {field:'m_strArticleDescription',title:'Item Name',width:170,align:'left',editor:{'type':'text'}},
	        {field:'m_strDetail',title:'Detail',width:80,editor:{'type':'text'}},
	        {field:'m_nQuantity',title:'Qty',width:60,align:'right',editor:{'type':'numberbox','options':{precision:'2'}}},
	        {field:'m_nPrice',title:'Price',width:80,align:'right',
	        	editor:
	        	{ 
	        		type:'combobox',
	        		options:
		            {
			    	    valueField:'m_strPriceDetail',
			    	    textField:'m_nPrice',
			    	    hasDownArrow: false,
			    	    panelHeight:100,
			    	    panelWidth: 200,
		        		mode: 'remote',
		        		selectOnNavigation: false,
		        		formatter:function(row)
		            	{
			            	var opts = $(this).combobox('options');
			            	return row[opts.valueField];
		            	},
		            	onSelect:function(row)
			            {
			        		var opts = $(this).combobox('options');
			        		m_oQuotationMemberData.m_arrPriceDetail = row[opts.valueField].split(" | ");
			        		quotation_setPriceDetails(m_oQuotationMemberData.m_arrPriceDetail);
			            }
		            }
	        	}
	        },
	        {field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',editor:{'type':'text'}},
	        {field:'m_strTaxName',title:'Tax Name',width:70,align:'right',
	        	editor:
	        	{ 
	        		type:'combobox',
	        		options:
		            {
			        	data:m_oQuotationMemberData.m_arrUniqueTaxNames,
			    	    valueField:'m_strTaxName',
			    	    textField:'m_strTaxName',
			    	    hasDownArrow: false,
			    	    panelHeight:100,
			    	    filter:function(q,row)
			        	{
			        		var opts = $(this).combobox('options');
			        		return row[opts.valueField].toUpperCase().indexOf(q.trim().toUpperCase()) >= 0;
			        	}
		            }
	        		
	        	}
	        },
	        {field:'m_nTax',title:'Tax(%)',width:60,align:'right',editor:{'type':'text'}},
	        {field:'m_nDiscountPrice',title:'Disc Price',width:60,align:'right',editor:{'type':'text'},
	        	formatter:function(value,row,index)
	        	{	
	        		var nDiscountPrice = row.m_nDiscountPrice;
	        		try
					{
						if (!isNaN(value))
							nDiscountPrice = nDiscountPrice.toFixed(2);
						else
							return value;
					}
					 catch(oException){}
						return nDiscountPrice;
	        	}
	        },
	        {field:'m_nAmount',title:'Amount',width:80,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}},
	        {field:'m_nItemId',hidden:true,editor:{'type':'text'}},
	        {field:'Actions',title:'Action',align:'center',
				formatter:function(value,row,index)
				{
		        	if(!isNaN(row.m_nDiscountPrice))
	        			return quotation_displayImages (row, index);
	        		else
	        			return quotation_removeAction ();
				}
			},
	    ]]
	    
	});
	
	$('#quotation_table_quotationDG').datagrid
	(
		{
			onClickRow: function (rowIndex, rowData)
			{
				quotation_editRowDG (rowIndex);
			}
		}
	)
}

function quotation_getDiscount (oData)
{
	assert.isObject(oData, 'oData is expected to be an object');
	assert( Object.keys(oData).length >0 , "oData cannot be an empty .");// checks for non emptyness 
	var nClientId = m_oQuotationMemberData.m_nSelectedClientId;
	if(nClientId > 0)
	{
		var oClientData = new ClientData ();
		oClientData.m_nClientId = nClientId;
		var oItemData = new ItemData ();
		oItemData.m_nItemId = oData.m_nItemId;
		DiscountStructureDataProcessor.getDiscount(oClientData, oItemData, quotation_gotDiscount)
	}
}

function quotation_gotDiscount (nDiscount)
{
	assert.isNumber(nDiscount, "nDiscount expected to be a Number.");
	var editors = $('#quotation_table_quotationDG').datagrid('getEditors', m_oQuotationMemberData.m_nEditIndex);
	if(editors.length > 0)
	{
		var discountEditor = editors[5];
		$(discountEditor.target).val(nDiscount);
	}
	else
	{
		var nQuantity = $('#quotation_table_quotationDG').datagrid('getRows')[m_oQuotationMemberData.m_nEditIndex].m_nQuantity;
		$('#quotation_table_quotationDG').datagrid('updateRow',{
			index: m_oQuotationMemberData.m_nEditIndex,
			row: 
			{
				m_nDiscount : nDiscount,
				m_strArticleNumber : m_oQuotationMemberData.m_oArticleDetails.m_strArticleNumber,
				m_strArticleDescription : m_oQuotationMemberData.m_oArticleDetails.m_strItemName,
				m_strDetail : m_oQuotationMemberData.m_oArticleDetails.m_strDetail,
				m_nPrice : m_oQuotationMemberData.m_oArticleDetails.m_nSellingPrice,
				m_nDiscountPrice : m_oQuotationMemberData.m_oArticleDetails.m_nSellingPrice - (m_oQuotationMemberData.m_oArticleDetails.m_nSellingPrice*nDiscount/100),
				m_nAmount : nQuantity * (m_oQuotationMemberData.m_oArticleDetails.m_nSellingPrice - (m_oQuotationMemberData.m_oArticleDetails.m_nSellingPrice*nDiscount/100))
			}
		});
	}
}

var getFilteredItemData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oItemData = new ItemData ();
		oItemData.m_strArticleNumber = strQuery;
		oItemData.m_strItemName = strQuery;
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		var nClientId =m_oQuotationMemberData.m_nSelectedClientId > 0 ?m_oQuotationMemberData.m_nSelectedClientId : -1;
		ItemDataProcessor.getClientArticleSuggesstions(oItemData, nClientId, "", "", function(oItemDataResponse)
				{
					var arrItemData = new Array();
					for(var nIndex=0; nIndex< oItemDataResponse.m_arrItems.length; nIndex++)
				    {
						arrItemData.push(oItemDataResponse.m_arrItems[nIndex]);
						var nTaxWithCFrom = arrItemData[nIndex].m_oTaxWithCForm != null ? arrItemData[nIndex].m_oTaxWithCForm.m_oTaxes[0].m_nPercentage : ""
						arrItemData[nIndex].m_strArticleDetail = encodeURIComponent(arrItemData[nIndex].m_strArticleNumber + " | " +
						arrItemData[nIndex].m_strItemName + " | " +
						arrItemData[nIndex].m_strDetail + " | " +
						arrItemData[nIndex].m_nSellingPrice + " | "+
						arrItemData[nIndex].m_oApplicableTax.m_oTaxes[0].m_strTaxName + " | "+
						arrItemData[nIndex].m_oApplicableTax.m_oTaxes[0].m_nPercentage + " | "+
						nTaxWithCFrom);
				    }
					success(arrItemData);
					quotation_showOrHideDGAction (arrItemData.length);
				});
	}
	else
		success(new Array ());
}

function quotation_showOrHideDGAction (nSuggestedItemsLength)
{
	assert.isNumber(nSuggestedItemsLength, "nSuggestedItemsLength expected to be a Number.");
	if(m_oQuotationMemberData.m_bRoleAdmin && nSuggestedItemsLength <= 1 && m_oQuotationMemberData.m_nSelectedClientId > 0)
		document.getElementById("quotation_td_customizeIcon_"+m_oQuotationMemberData.m_nEditIndex).style.visibility="visible";
	else
		document.getElementById("quotation_td_customizeIcon_"+m_oQuotationMemberData.m_nEditIndex).style.visibility="hidden";
}

function quotation_articleDetails()
{
	var editors = $('#quotation_table_quotationDG').datagrid('getEditors', m_oQuotationMemberData.m_nEditIndex);
	var articleNumberEditor = editors[0];
	var nameEditor = editors[1];
	var detailEditor = editors[2];
	var quantityEditor = editors[3];
	var priceEditor = editors[4];
	var discountEditor = editors[5];
	var taxNameEditor = editors[6];
	var taxPercentEditor = editors[7];
	var discountPriceEditor = editors[8];
    var amountEditor = editors[9];
    var itemIdEditor = editors[10];
	$(articleNumberEditor.target).focus();
	var bIsArticleDuplicate =  quotation_isArticleDuplicate (m_oQuotationMemberData.m_arrArticleDetails[0], m_oQuotationMemberData.m_nEditIndex)
	if(!bIsArticleDuplicate)
	{
		$(articleNumberEditor.target).combobox('setValue',m_oQuotationMemberData.m_arrArticleDetails[0]);
	    $(nameEditor.target).val(m_oQuotationMemberData.m_arrArticleDetails[1]);
		$(detailEditor.target).val(m_oQuotationMemberData.m_arrArticleDetails[2]);
		$(priceEditor.target).combobox('setValue',m_oQuotationMemberData.m_arrArticleDetails[3]);
		//$(taxNameEditor.target).combobox('setValue',m_oQuotationMemberData.m_arrArticleDetails[4]);
		$(taxNameEditor.target).combobox('setValue',m_oQuotationMemberData.m_bIsOutOfStateClient ? "CST" : m_oQuotationMemberData.m_arrArticleDetails[4]);
		quotation_setTaxOnCFormProvided (taxPercentEditor);
		$(discountEditor.target).val(0);
		$(quantityEditor.target).numberbox('enable');
		$(quantityEditor.target).numberbox('setValue','1');
		$(discountPriceEditor.target).val('');
		$(amountEditor.target).numberbox('setValue','');
		$(quantityEditor.target).focus();
		$(itemIdEditor.target).val(m_oQuotationMemberData.m_oArticleDetails.m_nItemId);
	}
	else
	{
		$(articleNumberEditor.target).combobox('setValue','');
		$(nameEditor.target).val('');
		$(detailEditor.target).val('');
		$(quantityEditor.target).numberbox('disable');
		$(quantityEditor.target).numberbox('setValue','');
		$(priceEditor.target).combobox('setValue','');
		$(discountEditor.target).val('');
		$(taxNameEditor.target).val('');
		$(taxPercentEditor.target).val('');
		$(discountPriceEditor.target).val('');
		$(amountEditor.target).numberbox('setValue','');
		$(articleNumberEditor.target).combobox('textbox').focus();
		$(itemIdEditor.target).val('');
	}
}

function quotation_setTaxOnCFormProvided (oTaxPercentEditor)
{
	assert.isObject(oTaxPercentEditor, "oTaxPercentEditor expected to be an Object.");
	if(quotation_isValidClient () && document.getElementById("quotation_input_CFormProvided").checked)
		$(oTaxPercentEditor.target).val(m_oQuotationMemberData.m_arrArticleDetails[6]);
	else
		$(oTaxPercentEditor.target).val(m_oQuotationMemberData.m_arrArticleDetails[5]);
}

function quotation_updateDGTaxBasedOnCForm ()
{
	quotation_acceptDGchanges();
	var arrArticles = $('#quotation_table_quotationDG').datagrid('getRows');
	for(var nIndex = 0; nIndex< arrArticles.length; nIndex++)
	{
		var nItemId = arrArticles[nIndex].m_nItemId;
		if(nItemId > 0)
			quotation_updateRowTaxField (nItemId, nIndex);
	}
}

function quotation_updateRowTaxField (nItemId, nIndex)
{
	assert.isString(nItemId, "nItemId expected to be a String.");
	var oItemData = new ItemData ();
	oItemData.m_nItemId = nItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.list(oItemData, "", "", 1, 10, quotation_gotlist);
			
}

function quotation_gotlist (oItemDataResponse) 
{
	quotation_gotItemListForTax(oItemDataResponse, nIndex);
}

function quotation_gotItemListForTax (oItemDataResponse, nDGRowIndex)
{
	var nTaxPercent = 0;
	var oItemData = oItemDataResponse.m_arrItems.length > 0 ? oItemDataResponse.m_arrItems[0] : null;
	var strTaxName = m_oQuotationMemberData.m_bIsOutOfStateClient ? "CST" : (oItemData != null ? oItemDataResponse.m_arrItems[0].m_oApplicableTax.m_oTaxes[0].m_strTaxName : "");
	if(document.getElementById("quotation_input_CFormProvided").checked)
		nTaxPercent = oItemData != null && oItemData.m_oTaxWithCForm != null ? oItemData.m_oTaxWithCForm.m_oTaxes[0].m_nPercentage : "";
	else
		nTaxPercent = oItemData != null ? oItemData.m_oApplicableTax.m_oTaxes[0].m_nPercentage : "";
	$('#quotation_table_quotationDG').datagrid('updateRow',{
		index: nDGRowIndex,
		row: 
		{
			m_nTax : nTaxPercent,
			m_strTaxName : strTaxName
		}
	});
}

function quotation_setPriceDetails (arrPriceDetails)
{
	assert.isArray(arrPriceDetails, "arrPriceDetails expected to be an Array.");
	assert(arrPriceDetails.length >=2, "arrPriceDetails array's length should be greater than 2.");
	var editors = $('#quotation_table_quotationDG').datagrid('getEditors', m_oQuotationMemberData.m_nEditIndex);
	var priceEditor = editors[4];
	var discountEditor = editors[5];
	if(arrPriceDetails.length > 2)
	{
		$(priceEditor.target).combobox('setValue', arrPriceDetails[2].split(":")[1].trim());
		$(discountEditor.target).val(arrPriceDetails[3].split(":")[1].trim());
		$(discountEditor.target).select();
	}
}

function quotation_displayImages (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td> <img title="Delete" src="images/delete.png" width="20"  id="deleteImageId" onClick="quotation_delete('+nIndex+')"/> </td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function quotation_removeAction (oRow, nIndex)
{
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td style="border-style:none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function quotation_initItemCustomizeAction (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td style="border-style:none"> <img title="customize" id="quotation_td_customizeIcon_'+nIndex+'" src="images/customize.png" width="20"  style="visibility:hidden" onClick="quotation_customizeArticleNumber('+nIndex+')"/> </td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function quotation_customizeArticleNumber (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	quotation_editRowDG (nIndex);
	var oEditors = $('#quotation_table_quotationDG').datagrid('getEditors', nIndex);
	var articleNumberEditor = oEditors[0];
	m_oQuotationMemberData.m_strClientArticleNumber = $(articleNumberEditor.target).combobox('getValue');
	m_oQuotationMemberData.m_nSelectedClientId =m_oTrademustMemberData.m_nSelectedClientId;
	if(m_oQuotationMemberData.m_nSelectedClientId > 0 && m_oQuotationMemberData.m_strClientArticleNumber.trim() != "")
		navigate ('articleCustomize','widgets/inventorymanagement/sales/articleCustomizeForQuotation.js');
	else
		m_oQuotationMemberData.m_nSelectedClientId > 0 ? informUser("Please enter a article number!", "kWarning") : informUser("Please Select Client Name!", "kWarning");
}

function quotation_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	$('#quotation_table_quotationDG').datagrid ('deleteRow', nIndex);
	$('#quotation_table_quotationDG').datagrid('acceptChanges');
	refreshDataGrid ('#quotation_table_quotationDG');
	quotation_loadFooterDG (true);
	if ($('#quotation_table_quotationDG').datagrid('getRows').length == 0)
		quotation_appendRow ();
}

function quotation_editRowDG (rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	 if (m_oQuotationMemberData.m_nEditIndex != rowIndex)
	    {
	        if (quotation_endEdit ())
	        {
	            m_oQuotationMemberData.m_nEditIndex = rowIndex;
	            $('#quotation_table_quotationDG').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
	            quotation_setEditing(m_oQuotationMemberData.m_nEditIndex, true);
	        } 
	        else 
	            $('#quotation_table_quotationDG').datagrid('selectRow', m_oQuotationMemberData.m_nEditIndex);
	    }
}

function quotation_endEdit ()
{
    if (m_oQuotationMemberData.m_nEditIndex == undefined)
    	return true
    if ($('#quotation_table_quotationDG').datagrid('validateRow', m_oQuotationMemberData.m_nEditIndex))
    {
        $('#quotation_table_quotationDG').datagrid('endEdit',  m_oQuotationMemberData.m_nEditIndex);
        m_oQuotationMemberData.m_nEditIndex = undefined;
        return true;
    } 
    else 
        return false;
}

function quotation_appendRow ()
{
    if (quotation_endEdit ())
    {	
    	quotation_acceptDGchanges();
    	$('#quotation_table_quotationDG').datagrid('appendRow',{m_strArticleNumber:''});
        m_oQuotationMemberData.m_nEditIndex = $('#quotation_table_quotationDG').datagrid('getRows').length-1;
        $('#quotation_table_quotationDG').datagrid('selectRow',  m_oQuotationMemberData.m_nEditIndex).datagrid('beginEdit',  m_oQuotationMemberData.m_nEditIndex);
        quotation_setEditing(m_oQuotationMemberData.m_nEditIndex, false)
    }
}

function quotation_setEditing(rowIndex, bIsEnabled)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	var oItem = '';
    var editors = $('#quotation_table_quotationDG').datagrid('getEditors', rowIndex);
    var articleNumberEditor = editors[0];
    var nameEditor = editors[1];
    var detailEditor = editors[2];
    var quantityEditor = editors[3];
    var priceEditor = editors[4];
    var discountEditor = editors[5];
    var taxNameEditor = editors[6];
    var taxPercentEditor = editors[7];
    var discountPriceEditor = editors[8];
    var amountEditor = editors[9];
    var articleNoTextbox =  $(articleNumberEditor.target).combobox('textbox');
    articleNoTextbox.focus ();
    var priceTextbox = $(priceEditor.target).combobox('textbox');
    articleNoTextbox.bind('keydown', function (e)
    {
    	quotation_handleKeyboardNavigation (e)
    });
    articleNoTextbox.bind('change', function (e)
    {
    	quotation_articleCheck ()
	});
    priceTextbox.bind('keydown', function (oEvent)
    	    {
    			if(oEvent.keyCode == 13)
    				quotation_setPriceDetails(m_oQuotationMemberData.m_arrPriceDetail);
    	    });
    
    quantityEditor.target.bind('change', function()
    		{
		    	if(quantityEditor.target.val() > 0)
				{
						quotation_calculateAmount(rowIndex);
						$(priceEditor.target).select();
				}
				else
				{
					$(quantityEditor.target).numberbox('setValue','');
					$(quantityEditor.target).focus();
				}
    		});
    quantityEditor.target.bind('blur', function()
    		{
    			if (Number (this.value) == 0)
    				this.focus();
    		});
    priceTextbox.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
		    });
    priceTextbox.bind('change', function ()
    		{
		    	if($(priceEditor.target).combobox('getValue') > 0)
				{
		    		quotation_calculateDiscountAmount(rowIndex, false);
					$(discountEditor.target).select();
				}
				else
				{
					$(priceEditor.target).combobox('setValue','');
					$(priceEditor.target).focus();
				}
		    });
    priceTextbox.bind('blur', function()
    		{
    			if (Number (this.value) == 0)
    				this.focus();
    			else
    				quotation_calculateDiscountAmount(rowIndex, false);
    		});
    discountEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
    			validatePercentage (this);
		    });
    discountEditor.target.bind('blur', function()
    		{
    			priceEditor.target.unbind ('blur');
    			if(this.value == "")
		    		$(discountEditor.target).val(0);
    			else
    				quotation_calculateDiscountAmount(rowIndex, false);
    		});
    discountEditor.target.bind('change', function()
    		{
	    		quotation_calculateDiscountAmount (rowIndex, false);
    		});
		    	
    taxPercentEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
    			validatePercentage (this);
		    });
    taxPercentEditor.target.bind('change', function()
    		{
    			quotation_calculateDiscountAmount (rowIndex, false);
    		});
    taxPercentEditor.target.bind('blur', function()
    		{
    			quotation_calculateDiscountAmount (rowIndex, false);
    		});
    discountPriceEditor.target.bind('keyup', function()
		    { 
		    validateFloatNumber (this);
		    if(parseInt(this.value) > parseInt($(priceEditor.target).combobox('getValue'))) 
		    this.value = ""; 
		    });
    discountPriceEditor.target.bind('blur', function()
    		{
    			quotation_calculateDiscountPercentage(rowIndex, true);
    		});
    discountPriceEditor.target.bind('change', function()
    		{
    			quotation_calculateDiscountPercentage (rowIndex, true);
    		});
    
    function quotation_handleKeyboardNavigation (e)
    {
    	assert.isObject(e, "e expected to be an Object.");
    	assert( Object.keys(e).length >0 , "e cannot be an empty .");// checks for non emptyness 
    	if(e.keyCode == 13)
    		quotation_articleDetails();
    }
    
    function quotation_calculateAmount (rowIndex)
    {
    	var nAmount = '';
    	var nPrice = discountPriceEditor.target.val() >= 0 ? discountPriceEditor.target.val() : $(priceEditor.target).combobox('getValue');
    	nAmount = quantityEditor.target.val() * nPrice;
		if(nAmount >= 0)
			$(amountEditor.target).numberbox('setValue',nAmount);
    }
    
    function quotation_calculateDiscountAmount (rowIndex, bAppendRow)
    {
    	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
    	assert.isBoolean(bAppendRow, "bAppendRow should be a boolean value");
    	var nAmount = '';
    	nAmount = $(priceEditor.target).combobox('getValue') - ($(priceEditor.target).combobox('getValue') * discountEditor.target.val()/100);
		if(nAmount >= 0)
		{
			$(discountPriceEditor.target).val(nAmount);
			quotation_calculateAmount (rowIndex);
			quotation_loadFooterDG (bAppendRow);
			if (bAppendRow && rowIndex ==$('#quotation_table_quotationDG').datagrid('getRows').length-1)
				quotation_appendRow ();
		}
		
    }
    
    function quotation_calculateDiscountPercentage (rowIndex, bAppendRow)
    {
    	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
    	assert.isBoolean(bAppendRow, "bAppendRow should be a boolean value");
    	var nDiscountPercent = '';
    	var nPrice = $(priceEditor.target).combobox('getValue');
    	var nDiscountPrice = $(discountPriceEditor.target).val();
    	nDiscountPercent = ((nPrice - nDiscountPrice)/nPrice)*100;
    	nDiscountPercent = nDiscountPercent.toFixed(2);
    	if(nDiscountPercent >= 0)
		{
			$(discountEditor.target).val(nDiscountPercent);
			quotation_calculateAmount (rowIndex);
			quotation_loadFooterDG (bAppendRow);
			if (bAppendRow && rowIndex ==$('#quotation_table_quotationDG').datagrid('getRows').length-1)
				quotation_appendRow ();
		}
    }
}

function quotation_articleCheck ()
{
	var editors = $('#quotation_table_quotationDG').datagrid('getEditors',m_oQuotationMemberData.m_nEditIndex);
	var articleNoEditor = editors[0];
	$(articleNoEditor.target).combobox('setValue','');
	$(articleNoEditor.target).combobox('textbox').focus();
}

function quotation_nameCheck()
{
	var editors = $('#quotation_table_quotationDG').datagrid('getEditors', m_oQuotationMemberData.m_nEditIndex);
	var nameEditor = editors[1];
	$(nameEditor.target).combobox('setValue','');
	$(nameEditor.target).combobox('textbox').focus();
}

function quotation_isArticleDuplicate (articleNumberEditor, rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	var arrLineItemData = $('#quotation_table_quotationDG').datagrid('getData').rows;
	var bIsArticleDuplicate = false;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		if(articleNumberEditor.toLowerCase() ==  arrLineItemData [nIndex].m_strArticleNumber.toLowerCase() && rowIndex!= nIndex)
		{
			informUser("Duplicate Article in Invoice", "kWarning");
			bIsArticleDuplicate = true;
			break
		}
	}
	return bIsArticleDuplicate;
}

function quotation_acceptDGchanges()
{
	if (quotation_endEdit())
        $('#quotation_table_quotationDG').datagrid('acceptChanges');
}

function quotation_loadFooterDG (bAccept)
{
	assert.isBoolean(bAccept, "bAccept should be a boolean value");
	if(bAccept)
	{
		quotation_acceptDGchanges ();
		var arrLineItemData = $('#quotation_table_quotationDG').datagrid('getData').rows;
		var nTotal = 0;
		for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
		{
			if(Number(arrLineItemData [nIndex].m_nAmount) > 0)
				nTotal = Number(nTotal) + Number(arrLineItemData [nIndex].m_nAmount);
		}
		nTotal = nTotal.toFixed(2);
		$('#quotation_table_quotationDG').datagrid('reloadFooter',[{m_nDiscountPrice:'<b>Total</b>', m_nAmount:'<span class="rupeeSign">R </span>' + nTotal}]);
	}
}

function quotation_validate ()
{
	return validateForm("quotation_form_id") && quotation_validateSelectField ();
}

function quotation_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	if(!quotation_isValidClient())
	{
		informUser("Please Select Client Name from list 0r Create a New Client", "kWarning");
		$('#quotation_input_to').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	else if(!isValidSite())
	{
		informUser("Please Select a Site from list 0r Create a New Site", "kWarning");
		$('#quotation_input_siteName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	else if(!isValidContact())
	{
		informUser("Please Select a Contact from list 0r Create a New Contact", "kWarning");
		$('#quotation_input_contactName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
//	else if(!quotation_articleValidate())
//	{
//		informUser("Please Select the Article", "kWarning");
//		bIsSelectFieldValid = false;
//	}
	return bIsSelectFieldValid;
}

function quotation_isValidClient()
{
	var bIsValid = false;
	try
	{
		var strClient =m_oQuotationMemberData.m_nSelectedClientId;
		if(strClient != null && strClient > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function isValidSite ()
{
	var bIsValid = false;
	try
	{
		var strSite = $('#quotation_input_siteName').combobox('getValue');
		if(strSite != null && strSite > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}
function isValidContact ()
	{
		var bIsValid = false;
		try
		{
			var strContact = $('#quotation_input_contactName').combobox('getValue');
			if(strContact != null && strContact > 0)
				bIsValid = true;
		}
		catch(oException)
		{
			
		}
	return bIsValid;
}
//function quotation_articleValidate ()
//{
//	var bIsValid = false;
//	try
//	{
//		var editors = $('#quotation_table_quotationDG').datagrid('getEditors', m_oquotationMemberData.editIndex);
//		var articleNumberEditor = editors[0];
//		var strArticleNumber = $(articleNumberEditor.target).combobox('getValue');
//		if(strArticleNumber.length > 0)
//			bIsValid = true;
//	}
//	catch(oException)
//	{
//		
//	}
//	return bIsValid;
//}
function quotation_getFormData ()
{
	quotation_acceptDGchanges ();
	var oQuotationData = new QuotationData ();
	oQuotationData.m_oUserCredentialsData = new UserInformationData ();
	oQuotationData.m_oSiteData = new SiteData ();
	oQuotationData.m_oContactData = new ContactData ();
//	oQuotationData.m_strTo = $('#quotation_input_to').combobox('getText');
//	var m_strDate = $('#quotation_input_date').datebox('getValue');
	var m_strDate = $('#quotation_input_date').val();
	oQuotationData.m_strDate = FormatDate (m_strDate);
	oQuotationData.m_strQuotationNumber =$("#quotation_input_quotationNumber").val();
	oQuotationData.m_arrQuotationLineItems = quotation_getLineItemDataArray ();
	oQuotationData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oQuotationData.m_strTermsAndCondition = $("#quotation_textarea_terms").val();
	oQuotationData.m_oClientData.m_nClientId = parseInt(m_oQuotationMemberData.m_nSelectedClientId);
	oQuotationData.m_oSiteData.m_nSiteId = parseInt($('#quotation_input_siteName').combobox('getValue'));
	oQuotationData.m_bIsCFormProvided = document.getElementById("quotation_input_CFormProvided").checked;
	var strContactId = $('#quotation_input_contactName').combobox('getValue');
	if (strContactId > 0)
		oQuotationData.m_oContactData.m_nContactId = parseInt($('#quotation_input_contactName').combobox('getValue'));
	else
		oQuotationData.m_oContactData = null;
	oQuotationData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oQuotationData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oQuotationData.m_arrQuotationAttachments = quotation_getAttachementData ();
	return oQuotationData;
}

function quotation_getAttachementData ()
{
	var arrAttachmentData = new Array ();
	var arrAttachment = $('#quotation_table_quotationAttachmentsDG').datagrid('getData').rows;
	for(nIndex = 0; nIndex < arrAttachment.length; nIndex ++)
	{
		var oQuotationAttachmentData = new QuotationAttachmentData ();
		oQuotationAttachmentData.m_nAttachmentId = arrAttachment[nIndex].m_nAttachmentId;
		oQuotationAttachmentData.m_strFileName = arrAttachment[nIndex].m_strFileName;
		oQuotationAttachmentData.m_oFile = arrAttachment[nIndex].m_oFile;
		arrAttachmentData.push(oQuotationAttachmentData);
	}
	return arrAttachmentData;
}

function quotation_getLineItemDataArray ()
{
	var arrLineItemDataArray = new Array ();
	var arrLineItemData = $('#quotation_table_quotationDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oQuotationLineItemData = new QuotationLineItemData ();
		oQuotationLineItemData.m_nSerialNumber = $('#quotation_table_quotationDG').datagrid('getRowIndex',arrLineItemData[nIndex]);
		oQuotationLineItemData.m_strArticleNumber =  arrLineItemData [nIndex].m_strArticleNumber;
		oQuotationLineItemData.m_strArticleDescription = arrLineItemData [nIndex].m_strArticleDescription;
		oQuotationLineItemData.m_strDetail = arrLineItemData [nIndex].m_strDetail;
		oQuotationLineItemData.m_nQuantity = parseInt(arrLineItemData [nIndex].m_nQuantity);
		oQuotationLineItemData.m_nPrice = parseInt(arrLineItemData [nIndex].m_nPrice);
		oQuotationLineItemData.m_nDiscount = parseInt(arrLineItemData [nIndex].m_nDiscount);
		oQuotationLineItemData.m_nTax = parseInt(arrLineItemData[nIndex].m_nTax);
		oQuotationLineItemData.m_strTaxName = arrLineItemData[nIndex].m_strTaxName != null ? arrLineItemData[nIndex].m_strTaxName : "";
		oQuotationLineItemData.m_nReturnedQuantity = (arrLineItemData[nIndex].m_nReturnedQuantity != undefined && arrLineItemData[nIndex].m_nReturnedQuantity != "") ? arrLineItemData[nIndex].m_nReturnedQuantity : 0;
		if (oQuotationLineItemData.m_nQuantity && oQuotationLineItemData.m_nPrice != 0)
			arrLineItemDataArray.push (oQuotationLineItemData);
	}
	return arrLineItemDataArray;
}

function quotation_submit ()
{
	if (quotation_validate () && quotation_validateDGRow () && quotation_validateArticleDetails())
	{
		loadPage ("include/process.html", "ProcessDialog", "quotation_submit_progressbarLoaded ()");
	}
}

function quotation_submit_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("quotation_button_save");
	var oQuotationData = quotation_getFormData ();
	if(document.getElementById("quotation_button_save").getAttribute('update') == "false")
		QuotationDataProcessor.create (oQuotationData, quotation_created);
	else
	{
		oQuotationData.m_nQuotationId = m_oQuotationListMemberData.m_nSelectedId;
		QuotationDataProcessor.update(oQuotationData, quotation_updated);
	}
}
function quotation_validateDGRow ()
{
	var bIsValidateDGRow = true;
	quotation_acceptDGchanges ();
	var arrLineItemData = $('#quotation_table_quotationDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oQuotationLineItemData = new QuotationLineItemData ();
		var strArticleNumber =  arrLineItemData [nIndex].m_strArticleNumber;
		var nQuantity = arrLineItemData [nIndex].m_nQuantity;
		var nPrice = arrLineItemData [nIndex].m_nPrice;
		if (strArticleNumber.trim() !="" && (nQuantity == "" || nQuantity == 0 || nPrice == "" || nPrice == 0))
		{
			bIsValidateDGRow = false;
			informUser (nQuantity == "" || nQuantity == 0 ? nQuantity == "" ? "Quantity cannot be empty." : "Quantity should be greater than 0." :  nPrice == "" ? "Price cannot be empty." : "Price should be greater than 0." );
			break;
		}
	}
	return bIsValidateDGRow;
}

function quotation_created (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{	
		HideDialog ("dialog");
		informUser ("Quotation saved successfully.", "kSuccess");
		try
		{
			quotation_handleAftersave ();
		}
		catch(oException){}
	}
	else
	{
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Quotation Save Failed", "kAlert");
		enable ("quotation_button_save");
	}
}

function quotation_updated (oResponse)
{
	HideDialog ("ProcessDialog");
	if(oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		informUser("Quotation updated successfully", "kSuccess");
		try
		{
			quotation_handleAftersave ();
		}
		catch(oException){}
	}
	else
	{
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Quotation Updation Failed", "kAlert");
		enable ("quotation_button_save");
	}
}

function quotation_saveAndPrint ()
{
	if (quotation_validate () && quotation_validateDGRow ())
	{
		loadPage ("include/process.html", "ProcessDialog", "quotation_saveAndPrint_progressbarLoaded ()");
	}
}
function quotation_saveAndPrint_progressbarLoaded ()
{	
	createPopup('ProcessDialog', '', '', true);
	disable ("quotation_button_saveAndPrint");
	var oQuotationData = quotation_getFormData ();
	if(document.getElementById("quotation_button_saveAndPrint").getAttribute('update') == "false")
		QuotationDataProcessor.saveAndPrint (oQuotationData, quotation_savedForPrint );
	else
	{
		oQuotationData.m_nQuotationId = m_oQuotationListMemberData.m_nSelectedId;
		QuotationDataProcessor.updateAndPrint(oQuotationData, quotation_savedForPrint);
	}
}

function quotation_saveAndSendMail ()
{
	if (quotation_validate () && quotation_validateDGRow ())
	{
		loadPage ("include/process.html", "ProcessDialog", "quotation_saveAndSendMail_progressbarLoaded ()");
	}
}
function quotation_saveAndSendMail_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("quotation_button_saveAndSendMail");
	var oQuotationData = quotation_getFormData ();
	if(document.getElementById("quotation_button_saveAndSendMail").getAttribute('update') == "false")
		QuotationDataProcessor.saveAndSendMail (oQuotationData, quotation_savedAndSentMail);
	else
	{
		oQuotationData.m_nQuotationId = m_oQuotationListMemberData.m_nSelectedId;
		QuotationDataProcessor.saveAndSendMail(oQuotationData, quotation_savedAndSentMail);
	}
}
function quotation_savedAndSentMail (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{	
		HideDialog ("dialog");
		informUser ("Quotation saved and sent mail successfully.", "kSuccess");
		try
		{
			quotation_handleAftersave ();
		}
		catch(oException){}
	}
	else
	{
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Quotation Save Failed", "kAlert");
		enable ("quotation_button_save");
	}
}

function quotation_savedForPrint (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		m_oQuotationMemberData.m_strXMLData = oResponse.m_strXMLData;
		navigate ('Print Quotation','widgets/quotationmanagement/quotation/quotationPrint.js');
		try
		{
			quotation_handleAftersave ();
		}
		catch(oException){}
	}
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Quotation Save Failed", "kAlert");
}
function quotation_validateArticleDetails() {
	var bIsValidArticle = false;
	var oQuotationData = new QuotationData();
	oQuotationData.m_arrQuotationLineItem =quotation_getLineItemDataArray();
	if (oQuotationData.m_arrQuotationLineItem.length > 0) {
		bIsValidArticle = true;
	} else
		informUser(" Article Details cannot be empty ", "kWarning");
	return bIsValidArticle;
}


function quotation_closePrint ()
{
	quotation_cancel ();
}

function quotation_print ()
{
	document.getElementById("secondDialog").style.top = "0";
	document.getElementById("secondDialog").style.bottom = "0";
	document.getElementById("secondDialog").style.left = "0";
	document.getElementById("secondDialog").style.right = "0";
	window.print();
}

function quotation_addClient ()
{
	navigate ("clientInfo", "widgets/clientmanagement/addClient.js");
}

function quotation_addClientSites ()
{
	m_oQuotationMemberData.m_bIsForSite = true;
	navigate ("addSites", "widgets/clientmanagement/addSites.js");
}

function quotation_addClientContacts ()
{
	m_oQuotationMemberData.m_bIsForSite = false;
	navigate ("addContacts", "widgets/clientmanagement/addContacts.js");
}

function clientInfo_handleAfterUpdate ()
{ 
	quotation_getClientInfo ();
}

function quotation_getClientInfo ()
{
	var oClientData = new ClientData ();
	oClientData.m_nClientId =m_oQuotationMemberData.m_nSelectedClientId;
	ClientDataProcessor.get (oClientData, quotation_gotClientInfo);
}

function quotation_gotClientInfo(oResponse)
{
	if(oResponse.m_arrClientData.length > 0)
	{
		if(m_oQuotationMemberData.m_bIsForSite)
			quotation_initSiteCombobox (quotation_getActiveSites(oResponse.m_arrClientData[0].m_oSites));
		else
			quotation_initContactsCombobox(oResponse.m_arrClientData[0].m_oContacts);
	}
}

function quotation_addAttachments ()
{
	navigate ("New Attachment", "widgets/quotationmanagement/quotation/newQuotationAttachment.js");
}
