var stockTransfer_includeDataObjects = 
[
	'widgets/inventorymanagement/stocktransfer/StockTransferData.js',
	'widgets/inventorymanagement/stocktransfer/StockTransferLineItemData.js',
	'widgets/inventorymanagement/location/LocationData.js',
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

includeDataObjects (stockTransfer_includeDataObjects, "stockTransfer_loaded ()");

function stockTransfer_memberData ()
{
	this.nEditIndex = undefined;
	this.m_arrArticle = new Array ();
	this.m_arrArticleDetails = "";
	this.m_strXMLData = "";
}

var m_oStockTransferMemberData = new stockTransfer_memberData ();

function stockTransfer_new ()
{
	createPopup("dialog", "#stockTransfer_button_create", "#stockTransfer_button_cancel", true);
	stockTransfer_init ();
}

function stockTransfer_edit ()
{
	
}

function stockTransfer_init ()
{
	initFormValidateBoxes ("stockTransfer_form_id");
//	$( "#stockTransfer_input_date" ).datebox({required:true});
	$( "#stockTransfer_input_date" ).datepicker({minDate:"-5y"});
	var dDate = new Date();
	var dCurrentDate = (dDate.getMonth()+1) +'/'+  dDate.getDate() +'/'+ dDate.getFullYear();
//	$('#stockTransfer_input_date').datebox('setValue', dCurrentDate);
	$('#stockTransfer_input_date').datepicker('setDate', dCurrentDate);
	stockTransfer_initializeDataGrid ();
	stockTransfer_appendRow ();
	stockTransfer_populateLocationList ();
}

function stockTransfer_initializeDataGrid ()
{
	$('#stockTransfer_table_articles').datagrid ({
	    columns:[[  
	        {field:'m_strArticleNumber',title:'Article#', width:95,
	        	editor:
	        	{
	        	type:'combobox',
		        options:
		        	{
			        	valueField:'m_strArticleDetail',
			        	textField:'m_strArticleNumber',
			        	hasDownArrow: false,
			        	panelWidth: 400,
			        	selectOnNavigation: false,
			        	loader: getFilteredItemData,
		        		mode: 'remote',
			        	formatter:function(row)
			        	{
			        		var opts = $(this).combobox('options');
			        		return decodeURIComponent(row[opts.valueField]);
			        	},
			        	onSelect: function(row)
			        	{
			        		var opts = $(this).combobox('options');
			        		var strDecodeDetails = decodeURIComponent (row[opts.valueField]);
			        		m_oStockTransferMemberData.m_arrArticleDetails = strDecodeDetails.split(" | ");
			        		stockTransfer_setArticleDetails ();
			        	}
		        	}
	        	}
	        },
	        {field:'m_strName',title:'Name',width:210, 
	        	editor:
	        	{
	        	type:'combobox',
		        options:
		        	{
			        	valueField:'m_strArticleDetail',
			        	textField:'m_strName',
			        	hasDownArrow: false,
			        	panelWidth: 400,
			        	selectOnNavigation: false,
			        	loader: getFilteredItemData,
		        		mode: 'remote',
			        	formatter:function(row)
			        	{
			        		var opts = $(this).combobox('options');
			        		return decodeURIComponent(row[opts.valueField]);
			        	},
			        	onSelect: function(row)
			        	{
			        		var opts = $(this).combobox('options');
			        		var strDecodeDetails = decodeURIComponent (row[opts.valueField]);
			        		m_oStockTransferMemberData.m_arrArticleDetails = strDecodeDetails.split(" | ");
			        		stockTransfer_setArticleDetails ();
			        	}
		        	}
	        	}
	        },
	        {field:'m_strDetail',title:'Detail',width:80,editor:'text'},
	        {field:'m_nQuantity',title:'Qty',align:'right', width:60,editor:{'type' :'numberbox','options':{precision:'2'}}},
	        {field:'Actions',title:'Action',align:'center',width:50,
				formatter:function(value,oRow,nIndex)
				{
		        	if(nIndex != 0)
	        			return stockTransfer_displayImages (oRow, nIndex);
	        		else
	        			return stockTransfer_removeAction ();
				}
			},
	    ]]
	});
	
	$('#stockTransfer_table_articles').datagrid
	(
		{
			onClickRow: function (nRowIndex, oRowData)
			{
				stockTransfer_editRowDG (oRowData, nRowIndex);
			}
		}
	)
}

function stockTransfer_listed (oResponse)
{
	if (oResponse.m_arrItems.length < 0)
	{
		informUser ("usermessage_item_articlealreadyexists", "kWarning");
	}
}

function stockTransfer_displayImages (oRow, nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage ='<table align="center">'+
					'<tr>'+
						'<td> <img title="Delete" src="images/delete.png" width="20"  id="deleteImageId" onClick="stockTransfer_delete('+nIndex+')"/> </td>'+
					'</tr>'+
				'</table>'
	return oImage;
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
		oItemData.m_strItemName = strQuery;
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		ItemDataProcessor.getArticleSuggesstions(oItemData, "", "", function(oItemDataResponse)
				{
					var arrItemData = new Array();
					for(var nIndex=0; nIndex< oItemDataResponse.m_arrItems.length; nIndex++)
				    {
						arrItemData.push(oItemDataResponse.m_arrItems[nIndex]);
						arrItemData[nIndex].m_strArticleDetail = encodeURIComponent(arrItemData[nIndex].m_strArticleNumber + " | " +
						arrItemData[nIndex].m_strItemName + " | " +
						arrItemData[nIndex].m_strDetail); 
				    }
					success(arrItemData);
				});
	}
	else
		success(new Array ());
}

function stockTransfer_delete (nIndex)
{
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	$('#stockTransfer_table_articles').datagrid ('deleteRow', nIndex);
}

function stockTransfer_removeAction ()
{
	var oImage ='<table align="center">'+
				'<tr>'+
				'<td style="border-style:none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>'+
				'</tr>'+
				'</table>'
	return oImage;
}

function stockTransfer_editRowDG (oRowData, nRowIndex)
{
	assert.isNumber(nRowIndex, "nRowIndex expected to be a Number.");
	 if (m_oStockTransferMemberData.nEditIndex != nRowIndex)
	    {
	        if (stockTransfer_endEdit ())
	        {
	            $('#stockTransfer_table_articles').datagrid('selectRow', nRowIndex)
	                    .datagrid('beginEdit', nRowIndex);
	            m_oStockTransferMemberData.editIndex = nRowIndex;
	            stockTransfer_setEditing(m_oStockTransferMemberData.editIndex);
	        } 
	        else 
	        {
	            $('#stockTransfer_table_articles').datagrid('selectRow', m_oStockTransferMemberData.nEditIndex);
	        }
	    }
}

function stockTransfer_appendRow ()
{
    if (stockTransfer_endEdit ())
    {
    	stockTransfer_acceptDGchanges ();
        $('#stockTransfer_table_articles').datagrid('appendRow',{m_strArticleNumber:''});
        m_oStockTransferMemberData.nEditIndex = $('#stockTransfer_table_articles').datagrid('getRows').length-1;
        $('#stockTransfer_table_articles').datagrid('selectRow',  m_oStockTransferMemberData.nEditIndex)
                .datagrid('beginEdit',  m_oStockTransferMemberData.nEditIndex);
        $('#m_strArticleNumber').validatebox ();
        stockTransfer_setEditing ( m_oStockTransferMemberData.nEditIndex);
    }
}

function stockTransfer_endEdit ()
{
    if (m_oStockTransferMemberData.nEditIndex == undefined)
    {
    	return true
	}
    if ($('#stockTransfer_table_articles').datagrid('validateRow', m_oStockTransferMemberData.nEditIndex))
    {
        $('#stockTransfer_table_articles').datagrid('endEdit',  m_oStockTransferMemberData.nEditIndex);
        m_oStockTransferMemberData.nEditIndex = undefined;
        return true;
    } 
    else 
    {
        return false;
    }
}

function stockTransfer_setEditing (nRowIndex)
{
    var editors = $('#stockTransfer_table_articles').datagrid('getEditors', nRowIndex);
    var articleNumberEditor = editors[0];
    var nameEditor = editors[1];
    var detailEditor = editors[2];
    var quantityEditor = editors[3];
    var articleNoTextbox =  $(articleNumberEditor.target).combobox('textbox');
    articleNoTextbox.focus ();
    var nameTextbox = $(nameEditor.target).combobox('textbox');
    articleNoTextbox.bind('keydown', function (e)
    {
    	stockTransfer_keyboardNavigation (e)
    });
    articleNoTextbox.bind('change', function (e)
    	    {
    	stockTransfer_articleCheck (e)
    	    });
    nameTextbox.bind('keydown', function (e)
    {
    	stockTransfer_keyboardNavigation (e)
    });
    nameTextbox.bind('change', function (e)
    	    {
    	stockTransfer_nameCheck (e)
    	    });
    quantityEditor.target.bind('change', function()
    		{
    			stockTransfer_appendNextRow(nRowIndex);
    		});
    
    function stockTransfer_appendNextRow(nRowIndex)
    {
    	if (nRowIndex ==$('#stockTransfer_table_articles').datagrid('getRows').length-1)
    		stockTransfer_appendRow ();
    }
    
    function stockTransfer_keyboardNavigation (e)
    {
    	if(e.keyCode == 13)
    	{
    		stockTransfer_setArticleDetails ();
    	}
    }
    
}

function stockTransfer_articleCheck ()
{
	var editors = $('#stockTransfer_table_articles').datagrid('getEditors', m_oStockTransferMemberData.nEditIndex);
	var articleNoEditor = editors[0];
	$(articleNoEditor.target).combobox('setValue','');
	$(articleNoEditor.target).combobox('textbox').focus();
}

function stockTransfer_nameCheck()
{
	var editors = $('#stockTransfer_table_articles').datagrid('getEditors', m_oStockTransferMemberData.nEditIndex);
	var nameEditor = editors[1];
	$(nameEditor.target).combobox('setValue','');
	$(nameEditor.target).combobox('textbox').focus();
}
function stockTransfer_acceptDGchanges()
{
    if (stockTransfer_endEdit ())
        $('#stockTransfer_table_articles').datagrid('acceptChanges');
}

function stockTransfer_populateLocationList ()
{
	var oLocationData = new LocationData ();
	LocationDataProcessor.list (oLocationData, "m_strName", "asc", "", "", stockTransfer_gotLocationList);
}

function stockTransfer_gotLocationList (oResponse)
{
	stockTransfer_prepareLocationNameDD ("stockTransfer_select_locationNameFrom", oResponse);
	stockTransfer_prepareLocationNameDD ("stockTransfer_select_locationNameTo", oResponse);
}

function stockTransfer_prepareLocationNameDD (strLocationNameDD, oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrLocations.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrLocations [nIndex].m_nLocationId,
				oResponse.m_arrLocations [nIndex].m_strName));
	PopulateDD (strLocationNameDD, arrOptions);
}

function stockTransfer_cancel ()
{
	HideDialog ("dialog");
}

function stockTransfer_getFormData ()
{
	stockTransfer_acceptDGchanges ();
	var oStockTransferData = new StockTransferData ();
	oStockTransferData.m_oUserCredentialsData = new UserInformationData ();
	oStockTransferData.m_oTransferredFrom.m_nLocationId = $("#stockTransfer_select_locationNameFrom").val();
	oStockTransferData.m_oTransferredTo.m_nLocationId = $("#stockTransfer_select_locationNameTo").val();
//	oStockTransferData.m_strDate = FormatDate ($('#stockTransfer_input_date').datebox('getValue'));
	oStockTransferData.m_strDate = FormatDate ($('#stockTransfer_input_date').val());
	oStockTransferData.m_arrStockTransferLineItem = stockTransfer_getLineItemDataArray ();
	oStockTransferData.m_oTransferredBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oStockTransferData.m_oTransferredBy.m_nUID = m_oTrademustMemberData.m_nUID;
	oStockTransferData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oStockTransferData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	return oStockTransferData;
}

function stockTransfer_getLineItemDataArray ()
{
	var oLineItemDataArray = new Array ();
	var arrLineItemData = $('#stockTransfer_table_articles').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		var oStockTransferLineItemData = new StockTransferLineItemData ();
		oStockTransferLineItemData.m_strArticleNumber =  arrLineItemData [nIndex].m_strArticleNumber;
		oStockTransferLineItemData.m_nQuantity = arrLineItemData [nIndex].m_nQuantity;
		if (oStockTransferLineItemData.m_strArticleNumber != "")
			oLineItemDataArray.push (oStockTransferLineItemData);
	}
	return oLineItemDataArray;
}

function stockTransfer_validate ()
{
	return stockTransfer_validateSelectField ();
}

function stockTransfer_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	if($("#stockTransfer_select_locationNameFrom").val() == $("#stockTransfer_select_locationNameTo").val())
	{
		bIsSelectFieldValid = false;
		informUser("Location cannot be same", "kWarning");
	}
	return bIsSelectFieldValid;
}

function stockTransfer_validateDGRow ()
{
	var bIsValidDGRow = true;
	stockTransfer_acceptDGchanges ();
	var arrStockTransferLineItem = $('#stockTransfer_table_articles').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrStockTransferLineItem.length; nIndex++)
	{
		var strArticleNumber =  arrStockTransferLineItem [nIndex].m_strArticleNumber;
		var nQuantity = arrStockTransferLineItem [nIndex].m_nQuantity;
		if (strArticleNumber.trim() !="" && (nQuantity == "" || nQuantity == 0))
		{
			bIsValidDGRow = false;
			informUser (nQuantity == "" ? "Quantity cannot be empty." : "Quantity should be greater than 0.", "kWarning");
			break;
		}
	}
	return bIsValidDGRow;
}

function stockTransfer_submit ()
{
	if (stockTransfer_validate () && stockTransfer_validateArticleDetails () && stockTransfer_validateDGRow ())
	{
		var oStockTransferData = stockTransfer_getFormData ();
		StockTransferDataProcessor.transfer (oStockTransferData, stockTransfer_created);
	}
}


function stockTransfer_validateArticleDetails ()
{
	var bIsValidArticle = false;
	var oStockTransferData = new StockTransferData ();
	oStockTransferData.m_arrStockTransferLineItem = stockTransfer_getLineItemDataArray ();
	if(oStockTransferData.m_arrStockTransferLineItem.length > 0)
		bIsValidArticle = true;
	else
		informUser(" Article Details cannot be empty ", "kWarning");
	return bIsValidArticle;
}

function stockTransfer_created (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser ("Stock transfered succesfully.", "kSuccess");
		HideDialog ("dialog");
		m_oStockTransferMemberData.m_strXMLData = oResponse.strStockTransferXML;
		navigate ('Print Stock Transfer Memo','widgets/inventorymanagement/stocktransfer/stockTransferMemoPrint.js');
		try
		{
			stockTransfer_handleAftersave ();
		}
		catch(oException){}
	}
	else
		informUser ("Stock Transfer failed", "kError");
}

function stockTransfer_setArticleDetails ()
{
	var editors = $('#stockTransfer_table_articles').datagrid('getEditors', m_oStockTransferMemberData.nEditIndex);
	var articleNoEditor = editors[0];
	var nameEditor = editors[1];
	var detailEditor = editors[2];
	var quantityEditor = editors[3];
	var bIsArticleDuplicate = stockTransfer_isArticleDuplicate (m_oStockTransferMemberData.m_arrArticleDetails[0], m_oStockTransferMemberData.nEditIndex);
	if(!bIsArticleDuplicate)
	{
		$(articleNoEditor.target).combobox('setValue',m_oStockTransferMemberData.m_arrArticleDetails[0]);
		$(nameEditor.target).combobox('setValue',m_oStockTransferMemberData.m_arrArticleDetails[1]);
		$(detailEditor.target).val(m_oStockTransferMemberData.m_arrArticleDetails[2]);
		$(quantityEditor.target).numberbox('enable');
		$(quantityEditor.target).numberbox('setValue','');
		$(quantityEditor.target).focus();
	}
	else
	{
		informUser("Duplicate Article in Invoice", "kWarning");
		$(articleNoEditor.target).combobox('setValue','');
		$(nameEditor.target).combobox('setValue','');
		$(detailEditor.target).val('');
		$(quantityEditor.target).numberbox('disable');
		$(quantityEditor.target).numberbox('setValue','');
		$(articleNoEditor.target).combobox('textbox').focus();
	}
}

function stockTransfer_isArticleDuplicate (articleNoEditor,rowIndex)
{
	assert.isString(articleNoEditor, "articleNoEditor expected to be a String.");	
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	var arrLineItemData = $('#stockTransfer_table_articles').datagrid('getData').rows;
	var bIsArticleDuplicate = false;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++)
	{
		if(articleNoEditor.toLowerCase() ==  arrLineItemData [nIndex].m_strArticleNumber.toLowerCase() && rowIndex != nIndex)
		{
			bIsArticleDuplicate = true;
			break;
		}
	}
	return bIsArticleDuplicate;
}