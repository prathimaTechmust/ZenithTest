var returnedItems_includeDataObjects = 
[
	'widgets/inventorymanagement/returneditems/ReturnedData.js',
	'widgets/inventorymanagement/returneditems/ReturnedLineItemData.js',
	'widgets/inventorymanagement/returneditems/NonStockReturnedLineItemData.js',
	'widgets/purchaseordermanagement/supply/NonStockSupplyItemsData.js',
	'widgets/purchaseordermanagement/supply/SupplyData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js'
];


includeDataObjects (returnedItems_includeDataObjects, "returnedItems_loaded()");

function returnedItems_memberData ()
{
	this.m_oClientDetails = new ClientData ();
	this.m_nEditIndex = undefined;
	this.m_nReturnedId = -1;
	this.m_strXMLData = "";
}
var m_oReturnedItemsMemberData = new returnedItems_memberData ();

function returnedItems_initAdmin ()
{
	document.getElementById ("returnedItems_img_add").style.visibility="visible";
	returnedItems_new ();
}

function returnedItemsList_initUser ()
{
	returnedItems_new ();
}

function returnedItems_new ()
{
	returnedItems_init ();
	initFormValidateBoxes ("returnedItems_form_id");
}

function returnedItems_edit ()
{
	createPopup('ProcessDialog', '', '', true);
	document.getElementById ("returnedItems_img_add").style.visibility="visible";
	returnedItems_init ();
	document.getElementById("returnedItems_button_save").setAttribute('update', true);
	document.getElementById("returnedItems_button_save").innerHTML = "Update";
	document.getElementById("returnedItems_button_saveAndPrint").setAttribute('update', true);
	document.getElementById("returnedItems_button_saveAndPrint").innerHTML = "Update & Print";
	var oReturnedData = new ReturnedData ();
	oReturnedData.m_nId = m_oReturnedItemsMemberData.m_nReturnedId;
	ReturnedDataProcessor.get (oReturnedData, returnedItems_gotData);
}

function returnedItems_gotData (oResponse)
{
	HideDialog ("ProcessDialog");
	var oReturnedData = oResponse.m_arrReturnedData[0];
	returnedItems_populateClientCombobox(oReturnedData);
	$('#returnedItems_input_clientName').combobox('select', oReturnedData.m_oClientData.m_nClientId);
	$("#returnedItems_input_creditNoteNumber").val(oReturnedData.m_strCreditNoteNumber);
	var arrReturns = returnedItems_buildReturnedItemsArray (oResponse.m_arrReturnedData[0]);
	$('#returnedItems_table_itemDG').datagrid('loadData', arrReturns);
	returnedItems_loadFooterDG ();
}

function returnedItems_buildReturnedItemsArray (oReturnedData)
{
	assert.isObject(oReturnedData, "oReturnedData expected to be an Object.");
	var arrReturns = new Array ();
	for(var nIndex=0; nIndex < oReturnedData.m_oReturnedLineItems.length; nIndex++)
	{
		var oReturnedLineItem = new ReturnedLineItem ();
		oReturnedLineItem.m_nRetunedId = oReturnedData.m_oReturnedLineItems[nIndex].m_nId;
		oReturnedLineItem.m_nLineItemId = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_nLineItemId;
		oReturnedLineItem.m_strArticleNumber = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_oItemData.m_strArticleNumber;
		oReturnedLineItem.m_strArticleDescription = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_oItemData.m_strItemName;
		oReturnedLineItem.m_nQuantity = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_nQuantity;
		oReturnedLineItem.m_nReturnedQty = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_nReturnedQuantity - oReturnedData.m_oReturnedLineItems[nIndex].m_nQuantity;
		oReturnedLineItem.m_nReturnQty = oReturnedData.m_oReturnedLineItems[nIndex].m_nQuantity;
		oReturnedLineItem.m_nPrice = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_nPrice;
		oReturnedLineItem.m_nDiscount = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_nDiscount;
		oReturnedLineItem.m_nTax = oReturnedData.m_oReturnedLineItems[nIndex].m_oSalesLineItemData.m_nTax;
		oReturnedLineItem.m_nReturnValue = returnedItems_setReturnAmount (oReturnedLineItem);
		arrReturns.push(oReturnedLineItem);
	}
	for(var nIndex=0; nIndex < oReturnedData.m_oNonStockReturnedLineItems.length; nIndex++)
	{
		var oReturnedLineItem = new ReturnedLineItem ();
		oReturnedLineItem.m_nRetunedId = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_nId;
		oReturnedLineItem.m_nLineItemId = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_nLineItemId;
		oReturnedLineItem.m_strArticleNumber = "";
		oReturnedLineItem.m_strArticleDescription = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_strArticleDescription;
		oReturnedLineItem.m_nQuantity = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_nQuantity;
		oReturnedLineItem.m_nReturnedQty = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_nReturnedQuantity - oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_nQuantity;
		oReturnedLineItem.m_nReturnQty = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_nQuantity;
		oReturnedLineItem.m_nPrice = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_nPrice;
		oReturnedLineItem.m_nDiscount = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_nDiscount;
		oReturnedLineItem.m_nTax = oReturnedData.m_oNonStockReturnedLineItems[nIndex].m_oNonStockSalesLineItemData.m_nTax;
		oReturnedLineItem.m_nReturnValue = returnedItems_setReturnAmount (oReturnedLineItem);
		arrReturns.push(oReturnedLineItem);
	}
	return arrReturns;
}

function returnedItems_setReturnAmount (oReturnedLineItem)
{
	assert.isObject(oReturnedLineItem, "oReturnedLineItem expected to be an Object.");
	assert( Object.keys(oReturnedLineItem).length >0 , "oReturnedLineItem cannot be an empty .");// checks for non emptyness 
	var nDiscountPrice = oReturnedLineItem.m_nPrice * oReturnedLineItem.m_nDiscount/100;
	var nTaxPrice = oReturnedLineItem.m_nPrice * oReturnedLineItem.m_nTax/100;
	var nTotalPrice = (oReturnedLineItem.m_nPrice + nTaxPrice - nDiscountPrice) * oReturnedLineItem.m_nReturnQty;
	return nTotalPrice.toFixed (2);
}

function returnedItems_init ()
{
	createPopup("dialog", "#returnedItems_button_save", "#returnedItems_button_cancel", true);
	returnedItems_initializeDataGrid ();
	returnedItems_initToCombobox ()
}

function returnedItems_cancel ()
{
	HideDialog ("dialog");
}

function returnedItems_initializeDataGrid ()
{
	$('#returnedItems_table_itemDG').datagrid ({
	    columns:[[  
	        {field:'m_strArticleNumber',title:'Article#', width:100},
	        {field:'m_strArticleDescription',title:'Name',width:200},
	        {field:'m_nQuantity',title:'Shipped Qty',width:80,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}},
	        {field:'m_nPrice',title:'Price',width:80,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}},
	        {field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',editor:{'type':'numberbox','options':{disabled:true}}},
	        {field:'m_nTax',title:'Tax(%)',width:60,align:'right',editor:{'type':'numberbox','options':{disabled:true}}},
	        {field:'m_nReturnedQty',title:'Returned Qty',width:80,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}}, 
	        {field:'m_nReturnQty',title:'Return Qty',width:80,align:'right',editor:{'type':'text'}},
	        {field:'m_nReturnValue',title:'Amount',width:80,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}},
	        {field:'Actions',title:'Actions',align:'center',width:50,
				formatter:function(value,row,index)
				{
	        		if(row.m_nReturnQty == "<b>Total</b>" && index == 0)
	        			return returnedItems_removeAction ();
	        		else
	        			return returnedItems_showDeleteImage (row, index);
				}
			}
	    ]],
	    onClickRow: function (rowIndex, rowData)
		{
			returnedItems_editRowDG (rowData, rowIndex);
		}
	});
}

function returnedItems_showDeleteImage (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td> <img title="Delete" src="images/delete.png" width="20"  id="deleteImageId" onClick="returnedItems_delete('+nIndex+')"/> </td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function returnedItems_removeAction ()
{
	var oImage ='<table align="center">'+
	'<tr>'+
	'<td></td>'+
	'</tr>'+
	'</table>'
	return oImage;
}

function returnedItems_delete (nIndex)
{
	$('#returnedItems_table_itemDG').datagrid ('deleteRow', nIndex);
	$('#returnedItems_table_itemDG').datagrid('acceptChanges');
	refreshDataGrid ('#returnedItems_table_itemDG');
	returnedItems_loadFooterDG ();
}

function returnedItems_initToCombobox ()
{
	$('#returnedItems_input_clientName').combobox
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
    		if(row.m_nClientId != m_oTrademustMemberData.m_nSelectedClientId)
	    		clearGridData ("#returnedItems_table_itemDG");
    		m_oReturnedItemsMemberData.m_oClientDetails = row;
    		returnedItems_setClientInfo(row);
	    },
	    onChange:function(row)
	    {
	    	returnedItems_showButtons ();
	    }
	});
	var toTextBox = $('#returnedItems_input_clientName').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Client Name";
	toTextBox.bind('keydown', function (e)
		    {
				returnedItems_handleKeyboardNavigation (e);
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

function returnedItems_populateClientCombobox(oSalesData)
{
	assert.isObject(oSalesData, "oSalesData expected to be an Object.");
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = oSalesData.m_oClientData.m_strCompanyName;
	ClientDataProcessor.getClientSuggesstions (oClientData, "", "","","", function(oResponse)
			{
				var arrClientInfo = new Array ();
				for(var nIndex=0; nIndex< oResponse.m_arrClientData.length; nIndex++)
			    {
					arrClientInfo.push(oResponse.m_arrClientData[nIndex]);
					arrClientInfo[nIndex].m_strTo = encodeURIComponent(oResponse.m_arrClientData[nIndex].m_strCompanyName + " "+"|" +" "+
			    											oResponse.m_arrClientData[nIndex].m_strTinNumber);
			    }
				$('#returnedItems_input_clientName').combobox('loadData',arrClientInfo)
			});
}

function returnedItems_handleKeyboardNavigation (oEvent)
{
	assert.isObject(oEvent, "oEvent expected to be an Object.");
	assert( Object.keys(oEvent).length >0 , "oEvent cannot be an empty .");// checks for non emptyness 
	if(oEvent.keyCode == 13)
	{
		returnedItems_setClientInfo(m_oReturnedItemsMemberData.m_oClientDetails);
	}
}

function returnedItems_setClientInfo(oClientData)
{
	assert.isObject(oClientData, "oClientData expected to be an Object.");
	m_oTrademustMemberData.m_nSelectedClientId = oClientData.m_nClientId;
	$("#returnedItems_input_clientName").combobox('setText',oClientData.m_strCompanyName);
	returnedItems_showButtons ();
}

function returnedItems_showButtons ()
{
	if($('#returnedItems_input_clientName').combobox('getValue') > 0)
		returnedItems_displayButtons (false);
	else
		returnedItems_displayButtons (true);
}

function returnedItems_displayButtons (bSelectSalesButtonDisabled)
{
	assert.isBoolean(bSelectSalesButtonDisabled, "bSelectSalesButtonDisabled should be a boolean value");
	var oSelectSalesButton = document.getElementById ("returnedItems_button_selectSales");
	oSelectSalesButton.disabled = bSelectSalesButtonDisabled;
	bSelectSalesButtonDisabled ? oSelectSalesButton.style.backgroundColor = "#c0c0c0" :  oSelectSalesButton.style.backgroundColor = "#0E486E";
}

function returnedItems_editRowDG (rowData, rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
//	assert(rowIndex !== 0, "rowIndex cannot be equal to zero.");
//	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	 if (m_oReturnedItemsMemberData.m_nEditIndex != rowIndex)
	    {
	        if (returnedItems_endEdit ())
	        {
	        	m_oReturnedItemsMemberData.m_nEditIndex = rowIndex;
	            $('#returnedItems_table_itemDG').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
	            returnedItems_setEditing(m_oReturnedItemsMemberData.m_nEditIndex);
	        } 
	        else 
	            $('#returnedItems_table_itemDG').datagrid('selectRow', m_oReturnedItemsMemberData.m_nEditIndex);
	    }
}

function returnedItems_endEdit ()
{
    if (m_oReturnedItemsMemberData.m_nEditIndex == undefined)
    	return true
    if ($('#returnedItems_table_itemDG').datagrid('validateRow', m_oReturnedItemsMemberData.m_nEditIndex))
    {
        $('#returnedItems_table_itemDG').datagrid('endEdit',  m_oReturnedItemsMemberData.m_nEditIndex);
        m_oReturnedItemsMemberData.m_nEditIndex = undefined;
        return true;
    } 
    else 
        return false;
}

function returnedItems_setEditing(rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	assert.isOk(rowIndex > -1, "rowIndex must be a positive value.");
    var editors = $('#returnedItems_table_itemDG').datagrid('getEditors', rowIndex);
    var shippedQtyEditor = editors[0];
    var priceEditor = editors[1];
    var discountEditor = editors[2];
    var taxEditor = editors[3]
    var returnedItemsEditor = editors[4];
    var returnItemsEditor = editors[5];
    var amountEditor = editors[6];
    returnItemsEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
		    });
    returnItemsEditor.target.bind('change', function()
    		{
    			returnedItems_validateShipValue (this.value);
    			returnedItems_calculateAmount (this.value);
    		});
    returnItemsEditor.target.bind('blur', function()
    		{
    			returnedItems_validateShipValue (this.value);
    			returnedItems_calculateAmount (this.value);
    		});
    
    function returnedItems_validateShipValue (nValue)
    {
    	assert.isString(nValue, "nValue expected to be a string.");
    	var nBalanceQty = Number(shippedQtyEditor.target.val()) - Number(returnedItemsEditor.target.val());
    	if(Number(nValue) > nBalanceQty)
    	{
    		informUser ("Returned items quantity cannot exceed " + nBalanceQty + ".", "kWarning");
    		$(returnItemsEditor.target).val('');
    		$(returnItemsEditor.target).focus();
    	}
    }
    
    function returnedItems_calculateAmount (nReturnQty)
    {
    	assert.isString (nReturnQty, "nReturnQty expected to be a String.");
    	var nDiscountPrice = $(priceEditor.target).numberbox('getValue') * $(discountEditor.target).numberbox('getValue')/100;
    	var nTaxPrice = $(priceEditor.target).numberbox('getValue') * $(taxEditor.target).numberbox('getValue')/100;
    	var nPrice = Number($(priceEditor.target).numberbox('getValue'));
    	var nTotalPrice = (nPrice + nTaxPrice - nDiscountPrice) * nReturnQty;
    	$(amountEditor.target).numberbox('setValue',nTotalPrice);
    	returnedItems_loadFooterDG ();
    }
}

function returnedItems_loadFooterDG ()
{
	returnedItems_acceptDGchanges ();
	var arrReturnLineItem = $('#returnedItems_table_itemDG').datagrid('getRows');
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrReturnLineItem.length; nIndex++)
	{
		if(Number(arrReturnLineItem [nIndex].m_nReturnValue) > 0)
			nTotal = Number(nTotal) + Number(arrReturnLineItem [nIndex].m_nReturnValue);
	}
	nTotal = nTotal.toFixed(2);
	$('#returnedItems_table_itemDG').datagrid('reloadFooter',[{m_nReturnQty:'<b>Total</b>', m_nReturnValue:'<span class="rupeeSign">R </span>' + nTotal}]);
}

function returnedItems_selectSales ()
{
	m_oReturnedItemsMemberData.m_nEditIndex = undefined;
	navigate ("", "widgets/inventorymanagement/returneditems/clientSalesList.js");
}

function returnedItems_cancel ()
{
	HideDialog("dialog");
}

function returnedItems_submit ()
{
	if (returnedItems_validate () && returnedItems_validateDGRow ())
		loadPage ("include/process.html", "ProcessDialog", "returnedItems_submit_progressbarLoaded ()");
}

function returnedItems_submit_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	var oReturnedData = returnedItems_getFormData ();
	if((document.getElementById("returnedItems_button_save").getAttribute('update') == "false"))
		ReturnedDataProcessor.create (oReturnedData, returnedItems_created);
	else
	{
		oReturnedData.m_nId = m_oReturnedItemsMemberData.m_nReturnedId;
		ReturnedDataProcessor.update (oReturnedData, returnedItems_updated);
	}
}
function returnedItems_saveAndPrint ()
{
	if (returnedItems_validate () && returnedItems_validateDGRow ())
		loadPage ("include/process.html", "ProcessDialog", "returnedItems_saveAndPrint_progressbarLoaded ()");
}

function returnedItems_saveAndPrint_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("returnedItems_button_saveAndPrint");
	var oReturnedData = returnedItems_getFormData ();
	if(document.getElementById("returnedItems_button_saveAndPrint").getAttribute('update') == "false")
		ReturnedDataProcessor.saveAndPrint (oReturnedData, returnedItems_savedForPrint );
	else
	{
		oReturnedData.m_nId = m_oReturnedItemsMemberData.m_nReturnedId;
		ReturnedDataProcessor.updateAndPrint(oReturnedData, returnedItems_savedForPrint);
	}
}
function returnedItems_savedForPrint (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		m_oReturnedItemsMemberData.m_strXMLData = oResponse.m_strXMLData;
		navigate ('Print Return','widgets/inventorymanagement/returneditems/returnedItemsPrint.js');
	}
	else
	{
		informUser ("Returned Items Print Failed", "kError");
		enable ("returnedItems_button_saveAndPrint");
	}
}

function returnedItems_validate ()
{
	return validateForm("returnedItems_form_id") && returnedItems_validateSelectField ();
}

function returnedItems_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	if(!returnedItems_isValidClient())
	{
		informUser("Please Select Client Name", "kWarning");
		$('#returnedItems_input_clientName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function returnedItems_isValidClient()
{
	var bIsValid = false;
	try
	{
		var strClient = $('#returnedItems_input_clientName').combobox('getValue');
		if(strClient != null && strClient > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function returnedItems_validateDGRow ()
{
	var bIsValidateDGRow = true;
	returnedItems_acceptDGchanges();
	var arrLineItemData = $('#returnedItems_table_itemDG').datagrid('getRows');
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var nReturnQuantity = arrLineItemData [nIndex].m_nReturnQty;
		if (nReturnQuantity == "" || Number(nReturnQuantity == 0))
		{
			bIsValidateDGRow = false;
			informUser(nReturnQuantity == "" ?  "Return Quantity cannot be empty." : "Return Quantity cannot be zero.", "kWarning");
			break;
		}
	}
	return bIsValidateDGRow;
}

function returnedItems_acceptDGchanges()
{
	if (returnedItems_endEdit())
        $('#returnedItems_table_itemDG').datagrid('acceptChanges');
}

function returnedItems_getFormData ()
{
	returnedItems_acceptDGchanges();
	var oReturnedData = new ReturnedData ();
	oReturnedData.m_oClientData = new ClientData ();
	oReturnedData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReturnedData.m_oClientData.m_nClientId = $('#returnedItems_input_clientName').combobox('getValue');
	oReturnedData.m_strCreditNoteNumber = $("#returnedItems_input_creditNoteNumber").val ();
	returnedItems_buildLineItemArray (oReturnedData);
	return oReturnedData;
}

function returnedItems_buildLineItemArray (oReturnedData)
{
	assert.isObject(oReturnedData, "oReturnedData expected to be an Object.");
	var arrLineItemData = $('#returnedItems_table_itemDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var strArticleNumber = arrLineItemData [nIndex].m_strArticleNumber;
		if(strArticleNumber != undefined && strArticleNumber != "")
		{
			var oReturnedLineItemData = new ReturnedLineItemData ();
			var nId = arrLineItemData [nIndex].m_nRetunedId;
			oReturnedLineItemData.m_nId = nId > 0 ? nId : -1;
			oReturnedLineItemData.m_oSalesLineItemData.m_nLineItemId =  arrLineItemData [nIndex].m_nLineItemId;
			oReturnedLineItemData.m_nQuantity = Number (arrLineItemData [nIndex].m_nReturnQty);
			oReturnedData.m_arrReturnedLineItemData.push(oReturnedLineItemData);
		}
		else
		{
			var oNonStockReturnedLineItemData = new NonStockReturnedLineItemData ();
			var nId = arrLineItemData [nIndex].m_nRetunedId;
			oNonStockReturnedLineItemData.m_nId = nId > 0 ? nId : -1;
			oNonStockReturnedLineItemData.m_oNonStockSalesLineItemData.m_nLineItemId =  arrLineItemData [nIndex].m_nLineItemId;
			oNonStockReturnedLineItemData.m_nQuantity = Number (arrLineItemData [nIndex].m_nReturnQty);
			oReturnedData.m_arrNonStockReturnedLineItemData.push(oNonStockReturnedLineItemData);
		}
	}
}

function returnedItems_created (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser("Returned items saved successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate ("returnedList", "widgets/inventorymanagement/returneditems/returnedListAdmin.js")
	}
	else
		informUser("Save Failed", "kError");
}

function returnedItems_updated (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser("Returned items updated successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate ("returnedList", "widgets/inventorymanagement/returneditems/returnedListAdmin.js")
	}
	else
		informUser("Updation Failed", "kError");
}

function returnedItems_addClient ()
{
	navigate ("clientInfo", "widgets/clientmanagement/addClient.js");
}

function clientInfo_handleAfterSave ()
{
	// Handler Function for Client Save.
}