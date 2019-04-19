var templateCategoryList_includeDataObjects = 
[
	'widgets/crmanagement/template/TemplateData.js',
	'widgets/crmanagement/template/TemplateCategoryData.js'
];

includeDataObjects (templateCategoryList_includeDataObjects, "templateCategoryList_loaded()");

function templateCategoryList_loaded ()
{
	loadPage ("crmanagement/template/templateCategoryList.html", "workarea", "templateCategoryList_init()");
}
	
function templateCategoryList_MemberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strCategoryName";
    this.m_strSortOrder = "asc";
    this.m_nCategoryId = -1;
}

var m_oTemplateCategoryList_MemberData = new templateCategoryList_MemberData ();

function templateCategoryList_init ()
{
	templateCategoryList_createDataGrid ();
}

function templateCategoryList_createDataGrid ()
{
	initHorizontalSplitter("#templateCategoryList_div_horizontalSplitter", "#templateCategoryList_table_categoryListDG");
	$('#templateCategoryList_table_categoryListDG').datagrid
	({
		fit:true,
	    columns:[[  
	        {field:'m_strCategoryName',title:'Template Category Name',sortable:true,width:375},
	        {field:'Action',title:'Action',width:75,align:'center',
	        	formatter:function(value,row,index)
	        	{
	        		 return templateCategoryList_displayImages (row, index);
	        	}
            }
	    ]],
	    onSelect: function (rowIndex, rowData)
		{
			templateCategoryList_selectedRowData (rowData, rowIndex);
		},
		onSortColumn: function (strColumn, strOrder)
		{
			m_oTemplateCategoryList_MemberData.m_strSortColumn = strColumn;
			m_oTemplateCategoryList_MemberData.m_strSortOrder = strOrder;
			templateCategoryList_list (strColumn, strOrder, m_oTemplateCategoryList_MemberData.m_nPageNumber, m_oTemplateCategoryList_MemberData.m_nPageSize);
		}
	});
	templateCategoryList_initDGPagination ();
	templateCategoryList_list (m_oTemplateCategoryList_MemberData.m_strSortColumn, m_oTemplateCategoryList_MemberData.m_strSortOrder, 1, 10);
}

function templateCategoryList_initDGPagination ()
{
	$('#templateCategoryList_table_categoryListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oTemplateCategoryList_MemberData.m_nPageNumber = nPageNumber;
				templateCategoryList_list (m_oTemplateCategoryList_MemberData.m_strSortColumn, m_oTemplateCategoryList_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("templateCategoryList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oTemplateCategoryList_MemberData.m_nPageNumber = nPageNumber;
				m_oTemplateCategoryList_MemberData.m_nPageSize = nPageSize;
				templateCategoryList_list (m_oTemplateCategoryList_MemberData.m_strSortColumn, m_oTemplateCategoryList_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("templateCategoryList_div_listDetail").innerHTML = "";
			}
		}
	);
}

function templateCategoryList_filter ()
{
	templateCategoryList_list (m_oTemplateCategoryList_MemberData.m_strSortColumn, m_oTemplateCategoryList_MemberData.m_strSortOrder, 1, 10);
}

function templateCategoryList_selectedRowData (oRowData, nIndex)
{
	document.getElementById("templateCategoryList_div_listDetail").innerHTML = "";
	var oTemplateCategoryData = new TemplateCategoryData();
	oTemplateCategoryData.m_nCategoryId = oRowData.m_nCategoryId;
	TemplateCategoryDataProcessor.getXML (oTemplateCategoryData, templateCategoryList_gotXMLData);
}

function templateCategoryList_gotXMLData (strXMLData)
{
	populateXMLData (strXMLData, "crmanagement/template/templateCategoryDetails.xslt", 'templateCategoryList_div_listDetail');
}

function templateCategoryList_list (strColumnName, strOrder, nPageNumber, nPageSize)
{
	m_oTemplateCategoryList_MemberData.m_strSortColumn = strColumnName;
	m_oTemplateCategoryList_MemberData.m_strSortOrder = strOrder;
	m_oTemplateCategoryList_MemberData.m_nPageNumber = nPageNumber;
	m_oTemplateCategoryList_MemberData.m_nPageSize = nPageSize; 
	loadPage ("inventorymanagement/progressbar.html", "dialog", "templateCategoryList_progressbarLoaded ()");
}

function templateCategoryList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oTemplateCategoryData = new TemplateCategoryData();
	oTemplateCategoryData.m_strCategoryName = $("#filterTemplateCategory_input_categoryname").val();
	TemplateCategoryDataProcessor.list(oTemplateCategoryData, m_oTemplateCategoryList_MemberData.m_strSortColumn, m_oTemplateCategoryList_MemberData.m_strSortOrder, m_oTemplateCategoryList_MemberData.m_nPageNumber, m_oTemplateCategoryList_MemberData.m_nPageSize, templateCategoryList_listed);
}

function templateCategoryList_listed (oResponse)
{
	$('#templateCategoryList_table_categoryListDG').datagrid ('loadData', oResponse.m_arrTemplateCategories);
	$('#templateCategoryList_table_categoryListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oTemplateCategoryList_MemberData.m_nPageNumber});
	HideDialog("dialog");
}

function templateCategoryList_displayImages (row, index)
{
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" onClick="templateCategoryList_edit('+ row.m_nCategoryId  +')" width="20" /> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="templateCategoryList_delete('+index+')" width="20"/> </td>'+
						'</tr>'+
					'</table>'
   return oImage;
}

function templateCategoryList_edit (nCategoryId)
{
	m_oTemplateCategoryList_MemberData.m_nCategoryId = nCategoryId;
	navigate("editTemplateCategory", "widgets/crmanagement/template/templateCategoryEdit.js");
}

function templateCategoryList_showAddPopup ()
{
	navigate ("TemplateCategoryNew", "widgets/crmanagement/template/templateCategoryNew.js");
}

function templateCategoryList_delete (nIndex)
{
	var oTemplateCategoryData = new TemplateCategoryData();
	var oListData = $("#templateCategoryList_table_categoryListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oTemplateCategoryData.m_nCategoryId = oData.m_nCategoryId;
	var bConfirm = getUserConfirmation("Do you really want to delete?")
	if (bConfirm == true)
		TemplateCategoryDataProcessor.deleteData(oTemplateCategoryData,templateCategoryList_deleted);
}

function templateCategoryList_deleted (oResponse)
{
	var oTemplateCategoryData = new TemplateCategoryData ();
	if(oResponse.m_bSuccess)
	{
		informUser("Template Category deleted successfully.", "kSuccess");
		document.getElementById("templateCategoryList_div_listDetail").innerHTML = "";
		templateCategoryList_list (m_oTemplateCategoryList_MemberData.m_strSortColumn, m_oTemplateCategoryList_MemberData.m_strSortOrder, 1, 10);
	}
}