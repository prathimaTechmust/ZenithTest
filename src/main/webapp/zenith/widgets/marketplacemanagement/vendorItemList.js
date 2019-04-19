var vendorItemList_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
];



 includeDataObjects (vendorItemList_includeDataObjects, "vendorItemList_loaded()");

function vendorItemList_memberData ()
{
	this.m_oSelectedVendorId = -1;
	this.m_nIndex = -1;
	this.m_strActionItemsFunction = "vendorItemList_addHyphen()";
	this.m_nPageNumber = 1;
    this.m_nPageSize =20;
    this.m_strSortColumn = "m_strCompanyName";
    this.m_strSortOrder = "asc";
    this.m_bVerify = false;
    this.m_oVendorData = "";
    this.oSubGridID = "";
}

var m_oVendorItemListMemberData = new vendorItemList_memberData ();

function vendorItemList_addHyphen ()
{
	var oHyphen = '<table class="trademust">'+
					'<tr>' +
						'<td style="border-style:none;">-</td>'+
					'</tr>'+
				  '</table>'
	return oHyphen;
}

function vendorItemList_initAdmin ()
{
	m_oVendorItemListMemberData.m_strActionItemsFunction = "vendorItemList_addActions (row, index)";
	document.getElementById ("vendorItemList_button_add").style.visibility="visible";
	vendorItemList_createDataGrid ();
}

function vendorItemList_addActions (row, index)
{
	var oImage = 	'<table align="center">'+
						'<tr>'+
							'<td> <img title="Info" src="images/messager_info.gif" width="20" align="center" id="InfoImageId" onClick="vendorItemList_getInfo ('+row.m_nClientId+')"/> </td>'+
						'</tr>'+
					'</table>'
	return oImage;
}

function vendorItemList_initUser ()
{
	vendorItemList_createDataGrid ();
}

function vendorItemList_getFormData () 
{
	var oVendorData = new VendorData ();
	oVendorData.m_strCompanyName = $("#filterVendorItemList_input_vendorName").val();
	oVendorData.m_strCity = $("#filterVendorItemList_input_city").val();
	return oVendorData;
}

function vendorItemList_createDataGrid ()
{
	initPanelWithoutSplitter ("#vendorItemList_div_dataGrid", "#vendorItemList_table_vendors");
	$('#vendorItemList_table_vendors').datagrid
	(
		{
			fit:true,
			columns:
			[[

				{field:'m_strCompanyName',title:'Vendor Name',sortable:true,width:250,
			  		styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
				},
				{field:'m_strCity',title:'City',sortable:true,width:200},
				{field:'m_strTelephone',title:'Telephone',sortable:true,width:120},
				{field:'m_strEmail',title:'Email',sortable:true,width:200},
				{field:'Status',title:'Status',sortable:true,width:70,align:'center',
					formatter:function(value,row,index)
		        	{
		        		return row.m_bVerified ? '<b style="color:green">Verified</b>' : '<b style="color:red">Blocked</b>';
		        	}
				},
				{field:'Action',title:'Action',sortable:false,width:60,align:'center',
		        	formatter:function(value,row,index)
		        	{
		        		return vendorItemList_addActions (row, index);
		        	}
		         },
			]],
			onSortColumn: function (strColumn, strOrder, oVendorData)
			{
				m_oVendorItemListMemberData.m_strSortColumn = strColumn;
				m_oVendorItemListMemberData.m_strSortOrder = strOrder;
				vendorItemList_list (strColumn, strOrder, m_oVendorItemListMemberData.m_nPageNumber, m_oVendorItemListMemberData.m_nPageSize);
			}
		}
	);
	vendorItemList_list (m_oVendorItemListMemberData.m_strSortColumn, m_oVendorItemListMemberData.m_strSortOrder, 1, 20);
	vendorItemList_itemSubGrid ();
	vendorItemList_initDGPagination ();
}

function vendorItemList_itemSubGrid ()
{
	$('#vendorItemList_table_vendors').datagrid({view: detailview,detailFormatter:function(index,row)
		{
            return '<div style="padding:2px"><table class="vendorItemList_table_itemListSubDG"></table></div>';
        },
        onExpandRow: function(index,row)
        {
        	m_oVendorItemListMemberData.m_oVendorData = row;
            var vendorItemList_table_itemListSubDG = $(this).datagrid('getRowDetail',index).find('table.vendorItemList_table_itemListSubDG');
            vendorItemList_table_itemListSubDG.datagrid({fitColumns:true,singleSelect:true,rownumbers:true,height:'auto',showFooter:true,
                columns:[[
      				  	{field:'m_strArticleNumber',title:'Article Number',sortable:true,width:100},
    					{field:'m_strItemName',title:'Item Name',sortable:true,width:200,
    				  		styler: function(value,row,index)
    				  		{
    				  			return {class:'DGcolumn'};
    				  		}
    					},
    					{field:'m_strBrand',title:'Brand',sortable:true,width:100},
    					{field:'m_strDetail',title:'Detail',sortable:true,width:200},
    					{field:'Action',title:'Action',sortable:true,width:60,align:'center',
    						formatter:function(value,row,index)
    			        	{
    			        		return row.m_bPublishOnline ? vendorItemList_addBlockButtons (row.m_oItemData,index) : vendorItemList_addVerifyButtons (row.m_oItemData,index);
    			        	}
    					}
    				]],
                onResize:function()
                {
                    $('#vendorItemList_table_vendors').datagrid('fixDetailRowHeight',index);
                }
            });
            vendorItemList_itemList(vendorItemList_table_itemListSubDG, index, row);
            m_oVendorItemListMemberData.oSubGridID = vendorItemList_table_itemListSubDG;
        }
    });
}

function vendorItemList_addBlockButtons (oRowData, nIndex)
{
	return '<table class="trademust">'+
						'<tr>'+
							'<td> <button class="filterButton" type="button" onclick="vendorItemList_LoadDetails ('+oRowData.m_nItemId+', '+nIndex+')">Block</button></td>'+
						'</tr>'+
					'</table>'
}

function vendorItemList_addVerifyButtons (oRowData, nIndex)
{
	return  '<table class="trademust">'+
					'<tr>'+
						'<td> <button class="filterButton" type="button" onclick="vendorItemList_LoadDetails ('+oRowData.m_nItemId+','+nIndex+')">Verify</button></td>'+
					'</tr>'+
				'</table>'
}

function vendorItemList_itemList (vendorItemList_table_itemListSubDG, index, row)
{
	var oItemData = new ItemData ();
	oItemData.m_oCreatedBy.m_nUserId = row.m_nClientId;
	VendorItemDataProcessor.list(oItemData, "", "",0,0,vendorItemList_itemList_listed);
}

function vendorItemList_itemList_listed(oResponse)
{

	var arrItemData = new Array ();
	var arrItems = oResponse.m_arrVendorItems;
	for(var nIndex = 0; nIndex < arrItems.length; nIndex++)
	{
		arrItems[nIndex].m_strArticleNumber = arrItems[nIndex].m_oItemData.m_strArticleNumber;
		arrItems[nIndex].m_strItemName = arrItems[nIndex].m_oItemData.m_strItemName;
		arrItems[nIndex].m_strBrand = arrItems[nIndex].m_oItemData.m_strBrand;
		arrItems[nIndex].m_strDetail = arrItems[nIndex].m_oItemData.m_strDetail;
		arrItems[nIndex].m_bPublishOnline = arrItems[nIndex].m_oItemData.m_bPublishOnline;
		arrItemData.push(arrItems[nIndex]);
	}
	vendorItemList_table_itemListSubDG.datagrid('loadData',arrItemData);
	$('#vendorItemList_table_vendors').datagrid('fixDetailRowHeight',index);

	
}

function vendorItemList_initDGPagination ()
{
	$('#vendorItemList_table_vendors').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oVendorItemListMemberData.m_nPageNumber = nPageNumber;
				vendorItemList_list (m_oVendorItemListMemberData.m_strSortColumn, m_oVendorItemListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("vendorItemList_div_listDetail").innerHTML = "";
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oVendorItemListMemberData.m_nPageNumber = nPageNumber;
				m_oVendorItemListMemberData.m_nPageSize = nPageSize;
				vendorItemList_list (m_oVendorItemListMemberData.m_strSortColumn, m_oVendorItemListMemberData.m_strSortOrder, nPageNumber, nPageSize);
				document.getElementById("vendorItemList_div_listDetail").innerHTML = "";
			}
		}
	)
}

function vendorItemList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	m_oVendorItemListMemberData.m_strSortColumn = strColumn;
	m_oVendorItemListMemberData.m_strSortOrder = strOrder;
	m_oVendorItemListMemberData.m_nPageNumber = nPageNumber;
	m_oVendorItemListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "dialog", "vendorItemList_progressbarLoaded ()");
}

function vendorItemList_progressbarLoaded ()
{
	createPopup('dialog', '', '', true);
	var oVendorData = vendorItemList_getFormData ();
	VendorDataProcessor.listVendor(oVendorData, m_oVendorItemListMemberData.m_strSortColumn, m_oVendorItemListMemberData.m_strSortOrder, m_oVendorItemListMemberData.m_nPageNumber, m_oVendorItemListMemberData.m_nPageSize, vendorItemList_listed);
}

function vendorItemList_showAddVendorPopup ()
{
	navigate ("vendorInfo", "widgets/vendormanagement/newVendorInfo.js");
}

function vendorItemList_listed (oVendorResponse)
{
	clearGridData ("#vendorItemList_table_vendors");
	$('#vendorItemList_table_vendors').datagrid('loadData',oVendorResponse.m_arrVendorData);
	$('#vendorItemList_table_vendors').datagrid('getPager').pagination ({total:oVendorResponse.m_nRowCount, pageNumber:m_oVendorItemListMemberData.m_nPageNumber});
	HideDialog ("dialog");
}

function vendorItemList_filter ()
{
	vendorItemList_list (m_oVendorItemListMemberData.m_strSortColumn, m_oVendorItemListMemberData.m_strSortOrder, 1, 20);
}

function vendorInfo_handleAfterSave ()
{
	vendorItemList_list (m_oVendorItemListMemberData.m_strSortColumn, m_oVendorItemListMemberData.m_strSortOrder, 1, 20);
}

function vendorItemList_getInfo (nVendorId)
{
	m_oVendorItemListMemberData.m_oSelectedVendorId = nVendorId;
	navigate ("vendorTransactionForList", "widgets/vendormanagement/vendorTransactionForList.js");
}

function vendorItemList_LoadDetails (nItemId,nIndex)
{
	m_oVendorItemListMemberData.m_nItemId = nItemId;
	m_oVendorItemListMemberData.m_nIndex = nIndex;
	loadPage ("marketplacemanagement/vendorItemDetails.html", "dialog", "vendorItemList_initPopup ()");
}

function vendorItemList_initPopup ()
{
	createPopup('dialog', '', '', true);
	vendorItemList_getDetailsToLoad ();
}

function vendorItemList_getDetailsToLoad ()
{
	var oItemData = new ItemData ();
	oItemData.m_nItemId = m_oVendorItemListMemberData.m_nItemId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	VendorItemDataProcessor.getXML (oItemData,	{
		async:false, 
		callback: function (strXMLData)
		{
			populateXMLData (strXMLData, "marketplacemanagement/vendorItemDetails.xslt", 'vendorItemDetails_div_details');
			vendorItemList_setImagePreview(oItemData);
		}
	});
}

function vendorItemList_setImagePreview (oItemData)
{
	VendorItemDataProcessor.get (oItemData, vendorItemList_gotData);
}

function vendorItemList_gotData (oResponse)
{
	var oItemData = oResponse.m_arrItems[0];
	var buffImage = oItemData.m_buffImgPhoto != null ? oItemData.m_buffImgPhoto : "images/noImage.jpg"; 
	$("#ItemListDetails_img_itemImage"+oItemData.m_nItemId).attr('src', buffImage);
}

function vendorItemList_blockItem (nItemId)
{
	m_oVendorItemListMemberData.m_bVerify = false;
	m_oVendorItemListMemberData.m_nItemId = nItemId;
	loadPage ("include/process.html", "ProcessDialog", "vendorItemList_initProgressbar ()");
}

function vendorItemList_verifyItem (nItemId)
{
	if(!m_oVendorItemListMemberData.m_oVendorData.m_bVerified)
		informUser ("This item will be visible only when the vendor is unblocked!", "kWarning");
	m_oVendorItemListMemberData.m_bVerify = true;
	m_oVendorItemListMemberData.m_nItemId = nItemId;
	loadPage ("include/process.html", "ProcessDialog", "vendorItemList_initProgressbar ()");
}

function vendorItemList_initProgressbar ()
{
	createPopup('ProcessDialog', '', '', true);
	var oItemData = new ItemData ();
	oItemData.m_nItemId = m_oVendorItemListMemberData.m_nItemId;
	ItemDataProcessor.updatePublishOnline (oItemData, m_oVendorItemListMemberData.m_bVerify, vendorItemList_itemVerified);
}

function vendorItemList_itemVerified (oResponse)
{
	if(oResponse.m_bSuccess)
	{
    	vendorItemList_updateRow (oResponse.m_arrItems[0]);
		HideDialog("dialog");
	}
	HideDialog("ProcessDialog");
}

function vendorItemList_updateRow (oRowData)
{
		m_oVendorItemListMemberData.oSubGridID.datagrid('updateRow',{
		index: m_oVendorItemListMemberData.m_nIndex,
		row: oRowData
	});
}

function vendorItemList_cancel ()
{
	HideDialog("dialog");
}