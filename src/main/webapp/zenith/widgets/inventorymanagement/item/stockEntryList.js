var stockEntryList_includeDataObjects = 
[
	'widgets/inventorymanagement/item/StockEntriesData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];


includeDataObjects (stockEntryList_includeDataObjects, "stockEntryList_loaded()");

function stockEntryList_loaded ()
{
	loadPage ("inventorymanagement/item/stockEntryList.html", "dialog", "stockEntryList_init ()");
}

function stockEntryList_memberData ()
{
	this.m_nSelectedItemId = -1;
	this.m_nIndex = -1;
	this.m_nEditIndexDG = undefined;
	this.m_bIsFilterSet = false;
	this.m_arrArticleNumber = new Array ();
	this.m_nPageNumber = 1;
    this.m_nPageSize =15;
    this.m_strSortColumn = "m_dCreatedOn";
    this.m_strSortOrder = "desc";
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			stockEntryList_list ("", "", 1, 15);
			$('#stockEntryList_input_articleNumber').combobox('textbox').unbind('keydown', m_oStockEntryListMemberData.m_oKeyDownHandler);
		}
    };
}

var m_oStockEntryListMemberData = new stockEntryList_memberData ();

function stockEntryList_init ()
{
	createPopup("dialog", "#stockEntryList_button_cancel", "#stockEntryList_button_save", true);
	stockEntryList_initArticleNumberCombobox ();
	stockEntryList_list (m_oStockEntryListMemberData.m_strSortColumn, m_oStockEntryListMemberData.m_strSortOrder, 1, 15);
	stockEntryList_createDataGrid ();
}

function stockEntryList_initArticleNumberCombobox ()
{
	$('#stockEntryList_input_articleNumber').combobox
	({
		valueField:'m_strArticleNumber',
	    textField:'m_strArticleNumber',
	    selectOnNavigation: false,
	    loader: getFilteredItemData,
	    mode: 'remote',
    	onSelect: function()
    	{
    		$('#stockEntryList_input_articleNumber').combobox('textbox').focus();
    		$('#stockEntryList_input_articleNumber').combobox('textbox').bind('keydown', m_oStockEntryListMemberData.m_oKeyDownHandler);
    	}
	});
	var articleNoTextBox = $('#stockEntryList_input_articleNumber').combobox('textbox');
	articleNoTextBox[0].placeholder = "Article Number";
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
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		ItemDataProcessor.getArticleSuggesstions(oItemData, "", "", function(oItemDataResponse)
				{
					var arrItemData = new Array();
					for(var nIndex=0; nIndex< oItemDataResponse.m_arrItems.length; nIndex++)
				    {
						arrItemData.push(oItemDataResponse.m_arrItems[nIndex]);
				    }
					success(arrItemData);
				});
	}
	else
		success(new Array ());
}

function stockEntryList_createDataGrid ()
{
	$('#stockEntryList_table_stockEntryListDG').datagrid
	(
		{
			columns:
			[[
			  	{field:'m_strArticleNumber',title:'Article Number',sortable:true,width:100},
				{field:'m_strItemName',title:'Item Name',sortable:true,width:200,
					styler: function(value,row,index)
			  		{
			  			return {class:'DGcolumn'};
			  		}
			  	},
				{field:'m_strBrand',title:'Brand',sortable:true,width:100},
				{field:'m_strDetail',title:'Detail',sortable:true,width:150},
				{field:'m_nOpeningStock',title:'Opening Stock',sortable:true,width:100,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}},
					formatter:function(value,row,index)
		        	{
						var nOpeningStock = Number(row.m_nOpeningStock);
			  			return nOpeningStock.toFixed(2);
		        	}
				},
				{field:'m_nReorderLevel',title:'Reorder Level',sortable:true,width:100,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}},
					formatter:function(value,row,index)
		        	{
						var nReorderLevel = Number(row.m_nReorderLevel);
			  			return nReorderLevel.toFixed(2);
		        	}
				},
				{field:'m_nSellingPrice',title:'Selling Price',sortable:true,width:100,align:'right',editor:{'type':'numberbox','options':{precision:'2'}},
					formatter:function(value,row,index)
		        	{
						var nSellingPrice = Number(row.m_nSellingPrice);
						var nIndianFormat = formatNumber (nSellingPrice.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	}
				},
				{field:'m_nCostPrice',title:'Cost Price',sortable:true,width:100,align:'right',editor:{'type':'numberbox','options':{precision:'2',disabled:true}},
					formatter:function(value,row,index)
		        	{
						var nCostPrice = Number(row.m_nCostPrice);
						var nIndianFormat = formatNumber (nCostPrice.toFixed(2),row,index);
						return '<span class="rupeeSign">R  </span>' + nIndianFormat;
		        	}
				}
			]],
			onSortColumn: function (strColumn, strOrder)
			{
				m_oStockEntryListMemberData.m_strSortColumn = strColumn;
				m_oStockEntryListMemberData.m_strSortOrder = strOrder;
				stockEntryList_list (m_oStockEntryListMemberData.m_strSortColumn, m_oStockEntryListMemberData.m_strSortOrder, m_oStockEntryListMemberData.m_nPageNumber, m_oStockEntryListMemberData.m_nPageSize);
			}
		}
	);
	stockEntryList_list ('m_dCreatedOn','desc', 1, 15);
	stockEntryList_initDGPagination ();
}

function stockEntryList_isRowEditable (nRowIndex)
{
	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	var bRowEditable = true;
	var oRowData = $('#stockEntryList_table_stockEntryListDG').datagrid ('getRows')[nRowIndex];
	if(oRowData.m_oChildItems != null && oRowData.m_oChildItems.length > 0)
		bRowEditable = false;
	return bRowEditable
}

function stockEntryList_initDGPagination ()
{
	$('#stockEntryList_table_stockEntryListDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oStockEntryListMemberData.m_nPageNumber = nPageNumber;
				stockEntryList_list (m_oStockEntryListMemberData.m_strSortColumn, m_oStockEntryListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oStockEntryListMemberData.m_nPageNumber = nPageNumber;
				m_oStockEntryListMemberData.m_nPageSize = nPageSize;
				stockEntryList_list (m_oStockEntryListMemberData.m_strSortColumn, m_oStockEntryListMemberData.m_strSortOrder, nPageNumber, nPageSize);
			}
		}
	)
}

function stockEntryList_list (strColumn, strOrder, nPageNumber, nPageSize)
{
	assert.isString(strColumn, "strColumn expected to be a string.");
	assert.isString(strOrder, "strOrder expected to be a string.");
	assert.isNumber(nPageNumber, "nPageNumber expected to be a Number.");
	assert.isNumber(nPageSize, "nPageSize expected to be a Number.");
	m_oStockEntryListMemberData.m_strSortColumn = strColumn;
	m_oStockEntryListMemberData.m_strSortOrder = strOrder;
	m_oStockEntryListMemberData.m_nPageNumber = nPageNumber;
	m_oStockEntryListMemberData.m_nPageSize = nPageSize;
	loadPage ("inventorymanagement/progressbar.html", "secondDialog", "stockEntryList_progressbarLoaded ()");
}

function stockEntryList_progressbarLoaded ()
{
	createPopup('secondDialog', '', '', true);
	var oItemData = stockEntryList_getFormData ();
	ItemDataProcessor.list(oItemData, m_oStockEntryListMemberData.m_strSortColumn, m_oStockEntryListMemberData.m_strSortOrder, m_oStockEntryListMemberData.m_nPageNumber, m_oStockEntryListMemberData.m_nPageSize, stockEntryList_listed);
}

function stockEntryList_listFilter ()
{
	m_oStockEntryListMemberData.m_bIsFilterSet = true;
	stockEntryList_bIsEditEnd();
	var arrItems = $('#stockEntryList_table_stockEntryListDG').datagrid('getChanges');
	if (arrItems.length > 0)
		stockEntryList_saveChangesOnUserConfirmation ();
	else
		stockEntryList_list (m_oStockEntryListMemberData.m_strSortColumn, m_oStockEntryListMemberData.m_strSortOrder , 1, 15);
}

function stockEntryList_saveChangesOnUserConfirmation ()
{
	processConfirmation ('Yes', 'No', 'Do you want to save the changes ?', 
			stockEntryList_saveChangesOnUserFilterConfirm);
}

function stockEntryList_saveChangesOnUserFilterConfirm (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if (bConfirm)
		stockEntryList_updateStockEntries ();
	else
		stockEntryList_list (m_oStockEntryListMemberData.m_strSortColumn, m_oStockEntryListMemberData.m_strSortOrder, 1, 15);
}

function stockEntryList_getFormData ()
{
	var oItemData = new ItemData ();
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oItemData.m_strItemName = $("#stockEntryList_input_itemname").val();
	oItemData.m_strBrand = $("#stockEntryList_input_brand").val();
	oItemData.m_strArticleNumber = $('#stockEntryList_input_articleNumber').combobox('getValue');
	return oItemData;
}

function stockEntryList_cancel ()
{
	stockEntryList_bIsEditEnd();
	var arrItems = $('#stockEntryList_table_stockEntryListDG').datagrid('getChanges');
	if (arrItems.length > 0)
		stockEntryList_saveChangesOnUserCancel ();
	else
		HideDialog ("dialog");
}

function stockEntryList_saveChangesOnUserCancel ()
{
	processConfirmation ('Yes', 'No', 'Do you want to save the changes ?', 
			stockEntryList_saveChangesOnUserCancelConfirm);
}

function stockEntryList_saveChangesOnUserCancelConfirm (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if (bConfirm)
		stockEntryList_updateStockEntries ();
	else
		HideDialog("dialog");
}

function stockEntryList_listed (oResponse)
{
//	clearGridData ("#stockEntryList_table_stockEntryListDG");
	$('#stockEntryList_table_stockEntryListDG').datagrid('loadData',oResponse.m_arrItems);
	$('#stockEntryList_table_stockEntryListDG').datagrid('acceptChanges');
	$('#stockEntryList_table_stockEntryListDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oStockEntryListMemberData.m_nPageNumber});
	HideDialog("secondDialog");
}

function stockEntryList_editDG_cell (index, field)
{
	assert.isNumber(index, "index expected to be a Number.");
	if (stockEntryList_bIsEditEnd())
	{
        $('#stockEntryList_table_stockEntryListDG').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
        m_oStockEntryListMemberData.m_nEditIndexDG = index;
    	stockEntryList_setEditing(m_oStockEntryListMemberData.m_nEditIndexDG);
    }
}

function stockEntryList_bIsEditEnd()
{
    if (m_oStockEntryListMemberData.m_nEditIndexDG == undefined)
    {
    	return true
    }
    if ($('#stockEntryList_table_stockEntryListDG').datagrid('validateRow', m_oStockEntryListMemberData.m_nEditIndexDG))
    {
        $('#stockEntryList_table_stockEntryListDG').datagrid('endEdit', m_oStockEntryListMemberData.m_nEditIndexDG);
        m_oStockEntryListMemberData.m_nEditIndexDGeditIndex = -1;
        return true;
    } 
    else 
    {
        return false;
    }
}

function stockEntryList_updateStockEntries () 
{ 
	HideDialog ("ProcessDialog");
	$('#stockEntryList_table_stockEntryListDG').datagrid('endEdit', m_oStockEntryListMemberData.m_nEditIndexDG);
    m_oStockEntryListMemberData.m_nEditIndexDGeditIndex = -1;
	var oStockEntriesData = new StockEntriesData ();
	oStockEntriesData.m_arrStockEntries = stockEntryList_getStockEntriesArray ();
	if(oStockEntriesData.m_arrStockEntries.length > 0)
	{
		var oItemData = new ItemData ();
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	    ItemDataProcessor.updateStockEntries(oStockEntriesData, oItemData , stockEntryList_updatedStockEntries);
	}
	else
	{
		informUser("usermessage_stockentrylist_stockentriesupdatedsuccessfully", "kSuccess");
		stockEntryList_closeDialog ();
	}
}

function stockEntryList_getStockEntriesArray ()
{
	 var oStockEntriesArray = new Array ();
	 var arrItems = $('#stockEntryList_table_stockEntryListDG').datagrid('getChanges');
	 for (var nIndex = 0; nIndex < arrItems.length; nIndex++)
	 {
	 	var oItemData = new ItemData ();
	 	oItemData.m_nItemId = arrItems[nIndex].m_nItemId;
	 	oItemData.m_nOpeningStock = arrItems[nIndex].m_nOpeningStock;
	 	oItemData.m_nReorderLevel = arrItems[nIndex].m_nReorderLevel;
	 	oItemData.m_nSellingPrice = arrItems[nIndex].m_nSellingPrice;
	 	oItemData.m_nCostPrice = arrItems[nIndex].m_nCostPrice;
	 	oStockEntriesArray.push (oItemData);
	 }
	return oStockEntriesArray;
}

function stockEntryList_updatedStockEntries (oResponse, bHideDialog)
{
	if(oResponse.m_bSuccess)
	{
		informUser("usermessage_stockentrylist_stockentriesupdatedsuccessfully", "kSuccess");
		stockEntryList_closeDialog ();
	}
	else
		informUser("usermessage_stockentrylist_stockentriesupdationfailed", "kError");
}

function stockEntryList_closeDialog ()
{
	if(!m_oStockEntryListMemberData.m_bIsFilterSet)
		HideDialog ("dialog");
	else
		stockEntryList_list (m_oStockEntryListMemberData.m_strSortColumn, m_oStockEntryListMemberData.m_strSortOrder, 1, 15);
}

function stockEntryList_save ()
{
	loadPage ("include/process.html", "ProcessDialog", "stockEntryList_progressbar ()");
}

function stockEntryList_progressbar ()
{
	createPopup('ProcessDialog', '', '', true);
	m_oStockEntryListMemberData.m_bIsFilterSet = false;
	stockEntryList_updateStockEntries ();
}

function stockEntryList_setEditing(nRowIndex)
{
	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	assert.isOk(nRowIndex > -1, "nRowIndex must be a positive value.");
	var arrEditors = $('#stockEntryList_table_stockEntryListDG').datagrid('getEditors', nRowIndex);
	var oOpeningStockEditor = arrEditors[0];
	var oReorderLevelEditor = arrEditors[1];
	var oSellingPriceEditor = arrEditors[2];
	var oCostPriceEditor = arrEditors[3];
	if(stockEntryList_isRowEditable (nRowIndex))
	{
    	$(oOpeningStockEditor.target).numberbox('enable');
    	$(oReorderLevelEditor.target).numberbox('enable');
    	$(oCostPriceEditor.target).numberbox('enable');
    	$(oOpeningStockEditor.target).select();
	}
	else
		$(oSellingPriceEditor.target).select();
	
	oOpeningStockEditor.target.bind('focus', function ()
    		{
				$(oOpeningStockEditor.target).select();
    		});
	
	oReorderLevelEditor.target.bind('focus', function ()
    		{
				$(oReorderLevelEditor.target).select();
    		});
	
	oSellingPriceEditor.target.bind('focus', function ()
    		{
				$(oSellingPriceEditor.target).select();
    		});
	
	oCostPriceEditor.target.bind('focus', function ()
    		{
				$(oCostPriceEditor.target).select();
    		});
	
	oCostPriceEditor.target.bind('keydown', function (oEvent)
    		{
		    	if(oEvent.keyCode == 9)
		    	{
		    		oEvent.preventDefault();
		    		stockEntryList_editDG_cell (nRowIndex +1);
		    	}
    		});
}
