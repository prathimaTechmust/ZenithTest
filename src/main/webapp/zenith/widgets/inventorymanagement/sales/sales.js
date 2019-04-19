var sales_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/sales/NonStockSalesLineItemData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/inventorymanagement/sales/CustomizedItemData.js'
];

 includeDataObjects (sales_includeDataObjects, "sales_loaded ()");

function sales_memberData ()
{
	this.editIndex = undefined;
	this.m_arrArticleDetails = new Array ();
	this.m_arrToDetails = new Array();
	this.m_strXMLData = "";
	this.m_oSalesData = new SalesData ();
	this.m_nSalesId = -1;
	this.m_nSelectedClientId = -1;
	this.m_bIsForSite = true;
	this.m_arrPriceDetail = new Array ();
	this.m_bIsForSales = false;
	this.m_strArticleNumber = "";
	this.m_bRoleAdmin = false;
	this.m_nChallanId = -1;
	this.m_nInvoiceId = -1;
}
var m_oSalesMemberData = new sales_memberData ();

function sales_initAdmin ()
{
	sales_new ();
	sales_updateVisibility ();
}

function sales_updateVisibility ()
{
	document.getElementById ("sales_img_add").style.visibility="visible";
	document.getElementById("sales_img_addSite").setAttribute ("enable", "true");
	document.getElementById("sales_img_addContact").setAttribute ("enable", "true");
	document.getElementById ("sales_img_addItem").style.visibility="visible";
	m_oSalesMemberData.m_bRoleAdmin = true;
}

function sales_initUser ()
{
	sales_new ();
}

function sales_new ()
{
	createPopup("dialog", "#sales_button_save", "#sales_button_cancel", true);
	sales_init ();
	if(m_oSalesMemberData.m_bIsForSales)
	{
		document.getElementById ("sales_img_add").style.visibility="hidden";
		sales_setValues ();
	}
	sales_appendRow ();
}

function sales_setValues ()
{
	var oSalesData = m_oSalesMemberData.m_oSalesData;
	sales_populateToCombobox(oSalesData);
	$('#sales_input_to').combobox('select', oSalesData.m_oClientData.m_nClientId);	
	$('#sales_input_siteName').combobox('select', oSalesData.m_oSiteData.m_nSiteId);	
	if(oSalesData.m_oContactData != null)
		$('#sales_input_contactName').combobox('select', oSalesData.m_oContactData.m_nContactId);
	$("#sales_input_invoiceNo").val(oSalesData.m_strInvoiceNo);
//	$('#sales_input_date').datebox('setValue', oSalesData.m_strDate);
	$('#sales_input_date').datepicker('setValue', oSalesData.m_strDate);
	document.getElementById("sales_input_CFormProvided").checked = oSalesData.m_bIsCFormProvided;
//	initFormValidateBoxes ("sales_form_id");
	var arrOrderedLineItem = getOrderedLineItems (oSalesData.m_oQuotationLineItems);
	for (var nIndex = 0; nIndex < arrOrderedLineItem.length; nIndex++)
	{
		arrOrderedLineItem[nIndex].m_strArticleNumber = arrOrderedLineItem[nIndex].m_oItemData != null ?arrOrderedLineItem[nIndex].m_oItemData.m_strArticleNumber : "";
		arrOrderedLineItem[nIndex].m_strName = arrOrderedLineItem[nIndex].m_strArticleDescription;
		arrOrderedLineItem[nIndex].m_strDetail = arrOrderedLineItem[nIndex].m_strDetail;
		arrOrderedLineItem[nIndex].m_nQuantity = Number(arrOrderedLineItem[nIndex].m_nQuantity).toFixed(2);
		arrOrderedLineItem[nIndex].m_nPrice = Number(arrOrderedLineItem[nIndex].m_nPrice).toFixed(2);
		var nDiscountPrice = (arrOrderedLineItem[nIndex].m_nPrice - (arrOrderedLineItem[nIndex].m_nPrice * (arrOrderedLineItem[nIndex].m_nDiscount/100))).toFixed(2);
		arrOrderedLineItem[nIndex].m_nDiscountPrice = Math.round(nDiscountPrice);
		arrOrderedLineItem[nIndex].m_nAmount =  (arrOrderedLineItem[nIndex].m_nDiscountPrice * arrOrderedLineItem[nIndex].m_nQuantity).toFixed(2);
		arrOrderedLineItem[nIndex].m_nLineItemId = arrOrderedLineItem[nIndex].m_nLineItemId;
		$('#sales_table_articles').datagrid('appendRow',arrOrderedLineItem[nIndex]);
	}
	sales_loadFooterDG (true);
}

function sales_edit ()
{
	createPopup('ProcessDialog', '', '', true);
	createPopup ("dialog", "#sales_button_save", "#sales_button_cancel", true);
	sales_init ();
	sales_updateVisibility ();
	var oSalesData = new SalesData ();
	oSalesData.m_nId = m_oSalesMemberData.m_nSalesId;
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	document.getElementById("sales_button_save").setAttribute('update', true);
	document.getElementById("sales_button_save").innerHTML = "Update";
	document.getElementById("sales_button_saveAndPrint").setAttribute('update', true);
	document.getElementById("sales_button_saveAndPrint").innerHTML = "Update & Print";
	document.getElementById("sales_button_saveAndMakeInvoice").setAttribute('update', true);
	document.getElementById("sales_button_saveAndMakeInvoice").innerHTML = "Update & Make Bill";
	document.getElementById("sales_button_saveAndMakeChallan").setAttribute('update', true);
	document.getElementById("sales_button_saveAndMakeChallan").innerHTML = "Update & Make Challan";
	SalesDataProcessor.get (oSalesData, sales_gotData);
}

function sales_gotData (oResponse)
{
	HideDialog ("ProcessDialog");
	var oSalesData = oResponse.m_arrSales[0];
	sales_populateToCombobox(oSalesData);
	m_oSalesMemberData.m_oSalesData =  oResponse.m_arrSales[0];
	sales_setClientInfo(oSalesData.m_oClientData);
	$('#sales_input_to').combobox('select', oSalesData.m_oClientData.m_strCompanyName);
	$('#sales_input_siteName').combobox('select', oSalesData.m_oSiteData.m_nSiteId);	
	if(oSalesData.m_oContactData != null)
		$('#sales_input_contactName').combobox('select', oSalesData.m_oContactData.m_nContactId);
	$("#sales_input_invoiceNo").val(oSalesData.m_strInvoiceNo);
//	$('#sales_input_date').datebox('setValue', oSalesData.m_strDate);
	$('#sales_input_date').datepicker('setDate', oSalesData.m_strDate);
	document.getElementById("sales_input_CFormProvided").checked = oSalesData.m_bIsCFormProvided;
	//initFormValidateBoxes ("sales_form_id");
	var arrOrderedLineItem = getOrderedLineItems (oSalesData.m_oSalesLineItems);
	for (var nIndex = 0; nIndex < arrOrderedLineItem.length; nIndex++)
	{
		arrOrderedLineItem[nIndex].m_strArticleNumber = arrOrderedLineItem[nIndex].m_oItemData.m_strArticleNumber;
		arrOrderedLineItem[nIndex].m_strName = arrOrderedLineItem[nIndex].m_oItemData.m_strItemName;
		arrOrderedLineItem[nIndex].m_strDetail = arrOrderedLineItem[nIndex].m_oItemData.m_strDetail;
		arrOrderedLineItem[nIndex].m_nQuantity = Number(arrOrderedLineItem[nIndex].m_nQuantity).toFixed(2);
		arrOrderedLineItem[nIndex].m_nPrice = Number(arrOrderedLineItem[nIndex].m_nPrice).toFixed(2);
		var nDiscountPrice = (arrOrderedLineItem[nIndex].m_nPrice - (arrOrderedLineItem[nIndex].m_nPrice * (arrOrderedLineItem[nIndex].m_nDiscount/100))).toFixed(2);
		arrOrderedLineItem[nIndex].m_nDiscountPrice = Math.round(nDiscountPrice);
		arrOrderedLineItem[nIndex].m_nAmount =  (arrOrderedLineItem[nIndex].m_nDiscountPrice * arrOrderedLineItem[nIndex].m_nQuantity).toFixed(2);
		arrOrderedLineItem[nIndex].m_nLineItemId = arrOrderedLineItem[nIndex].m_nLineItemId;
		arrOrderedLineItem[nIndex].m_nItemId = arrOrderedLineItem[nIndex].m_oItemData.m_nItemId;
		$('#sales_table_articles').datagrid('appendRow',arrOrderedLineItem[nIndex]);
	}
	sales_loadFooterDG (true);
	sales_appendRow ();
}

function sales_populateToCombobox(oSalesData)
{
	assert.isObject(oSalesData, "oSalesData expected to be an Object.");
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = oSalesData.m_oClientData.m_strCompanyName;
	ClientDataProcessor.getClientSuggesstions (oClientData, "", "", "", "", function(oResponse)
			{
				var arrClientInfo = new Array ();
				for(var nIndex=0; nIndex< oResponse.m_arrClientData.length; nIndex++)
			    {
					arrClientInfo.push(oResponse.m_arrClientData[nIndex]);
					arrClientInfo[nIndex].m_strTo = encodeURIComponent(oResponse.m_arrClientData[nIndex].m_strCompanyName + " "+"|" +" "+
			    											oResponse.m_arrClientData[nIndex].m_strTinNumber);
			    }
				$('#sales_input_to').combobox('loadData',arrClientInfo)
			});
}

function sales_init ()
{
//	initFormValidateBoxes ("sales_form_id");
	// $( "#sales_input_date" ).datebox({required:true});
	$( "#sales_input_date" ).datepicker({minDate:"-90y", maxDate: new Date()});
	var dDate = new Date();
	var dCurrentDate = (dDate.getMonth()+1) +'/'+  dDate.getDate() +'/'+ dDate.getFullYear();
//	$('#sales_input_date').datebox('setValue', dCurrentDate);
	$('#sales_input_date').datepicker('setDate', dCurrentDate);
	sales_initToCombobox ();
	sales_initializeDataGrid ();
}

function sales_cancel ()
{
	HideDialog ("dialog");
}

function clientInfo_handleAfterSave ()
{
	// Handler Function for Client Save.
}

function sales_initToCombobox ()
{
	$('#sales_input_to').combobox
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
    		var oUpdateButton = document.getElementById("sales_button_save");
    		if(row.m_bClientLock && oUpdateButton.getAttribute('update') == "false")
    		{
    			informUser ("This Client is locked!", "");
    			$("#sales_input_to").combobox('setText',"");
    			$('#sales_input_to').combobox('textbox').focus ();
    		}
    		else
    		{
    			sales_updateDGTaxBasedOnCForm ();
	    		m_oSalesMemberData.m_arrToDetails = row;
				sales_setClientInfo(m_oSalesMemberData.m_arrToDetails);
				$('#sales_input_siteName').combobox('textbox').focus ();
    		}
	    }
	});
	var toTextBox = $('#sales_input_to').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Client Name";
	toTextBox.focus ();
	toTextBox.bind('keydown', function (e)
		    {
		      	sales_handleKeyboardNavigation (e);
		    });
}

function sales_getItems (nClientId, arrArticles)
{
	for(var nIndex = 0; nIndex< arrArticles.length; nIndex++)
	{
		if(arrArticles[nIndex].m_nItemId > 0)
		{
			var oItemData  = new ItemData ();
			oItemData.m_nItemId = arrArticles[nIndex].m_nItemId;
			ItemDataProcessor.getItemData(oItemData, nClientId, function(oData)
					{
							m_oSalesMemberData.m_oArticleDetails = oData;
							m_oSalesMemberData.editIndex = nIndex;
							sales_getDiscount (oData);
					});
		}
	}
	sales_loadFooterDG (true);
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
		ClientDataProcessor.getClientSuggesstions (oClientData, "", "", "", "", function(oResponse)
				{
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

function sales_handleKeyboardNavigation (e)
{
	if(e.keyCode == 13)
	{
		sales_setClientInfo(m_oSalesMemberData.m_arrToDetails);
		$('#sales_input_siteName').combobox('textbox').focus ();
	}
}

function sales_setClientInfo(oClientData)
{
	assert.isObject(oClientData, "oClientData expected to be an Object.");
	m_oTrademustMemberData.m_nSelectedClientId = oClientData.m_nClientId;
	m_oSalesMemberData.m_nSelectedClientId = oClientData.m_nClientId;
	$("#sales_input_to").combobox('setText',oClientData.m_strCompanyName);
	$("#sales_label_address").text(oClientData.m_strAddress);
	$("#sales_label_city").text(oClientData.m_strCity + "-" + oClientData.m_strPinCode);
	$("#sales_label_phoneNumber").text(oClientData.m_strMobileNumber);
	$("#sales_label_email").text(oClientData.m_strEmail);
	$("#sales_label_tinNo").text(oClientData.m_strTinNumber);
	$("#sales_label_vatNo").text(oClientData.m_strVatNumber);
	$("#sales_label_cstNo").text(oClientData.m_strCSTNumber);
	if(document.getElementById("sales_img_addSite").getAttribute("enable") == "true")
		document.getElementById ("sales_img_addSite").style.visibility="visible";
	if(document.getElementById("sales_img_addContact").getAttribute("enable") == "true")
		document.getElementById ("sales_img_addContact").style.visibility="visible";
	sales_initSiteCombobox (sales_getActiveSites(oClientData.m_oSites));
	sales_initContactsCombobox(oClientData.m_oContacts);
	sales_showOrHideCformChkBox (oClientData.m_bOutstationClient);
}

function sales_getActiveSites (arrSites)
{
	assert.isArray(arrSites, "arrSites expected to be an Array.");
	var arrActiveSites = new Array ();
	for(var nIndex = 0; nIndex < arrSites.length; nIndex++)
	{
		if(arrSites[nIndex].m_nSiteStatus == "kActive")
			arrActiveSites.push(arrSites[nIndex]);
	}
	return arrActiveSites;
}

function sales_showOrHideCformChkBox (bIsOutstationClient)
{
	assert.isBoolean(bIsOutstationClient, "bIsOutstationClient should be a boolean value");
	if(bIsOutstationClient)
		document.getElementById ("sales_td_CformProvided").style.visibility="visible";
	else
		document.getElementById ("sales_td_CformProvided").style.visibility="hidden";
}

function sales_initSiteCombobox (arrClientSites)
{
	$('#sales_input_siteName').combobox
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
    		$('#sales_input_contactName').combobox('textbox').focus ();
	    }
	});
}

function sales_initContactsCombobox(arrClientContacts)
{
	$('#sales_input_contactName').combobox
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
    		var oUpdateButton = document.getElementById("sales_button_save");
    		if(oUpdateButton.getAttribute('update') == "false")
    		{
    			var rowIndex = $('#sales_table_articles').datagrid('getRows').length-1;
    			var editors = $('#sales_table_articles').datagrid('getEditors', rowIndex);
    			var articleNumberEditor = editors[0];
    			//var articleNoTextbox =  $(articleNumberEditor.target).combobox('textbox');
    		    //articleNoTextbox.focus ();
    		}
	    }
	});
}

function sales_addItem ()
{
	navigate ("newItem", "widgets/inventorymanagement/item/itemAdmin.js");
}

function sales_initializeDataGrid ()
{
	$('#sales_table_articles').datagrid ({
	    columns:[[  
	        {field:'m_strArticleNumber',title:'Article#<img title="Add" src="images/add.PNG" align="right" style="visibility:hidden;" id="sales_img_addItem" onClick="sales_addItem ()"/>', width:100,
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
				        		m_oSalesMemberData.m_oArticleDetails = row;
				        		var strDecodeDetails = decodeURIComponent (row[opts.valueField]);
				        		m_oSalesMemberData.m_arrArticleDetails = strDecodeDetails.split(" | ");
				        		sales_articleDetails();
				        		sales_getDiscount (row);
				            }
		        	    }
	        		},
	        },
	        {field:'itemCustomizeAction',title:'',align:'center',width:25,
				formatter:function(value,row,index)
				{
	        		if(!isNaN(row.m_nDiscountPrice))
	        			return sales_initCustomizeAction (row, index);
				}
			},
	        {field:'m_strName',title:'Name',width:180,
	        	editor:
	        		{
		    			type:'combobox',
		                options:
		                {
			                valueField:'m_strArticleDetail',
			                textField:'m_strName',
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
				        		m_oSalesMemberData.m_oArticleDetails = row;
				        		var strDecodeDetails = decodeURIComponent (row[opts.valueField]);
				        		m_oSalesMemberData.m_arrArticleDetails = strDecodeDetails.split(" | ");
				        		sales_articleDetails();
				            }
		        	    }
	        		}
	        },
	        {field:'m_strDetail',title:'Detail',width:80,editor:{'type':'text'}},
	        {field:'m_nQuantity',title:'Qty',width:60,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}},
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
			    	    loader: getPreviousSales,
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
			        		m_oSalesMemberData.m_arrPriceDetail = row[opts.valueField].split(" | ");
			        		sales_setPriceDetails(m_oSalesMemberData.m_arrPriceDetail);
			            }
		            }
	        	}
	        },
	        {field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',editor:{'type':'text'}},
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
	        {field:'Actions',title:'Action',align:'center',width:30,
				formatter:function(value,row,index)
				{
	        		if(!isNaN(row.m_nDiscountPrice))
	        			return sales_displayImages (row, index);
	        		else
	        			return sales_removeAction ();
				}
			},
	    ]]
	    
	});
	
	$('#sales_table_articles').datagrid
	(
		{
			onClickRow: function (rowIndex, rowData)
			{
				sales_editRowDG (rowIndex);
			}
		}
	)
}

function sales_getDiscount (oData)
{
	assert.isObject(oData, "oData expected to be an Object.");
	var nClientId = m_oSalesMemberData.m_nSelectedClientId;
	if(nClientId > 0)
	{
		var oClientData = new ClientData ();
		oClientData.m_nClientId = nClientId;
		var oItemData = new ItemData ();
		oItemData.m_nItemId = oData.m_nItemId;
		DiscountStructureDataProcessor.getDiscount(oClientData, oItemData, sales_gotDiscount)
	}
}

function sales_gotDiscount (nDiscount)
{
	assert.isNumber(nDiscount, "nDiscount expected to be a Number.");
	assert.isOk(nDiscount > -1, "nDiscount must be a positive value.");
	var editors = $('#sales_table_articles').datagrid('getEditors', m_oSalesMemberData.editIndex);
	if(editors.length > 0)
	{
		var discountEditor = editors[5];
		$(discountEditor.target).val(nDiscount);
	}
	else
	{
		var nQuantity = $('#sales_table_articles').datagrid('getRows')[m_oSalesMemberData.editIndex].m_nQuantity;
		$('#sales_table_articles').datagrid('updateRow',{
			index: m_oSalesMemberData.editIndex,
			row: 
			{
				m_nDiscount : nDiscount,
				m_strArticleNumber : m_oSalesMemberData.m_oArticleDetails.m_strArticleNumber,
				m_strName : m_oSalesMemberData.m_oArticleDetails.m_strItemName,
				m_strDetail : m_oSalesMemberData.m_oArticleDetails.m_strDetail,
				m_nPrice : m_oSalesMemberData.m_oArticleDetails.m_nSellingPrice,
				m_nDiscountPrice : m_oSalesMemberData.m_oArticleDetails.m_nSellingPrice - (m_oSalesMemberData.m_oArticleDetails.m_nSellingPrice*nDiscount/100),
				m_nAmount : nQuantity * (m_oSalesMemberData.m_oArticleDetails.m_nSellingPrice - (m_oSalesMemberData.m_oArticleDetails.m_nSellingPrice*nDiscount/100))
			}
		});
	}
}

var getFilteredItemData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined)
	{
		var strQuery = param.q.trim();
		var oItemData = new ItemData ();
		oItemData.m_strArticleNumber = strQuery;
		oItemData.m_strItemName = strQuery;
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		var nClientId = m_oSalesMemberData.m_nSelectedClientId > 0 ? m_oSalesMemberData.m_nSelectedClientId : -1;
		ItemDataProcessor.getClientArticleSuggesstions(oItemData, nClientId, "", "", function(oItemDataResponse)
				{
					var arrItemData = new Array();
					for(var nIndex=0; nIndex< oItemDataResponse.m_arrItems.length; nIndex++)
				    {
						arrItemData.push(oItemDataResponse.m_arrItems[nIndex]);
						var nTaxWithCFrom = arrItemData[nIndex].m_oTaxWithCForm != null ? arrItemData[nIndex].m_oTaxWithCForm.m_oTaxes[0].m_nPercentage : "";
						arrItemData[nIndex].m_strArticleDetail = encodeURIComponent(arrItemData[nIndex].m_strArticleNumber + " | " +
						arrItemData[nIndex].m_strItemName + " | " +
						arrItemData[nIndex].m_strDetail + " | " +
						arrItemData[nIndex].m_nSellingPrice + " | "+
						arrItemData[nIndex].m_oApplicableTax.m_oTaxes[0].m_nPercentage + " | "+
						nTaxWithCFrom);
				    }
					success(arrItemData);
					sales_showOrHideDGAction (arrItemData.length);
				});
	}
	else
		success(new Array ());
}

var getPreviousSales = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	var nClientId = m_oSalesMemberData.m_nSelectedClientId;
	var editors = $('#sales_table_articles').datagrid('getEditors', m_oSalesMemberData.editIndex);
	var articleNumberEditor = editors[0];
	var strArticleNumber = $(articleNumberEditor.target).combobox('getValue');
	if(param.q != undefined && strArticleNumber.length > 0 && nClientId > 0)
	{
		SalesLineItemDataProcessor.getPreviousSales(strArticleNumber, nClientId, function(oResponse)
				{
					var arrSalesLineItem = new Array ();
					for(var nIndex=0; nIndex< oResponse.m_arrSalesLineItem.length; nIndex++)
				    {
						arrSalesLineItem.push(oResponse.m_arrSalesLineItem[nIndex]);
						arrSalesLineItem[nIndex].m_strPriceDetail = oResponse.m_arrSalesLineItem[nIndex].m_strDate + " | "
						+"Q: "+ oResponse.m_arrSalesLineItem[nIndex].m_nQuantity + " | "
						+"P: "+ oResponse.m_arrSalesLineItem[nIndex].m_nPrice + " | "
						+"D: "+ oResponse.m_arrSalesLineItem[nIndex].m_nDiscount;
					}
					success(arrSalesLineItem);
				})
	}
	else
		success(new Array ());
}

function sales_articleDetails()
{
	var editors = $('#sales_table_articles').datagrid('getEditors', m_oSalesMemberData.editIndex);
	var articleNumberEditor = editors[0];
	var nameEditor = editors[1];
	var detailEditor = editors[2];
	var quantityEditor = editors[3];
	var priceEditor = editors[4];
	var discountEditor = editors[5];
	var taxPercentEditor = editors[6];
	var discountPriceEditor = editors[7];
    var amountEditor = editors[8];
    var itemIdEditor = editors[9];
	$(articleNumberEditor.target).focus();
	var bIsArticleDuplicate =  sales_isArticleDuplicate (m_oSalesMemberData.m_arrArticleDetails[0], m_oSalesMemberData.editIndex)
	if(!bIsArticleDuplicate)
	{
		$(articleNumberEditor.target).combobox('setValue',m_oSalesMemberData.m_arrArticleDetails[0]);
	    $(nameEditor.target).combobox('setValue',m_oSalesMemberData.m_arrArticleDetails[1]);
		$(detailEditor.target).val(m_oSalesMemberData.m_arrArticleDetails[2]);
		$(priceEditor.target).combobox('setValue',m_oSalesMemberData.m_arrArticleDetails[3]);
		sales_setTaxOnCFormProvided ();
		$(discountEditor.target).val(0);
		$(quantityEditor.target).numberbox('enable');
		$(quantityEditor.target).numberbox('setValue','');
		$(discountPriceEditor.target).val('');
		$(amountEditor.target).numberbox('setValue','');
		$(quantityEditor.target).focus();
		$(itemIdEditor.target).val(m_oSalesMemberData.m_oArticleDetails.m_nItemId);
	}
	else
	{
		$(articleNumberEditor.target).combobox('setValue','');
		$(nameEditor.target).combobox('setValue','');
		$(detailEditor.target).val('');
		$(quantityEditor.target).numberbox('disable');
		$(quantityEditor.target).numberbox('setValue','');
		$(priceEditor.target).combobox('setValue','');
		$(discountEditor.target).val('');
		$(taxPercentEditor.target).val('');
		$(discountPriceEditor.target).val('');
		$(amountEditor.target).numberbox('setValue','');
		$(articleNumberEditor.target).combobox('textbox').focus();
		$(itemIdEditor.target).val('');
	}
}

function sales_setTaxOnCFormProvided ()
{
	var editors = $('#sales_table_articles').datagrid('getEditors', m_oSalesMemberData.editIndex);
	var taxPercentEditor = editors[6];
	if(sales_isValidClient () && document.getElementById("sales_input_CFormProvided").checked)
		$(taxPercentEditor.target).val(m_oSalesMemberData.m_arrArticleDetails[5]);
	else
		$(taxPercentEditor.target).val(m_oSalesMemberData.m_arrArticleDetails[4]);
}

function sales_updateDGTaxBasedOnCForm ()
{
	sales_acceptDGchanges();
	var arrArticles = $('#sales_table_articles').datagrid('getRows');
	for(var nIndex = 0; nIndex< arrArticles.length; nIndex++)
	{
		var nItemId = arrArticles[nIndex].m_nItemId;
		if(nItemId > 0)
			sales_updateRowTaxField (nItemId, nIndex);
	}
}

function sales_updateRowTaxField (nItemId, nIndex)
{
	assert.isString(nItemId, "nItemId expected to be a String.");
	var oItemData = new ItemData ();
	oItemData.m_nItemId = nItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.list(oItemData, "", "", function(oItemDataResponse) 
				{
					sales_gotItemListForTax(oItemDataResponse, nIndex);
				});
}

function sales_gotItemListForTax (oItemDataResponse, nDGRowIndex)
{
	var nTaxPercent = 0;
	if(document.getElementById("sales_input_CFormProvided").checked)
		nTaxPercent = oItemDataResponse.m_arrItems.length > 0 && oItemDataResponse.m_arrItems[0].m_oTaxWithCForm != null ? oItemDataResponse.m_arrItems[0].m_oTaxWithCForm.m_oTaxes[0].m_nPercentage : ""
	else
		nTaxPercent = oItemDataResponse.m_arrItems.length > 0 ? oItemDataResponse.m_arrItems[0].m_oApplicableTax.m_oTaxes[0].m_nPercentage : "";
	$('#sales_table_articles').datagrid('updateRow',{
		index: nDGRowIndex,
		row: 
		{
			m_nTax : nTaxPercent
		}
	});
}

function sales_setPriceDetails (arrPriceDetails)
{
	assert.isArray(arrPriceDetails, "arrPriceDetails expected to be an Array.");
	var editors = $('#sales_table_articles').datagrid('getEditors', m_oSalesMemberData.editIndex);
	var priceEditor = editors[4];
	var discountEditor = editors[5];
	if(arrPriceDetails.length > 2)
	{
		$(priceEditor.target).combobox('setValue', arrPriceDetails[2].split(":")[1].trim());
		$(discountEditor.target).val(arrPriceDetails[3].split(":")[1].trim());
		$(discountEditor.target).select();
	}
}

function sales_initCustomizeAction (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td style="border-style:none"> <img title="customize" id="sales_td_customizeIcon_'+nIndex+'" src="images/customize.png" width="20"  style="visibility:hidden" onClick="sales_customizeArticleNumber('+nIndex+')"/> </td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function sales_displayImages (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td> <img title="Delete" src="images/delete.png" width="20"  id="deleteImageId" onClick="sales_delete('+nIndex+')"/> </td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function sales_customizeArticleNumber (nIndex)
{
	sales_editRowDG (nIndex);
	var oEditors = $('#sales_table_articles').datagrid('getEditors', nIndex);
	var articleNumberEditor = oEditors[0];
	m_oSalesMemberData.m_strArticleNumber = $(articleNumberEditor.target).combobox('getValue');
	m_oSalesMemberData.m_nSelectedClientId = m_oTrademustMemberData.m_nSelectedClientId;
	if(m_oSalesMemberData.m_nSelectedClientId > 0 && m_oSalesMemberData.m_strArticleNumber.trim() != "")
		navigate ('articleCustomize','widgets/inventorymanagement/sales/articleCustomizeForSales.js');
	else
		m_oSalesMemberData.m_nSelectedClientId > 0 ? informUser("Please Select choose the article number!", "kWarning") : informUser("Please Select Client Name!", "kWarning");
}

function sales_removeAction (oRow, nIndex)
{
	var oImage ='<table align="center">'+
	'<tr>'+
	'<td></td>'+
	'</tr>'+
	'</table>'
return oImage;
}

function sales_delete (nIndex)
{
	$('#sales_table_articles').datagrid ('deleteRow', nIndex);
	$('#sales_table_articles').datagrid('acceptChanges');
	refreshDataGrid ('#sales_table_articles');
	sales_loadFooterDG (true);
	if ($('#sales_table_articles').datagrid('getRows').length == 0)
		sales_appendRow ();
}

function sales_editRowDG (rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	assert.isOk(rowIndex > -1, "rowIndex must be a positive value.");
	 if (m_oSalesMemberData.editIndex != rowIndex)
	    {
	        if (sales_endEdit ())
	        {
	            m_oSalesMemberData.editIndex = rowIndex;
	            $('#sales_table_articles').datagrid('selectRow', rowIndex)
	                    .datagrid('beginEdit', rowIndex);
	            sales_setEditing(m_oSalesMemberData.editIndex, true);
	        } 
	        else 
	            $('#sales_table_articles').datagrid('selectRow', m_oSalesMemberData.editIndex);
	    }
}

function sales_endEdit ()
{
    if (m_oSalesMemberData.editIndex == undefined)
    	return true
    if ($('#sales_table_articles').datagrid('validateRow', m_oSalesMemberData.editIndex))
    {
        $('#sales_table_articles').datagrid('endEdit',  m_oSalesMemberData.editIndex);
        m_oSalesMemberData.editIndex = undefined;
        return true;
    } 
    else 
        return false;
}

function sales_appendRow ()
{
    if (sales_endEdit ())
    {	
    	sales_acceptDGchanges();
    	$('#sales_table_articles').datagrid('appendRow',{m_strArticleNumber:'', m_nDiscountPrice:''});
        m_oSalesMemberData.editIndex = $('#sales_table_articles').datagrid('getRows').length-1;
        $('#sales_table_articles').datagrid('selectRow',  m_oSalesMemberData.editIndex).datagrid('beginEdit',  m_oSalesMemberData.editIndex);
        sales_setEditing(m_oSalesMemberData.editIndex, false)
    }
}

function sales_showOrHideDGAction (nSuggestedItemsLength)
{
	if(m_oSalesMemberData.m_bRoleAdmin && nSuggestedItemsLength <= 1 && m_oSalesMemberData.m_nSelectedClientId > 0)
		document.getElementById("sales_td_customizeIcon_"+m_oSalesMemberData.editIndex).style.visibility="visible";
	else
		document.getElementById("sales_td_customizeIcon_"+m_oSalesMemberData.editIndex).style.visibility="hidden";
}

function sales_setEditing(rowIndex, bIsEnabled)
{
	assert.isBoolean(bIsEnabled, "bIsEnabled should be a boolean value");
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	assert.isOk(rowIndex > -1, "rowIndex must be a positive value.");
	var oItem = '';
    var editors = $('#sales_table_articles').datagrid('getEditors', rowIndex);
    var articleNumberEditor = editors[0];
    var nameEditor = editors[1];
    var detailEditor = editors[2];
    var quantityEditor = editors[3];
    var priceEditor = editors[4];
    var discountEditor = editors[5];
    var taxPercentEditor = editors[6];
    var discountPriceEditor = editors[7];
    var amountEditor = editors[8];
    var articleNoTextbox =  $(articleNumberEditor.target).combobox('textbox');
    articleNoTextbox.focus ();
    var nameTextbox = $(nameEditor.target).combobox('textbox');
    var priceTextbox = $(priceEditor.target).combobox('textbox');
    
    articleNoTextbox.bind('keydown', function (e)
    {
    	sales_handleKeyboardNavigation (e)
    });
    articleNoTextbox.bind('change', function (e)
    	    {
    	sales_articleCheck (e)
    	    });
    nameTextbox.bind('keydown', function (e)
    {
    	sales_handleKeyboardNavigation (e)
    });
    nameTextbox.bind('change', function (e)
    	    {
    	sales_nameCheck (e)
    	    });
    priceTextbox.bind('keydown', function (oEvent)
    	    {
    			if(oEvent.keyCode == 13)
    				sales_setPriceDetails(m_oSalesMemberData.m_arrPriceDetail);
    	    });
    
    if(bIsEnabled)
    	$(quantityEditor.target).numberbox('enable');
    quantityEditor.target.bind('change', function()
    		{
		    	if(quantityEditor.target.val() > 0)
				{
					sales_calculateAmount(rowIndex);
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
		    		sales_calculateDiscountAmount(rowIndex, false);
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
    				sales_calculateDiscountAmount(rowIndex, false);
    		});
    discountEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
    			validatePercentage (this);
		    });
    discountEditor.target.bind('blur', function()
    		{
    			priceTextbox.unbind ('blur');
    			sales_calculateDiscountAmount(rowIndex, false);
    		});
    discountEditor.target.bind('change', function()
    		{
	    		sales_calculateDiscountAmount (rowIndex, false);
    		});
    taxPercentEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
    			validatePercentage (this);
		    });
    taxPercentEditor.target.bind('change', function()
    		{
    			sales_calculateDiscountAmount (rowIndex, false);
    		});
    taxPercentEditor.target.bind('blur', function()
    		{
    			sales_calculateDiscountAmount (rowIndex, false);
    		});
		
    discountPriceEditor.target.bind('keyup', function()
		    { 
		    validateFloatNumber (this);
		    if(parseInt(this.value) > parseInt($(priceEditor.target).combobox('getValue'))) 
		    this.value = ""; 
		    });
    discountPriceEditor.target.bind('blur', function()
    		{
    			sales_calculateDiscountPercentage(rowIndex, true);
    		});
    discountPriceEditor.target.bind('change', function()
    		{
    			sales_calculateDiscountPercentage (rowIndex, true);
    		});
    
    
    function sales_handleKeyboardNavigation (e)
    {
    	assert.isObject(e, "e expected to be an Object.");
    	assert( Object.keys(e).length >0 , "e cannot be an empty .");// checks for non emptyness 
    	if(e.keyCode == 13)
    		sales_articleDetails();
    }
    
    
    function sales_calculateAmount (rowIndex)
    {
    	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
    	assert.isOk(rowIndex > -1, "rowIndex must be a positive value.");
    	var nAmount = '';
    	var nPrice = discountPriceEditor.target.val() >= 0 ? discountPriceEditor.target.val() : $(priceEditor.target).combobox('getValue');
    	nAmount = quantityEditor.target.val() * nPrice;
		if(nAmount >= 0)
			$(amountEditor.target).numberbox('setValue',nAmount);
    }
    
    function sales_calculateDiscountAmount (rowIndex, bAppendRow)
    {
    	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
    	assert.isOk(rowIndex > -1, "rowIndex must be a positive value.");
    	assert.isBoolean(bAppendRow, "bAppendRow should be a boolean value");
    	var nAmount = '';
    	nAmount = $(priceEditor.target).combobox('getValue') - ($(priceEditor.target).combobox('getValue') * discountEditor.target.val()/100);
		if(nAmount >= 0)
		{
			$(discountPriceEditor.target).val(nAmount);
			sales_calculateAmount (rowIndex);
			sales_loadFooterDG (bAppendRow);
			if (bAppendRow && rowIndex ==$('#sales_table_articles').datagrid('getRows').length-1)
				sales_appendRow ();
		}
    }
    
    function  sales_calculateDiscountPercentage (rowIndex, bAppendRow)
    {
       	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
    	assert(rowIndex !== 0, "rowIndex cannot be equal to zero.");
    	assert.isOk(rowIndex > -1, "rowIndex must be a positive value.");
    	assert.isBoolean(bAppendRow, "bAppendRow should be a boolean value");
    	var nDiscountPercent = '';
    	var nPrice = $(priceEditor.target).combobox('getValue');
    	var nDiscountPrice = $(discountPriceEditor.target).val();
    	nDiscountPercent = ((nPrice - nDiscountPrice)/nPrice)*100;
    	nDiscountPercent = nDiscountPercent.toFixed(2);
    	if(nDiscountPercent >= 0)
		{
			$(discountEditor.target).val(nDiscountPercent);
			sales_calculateAmount (rowIndex);
			sales_loadFooterDG (bAppendRow);
			if (bAppendRow && rowIndex ==$('#sales_table_articles').datagrid('getRows').length-1)
				sales_appendRow ();
		}
    }
}

function sales_articleCheck ()
{
	var editors = $('#sales_table_articles').datagrid('getEditors', m_oSalesMemberData.editIndex);
	var articleNoEditor = editors[0];
	$(articleNoEditor.target).combobox('setValue','');
	$(articleNoEditor.target).combobox('textbox').focus();
}

function sales_nameCheck()
{
	var editors = $('#sales_table_articles').datagrid('getEditors', m_oSalesMemberData.editIndex);
	var nameEditor = editors[1];
	$(nameEditor.target).combobox('setValue','');
	$(nameEditor.target).combobox('textbox').focus();
}

function sales_isArticleDuplicate (articleNumberEditor, rowIndex)
{
	var arrLineItemData = $('#sales_table_articles').datagrid('getData').rows;
	var bIsArticleDuplicate = false;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		if(articleNumberEditor.toLowerCase() ==  arrLineItemData [nIndex].m_strArticleNumber.toLowerCase() && rowIndex!= nIndex)
		{
			informUser("Duplicate Article in Invoice", "kWarning");
			bIsArticleDuplicate = true;
			break;
		}
	}
	return bIsArticleDuplicate;
}

function sales_acceptDGchanges()
{
	if (sales_endEdit())
        $('#sales_table_articles').datagrid('acceptChanges');
}

function sales_loadFooterDG (bAccept)
{
	assert.isBoolean(bAccept, "bAccept should be a boolean value");
	if(bAccept)
	{
		sales_acceptDGchanges ();
		var arrLineItemData = $('#sales_table_articles').datagrid('getData').rows;
		var nTotal = 0;
		for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
		{
			if(Number(arrLineItemData [nIndex].m_nAmount) > 0)
				nTotal = Number(nTotal) + Number(arrLineItemData [nIndex].m_nAmount);
		}
		nTotal = nTotal.toFixed(2);
		$('#sales_table_articles').datagrid('reloadFooter',[{m_nDiscountPrice:'<b>Total</b>', m_nAmount:'<span class="rupeeSign">R </span>' + nTotal}]);
	}
}

function sales_validate ()
{
	return validateForm("sales_form_id") && sales_validateSelectField ();
}

function sales_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	if(!sales_isValidClient())
	{
		informUser("Please Select Client Name", "kWarning");
		$('#sales_input_to').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	else if(!sales_isValidSite())
	{
		informUser("Please Select a Site", "kWarning");
		$('#sales_input_siteName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	else if(!sales_isValidContact())
	{
		informUser("Please Select a Contact from list 0r Create a New Contact", "kWarning");
		$('#sales_input_contactName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
//	else if(!sales_articleValidate())
//	{
//		informUser("Please Select the Article", "kWarning");
//		$('#sales_table_articles').combobox('textbox').focus ();
//		bIsSelectFieldValid = false;
//	}
	return bIsSelectFieldValid;
}

function sales_isValidClient()
{
	var bIsValid = false;
	try
	{
		var strClient = m_oSalesMemberData.m_nSelectedClientId;
		if(strClient != null && strClient > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function sales_isValidSite ()
{
	var bIsValid = false;
	try
	{
		var strSite = $('#sales_input_siteName').combobox('getValue');
		if(strSite != null && strSite > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function sales_isValidContact ()
{
	var bIsValid = false;
	try
	{
		var strContact = $('#sales_input_contactName').combobox('getValue');
		if(strContact != null && strContact > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
return bIsValid;
}

//function sales_articleValidate ()
//{
//	var bIsValid = false;
//	try
//	{
//		var editors = $('#sales_table_articles').datagrid('getEditors', m_oSalesMemberData.editIndex);
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
function  sales_validateArticleDetails() {
	var bIsValidArticle = false;
	var oSalesData = new SalesData();
	oSalesData.m_arrSalesLineItem = sales_getLineItemDataArray();
	if (oSalesData.m_arrSalesLineItem.length > 0) {
		bIsValidArticle = true;
	} else
		informUser(" Article Details cannot be empty ", "kWarning");
	return bIsValidArticle;
}

function sales_getFormData ()
{
	sales_acceptDGchanges ();
	var oSalesData = new SalesData ();
	oSalesData.m_oUserCredentialsData = new UserInformationData ();
	oSalesData.m_oSiteData = new SiteData ();
	oSalesData.m_oContactData = new ContactData ();
	oSalesData.m_strTo = $('#sales_input_to').combobox('getText');
	oSalesData.m_strInvoiceNo = $("#sales_input_invoiceNo").val();
	oSalesData.m_strChallanNumber = m_oSalesMemberData.m_oSalesData.m_strChallanNumber;
	//var m_strDate = $('#sales_input_date').datebox('getValue');
	var m_strDate = $('#sales_input_date').val();
	oSalesData.m_strDate = FormatDate (m_strDate);
	oSalesData.m_arrSalesLineItem = sales_getLineItemDataArray ();
	oSalesData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oClientData.m_nClientId = parseInt(m_oSalesMemberData.m_nSelectedClientId);
	oSalesData.m_oSiteData.m_nSiteId = $('#sales_input_siteName').combobox('getValue');
	oSalesData.m_bIsCFormProvided = document.getElementById("sales_input_CFormProvided").checked;
	var strContactId = $('#sales_input_contactName').combobox('getValue');
	if (strContactId > 0)
		oSalesData.m_oContactData.m_nContactId = $('#sales_input_contactName').combobox('getValue');
	else
		oSalesData.m_oContactData = null;
	oSalesData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oSalesData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	return oSalesData;
}

function sales_getLineItemDataArray ()
{
	var oLineItemDataArray = new Array ();
	var arrLineItemData = $('#sales_table_articles').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oSalesLineItemData = new SalesLineItemData ();
		oSalesLineItemData.m_nSerialNumber = $('#sales_table_articles').datagrid('getRowIndex',arrLineItemData[nIndex]);
		oSalesLineItemData.m_strArticleNumber =  arrLineItemData [nIndex].m_strArticleNumber;
		oSalesLineItemData.m_nQuantity = arrLineItemData [nIndex].m_nQuantity;
		oSalesLineItemData.m_nPrice = arrLineItemData [nIndex].m_nPrice;
		oSalesLineItemData.m_nDiscount = arrLineItemData [nIndex].m_nDiscount;
		oSalesLineItemData.m_nTax = arrLineItemData[nIndex].m_nTax;
		oSalesLineItemData.m_nReturnedQuantity = (arrLineItemData[nIndex].m_nReturnedQuantity != undefined && arrLineItemData[nIndex].m_nReturnedQuantity != "") ? arrLineItemData[nIndex].m_nReturnedQuantity : 0;
		if (oSalesLineItemData.m_strArticleNumber.trim() != "")
			oLineItemDataArray.push (oSalesLineItemData);
	}
	return oLineItemDataArray;
}

function sales_submit ()
{
	if (sales_validate () && sales_validateDGRow ())
		loadPage ("include/process.html", "ProcessDialog", "sales_progressbarLoaded ()");
}	

function sales_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("sales_button_save");
	var oSalesData = sales_getFormData ();
	if(document.getElementById("sales_button_save").getAttribute('update') == "false")
		SalesDataProcessor.create (oSalesData, sales_created);
	else
	{
		oSalesData.m_nId = m_oSalesListMemberData.m_nSelectedSalesId;
		SalesDataProcessor.update(oSalesData, sales_updated);
	}
}

function sales_validateDGRow ()
{
	var bIsValidateDGRow = true;
	sales_acceptDGchanges ();
	var arrLineItemData = $('#sales_table_articles').datagrid('getData').rows;
	if (arrLineItemData.length > 0)
	{
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oSalesLineItemData = new SalesLineItemData ();
		var strArticleNumber =  arrLineItemData [nIndex].m_strArticleNumber;
		var nQuantity = arrLineItemData [nIndex].m_nQuantity;
		var nPrice = arrLineItemData [nIndex].m_nPrice;
		if (strArticleNumber.trim() !="" && (nQuantity == "" || nQuantity == 0 || nPrice == "" || nPrice == 0))
		{
			bIsValidateDGRow = false;
			informUser (nQuantity == "" || nQuantity == 0 ? nQuantity == "" ? "Quantity cannot be empty." : "Quantity should be greater than 0." :  nPrice == "" ? "Price cannot be empty." : "Price should be greater than 0." );
			break;
		}
//		else if(strArticleNumber == "")
//		{
//			bIsValidateDGRow = false;
//			informUser("Items Not Added", "kWarning");
//		}
			
		}
	}
	return bIsValidateDGRow;
}

function sales_created (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{	
		HideDialog ("dialog");
		informUser ("Sales Saved Succesfully", "kSuccess");
		try
		{
			sales_handleAftersave ();
		}
		catch(oException){}
	}
	else
	{
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Sales Save Failed", "kAlert");
		enable ("sales_button_save");
	}
}

function sales_updated (oResponse)
{
	HideDialog ("ProcessDialog");
	if(oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		informUser("Sales updated successfully", "kSuccess");
		try
		{
			sales_handleAftersave ();
		}
		catch(oException){}
	}
	else
	{
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Sales Updation Failed", "kError");
		enable ("sales_button_save");
	}
}

function sales_saveAndPrint ()
{
	if (sales_validate () && sales_validateDGRow ())
	{
		loadPage ("include/process.html", "ProcessDialog", "sales_saveAndPrint_progressbarLoaded ()");
	}
}

function sales_saveAndPrint_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("sales_button_saveAndPrint");
	var oSalesData = sales_getFormData ();
	if(document.getElementById("sales_button_saveAndPrint").getAttribute('update') == "false")
		SalesDataProcessor.saveAndPrint (oSalesData, sales_savedForPrint );
	else
	{
		oSalesData.m_nId = m_oSalesMemberData.m_nSalesId;
		SalesDataProcessor.updateAndPrint(oSalesData, sales_savedForPrint);
	}
}

function sales_savedForPrint (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		m_oSalesMemberData.m_strXMLData = oResponse.m_strXMLData;
		navigate ('Print Sales','widgets/inventorymanagement/sales/salesPrint.js');
		try
		{
			sales_handleAftersave ();
		}
		catch(oException){}
	}
	else
	{
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Sales Save Failed", "kAlert");
		enable ("sales_button_saveAndPrint");
	}
}

function sales_saveAndMakeInvoice ()
{
	if (sales_validate () && sales_validateDGRow ())
	{
		loadPage ("include/process.html", "ProcessDialog", "sales_saveAndMakeInvoice_progressbarLoaded ()");
	}
}

function sales_saveAndMakeInvoice_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("sales_button_saveAndMakeInvoice");
	var oSalesData = sales_getFormData ();
	if(document.getElementById("sales_button_saveAndMakeInvoice").getAttribute('update') == "false")
		InvoiceDataProcessor.create (oSalesData, sales_savedForInvoice);
	else
	{
		oSalesData.m_nId = m_oSalesMemberData.m_nSalesId;
		InvoiceDataProcessor.update (oSalesData, sales_savedForInvoice);
	}
}

function sales_savedForInvoice (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		m_oSalesMemberData.m_strXMLData = oResponse.m_strXMLData;
		m_oSalesMemberData.m_oInvoiceData = oResponse.m_arrInvoice[0];
		navigate ('invoice','widgets/inventorymanagement/invoice/invoiceSales.js');
		try
		{
			sales_handleAftersave ();
		}
		catch(oException){}
	}
	else
	{
		informUser ("Sales Generate Invoice Failed", "kError");
		enable ("sales_button_saveAndMakeInvoice");
	}
}

function sales_saveAndMakeChallan ()
{
	if (sales_validate () && sales_validateDGRow ())
	{
		loadPage ("include/process.html", "ProcessDialog", "sales_saveAndMakeChallan_progressbarLoaded ()");
	}
}

function sales_saveAndMakeChallan_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("sales_button_saveAndMakeChallan");
	var oSalesData = sales_getFormData ();
	if(document.getElementById("sales_button_saveAndMakeChallan").getAttribute('update') == "false")
		ChallanDataProcessor.create (oSalesData, sales_savedForChallan);
	else
	{
		oSalesData.m_nId = m_oSalesMemberData.m_nSalesId;
		ChallanDataProcessor.update (oSalesData, sales_savedForChallan);
	}
}
function sales_savedForChallan (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		var oChallanData = oResponse.m_arrChallan[0];
		m_oSalesMemberData.m_strXMLData = oResponse.m_strXMLData;
		m_oSalesMemberData.m_nChallanId = oChallanData.m_nChallanId;
		m_oSalesMemberData.m_nInvoiceId = oChallanData.m_oInvoiceData != null ? oChallanData.m_oInvoiceData.m_nInvoiceId : -1;
		navigate ('challan','widgets/inventorymanagement/challan/challanSales.js')
		try
		{
			sales_handleAftersave ();
		}
		catch(oException){}
	}
	else
	{
		informUser ("Sales Generate Challan Failed", "kError");
		enable ("sales_button_saveAndMakeChallan");
	}
}

function sales_closePrint ()
{
	sales_cancel ();
}

function sales_print ()
{
	document.getElementById("secondDialog").style.top = "0";
	document.getElementById("secondDialog").style.bottom = "0";
	document.getElementById("secondDialog").style.left = "0";
	document.getElementById("secondDialog").style.right = "0";
	window.print();
}

function sales_addClient ()
{
	navigate ("clientInfo", "widgets/clientmanagement/addClient.js");
}

function sales_addClientSites ()
{
	m_oSalesMemberData.m_bIsForSite = true;
	navigate ("addSites", "widgets/clientmanagement/addSites.js");
}

function sales_addClientContacts ()
{
	m_oSalesMemberData.m_bIsForSite = false;
	navigate ("addContacts", "widgets/clientmanagement/addContacts.js");
}

function clientInfo_handleAfterUpdate ()
{ 
	sales_getClientInfo ();
}

function sales_getClientInfo ()
{
	var oClientData = new ClientData ();
	oClientData.m_nClientId = m_oTrademustMemberData.m_nSelectedClientId;
	ClientDataProcessor.get (oClientData, sales_gotClientInfo);
}

function sales_gotClientInfo(oResponse)
{
	if(oResponse.m_arrClientData.length > 0)
	{
		if(m_oSalesMemberData.m_bIsForSite)
			sales_initSiteCombobox (sales_getActiveSites(oResponse.m_arrClientData[0].m_oSites));
		else
			sales_initContactsCombobox(oResponse.m_arrClientData[0].m_oContacts);
	}
		
	
}