var supply_includeDataObjects = 
[
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/purchaseordermanagement/supply/SupplyData.js',
	'widgets/inventorymanagement/purchase/NonStockPurchaseLineItem.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderLineItemData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js'
];

 includeDataObjects (supply_includeDataObjects, "supply_loaded ()");

function supply_memberData ()
{
	this.editIndex = undefined;
	this.m_arrUniqueTaxNames = new Array ();
	this.m_arrPOLineItemIds = new Array ();
}

var m_oSupplyMemberData = new supply_memberData ();

function supply_new ()
{
	createPopup("dialog", "#supply_button_create", "#supply_button_cancel", true);
	supply_init ();
}

function supply_initUser ()
{
	supply_new ();
}

function supply_initAdmin ()
{
	document.getElementById ("supply_img_addVendor").style.visibility="visible";
	supply_new ();
}

function supply_init ()
{
	initFormValidateBoxes ("supply_form_id");
//	$("#supply_input_date" ).datebox({required:true});
	$("#supply_input_date" ).datepicker({minDate:'-6y',maxDate:'+6m'});
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
//	$('#supply_input_date').datebox('setValue', dCurrentDate);
	$('#supply_input_date').datepicker('setDate', dCurrentDate);
	supply_initFromCombobox ();
	supply_getUniqueTaxNames ();
	supply_initializeDataGrid ();
}

function supply_edit ()
{
	createPopup ("dialog", "#supply_button_save", "#supply_button_cancel", true);
	document.getElementById ("supply_img_addVendor").style.visibility="visible";
	supply_init ();
	document.getElementById("supply_button_save").setAttribute('update', true);
	document.getElementById("supply_button_save").innerHTML = "Update";
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_nId = m_oSupplyMemberData.m_nSupplyId;
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseDataProcessor.get (oPurchaseData, supply_gotPurchaseData);
}

function supply_gotPurchaseData (oResponse)
{
	var arrPurchase = oResponse.m_arrPurchase[0];
	supply_populateFromCombobox(arrPurchase);
	$('#supply_input_from').combobox('select', arrPurchase.m_oVendorData.m_nClientId);	
	$("#supply_input_invoiceNo").val(arrPurchase.m_strInvoiceNo);
//	$('#supply_input_date').datebox('setValue', arrPurchase.m_strDate);
	$('#supply_input_date').datepicker('setDate', arrPurchase.m_strDate);
	initFormValidateBoxes ("supply_form_id");
	$('#supply_table_articles').datagrid('loadData',arrPurchase.m_oNonStockPurchaseLineItems);
}

function supply_populateFromCombobox(arrPurchase)
{
	assert.isArray(arrPurchase, "arrPurchase expected to be an Array.");
	assert(arrPurchase.length >= 1, "arrPurchase array cannot be empty.");
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName = arrPurchase.m_oVendorData.m_strCompanyName;
	VendorDataProcessor.getVendorSuggesstions (oVendorData, "", "", function(oResponse)
			{
				var arrVendorData = new Array ();
				for(var nIndex=0; nIndex< oResponse.m_arrVendorData.length; nIndex++)
			    {
					arrVendorData.push(oResponse.m_arrVendorData[nIndex]);
					arrVendorData[nIndex].m_strFrom = oResponse.m_arrVendorData[nIndex].m_strCompanyName;
			    }
				$('#supply_input_from').combobox('loadData',arrVendorData);
			});
}

function supply_getAmount (oNonStockPurchaseLineItem)
{
	assert.isObject(oNonStockPurchaseLineItem, "oNonStockPurchaseLineItem expected to be an Object.");
	assert( Object.keys(oNonStockPurchaseLineItem).length >0 , "oNonStockPurchaseLineItem cannot be an empty .");// checks for non emptyness 
	var nAmount = 0;
	nAmount =  oNonStockPurchaseLineItem.m_nQuantity * oNonStockPurchaseLineItem.m_nPrice ;
	nAmount -= nAmount *(oNonStockPurchaseLineItem.m_nDiscount/100);
	nAmount += nAmount *(oNonStockPurchaseLineItem.m_nTax/100);
	nAmount += nAmount *(oNonStockPurchaseLineItem.m_nOtherCharges/100);
	return nAmount.toFixed(2);
}

function supply_cancel ()
{
	HideDialog ("dialog");
}

function supply_initFromCombobox ()
{
	$('#supply_input_from').combobox
	({
		valueField:'m_nClientId',
	    textField:'m_strFrom',
	    selectOnNavigation: false,
	    loader: getFilteredVendorData,
		mode: 'remote',
	});
	var toTextBox = $('#supply_input_from').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Vendor Name";
	toTextBox.focus ();
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
					var arrVendorData = new Array ();
					for(var nIndex=0; nIndex< oResponse.m_arrVendorData.length; nIndex++)
				    {
						arrVendorData.push(oResponse.m_arrVendorData[nIndex]);
						arrVendorData[nIndex].m_strFrom = oResponse.m_arrVendorData[nIndex].m_strCompanyName;
				    }
					success(arrVendorData);
				});
	}
	else
		success(new Array ());
}

function supply_getUniqueTaxNames()
{
	TaxDataProcessor.getUniqueTaxNames ( function(oResponse)
			{
				m_oSupplyMemberData.m_arrUniqueTaxNames = oResponse.m_arrTax;
			});
}

function supply_initializeDataGrid ()
{
	$('#supply_table_articles').datagrid ({
	    columns:[[  
	        {field:'m_strArticleDescription',title:'Name', width:210},
	        {field:'m_nBalanceQuantity',title:'Bal Qty',width:60,
	        	formatter:function(value,row,index)
	        	{
	        		return Number(row.m_nBalanceQuantity).toFixed(2);
	        	}
	        },
	        {field:'m_nQuantity',title:'Qty',align:'right', width:60,editor:{'type' :'numberbox', 'options':{precision:'2'}},
	        	formatter:function(value,row,index)
	        	{
	        		return Number(row.m_nQuantity).toFixed(2);
	        	}
	        },
	        {field:'m_nPrice',title:'Price',align:'right',width:80,editor:{'type' :'text'},
		        formatter:function(value,row,index)
	        	{
	        		return Number(row.m_nPrice).toFixed(2);
	        	}
	        },
	        {field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',editor:{'type':'text'},
	        	 formatter:function(value,row,index)
		        	{
		        		return Number(row.m_nDiscount).toFixed(2);
		        	}
	        },
	        {field:'m_strTaxName',title:'Tax Name',width:70,
	        	editor:
	        	{ 
	        		type:'combobox',
	        		options:
		            {
			        	data:m_oSupplyMemberData.m_arrUniqueTaxNames,
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
	        {field:'m_nTax',title:'Tax(%)',width:45,align:'right',editor:{'type':'text'},
	        	formatter:function(value,row,index)
	        	{
	        		return Number(row.m_nTax).toFixed(2);
	        	}
	        },
	        {field:'m_nOtherCharges',title:'Other Chgs(%)',width:100,align:'right',editor:{'type':'text'},
	        	formatter:function(value,row,index)
	        	{
	        		return Number(row.m_nOtherCharges).toFixed(2);
	        	}
	        },
	        {field:'m_nAmount',title:'Amount',width:90,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}}}
	    ]]
	});
	$('#supply_table_articles').datagrid
	(
		{
			onClickRow: function (rowIndex, rowData)
			{
				supply_editRowDG (rowData, rowIndex);
			}
		}
	)
}

function supply_editRowDG (rowData, rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	if (m_oSupplyMemberData.editIndex != rowIndex)
    {
        if (supply_endEdit ())
        {
            $('#supply_table_articles').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
            m_oSupplyMemberData.editIndex = rowIndex;
            supply_setEditing(m_oSupplyMemberData.editIndex);
        } 
        else 
            $('#supply_table_articles').datagrid('selectRow', m_oSupplyMemberData.editIndex);
    }
}

function supply_endEdit ()
{
    if (m_oSupplyMemberData.editIndex == undefined)
    {return true}
    if ($('#supply_table_articles').datagrid('validateRow', m_oSupplyMemberData.editIndex))
    {
        $('#supply_table_articles').datagrid('endEdit',  m_oSupplyMemberData.editIndex);
        m_oSupplyMemberData.editIndex = undefined;
        return true;
    }
    else 
    {
        return false;
    }
}

function supply_setEditing (rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	var editors = $('#supply_table_articles').datagrid('getEditors', rowIndex);
	var quantityEditor = editors[0];
	var priceEditor = editors[1];
	var discountEditor = editors[2];
	var taxNameEditor = editors[3];
	var taxPercentEditor = editors[4];
	var otherChargesEditor = editors[5];
	var amountEditor = editors[6];
	var taxNameTextbox =  $(taxNameEditor.target).combobox('textbox');
	
	quantityEditor.target.bind('change', function()
    		{
				supply_validateOrderQty (this.value, rowIndex);
    		});
    quantityEditor.target.bind('blur', function()
    		{
    			supply_validateOrderQty (this.value, rowIndex);
    			if (Number (this.value) == 0)
    				this.focus();
    		});
	
    priceEditor.target.bind('change', function()
    		{
    			supply_calculateAmount(rowIndex);
    			
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
    			priceEditor.target.unbind ('blur');
    			supply_calculateAmount(rowIndex);
    		});
    discountEditor.target.bind('change', function()
    		{
    			supply_calculateAmount (rowIndex);
    		});
    
    taxNameTextbox.bind('blur', function()
    		{	
    			if(!supply_validateTaxName(this.value))
    				this.focus ();
    		});
    taxNameTextbox.bind('change', function()
    		{	
    			if(!supply_validateTaxName(this.value))
    				this.focus ();
    		});
    
    taxPercentEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
    			validatePercentage (this);
		    });
    taxPercentEditor.target.bind('blur', function()
    		{
    			priceEditor.target.unbind ('blur');
    			supply_calculateAmount(rowIndex);
    		});
    taxPercentEditor.target.bind('change', function()
    		{
    			supply_calculateAmount (rowIndex);
    		});
    
    otherChargesEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
    			validatePercentage (this);
		    });
    otherChargesEditor.target.bind('blur', function()
    		{
    			priceEditor.target.unbind ('blur');
    			supply_calculateAmount(rowIndex);
    		});
	otherChargesEditor.target.bind('change', function()
			{
				supply_calculateAmount (rowIndex);
			});
	 
	 function supply_calculateAmount (rowIndex)
	 {
		 assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
		var nAmount = 0;
    	nAmount = quantityEditor.target.val() * priceEditor.target.val() ;
    	nAmount -= nAmount *(discountEditor.target.val()/100);
    	nAmount += nAmount *(taxPercentEditor.target.val()/100);
    	nAmount += nAmount *(otherChargesEditor.target.val()/100);
	 	if(nAmount >= 0)
		{
	 		$(amountEditor.target).numberbox('setValue',nAmount);
		}
	 }
	 
	 function supply_validateOrderQty (nValue, nRowIndex)
	    {
		 assert.isNumber(nValue, "nValue expected to be a Number.");
		 assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	    	var nBalanceQty = supply_getBalanceQty (nRowIndex);
	    	if(Number(nValue) > nBalanceQty)
	    	{
	    		informUser ("Quantity should be less than " + nBalanceQty + ".", "kWarning");
	    		$(quantityEditor.target).numberbox('setValue', 0);
	    		//quantityEditor.target.focus ();
	    	}
	    	else
	    		supply_calculateAmount(rowIndex);
	    }
	 
	 function supply_getBalanceQty (nRowIndex)
	 {
		 assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
		 var oNSItems = $('#supply_table_articles').datagrid ('getRows')[nRowIndex];
		 var nBalanceQuantity = oNSItems.m_nBalanceQuantity;
		 return nBalanceQuantity;
	 }
	 
	 function supply_validateTaxName(strTaxName)
	    {
		 assert.isString(strTaxName, "strTaxName expected to be a string.");
		 assert(strTaxName !== "", "strTaxName cannot be an empty string");
	    	var bIsValidTaxName = false;
	    	for (var nIndex = 0; nIndex < m_oSupplyMemberData.m_arrUniqueTaxNames.length; nIndex++)
	    	{
	    		if(strTaxName.toLowerCase() == m_oSupplyMemberData.m_arrUniqueTaxNames[nIndex].m_strTaxName.toLowerCase())
	    			bIsValidTaxName = true;
	    	}
	    	return bIsValidTaxName;
	    }
}

function supply_submit ()
{
	if (supply_validate () && supply_validateDGRow ())
	{
		disable ("supply_button_save");
		var oPurchaseData = supply_getFormData ();
		if(document.getElementById("supply_button_save").getAttribute('update') == "false")
			SupplyDataProcessor.create (oPurchaseData, supply_created);
		else
		{
			oPurchaseData.m_nId = m_oSupplyMemberData.m_nSupplyId;
			SupplyDataProcessor.update(oPurchaseData, supply_updated);
		}
	}
}

function supply_addSupply ()
{
	supply_accept ();
	navigate ('addedSupply','widgets/purchaseordermanagement/supply/nonStockSupplyItems.js');
}

function supply_addNonStockItems ()
{
	
}

function supply_getSupplyList (strFromName,strInvoiceNo)
{
	var oSupplyData = new SupplyData ();
	SupplyDataProcessor.list(oSupplyData, "", "", supply_listed);
}

function supply_listed (oResponse)
{
	if (oResponse.m_arrSupply.length > 0)
	{
		informUser ("Supply Item already exists", "kWarning");
		$("#supply_input_invoiceNo").val("");
		document.getElementById("supply_input_invoiceNo").focus();
	}
}

function supply_cancelNonStockItems ()
{
	HideDialog ("secondDialog");
}

function supply_validate ()
{
	return validateForm ("supply_form_id");
}

function supply_validateDGRow ()
{
	var bIsValidateDGRow = true;
	supply_accept ();
	var arrLineItemData = $('#supply_table_articles').datagrid('getData').rows;
	if (arrLineItemData.length > 0)
	{
		for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
		{
			var nQuantity = arrLineItemData [nIndex].m_nQuantity;
			var nPrice = arrLineItemData [nIndex].m_nPrice;
			if (nQuantity == "" || nQuantity == 0 || nPrice == "" || nPrice == 0)
			{
				bIsValidateDGRow = false;
				informUser (nQuantity == "" || nQuantity == 0 ? nQuantity == "" ? "Quantity cannot be empty." : "Quantity should be greater than 0." :  nPrice == "" ? "Price cannot be empty." : "Price should be greater than 0." );
				break;
			}
		}
	}
	else
	{
		bIsValidateDGRow = false;
		informUser("Non Stock Sales Items Not Added", "kWarning");
	}
	return bIsValidateDGRow;
}

function supply_accept ()
{
        $('#supply_table_articles').datagrid('acceptChanges');
        m_oSupplyMemberData.editIndex = undefined ;
}

function supply_getFormData ()
{
	supply_accept ();
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_oCreatedBy = new UserInformationData ();
	oPurchaseData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_strFrom = $('#supply_input_from').combobox('getText');
	oPurchaseData.m_strInvoiceNo = $("#supply_input_invoiceNo").val();
//	var m_strDate = $('#supply_input_date').datebox('getValue');
	var m_strDate = $('#supply_input_date').val();
	oPurchaseData.m_strDate = FormatDate (m_strDate);
	oPurchaseData.m_oVendorData.m_nClientId = $('#supply_input_from').combobox('getValue');
//	oPurchaseData.m_arrNonStockPurchaseLineItem = supply_getLineItemDataArray ();
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	return oPurchaseData;
}

function supply_getLineItemDataArray ()
{
	var oLineItemDataArray = new Array ();
	var arrLineItemData = $('#supply_table_articles').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oSupplyLineItem = new NonStockPurchaseLineItem ();
		oSupplyLineItem.m_strArticleDescription =  arrLineItemData [nIndex].m_strArticleDescription;
		oSupplyLineItem.m_nLineItemId = arrLineItemData [nIndex].m_nLineItemId != undefined ? arrLineItemData [nIndex].m_nLineItemId : -1;
		oSupplyLineItem.m_nQuantity = arrLineItemData [nIndex].m_nQuantity;
		oSupplyLineItem.m_nPrice = arrLineItemData [nIndex].m_nPrice;
		oSupplyLineItem.m_nDiscount = arrLineItemData [nIndex].m_nDiscount;
		oSupplyLineItem.m_strTaxName = arrLineItemData [nIndex].m_strTaxName;
		oSupplyLineItem.m_nTax = arrLineItemData [nIndex].m_nTax;
		oSupplyLineItem.m_nOtherCharges = arrLineItemData [nIndex].m_nOtherCharges;
		oSupplyLineItem.m_oPOLineItem = new PurchaseOrderLineItemData ();
		oSupplyLineItem.m_oPOLineItem.m_nId = arrLineItemData [nIndex].m_nId;
		oLineItemDataArray.push(oSupplyLineItem);
	}
	return oLineItemDataArray;
}

function supply_created (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser ("Supply Created Succesfully", "kSuccess");
		HideDialog ("dialog");
		navigate ("Supply List", "widgets/purchaseordermanagement/supply/supplyListAdmin.js");
	}
	else
	{
		informUser ("Supply Creation Failed", "kError");
		enable ("supply_button_save");
	}
}

function supply_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		informUser("Supply updated successfully", "kSuccess");
		navigate ("Supply List", "widgets/purchaseordermanagement/supply/supplyListAdmin.js");
	}
	else
	{
		informUser ("Supply Updation Failed", "kError");
		enable ("supply_button_save");
	}
}

function supply_addVendor ()
{
	navigate ("VendorInfo", "widgets/vendormanagement/addVendor.js");
}

function vendorInfo_handleAfterSave ()
{
	// Handler Function for New Vendor Save.
}