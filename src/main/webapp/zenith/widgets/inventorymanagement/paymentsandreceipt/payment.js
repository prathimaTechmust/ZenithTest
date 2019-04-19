var payment_includeDataObjects = 
[
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js',
	'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/PaymentData.js',
	'widgets/inventorymanagement/paymentsandreceipt/PurchasePaymentData.js'
];

includeDataObjects (payment_includeDataObjects, "payment_loaded()");

function payment_memberData ()
{
	this.m_nEditIndex = undefined;
	this.m_nPaymentId = -1;
	this.m_strXMLData = "";
	this.m_strEmailAddress = "";
	this.m_strSubject = "";
	this.m_strHTML = "";
	this.m_nSelectedId = -1;
	this.m_oPurchaseData= null;
	this.m_arrDeletedLineItems = new Array ();
	this.m_nInvalidChars = /[^0-9.]/gi;
}
var m_oPaymentMemberData = new payment_memberData ();

function payment_new ()
{
	createPopup("dialog", "#payment_button_create", "#payment_button_cancel", true);
	initFormValidateBoxes ("payment_form_id");
	payment_init ();
}

function payment_init ()
{
	payment_initVendorCombobox ();
	payment_populateTransactionModeList ();
	payment_initDataGrid ();
}

function payment_makePaymentPurchase ()
{
	payment_new ();
	payment_setValues ();
}

function payment_setValues ()
{
	payment_populateClientCombobox (m_oPaymentMemberData.m_oPurchaseData);
	$('#payment_input_vendor').combobox('select', m_oPaymentMemberData.m_oPurchaseData.m_oVendorData.m_nClientId);
	$('#payment_input_vendor').combobox('readonly');
	$("#payment_input_amount").val(m_oPaymentMemberData.m_oPurchaseData.m_nBalanceAmount);
	m_oPaymentMemberData.m_oPurchaseData.m_nPaid = m_oPaymentMemberData.m_oPurchaseData.m_nBalanceAmount;
	$('#payment_table_purchaseDG').datagrid('loadData',m_oPaymentMemberData.m_oPurchaseData);
	payment_updateBalanceAmount ();
	payment_enableOrDisableAddButton ();
	initFormValidateBoxes ("payment_form_id");
}

function payment_initVendorCombobox ()
{
	$('#payment_input_vendor').combobox
	({
		valueField:'m_nClientId',
	    textField:'m_strCompanyName',
	    selectOnNavigation: false,
	    loader: getFilteredVendorData,
	    mode: 'remote',
	    onSelect: function ()
    	{
			payment_enableOrDisableAddButton ();
    	},
		 onChange: function ()
	 	{
    		payment_enableOrDisableAddButton ();
//    		var editors = $('#payment_input_vendor').datagrid('getEditors', m_oPaymentMemberData.nEditIndex);
//			var vendorNoEditor = editors[0];
//			$(vendorNoEditor.target).combobox('setValue','');
//			$(vendorNoEditor.target).combobox('textbox').focus();
//   
   		
	 	}
	});
	var toTextBox = $('#payment_input_vendor').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Vendor Name";
	toTextBox.focus ();
	toTextBox.bind('keyup', function(e){
		var nClientId = $('#payment_input_vendor').combobox('getText');		
		if(!isNaN(Number(nClientId)))
		{
			console.log(Number(nClientId));
			var toTextBox = $('#payment_input_vendor').combobox('textbox');
			$('#payment_input_vendor').combobox('setValue','');
			toTextBox[0].placeholder = "Enter Your Vendor Name";
			toTextBox.focus ();
		}		
	});
}

function payment_populateTransactionModeList ()
{
	var oTransactionModeData = new TransactionModeData ();
	TransactionModeDataProcessor.list (oTransactionModeData, "", "", payment_listed);		
}

function payment_listed(oResponse)
{
	payment_prepareTransactionModeDD ("payment_select_mode", oResponse);
}

function payment_prepareTransactionModeDD (strTransactionModeDD, oResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption (-1,"Select"));
	for (var nIndex = 0; nIndex < oResponse.m_arrTransactionMode.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrTransactionMode [nIndex].m_nModeId,
				oResponse.m_arrTransactionMode [nIndex].m_strModeName));
	PopulateDD (strTransactionModeDD, arrOptions);
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
		VendorDataProcessor.getVendorSuggesstions (oVendorData, "", "", payment_gotVendorSuggesstions);
	}
	else
		success(new Array ());
}

function payment_gotVendorSuggesstions (oResponse)
{
	var arrVendorData = new Array ();
	for(var nIndex=0; nIndex< oResponse.m_arrVendorData.length; nIndex++)
		arrVendorData.push(oResponse.m_arrVendorData[nIndex]);
	success(arrVendorData);
}

function payment_initDataGrid ()
{
	$('#payment_table_purchaseDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strInvoiceNo',title:'Invoice No.',sortable:true,width:150},
			  	{field:'m_strDate',title:'Date',sortable:true,width:100,align:'center'},
			  	{field:'m_nTotalAmount',title:'Invoice Amount',sortable:true,width:150,align:'right',
			  		formatter:function(value,row,index)
		        	{
			  			var nTotal = value;
			  			try
			  			{
			  				var nIndianFormat = formatNumber (nTotal.toFixed(2),row,index);
			  				nTotal = '<span class="rupeeSign">R  </span>' + nIndianFormat;
			  			}
						catch (oException)
						{
							
						}
						return nTotal;
		        	}
		        },
		        {field:'m_nPaymentAmount',title:'Total Paid',width:120,align:'right',
		        	formatter:function(value,row,index)
		        	{
			  			var nTotal = value;
			  			try
			  			{
			  				var nIndianFormat = formatNumber (nTotal.toFixed(2),row,index);
			  				nTotal = '<span class="rupeeSign">R  </span>' + nIndianFormat;
			  			}
						catch (oException)
						{
							
						}
						return nTotal;
		        	}
		        },
			  	{field:'m_nPaid',title:'Current Payment Amount',sortable:true,width:120,align:'right',editor:{'type':'text'},
		        	formatter:function(value,row,index)
		        	{
				  		var nPaid = 0;
		        		try
		        		{
		        			nPaid = Number(value).toFixed(2);
		        		}
		        		catch(oException)
		        		{
		        			nPaid.toFixed(2);
		        		}
		        		return '<span class="rupeeSign">R </span>' + nPaid
		        	}	
			  	},
			  	{field:'Action',title:'Action',width:40,
			  		formatter:function(value,row,index)
		        	{
			  			if(row.m_strDate == "<b>Total</b>" && index == 0)
		        			return payment_removeAction ();
		        		else
		        			return payment_displayImages (row, index);
		        	}
		         } 
			]]
		}
	);
	$('#payment_table_purchaseDG').datagrid
	(
		{
			onClickRow: function (nRowIndex, oRowData)
			{
				payment_editRowDG (nRowIndex, oRowData);
			}
		}
	)
	
}

function payment_displayImages (oRow, nIndex)
{
	assert.isObject(oRow, "oRow expected to be an Object.");
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="payment_delete ('+nIndex+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function payment_removeAction ()
{
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td style="border-style:none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function payment_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oPurchaseData = $('#payment_table_purchaseDG').datagrid ('getRows')[nIndex];
	if(!payment_isRowAdded (oPurchaseData))
		m_oPaymentMemberData.m_arrDeletedLineItems.push (oPurchaseData);
	$('#payment_table_purchaseDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#payment_table_purchaseDG');
	payment_updateInvoiceDG ();
}

function payment_isRowAdded (oRowData)
{
 	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_oPaymentMemberData.m_arrDeletedLineItems.length; nIndex++)
		bIsRowAdded = (m_oPaymentMemberData.m_arrDeletedLineItems [nIndex].m_nId == oRowData.m_nId);
	return bIsRowAdded;
}

function payment_updateInvoiceDG ()
{
	payment_acceptDGchanges ();
	payment_enableOrDisableAddButton ();
	var arrPurchases = $('#payment_table_purchaseDG').datagrid('getRows');
	var nCount = 0;
	var nPaymentAmount = $("#payment_input_amount").val();
	for (var nIndex = 0; nIndex < arrPurchases.length; nIndex++)
	{
		var nPaid = payment_getReceivedAmount (nPaymentAmount, arrPurchases[nIndex].m_nTotalAmount, arrPurchases[nIndex].m_nPaymentAmount, nCount);
		nCount += nPaid ;
		$('#payment_table_purchaseDG').datagrid('updateRow',{
			index: nIndex,
			row: 
			{
				m_nPaid : nPaid
			}
		});
	}
	payment_updateBalanceAmount ();
}

function payment_getReceivedAmount (nPaymentAmount, nTotalAmount, nPaidAmount, nCount)
{
    assert.isString(nPaymentAmount, "nPaymentAmount is expected to be of type string");
	assert.isNumber(nTotalAmount, "nTotalAmount is expected to be of type number");
	assert.isNumber(nPaidAmount, "nPaidAmount is expected to be of type number");
	assert.isNumber(nCount, "nCount is expected to be of type number");
	return (nPaymentAmount - nCount - (nTotalAmount-nPaidAmount)) > 0 ? (nTotalAmount-nPaidAmount) : (nPaymentAmount - nCount);
}

function payment_enableOrDisableAddButton ()
{
	var nClientId = $('#payment_input_vendor').combobox('getValue');
	var oAddButton = document.getElementById("payment_button_add");
	if (nClientId > 0)
	{
		oAddButton.disabled = false;
		oAddButton.style.backgroundColor = "#0E486E"
	}
	else
	{
		oAddButton.disabled = true;
		oAddButton.style.backgroundColor = "#c0c0c0"
	}
}

function payment_endEdit ()
{
    if (m_oPaymentMemberData.m_nEditIndex == undefined)
    	return true
    if ($('#payment_table_purchaseDG').datagrid('validateRow', m_oPaymentMemberData.m_nEditIndex))
    {
        $('#payment_table_purchaseDG').datagrid('endEdit',  m_oPaymentMemberData.m_nEditIndex);
        m_oPaymentMemberData.m_nEditIndex = undefined;
        return true;
    } 
    else 
        return false;
}

function payment_acceptDGchanges()
{
	if (payment_endEdit())
	{
		 m_oPaymentMemberData.m_nEditIndex = undefined;
        $('#payment_table_purchaseDG').datagrid('acceptChanges');
	}
}

function payment_editRowDG (nRowIndex, oRowData)
{
    assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	 if (m_oPaymentMemberData.m_nEditIndex != nRowIndex)
	    {
	        if (payment_endEdit ())
	        {
	        	m_oPaymentMemberData.m_nEditIndex = nRowIndex;
	            $('#payment_table_purchaseDG').datagrid('selectRow', nRowIndex)
	                    .datagrid('beginEdit', nRowIndex);
	            payment_setEditing(m_oPaymentMemberData.m_nEditIndex, oRowData);
	        } 
	        else 
	            $('#payment_table_purchaseDG').datagrid('selectRow', m_oPaymentMemberData.m_nEditIndex);
	    }
}

function payment_setEditing(nRowIndex, oRowData)
{
 	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	assert.isOk(nRowIndex > -1, "nRowIndex must be a positive value.");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
    var arrEditors = $('#payment_table_purchaseDG').datagrid('getEditors', nRowIndex);
    var oPaidAmountEditor = arrEditors[0];
    $(oPaidAmountEditor.target).focus();
    oPaidAmountEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
		    });
    
    oPaidAmountEditor.target.bind('blur', function()
    		{
		    	if(payment_validateReceivedAmount (this.value, oRowData) && payment_validateWithBalanceAmount (this.value, oRowData))
		    		payment_updateBalanceAmount ();
				else
					this.focus();
    		});
    
    oPaidAmountEditor.target.bind('change', function()
    		{
    			if(payment_validateReceivedAmount (this.value, oRowData) && payment_validateWithBalanceAmount (this.value, oRowData))
		    		payment_updateBalanceAmount ();
		    	else
		    		this.focus();
    		});
}

function payment_validateReceivedAmount (nPaymentAmount, oRowData)
{
    assert.isString(nPaymentAmount, "nPaymentAmount is expected to be of type String");
	assert(nPaymentAmount !== "", "nPaymentAmount cannot be an empty string");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var bIsValid = nPaymentAmount <= (oRowData.m_nTotalAmount - oRowData.m_nPaymentAmount) ;
	if(!bIsValid)
		informUser("Total Paid Amount Exceeds Invoice Amount", "kWarning"); 
	return bIsValid;
}

function payment_validateWithBalanceAmount (nPaymentAmount, oRowData)
{
 	assert.isString(nPaymentAmount, "nPaymentAmount expected to be a string.");
	assert(nPaymentAmount !== "", "nPaymentAmount cannot be an empty string");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var nBalanceAmount = payment_getBalanceAmount (oRowData);
	var bIsValid = nPaymentAmount <= nBalanceAmount ;
	if(!bIsValid)
		informUser("Entered Value Exceeds Available Balance Amount(Balance Available : "+nBalanceAmount+")", "kWarning");
	return bIsValid;
}

function payment_getBalanceAmount (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var nBalanceAmount = $("#payment_label_balanceAmount").val();
	var nPaymentAmount = $("#payment_input_amount").val();
	var nTotalPaid = 0;
	var arrInvoices = $('#payment_table_purchaseDG').datagrid('getRows');
	for (var nIndex = 0; nIndex < arrInvoices.length; nIndex++)
	{
		if(oRowData.m_nId != arrInvoices[nIndex].m_nId)
			nTotalPaid += Number(arrInvoices[nIndex].m_nPaid);
	}
	return (Number(nPaymentAmount) - Number(nTotalPaid)) + Number(nBalanceAmount);
}

function payment_updateBalanceAmount ()
{
	payment_acceptDGchanges ();
	var arrPurchases = $('#payment_table_purchaseDG').datagrid('getRows');
	var nPaymentAmount =$("#payment_input_amount").val();
	var nTotalCurrentPaid = 0;
	var nTotalPaid = 0;
	var nTotalInvoiceAmount = 0;
	for (var nIndex = 0; nIndex < arrPurchases.length; nIndex++)
	{
		nTotalCurrentPaid += Number(arrPurchases[nIndex].m_nPaid);
		nTotalPaid += Number(arrPurchases[nIndex].m_nPaymentAmount);
		nTotalInvoiceAmount += Number(arrPurchases[nIndex].m_nTotalAmount);
	}
	$("#payment_label_balanceAmount").val(nPaymentAmount - nTotalCurrentPaid.toFixed(2));
	payment_loadFooterDG (nTotalCurrentPaid, nTotalPaid, nTotalInvoiceAmount);
}

function payment_loadFooterDG (nTotalCurrentPaid, nTotalPaid, nTotalInvoiceAmount)
{
 	assert.isNumber(nTotalCurrentPaid, "nTotalCurrentPaid expected to be a Number.");
	assert.isNumber(nTotalPaid, "nTotalPaid expected to be a Number.");
	assert.isNumber(nTotalInvoiceAmount, "nTotalInvoiceAmount expected to be a Number.");
	$('#payment_table_purchaseDG').datagrid('reloadFooter',[{m_strDate:'<b>Total</b>', m_nTotalAmount:nTotalInvoiceAmount, m_nPaymentAmount:nTotalPaid, m_nPaid: nTotalCurrentPaid}]);
}

function payment_validate ()
{
	return validateForm("payment_form_id") && payment_validateSelectField () && payment_validateInvoiceDG ();
}

function payment_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	var nClientId = $('#payment_input_vendor').combobox('getValue');
	if(isNaN(nClientId))
	{
		informUser("Please Select Vendor from list", "kWarning");
		bIsSelectFieldValid = false;
	}
	
	if($("#payment_select_mode").val()== -1)
	{
		informUser("Please Select Transaction Mode", "kWarning");
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function payment_validateInvoiceDG ()
{
	var bIsValid = true;
	var nCount = 0;
	var arrInvoiceData = $('#payment_table_purchaseDG').datagrid('getData').rows;
	for(nIndex = 0; nIndex < arrInvoiceData.length; nIndex ++)
	{
		if(arrInvoiceData[nIndex].m_nPaid <= 0)
			nCount++;
	}
	if(nCount > 0)
		bIsValid = getUserConfirmation("Some Entries have Zero value in Current Paid Amount which will not be Saved. Do you want to continue ?")
	return bIsValid;
}

function payment_getFormData ()
{
	payment_acceptDGchanges ();
	var oPaymentData = new PaymentData ();
	oPaymentData.m_oVendorData.m_nClientId = $('#payment_input_vendor').combobox('getValue');
	oPaymentData.m_nAmount = $("#payment_input_amount").val();
	oPaymentData.m_oMode.m_nModeId =$("#payment_select_mode").val();
	oPaymentData.m_strDetails = $("#payment_textarea_details").val();
	oPaymentData.m_strPaymentNumber = $("#payment_select_paymentNumber").val();
	oPaymentData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPaymentData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPaymentData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oPaymentData.m_arrPurchases = payment_getPurchases ();
	return oPaymentData;
}

function payment_getPurchases ()
{
	var arrPurchases = new Array ();
	var arrPurchaseData = $('#payment_table_purchaseDG').datagrid('getRows');
	for(nIndex = 0; nIndex < arrPurchaseData.length; nIndex ++)
	{
		var oPurchasePaymentData = new PurchasePaymentData ();
		oPurchasePaymentData.m_nSerialNumber = $('#payment_table_purchaseDG').datagrid('getRowIndex',arrPurchaseData[nIndex]);
		oPurchasePaymentData.m_oPurchaseData.m_nId = arrPurchaseData[nIndex].m_nId;
		oPurchasePaymentData.m_nAmount = Number(arrPurchaseData[nIndex].m_nPaid);
		if(oPurchasePaymentData.m_nAmount > 0)
			arrPurchases.push(oPurchasePaymentData);
	}
	return arrPurchases;
}

function payment_create ()
{
	if (payment_validate ())
		loadPage ("include/process.html", "ProcessDialog", "payment_create_progressbarLoaded ()");
}

function payment_create_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	var oPaymentData = payment_getFormData ();
	if(document.getElementById("payment_button_create").getAttribute('update') == "false")
		PaymentDataProcessor.create (oPaymentData, payment_created);
	else
	{
		oPaymentData.m_nPaymentId = m_oPaymentListMemberData.m_nSelectedPaymentId;
		PaymentDataProcessor.update (oPaymentData, payment_updated);
	}
}
function payment_created (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser("Payment Created successfully.", "kSuccess");
		HideDialog ("dialog");
		try
		{
			payment_handleAfterSave ();
		}
		catch (oException)
		{
			
		}
	}
	else
	{
		informUser("Payment Creation Failed", "kError");
		enable ("payment_button_create");
	}
}

function payment_updated (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser("Payment Updated successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate ("", "widgets/inventorymanagement/paymentsandreceipt/paymentListAdmin.js");
	}
	else
	{
		informUser("Payment Updation Failed", "kError");
		enable ("payment_button_create");
	}
}

function payment_addPurchases ()
{
	navigate ("", "widgets/inventorymanagement/paymentsandreceipt/purchaseAdd.js");
}

function payment_cancel ()
{
	HideDialog("dialog");
}

function payment_edit ()
{
	createPopup('ProcessDialog', '', '', true);
	var oPaymentData = new PaymentData ()
	oPaymentData.m_nPaymentId = m_oPaymentMemberData.m_nPaymentId;
	oPaymentData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPaymentData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	createPopup ("dialog", "#payment_button_create", "#payment_button_cancel", true);
	payment_init ();
	document.getElementById("payment_button_create").setAttribute('update', true);
	document.getElementById("payment_button_create").innerHTML = "Update";
	document.getElementById("payment_button_saveAndPrint").setAttribute('update', true);
	document.getElementById("payment_button_saveAndPrint").innerHTML = "Update & Print";
	PaymentDataProcessor.get (oPaymentData,payment_gotData)
}

function payment_gotData (oResponse)
{
	HideDialog ("ProcessDialog");
	var oPaymentData = oResponse.m_arrPaymentData[0];
	var arrPurchase = new Array ();
	payment_populateClientCombobox(oPaymentData);
	$('#payment_input_vendor').combobox('select', oPaymentData.m_oVendorData.m_nClientId);
	$("#payment_select_mode").val(oPaymentData.m_oMode.m_nModeId);
	$("#payment_input_amount").val(oPaymentData.m_nAmount);
	$("#payment_textarea_details").val(oPaymentData.m_strDetails);
	$("#payment_select_pa" +
			"ymentNumber").val(oPaymentData.m_strPaymentNumber);
	initFormValidateBoxes ("payment_form_id");
	var arrPaymentData =  getOrderedLineItems(oPaymentData.m_oPurchases);
	for(nIndex = 0; nIndex < arrPaymentData.length; nIndex++)
	{
		var oPurchaseData = arrPaymentData[nIndex].m_oPurchaseData;
		oPurchaseData.m_nPaymentAmount -= arrPaymentData[nIndex].m_nAmount;
		oPurchaseData.m_nPaid = arrPaymentData[nIndex].m_nAmount;
		arrPurchase.push(oPurchaseData);
	}
	$('#payment_table_purchaseDG').datagrid('loadData',arrPurchase);
	payment_updateBalanceAmount ();
	payment_enableOrDisableAddButton ();
}

function payment_populateClientCombobox(oPaymentData)
{
	assert.isObject(oPaymentData, "oPaymentData expected to be an Object.");
	assert( Object.keys(oPaymentData).length >0 , "oPaymentData cannot be an empty .");// checks for non emptyness 
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName = oPaymentData.m_oVendorData.m_strCompanyName;
	VendorDataProcessor.getVendorSuggesstions (oVendorData, "", "", payment_gotVendorSuggesstions)
		//	{
			//	$('#payment_input_vendor').combobox('loadData',oResponse.m_arrVendorData);
		//	});
}
function payment_gotVendorSuggesstions (oResponse){
	
	
		$('#payment_input_vendor').combobox('loadData',oResponse.m_arrVendorData);
	
	
}
function payment_saveAndPrint ()
{
	if (payment_validate ())
		loadPage ("include/process.html", "ProcessDialog", "payment_saveAndPrint_progressbarLoaded ()");
}

function payment_saveAndPrint_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("payment_button_saveAndPrint");
	var oPaymentData = payment_getFormData ();
	if(document.getElementById("payment_button_saveAndPrint").getAttribute('update') == "false")
		PaymentDataProcessor.saveAndPrint (oPaymentData, payment_savedForPrint );
	else
	{
		oPaymentData.m_nPaymentId = m_oPaymentListMemberData.m_nSelectedPaymentId;
		PaymentDataProcessor.saveAndPrint(oPaymentData, payment_savedForPrint);
	}
}

function payment_savedForPrint (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		HideDialog ("dialog");
		m_oPaymentMemberData.m_strXMLData = oResponse.m_strXMLData;
		m_oPaymentMemberData.m_strEmailAddress = oResponse.m_arrPaymentData[0].m_oVendorData.m_strEmail;
		m_oPaymentMemberData.m_strSubject = "Payment Details";
		navigate ('Payment Print','widgets/inventorymanagement/paymentsandreceipt/paymentPrint.js');
	}
	else
		informUser ("Payment Print Failed", "kError");
}  