var purchaseReturnedItems_includeDataObjects = 
[
	'widgets/inventorymanagement/purchasereturneditems/PurchaseReturnedData.js',
	'widgets/inventorymanagement/purchasereturneditems/PurchaseReturnedLineItemData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js'
];

includeDataObjects (purchaseReturnedItems_includeDataObjects, "purchaseReturnedItems_loaded()");

function purchaseReturnedItems_memberData ()
{
	this.m_oVendorDetails = new VendorData ();
	this.m_nEditIndex = undefined;
	this.m_nPurchaseReturnedId = -1;
	this.m_strXMLData = "";
}
var m_oPurchaseReturnedItemsMemberData = new purchaseReturnedItems_memberData ();

function purchaseReturnedItems_initAdmin ()
{
	document.getElementById ("purchaseReturnedItems_img_add").style.visibility="visible";
	purchaseReturnedItems_new ();
}

function returnedItemsList_initUser ()
{
	purchaseReturnedItems_new ();
}

function purchaseReturnedItems_new ()
{
	purchaseReturnedItems_init ();
	initFormValidateBoxes ("purchaseReturnedItems_form_id");
}

function purchaseReturnedItems_edit ()
{
	document.getElementById ("purchaseReturnedItems_img_add").style.visibility="visible";
	purchaseReturnedItems_init ();
	document.getElementById("purchaseReturnedItems_button_save").setAttribute('update', true);
	document.getElementById("purchaseReturnedItems_button_save").innerHTML = "Update";
	document.getElementById("purchaseReturnedItems_button_saveAndPrint").setAttribute('update', true);
	document.getElementById("purchaseReturnedItems_button_saveAndPrint").innerHTML = "Update & Print";
	var oPurchaseReturnedData = new PurchaseReturnedData ();
	oPurchaseReturnedData.m_nId = m_oPurchaseReturnedItemsMemberData.m_nPurchaseReturnedId;
	PurchaseReturnedDataProcessor.get (oPurchaseReturnedData, purchaseReturnedItems_gotData);
}

function purchaseReturnedItems_gotData (oResponse)
{
	var oPurchaseReturnedData = oResponse.m_arrPurchaseReturnedData[0];
	purchaseReturnedItems_populateVendorCombobox(oPurchaseReturnedData);
	$('#purchaseReturnedItems_input_vendorName').combobox('select', oPurchaseReturnedData.m_oVendorData.m_nClientId);
	$("#purchaseReturnedItems_input_debitNoteNumber").val(oPurchaseReturnedData.m_strDebitNoteNumber);
	var arrPurchaseReturns = purchaseReturnedItems_buildReturnedItemsArray (oResponse.m_arrPurchaseReturnedData[0]);
	$('#purchaseReturnedItems_table_itemDG').datagrid('loadData', arrPurchaseReturns);
	purchaseReturnedItems_loadFooterDG ();
}

function purchaseReturnedItems_buildReturnedItemsArray (oPurchaseReturnedData)
{
	assert.isObject(oPurchaseReturnedData, "oPurchaseReturnedData expected to be an Object.");
	assert( Object.keys(oPurchaseReturnedData).length >0 , "oPurchaseReturnedData cannot be an empty .");// checks for non emptyness 
	var arrPurchaseReturns = new Array ();
	for(var nIndex=0; nIndex < oPurchaseReturnedData.m_oPurchaseReturnedLineItemData.length; nIndex++)
	{
		var oPurchaseReturnedLineItem = new PurchaseReturnedLineItemData ();
		var oLineItem = oPurchaseReturnedData.m_oPurchaseReturnedLineItemData[nIndex];
		oPurchaseReturnedLineItem.m_nRetunedId = oLineItem.m_nId;
		oPurchaseReturnedLineItem.m_nLineItemId = oLineItem.m_oPurchaseLineItem.m_nLineItemId;
		oPurchaseReturnedLineItem.m_strArticleNumber = oLineItem.m_oPurchaseLineItem.m_oItemData.m_strArticleNumber;
		oPurchaseReturnedLineItem.m_strArticleDescription = oLineItem.m_oPurchaseLineItem.m_oItemData.m_strItemName;
		oPurchaseReturnedLineItem.m_nQuantity = oLineItem.m_oPurchaseLineItem.m_nQuantity;
		var nReturnedQuantity = oLineItem.m_oPurchaseLineItem.m_nReturnedQuantity > 0 ? (oLineItem.m_oPurchaseLineItem.m_nReturnedQuantity - oLineItem.m_nQuantity) : 0;
		oPurchaseReturnedLineItem.m_nReturnedQuantity = nReturnedQuantity;
		oPurchaseReturnedLineItem.m_nReturnQuantity = oLineItem.m_nQuantity;
		oPurchaseReturnedLineItem.m_nPrice = oLineItem.m_oPurchaseLineItem.m_nPrice;
		oPurchaseReturnedLineItem.m_nDiscount = oLineItem.m_oPurchaseLineItem.m_nDiscount;
		oPurchaseReturnedLineItem.m_nTax = oLineItem.m_oPurchaseLineItem.m_nTax;
		oPurchaseReturnedLineItem.m_nReturnValue = purchaseReturnedItems_setReturnAmount (oPurchaseReturnedLineItem);
		arrPurchaseReturns.push(oPurchaseReturnedLineItem);
	}
	return arrPurchaseReturns;
}

function purchaseReturnedItems_setReturnAmount (oPurchaseReturnedLineItem)
{
	assert.isObject(oPurchaseReturnedLineItem, "oPurchaseReturnedLineItem expected to be an Object.");
	assert( Object.keys(oPurchaseReturnedLineItem).length >0 , "oPurchaseReturnedLineItem cannot be an empty .");// checks for non emptyness 
	var nDiscountPrice = oPurchaseReturnedLineItem.m_nPrice * oPurchaseReturnedLineItem.m_nDiscount/100;
	var nTaxPrice = oPurchaseReturnedLineItem.m_nPrice * oPurchaseReturnedLineItem.m_nTax/100;
	var nTotalPrice = (oPurchaseReturnedLineItem.m_nPrice + nTaxPrice - nDiscountPrice) * oPurchaseReturnedLineItem.m_nReturnQuantity;
	return nTotalPrice.toFixed (2);
}

function purchaseReturnedItems_init ()
{
	createPopup("dialog", "#purchaseReturnedItems_button_save", "#purchaseReturnedItems_button_cancel", true);
	purchaseReturnedItems_initializeDataGrid ();
	purchaseReturnedItems_initToCombobox ()
}

function purchaseReturnedItems_cancel ()
{
	HideDialog ("dialog");
}

function purchaseReturnedItems_initializeDataGrid ()
{
	$('#purchaseReturnedItems_table_itemDG').datagrid ({
	    columns:[[  
	        {field:'m_strArticleNumber',title:'Article#', width:100},
	        {field:'m_strArticleDescription',title:'Name',width:200},
	        {field:'m_nQuantity',title:'Received Qty',width:80,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}},
	        {field:'m_nPrice',title:'Price',width:80,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}},
	        {field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',editor:{'type':'numberbox','options':{disabled:true}}},
	        {field:'m_nTax',title:'Tax(%)',width:60,align:'right',editor:{'type':'numberbox','options':{disabled:true}}},
	        {field:'m_nReturnedQuantity',title:'Returned Qty',width:80,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}}, 
	        {field:'m_nReturnQuantity',title:'Return Qty',width:80,align:'right',editor:{'type':'text'}},
	        {field:'m_nReturnValue',title:'Amount',width:80,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}},
	        {field:'Actions',title:'Actions',align:'center',width:50,
				formatter:function(value,row,index)
				{
	        		if(row.m_nReturnQuantity == "<b>Total</b>" && index == 0)
	        			return purchaseReturnedItems_removeAction ();
	        		else
	        			return purchaseReturnedItems_showDeleteImage (row, index);
				}
			}
	    ]],
	    onClickRow: function (rowIndex, rowData)
		{
			purchaseReturnedItems_editRowDG (rowData, rowIndex);
		}
	});
}

function purchaseReturnedItems_showDeleteImage (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td> <img title="Delete" src="images/delete.png" width="20"  id="deleteImageId" onClick="purchaseReturnedItems_delete('+nIndex+')"/> </td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function purchaseReturnedItems_removeAction ()
{
	var oImage ='<table align="center">'+
	'<tr>'+
	'<td></td>'+
	'</tr>'+
	'</table>'
	return oImage;
}

function purchaseReturnedItems_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	$('#purchaseReturnedItems_table_itemDG').datagrid ('deleteRow', nIndex);
	$('#purchaseReturnedItems_table_itemDG').datagrid('acceptChanges');
	refreshDataGrid ('#purchaseReturnedItems_table_itemDG');
	purchaseReturnedItems_loadFooterDG ();
}

function purchaseReturnedItems_initToCombobox ()
{
	$('#purchaseReturnedItems_input_vendorName').combobox
	({
		valueField:'m_nClientId',
	    textField:'m_strTo',
	    selectOnNavigation: false,
	    loader: getFilteredVendorData,
		mode: 'remote',
	    formatter:function(row)
    	{
        	var opts = $(this).combobox('options');
        	return decodeURIComponent(row[opts.textField]);
    	},
		onSelect:function(row)
	    {
    		if(row.m_nClientId != m_oTrademustMemberData.m_nSelectedClientId)
	    		clearGridData ("#purchaseReturnedItems_table_itemDG");
    		m_oPurchaseReturnedItemsMemberData.m_oVendorDetails = row;
    		purchaseReturnedItems_setVendorInfo(row);
	    },
	    onChange:function(row)
	    {
	    	purchaseReturnedItems_showButtons ();
	    }
	});
	var toTextBox = $('#purchaseReturnedItems_input_vendorName').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Vendor Name";
	toTextBox.bind('keydown', function (e)
		    {
				purchaseReturnedItems_handleKeyboardNavigation (e);
		    });
}

var getFilteredVendorData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oVendorData = new VendorData ();
		oVendorData.m_strCompanyName = strQuery;
		VendorDataProcessor.getVendorSuggesstions (oVendorData, "", "", function(oResponse)
				{
					var arrVendorInfo = new Array ();
					for(var nIndex=0; nIndex< oResponse.m_arrVendorData.length; nIndex++)
				    {
						arrVendorInfo.push(oResponse.m_arrVendorData[nIndex]);
						arrVendorInfo[nIndex].m_strTo = encodeURIComponent(oResponse.m_arrVendorData[nIndex].m_strCompanyName + " "+"|" +" "+
				    											oResponse.m_arrVendorData[nIndex].m_strTinNumber);
				    }
					success(arrVendorInfo);
				});
	}
	else
		success(new Array ());
}

function purchaseReturnedItems_populateVendorCombobox(oSalesData)
{
	assert.isObject(oSalesData, "oSalesData expected to be an Object.");
	assert( Object.keys(oSalesData).length >0 , "oSalesData cannot be an empty .");// checks for non emptyness 
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName = oSalesData.m_oVendorData.m_strCompanyName;
	VendorDataProcessor.getVendorSuggesstions (oVendorData, "", "", function(oResponse)
			{
				var arrVendorInfo = new Array ();
				for(var nIndex=0; nIndex< oResponse.m_arrVendorData.length; nIndex++)
			    {
					arrVendorInfo.push(oResponse.m_arrVendorData[nIndex]);
					arrVendorInfo[nIndex].m_strTo = encodeURIComponent(oResponse.m_arrVendorData[nIndex].m_strCompanyName + " "+"|" +" "+
			    											oResponse.m_arrVendorData[nIndex].m_strTinNumber);
			    }
				$('#purchaseReturnedItems_input_vendorName').combobox('loadData',arrVendorInfo)
			});
}

function purchaseReturnedItems_handleKeyboardNavigation (oEvent)
{
	assert.isObject(oEvent, "oEvent expected to be an Object.");
	assert( Object.keys(oEvent).length >0 , "oEvent cannot be an empty .");// checks for non emptyness 
	if(oEvent.keyCode == 13)
	{
		purchaseReturnedItems_setVendorInfo(m_oPurchaseReturnedItemsMemberData.m_oVendorDetails);
	}
}

function purchaseReturnedItems_setVendorInfo(oVendorData)
{
	assert.isObject(oVendorData, "oVendorData expected to be an Object.");
	assert( Object.keys(oVendorData).length >0 , "oVendorData cannot be an empty .");// checks for non emptyness 
	m_oTrademustMemberData.m_nSelectedClientId = oVendorData.m_nClientId;
	$("#purchaseReturnedItems_input_vendorName").combobox('setText',oVendorData.m_strCompanyName);
	purchaseReturnedItems_showButtons ();
}

function purchaseReturnedItems_showButtons ()
{
	if($('#purchaseReturnedItems_input_vendorName').combobox('getValue') > 0)
		purchaseReturnedItems_displayButtons (false);
	else
		purchaseReturnedItems_displayButtons (true);
}

function purchaseReturnedItems_displayButtons (bSelectSalesButtonDisabled)
{
	assert.isBoolean(bSelectSalesButtonDisabled, "bSelectSalesButtonDisabled should be a boolean value");
	var oSelectSalesButton = document.getElementById ("purchaseReturnedItems_button_selectPurchases");
	oSelectSalesButton.disabled = bSelectSalesButtonDisabled;
	bSelectSalesButtonDisabled ? oSelectSalesButton.style.backgroundColor = "#c0c0c0" :  oSelectSalesButton.style.backgroundColor = "#0E486E";
}

function purchaseReturnedItems_editRowDG (rowData, rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	 if (m_oPurchaseReturnedItemsMemberData.m_nEditIndex != rowIndex)
	    {
	        if (purchaseReturnedItems_endEdit ())
	        {
	        	m_oPurchaseReturnedItemsMemberData.m_nEditIndex = rowIndex;
	            $('#purchaseReturnedItems_table_itemDG').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
	            purchaseReturnedItems_setEditing(m_oPurchaseReturnedItemsMemberData.m_nEditIndex);
	        } 
	        else 
	            $('#purchaseReturnedItems_table_itemDG').datagrid('selectRow', m_oPurchaseReturnedItemsMemberData.m_nEditIndex);
	    }
}

function purchaseReturnedItems_endEdit ()
{
    if (m_oPurchaseReturnedItemsMemberData.m_nEditIndex == undefined)
    	return true
    if ($('#purchaseReturnedItems_table_itemDG').datagrid('validateRow', m_oPurchaseReturnedItemsMemberData.m_nEditIndex))
    {
        $('#purchaseReturnedItems_table_itemDG').datagrid('endEdit',  m_oPurchaseReturnedItemsMemberData.m_nEditIndex);
        m_oPurchaseReturnedItemsMemberData.m_nEditIndex = undefined;
        return true;
    } 
    else 
        return false;
}

function purchaseReturnedItems_setEditing(rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
    var editors = $('#purchaseReturnedItems_table_itemDG').datagrid('getEditors', rowIndex);
    var shippedQtyEditor = editors[0];
    var priceEditor = editors[1];
    var discountEditor = editors[2];
    var taxEditor = editors[3]
    var returnedItemsEditor = editors[4];
    var returnItemsEditor = editors[5];
    var amountEditor = editors[6];
    returnItemsEditor.target.bind('keyup', function (oEvent)
    		{
    			oEvent.preventDefault();
    			validateFloatNumber (this); 
		    });
    returnItemsEditor.target.bind('change blur', function()
    		{
    			purchaseReturnedItems_validateShipValue (this.value);
    			purchaseReturnedItems_calculateAmount (this.value);
    		});
    
    function purchaseReturnedItems_validateShipValue (nValue)
    {
    	var nBalanceQty = Number(shippedQtyEditor.target.val()) - Number(returnedItemsEditor.target.val());
    	if(Number(nValue) > nBalanceQty)
    	{
    		informUser ("Returned items quantity cannot exceed " + nBalanceQty + ".", "kWarning");
    		$(returnItemsEditor.target).val('1.00');
    		$(returnItemsEditor.target).focus();
    	}
    }
    
    function purchaseReturnedItems_calculateAmount (nReturnQty)
    {
    	assert.isString(nReturnQty, "nReturnQty expected to be a String.");
    	var nDiscountPrice = $(priceEditor.target).numberbox('getValue') * $(discountEditor.target).numberbox('getValue')/100;
    	var nTaxPrice = $(priceEditor.target).numberbox('getValue') * $(taxEditor.target).numberbox('getValue')/100;
    	var nPrice = Number($(priceEditor.target).numberbox('getValue'));
    	var nTotalPrice = (nPrice + nTaxPrice - nDiscountPrice) * nReturnQty;
    	$(amountEditor.target).numberbox('setValue',nTotalPrice);
    	purchaseReturnedItems_loadFooterDG ();
    }
}

function purchaseReturnedItems_loadFooterDG ()
{
	purchaseReturnedItems_acceptDGchanges ();
	var arrReturnLineItem = $('#purchaseReturnedItems_table_itemDG').datagrid('getRows');
	var nTotal = 0;
	for (var nIndex = 0; nIndex < arrReturnLineItem.length; nIndex++)
	{
		if(Number(arrReturnLineItem [nIndex].m_nReturnValue) > 0)
			nTotal = Number(nTotal) + Number(arrReturnLineItem [nIndex].m_nReturnValue);
	}
	nTotal = nTotal.toFixed(2);
	$('#purchaseReturnedItems_table_itemDG').datagrid('reloadFooter',[{m_nReturnQuantity:'<b>Total</b>', m_nReturnValue:'<span class="rupeeSign">R </span>' + nTotal}]);
}

function purchasepurchaseReturnedItems_selectPurchases ()
{
	m_oPurchaseReturnedItemsMemberData.m_nEditIndex = undefined;
	navigate ("", "widgets/inventorymanagement/purchasereturneditems/vendorPurchaseList.js");
}

function purchaseReturnedItems_cancel ()
{
	HideDialog("dialog");
}

function purchaseReturnedItems_submit ()
{
	if (purchaseReturnedItems_validate () && purchaseReturnedItems_validateDGRow ())
	{
		var oPurchaseReturnedData = purchaseReturnedItems_getFormData ();
		if((document.getElementById("purchaseReturnedItems_button_save").getAttribute('update') == "false"))
			PurchaseReturnedDataProcessor.create (oPurchaseReturnedData, purchaseReturnedItems_created);
		else
		{
			oPurchaseReturnedData.m_nId = m_oPurchaseReturnedItemsMemberData.m_nPurchaseReturnedId;
			PurchaseReturnedDataProcessor.update (oPurchaseReturnedData, purchaseReturnedItems_updated);
		}
	}
}

function purchaseReturnedItems_saveAndPrint ()
{
	if (purchaseReturnedItems_validate () && purchaseReturnedItems_validateDGRow ())
	{
		disable ("purchaseReturnedItems_button_saveAndPrint");
		var oPurchaseReturnedData = purchaseReturnedItems_getFormData ();
		if(document.getElementById("purchaseReturnedItems_button_saveAndPrint").getAttribute('update') == "false")
			PurchaseReturnedDataProcessor.saveAndPrint (oPurchaseReturnedData, purchaseReturnedItems_savedForPrint );
		else
		{
			oPurchaseReturnedData.m_nId = m_oPurchaseReturnedItemsMemberData.m_nPurchaseReturnedId;
			PurchaseReturnedDataProcessor.updateAndPrint(oPurchaseReturnedData, purchaseReturnedItems_savedForPrint);
		}
	}
}

function purchaseReturnedItems_savedForPrint (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		m_oPurchaseReturnedItemsMemberData.m_strXMLData = oResponse.m_strXMLData;
		navigate ('Print Purchase Return','widgets/inventorymanagement/purchasereturneditems/purchaseReturnedItemsPrint.js');
	}
	else
	{
		informUser ("Purchase Returned Items Print Failed", "kError");
		enable ("purchaseReturnedItems_button_saveAndPrint");
	}
}

function purchaseReturnedItems_validate ()
{
	return validateForm("purchaseReturnedItems_form_id") && purchaseReturnedItems_validateSelectField ();
}

function purchaseReturnedItems_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	if(!purchaseReturnedItems_isValidVendor())
	{
		informUser("Please Select Vendor Name", "kWarning");
		$('#purchaseReturnedItems_input_vendorName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function purchaseReturnedItems_isValidVendor()
{
	var bIsValid = false;
	try
	{
		var strVendor = $('#purchaseReturnedItems_input_vendorName').combobox('getValue');
		if(strVendor != null && strVendor > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function purchaseReturnedItems_validateDGRow ()
{
	var bIsValidateDGRow = true;
	purchaseReturnedItems_acceptDGchanges();
	var arrLineItemData = $('#purchaseReturnedItems_table_itemDG').datagrid('getRows');
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var nReturnQuantity = arrLineItemData [nIndex].m_nReturnQuantity;
		if (nReturnQuantity == "" || Number(nReturnQuantity == 0))
		{
			bIsValidateDGRow = false;
			informUser(nReturnQuantity == "" ?  "Return Quantity cannot be empty." : "Return Quantity cannot be zero.", "kWarning");
			break;
		}
	}
	return bIsValidateDGRow;
}

function purchaseReturnedItems_acceptDGchanges()
{
	if (purchaseReturnedItems_endEdit())
        $('#purchaseReturnedItems_table_itemDG').datagrid('acceptChanges');
}

function purchaseReturnedItems_getFormData ()
{
	purchaseReturnedItems_acceptDGchanges();
	var oPurchaseReturnedData = new PurchaseReturnedData ();
	oPurchaseReturnedData.m_oVendorData = new VendorData ();
	oPurchaseReturnedData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseReturnedData.m_oVendorData.m_nClientId = $('#purchaseReturnedItems_input_vendorName').combobox('getValue');
	oPurchaseReturnedData.m_strDebitNoteNumber = $("#purchaseReturnedItems_input_debitNoteNumber").val();
	purchaseReturnedItems_buildLineItemArray (oPurchaseReturnedData);
	return oPurchaseReturnedData;
}

function purchaseReturnedItems_buildLineItemArray (oPurchaseReturnedData)
{
	assert.isObject(oPurchaseReturnedData, "oPurchaseReturnedData expected to be an Object.");
	assert( Object.keys(oPurchaseReturnedData).length >0 , "oPurchaseReturnedData cannot be an empty .");// checks for non emptyness 
	var arrLineItemData = $('#purchaseReturnedItems_table_itemDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oPurchaseReturnedLineItemData = new PurchaseReturnedLineItemData ();
		var nId = arrLineItemData [nIndex].m_nRetunedId;
		oPurchaseReturnedLineItemData.m_nId = nId > 0 ? nId : -1;
		oPurchaseReturnedLineItemData.m_oPurchaseLineItem.m_nLineItemId =  arrLineItemData [nIndex].m_nLineItemId;
		oPurchaseReturnedLineItemData.m_nQuantity = Number (arrLineItemData [nIndex].m_nReturnQuantity);
		oPurchaseReturnedData.m_arrPurchaseReturnedLineItemData.push(oPurchaseReturnedLineItemData);
	}
}

function purchaseReturnedItems_created (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Purchase Returned items saved successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate ("purchaseReturnedList", "widgets/inventorymanagement/purchasereturneditems/purchaseReturnedListAdmin.js")
	}
	else
		informUser("Save Failed", "kError");
}

function purchaseReturnedItems_updated (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Purchase Returned items updated successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate ("purchaseReturnedList", "widgets/inventorymanagement/purchasereturneditems/purchaseReturnedListAdmin.js")
	}
	else
		informUser("Updation Failed", "kError");
}

function purchaseReturnedItems_addVendor ()
{
	navigate ("vendorInfo", "widgets/vendormanagement/addVendor.js");
}

function vendorInfo_handleAfterSave ()
{
	// Handler Function for Vendor Save.
}