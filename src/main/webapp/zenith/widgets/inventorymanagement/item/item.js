var item_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/item/ItemGroupData.js',
	'widgets/inventorymanagement/item/ChildItemData.js',
	'widgets/inventorymanagement/sales/SalesLineItemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/item/ItemImagesData.js'
];

includeDataObjects (item_includeDataObjects, "item_loaded()");

function item_memberData ()
{
	this.m_buffImage = null;
	this.m_oItemData = new ItemData ();
	this.m_nItemId = -1;
	this.m_nPageNumber = 1;
    this.m_nPageSize =10;
    this.m_strSortColumn = "m_strGroupName";
    this.m_strSortOrder = "desc";
    this.m_arrSelectedGroupItems = new Array();
    this.m_nEditIndexDG = -1;
    this.m_arrItemPhotos = new Array();
}

var m_oItemMemberData = new item_memberData ();

function item_new ()
{
	createPopup("secondDialog", "#item_button_create", "#item_button_cancel", true);
	item_init ();
}

function item_initAdmin ()
{
	item_new ();
	document.getElementById ("item_img_add").style.visibility="visible";
	document.getElementById ("item_img_addCategory").style.visibility="visible";
}

function item_init ()
{
	$('#item_div_tabs').tabs ();
	initFormValidateBoxes ("item_form_id");
	item_initializeItemListDataGrid ();
	item_initializeItemGroupDataGrid ();
	item_populateApplicableTaxList ();
	item_initCategoryCombobox ();
	document.getElementById("item_button_saveAndContinue").style.visibility = 'visible';
}

function item_initCategoryCombobox ()
{
	$('#item_input_category').combobox
	({
		valueField:'m_nCategoryId',
	    textField:'m_strCategoryName',
	    selectOnNavigation: false,
	    loader: getFilteredCategoryData,
		mode: 'remote',
		formatter:function(row)
    	{
        	var opts = $(this).combobox('options');
        	return decodeURIComponent(row[opts.textField]);
    	},
		onSelect:function(row)
	    {
    		m_oItemMemberData.m_oCategoryDataRow = row;
    		item_setCategoryName ();
	    }
	});
	var categoryTextBox = $('#item_input_category').combobox('textbox');
	categoryTextBox[0].placeholder = "Enter Category Name";
	categoryTextBox.bind('keydown', function (e)
		    {
		      	item_handleKeyboardNavigation (e);
		    });
}

function item_handleKeyboardNavigation (e)
{
	assert.isObject(e, "e expected to be an Object.");
	if(e.keyCode == 13)
		item_setCategoryName ();
}

function item_setCategoryName ()
{
	var strCategoryName = decodeURIComponent(m_oItemMemberData.m_oCategoryDataRow[$('#item_input_category').combobox('options').textField])
	$("#item_input_category").combobox('setText',strCategoryName);
}

var getFilteredCategoryData = function(param, success, error)
{
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oItemCategoryData = new ItemCategoryData ();
		oItemCategoryData.m_strCategoryName = strQuery;
		ItemCategoryDataProcessor.getCategorySuggesstions (oItemCategoryData, "", "", function(oResponse)
				{
					var arrCategoryInfo = new Array ();
					for(var nIndex=0; nIndex< oResponse.m_arrItemCategory.length; nIndex++)
				    {
						arrCategoryInfo.push(oResponse.m_arrItemCategory[nIndex]);
						arrCategoryInfo[nIndex].m_strCategoryName = encodeURIComponent(oResponse.m_arrItemCategory[nIndex].m_strCategoryName);
				    }
					success(arrCategoryInfo);
				});
	}
	else
		success(new Array ());
}

function item_initializeItemGroupDataGrid ()
{
	$('#item_table_itemGroupDG').datagrid
	(
		{
			columns:
				[[
				    {field:'ckBox',checkbox:true,width:30},
				  	{field:'m_strGroupName',title:'Item Group<img title="Add" src="images/add.PNG" align="right" style="visibility:hidden;" id="item_img_add" onClick="item_addGroup ()"/>',sortable:true,width:150}
				]],
					onCheck: function (rowIndex, rowData)
					{
						item_holdCheckedData (rowData, true); 
					},
					onUncheck: function (rowIndex, rowData)
					{
						item_holdCheckedData (rowData, false); 
					},
					onCheckAll: function (arrRows)
					{
						Item_holdAllCheckedData (arrRows);
					},
					onUncheckAll: function (arrRows)
					{
						Item_holdAllUnCheckedData (arrRows); 
					}
		});
	item_initItemGroupDataGridPagination ();
	item_getItemGroupsName (m_oItemMemberData.m_strSortColumn, m_oItemMemberData.m_strSortOrder, 1, 10);	
}

function item_initializeItemListDataGrid ()
{
	$('#item_table_itemListDG').datagrid
	(
		{
			columns:
				[[
				  	{field:'m_strArticleNumber',title:'Article Number',sortable:true,width:60,
				  		formatter:function(value,row,index)
			        	{	
				  			return row.m_oItemData.m_strArticleNumber;
			        	}	
				  	},
				  	{field:'m_strItemName',title:'Item Name',sortable:true,width:60,
				  		formatter:function(value,row,index)
			        	{	
				  			return row.m_oItemData.m_strItemName;
			        	}
				  	},
					{field:'m_nQuantity',title:'Quantity',sortable:true,width:50,align:'right',editor:{'type':'numberbox','options':{precision:'2'}},
						formatter:function(value,row,index)
			        	{	
				  			return Number(value).toFixed(2);
			        	}
					},
					{field:'Actions',title:'',width:20,align:'center',
						formatter:function(value,row,index)
			        	{
			        		return item_addActions (row, index);
			        	}
					}
				]]
		});
}

function item_addActions (row, index)
{
	assert.isNumber(index, "index expected to be a Number.");
	var oActions = 
		'<table align="center">'+
				'<tr>'+
					'<td> <img title="Delete" src="images/delete.png" width="20" align="center" onClick="item_delete ('+index+')"/> </td>'+
				'</tr>'+
			'</table>'
	return oActions;
}

function item_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	$('#item_table_itemListDG').datagrid ('deleteRow', nIndex);
	refreshDataGrid ('#item_table_itemListDG');
	var arrItemList = $('#item_table_itemListDG').datagrid ('getRows');
	if(arrItemList.length > 0)
		item_setSKUPartTax (arrItemList);
	else
	{
		item_setSelectBoxEnability (false);
		document.getElementById("item_select_applicableTax").value = -1;
		document.getElementById("item_select_applicableTaxWithCForm").value = -1;
	}
		
}

function item_initItemGroupDataGridPagination ()
{
	$('#item_table_itemGroupDG').datagrid('getPager').pagination
	(
		{ 
			onRefresh:function(nPageNumber, nPageSize)
			{
				m_oItemMemberData.m_nPageNumber = nPageNumber;
				item_getItemGroupsName (m_oItemMemberData.m_strSortColumn, m_oItemMemberData.m_strSortOrder,nPageNumber, nPageSize);
			},
			onSelectPage:function (nPageNumber, nPageSize)
			{
				m_oItemMemberData.m_nPageNumber = nPageNumber;
				m_oItemMemberData.m_nPageSize = nPageSize;
				item_getItemGroupsName (m_oItemMemberData.m_strSortColumn, m_oItemMemberData.m_strSortOrder,nPageNumber, nPageSize);
			}
		}
	)
}

function item_getItemGroupsName (strColumn, strOrder, nPageNumber, nPageSize)
{
	var oItemGroupData = new ItemGroupData ();
	//ItemGroupDataProcessor.list (oItemGroupData, strColumn, strOrder, nPageNumber, nPageSize, item_gotItemGroupsName);
	ItemGroupDataProcessor.list (oItemGroupData, "", "", 1, 10, item_gotItemGroupsName);
}

function item_gotItemGroupsName (oResponse)
{
	$('#item_table_itemGroupDG').datagrid ('loadData',oResponse.m_arrItemGroupData);
	$('#item_table_itemGroupDG').datagrid('getPager').pagination ({total:oResponse.m_nRowCount, pageNumber:m_oItemMemberData.m_nPageNumber});
	Item_checkDGRow ();
}

function Item_checkDGRow ()
{
	var arrItemGroupData = $('#item_table_itemGroupDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrItemGroupData.length; nIndex++)
	{
		if(Item_isRowSelectable(arrItemGroupData[nIndex].m_strGroupName))
			$("#item_table_itemGroupDG").datagrid('checkRow', nIndex);
	}
}

function Item_isRowSelectable (strGroupName)
{
	assert.isString(strGroupName, "strGroupName expected to be a String.");
	var bIsSelectable = false;
	for (var nIndex = 0; nIndex < m_oItemMemberData.m_arrSelectedGroupItems.length && !bIsSelectable; nIndex++)
	{
		if(m_oItemMemberData.m_arrSelectedGroupItems[nIndex].m_strGroupName == strGroupName)
			bIsSelectable = true;
	}
	return bIsSelectable;
}

function item_holdCheckedData (oRowData, bIsForAdd)
{
	assert.isBoolean(bIsForAdd, "bIsForAdd should be a boolean value");
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	if(bIsForAdd)
	{
		if(!Item_isRowAdded (oRowData))
			m_oItemMemberData.m_arrSelectedGroupItems.push(oRowData);
	}
	else
		Item_removeItemGroup (oRowData);
}

function Item_isRowAdded (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	var bIsadded = false;
	for (var nIndex = 0; !bIsadded && nIndex < m_oItemMemberData.m_arrSelectedGroupItems.length; nIndex++)
		bIsadded = (m_oItemMemberData.m_arrSelectedGroupItems [nIndex].m_strGroupName == oRowData.m_strGroupName);
	return bIsadded;
}

function Item_removeItemGroup (oRowData)
{
	assert.isObject(oRowData, "oRowData expected to be an Object.");
	assert( Object.keys(oRowData).length >0 , "oRowData cannot be an empty .");// checks for non emptyness 
	for (var nIndex = 0; nIndex < m_oItemMemberData.m_arrSelectedGroupItems.length; nIndex++)
	{
		if(m_oItemMemberData.m_arrSelectedGroupItems[nIndex].m_strGroupName == oRowData.m_strGroupName)
		{
			m_oItemMemberData.m_arrSelectedGroupItems.splice(nIndex, 1);
			break;
		}
	}
}

function Item_holdAllCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		item_holdCheckedData(arrRows[nIndex], true);
}

function Item_holdAllUnCheckedData (arrRows)
{
	assert.isArray(arrRows, "arrRows is expected to be a type of Array");
	for (var nIndex = 0; nIndex < arrRows.length; nIndex++)
		item_holdCheckedData(arrRows[nIndex], false);
}

function item_edit ()
{
	m_oItemMemberData.m_oItemData.m_nItemId = m_oItemMemberData.m_nItemId;
	m_oItemMemberData.m_oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	m_oItemMemberData.m_oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	createPopup ("secondDialog", "#item_button_create", "#item_button_cancel", true);
	item_init ();
	document.getElementById ("item_img_add").style.visibility="visible";
	document.getElementById("item_button_create").setAttribute('update', true);
	document.getElementById("item_button_create").innerHTML = "Update";
	document.getElementById("item_button_saveAndContinue").style.visibility = 'hidden';
	document.getElementById("item_button_saveAndContinue").style.width='0px';
	document.getElementById("item_button_reset").style.visibility = 'hidden';
	document.getElementById("item_button_reset").style.width='0px';
	ItemDataProcessor.get (m_oItemMemberData.m_oItemData, item_gotData);
}

function item_gotData (oResponse)
{	
	HideDialog("ProcessDialog");
	m_oItemMemberData.m_oItemData = oResponse.m_arrItems[0];
	m_oItemMemberData.m_strImageName = m_oItemMemberData.m_oItemData.m_strImageName;
	item_populateCategoryCombobox(m_oItemMemberData.m_oItemData);
	if (m_oItemMemberData.m_oItemData != null && m_oItemMemberData.m_oItemData != undefined && m_oItemMemberData.m_oItemData.m_buffImgPhoto != null)
		$("#item_img_itemImage").attr('src', m_oItemMemberData.m_oItemData.m_buffImgPhoto);
	try
	{
		$("#item_select_applicableTax").val(m_oItemMemberData.m_oItemData.m_oApplicableTax.m_nId);
		$("#item_select_applicableTaxWithCForm").val(m_oItemMemberData.m_oItemData.m_oTaxWithCForm.m_nId);
	}
	catch(oException){}
	$("#item_input_articleNumber").val(m_oItemMemberData.m_oItemData.m_strArticleNumber);
	$("#item_input_itemname").val(m_oItemMemberData.m_oItemData.m_strItemName);
	$('#item_input_category').combobox('select', m_oItemMemberData.m_oItemData.m_oItemCategoryData.m_nCategoryId);	
	$("#item_input_brand").val(m_oItemMemberData.m_oItemData.m_strBrand);
	$("#item_input_detail").val(m_oItemMemberData.m_oItemData.m_strDetail);
	$("#item_input_unit").val(m_oItemMemberData.m_oItemData.m_strUnit);
	$("#item_input_locationCode").val(m_oItemMemberData.m_oItemData.m_strLocationCode);
	item_loadCheckedData(m_oItemMemberData.m_oItemData);
	item_loadChildItems(m_oItemMemberData.m_oItemData.m_oChildItems);
	item_loadItemImages (m_oItemMemberData.m_oItemData.m_oItemImage);
	initFormValidateBoxes ("item_form_id");
}

function item_loadItemImages (arrItemImages)
{
	assert.isArray(arrItemImages, "arrItemImages is expected to be a type of Array");
	for(var nIndex=0; nIndex < arrItemImages.length; nIndex++)
	{
		var oItemImagesData = new ItemImagesData ();
		oItemImagesData.m_nId = arrItemImages[nIndex].m_nId;
		oItemImagesData.m_strFileName = arrItemImages[nIndex].m_strFileName;
		m_oItemMemberData.m_arrItemPhotos.push(oItemImagesData);
	}
}

function item_populateCategoryCombobox (oItemData)
{
	assert.isObject(oItemData, "oItemData expected to be an Object.");
	assert( Object.keys(oItemData).length >0 , "oItemData cannot be an empty .");// checks for non emptyness 
	var oItemCategoryData = new ItemCategoryData ();
	oItemCategoryData.m_strCategoryName = oItemData.m_oItemCategoryData.m_strCategoryName;
	ItemCategoryDataProcessor.getCategorySuggesstions (oItemCategoryData, "", "", function(oResponse)
			{
				var arrCategoryInfo = new Array ();
				for(var nIndex=0; nIndex< oResponse.m_arrItemCategory.length; nIndex++)
			    {
					arrCategoryInfo.push(oResponse.m_arrItemCategory[nIndex]);
					arrCategoryInfo[nIndex].m_strCategoryName = encodeURIComponent(oResponse.m_arrItemCategory[nIndex].m_strCategoryName);
			    }
				$('#item_input_category').combobox('loadData',arrCategoryInfo)
			});
}

function item_loadCheckedData (oItemData)
{
	assert.isObject(oItemData, "oItemData expected to be an Object.");
	assert( Object.keys(oItemData).length >0 , "oItemData cannot be an empty .");// checks for non emptyness 
	arrItemGroup = oItemData.m_oItemGroups;
	for(var nIndex=0; nIndex < arrItemGroup.length; nIndex++)
		item_checkRow (arrItemGroup[nIndex])
}

function item_checkRow (oItemGroup)
{
	assert.isObject(oItemGroup, "oItemGroup expected to be an Object.");
	assert( Object.keys(oItemGroup).length >0 , "oItemGroup cannot be an empty .");// checks for non emptyness 
	var arrItemGroupData = $('#item_table_itemGroupDG').datagrid('getRows');
	for (nIndex = 0; nIndex < arrItemGroupData.length; nIndex++)
	{
		if(oItemGroup.m_nItemGroupId == arrItemGroupData[nIndex].m_nItemGroupId)
		{
			$("#item_table_itemGroupDG").datagrid('checkRow', nIndex);
			break;
		}
	}
}

function item_loadChildItems (arrChildItems)
{
	assert.isArray(arrChildItems, "arrChildItems is expected to be a type of Array");
	$('#item_table_itemListDG').datagrid('loadData',arrChildItems);
	if(arrChildItems.length > 0)
		item_setSKUPartTax (arrChildItems);
}

function item_submit ()
{
	if (item_validate())
		loadPage ("include/process.html", "ProcessDialog", "item_progressbarLoaded ()");
}

function item_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	oItemData = item_getFormData ();
	if(document.getElementById("item_button_create").getAttribute('update') == "false")
		OwnItemDataProcessor.create (oItemData, item_created);
	else
		ItemDataProcessor.update(oItemData, item_updated);
}

function item_saveAndContinue ()
{
	if (item_validate())
		loadPage ("include/process.html", "ProcessDialog", "item_progressbarcreateAndContinue ()");
}

function item_reset ()
{
	$("#item_input_articleNumber").val("");
	$("#item_input_itemname").val("");
	$("#item_input_category").combobox('setText','');
	$("#item_input_brand").val("");
	$("#item_input_detail").val("");
	$("#item_input_unit").val("");
	$("#item_input_locationCode").val("");
	$("#item_input_uploadImg").val("");  
	$("#itemGroup_input_groupName").val("");
	$('#item_table_itemGroupDG').datagrid('clearChecked');
	$('#item_table_itemListDG').datagrid('loadData',[]);
	item_setSelectBoxEnability (false);
	item_populateApplicableTaxList ();
}


function item_progressbarcreateAndContinue ()
{
	createPopup('ProcessDialog', '', '', true);
	oItemData = item_getFormData ();
	OwnItemDataProcessor.create (oItemData, item_createAndContinue);
}

function item_createAndContinue (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser("usermessage_item_itemcreatedsuccessfully", "kSuccess");
		$("#item_input_articleNumber").val("");
	}
	else
		informUser("usermessage_item_itemcreationfailed", "kError");
}


function item_updated (oResponse)
{
	HideDialog("ProcessDialog");
	if(oResponse.m_bSuccess)
	{
		informUser("usermessage_item_itemupdatedsuccessfully", "kSuccess");
		try
		{
			item_handleAfterUpdate ();
		}
		catch(oException){}
	}
	HideDialog ("secondDialog");
}

function item_created (oResponse)
{
	HideDialog("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser("usermessage_item_itemcreatedsuccessfully", "kSuccess");
		HideDialog ("secondDialog");
		try
		{
			item_handleAfterSave ();
		}
		catch(oException){}
	}
	else
		informUser("usermessage_item_itemcreationfailed", "kError");
}

function item_validate ()
{
	return validateForm("item_form_id") && item_selectValidate ();
}

function item_selectValidate ()
{
	var bIsSelectFieldValid = true;
	if($("#item_select_applicableTax").val()== -1)
	{
		informUser("Please Select Applicable Tax With In State Code", "kWarning");
		bIsSelectFieldValid = false;
	}
	else if($("#item_select_applicableTaxWithCForm").val()== -1)
	{
		informUser("Please Select Applicable Tax With C Form", "kWarning");
		bIsSelectFieldValid = false;
	}
	else if(!item_isValidCategory())
	{
		informUser("Please Select Category Name", "kWarning");
		$('#item_input_category').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function item_isValidCategory ()
{
	var bIsValid = false;
	try
	{
		var strCategory = $('#item_input_category').combobox('getValue');
		if(strCategory != "" && parseInt(strCategory)> 0 && parseInt(strCategory)!= NaN)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function item_getFormData ()
{
	item_acceptDGchanges ();
	var oItemData = new ItemData ();
	oItemData.m_oUserCredentialsData = new UserInformationData ();
	oItemData.m_nItemId = m_oItemMemberData.m_oItemData.m_nItemId;
	oItemData.m_nOpeningStock = m_oItemMemberData.m_oItemData.m_nOpeningStock;
	oItemData.m_nReorderLevel = m_oItemMemberData.m_oItemData.m_nReorderLevel;
	oItemData.m_nSellingPrice = m_oItemMemberData.m_oItemData.m_nSellingPrice;
	oItemData.m_nCostPrice = m_oItemMemberData.m_oItemData.m_nCostPrice;
	oItemData.m_nReceived = m_oItemMemberData.m_oItemData.m_nReceived;
	oItemData.m_nIssued = m_oItemMemberData.m_oItemData.m_nIssued;
	oItemData.m_bPublishOnline = m_oItemMemberData.m_oItemData.m_bPublishOnline;
	oItemData.m_strArticleNumber = $("#item_input_articleNumber").val();
	oItemData.m_strItemName = $("#item_input_itemname").val();
	oItemData.m_oItemCategoryData = new ItemCategoryData();
	oItemData.m_oItemCategoryData.m_nCategoryId = $('#item_input_category').combobox('getValue');
	oItemData.m_strBrand = $("#item_input_brand").val();
	oItemData.m_strDetail = $("#item_input_detail").val();
	oItemData.m_strUnit = $("#item_input_unit").val();
	oItemData.m_oApplicableTax.m_nId = $("#item_select_applicableTax").val();
	oItemData.m_oTaxWithCForm.m_nId = $("#item_select_applicableTaxWithCForm").val();
	oItemData.m_strLocationCode = $("#item_input_locationCode").val();
	oItemData.m_buffImgPhoto =  m_oItemMemberData.m_buffImage;
	oItemData.m_strImageName = m_oItemMemberData.m_strImageName; 
	oItemData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	oItemData.m_arrChildItems = item_getChildItemList ();
	oItemData.m_arrItemGroups = item_getCheckedItemGroups ();
	oItemData.m_arrItemImages = item_getItemImages ();
	return oItemData;
}

function item_getItemImages ()
{
	var oItemImagesDataArray = new Array ();
	var arrItemImages = m_oItemMemberData.m_arrItemPhotos;
	for (var nIndex = 0; nIndex < arrItemImages.length; nIndex++)
	{
			var oItemImagesData = new ItemImagesData ();
			oItemImagesData.m_nId = arrItemImages [nIndex].m_nId;
			oItemImagesData.m_strFileName = arrItemImages [nIndex].m_strFileName;
			oItemImagesDataArray.push(oItemImagesData);
	}
	return oItemImagesDataArray;
}

function item_buildSubImagesList ()
{
	var oItemImagesDataArray = new Array ();
	var arrItemImages = m_oItemMemberData.m_arrItemPhotos;
	for (var nIndex = 0; nIndex < arrItemImages.length; nIndex++)
	{
			var oItemImagesData = new ItemImagesData ();
			oItemImagesData.m_nId = arrItemImages [nIndex].m_nId;
			oItemImagesDataArray.push(oItemImagesData);
	}
	return oItemImagesDataArray;
}

function item_getCheckedItemGroups ()
{
	var oItemGroupDataArray = new Array ();
	var arrItemGroups = $('#item_table_itemGroupDG').datagrid('getChecked');
	for (var nIndex = 0; nIndex < arrItemGroups.length; nIndex++)
	{
		var oItemGroupData = new ItemGroupData ();
		oItemGroupData.m_nItemGroupId = arrItemGroups [nIndex].m_nItemGroupId;
		oItemGroupData.m_strGroupName = arrItemGroups [nIndex].m_strGroupName;
		oItemGroupDataArray.push(oItemGroupData);
	}
	return oItemGroupDataArray;
}

function item_loadImagePreview ()
{
	var oItemData = new ItemData ();
	oItemData.m_buffImgPhoto = $("#item_input_uploadImg").val();  
	m_oItemMemberData.m_buffImage = oItemData.m_buffImgPhoto;
	m_oItemMemberData.m_strImageName = getImageName (m_oItemMemberData.m_buffImage.value);
	validateImageFile($("#item_input_uploadImg")[0].files, "#item_input_uploadImg");
	ItemDataProcessor.getImagePreview(oItemData, item_gotImagePreviewData);
}

function item_gotImagePreviewData (oItemData)
{
	assert.isObject(oItemData, "oItemData expected to be an Object.");
	assert( Object.keys(oItemData).length >0 , "oItemData cannot be an empty .");// checks for non emptyness 
	$("#item_img_itemImage").attr('src', oItemData.m_buffImgPhoto);
}

function item_populateApplicableTaxList ()
{
	var oItemData = new ItemData ();
	ApplicableTaxDataProcessor.list (oItemData.m_oApplicableTax, "m_strApplicableTaxName", "asc", 1, 10, item_gotApplicableTaxList);
}

function item_gotApplicableTaxList(oResponse)
{
	item_prepareApplicableTaxDD ("item_select_applicableTax", oResponse);
	item_prepareApplicableTaxDD ("item_select_applicableTaxWithCForm", oResponse);
}

function item_prepareApplicableTaxDD (strApplicableTaxDD, oResponse)
{
	var arrOptions = new Array ();
	arrOptions.push (CreateOption (-1,"Select"));
	for (var nIndex = 0; nIndex < oResponse.m_arrApplicableTax.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrApplicableTax [nIndex].m_nId, oResponse.m_arrApplicableTax [nIndex].m_strApplicableTaxName));
	PopulateDD (strApplicableTaxDD, arrOptions);
}

function item_cancel ()
{
	HideDialog ("secondDialog");
}

function item_checkArticleNumber ()
{
	var strArticleNumber = $("#item_input_articleNumber").val();
	if (strArticleNumber != m_oItemMemberData.m_oItemData.m_strArticleNumber)
	{
		var oItemData = new ItemData ();
		oItemData.m_strArticleNumber = strArticleNumber;
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		ItemDataProcessor.list(oItemData, "", "", 1,10, item_listed);
	}
}

function item_listed (oResponse)
{
	if (oResponse.m_arrItems.length > 0)
	{
		informUser ("usermessage_item_articlealreadyexists", "kWarning");
		$("#item_input_articleNumber").val("");
		document.getElementById("item_input_articleNumber").focus();
	}
}

function item_addGroup ()
{
	navigate ("itemGroup", "widgets/inventorymanagement/item/addItemGroup.js");
}

function itemGroup_handleAfterSave ()
{
	//functionality after create item group
	item_initializeItemGroupDataGrid ();
}

function item_getChildItemList ()
{
	var oItemListDataArray = new Array ();
	var arrItemList = $('#item_table_itemListDG').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrItemList.length; nIndex++)
	{
		if(arrItemList[nIndex].m_nQuantity < 0)
		{
			var oChildItemData = new ChildItemData ();
			oChildItemData.m_oItemData.m_nItemId = arrItemList [nIndex].m_oItemData.m_nItemId;
			oChildItemData.m_nQuantity = Number(arrItemList [nIndex].m_nQuantity);
			oItemListDataArray.push(oChildItemData);
		}
	}
	return oItemListDataArray;
}


function item_editDG_cell (index, field)
{
	assert.isNumber(index, "index expected to be a Number.");
	if (item_bIsEditEnd())
	{
        $('#item_table_itemListDG').datagrid('selectRow', index)
                .datagrid('beginEdit', index);
        m_oItemMemberData.m_nEditIndexDG = index;
        item_setEditing(m_oItemMemberData.m_nEditIndexDG);
    }
}

function item_setEditing (nRowIndex)
{
	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	var arrEditors = $('#item_table_itemListDG').datagrid('getEditors', nRowIndex);
	var oQuantityEditor = arrEditors[0];
	$(oQuantityEditor.target).select();
	
	oQuantityEditor.target.bind('focus', function ()
    		{
				$(oQuantityEditor.target).select();
    		});
	
	oQuantityEditor.target.bind('change blur', function ()
    		{
				var nQuantity = Number(oQuantityEditor.target.val());
		    	if(nQuantity <= 0)
		    	{
		    		informUser ("Quantity should not be zero", "kWarning");
		    		$(oQuantityEditor.target).val('');
		    		oQuantityEditor.target.focus ();
		    	}
    		});
	
	oQuantityEditor.target.bind('keydown', function (oEvent)
    		{
		    	if(oEvent.keyCode == 9)
		    	{
		    		oEvent.preventDefault();
		    		item_editDG_cell (nRowIndex +1);
		    	}
    		});
}

function item_bIsEditEnd ()
{
	 if (m_oItemMemberData.m_nEditIndexDG == undefined)
	    {
	    	return true
	    }
	    if ($('#item_table_itemListDG').datagrid('validateRow', m_oItemMemberData.m_nEditIndexDG))
	    {
	        $('#item_table_itemListDG').datagrid('endEdit', m_oItemMemberData.m_nEditIndexDG);
	        m_oItemMemberData.m_nEditIndexDG = -1;
	        return true;
	    } 
	    else 
	    {
	        return false;
	    }
}

function item_acceptDGchanges()
{
	if (item_bIsEditEnd())
        $('#item_table_itemListDG').datagrid('acceptChanges');
}

function item_addChildItems ()
{
	navigate ('addChildItems','widgets/inventorymanagement/item/listItemsForChild.js');
}

function item_setSKUPartTax (arrItemList)
{
	assert.isArray(arrItemList, "arrItemList is expected to be a type of Array");
	var arrApplicableTaxWithinState = new Array ();
	var arrApplicableTaxWithCForm = new Array ();
	for (var nIndex = 0; nIndex < arrItemList.length; nIndex++)
	{
		arrApplicableTaxWithinState.push(arrItemList[nIndex].m_oItemData.m_oApplicableTax);
		arrApplicableTaxWithCForm.push(arrItemList[nIndex].m_oItemData.m_oTaxWithCForm);
	}
	arrApplicableTaxWithinState.sort(function(oApplicableTax1, oApplicableTax2)
			{
				return (oApplicableTax2.m_oTaxes[0].m_nPercentage - oApplicableTax1.m_oTaxes[0].m_nPercentage)
			});
	arrApplicableTaxWithCForm.sort(function(oTaxWithCForm1, oTaxWithCForm2)
			{
				return (oTaxWithCForm2.m_oTaxes[0].m_nPercentage - oTaxWithCForm1.m_oTaxes[0].m_nPercentage)
			});
	document.getElementById("item_select_applicableTax").value = arrApplicableTaxWithinState[0].m_nId;
	document.getElementById("item_select_applicableTaxWithCForm").value = arrApplicableTaxWithCForm[0].m_nId;
	item_setSelectBoxEnability (true);
}

function item_setSelectBoxEnability (bDisable)
{
	assert.isBoolean(bDisable, "bDisable should be a boolean value");
	document.getElementById("item_select_applicableTax").disabled = bDisable;
	document.getElementById("item_select_applicableTaxWithCForm").disabled = bDisable;
}

function item_addCategory ()
{
	navigate ('itemCategoryNew','widgets/inventorymanagement/item/itemCategoryNew.js');
}