var transactionModeList_includeDataObjects = 
[
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js'
];



 includeDataObjects (transactionModeList_includeDataObjects, "transactionModeList_loaded()");

function transactionModeList_loaded ()
{
	loadPage ("inventorymanagement/paymentsandreceipt/transactionModeList.html", "workarea", "transactionModeList_init ()");
}

function transactionModeList_init ()
{
	transactionModeList_initializeDataGrid ();
}

function transactionModeList_memberData () 
{
	this.m_nSelectedTransactionModeId = -1;
}

var m_oTransactionModeListMemberData = new transactionModeList_memberData ();

function transactionModeList_initializeDataGrid ()
{
	initHorizontalSplitter("#transactionModeList_div_horizontalSplitter", "#transactionModeList_table_transactionModeListDG");
	$('#transactionModeList_table_transactionModeListDG').datagrid
	(
		{
			fit:true,
			columns:
			[[
			  	{field:'m_strModeName',title:'Transaction Mode',sortable:true,width:150},
			  	{field:'Action',title:'Action',width:40,align:'center',
		        	formatter:function(value,row,index)
		        	{
		        		 return transactionModeList_displayImages (row, index);
		        	}
	            }
			]]
		}
	);
	
	$('#transactionModeList_table_transactionModeListDG').datagrid
	(
		{
			onSelect: function (rowIndex, rowData)
			{
				transactionModeList_selectedRowData (rowData, rowIndex);
			}
		}
	)
	
	transactionModeList_list ("", "");
}

function transactionModeList_list ()
{
	loadPage ("inventorymanagement/progressbar.html", "dialog", "transactionModeList_progressbarLoaded ()");
}

function transactionModeList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oTransactionModeData = new TransactionModeData ();
	TransactionModeDataProcessor.list(oTransactionModeData, "", "", transactionModeList_listed);
}

function transactionModeList_listed (oResponse)
{
	$('#transactionModeList_table_transactionModeListDG').datagrid('loadData',oResponse.m_arrTransactionMode);
	HideDialog ("dialog");
}

function transactionModeList_displayImages (oRow , nIndex)
{
 	assert.isObject(oRow, "oRow expected to be an Object.");
    var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Edit" src="images/edit_database_24.png" onClick="transactionModeList_edit('+ oRow.m_nModeId  +')" width="20" /> </td>'+
						'<td></td>'+
						'<td></td>'+
						'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="transactionModeList_delete('+oRow.m_nModeId+')" width="20"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
}

function transactionModeList_edit (nModeId)
{
	assert.isNumber(nModeId, "nModeId expected to be a Number.");
	assert(nModeId !== 0, "nModeId cannot be equal to zero.");
	m_oTransactionModeListMemberData.m_nSelectedTransactionModeId = nModeId;
	navigate ("editTransactionMode", "widgets/inventorymanagement/paymentsandreceipt/editTransactionMode.js");
}

function transactionModeList_delete (nModeId)
{
 	assert.isNumber(nModeId, "nModeId expected to be a Number.");
 	assert.isOk(nModeId > -1, "nModeId must be a positive value.");
	var oTransactionModeData = new TransactionModeData ();
	oTransactionModeData.m_nModeId = nModeId;
	var bConfirm = getUserConfirmation("Do you really want to Delete ?")
	if (bConfirm == true)
		TransactionModeDataProcessor.deleteData(oTransactionModeData,
				function (oResponse)
				{
					transactionModeList_deleted(oResponse)
				});
}

function transactionModeList_deleted (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Deleted Successfully", "kSuccess");
		document.getElementById ("transactionModeList_div_listDetail").innerHTML = "";
		transactionModeList_list ("", "");
	}
}

function transactionModeList_selectedRowData (oRowData, nRowIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	document.getElementById("transactionModeList_div_listDetail").innerHTML = "";
	var oTransactionModeData = new TransactionModeData ();
	oTransactionModeData.m_nModeId = oRowData.m_nModeId;
	TransactionModeDataProcessor.getXML (oTransactionModeData,	transactionModeList_gotXML);
}

function transactionModeList_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "inventorymanagement/paymentsandreceipt/transactionModeDetails.xslt", 'transactionModeList_div_listDetail');
}

function transactionModeList_addNewPopUp ()
{
	navigate("New Transaction Mode", "widgets/inventorymanagement/paymentsandreceipt/newTransactionMode.js")
}
