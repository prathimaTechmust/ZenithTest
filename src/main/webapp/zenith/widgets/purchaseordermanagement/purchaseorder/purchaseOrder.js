var purchaseOrder_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/challan/ChallanData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderLineItemData.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderStockLineItemData.js',
	'widgets/quotationmanagement/quotation/QuotationData.js',
	'widgets/quotationmanagement/quotation/QuotationLineItemData.js'
];

 includeDataObjects (purchaseOrder_includeDataObjects, "purchaseOrder_loaded ()");

function purchaseOrder_memberData ()
{
	this.m_nPurchaseOrderId = -1;
	this.m_nEditIndex = undefined;
	this.m_arrArticle = new Array ();
	this.m_oArticleDetails = null;
	this.m_oClientData = new ClientData ();
	this.m_arrDeletedLineItems = new Array ();
	this.m_bDisableInvoiceButton = true;
	this.m_arrUniqueTaxNames = new Array();
	this.m_bIsForSite = true;
	this.m_bIsForQuotation = false;
	this.m_oQuotationData = new QuotationData();
	this.m_nQuotationId = -1
	this.m_strPONumber = "";
	this.m_nSelectedClientId = -1;
	this.m_strClientArticleNumber = "";
	this.m_nChallanId = -1;
	this.m_nInvoiceId = -1;
}

var m_oPurchaseOrderMemberData = new purchaseOrder_memberData ();

function purchaseOrder_new ()
{
	createPopup("dialog", "#purchaseOrder_button_create", "#purchaseOrder_button_cancel", true);
	purchaseOrder_init ();
	if(m_oPurchaseOrderMemberData.m_bIsForQuotation)
	{
		document.getElementById ("purchaseOrder_img_add").style.visibility="hidden";
		purchaseOrder_setValues ();
	}
	purchaseOrder_appendRow ();
	var oUpdateButton = document.getElementById("purchaseOrder_button_create");
}

function purchaseOrder_init ()
{
	initFormValidateBoxes ("purchaseOrder_form_id");
//	$("#purchaseOrder_input_date" ).datebox({required:true});
	$("#purchaseOrder_input_date" ).datepicker({minDate:'-6y',maxDate:'+6m'});
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
//	$('#purchaseOrder_input_date').datebox('setValue', dCurrentDate);
	$('#purchaseOrder_input_date').datepicker('setDate', dCurrentDate);
	purchaseOrder_initToCombobox ()
	purchaseOrder_getUniqueTaxNames ();
	purchaseOrder_initializeDataGrid ();

}

function purchaseOrder_setValues ()
{
	var oPurchaseOrderItem = m_oPurchaseOrderMemberData.m_oQuotationData;
	m_oPurchaseOrderMemberData.m_nQuotationId = m_oPurchaseOrderMemberData.m_oQuotationData.m_nQuotationId;
	purchaseOrder_populateToCombobox(oPurchaseOrderItem);
	var nShippedQty = 0;
	$('#purchaseOrder_input_clientName').combobox('select', oPurchaseOrderItem.m_oClientData.m_nClientId);
	$('#purchaseOrder_input_clientName').combobox('disable');
	$('#purchaseOrder_input_siteName').combobox('select', oPurchaseOrderItem.m_oSiteData.m_nSiteId);
	if(oPurchaseOrderItem.m_oContactData != null)
		$('#purchaseOrder_input_contactName').combobox('select', oPurchaseOrderItem.m_oContactData.m_nContactId);
	document.getElementById("purchaseOrder_input_CFormProvided").checked = oPurchaseOrderItem.m_bIsCFormProvided;
	var arrOrderedLineItem = getOrderedLineItems (oPurchaseOrderItem.m_oQuotationLineItems);
	for (var nIndex = 0; nIndex < arrOrderedLineItem.length; nIndex++)
	{
		nShippedQty += arrOrderedLineItem[nIndex].m_nShippedQty;
		try
		{
			arrOrderedLineItem[nIndex].m_strArticleNumber = arrOrderedLineItem[nIndex].m_oItemData.m_strArticleNumber;
		}catch (oException){}
		arrOrderedLineItem[nIndex].m_strUnit = arrOrderedLineItem[nIndex].m_oItemData != null ?arrOrderedLineItem[nIndex].m_oItemData.m_strUnit :"No.";
		arrOrderedLineItem[nIndex].m_strDesc = arrOrderedLineItem[nIndex].m_strArticleDescription + " | " + arrOrderedLineItem[nIndex].m_strDetail;
		arrOrderedLineItem[nIndex].m_nQty = Number(arrOrderedLineItem[nIndex].m_nQuantity);
		arrOrderedLineItem[nIndex].m_nShippedQty = 0;
		arrOrderedLineItem[nIndex].m_nShipQty = 0;
		
		$('#purchaseOrder_table_itemsDG').datagrid('appendRow',arrOrderedLineItem[nIndex]);
	}
	if(nShippedQty > 0)
	{
		m_oPurchaseOrderMemberData.m_bDisableInvoiceButton = false;
		purchaseOrder_displayButtons (false, true, false);
	}
}

function clientInfo_handleAfterSave ()
{
	// Handler Function for Client Save.
}

function purchaseOrder_initToCombobox ()
{
	$('#purchaseOrder_input_clientName').combobox
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
    		var oUpdateButton = document.getElementById("purchaseOrder_button_create");
    		if(row.m_bClientLock && oUpdateButton.getAttribute('update') == "false")
    		{
    			informUser ("This Client is locked!", "");
    			$("#purchaseOrder_input_clientName").combobox('setText',"");
    			$('#purchaseOrder_input_clientName').combobox('textbox').focus ();
    		}
    		else
    		{
    			purchaseOrder_updateDGTaxBasedOnCForm ();
	    		m_oPurchaseOrderMemberData.m_oClientData = row;
	    		purchaseOrder_setClientInfo(m_oPurchaseOrderMemberData.m_oClientData);
	    		$('#purchaseOrder_input_siteName').combobox('textbox').focus ();
    		}
	    },
    	onChange:function(row)
	    {
	    	document.getElementById ("purchaseOrder_img_addSite").style.visibility="hidden";
	    	document.getElementById ("purchaseOrder_img_addContact").style.visibility="hidden";
	    	document.getElementById("purchaseOrder_input_CFormProvided").checked = false;
	    	document.getElementById("purchaseOrder_td_CformProvided").style.visibility="hidden";
	    	var nClientId = $('#purchaseOrder_input_clientName').combobox('getValue') > 0 ? $('#purchaseOrder_input_clientName').combobox('getValue') : -1;
	    	var arrArticles = $('#purchaseOrder_table_itemsDG').datagrid('getRows');
	    	if(arrArticles.length > 1 && nClientId > 0)
	    		purchaseOrder_getItems (nClientId, arrArticles);
	    	purchaseOrder_validatePONumber ();
	    }
	});
	var toTextBox = $('#purchaseOrder_input_clientName').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Client Name";
	toTextBox.focus ();
	toTextBox.bind('keydown', function (e)
		    {
				purchaseOrder_handleKeyboardNavigation (e);
		    });
}

function purchaseOrder_getItems (nClientId, arrArticles)
{
	assert.isArray(arrArticles, "arrArticles expected to be an Array.");
	for(var nIndex = 0; nIndex< arrArticles.length; nIndex++)
	{
		if(arrArticles[nIndex].m_nItemId > 0)
		{
			var oItemData  = new ItemData ();
			oItemData.m_nItemId = arrArticles[nIndex].m_nItemId;
		
			ItemDataProcessor.getItemData(oItemData, nClientId, function(oData)
					{
						m_oPurchaseOrderMemberData.m_oArticleDetails = oData;
						m_oPurchaseOrderMemberData.m_nEditIndex = nIndex;
						purchaseOrder_getDiscount (oData);
					
					});
		}
	}
}
	
var getFilteredClientData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined && param.q.trim() != "")
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

function purchaseOrder_setClientInfo(oClientData)
{
	assert.isObject(oClientData, "oClientData expected to be an Object.");
	m_oTrademustMemberData.m_nSelectedClientId = oClientData.m_nClientId;
	document.getElementById ("purchaseOrder_td_tinNo").style.visibility = "visible";
	document.getElementById ("purchaseOrder_td_vatNo").style.visibility = "visible";
	document.getElementById ("purchaseOrder_td_cstNo").style.visibility = "visible";
	$("#purchaseOrder_input_clientName").combobox('setText',oClientData.m_strCompanyName);
	$("#purchaseOrder_label_address").val(oClientData.m_strAddress);
	$("#purchaseOrder_label_city").val(oClientData.m_strCity + "-" + oClientData.m_strPinCode);
	$("#purchaseOrder_label_phoneNumber").val(oClientData.m_strMobileNumber);
	$("#purchaseOrder_label_email").val(oClientData.m_strEmail);
	$("#purchaseOrder_label_tinNo").val(oClientData.m_strTinNumber);
	$("#purchaseOrder_label_vatNo").val(oClientData.m_strVatNumber);
	$("#purchaseOrder_label_cstNo").val(oClientData.m_strCSTNumber);
	document.getElementById ("purchaseOrder_img_addSite").style.visibility="visible";
	document.getElementById ("purchaseOrder_img_addContact").style.visibility="visible";
	purchaseOrder_initSiteCombobox(purchaseOrder_getActiveSites(oClientData.m_oSites));
	purchaseOrder_initContactsCombobox(oClientData.m_oContacts);
	purchaseOrder_showOrHideCformChkBox (oClientData.m_bOutstationClient);
}

function purchaseOrder_getActiveSites (arrSites)
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

function purchaseOrder_showOrHideCformChkBox (bIsOutstationClient)
{
	assert.isBoolean(bIsOutstationClient, "bIsOutstationClient should be a boolean value");
	if(bIsOutstationClient)
		document.getElementById ("purchaseOrder_td_CformProvided").style.visibility="visible";
	else
		document.getElementById ("purchaseOrder_td_CformProvided").style.visibility="hidden";
}

function purchaseOrder_initSiteCombobox(arrClientSites)
{
	$('#purchaseOrder_input_siteName').combobox
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
    	onSelect: function ()
    	{
    		$('#purchaseOrder_input_contactName').combobox('textbox').focus ();
    	}
	});
}

function purchaseOrder_initContactsCombobox(arrClientContacts)
{
	$('#purchaseOrder_input_contactName').combobox
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
    	onSelect: function ()
    	{
    		$('#purchaseOrder_input_purchaseOrderNumber').focus ();
    	}
	});
}

function purchaseOrder_handleKeyboardNavigation (e)
{
	if(e.keyCode == 13)
	{
		purchaseOrder_setClientInfo(m_oPurchaseOrderMemberData.m_oClientData);
		$('#purchaseOrder_input_siteName').combobox('textbox').focus ();
	}
}

function purchaseOrder_getUniqueTaxNames()
{
	TaxDataProcessor.getUniqueTaxNames ( function(oResponse)
			{
				m_oPurchaseOrderMemberData.m_arrUniqueTaxNames = oResponse.m_arrTax;
			});
}

function purchaseOrder_addItem ()
{
	navigate ("newItem", "widgets/inventorymanagement/item/itemAdmin.js");
}

function purchaseOrder_initializeDataGrid ()
{
	$('#purchaseOrder_table_itemsDG').datagrid ({
	    columns:[[  
			{field:'m_strArticleNumber',title:'Article# <img title="Add" src="images/add.PNG" align="right" id="purchaseOrder_img_addItem" onClick="purchaseOrder_addItem ()"/>', width:90,
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
				        		m_oPurchaseOrderMemberData.m_oArticleDetails = row;
				        		purchaseOrder_setArticleDetails (row);
				        		purchaseOrder_getDiscount (row);
				            }
			    	    }
					},
			},
			{field:'itemCustomizeAction',title:'',align:'center',width:25,
				formatter:function(value,row,index)
				{
        			return purchaseOrder_initItemCustomizeAction (row, index);
				}
			},
	        {field:'m_strDesc',title:'Item Name',width:150,align:'left',editor:{'type':'text'}},
	        {field:'m_nQty',title:'Qty',width:60,align:'right',editor:{'type':'numberbox'}},
	        {field:'m_strUnit',title:'Unit',width:60,align:'left',editor:{'type':'text'}},
	        {field:'m_nPrice',title:'Price',width:70,align:'right',editor:{'type':'text'}},
	        {field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',editor:{'type':'text'}},
	        {field:'m_strTaxName',title:'Tax Name',width:70,align:'right',
	        	editor:
	        	{ 
	        		type:'combobox',
	        		options:
		            {
			        	data:m_oPurchaseOrderMemberData.m_arrUniqueTaxNames,
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
	        {field:'m_nTax',title:'Tax(%)',width:50,align:'right',editor:{'type':'text'}},
	        {field:'m_nShippedQty',title:'Shipped Qty',width:70,align:'right',
	        	editor:
	        	{
	        		'type':'numberbox','options':{precision:'2',disabled:true}
	        	}
	        },
	        {field:'m_nShipQty',title:'Ship Qty',width:50,align:'right',editor:{'type':'text'}},
	        {field:'m_nItemId',hidden:true,editor:{'type':'text'}},
	        {field:'Actions',title:'',align:'center',width:20,
				formatter:function(value,row,index)
				{
		        	if(Number(row.m_nShipQty) > 0 || Number(row.m_nShippedQty) > 0)
	        			return purchaseOrder_hideDeleteImage ();
		        	else
		        		return purchaseOrder_showDeleteImage (row, index);
				}
			}
	    ]],
	    onClickRow: function (rowIndex, rowData)
		{
			purchaseOrder_editRowDG (rowIndex);
		}
	});
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
		var nClientId = $('#purchaseOrder_input_clientName').combobox('getValue') > 0 ? $('#purchaseOrder_input_clientName').combobox('getValue') : -1;
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
						arrItemData[nIndex].m_oApplicableTax.m_oTaxes[0].m_nPercentage + " | "+
						nTaxWithCFrom);
				    }
					success(arrItemData);
					purchaseOrder_showOrHideDGAction (arrItemData.length);
				});
	}
	else
		success(new Array ());
}

function purchaseOrder_showOrHideDGAction (nSuggestedItemsLength)
{
	assert.isNumber(nSuggestedItemsLength, "nSuggestedItemsLength expected to be a Number.");
	if(nSuggestedItemsLength <= 1 && $('#purchaseOrder_input_clientName').combobox('getValue') > 0)
		document.getElementById("purchaseOrder_td_customizeIcon_"+m_oPurchaseOrderMemberData.m_nEditIndex).style.visibility="visible";
	else
		document.getElementById("purchaseOrder_td_customizeIcon_"+m_oPurchaseOrderMemberData.m_nEditIndex).style.visibility="hidden";
}

function purchaseOrder_setArticleDetails(oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	var editors = $('#purchaseOrder_table_itemsDG').datagrid('getEditors', m_oPurchaseOrderMemberData.m_nEditIndex);
	var articleNumberEditor = editors[0];
    var itemNameEditor = editors[1];
    var quantityEditor = editors[2];
    var unitEditor = editors[3];
    var priceEditor = editors[4];
    var discountEditor = editors[5];
    var taxNameEditor = editors[6];
    var taxEditor = editors[7];
    var shippedQtyEditor = editors[8];
    var shipQtyEditor = editors[9];
    var itemIdEditor = editors[10];
    $(articleNumberEditor.target).combobox('textbox').focus ();
	var bIsArticleDuplicate =  purchaseOrder_isArticleDuplicate (oRowData.m_strArticleNumber, m_oPurchaseOrderMemberData.m_nEditIndex);
	if(!bIsArticleDuplicate)
	{
		$(articleNumberEditor.target).combobox('setValue',oRowData.m_strArticleNumber);
		$(itemNameEditor.target).val(oRowData.m_strItemName + " | " + oRowData.m_strDetail);
		$(unitEditor.target).val(oRowData.m_strUnit);
		$(priceEditor.target).val(oRowData.m_nSellingPrice);
		$(discountEditor.target).val(0);
		$(taxNameEditor.target).combobox('setValue',m_oPurchaseOrderMemberData.m_oClientData.m_bOutstationClient ? "CST" : oRowData.m_oApplicableTax.m_oTaxes[0].m_strTaxName);
		purchaseOrder_setTaxOnCFormProvided ();
		$(itemNameEditor.target).select();
		$(itemIdEditor.target).val(oRowData.m_nItemId);
	}
	else
	{
		$(articleNumberEditor.target).combobox('setValue','');
		$(itemNameEditor.target).val('');
		$(unitEditor.target).val('');
		$(priceEditor.target).val('');
		$(discountEditor.target).val(0);
		$(taxNameEditor.target).combobox('setValue','');
		$(taxEditor.target).val('');
		$(articleNumberEditor.target).combobox('textbox').focus();
		$(itemIdEditor.target).val('');
	}
}

function purchaseOrder_setTaxOnCFormProvided ()
{
	var editors = $('#purchaseOrder_table_itemsDG').datagrid('getEditors', m_oPurchaseOrderMemberData.m_nEditIndex);
	var taxPercentEditor = editors[7];
	if(purchaseOrder_isValidClient () && document.getElementById("purchaseOrder_input_CFormProvided").checked)
		$(taxPercentEditor.target).val(m_oPurchaseOrderMemberData.m_oArticleDetails.m_oTaxWithCForm != null ? m_oPurchaseOrderMemberData.m_oArticleDetails.m_oTaxWithCForm.m_oTaxes[0].m_nPercentage : "");
	else
		$(taxPercentEditor.target).val(m_oPurchaseOrderMemberData.m_oArticleDetails.m_oApplicableTax.m_oTaxes[0].m_nPercentage);
}

function purchaseOrder_updateDGTaxBasedOnCForm ()
{
	purchaseOrder_acceptDGchanges();
	var arrArticles = $('#purchaseOrder_table_itemsDG').datagrid('getRows');
	for(var nIndex = 0; nIndex< arrArticles.length; nIndex++)
	{
		var nItemId = arrArticles[nIndex].m_nItemId;
		if(nItemId > 0)
			purchaseOrder_updateRowTaxField (nItemId, nIndex);
	}
}

function purchaseOrder_updateRowTaxField (nItemId, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oItemData = new ItemData ();
	oItemData.m_nItemId = nItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	ItemDataProcessor.list(oItemData, "", "", "", "", function(oItemDataResponse) 
			{
				purchaseOrder_gotItemListForTax(oItemDataResponse, nIndex);
			});
}

function purchaseOrder_gotItemListForTax (oItemDataResponse, nDGRowIndex)
{
	var nTaxPercent = 0;
	var oItemData = oItemDataResponse.m_arrItems.length > 0 ? oItemDataResponse.m_arrItems[0] : null;
	var strTaxName = m_oPurchaseOrderMemberData.m_oClientData.m_bOutstationClient ? "CST" : (oItemData != null ? oItemDataResponse.m_arrItems[0].m_oApplicableTax.m_oTaxes[0].m_strTaxName : "");
	if(document.getElementById("purchaseOrder_input_CFormProvided").checked)
		nTaxPercent = oItemData != null && oItemData.m_oTaxWithCForm != null ? oItemData.m_oTaxWithCForm.m_oTaxes[0].m_nPercentage : "";
	else
		nTaxPercent = oItemData != null ? oItemData.m_oApplicableTax.m_oTaxes[0].m_nPercentage : "";
	$('#purchaseOrder_table_itemsDG').datagrid('updateRow',{
		index: nDGRowIndex,
		row: 
		{
			m_nTax : nTaxPercent,
			m_strTaxName : strTaxName
		}
	});
}

function purchaseOrder_appendRow ()
{
    if (purchaseOrder_endEdit ())
    {	
    	purchaseOrder_acceptDGchanges();
    	$('#purchaseOrder_table_itemsDG').datagrid('appendRow',{m_strArticleNumber:''});
    	m_oPurchaseOrderMemberData.m_nEditIndex = $('#purchaseOrder_table_itemsDG').datagrid('getRows').length-1;
        $('#purchaseOrder_table_itemsDG').datagrid('selectRow',  m_oPurchaseOrderMemberData.m_nEditIndex).datagrid('beginEdit',  m_oPurchaseOrderMemberData.m_nEditIndex);
        purchaseOrder_setEditing(m_oPurchaseOrderMemberData.m_nEditIndex)
    }
}

function purchaseOrder_acceptDGchanges()
{
	if (purchaseOrder_endEdit())
        $('#purchaseOrder_table_itemsDG').datagrid('acceptChanges');
}

function purchaseOrder_endEdit ()
{
    if (m_oPurchaseOrderMemberData.m_nEditIndex == undefined)
    	return true
    if ($('#purchaseOrder_table_itemsDG').datagrid('validateRow', m_oPurchaseOrderMemberData.m_nEditIndex))
    {
        $('#purchaseOrder_table_itemsDG').datagrid('endEdit',  m_oPurchaseOrderMemberData.m_nEditIndex);
        m_oPurchaseOrderMemberData.m_nEditIndex = undefined;
        return true;
    } 
    else 
        return false;
}

function purchaseOrder_getDiscount (oData)
{
	assert.isObject(oData, "oData expected to be an Object.");
	var nClientId = $('#purchaseOrder_input_clientName').combobox('getValue');
	if(nClientId > 0)
	{
		var oClientData = new ClientData ();
		oClientData.m_nClientId = nClientId;
		var oItemData = new ItemData ();
		oItemData.m_nItemId = oData.m_nItemId;
		DiscountStructureDataProcessor.getDiscount(oClientData, oItemData, purchaseOrder_gotDiscount)
	}
}

function purchaseOrder_gotDiscount (nDiscount)
{
	assert.isNumber(nDiscount, "nDiscount expected to be a Number.");
	var editors = $('#purchaseOrder_table_itemsDG').datagrid('getEditors', m_oPurchaseOrderMemberData.m_nEditIndex);
	if(editors.length > 0)
	{
		var discountEditor = editors[5];
		$(discountEditor.target).val(nDiscount);
	}
	else
	{
		$('#purchaseOrder_table_itemsDG').datagrid('updateRow',{
			index: m_oPurchaseOrderMemberData.m_nEditIndex,
			row: 
			{
				m_nDiscount : nDiscount,
				m_strArticleNumber : m_oPurchaseOrderMemberData.m_oArticleDetails.m_strArticleNumber,
				m_strDesc : m_oPurchaseOrderMemberData.m_oArticleDetails.m_strItemName,
				m_nPrice : m_oPurchaseOrderMemberData.m_oArticleDetails.m_nSellingPrice,
				m_strUnit : m_oPurchaseOrderMemberData.m_oArticleDetails.m_strUnit
			}
		});
	}
}

function purchaseOrder_setEditing(rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
    var editors = $('#purchaseOrder_table_itemsDG').datagrid('getEditors', rowIndex);
    var articleNumberEditor = editors[0];
    var itemNameEditor = editors[1];
    var quantityEditor = editors[2];
    var unitEditor = editors[3];
    var priceEditor = editors[4];
    var discountEditor = editors[5];
    var taxNameEditor = editors[6];
    var taxEditor = editors[7];
    var shippedQtyEditor = editors[8];
    var shipQtyEditor = editors[9];
    var articleNoTextbox =  $(articleNumberEditor.target).combobox('textbox');
    articleNoTextbox.focus ();
    var taxNameTextbox =  $(taxNameEditor.target).combobox('textbox');
    m_oPurchaseOrderMemberData.m_nEditIndex = rowIndex;
    articleNoTextbox.bind('keydown', function (e)
    	    {
    			purchaseOrder_handleKeyboardNavigation (e)
    	    });
    quantityEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
		    });
    unitEditor.target.bind('keydown', function (e)
    	    {
    	 		if(articleNoTextbox[0].value != "" && e.keyCode != 9)
    	 			return false;
    	    });
    priceEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
		    });
    taxEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
    			validatePercentage (this);
		    });
    quantityEditor.target.bind('change', function()
    		{
    			purchaseOrder_validateShipValue (shipQtyEditor.target.val());
    			if (Number (this.value) == 0)
    				this.focus();
    		});
    quantityEditor.target.bind('blur', function()
    		{
    			if (Number (this.value) == 0)
    				this.focus();
    		});
    priceEditor.target.bind('change', function()
    		{
		    	if (Number (this.value) == 0)
					this.focus();
    		});
    priceEditor.target.bind('blur', function()
    		{
		    	if (Number (this.value) == 0)
					this.focus();
    		});
    discountEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
    			validatePercentage (this);
		    });
    discountEditor.target.bind('blur', function()
    		{
		    	if(this.value == "")
		    		$(discountEditor.target).val(0);
    		});
    taxNameTextbox.bind('blur', function()
    		{	
    			if(!purchaseOrder_validateTaxName(this.value))
    				this.focus ();
    		});
    taxNameTextbox.bind('change', function()
    		{	
    			if(!purchaseOrder_validateTaxName(this.value))
    				this.focus ();
    		});
    taxEditor.target.bind('blur', function()
    		{	
    			if(this.value == "")
    				this.focus ();
    		});
    shipQtyEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
		    });
    shipQtyEditor.target.bind('change', function()
    		{
    			purchaseOrder_validateShipValue (this.value);
    			if (purchaseOrder_getShipQty (Number (this.value), rowIndex) > 0)
    				purchaseOrder_showButtons ();
    			else
    				purchaseOrder_hideButtons ();
    			if (rowIndex ==$('#purchaseOrder_table_itemsDG').datagrid('getRows').length-1)
    				purchaseOrder_appendRow ();
    		});
    shipQtyEditor.target.bind('blur', function()
    		{
    			purchaseOrder_validateShipValue (this.value)
    			if (purchaseOrder_getShipQty (Number (this.value), rowIndex) > 0)
    				purchaseOrder_showButtons ();
    			else
    				purchaseOrder_hideButtons ();
    			if (rowIndex ==$('#purchaseOrder_table_itemsDG').datagrid('getRows').length-1)
    				purchaseOrder_appendRow ();
    		});
    function purchaseOrder_validateShipValue (nValue)
    {
    	var nBalanceQty = Number(quantityEditor.target.val() - shippedQtyEditor.target.val());
    	if(Number(nValue) > nBalanceQty)
    	{
    		informUser ("ship Quantity cannot exceed " + nBalanceQty + ".", "kWarning");
    		$(shipQtyEditor.target).val('');
    		shipQtyEditor.target.focus ();
    	}
    }
    function purchaseOrder_handleKeyboardNavigation (e)
    {
    	assert.isObject(e, "e expected to be an Object.");
    	if(e.keyCode == 13)
    		purchaseOrder_setArticleDetails (m_oPurchaseOrderMemberData.m_oArticleDetails);
    }
    function purchaseOrder_validateTaxName(strTaxName)
    {
    	assert.isString(strTaxName, "strTaxName expected to be a string.");
    	var bIsValidTaxName = false;
    	for (var nIndex = 0; nIndex < m_oPurchaseOrderMemberData.m_arrUniqueTaxNames.length; nIndex++)
    	{
    		if(strTaxName.toLowerCase() == m_oPurchaseOrderMemberData.m_arrUniqueTaxNames[nIndex].m_strTaxName.toLowerCase())
    			bIsValidTaxName = true;
    	}
    	return bIsValidTaxName;
    }

    function purchaseOrder_getShipQty(nShipValue, rowIndex)
    {
    	assert.isNumber(nShipValue, "nShipValue expected to be a Number.");
    	var nShipQty = nShipValue;
    	if(nShipQty <= 0)
    		nShipQty = purchaseOrder_calculateShipQty(rowIndex);
    	return nShipQty;
    }

    function purchaseOrder_calculateShipQty(rowIndex)
    {
    	var nShipQty = 0;
    	var arrRows = $('#purchaseOrder_table_itemsDG').datagrid('getRows');
    	for(nIndex = 0; nIndex < arrRows.length; nIndex++)
    	{
    		if(rowIndex != nIndex)
    			nShipQty += Number(arrRows[nIndex].m_nShipQty);
    	}
    	return nShipQty;
    }
}

function purchaseOrder_showButtons ()
{
	purchaseOrder_displayButtons (true, false, false);
}

function purchaseOrder_hideButtons ()
{
	purchaseOrder_displayButtons (false, true, true);
}

function purchaseOrder_displayButtons (bSaveButtonDisabled, bMakeChallanButtonDisabled, bMakeInvoiceButtonDisabled)
{
	assert.isBoolean(bSaveButtonDisabled, "bSaveButtonDisabled should be a boolean value");
	assert.isBoolean(bMakeChallanButtonDisabled, "bMakeChallanButtonDisabled should be a boolean value");
	assert.isBoolean(bMakeInvoiceButtonDisabled, "bMakeInvoiceButtonDisabled should be a boolean value");
	var oPOCreateButton = document.getElementById ("purchaseOrder_button_create");
	var oPOMakeChallanButton = document.getElementById ("purchaseOrder_button_saveAndMakeChallan");
	var oPOMakeInvoiceButton = document.getElementById ("purchaseOrder_button_saveAndMakeInvoice");
	oPOCreateButton.disabled = bSaveButtonDisabled
	oPOMakeChallanButton.disabled = bMakeChallanButtonDisabled;
	oPOMakeInvoiceButton.disabled = m_oPurchaseOrderMemberData.m_bDisableInvoiceButton ? bMakeInvoiceButtonDisabled : false;
	bSaveButtonDisabled ? oPOCreateButton.style.backgroundColor = "#c0c0c0" :  oPOCreateButton.style.backgroundColor = "#0E486E";
	bMakeChallanButtonDisabled ? oPOMakeChallanButton.style.backgroundColor = "#c0c0c0" :  oPOMakeChallanButton.style.backgroundColor = "#0E486E";
	oPOMakeInvoiceButton.disabled ? oPOMakeInvoiceButton.style.backgroundColor = "#c0c0c0" : oPOMakeInvoiceButton.style.backgroundColor = "#0E486E"
}

function purchaseOrder_initItemCustomizeAction (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td style="border-style:none"> <img title="customize" id="purchaseOrder_td_customizeIcon_'+nIndex+'" src="images/customize.png" width="20"  style="visibility:hidden" onClick="purchaseOrder_customizeArticleNumber('+nIndex+')"/> </td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function purchaseOrder_showDeleteImage (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td> <img title="Delete" src="images/delete.png" width="20"  id="deleteImageId" onClick="purchaseOrder_delete('+nIndex+')"/> </td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function purchaseOrder_customizeArticleNumber (nIndex)
{
	purchaseOrder_editRowDG (nIndex);
	var oEditors = $('#purchaseOrder_table_itemsDG').datagrid('getEditors', nIndex);
	var articleNumberEditor = oEditors[0];
	m_oPurchaseOrderMemberData.m_strClientArticleNumber = $(articleNumberEditor.target).combobox('getValue');
	m_oPurchaseOrderMemberData.m_nSelectedClientId = $('#purchaseOrder_input_clientName').combobox('getValue');
	if(m_oPurchaseOrderMemberData.m_nSelectedClientId > 0 && m_oPurchaseOrderMemberData.m_strClientArticleNumber.trim() != "")
		navigate ('articleCustomize','widgets/inventorymanagement/sales/articleCustomizeForPO.js');
	else
		$('#purchaseOrder_input_clientName').combobox('getValue') > 0 ? informUser("Please enter a article number!", "kWarning") : informUser("Please Select Client Name!", "kWarning");
}

function purchaseOrder_validatePONumber ()
{
	var bIsPONumberExist = false;
	if($('#purchaseOrder_input_clientName').combobox('getValue') > 0 && $("#purchaseOrder_input_purchaseOrderNumber").val() != m_oPurchaseOrderMemberData.m_strPONumber)
	{
		var oPurchaseOrderData = new PurchaseOrderData ();
		oPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		oPurchaseOrderData.m_oClientData.m_nClientId = $('#purchaseOrder_input_clientName').combobox('getValue');
		oPurchaseOrderData.m_strPurchaseOrderNumber = $("#purchaseOrder_input_purchaseOrderNumber").val();
		
		PurchaseOrderDataProcessor.validateOrderNumber(oPurchaseOrderData,
				function (oResponse)
				{		
					if(oResponse.m_arrPurchaseOrder.length > 0)
					{
						informUser("PurchaseOrder Number already exists!", "kWarning");
						$("#purchaseOrder_input_purchaseOrderNumber", "").val();
						$("#purchaseOrder_input_purchaseOrderNumber").focus ();
						bIsPONumberExist = true;
						
					}
				}
		);
	}
	return bIsPONumberExist;
}

function purchaseOrder_delete (nIndex)
{
	var oPurchaseOrderLineItemData = new PurchaseOrderLineItemData ();
	var nLineItemId = $('#purchaseOrder_table_itemsDG').datagrid ('getRows')[nIndex].m_nId;
	oPurchaseOrderLineItemData.m_nId = nLineItemId > 0 ? nLineItemId : -1;
	if (oPurchaseOrderLineItemData.m_nId > 0)
		m_oPurchaseOrderMemberData.m_arrDeletedLineItems.push (oPurchaseOrderLineItemData);
	$('#purchaseOrder_table_itemsDG').datagrid ('deleteRow', nIndex);
	$('#purchaseOrder_table_itemsDG').datagrid('acceptChanges');
	refreshDataGrid ('#purchaseOrder_table_itemsDG');
	if ($('#purchaseOrder_table_itemsDG').datagrid('getRows').length == 0)
		purchaseOrder_appendRow ();
}

function purchaseOrder_hideDeleteImage ()
{
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td style="border-style:none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function purchaseOrder_editRowDG (rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	assert.isOk(rowIndex > -1, "nIndex must be a positive value.");
	 if (m_oPurchaseOrderMemberData.m_nEditIndex != rowIndex)
	    {
	        if (purchaseOrder_endEdit ())
	        {
	        	m_oPurchaseOrderMemberData.m_nEditIndex = rowIndex;
	            $('#purchaseOrder_table_itemsDG').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
	            purchaseOrder_setEditing(m_oPurchaseOrderMemberData.m_nEditIndex);
	        } 
	        else 
	            $('#purchaseOrder_table_itemsDG').datagrid('selectRow', m_oPurchaseOrderMemberData.m_nEditIndex);
	    }
}

function purchaseOrder_isArticleDuplicate (articleNumberEditor, rowIndex)
{
	assert.isString(articleNumberEditor, "articleNumberEditor expected to be a String.");
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	assert.isOk(rowIndex > -1, "rowIndex must be a positive value.");
	var arrLineItemData = $('#purchaseOrder_table_itemsDG').datagrid('getData').rows;
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

function purchaseOrder_addClient ()
{
	navigate ("clientInfo", "widgets/clientmanagement/addClient.js");
}

function purchaseOrder_cancel ()
{
	HideDialog ("dialog");
}

function purchaseOrder_submit ()
{
	if (purchaseOrder_validate () && purchaseOrder_validateDGRow ())
	{
		disable ("purchaseOrder_button_create");
		var oPurchaseOrderData = purchaseOrder_getFormData ();
		if(document.getElementById("purchaseOrder_button_create").getAttribute("update") == "false")
			PurchaseOrderDataProcessor.create (oPurchaseOrderData, purchaseOrder_created);
		else
		{
			oPurchaseOrderData.m_nPurchaseOrderId = m_oPurchaseOrderMemberData.m_nPurchaseOrderId;
			PurchaseOrderDataProcessor.update(oPurchaseOrderData, purchaseOrder_updated);
		}
	}
}

function purchaseOrder_validate ()
{
	return validateForm("purchaseOrder_form_id") && purchaseOrder_validateSelectField () && !purchaseOrder_validatePONumber ();
}

function purchaseOrder_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	if(!purchaseOrder_isValidClient())
	{
		informUser("Please Select Client Name", "kWarning");
		$('#purchaseOrder_input_clientName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	else if(!purchaseOrder_isValidSite())
	{
		informUser("Please Select a Site", "kWarning");
		$('#purchaseOrder_input_siteName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function purchaseOrder_isValidClient()
{
	var bIsValid = false;
	try
	{
		var strClient = $('#purchaseOrder_input_clientName').combobox('getValue');
		if(strClient != null && strClient > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function purchaseOrder_isValidSite ()
{
	var bIsValid = false;
	try
	{
		var strSite = $('#purchaseOrder_input_siteName').combobox('getValue');
		if(strSite != null && strSite > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}


function purchaseOrder_validateDGRow ()
{
	var bIsValidateDGRow = true;
	purchaseOrder_acceptDGchanges();
	var arrLineItemData = $('#purchaseOrder_table_itemsDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oPurchaseOrderLineItemData = new PurchaseOrderLineItemData ();
		var strDesc =  arrLineItemData [nIndex].m_strDesc;
		var nQuantity = arrLineItemData [nIndex].m_nQty;
		var nPrice = arrLineItemData [nIndex].m_nPrice;
		if (strDesc.trim() !="" && (nQuantity == "" || nQuantity == 0 || nPrice == "" || nPrice == 0))
		{
			bIsValidateDGRow = false;
			informUser (nQuantity == "" || nQuantity == 0 ? nQuantity == "" ? "Quantity cannot be empty." : "Quantity should be greater than 0." :  nPrice == "" ? "Price cannot be empty." : "Price should be greater than 0." );
			break;
		}
	}
	return bIsValidateDGRow;
}

function purchaseOrder_getFormData ()
{
	purchaseOrder_acceptDGchanges();
	var oPurchaseOrderData = new PurchaseOrderData ();
	oPurchaseOrderData.m_oUserCredentialsData = new UserInformationData ();
	oPurchaseOrderData.m_oClientData = new ClientData ();
	oPurchaseOrderData.m_oSiteData = new SiteData ();
	oPurchaseOrderData.m_oContactData = new ContactData ();
	oPurchaseOrderData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseOrderData.m_oClientData.m_nClientId = $('#purchaseOrder_input_clientName').combobox('getValue');
	oPurchaseOrderData.m_oSiteData.m_nSiteId = $('#purchaseOrder_input_siteName').combobox('getValue');
	var strContactId =  $('#purchaseOrder_input_contactName').combobox('getValue');
	if (strContactId > 0)
		oPurchaseOrderData.m_oContactData.m_nContactId = $('#purchaseOrder_input_contactName').combobox('getValue');
	else
		oPurchaseOrderData.m_oContactData = null;
	oPurchaseOrderData.m_strPurchaseOrderNumber = $("#purchaseOrder_input_purchaseOrderNumber").val();
	oPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
//	var m_strDate = $('#purchaseOrder_input_date').datebox('getValue');
	var m_strDate = $('#purchaseOrder_input_date').val();
	oPurchaseOrderData.m_strPurchaseOrderDate = FormatDate (m_strDate);
	oPurchaseOrderData.m_bIsCFormProvided = document.getElementById("purchaseOrder_input_CFormProvided").checked;
	oPurchaseOrderData.m_arrPurchaseOrderLineItems = purchaseOrder_getLineItemDataArray ();
	if(m_oPurchaseOrderMemberData.m_nQuotationId > 0)
	{
		oPurchaseOrderData.m_oQuotationData = new QuotationData ();
		oPurchaseOrderData.m_oQuotationData.m_nQuotationId = m_oPurchaseOrderMemberData.m_nQuotationId;
	}
	else
		oPurchaseOrderData.m_oQuotationData = null;
	return oPurchaseOrderData;
}

function purchaseOrder_getLineItemDataArray ()
{
	var arrPurchaseOrderLineItems = new Array ();
	var arrLineItemData = $('#purchaseOrder_table_itemsDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oPurchaseOrderLineItemData = new PurchaseOrderLineItemData ();
		oPurchaseOrderLineItemData.m_oPurchaseOrderStockLineItems = arrLineItemData [nIndex].m_oPurchaseOrderStockLineItems;
		oPurchaseOrderLineItemData.m_nSerialNumber = $('#purchaseOrder_table_itemsDG').datagrid('getRowIndex',arrLineItemData[nIndex]);
		oPurchaseOrderLineItemData.m_nId = Number((arrLineItemData [nIndex].m_nId) > 0) ? arrLineItemData [nIndex].m_nId : -1;
		oPurchaseOrderLineItemData.m_strArticleNumber = arrLineItemData [nIndex].m_strArticleNumber.trim ();
		oPurchaseOrderLineItemData.m_strDesc =  arrLineItemData [nIndex].m_strDesc;
		oPurchaseOrderLineItemData.m_nQty = arrLineItemData [nIndex].m_nQty;
		oPurchaseOrderLineItemData.m_strUnit = arrLineItemData [nIndex].m_strUnit;
		oPurchaseOrderLineItemData.m_nPrice = arrLineItemData [nIndex].m_nPrice;
		oPurchaseOrderLineItemData.m_nDiscount = arrLineItemData [nIndex].m_nDiscount;
		oPurchaseOrderLineItemData.m_strTaxName = arrLineItemData [nIndex].m_strTaxName;
		oPurchaseOrderLineItemData.m_nTax = arrLineItemData [nIndex].m_nTax;
		oPurchaseOrderLineItemData.m_nShipQty = arrLineItemData [nIndex].m_nShipQty;
		oPurchaseOrderLineItemData.m_nShippedQty = arrLineItemData [nIndex].m_nShippedQty;
		oPurchaseOrderLineItemData.m_nPurchasedQty = (arrLineItemData [nIndex].m_nPurchasedQty != undefined && arrLineItemData [nIndex].m_nPurchasedQty != "") ? arrLineItemData [nIndex].m_nPurchasedQty : 0;
		oPurchaseOrderLineItemData.m_nReturnedQty = (arrLineItemData [nIndex].m_nReturnedQty != undefined && arrLineItemData [nIndex].m_nReturnedQty != "") ? arrLineItemData [nIndex].m_nReturnedQty : 0;
		oPurchaseOrderLineItemData.m_arrPurchaseOrderStockLineItems = purchaseOrder_getStockLineItems (oPurchaseOrderLineItemData);
		if (oPurchaseOrderLineItemData.m_strDesc.trim() && oPurchaseOrderLineItemData.m_nQty > 0 && oPurchaseOrderLineItemData.m_nPrice > 0)
			arrPurchaseOrderLineItems.push (oPurchaseOrderLineItemData);
	}
	return arrPurchaseOrderLineItems;
}

function purchaseOrder_getStockLineItems (oPurchaseOrderLineItemData)
{
	assert.isObject(oPurchaseOrderLineItemData, "oPurchaseOrderLineItemData expected to be an Object.");
	try
	{
		var arrPurchaseOrderStockLineItem = new Array ();
		if (oPurchaseOrderLineItemData.m_strArticleNumber != "")
		{
			var oPurchaseOrderStockLineItemData = new PurchaseOrderStockLineItemData ();
			try
			{
				oPurchaseOrderStockLineItemData.m_nId = oPurchaseOrderLineItemData.m_oPurchaseOrderStockLineItems[0].m_nId;
			}
			catch (oException)
			{
				oPurchaseOrderStockLineItemData.m_nId = -1;
			}
			oPurchaseOrderStockLineItemData.m_oItemData.m_strArticleNumber = oPurchaseOrderLineItemData.m_strArticleNumber;
			oPurchaseOrderStockLineItemData.m_nQuantity = oPurchaseOrderLineItemData.m_nQty;
			arrPurchaseOrderStockLineItem.push (oPurchaseOrderStockLineItemData);
		}
	}
	catch (oException)
	{
		
	}
	return arrPurchaseOrderStockLineItem;
}

function purchaseOrder_created (oResponse)
{
	if (oResponse.m_bSuccess)
		purchaseOrder_displayInformation ("Purchase Order Saved Succesfully.");
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Purchase Order Save Failed.", "kAlert");
	enable ("purchaseOrder_button_create");
}

function purchaseOrder_updated (oResponse)
{
	if(oResponse.m_bSuccess)
		purchaseOrder_deletePurchaseOrderLineItems ();
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Purchase Order Updation Failed.", "kAlert");
	enable ("purchaseOrder_button_create");
}

function purchaseOrder_deletePurchaseOrderLineItems ()
{
	if(m_oPurchaseOrderMemberData.m_arrDeletedLineItems.length > 0)
	{
		var oPurchaseOrderData = purchaseOrder_getFormData ();
		oPurchaseOrderData.m_arrPurchaseOrderLineItems = m_oPurchaseOrderMemberData.m_arrDeletedLineItems;
		PurchaseOrderDataProcessor.deletePurchaseOrderLineItems(oPurchaseOrderData, purchaseOrder_lineItemsDeleted);
	}
	else
		purchaseOrder_displayInformation ("Purchase Order updated successfully.", "kSuccess")
}

function purchaseOrder_lineItemsDeleted (oResponse)
{
	if(oResponse.m_bSuccess)
		purchaseOrder_displayInformation ("Purchase Order updated successfully.")
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Purchase Order updation Failed.", "kAlert");
	enable ("purchaseOrder_button_create");
}

function purchaseOrder_displayInformation (strMessage)
{
	informUser(strMessage, "kSuccess");
	HideDialog ("dialog");
	navigate("purchaseOrderList", "widgets/purchaseordermanagement/purchaseorder/purchaseOrderList.js");
}

function purchaseOrder_executePurchaseOrder ()
{
	purchaseOrder_init ();
	document.getElementById("purchaseOrder_button_create").setAttribute ("update", "true");
	document.getElementById("purchaseOrder_button_saveAndMakeChallan").setAttribute ("update", "true");
	document.getElementById("purchaseOrder_button_saveAndMakeInvoice").setAttribute ("update", "true");
	purchaseOrder_getPurchaseOrderItem ();
}

function purchaseOrder_getPurchaseOrderItem ()
{
	createPopup('ProcessDialog', '', '', true);
	var oPurchaseOrderData = new PurchaseOrderData ();
	oPurchaseOrderData.m_nPurchaseOrderId = m_oPurchaseOrderMemberData.m_nPurchaseOrderId;
	PurchaseOrderDataProcessor.get (oPurchaseOrderData, purchaseOrder_gotPurchaseOrderItem);
}

function purchaseOrder_gotPurchaseOrderItem (oResponse)
{
	HideDialog ("ProcessDialog");
	var oPurchaseOrderItem = oResponse.m_arrPurchaseOrder[0];
	purchaseOrder_populateToCombobox(oPurchaseOrderItem);
	try
	{
		m_oPurchaseOrderMemberData.m_nQuotationId = oPurchaseOrderItem.m_oQuotationData.m_nQuotationId;
		if(m_oPurchaseOrderMemberData.m_nQuotationId > 0)
		{
			document.getElementById ("purchaseOrder_img_add").style.visibility="hidden";
			$('#purchaseOrder_input_clientName').combobox('disable');
		}
	}catch (oException){}
	var nShippedQty = 0;
	$('#purchaseOrder_input_clientName').combobox('select', oPurchaseOrderItem.m_oClientData.m_nClientId);
	$('#purchaseOrder_input_siteName').combobox('select', oPurchaseOrderItem.m_oSiteData.m_nSiteId);
	if(oPurchaseOrderItem.m_oContactData != null)
		$('#purchaseOrder_input_contactName').combobox('select', oPurchaseOrderItem.m_oContactData.m_nContactId);
	$("#purchaseOrder_input_purchaseOrderNumber").val(oPurchaseOrderItem.m_strPurchaseOrderNumber);
	m_oPurchaseOrderMemberData.m_strPONumber = oPurchaseOrderItem.m_strPurchaseOrderNumber;
//	$('#purchaseOrder_input_date').datebox('setValue', oPurchaseOrderItem.m_strPurchaseOrderDate);
	$('#purchaseOrder_input_date').datepicker('setDate', oPurchaseOrderItem.m_strPurchaseOrderDate);
	document.getElementById("purchaseOrder_input_CFormProvided").checked = oPurchaseOrderItem.m_bIsCFormProvided;
	var arrOrderedLineItem = getOrderedLineItems (oPurchaseOrderItem.m_oPurchaseOrderLineItems);
	for (var nIndex = 0; nIndex < arrOrderedLineItem.length; nIndex++)
	{
		nShippedQty += arrOrderedLineItem[nIndex].m_nShippedQty;
		try
		{
			arrOrderedLineItem[nIndex].m_strArticleNumber = 
				arrOrderedLineItem[nIndex].m_oPurchaseOrderStockLineItems[0].m_oItemData.m_strArticleNumber;
			arrOrderedLineItem[nIndex].m_nItemId = arrOrderedLineItem[nIndex].m_oPurchaseOrderStockLineItems[0].m_oItemData.m_nItemId;
		}
		catch (oException)
		{
			
		}
		$('#purchaseOrder_table_itemsDG').datagrid('appendRow',arrOrderedLineItem[nIndex]);
	}
	if(nShippedQty > 0)
	{
		m_oPurchaseOrderMemberData.m_bDisableInvoiceButton = false;
		purchaseOrder_displayButtons (false, true, false);
		$("#purchaseOrder_input_purchaseOrderNumber").attr('readonly', 'readonly');
	}
	purchaseOrder_appendRow ();
	initFormValidateBoxes ("purchaseOrder_form_id");
}

function purchaseOrder_populateToCombobox(oPurchaseOrderData)
{
	assert.isObject(oPurchaseOrderData, "oPurchaseOrderData expected to be an Object.");
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = oPurchaseOrderData.m_oClientData.m_strCompanyName;
	ClientDataProcessor.getClientSuggesstions (oClientData, "", "", function(oResponse)
			{
				var arrClientInfo = new Array ();
				for(var nIndex=0; nIndex< oResponse.m_arrClientData.length; nIndex++)
			    {
					arrClientInfo.push(oResponse.m_arrClientData[nIndex]);
					arrClientInfo[nIndex].m_strTo = encodeURIComponent(oResponse.m_arrClientData[nIndex].m_strCompanyName + " "+"|" +" "+
			    											oResponse.m_arrClientData[nIndex].m_strTinNumber);
			    }
				$('#purchaseOrder_input_clientName').combobox('loadData',arrClientInfo)
			});

}

function purchaseOrder_saveAndMakeChallan ()
{
	if (purchaseOrder_validate () && purchaseOrder_validateDGRow ())
		loadPage ("include/process.html", "ProcessDialog", "purchaseOrder_saveAndMakeChallan_progressbarLoaded ()");
}

function purchaseOrder_saveAndMakeChallan_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("purchaseOrder_button_saveAndMakeChallan");
	var oPurchaseOrderData = purchaseOrder_getFormData ();
	if(document.getElementById("purchaseOrder_button_saveAndMakeChallan").getAttribute("update") == "false")
		POChallanDataProcessor.createChallan (oPurchaseOrderData, purchaseOrder_savedForChallan);
	else
	{
		oPurchaseOrderData.m_nPurchaseOrderId = m_oPurchaseOrderMemberData.m_nPurchaseOrderId;
		POChallanDataProcessor.updateAndMakeChallan (oPurchaseOrderData, purchaseOrder_savedForChallan);
	}
}
function purchaseOrder_savedForChallan (oResponse)
{
	HideDialog ("ProcessDialog");
	enable ("purchaseOrder_button_saveAndMakeChallan");
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		var oChallanData = oResponse.m_arrChallan[0];
		m_oPurchaseOrderMemberData.m_strXMLData = oResponse.m_strXMLData;
		m_oPurchaseOrderMemberData.m_nChallanId = oChallanData.m_nChallanId;
		m_oPurchaseOrderMemberData.m_nInvoiceId = oChallanData.m_oInvoiceData != null ? oChallanData.m_oInvoiceData.m_nInvoiceId : -1;
		navigate ('challan','widgets/purchaseordermanagement/purchaseorder/challanPO.js')
		try
		{
			purchaseOrder_handleAfterSave ();
		}
		catch(oException)
		{
			
		}
	}
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Purchase Order Challan Failed to Generate.", "kAlert");
}

function purchaseOrder_saveAndMakeInvoice ()
{
	if (purchaseOrder_validate () && purchaseOrder_validateDGRow ())
		loadPage ("include/process.html", "ProcessDialog", "purchaseOrder_progressbarSaveAndMakeInvoice ()");
}

function purchaseOrder_progressbarSaveAndMakeInvoice ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("purchaseOrder_button_saveAndMakeInvoice");
	var oPurchaseOrderData = purchaseOrder_getFormData ();
	if(document.getElementById("purchaseOrder_button_saveAndMakeInvoice").getAttribute("update") == "false")
		POInvoiceDataProcessor.createInvoice (oPurchaseOrderData, purchaseOrder_savedForInvoice);
	else
		purchaseOrder_getUnbilledChallans ();
}

function purchaseOrder_getUnbilledChallans ()
{
	var oPurchaseOrderData = new PurchaseOrderData ();
	oPurchaseOrderData.m_nPurchaseOrderId = m_oPurchaseOrderMemberData.m_nPurchaseOrderId;
	POChallanDataProcessor.getUnbilledChallans (oPurchaseOrderData, purchaseOrder_gotUnbilledChallans);
}

function purchaseOrder_gotUnbilledChallans (oResponse)
{
	if (oResponse.m_arrChallanData.length <= 0)
		POChallanList_handleMakeBill (new Array ());
	else
		navigate ('invoice','widgets/purchaseordermanagement/purchaseorder/POMakeInvoice.js');
}

function purchaseOrder_savedForInvoice (oResponse)
{
	HideDialog ("ProcessDialog");
	enable ("purchaseOrder_button_saveAndMakeInvoice");
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		m_oPurchaseOrderMemberData.m_strXMLData = oResponse.m_strXMLData;
		m_oPurchaseOrderMemberData.m_oInvoiceData = oResponse.m_arrInvoice[0];
		navigate ('invoice','widgets/purchaseordermanagement/purchaseorder/invoicePO.js');
		try
		{
			purchaseOrder_handleAfterSave ();
		}
		catch(oException)
		{
			
		}
	}
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Purchase Order Invoice Failed to Generate.", "kAlert");
}

function POChallanList_handleMakeBill (arrSelectedChallans)
{
	var oPurchaseOrderData = purchaseOrder_getFormData ();
	oPurchaseOrderData.m_arrChallans = purchaseOrder_buildChallanArray (arrSelectedChallans);
	if(document.getElementById("purchaseOrder_button_saveAndMakeChallan").getAttribute("update") == "false")
		POInvoiceDataProcessor.createInvoice (oPurchaseOrderData, purchaseOrder_savedForInvoice);
	else
	{
		oPurchaseOrderData.m_nPurchaseOrderId = m_oPurchaseOrderMemberData.m_nPurchaseOrderId;
		POInvoiceDataProcessor.updateAndMakeInvoice (oPurchaseOrderData, purchaseOrder_savedForInvoice);
	}
}

function purchaseOrder_buildChallanArray (arrSelectedChallans)
{
	assert.isArray(arrSelectedChallans, "arrSelectedChallans expected to be an Array.");
	var arrChallans = new Array ();
	for (var nIndex = 0; nIndex < arrSelectedChallans.length; nIndex++)
	{
		var oChallanData = new ChallanData ();
		oChallanData.m_nChallanId = arrSelectedChallans [nIndex].m_nChallanId;
		arrChallans.push (oChallanData);
	}
	return arrChallans;
}

function purchaseOrder_addClientSites ()
{
	m_oPurchaseOrderMemberData.m_bIsForSite = true;
	navigate ("addSites", "widgets/clientmanagement/addSites.js");
}

function purchaseOrder_addClientContacts ()
{
	m_oPurchaseOrderMemberData.m_bIsForSite = false;
	navigate ("addContacts", "widgets/clientmanagement/addContacts.js");
}

function clientInfo_handleAfterUpdate ()
{ 
	purchaseOrder_getClientInfo ();
}

function purchaseOrder_getClientInfo ()
{
	var oClientData = new ClientData ();
	oClientData.m_nClientId = m_oTrademustMemberData.m_nSelectedClientId;
	ClientDataProcessor.get (oClientData, purchaseOrder_gotClientInfo);
}

function purchaseOrder_gotClientInfo(oResponse)
{
	if(oResponse.m_arrClientData.length > 0)
	{
		if(m_oPurchaseOrderMemberData.m_bIsForSite)
			purchaseOrder_initSiteCombobox (purchaseOrder_getActiveSites(oResponse.m_arrClientData[0].m_oSites));
		else
			purchaseOrder_initContactsCombobox(oResponse.m_arrClientData[0].m_oContacts);
	}
}
