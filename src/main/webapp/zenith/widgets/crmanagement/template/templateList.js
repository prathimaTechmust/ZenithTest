var templateList_includeDataObjects = 
[
	'widgets/crmanagement/template/TemplateData.js',
	'widgets/crmanagement/template/TemplateCategoryData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

includeDataObjects (templateList_includeDataObjects, "templateList_loaded()");

function templateList_loaded ()
{
	loadPage ("crmanagement/template/templateList.html", "workarea", "templateList_init()");
}
	
function templateList_MemberData ()
{
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strTemplateName";
    this.m_strSortOrder = "asc";
    this.m_nTemplateId = -1;
}

var m_oTemplateList_MemberData = new templateList_MemberData ();

function templateList_init ()
{
	templateList_createDataGrid ();
}

function templateList_createDataGrid ()
{
	initHorizontalSplitter("#templateList_div_horizontalSplitter", "#templateList_table_templateListDG");
	$('#templateList_table_templateListDG').datagrid
	({
		fit:true,
	    columns:[[  
	        {field:'m_strTemplateName',title:'Template Name',sortable:true,width:200},
	        {field:'m_strCategoryName',title:'Template Category Name',sortable:true,width:175,
	        	formatter:function(value,row,index)
	        	{
	        		return row.m_oCategoryData.m_strCategoryName;
	        	}
	        },
	        {field:'m_strTemplateFileName',title:'Template File',sortable:true,width:275,align:'center'},
	        {field:'Action',title:'Action',width:75,align:'center',
	        	formatter:function(value,row,index)
	        	{
	        		 return templateList_displayImages (row, index);
	        	}
            },
	    ]],
	    onSelect: function (rowIndex, rowData)
		{
			templateList_selectedRowData (rowData, rowIndex);
		},
		onSortColumn: function (strColumn, strOrder)
		{
			m_oTemplateList_MemberData.m_strSortColumn = strColumn;
			m_oTemplateList_MemberData.m_strSortOrder = strOrder;
			templateList_list (strColumn, strOrder, m_oTemplateList_MemberData.m_nPageNumber, m_oTemplateList_MemberData.m_nPageSize);
		}
	});
	templateList_initDGPagination ();
	templateList_list (m_oTemplateList_MemberData.m_strSortColumn, m_oTemplateList_MemberData.m_strSortOrder, 1, 10);
}

function templateList_initDGPagination ()
{
	$('#templateList_table_templateListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oTemplateList_MemberData.m_nPageNumber = nPageNumber;
				templateList_list (m_oTemplateList_MemberData.m_strSortColumn, m_oTemplateList_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("templateList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oTemplateList_MemberData.m_nPageNumber = nPageNumber;
				m_oTemplateList_MemberData.m_nPageSize = nPageSize;
				templateList_list (m_oTemplateList_MemberData.m_strSortColumn, m_oTemplateList_MemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("templateList_div_listDetail").innerHTML = "";
			}
		}
	)
	
}

function templateList_filter ()
{
	templateList_list (m_oTemplateList_MemberData.m_strSortColumn, m_oTemplateList_MemberData.m_strSortOrder, 1, 10);
}

function templateList_selectedRowData (oRowData, nIndex)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	document.getElementById("templateList_div_listDetail").innerHTML = "";
	var oTemplateData = new TemplateData ();
	oTemplateData.m_nTemplateId = oRowData.m_nTemplateId;
	TemplateDataProcessor.getXML (oTemplateData,templateList_GotRowData);
}
		

function templateList_GotRowData(strXMLData)
{
	populateXMLData (strXMLData, "crmanagement/template/templateDetails.xslt", 'templateList_div_listDetail');
}

function templateList_list (strColumnName, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumnName, "strColumnName expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oTemplateList_MemberData.m_strSortColumn = strColumnName;
	m_oTemplateList_MemberData.m_strSortOrder = strOrder;
	m_oTemplateList_MemberData.m_nPageNumber = nPageNumber;
	m_oTemplateList_MemberData.m_nPageSize = nPageSize; 
	loadPage ("inventorymanagement/progressbar.html", "dialog", "templateList_progressbarLoaded ()");
}

function templateList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oTemplateData = new TemplateData();
	oTemplateData.m_strTemplateName = $("#filterTemplate_input_templatename").val();
	TemplateDataProcessor.list(oTemplateData, m_oTemplateList_MemberData.m_strSortColumn, m_oTemplateList_MemberData.m_strSortOrder, m_oTemplateList_MemberData.m_nPageNumber, m_oTemplateList_MemberData.m_nPageSize, templateList_listed);
}

function templateList_listed (oResponse)
{
	$('#templateList_table_templateListDG').datagrid ('loadData', oResponse.m_arrTemplates);
	$('#templateList_table_templateListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oTemplateList_MemberData.m_nPageNumber});
	HideDialog("dialog");
}

function templateList_displayImages (row, index)
{
	assert.isObject(row, "row expected to be an Object.");
	assert( Object.keys(row).length >0 , "row cannot be an empty .");// checks for non emptyness 
	assert.isNumber(index, "index expected to be a Number.");
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Edit" src="images/edit_database_24.png" onClick="templateList_edit('+ row.m_nTemplateId  +')" width="20" /> </td>'+
							'<td></td>'+
							'<td></td>'+
							'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="templateList_delete('+index+')" width="20"/> </td>'+
						'</tr>'+
					'</table>'
   return oImage;
}

function templateList_edit (nTemplateId)
{
	assert.isNumber(nTemplateId, "nTemplateId expected to be a Number.");
	m_oTemplateList_MemberData.m_nTemplateId = nTemplateId;
	navigate("template", "widgets/crmanagement/template/editTemplate.js");
}

function templateList_showAddPopup ()
{
	navigate ("newtax", "widgets/crmanagement/template/newTemplate.js");
}

function templateList_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	var oTemplateData = new TemplateData();
	var oListData = $("#templateList_table_templateListDG").datagrid('getData');
	var oData = oListData.rows[nIndex];
	oTemplateData.m_nTemplateId = oData.m_nTemplateId;
	oTemplateData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	var bConfirm =getUserConfirmation("Do you really want to delete?");
	if (bConfirm == true)
		TemplateDataProcessor.deleteData(oTemplateData ,templateList_deleted);
}


function templateList_deleted (oResponse)
{
	var oTemplateData = new TemplateData ();
	if(oResponse.m_bSuccess)
	{
		informUser("Template deleted successfully.", "kSuccess");
		document.getElementById("templateList_div_listDetail").innerHTML = "";
		clearGridData ("#templateList_table_templateListDG");
		TemplateDataProcessor.list(oTemplateData, "", "",templateList_listed);
		//templateList_list(oTemplateData, strSortColumn, m_oTemplateList_MemberData.m_strSortOrder, m_oTemplateList_MemberData.m_nPageNumber, m_oTemplateList_MemberData.m_nPageSize);

	}
}