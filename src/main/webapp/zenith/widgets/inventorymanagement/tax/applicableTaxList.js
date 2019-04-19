var applicableTaxList_includeDataObjects = 
[
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
];



includeDataObjects (applicableTaxList_includeDataObjects, "applicableTaxList_loaded()");

function applicableTaxList_loaded ()
{
	loadPage ("inventorymanagement/tax/applicableTaxList.html", "workarea", "applicableTaxList_init ()");
}

function applicableTaxList_memberData ()
{
	this.m_nId = -1;
	this.m_nSelectedApplicableTaxId = -1;
	this.m_oApplicableTaxData = null;
	this.m_nIndex = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strApplicableTaxName";
    this.m_strSortOrder = "desc";
}

var m_oApplicableTaxListMemberData = new applicableTaxList_memberData ();

function applicableTaxList_init ()
{
	applicableTaxList_createDataGrid ();
}

function applicableTaxList_createDataGrid ()
{
	initHorizontalSplitter("#applicableTaxList_div_horizontalSplitter", "#applicableTaxList_table_applicableTaxListDG");
	$('#applicableTaxList_table_applicableTaxListDG').datagrid
	(
		{
			fit:true,
			columns:
				[[
				  	{field:'m_strApplicableTaxName',title:'Applicable Tax Name',sortable:true,width:775},
				  	{field:'Action',title:'Action',sortable:false,width:200,
						formatter:function(value,row,index)
			        	{
			        		return applicableTaxList_displayImages (row, index);
			        	}
					},
				]],
				onSelect: function (rowIndex, rowData)
				{
					applicableTaxList_selectedRowData (rowData, rowIndex);
				},
				onSortColumn: function (strColumn, strOrder)
				{
					m_oApplicableTaxListMemberData.m_strSortColumn = strColumn;
					m_oApplicableTaxListMemberData.m_strSortOrder = strOrder;
					applicableTaxList_list (strColumn, strOrder, m_oApplicableTaxListMemberData.m_nPageNumber, m_oApplicableTaxListMemberData.m_nPageSize);
				}
		}
	);
	applicableTaxList_initDGPagination ();
	applicableTaxList_list (m_oApplicableTaxListMemberData.m_strSortColumn, m_oApplicableTaxListMemberData.m_strSortOrder, 1, 10);
}

function applicableTaxList_initDGPagination ()
{
	$('#applicableTaxList_table_applicableTaxListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
			m_oApplicableTaxListMemberData.m_nPageNumber = nPageNumber;
				applicableTaxList_list (m_oApplicableTaxListMemberData.m_strSortColumn, m_oApplicableTaxListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("applicableTaxList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oApplicableTaxListMemberData.m_nPageNumber = nPageNumber;
				m_oApplicableTaxListMemberData.m_nPageSize = nPageSize;
				applicableTaxList_list (m_oApplicableTaxListMemberData.m_strSortColumn, m_oApplicableTaxListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("applicableTaxList_div_listDetail").innerHTML = "";
			}
				
		}
	)		
}

function applicableTaxList_filter ()
{
	applicableTaxList_list (m_oApplicableTaxListMemberData.m_strSortColumn, m_oApplicableTaxListMemberData.m_strSortOrder, 1, 10);
}

function applicableTaxList_displayImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" width="20" align="center" id="editImageId" onClick="applicableTaxList_edit ('+ row.m_nId +')"/> </td>'+
							'<td> <img title="Delete" src="images/delete.png" width="20" align="center" id="deleteImageId" onClick="applicableTaxList_delete ('+index+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function applicableTaxList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	var oApplicableTaxData = applicableTaxList_getFormData ();
	filterApplicableTax_cancel ();
	ApplicableTaxDataProcessor.list(oApplicableTaxData, strColumn, strOrder, nPageNumber, nPageSize, applicableTaxList_listed);
}

function applicableTaxList_listed (oResponse)
{
	clearGridData ("#applicableTaxList_table_applicableTaxListDG");
	$('#applicableTaxList_table_applicableTaxListDG').datagrid('loadData',oResponse.m_arrApplicableTax);
	$('#applicableTaxList_table_applicableTaxListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oApplicableTaxListMemberData.m_nPageNumber});
}

function applicableTaxList_getFormData ()
{
	var oApplicableTaxData = new ApplicableTaxData ();
	oApplicableTaxData.m_strApplicableTaxName = $("#filterApplicableTax_input_applicabletaxname").val();
	oApplicableTaxData.m_nId = -1;
	if(oApplicableTaxData.m_strApplicableTaxName.trim() != "")
		m_oApplicableTaxListMemberData.m_bIsForfilter = true;
	return oApplicableTaxData;
}

function applicableTaxList_selectedRowData (oRowData, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	m_oApplicableTaxListMemberData.m_oApplicableTaxData = oRowData;
	m_oApplicableTaxListMemberData.m_nIndex = nIndex;
	document.getElementById("applicableTaxList_div_listDetail").innerHTML = "";
	var oApplicableTaxData = new ApplicableTaxData ();
	oApplicableTaxData.m_nId = oRowData.m_nId;
	ApplicableTaxDataProcessor.getXML (oApplicableTaxData, function (strXMLData)
		{
			populateXMLData (strXMLData, "inventorymanagement/tax/applicableTaxDetails.xslt", 'applicableTaxList_div_listDetail');
			applicableTaxList_initializeDetailsDG ();
			ApplicableTaxDataProcessor.get (oApplicableTaxData, applicableTaxList_gotTaxListData)
		});
}

function applicableTaxList_initializeDetailsDG ()
{
	$('#applicableTaxDetails_table_applicableTaxDetailsDG').datagrid ({
	    columns:[[  
	        {field:'m_strTaxName',title:'Tax Name',sortable:true,width:150},
	        {field:'m_nPercentage',title:'Percentage',sortable:true,width:150}
	    ]]
	});
}

function applicableTaxList_gotTaxListData (oResponse)
{
	try
	{
		var arrApplicableTax = oResponse.m_arrApplicableTax;
		var arrTax = arrApplicableTax[0].m_oTaxes;
		for (var nIndex = 0; nIndex < arrTax.length; nIndex++)
		{
			$('#applicableTaxDetails_table_applicableTaxDetailsDG').datagrid('appendRow',arrTax[nIndex]);
		}
	}
	catch(oException)
	{
		
	}
}

function applicableTaxList_edit (nId)
{
	assert.isNumber(nId, "nId expected to be a Number.");
	assert(nId !== 0, "nId cannot be equal to zero.");
	m_oApplicableTaxListMemberData.m_nId = nId;
	m_oApplicableTaxListMemberData.m_nSelectedApplicableTaxId = nId;
	navigate ("applicabletaxlist", "widgets/inventorymanagement/tax/editApplicableTax.js");
}

function applicableTaxList_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oApplicableTaxData = new ApplicableTaxData ();
	var oListData = $("#applicableTaxList_table_applicableTaxListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oApplicableTaxData.m_nId = oData.m_nId;
	var bConfirm = getUserConfirmation ("usermessage_applicabletaxlist_doyoureallywanttodeletethisapplicabletax");
	if (bConfirm == true)
		ApplicableTaxDataProcessor.deleteData(oApplicableTaxData, applicableTaxList_deleted);
}

function applicableTaxList_deleted (oApplicableTaxResponse)
{
	if (oApplicableTaxResponse.m_bSuccess)
		informUser ("usermessage_applicabletaxlist_applicabletaxdeletedsuccessfully", "kSuccess");
	clearGridData ("#applicableTaxList_table_applicableTaxListDG");
	var oApplicableTaxData = new ApplicableTaxData ();
	ApplicableTaxDataProcessor.list (oApplicableTaxData, "", "", 1, 10, applicableTaxList_listed);
}

function filterApplicableTax_cancel ()
{
	HideDialog("dialog");
}

function applicableTaxList_showFilterPopup ()
{
	loadPage ("inventorymanagement/tax/filetrApplicableTax.html", "dialog", "filterApplicableTax_init ()");
}

function applicableTaxList_showAddPopup ()
{
	navigate ("newapplicabletax", "widgets/inventorymanagement/tax/newApplicableTax.js");
}

function filterApplicableTax_init ()
{
	initFormValidateBoxes ("filterApplicableTax_form_id");
	createPopup('dialog', '#filterApplicableTax_button_cancel', '#filterApplicableTax_button_create', true);
}

function applicableTaxList_listDetail_edit ()
{
	m_oApplicableTaxListMemberData.m_nSelectedApplicableTaxId  = m_oApplicableTaxListMemberData.m_oApplicableTaxData.m_nId;
	navigate ("applicabletaxlist", "widgets/inventorymanagement/tax/editApplicableTax.js");
}

function applicableTaxList_listDetail_delete ()
{
	applicableTaxList_delete (m_oApplicableTaxListMemberData.m_nIndex);
}
