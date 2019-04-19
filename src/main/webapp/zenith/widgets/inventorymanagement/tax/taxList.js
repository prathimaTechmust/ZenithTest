var taxList_includeDataObjects = 
	[
		'widgets/inventorymanagement/tax/TaxData.js',
	];


includeDataObjects (taxList_includeDataObjects, "taxList_loaded()");

function taxList_loaded ()
{
	loadPage ("inventorymanagement/tax/taxList.html", "workarea", "taxList_init()");
}
	
function taxList_MemberData ()
{
	this.m_nTaxId = -1;
	this.m_oTaxData = null;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strTaxName";
    this.m_strSortOrder = "asc";
}

var m_oTaxList_MemberData = new taxList_MemberData ();

function taxList_init ()
{
	taxList_createDataGrid ();
}

function taxList_getFormData ()
{
	var oTaxData = new TaxData();
	oTaxData.m_strTaxName = $("#filterTax_input_taxname").val();
	oTaxData.m_nPercentage = $("#filterTax_input_percentage").val();
	return oTaxData;
}

function taxList_createDataGrid ()
{
	initHorizontalSplitter("#taxList_div_horizontalSplitter", "#taxList_table_taxListDG");
	$('#taxList_table_taxListDG').datagrid({
		fit:true,
	    columns:[[  
	        {field:'m_strTaxName',title:'Tax Name',sortable:true,width:630},
	        {field:'m_nPercentage',title:'Percentage',sortable:true,width:130,align:'center'},
	        {field:'Action',title:'Action',width:200,align:'center',
	        	formatter:function(value,row,index)
	        	{
	        		 return taxList_displayImages (row, index);
	        	}
            },
	    ]],
	    onSelect: function (rowIndex, rowData)
		{
			taxList_selectedRowData (rowData, rowIndex);
		},
		onSortColumn: function (strColumn, strOrder)
		{
			m_oTaxList_MemberData.m_strSortColumn = strColumn;
			m_oTaxList_MemberData.m_strSortOrder = strOrder;
			taxList_list (strColumn, strOrder, m_oTaxList_MemberData.m_nPageNumber, m_oTaxList_MemberData.m_nPageSize);
		}
	});
	taxList_initDGPagination ();
	taxList_list (m_oTaxList_MemberData.m_strSortColumn, m_oTaxList_MemberData.m_strSortOrder, 1, 10);
}

function taxList_initDGPagination ()
{
	$('#taxList_table_taxListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oTaxList_MemberData.m_nPageNumber = nPageNumber;
				taxList_list (m_oTaxList_MemberData.m_strSortColumn, m_oTaxList_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("taxList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oTaxList_MemberData.m_nPageNumber = nPageNumber;
				m_oTaxList_MemberData.m_nPageSize = nPageSize;
				taxList_list (m_oTaxList_MemberData.m_strSortColumn, m_oTaxList_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("taxList_div_listDetail").innerHTML = "";
			}
		}
	)
	
}

function taxList_filter ()
{
	taxList_list (m_oTaxList_MemberData.m_strSortColumn, m_oTaxList_MemberData.m_strSortOrder, 1, 10);
}

function taxList_selectedRowData (oRowData, nIndex)
{
    assert.isObject(oRowData, "oRowData expected to be an Object.");
    assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	m_oTaxList_MemberData.m_oTaxData = oRowData;
	m_oTaxList_MemberData.m_nIndex = nIndex;
	document.getElementById("taxList_div_listDetail").innerHTML = "";
	var oTaxData = new TaxData ();
	oTaxData.m_nTaxId = oRowData.m_nTaxId;
	TaxDataProcessor.getXML (oTaxData,taxList_gotXML);
}

function taxList_gotXML (strXMLData)
{
	populateXMLData (strXMLData, "inventorymanagement/tax/taxDetails.xslt", 'taxList_div_listDetail');
}
function taxList_list (strColumnName, strOrder, nPageNumber, nPageSize)
{
    assert.isString(strColumnName, "strColumnName expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	var oTaxData = taxList_getFormData ();
	taxList_cancel ();
	TaxDataProcessor.list(oTaxData, strColumnName, strOrder, nPageNumber, nPageSize, taxList_listed);
}

function taxList_displayImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" onClick="taxList_edit('+ row.m_nTaxId  +')" width="20" /> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="taxList_delete('+index+')" width="20"/> </td>'+
						'</tr>'+
					'</table>'
   return oImage;
}

function taxList_edit (nTaxId)
{
	assert.isNumber(nTaxId, "nTaxId expected to be a Number.");
	assert(nTaxId !== 0, "nTaxId cannot be equal to zero.");
	m_oTaxList_MemberData.m_nTaxId = nTaxId;
	navigate("tax", "widgets/inventorymanagement/tax/editTax.js");
}

function taxList_listDetail_edit ()
{
	m_oTaxList_MemberData.m_nTaxId = m_oTaxList_MemberData.m_oTaxData.m_nTaxId;
	navigate("tax", "widgets/inventorymanagement/tax/editTax.js");
}

function taxList_showFilterPopup () 
{
	loadPage ("inventorymanagement/tax/filterTax.html", "dialog", "filterTax_init ()");
}

function taxList_showAddPopup ()
{
	navigate ("newtax", "widgets/inventorymanagement/tax/newTax.js");
}

function filterTax_init ()
{
	createPopup('dialog', '#filterTax_button_cancel', '#filterTax_button_create', true);
}

function taxList_cancel ()
{
	HideDialog("dialog");
}

function taxList_listDetail_delete ()
{
	taxList_delete(m_oTaxList_MemberData.m_nIndex);
}

function taxList_delete (nIndex)
{
 	assert.isNumber(nIndex, "nIndex expected to be a Number.");
 	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oTaxData = new TaxData();
	var oListData = $("#taxList_table_taxListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oTaxData.m_nTaxId = oData.m_nTaxId;
	var bConfirm = getUserConfirmation("usermessage_taxList_doyoureallywanttodelete")
	if (bConfirm == true)
		TaxDataProcessor.deleteData(oTaxData,taxList_deleted);
}

function taxList_listed (oResponse)
{
	clearGridData ("#taxList_table_taxListDG");
	$('#taxList_table_taxListDG').datagrid ('loadData', oResponse.m_arrTax);
	$('#taxList_table_taxListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oTaxList_MemberData.m_nPageNumber});
}

function taxList_deleted (oResponse)
{
	var oTaxData = new TaxData ();
	if(oResponse.m_bSuccess)
	{
		informUser("usermessage_tax_taxdeletedsuccessfully", "kSuccess");
		document.getElementById("taxList_div_listDetail").innerHTML = "";
		clearGridData ("#taxList_table_taxListDG");
		taxList_list (m_oTaxList_MemberData.m_strSortColumn, m_oTaxList_MemberData.m_strSortOrder, 1, 10);
	}
}