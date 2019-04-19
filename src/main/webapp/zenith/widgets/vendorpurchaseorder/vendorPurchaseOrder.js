var vendorPurchaseOrder_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/vendorpurchaseorder/VendorPurchaseOrderData.js',
	'widgets/vendorpurchaseorder/VendorPOLineItemData.js'
];

includeDataObjects (vendorPurchaseOrder_includeDataObjects, "vendorPurchaseOrder_loaded ()");

function vendorPurchaseOrder_memberData ()
{
	this.m_nPurchaseOrderId = -1;
	this.m_nEditIndex = undefined;
	this.m_arrArticle = new Array ();
	this.m_arrArticleDetails = new Array ();
	this.m_arrVendorInfo = new Array ();
	this.m_arrToDetails = new Array();
	this.m_arrDeletedLineItems = new Array ();
	this.m_bDisableInvoiceButton = true;
	this.m_arrUniqueTaxNames = new Array();
	this.m_strPONumber = "";
	this.m_bIsForReorder = false;
	this.m_oQuotationData = new QuotationData();
}

var m_oVendorPurchaseOrderMemberData = new vendorPurchaseOrder_memberData ();

function vendorPurchaseOrder_new ()
{
	vendorPurchaseOrder_init ();
	if(m_oVendorPurchaseOrderMemberData.m_bIsForReorder)
		vendorPurchaseOrder_setValues ();
	vendorPurchaseOrder_appendRow ();
}

function vendorPurchaseOrder_setValues ()
{
	var arrVendorPurchaseOrder = getOrderedLineItems(m_oVendorPurchaseOrderMemberData.m_oItemData);
	for (var nIndex = 0; nIndex < arrVendorPurchaseOrder.length; nIndex++)
	{
		arrVendorPurchaseOrder[nIndex].m_strArticleNumber = arrVendorPurchaseOrder [nIndex].m_strArticleNumber;
		arrVendorPurchaseOrder[nIndex].m_strDesc = arrVendorPurchaseOrder [nIndex].m_strItemName;
		arrVendorPurchaseOrder[nIndex].m_strUnit = arrVendorPurchaseOrder [nIndex].m_strUnit;
		arrVendorPurchaseOrder[nIndex].m_nPrice = arrVendorPurchaseOrder [nIndex].m_nSellingPrice;
		arrVendorPurchaseOrder[nIndex].m_nDiscount = 0;
		arrVendorPurchaseOrder[nIndex].m_nTax = arrVendorPurchaseOrder [nIndex].m_oApplicableTax.m_oTaxes[0].m_nPercentage
		arrVendorPurchaseOrder[nIndex].m_strTaxName = arrVendorPurchaseOrder [nIndex].m_oApplicableTax.m_oTaxes[0].m_strTaxName
		$('#vendorPurchaseOrder_table_itemsDG').datagrid('appendRow',arrVendorPurchaseOrder[nIndex]);
	}
}

function vendorPurchaseOrder_init ()
{
	createPopup("dialog", "#vendorPurchaseOrder_button_create", "#vendorPurchaseOrder_button_cancel", true);
	initFormValidateBoxes ("vendorPurchaseOrder_form_id");
//	$("#vendorPurchaseOrder_input_date" ).datebox({required:true});
	$("#vendorPurchaseOrder_input_date" ).datepicker({minDate:'-6y',maxDate:'+6m'});
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 +'/'+ dDate.getDate() +'/'+ dDate.getFullYear();
//	$('#vendorPurchaseOrder_input_date').datebox('setValue', dCurrentDate);
	$('#vendorPurchaseOrder_input_date').datepicker('setDate', dCurrentDate);
	vendorPurchaseOrder_initToCombobox ()
	vendorPurchaseOrder_getUniqueTaxNames ();
	vendorPurchaseOrder_initializeDataGrid ();
}

function vendorPurchaseOrder_initToCombobox ()
{
	$('#vendorPurchaseOrder_input_vendorName').combobox
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
    		m_oVendorPurchaseOrderMemberData.m_arrToDetails = row;
    		vendorPurchaseOrder_setVendorInfo(m_oVendorPurchaseOrderMemberData.m_arrToDetails);
	    },
	});
	var toTextBox = $('#vendorPurchaseOrder_input_vendorName').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Vendor Name";
	toTextBox.focus ();
	toTextBox.bind('keydown', function (e)
		    {
				vendorPurchaseOrder_handleKeyboardNavigation (e);
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

function vendorPurchaseOrder_setVendorInfo(oVendorData)
{
	assert.isObject(oVendorData, "oVendorData expected to be an Object.");
	assert( Object.keys(oVendorData).length >0 , "oVendorData cannot be an empty .");// checks for non emptyness 
	m_oTrademustMemberData.m_nSelectedVendorId = oVendorData.m_nClientId;
	document.getElementById ("vendorPurchaseOrder_td_tinNo").style.visibility = "visible";
	document.getElementById ("vendorPurchaseOrder_td_vatNo").style.visibility = "visible";
	document.getElementById ("vendorPurchaseOrder_td_cstNo").style.visibility = "visible";
	$("#vendorPurchaseOrder_input_vendorName").combobox('setText',oVendorData.m_strCompanyName);
	$("#vendorPurchaseOrder_label_address").val(oVendorData.m_strAddress);
	$("#vendorPurchaseOrder_label_city").val(oVendorData.m_strCity + "-" + oVendorData.m_strPinCode);
	$("#vendorPurchaseOrder_label_phoneNumber").val(oVendorData.m_strMobileNumber);
	$("#vendorPurchaseOrder_label_email").val(oVendorData.m_strEmail);
	$("#vendorPurchaseOrder_label_tinNo").val(oVendorData.m_strTinNumber);
	$("#vendorPurchaseOrder_label_vatNo").val(oVendorData.m_strVatNumber);
	$("#vendorPurchaseOrder_label_cstNo").val(oVendorData.m_strCSTNumber);
}

function vendorPurchaseOrder_handleKeyboardNavigation (e)
{
	assert.isObject(e, "e expected to be an Object.");
	if(e.keyCode == 13)
	{
		vendorPurchaseOrder_setVendorInfo(m_oVendorPurchaseOrderMemberData.m_arrToDetails);
	}
}

function vendorPurchaseOrder_getUniqueTaxNames()
{
	TaxDataProcessor.getUniqueTaxNames ( function(oResponse)
			{
				m_oVendorPurchaseOrderMemberData.m_arrUniqueTaxNames = oResponse.m_arrTax;
			});
}

function vendorPurchaseOrder_addItem ()
{
	navigate ("newItem", "widgets/inventorymanagement/item/itemAdmin.js");
}

function vendorPurchaseOrder_initializeDataGrid ()
{
	$('#vendorPurchaseOrder_table_itemsDG').datagrid ({
	    columns:[[  
			{field:'m_strArticleNumber',title:'Article# <img title="Add" src="images/add.PNG" align="right" id="vendorPurchaseOrder_img_addItem" onClick="vendorPurchaseOrder_addItem ()"/>', width:90, sortable:true,
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
				        		m_oVendorPurchaseOrderMemberData.m_arrArticleDetails = row;
				        		vendorPurchaseOrder_setArticleDetails (row);
				            }
			    	    }
					},
			},
	        {field:'m_strDesc',title:'Item Name',width:200,align:'left',editor:{'type':'text'}},
	        {field:'m_nQuantity',title:'Qty',width:60,align:'right',editor:{'type':'numberbox'}},
	        {field:'m_strUnit',title:'Unit',width:60,align:'left',editor:{'type':'text'}},
	        {field:'m_nPrice',title:'Price',width:70,align:'right',editor:{'type':'text'}},
	        {field:'m_nDiscount',title:'Disc(%)',width:60,align:'right',editor:{'type':'text'}},
	        {field:'m_strTaxName',title:'Tax Name',width:70,align:'right',
	        	editor:
	        	{ 
	        		type:'combobox',
	        		options:
		            {
			        	data:m_oVendorPurchaseOrderMemberData.m_arrUniqueTaxNames,
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
	        {field:'m_nReceivedQty',title:'Received Qty',width:70,align:'right',
	        	editor:
	        	{
	        		'type':'numberbox','options':{precision:'2',disabled:true}
	        	}
	        },
	        {field:'m_nReceiveQty',title:'Receive Qty',width:50,align:'right',editor:{'type':'text'}},
	        {field:'Actions',title:'',align:'center',width:20,
				formatter:function(value,row,index)
				{
		        	if(Number(row.m_nReceiveQty) > 0 || Number(row.m_nReceivedQty) > 0)
	        			return vendorPurchaseOrder_hideDeleteImage ();
		        	else
		        		return vendorPurchaseOrder_showDeleteImage (row, index);
				}
			}
	    ]],
	    onClickRow: function (rowIndex, rowData)
		{
			vendorPurchaseOrder_editRowDG (rowData, rowIndex);
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
		ItemDataProcessor.getArticleSuggesstions(oItemData, "", "", function(oItemDataResponse)
				{
					var arrItemData = new Array();
					for(var nIndex=0; nIndex< oItemDataResponse.m_arrItems.length; nIndex++)
				    {
						arrItemData.push(oItemDataResponse.m_arrItems[nIndex]);
						arrItemData[nIndex].m_strArticleDetail = encodeURIComponent(arrItemData[nIndex].m_strArticleNumber + " | " +
						arrItemData[nIndex].m_strItemName + " | " +
						arrItemData[nIndex].m_strDetail + " | " +
						arrItemData[nIndex].m_nSellingPrice + " | "+
						arrItemData[nIndex].m_oApplicableTax.m_oTaxes[0].m_nPercentage);
				    }
					success(arrItemData);
				});
	}
	else
		success(new Array ());
}

function vendorPurchaseOrder_setArticleDetails(oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var editors = $('#vendorPurchaseOrder_table_itemsDG').datagrid('getEditors', m_oVendorPurchaseOrderMemberData.m_nEditIndex);
	var articleNumberEditor = editors[0];
    var itemNameEditor = editors[1];
    var quantityEditor = editors[2];
    var unitEditor = editors[3];
    var priceEditor = editors[4];
    var discountEditor = editors[5];
    var taxNameEditor = editors[6];
    var taxEditor = editors[7];
    var receivedQtyEditor = editors[8];
    var receiveQtyEditor = editors[9];
    $(articleNumberEditor.target).combobox('textbox').focus ();
	var bIsArticleDuplicate =  vendorPurchaseOrder_isArticleDuplicate (oRowData.m_strArticleNumber, m_oVendorPurchaseOrderMemberData.m_nEditIndex);
	if(!bIsArticleDuplicate)
	{
		$(articleNumberEditor.target).combobox('setValue',oRowData.m_strArticleNumber);
		$(itemNameEditor.target).val(oRowData.m_strItemName + " | " + oRowData.m_strDetail);
		$(unitEditor.target).val(oRowData.m_strUnit);
		$(priceEditor.target).val(oRowData.m_nSellingPrice);
		$(discountEditor.target).val(0);
		$(taxNameEditor.target).combobox('setValue',oRowData.m_oApplicableTax.m_oTaxes[0].m_strTaxName);
		$(taxEditor.target).val(oRowData.m_oApplicableTax.m_oTaxes[0].m_nPercentage);
		$(itemNameEditor.target).select();
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
	}
}

function vendorPurchaseOrder_appendRow ()
{
    if (vendorPurchaseOrder_endEdit ())
    {	
    	vendorPurchaseOrder_acceptDGchanges();
    	$('#vendorPurchaseOrder_table_itemsDG').datagrid('appendRow',{m_strArticleNumber:''});
    	m_oVendorPurchaseOrderMemberData.m_nEditIndex = $('#vendorPurchaseOrder_table_itemsDG').datagrid('getRows').length-1;
        $('#vendorPurchaseOrder_table_itemsDG').datagrid('selectRow',  m_oVendorPurchaseOrderMemberData.m_nEditIndex).datagrid('beginEdit',  m_oVendorPurchaseOrderMemberData.m_nEditIndex);
        vendorPurchaseOrder_setEditing(m_oVendorPurchaseOrderMemberData.m_nEditIndex)
    }
}

function vendorPurchaseOrder_acceptDGchanges()
{
	if (vendorPurchaseOrder_endEdit())
        $('#vendorPurchaseOrder_table_itemsDG').datagrid('acceptChanges');
}

function vendorPurchaseOrder_endEdit ()
{
    if (m_oVendorPurchaseOrderMemberData.m_nEditIndex == undefined)
    	return true
    if ($('#vendorPurchaseOrder_table_itemsDG').datagrid('validateRow', m_oVendorPurchaseOrderMemberData.m_nEditIndex))
    {
        $('#vendorPurchaseOrder_table_itemsDG').datagrid('endEdit',  m_oVendorPurchaseOrderMemberData.m_nEditIndex);
        m_oVendorPurchaseOrderMemberData.m_nEditIndex = undefined;
        return true;
    } 
    else 
        return false;
}

function vendorPurchaseOrder_setEditing(rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
    var editors = $('#vendorPurchaseOrder_table_itemsDG').datagrid('getEditors', rowIndex);
    var articleNumberEditor = editors[0];
    var itemNameEditor = editors[1];
    var quantityEditor = editors[2];
    var unitEditor = editors[3];
    var priceEditor = editors[4];
    var discountEditor = editors[5];
    var taxNameEditor = editors[6];
    var taxEditor = editors[7];
    var receivedQtyEditor = editors[8];
    var receiveQtyEditor = editors[9];
    var articleNoTextbox =  $(articleNumberEditor.target).combobox('textbox');
    articleNoTextbox.focus ();
    var taxNameTextbox =  $(taxNameEditor.target).combobox('textbox');
    m_oVendorPurchaseOrderMemberData.m_nEditIndex = rowIndex;
    articleNoTextbox.bind('keydown', function (e)
    	    {
    			vendorPurchaseOrder_handleKeyboardNavigation (e)
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
    			vendorPurchaseOrder_validateReceiveValue (receiveQtyEditor.target.val());
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
    			if(!vendorPurchaseOrder_validateTaxName(this.value))
    				this.focus ();
    		});
    taxNameTextbox.bind('change', function()
    		{	
    			if(!vendorPurchaseOrder_validateTaxName(this.value))
    				this.focus ();
    		});
    taxEditor.target.bind('blur', function()
    		{	
    			if(this.value == "")
    				this.focus ();
    		});
    receiveQtyEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
		    });
    receiveQtyEditor.target.bind('change', function()
    		{
    			vendorPurchaseOrder_validateReceiveValue (this.value);
    			if (vendorPurchaseOrder_getReceiveQty (Number (this.value), rowIndex) > 0)
    				vendorPurchaseOrder_showButtons ();
    			else
    				vendorPurchaseOrder_hideButtons ();
    			if (rowIndex ==$('#vendorPurchaseOrder_table_itemsDG').datagrid('getRows').length-1)
    				vendorPurchaseOrder_appendRow ();
    		});
    receiveQtyEditor.target.bind('blur', function()
    		{
    			vendorPurchaseOrder_validateReceiveValue (this.value)
    			if (vendorPurchaseOrder_getReceiveQty (Number (this.value), rowIndex) > 0)
    				vendorPurchaseOrder_showButtons ();
    			else
    				vendorPurchaseOrder_hideButtons ();
    			if (rowIndex ==$('#vendorPurchaseOrder_table_itemsDG').datagrid('getRows').length-1)
    				vendorPurchaseOrder_appendRow ();
    		});
    function vendorPurchaseOrder_validateReceiveValue (nValue)
    {
    	assert.isNumber(nValue, "nValue expected to be a Number.");
    	var nBalanceQty = Number(quantityEditor.target.val() - receivedQtyEditor.target.val());
    	if(Number(nValue) > nBalanceQty)
    	{
    		informUser ("receive Quantity cannot exceed " + nBalanceQty + ".", "kWarning");
    		$(receiveQtyEditor.target).val('');
    		receiveQtyEditor.target.focus ();
    	}
    }
    function vendorPurchaseOrder_handleKeyboardNavigation (e)
    {
    	assert.isObject(e, "e expected to be an Object.");
    	if(e.keyCode == 13)
    		vendorPurchaseOrder_setArticleDetails (m_oVendorPurchaseOrderMemberData.m_arrArticleDetails);
    }
    function vendorPurchaseOrder_validateTaxName(strTaxName)
    {
    	assert.isString(strTaxName, "strTaxName expected to be a string.");
    	var bIsValidTaxName = false;
    	for (var nIndex = 0; nIndex < m_oVendorPurchaseOrderMemberData.m_arrUniqueTaxNames.length; nIndex++)
    	{
    		if(strTaxName.toLowerCase() == m_oVendorPurchaseOrderMemberData.m_arrUniqueTaxNames[nIndex].m_strTaxName.toLowerCase())
    			bIsValidTaxName = true;
    	}
    	return bIsValidTaxName;
    }

    function vendorPurchaseOrder_getReceiveQty(nReceiveValue, rowIndex)
    {
    	assert.isNumber(nReceiveValue, "nReceiveValue expected to be a Number.");
    	var nReceiveQty = nReceiveValue;
    	if(nReceiveQty <= 0)
    		nReceiveQty = vendorPurchaseOrder_calculateReceiveQty(rowIndex);
    	return nReceiveQty;
    }

    function vendorPurchaseOrder_calculateReceiveQty(rowIndex)
    {
    	var nReceiveQty = 0;
    	var arrRows = $('#vendorPurchaseOrder_table_itemsDG').datagrid('getRows');
    	for(nIndex = 0; nIndex < arrRows.length; nIndex++)
    	{
    		if(rowIndex != nIndex)
    			nReceiveQty += Number(arrRows[nIndex].m_nReceiveQty);
    	}
    	return nReceiveQty;
    }
}

function vendorPurchaseOrder_showButtons ()
{
	vendorPurchaseOrder_displayButtons (true, false, false);
}

function vendorPurchaseOrder_hideButtons ()
{
	vendorPurchaseOrder_displayButtons (false, true, true);
}

function vendorPurchaseOrder_displayButtons (bSaveButtonDisabled, bMakeChallanButtonDisabled, bMakeInvoiceButtonDisabled)
{
	assert.isBoolean(bSaveButtonDisabled, "bSaveButtonDisabled should be a boolean value");
	assert.isBoolean(bMakeInvoiceButtonDisabled, "bMakeInvoiceButtonDisabled should be a boolean value");
	var oPOCreateButton = document.getElementById ("vendorPurchaseOrder_button_create");
	var oPOSaveAndPrintButton = document.getElementById ("vendorPurchaseOrder_button_saveAndPrint");
	var oPOMakeInvoiceButton = document.getElementById ("vendorPurchaseOrder_button_saveAndMakeInvoice");
	oPOCreateButton.disabled = bSaveButtonDisabled;
	oPOSaveAndPrintButton.disabled = bSaveButtonDisabled;
	oPOMakeInvoiceButton.disabled = m_oVendorPurchaseOrderMemberData.m_bDisableInvoiceButton ? bMakeInvoiceButtonDisabled : false;
	bSaveButtonDisabled ? oPOCreateButton.style.backgroundColor = "#c0c0c0" :  oPOCreateButton.style.backgroundColor = "#0E486E";
	bSaveButtonDisabled ? oPOSaveAndPrintButton.style.backgroundColor = "#c0c0c0" :  oPOSaveAndPrintButton.style.backgroundColor = "#0E486E";
	oPOMakeInvoiceButton.disabled ? oPOMakeInvoiceButton.style.backgroundColor = "#c0c0c0" : oPOMakeInvoiceButton.style.backgroundColor = "#0E486E"
}

function vendorPurchaseOrder_showDeleteImage (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td> <img title="Delete" src="images/delete.png" width="20"  id="deleteImageId" onClick="vendorPurchaseOrder_delete('+nIndex+')"/> </td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function vendorPurchaseOrder_validatePONumber ()
{
	var bIsPONumberExist = false;
	var oVendorPurchaseOrderData = new VendorPurchaseOrderData ();
	oVendorPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	VendorPurchaseOrderDataProcessor.list(oVendorPurchaseOrderData, "", "", "", "", 
		function (oResponse)
		{		
			var strPONumber = $("#vendorPurchaseOrder_input_purchaseOrderNumber").val();
			if(m_oVendorPurchaseOrderMemberData.m_strPONumber != strPONumber)
			{
				for(var nIndex=0; nIndex <oResponse.m_arrVendorPurchaseOrderData.length ; nIndex++)
				{
					if(strPONumber == oResponse.m_arrVendorPurchaseOrderData[nIndex].m_strPurchaseOrderNumber)
					{
						informUser("PurchaseOrder Number already exists!", "kWarning");
						$("#vendorPurchaseOrder_input_purchaseOrderNumber").val("");
						$("#vendorPurchaseOrder_input_purchaseOrderNumber").focus ();
						bIsPONumberExist = true;
						break;
					}
				}
			}
		}
	);
	return bIsPONumberExist;
}

function vendorPurchaseOrder_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oVendorPOLineItemData = new VendorPOLineItemData ();
	var nLineItemId = $('#vendorPurchaseOrder_table_itemsDG').datagrid ('getRows')[nIndex].m_nLineItemId;
	oVendorPOLineItemData.m_nLineItemId = nLineItemId > 0 ? nLineItemId : -1;
	if (oVendorPOLineItemData.m_nLineItemId > 0)
		m_oVendorPurchaseOrderMemberData.m_arrDeletedLineItems.push (oVendorPOLineItemData);
	$('#vendorPurchaseOrder_table_itemsDG').datagrid ('deleteRow', nIndex);
	$('#vendorPurchaseOrder_table_itemsDG').datagrid('acceptChanges');
	refreshDataGrid ('#vendorPurchaseOrder_table_itemsDG');
	if ($('#vendorPurchaseOrder_table_itemsDG').datagrid('getRows').length == 0)
		vendorPurchaseOrder_appendRow ();
}

function vendorPurchaseOrder_hideDeleteImage ()
{
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td style="border-style:none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function vendorPurchaseOrder_editRowDG (rowData, rowIndex)
{
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	 if (m_oVendorPurchaseOrderMemberData.m_nEditIndex != rowIndex)
	    {
	        if (vendorPurchaseOrder_endEdit ())
	        {
	        	m_oVendorPurchaseOrderMemberData.m_nEditIndex = rowIndex;
	            $('#vendorPurchaseOrder_table_itemsDG').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
	            vendorPurchaseOrder_setEditing(m_oVendorPurchaseOrderMemberData.m_nEditIndex);
	        } 
	        else 
	            $('#vendorPurchaseOrder_table_itemsDG').datagrid('selectRow', m_oVendorPurchaseOrderMemberData.m_nEditIndex);
	    }
}

function vendorPurchaseOrder_isArticleDuplicate (articleNumberEditor, rowIndex)
{
	assert.isString(articleNumberEditor, "articleNumberEditor expected to be a string.");
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	var arrLineItemData = $('#vendorPurchaseOrder_table_itemsDG').datagrid('getData').rows;
	var bIsArticleDuplicate = false;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		if(articleNumberEditor.toLowerCase() ==  arrLineItemData [nIndex].m_strArticleNumber.toLowerCase() && rowIndex!= nIndex)
		{
			informUser("Duplicate Article in Purchase Order!", "kWarning");
			bIsArticleDuplicate = true;
			break;
		}
	}
	return bIsArticleDuplicate;
}

function vendorPurchaseOrder_addVendor ()
{
	navigate ("addVendor", "widgets/vendormanagement/addVendor.js");
}

function vendorPurchaseOrder_cancel ()
{
	HideDialog ("dialog");
}

function vendorPurchaseOrder_submit ()
{
	if (vendorPurchaseOrder_validate () && vendorPurchaseOrder_validateDGRow ())
	{
		disable ("vendorPurchaseOrder_button_create");
		var oVendorPurchaseOrderData = vendorPurchaseOrder_getFormData ();
		if(document.getElementById("vendorPurchaseOrder_button_create").getAttribute("update") == "false")
			VendorPurchaseOrderDataProcessor.create (oVendorPurchaseOrderData, vendorPurchaseOrder_created);
		else
		{
			oVendorPurchaseOrderData.m_nPurchaseOrderId = m_oVendorPurchaseOrderMemberData.m_nPurchaseOrderId;
			VendorPurchaseOrderDataProcessor.update(oVendorPurchaseOrderData, vendorPurchaseOrder_updated);
		}
	}
}

function vendorPurchaseOrder_saveAndPrint ()
{
	if (vendorPurchaseOrder_validate () && vendorPurchaseOrder_validateDGRow ())
	{
		disable ("vendorPurchaseOrder_button_saveAndPrint");
		var oVendorPurchaseOrderData = vendorPurchaseOrder_getFormData ();
		if(document.getElementById("vendorPurchaseOrder_button_saveAndPrint").getAttribute("update") == "false")
			VendorPurchaseOrderDataProcessor.saveAndPrint (oVendorPurchaseOrderData, vendorPurchaseOrder_savedForPrint);
		else
		{
			oVendorPurchaseOrderData.m_nPurchaseOrderId = m_oVendorPurchaseOrderMemberData.m_nPurchaseOrderId;
			VendorPurchaseOrderDataProcessor.saveAndPrint(oVendorPurchaseOrderData, vendorPurchaseOrder_savedForPrint);
		}
	}
}

function vendorPurchaseOrder_savedForPrint (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		m_oVendorPurchaseOrderMemberData.m_strXMLData = oResponse.m_strXMLData;
		m_oVendorPurchaseOrderMemberData.m_strEmailAddress = oResponse.m_arrVendorPurchaseOrderData[0].m_oVendorData.m_strEmail;
		m_oVendorPurchaseOrderMemberData.m_strSubject = "Vendor Purchase Order Details";
		navigate ('printVendorPurchaseOrder','widgets/vendorpurchaseorder/vendorPurchaseOrderPrint.js');
		try
		{
			vendorPurchaseOrder_handleAftersave ();
		}
		catch(oException){}
	}
	else
	{
		enable ("vendorPurchaseOrder_button_saveAndPrint");
		informUser ("Vendor PurchaseOrder Print Failed", "kError");
	}
		
}

function vendorPurchaseOrder_validate ()
{
	return validateForm("vendorPurchaseOrder_form_id") && vendorPurchaseOrder_validateSelectField () && !vendorPurchaseOrder_validatePONumber ();
}

function vendorPurchaseOrder_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	if(!vendorPurchaseOrder_isValidVendor())
	{
		informUser("Please Select Vendor Name", "kWarning");
		$('#vendorPurchaseOrder_input_vendorName').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function vendorPurchaseOrder_isValidVendor()
{
	var bIsValid = false;
	try
	{
		var strVendor = $('#vendorPurchaseOrder_input_vendorName').combobox('getValue');
		if(strVendor != null && strVendor > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function vendorPurchaseOrder_validateDGRow ()
{
	var bIsValidateDGRow = true;
	vendorPurchaseOrder_acceptDGchanges();
	var arrLineItemData = $('#vendorPurchaseOrder_table_itemsDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oVendorPOLineItemData = new VendorPOLineItemData ();
		var strDesc =  arrLineItemData [nIndex].m_strDesc;
		var nQuantity = arrLineItemData [nIndex].m_nQuantity;
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

function vendorPurchaseOrder_getFormData ()
{
	vendorPurchaseOrder_acceptDGchanges();
	var oVendorPurchaseOrderData = new VendorPurchaseOrderData ();
	oVendorPurchaseOrderData.m_oUserCredentialsData = new UserInformationData ();
	oVendorPurchaseOrderData.m_oVendorData = new VendorData ();
	oVendorPurchaseOrderData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorPurchaseOrderData.m_oVendorData.m_nClientId = $('#vendorPurchaseOrder_input_vendorName').combobox('getValue');
	oVendorPurchaseOrderData.m_strPurchaseOrderNumber = $("#vendorPurchaseOrder_input_purchaseOrderNumber").val();
	oVendorPurchaseOrderData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oVendorPurchaseOrderData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
//	var m_strDate = $('#vendorPurchaseOrder_input_date').datebox('getValue');
	var m_strDate = $('#vendorPurchaseOrder_input_date').val();
	oVendorPurchaseOrderData.m_strPurchaseOrderDate = FormatDate (m_strDate);
//	oVendorPurchaseOrderData.m_arrPurchaseOrderLineItems = vendorPurchaseOrder_getLineItemDataArray ();
	return oVendorPurchaseOrderData;
}

function vendorPurchaseOrder_getLineItemDataArray ()
{
	var arrPurchaseOrderLineItems = new Array ();
	var arrLineItemData = $('#vendorPurchaseOrder_table_itemsDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oVendorPOLineItemData = new VendorPOLineItemData ();
		oVendorPOLineItemData.m_nSerialNumber = $('#vendorPurchaseOrder_table_itemsDG').datagrid('getRowIndex',arrLineItemData[nIndex]);
		oVendorPOLineItemData.m_nLineItemId = Number((arrLineItemData [nIndex].m_nLineItemId) > 0) ? arrLineItemData [nIndex].m_nLineItemId : -1;
		oVendorPOLineItemData.m_oItemData.m_strArticleNumber = arrLineItemData [nIndex].m_strArticleNumber;
		oVendorPOLineItemData.m_nQuantity = arrLineItemData [nIndex].m_nQuantity;
		oVendorPOLineItemData.m_nPrice = arrLineItemData [nIndex].m_nPrice;
		oVendorPOLineItemData.m_nDiscount = arrLineItemData [nIndex].m_nDiscount;
		oVendorPOLineItemData.m_strTaxName = arrLineItemData [nIndex].m_strTaxName;
		oVendorPOLineItemData.m_nTax = arrLineItemData [nIndex].m_nTax;
		oVendorPOLineItemData.m_nReceiveQty = arrLineItemData [nIndex].m_nReceiveQty;
		oVendorPOLineItemData.m_nReceivedQty = arrLineItemData [nIndex].m_nReceivedQty;
		if (oVendorPOLineItemData.m_nQuantity > 0 && oVendorPOLineItemData.m_nPrice > 0)
			arrPurchaseOrderLineItems.push (oVendorPOLineItemData);
	}
	return arrPurchaseOrderLineItems;
}

function vendorPurchaseOrder_created (oResponse)
{
	if (oResponse.m_bSuccess)
		vendorPurchaseOrder_displayInformation ("Vendor Purchase Order Saved Succesfully.");
	else
		informUser (oResponse.m_strError_Desc.length > 0 ? oResponse.m_strError_Desc : "Purchase Order Save Failed.", "kError");
	enable ("vendorPurchaseOrder_button_create");
}

function vendorPurchaseOrder_updated (oResponse)
{
	if(oResponse.m_bSuccess)
		vendorPurchaseOrder_deletePurchaseOrderLineItems ();
	else
		enable ("vendorPurchaseOrder_button_create");
}

function vendorPurchaseOrder_deletePurchaseOrderLineItems ()
{
	if(m_oVendorPurchaseOrderMemberData.m_arrDeletedLineItems.length > 0)
	{
		var oVendorPurchaseOrderData = vendorPurchaseOrder_getFormData ();
		oVendorPurchaseOrderData.m_arrPurchaseOrderLineItems = m_oVendorPurchaseOrderMemberData.m_arrDeletedLineItems;
		VendorPurchaseOrderDataProcessor.deletePurchaseOrderLineItems(oVendorPurchaseOrderData, vendorPurchaseOrder_lineItemsDeleted);
	}
	else
		vendorPurchaseOrder_displayInformation ("Vendor Purchase Order updated successfully.", "kSuccess");
}

function vendorPurchaseOrder_lineItemsDeleted (oResponse)
{
	if(oResponse.m_bSuccess)
		vendorPurchaseOrder_displayInformation ("Vendor Purchase Order updated successfully.", "kSuccess");
	else
		enable ("vendorPurchaseOrder_button_create");
}

function vendorPurchaseOrder_displayInformation (strMessage)
{
	informUser(strMessage, "kSuccess");
	HideDialog ("dialog");
	try
	{
		vendorPurchaseOrder_handleAfterSave ();
	}catch(oException){}
}

function vendorPurchaseOrder_executePurchaseOrder ()
{
	vendorPurchaseOrder_init ();
	document.getElementById("vendorPurchaseOrder_button_create").setAttribute ("update", "true");
	document.getElementById("vendorPurchaseOrder_button_saveAndPrint").setAttribute ("update", "true");
	vendorPurchaseOrder_getPurchaseOrderItem ();
}

function vendorPurchaseOrder_getPurchaseOrderItem ()
{
	var oVendorPurchaseOrderData = new VendorPurchaseOrderData ();
	oVendorPurchaseOrderData.m_nPurchaseOrderId = m_oVendorPurchaseOrderMemberData.m_nPurchaseOrderId;
	VendorPurchaseOrderDataProcessor.get (oVendorPurchaseOrderData, vendorPurchaseOrder_gotPurchaseOrderItem);
}

function vendorPurchaseOrder_gotPurchaseOrderItem (oResponse)
{
	var oPurchaseOrderItem = oResponse.m_arrVendorPurchaseOrderData[0];
	vendorPurchaseOrder_populateToCombobox(oPurchaseOrderItem);
	var nReceivedQty = 0;
	$('#vendorPurchaseOrder_input_vendorName').combobox('select', oPurchaseOrderItem.m_oVendorData.m_nClientId);
	$("#vendorPurchaseOrder_input_purchaseOrderNumber").val(oPurchaseOrderItem.m_strPurchaseOrderNumber);
	m_oVendorPurchaseOrderMemberData.m_strPONumber = oPurchaseOrderItem.m_strPurchaseOrderNumber;
//	$('#vendorPurchaseOrder_input_date').datebox('setValue', oPurchaseOrderItem.m_strPurchaseOrderDate);
	$('#vendorPurchaseOrder_input_date').datepicker('setDate', oPurchaseOrderItem.m_strPurchaseOrderDate);
	var arrPurchaseOrderLineItemData = getOrderedLineItems (oPurchaseOrderItem.m_oVendorPOLineItemData);
	for (var nIndex = 0; nIndex < arrPurchaseOrderLineItemData.length; nIndex++)
	{
		arrPurchaseOrderLineItemData[nIndex].m_strArticleNumber = arrPurchaseOrderLineItemData[nIndex].m_oItemData.m_strArticleNumber;
		arrPurchaseOrderLineItemData[nIndex].m_strDesc = arrPurchaseOrderLineItemData[nIndex].m_oItemData.m_strItemName + " | " + arrPurchaseOrderLineItemData[nIndex].m_oItemData.m_strDetail;
		arrPurchaseOrderLineItemData[nIndex].m_strUnit = arrPurchaseOrderLineItemData[nIndex].m_oItemData.m_strUnit;
		$('#vendorPurchaseOrder_table_itemsDG').datagrid('appendRow',arrPurchaseOrderLineItemData[nIndex]);
	}
	if(nReceivedQty > 0)
	{
		m_oVendorPurchaseOrderMemberData.m_bDisableInvoiceButton = false;
		vendorPurchaseOrder_displayButtons (false, true, false);
		$("#vendorPurchaseOrder_input_purchaseOrderNumber").attr('readonly', 'readonly');
	}
	vendorPurchaseOrder_appendRow ();
	initFormValidateBoxes ("vendorPurchaseOrder_form_id");
}

function vendorPurchaseOrder_populateToCombobox(oVendorPurchaseOrderData)
{
	assert.isObject(oVendorPurchaseOrderData, "oVendorPurchaseOrderData expected to be an Object.");
	assert( Object.keys(oVendorPurchaseOrderData).length >0 , "oVendorPurchaseOrderData cannot be an empty .");// checks for non emptyness 
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName = oVendorPurchaseOrderData.m_oVendorData.m_strCompanyName;
	VendorDataProcessor.getVendorSuggesstions (oVendorData, "", "", function(oResponse)
			{
				var arrVendorInfo = new Array ();
				for(var nIndex=0; nIndex< oResponse.m_arrVendorData.length; nIndex++)
			    {
					arrVendorInfo.push(oResponse.m_arrVendorData[nIndex]);
					arrVendorInfo[nIndex].m_strTo = encodeURIComponent(oResponse.m_arrVendorData[nIndex].m_strCompanyName + " "+"|" +" "+
			    											oResponse.m_arrVendorData[nIndex].m_strTinNumber);
			    }
				$('#vendorPurchaseOrder_input_vendorName').combobox('loadData',arrVendorInfo)
			});
}

function vendorPurchaseOrder_enterInvoice ()
{
	var oVendorPurchaseOrderData = vendorPurchaseOrder_getFormData ();
	oVendorPurchaseOrderData.m_nPurchaseOrderId = m_oVendorPurchaseOrderMemberData.m_nPurchaseOrderId > 0 ? m_oVendorPurchaseOrderMemberData.m_nPurchaseOrderId : -1;
	m_oVendorPurchaseOrderMemberData.m_oVendorPurchaseOrderData = oVendorPurchaseOrderData;
	navigate ("orderToPurchase", "widgets/inventorymanagement/purchase/orderToPurchase.js");
}

function clientInfo_handleAfterSave ()
{
	// Handler Function for Vendor Save.
}

