var exportImportProviders_includeDataObjects = 
[
	'widgets/inventorymanagement/exportimport/ExportImportProviderData.js',
	'widgets/inventorymanagement/exportimport/DataExchangeClassesData.js'
];

includeDataObjects (exportImportProviders_includeDataObjects, "exportImportProviders_loaded()");

function exportImportProviders_MemberData ()
{
	this.m_nId = -1;
}

var m_oExportImportProvidersMemberData = new exportImportProviders_MemberData ();

function exportImportProviders_init()
{
	createPopup ("dialog", "#exportImportProviders_button_create", "#exportImportProviders_button_cancel", true);
	initFormValidateBoxes ("exportImportProviders_form_id");
}

function exportImportProviders_new ()
{   
	exportImportProviders_init();
	exportImportProviders_createDG()
}

function exportImportProviders_createDG ()
{
	$('#exportImportProviders_table_classesDG').datagrid
	(
		{
			columns:
				[[
				  	{field:'m_strRegisteredClassName',title:'Class Name',sortable:true,width:150},
				  	{field:'Action',title:'Action',width:200,align:'center',
			        	formatter:function(value,row,index)
			        	{
			        		 return exportImportProviders_displayImages (row, index);
			        	}
		            },
				  	]]
		});
}

function exportImportProviders_displayImages (row, index)
{
	var oImage = '<table align="center">'+
					'<tr>'+
						'<td> <img title="Delete" src="images/delete.png"  align="center" onclick="exportImportProviders_deleteClasses('+index+')" width="20"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
}

function exportImportProviders_deleteClasses (nIndex)
{
	$('#exportImportProviders_table_classesDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#exportImportProviders_table_classesDG');
}

function exportImportProviders_edit()
{
	exportImportProviders_new();
	document.getElementById("exportImportProviders_button_create").setAttribute('update', true);
	document.getElementById("exportImportProviders_button_create").innerHTML = "Update";
	var oExportImportProviderData = new ExportImportProviderData ();
	oExportImportProviderData.m_nId = m_oExportImportProvidersMemberData.m_nId;
	ExportImportProviderDataProcessor.get(oExportImportProviderData,exportImportProviders_gotData);
}

function exportImportProviders_getFormData()
{
	var oExportImportProviderData = new ExportImportProviderData();
	oExportImportProviderData.m_strProviderName = dwr.util.getValue("exportImportProviders_input_providerName");
	oExportImportProviderData.m_strDescription = dwr.util.getValue("exportImportProviders_input_description");
	oExportImportProviderData.m_arrDataExchangeClasses = exportImportProviders_getClasses ();
	return oExportImportProviderData;
}

function exportImportProviders_getClasses ()
{
	var oDataExchangeClassesArray = new Array ();
	var arrDataExchangeClasses = $('#exportImportProviders_table_classesDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrDataExchangeClasses.length; nIndex++)
	{
			var oDataExchangeClassesData = new DataExchangeClassesData ();
			oDataExchangeClassesData.m_strRegisteredClassName = arrDataExchangeClasses [nIndex].m_strRegisteredClassName;
			oDataExchangeClassesArray.push(oDataExchangeClassesData);
	}
	return oDataExchangeClassesArray;
}

function exportImportProviders_validate ()
{
	return validateForm("exportImportProviders_form_Id");
}

function exportImportProviders_submit()
{
	if (exportImportProviders_validate())
	{
		var oExportImportProviderData = exportImportProviders_getFormData ();
		if((document.getElementById("exportImportProviders_button_create").getAttribute('update') == "false"))
			ExportImportProviderDataProcessor.create(oExportImportProviderData, exportImportProviders_created);
		else
		{
			oExportImportProviderData.m_nId = m_oExportImportProvidersMemberData.m_nId;
			ExportImportProviderDataProcessor.update(oExportImportProviderData, exportImportProviders_updated);
		}
	}
}

function exportImportProviders_created(oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Export import provider created successfully", "kSuccess");
		HideDialog("dialog");
		navigate("ExportImportProvidesrList", "widgets/inventorymanagement/exportimport/exportImportProvidersList.js");
	}
	else if(oResponse.m_nErrorID == 1)
	{
		informUser ("Export import provider creation failed", "kError");
	}
}

function exportImportProviders_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Export import provider updated successfully", "kSuccess");
		HideDialog("dialog");
		navigate("ExportImportProvidesrList", "widgets/inventorymanagement/exportimport/exportImportProvidersList.js");
	}
	else 
		informUser ("Export import provider updation failed", "kError");
}

function exportImportProviders_cancel()
{
	 HideDialog("dialog");
}

function exportImportProviders_gotData(oResponse)
{
	var oExportImportProviderData = oResponse.m_arrExportImportProvider[0];
	dwr.util.setValue("exportImportProviders_input_providerName", oExportImportProviderData.m_strProviderName);
	dwr.util.setValue("exportImportProviders_input_description", oExportImportProviderData.m_strDescription);
	$('#exportImportProviders_table_classesDG').datagrid('loadData',oExportImportProviderData.m_oDataExchangeClasses);
	initFormValidateBoxes ("exportImportProviders_form_id");
}

function exportImportProviders_add ()
{
	navigate ('addClasses','widgets/inventorymanagement/exportimport/dataExchangeClasses.js');
}

