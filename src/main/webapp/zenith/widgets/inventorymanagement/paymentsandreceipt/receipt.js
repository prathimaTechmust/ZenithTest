var receipt_includeDataObjects = 
[
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/InvoiceReceiptData.js',
	'widgets/inventorymanagement/paymentsandreceipt/ReceiptData.js'
];

includeDataObjects (receipt_includeDataObjects, "receipt_loaded()");

function receipt_memberData ()
{
	this.m_nEditIndex = undefined;
	this.m_nReceiptId = -1;
	this.m_strXMLData = "";
	this.m_oInvoiceData = null;
	this.m_arrDeletedLineItems = new Array ();
}
var m_oReceiptMemberData = new receipt_memberData ();

function receipt_new ()
{
	createPopup("thirdDialog", "#receipt_button_create", "#receipt_button_cancel", true);
	initFormValidateBoxes ("receipt_form_id");
	receipt_init ();
}

function receipt_makeReceiptForInvoice ()
{
	receipt_new ();
	receipt_setValues ();
}

function receipt_setValues ()
{
	receipt_populateClientCombobox(m_oReceiptMemberData.m_oInvoiceData);
	$('#receipt_input_client').combobox('select', m_oReceiptMemberData.m_oInvoiceData.m_oClientData.m_nClientId);
	$('#receipt_input_client').combobox('readonly');
	$("#receipt_input_amount").val( m_oReceiptMemberData.m_oInvoiceData.m_nBalanceAmount);
	m_oReceiptMemberData.m_oInvoiceData.m_nReceived = m_oReceiptMemberData.m_oInvoiceData.m_nBalanceAmount;
	$('#receipt_table_invoiceDG').datagrid('appendRow',m_oReceiptMemberData.m_oInvoiceData);
	receipt_updateBalanceAmount ();
	receipt_enableOrDisableAddButton ();
	initFormValidateBoxes ("receipt_form_id");
}

function receipt_init ()
{
	receipt_initClientCombobox ();
	receipt_populateTransactionModeList ();
	receipt_initDataGrid ();
}

function receipt_initClientCombobox ()
{
	$('#receipt_input_client').combobox
	({
		valueField:'m_nClientId',
	    textField:'m_strCompanyName',
	    selectOnNavigation: false,
	    loader: getFilteredClientData,
	    mode: 'remote',
	    onSelect: function ()
    	{
			receipt_enableOrDisableAddButton ();
			
    	},
		 onChange: function ()
	 	{
			receipt_enableOrDisableAddButton ();
	 	}
	});
	var toTextBox = $('#receipt_input_client').combobox('textbox');
	toTextBox[0].placeholder = "Enter Your Client Name";
	toTextBox.focus ();
	toTextBox.bind('keyup', function(e){
		var nClientId = $('#receipt_input_client').combobox('getText');		
		if(!isNaN(Number(nClientId)))
		{
			console.log(Number(nClientId));
			var toTextBox = $('#receipt_input_client').combobox('textbox');
			$('#receipt_input_client').combobox('setValue','');
			toTextBox[0].placeholder = "Enter Your Client Name";
			toTextBox.focus ();
		}		
	});
}

function receipt_populateTransactionModeList ()
{
	var oTransactionModeData = new TransactionModeData ();
	TransactionModeDataProcessor.list (oTransactionModeData, "", "",
			function (oResponse)
			{
				receipt_prepareTransactionModeDD ("receipt_select_mode", oResponse);
			}				
		);
}

function receipt_prepareTransactionModeDD (strTransactionModeDD, oResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption (-1,"Select"));
	for (var nIndex = 0; nIndex < oResponse.m_arrTransactionMode.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrTransactionMode [nIndex].m_nModeId,
				oResponse.m_arrTransactionMode [nIndex].m_strModeName));
	PopulateDD (strTransactionModeDD, arrOptions);
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
					success(oResponse.m_arrClientData);
				});
	}
	else
		success(new Array ());
}

function receipt_initDataGrid ()
{
	$('#receipt_table_invoiceDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strInvoiceNumber',title:'Invoice No.',width:150},
			  	{field:'m_strDate',title:'Date',width:100,align:'center'},
			  	{field:'m_nInvoiceAmount',title:'Invoice Amount',width:130,align:'right',
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
		        {field:'m_nReceiptAmount',title:'Total Received',width:120,align:'right',
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
		        {field:'m_nReceived',title:'Current Receipt Amt',width:140,align:'right',editor:{'type':'text'},
		        	formatter:function(value,row,index)
		        	{
		        		var nReceived = 0;
		        		try
		        		{
		        			nReceived = Number(value).toFixed(2);
		        			var nIndianFormat = formatNumber (nReceived, row, index);
		        			
		        		}
		        		catch(oException)
		        		{
		        			nReceived.toFixed(2);
		        		}
		        		return '<span class="rupeeSign">R </span>' + nIndianFormat;
		        	}
		        },
			  	{field:'Action',title:'Action',width:40,
		        	formatter:function(value,row,index)
		        	{
		        		if(row.m_strDate == "<b>Total</b>" && index == 0)
		        			return receipt_removeAction ();
		        		else
		        			return receipt_displayImages (row, index);
		        	}
		         } 
			]]
		}
	);
	
	$('#receipt_table_invoiceDG').datagrid
	(
		{
			onClickRow: function (nRowIndex, oRowData)
			{
				receipt_editRowDG (nRowIndex, oRowData);
			}
		}
	)
}

function receipt_displayImages (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oActions = 
		'<table align="center" align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="receipt_delete ('+nIndex+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function receipt_removeAction ()
{
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td style="border-style:none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function receipt_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oInvoiceData = $('#receipt_table_invoiceDG').datagrid ('getRows')[nIndex];
	if(!receipt_isRowAdded (oInvoiceData))
		m_oReceiptMemberData.m_arrDeletedLineItems.push (oInvoiceData);
	$('#receipt_table_invoiceDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#receipt_table_invoiceDG');
	receipt_updateInvoiceDG ();
}

function receipt_isRowAdded (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var bIsRowAdded = false;
	for (var nIndex = 0; !bIsRowAdded && nIndex < m_oReceiptMemberData.m_arrDeletedLineItems.length; nIndex++)
		bIsRowAdded = (m_oReceiptMemberData.m_arrDeletedLineItems [nIndex].m_nInvoiceId == oRowData.m_nInvoiceId);
	return bIsRowAdded;
}

function receipt_endEdit ()
{
    if (m_oReceiptMemberData.m_nEditIndex == undefined)
    	return true
    if ($('#receipt_table_invoiceDG').datagrid('validateRow', m_oReceiptMemberData.m_nEditIndex))
    {
        $('#receipt_table_invoiceDG').datagrid('endEdit',  m_oReceiptMemberData.m_nEditIndex);
        m_oReceiptMemberData.m_nEditIndex = undefined;
        return true;
    } 
    else 
        return false;
}

function receipt_acceptDGchanges ()
{
	if (receipt_endEdit())
	{
		m_oReceiptMemberData.m_nEditIndex = undefined;
		$('#receipt_table_invoiceDG').datagrid('acceptChanges');
	}
}

function receipt_editRowDG (nRowIndex, oRowData)
{
	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	 if (m_oReceiptMemberData.m_nEditIndex != nRowIndex)
	    {
	        if (receipt_endEdit ())
	        {
	        	m_oReceiptMemberData.m_nEditIndex = nRowIndex;
	            $('#receipt_table_invoiceDG').datagrid('selectRow', nRowIndex)
	                    .datagrid('beginEdit', nRowIndex);
	            receipt_setEditing(m_oReceiptMemberData.m_nEditIndex, oRowData);
	        } 
	        else 
	            $('#receipt_table_invoiceDG').datagrid('selectRow', m_oReceiptMemberData.m_nEditIndex);
	    }
}

function receipt_setEditing(nRowIndex, oRowData)
{
	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
    var arrEditors = $('#receipt_table_invoiceDG').datagrid('getEditors', nRowIndex);
    var oReceivedAmountEditor = arrEditors[0];
    $(oReceivedAmountEditor.target).focus();
    oReceivedAmountEditor.target.bind('keyup', function ()
    		{
    			validateFloatNumber (this); 
		    });
    
    oReceivedAmountEditor.target.bind('blur change', function()
    		{
		    	var nAmount = Number(oReceivedAmountEditor.target.val());
		    	if(nAmount <= 0)
		    	{
		    		informUser ("Current Receipt Amount should not be zero", "kWarning");
		    		$(oReceivedAmountEditor.target).val('');
		    		oReceivedAmountEditor.target.focus ();
		    	}
    			if(receipt_validateReceivedAmount (this.value, oRowData) && receipt_validateWithBalanceAmount (this.value, oRowData))
    				receipt_updateBalanceAmount ();
    			else
		    		this.focus();
    		});
}

function receipt_validateReceivedAmount (nReceivedAmount, oRowData)
{
	assert.isString(nReceivedAmount, "nReceivedAmount expected to be a String.");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var bIsValid = nReceivedAmount <= (oRowData.m_nInvoiceAmount - oRowData.m_nReceiptAmount) ;
	if(!bIsValid)
		informUser("Total Received Amount Exceeds Invoice Amount", "kWarning");
	return bIsValid;
}

function receipt_validateWithBalanceAmount (nReceivedAmount, oRowData)
{
	assert.isString(nReceivedAmount, "nReceivedAmount expected to be a String.");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	var nBalanceAmount = receipt_getBalanceAmount (oRowData);
	var bIsValid = nReceivedAmount <= nBalanceAmount ;
	if(!bIsValid)
		informUser("Entered Value Exceeds Available Balance Amount(Balance Available : "+nBalanceAmount+")", "kWarning");
	return bIsValid;
}

function receipt_getBalanceAmount (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var nBalanceAmount = $("#receipt_label_balanceAmount").val();
	var nReceiptAmount = $("#receipt_input_amount").val();
	var nTotalReceived = 0;
	var arrInvoices = $('#receipt_table_invoiceDG').datagrid('getRows');
	for (var nIndex = 0; nIndex < arrInvoices.length; nIndex++)
	{
		if(oRowData.m_nInvoiceId != arrInvoices[nIndex].m_nInvoiceId)
			nTotalReceived += Number(arrInvoices[nIndex].m_nReceived);
	}
	return (Number(nReceiptAmount) - Number(nTotalReceived)) + Number(nBalanceAmount);
}

function receipt_validate ()
{
	return validateForm("receipt_form_id") && receipt_validateSelectField () && receipt_validateInvoiceDG ();
}

function receipt_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	var nClientId = $('#receipt_input_client').combobox('getValue');
	if(isNaN(nClientId))
	{
		informUser("Please Select Client from list", "kWarning");
		bIsSelectFieldValid = false;
	}
	
	if($("#receipt_select_mode").val()== -1)          
	{
		informUser("Please Select Transaction Mode", "kWarning");
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function receipt_validateInvoiceDG ()
{
	var bIsValid = true;
	var nCount = 0;
	var arrInvoiceData = $('#receipt_table_invoiceDG').datagrid('getData').rows;
	for(nIndex = 0; nIndex < arrInvoiceData.length; nIndex ++)
	{
		if(arrInvoiceData[nIndex].m_nReceived <= 0)
			nCount++;
	}
	if(nCount > 0)
		bIsValid = getUserConfirmation("Some Entries have Zero value in Current Receipt Amount which will not be Saved. Do you want to continue ?")
	return bIsValid;
}

function receipt_getFormData ()
{
	receipt_acceptDGchanges ();
	var oReceiptData = new ReceiptData ()
	oReceiptData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReceiptData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oReceiptData.m_oClientData.m_nClientId = $('#receipt_input_client').combobox('getValue');
	oReceiptData.m_nAmount = $("#receipt_input_amount").val();
	oReceiptData.m_oMode.m_nModeId = $("#receipt_select_mode").val();
	oReceiptData.m_strDetails = $("#receipt_textarea_details").val();
	oReceiptData.m_strReceiptNumber = $("#receipt_select_receiptNumber").val();
	oReceiptData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReceiptData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReceiptData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oReceiptData.m_arrInvoiceReceipt = receipt_getInvoiceReceiptArray ();
	return oReceiptData;
}

function receipt_getInvoiceReceiptArray ()
{
	var arrInvoiceReceipt = new Array ();
	var arrInvoiceData = $('#receipt_table_invoiceDG').datagrid('getData').rows;
	for(nIndex = 0; nIndex < arrInvoiceData.length; nIndex ++)
	{
		var oInvoiceReceiptData = new InvoiceReceiptData ();
		oInvoiceReceiptData.m_nSerialNumber = $('#receipt_table_invoiceDG').datagrid('getRowIndex',arrInvoiceData[nIndex]);
		oInvoiceReceiptData.m_nAmount = Number(arrInvoiceData[nIndex].m_nReceived);
		oInvoiceReceiptData.m_oInvoiceData.m_nInvoiceId = arrInvoiceData[nIndex].m_nInvoiceId;
		if(oInvoiceReceiptData.m_nAmount > 0)
			arrInvoiceReceipt.push(oInvoiceReceiptData);
	}
	return arrInvoiceReceipt;
}

function receipt_create ()
{
	if (receipt_validate ())
		loadPage ("include/process.html", "ProcessDialog", "receipt_create_progressbarLoaded ()");
}

function receipt_create_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	var oReceiptData = receipt_getFormData ();
	if(document.getElementById("receipt_button_create").getAttribute('update') == "false")
		ReceiptDataProcessor.create (oReceiptData, receipt_created);
	else
	{
		oReceiptData.m_nReceiptId = m_oReceiptListMemberData.m_nSelectedReceiptId;
		ReceiptDataProcessor.update (oReceiptData, receipt_updated);
	}
}

function receipt_created (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser("Receipt Created successfully.", "kSuccess");
		HideDialog ("thirdDialog");
		try
		{
			receipt_handleAfterSave ();
		}
		catch (oException)
		{
			
		}
	}
	else
	{
		informUser("Receipt Creation Failed", "kError");
		enable ("receipt_button_create");
	}
}

function receipt_updated (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser("Receipt Updated successfully.", "kSuccess");
		HideDialog ("thirdDialog");
		navigate ("", "widgets/inventorymanagement/paymentsandreceipt/receiptListAdmin.js");
	}
	else
	{
		informUser("Receipt Updation Failed", "kError");
		enable ("receipt_button_create");
	}
}

function receipt_addInvoice ()
{
	navigate ("", "widgets/inventorymanagement/paymentsandreceipt/invoiceAdd.js");
}

function receipt_cancel ()
{
	HideDialog("thirdDialog");
}

function receipt_updateInvoiceDG ()
{
	receipt_acceptDGchanges ();
	receipt_enableOrDisableAddButton ();
	var arrInvoices = $('#receipt_table_invoiceDG').datagrid('getRows');
	var nCount = 0;
	var nReceiptAmount = $("#receipt_input_amount").val();
	for (var nIndex = 0; nIndex < arrInvoices.length; nIndex++)
	{
		var nReceived = receipt_getReceivedAmount (nReceiptAmount, arrInvoices[nIndex].m_nInvoiceAmount, arrInvoices[nIndex].m_nReceiptAmount, nCount);
		nCount += nReceived ;
		$('#receipt_table_invoiceDG').datagrid('updateRow',
				{
					index: nIndex,
					row: 
					{
						m_nReceived : nReceived
					}
				});
	}
	receipt_updateBalanceAmount ();
}
function receipt_getReceivedAmount (nReceiptAmount, nInvoiceTotal, nReceivedAmount, nCount)
{
	assert.isString(nReceiptAmount, "nReceiptAmount expected to be a String.");
	assert.isNumber(nInvoiceTotal, "nInvoiceTotal expected to be a Number.");
	assert.isNumber(nReceivedAmount, "nReceivedAmount expected to be a Number.");
	assert.isNumber(nCount, "nCount expected to be a Number.");
	return (nReceiptAmount - nCount - (nInvoiceTotal - nReceivedAmount)) > 0 ? (nInvoiceTotal - nReceivedAmount) : (nReceiptAmount - nCount);
}

function receipt_enableOrDisableAddButton ()
{
	var nClientId = $('#receipt_input_client').combobox('getValue');
	var oAddButton = document.getElementById("receipt_button_add");
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

function receipt_updateBalanceAmount ()
{
	receipt_acceptDGchanges ();
	var arrInvoices = $('#receipt_table_invoiceDG').datagrid('getRows');
	var nReceiptAmount =$("#receipt_input_amount").val();
	var nTotalCurrentReceived = 0;
	var nTotalReceived = 0;
	var nTotalInvoiceAmount = 0;
	for (var nIndex = 0; nIndex < arrInvoices.length; nIndex++)
	{
		nTotalCurrentReceived += Number(arrInvoices[nIndex].m_nReceived);
		nTotalReceived += Number(arrInvoices[nIndex].m_nReceiptAmount);
		nTotalInvoiceAmount += Number(arrInvoices[nIndex].m_nInvoiceAmount);
	}
	$("#receipt_label_balanceAmount").val((nReceiptAmount - nTotalCurrentReceived).toFixed(2));
	receipt_loadFooterDG (nTotalCurrentReceived, nTotalReceived, nTotalInvoiceAmount);
}

function receipt_loadFooterDG (nTotalCurrentReceived, nTotalReceived, nTotalInvoiceAmount)
{
	assert.isNumber(nTotalCurrentReceived, "nTotalCurrentReceived expected to be a Number.");
	assert.isNumber(nTotalReceived, "nTotalReceived expected to be a Number.");
	assert.isNumber(nTotalInvoiceAmount, "nTotalInvoiceAmount expected to be a Number.");
	$('#receipt_table_invoiceDG').datagrid('reloadFooter',[{m_strDate:'<b>Total</b>', m_nInvoiceAmount:nTotalInvoiceAmount, m_nReceiptAmount:nTotalReceived, m_nReceived: nTotalCurrentReceived}]);
}

function receipt_edit ()
{
	createPopup('ProcessDialog', '', '', true);
	var oReceiptData = new ReceiptData ()
	oReceiptData.m_nReceiptId = m_oReceiptMemberData.m_nReceiptId;
	oReceiptData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oReceiptData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	createPopup ("thirdDialog", "#receipt_button_create", "#receipt_button_cancel", true);
	receipt_init ();
	document.getElementById("receipt_button_create").setAttribute('update', true);
	document.getElementById("receipt_button_create").innerHTML = "Update";
	document.getElementById("receipt_button_saveAndPrint").setAttribute('update', true);
	document.getElementById("receipt_button_saveAndPrint").innerHTML = "Update & Print";
	ReceiptDataProcessor.get (oReceiptData, receipt_gotData)
}

function receipt_gotData (oResponse)
{
	HideDialog ("ProcessDialog");
	var oReceiptData = oResponse.m_arrReceiptData[0];
	var arrInvoiceData = new Array ();
	receipt_populateClientCombobox(oReceiptData);
	$('#receipt_input_client').combobox('select', oReceiptData.m_oClientData.m_nClientId);
	$("#receipt_select_mode").val(oReceiptData.m_oMode.m_nModeId);
	$("#receipt_input_amount").val(oReceiptData.m_nAmount);
	$("#receipt_textarea_details").val(oReceiptData.m_strDetails);
	$("#receipt_select_receiptNumber").val(oReceiptData.m_strReceiptNumber);
	initFormValidateBoxes ("receipt_form_id");
	var arrInvoiceReceipts =  getOrderedLineItems(oReceiptData.m_oInvoiceReceipts);
	for(nIndex = 0; nIndex < arrInvoiceReceipts.length; nIndex++)
	{
		var oInvoiceData = arrInvoiceReceipts[nIndex].m_oInvoiceData;
		oInvoiceData.m_nInvoiceAmount = oInvoiceData.m_nBalanceAmount + oInvoiceData.m_nReceiptAmount ;
		oInvoiceData.m_nReceiptAmount -= arrInvoiceReceipts[nIndex].m_nAmount;
		oInvoiceData.m_nReceived = arrInvoiceReceipts[nIndex].m_nAmount;
		arrInvoiceData.push(oInvoiceData);
	}
	$('#receipt_table_invoiceDG').datagrid('loadData',arrInvoiceData);
	receipt_updateBalanceAmount ();
	receipt_enableOrDisableAddButton ();
}

function receipt_populateClientCombobox(oReceiptData)
{
	assert.isObject(oReceiptData, "oReceiptData expected to be an Object.");
	assert( Object.keys(oReceiptData).length >0 , "oReceiptData cannot be an empty .");// checks for non emptyness 
	var oClientData = new ClientData ();
	oClientData.m_strCompanyName = oReceiptData.m_oClientData.m_strCompanyName;
	ClientDataProcessor.getClientSuggesstions (oClientData, "", "", "", "", function(oResponse)
			{
				$('#receipt_input_client').combobox('loadData',oResponse.m_arrClientData)
			});
	
}

function receipt_saveAndPrint ()
{
	if (receipt_validate ())
		loadPage ("include/process.html", "ProcessDialog", "receipt_saveAndPrint_progressbarLoaded ()");
}

function receipt_saveAndPrint_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	disable ("receipt_button_saveAndPrint");
	var oReceiptData = receipt_getFormData ();
	if(document.getElementById("receipt_button_saveAndPrint").getAttribute('update') == "false")
		ReceiptDataProcessor.saveAndPrint (oReceiptData, receipt_savedForPrint );
	else
	{
		oReceiptData.m_nReceiptId = m_oReceiptListMemberData.m_nSelectedReceiptId;
		ReceiptDataProcessor.saveAndPrint(oReceiptData, receipt_savedForPrint);
	}
}
function receipt_savedForPrint (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		HideDialog ("thirdDialog");
		m_oReceiptMemberData.m_strXMLData = oResponse.m_strXMLData;
		m_oReceiptMemberData.m_strEmailAddress = oResponse.m_arrReceiptData[0].m_oClientData.m_strEmail;
		m_oReceiptMemberData.m_strSubject = "Receipt Details";
		navigate ('Print Receipt','widgets/inventorymanagement/paymentsandreceipt/receiptPrint.js');
	}
	else
		informUser ("Receipt Print Failed", "kError");
}
