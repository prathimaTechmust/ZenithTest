var purchase_includeDataObjects = [
		'widgets/inventorymanagement/purchase/PurchaseData.js',
		'widgets/vendorpurchaseorder/VendorPurchaseOrderData.js',
		'widgets/inventorymanagement/purchase/PurchaseLineItem.js',
		'widgets/clientmanagement/SiteData.js',
		'widgets/inventorymanagement/item/itemData.js',
		'widgets/inventorymanagement/item/ItemCategoryData.js',
		'widgets/inventorymanagement/tax/ApplicableTaxData.js',
		'widgets/usermanagement/userinfo/UserInformationData.js',
		'widgets/vendormanagement/VendorData.js',
		'widgets/vendormanagement/VendorDemographyData.js',
		'widgets/vendormanagement/VendorContactData.js',
		'widgets/vendormanagement/VendorBusinessTypeData.js',
		'widgets/vendorpurchaseorder/VendorPOLineItemData.js',
		'widgets/inventorymanagement/location/LocationData.js' ];


includeDataObjects(purchase_includeDataObjects, "purchase_loaded ()");

function purchase_memberData() {
	this.m_oVendorPurchaseOrderData = new VendorPurchaseOrderData();
	this.editIndex = undefined;
	this.m_arrArticle = new Array();
	this.m_arrArticleDetails = new Array();
	this.m_arrFromDetails = new Array();
	this.m_nPurchaseId = -1;
	this.m_bIsForPucchase = false;
}

var m_oPurchasememberData = new purchase_memberData();

function purchase_new() {
	createPopup("dialog", "#purchase_button_create", "#purchase_button_cancel",
			true);
	document.getElementById("purchase_select_locationName").style.visibility = "visible";
	document.getElementById("purchase_td_locationLabel").style.visibility = "visible";
	purchase_init();
	if (m_oPurchasememberData.m_bIsForPucchase) {
		document.getElementById("purchase_img_add").style.visibility = "visible";
		purchase_setValues();
	}
	purchase_appendRow();
}

function purchase_setValues() {
	var oVendorPurchaseOrderData = m_oPurchasememberData.m_oVendorPurchaseOrderData;
	purchase_populateFromCombobox(oVendorPurchaseOrderData);
	m_oPurchasememberData.m_strFromName = oVendorPurchaseOrderData.m_oVendorData.m_strCompanyName;
	$('#purchase_input_from').combobox('select',
			oVendorPurchaseOrderData.m_oVendorData.m_nClientId);
	document.getElementById("purchase_img_add").style.visibility = "hidden";
	$('#purchase_input_from').combobox('disable');
	$("#purchase_input_invoiceNo").val(oVendorPurchaseOrderData.m_strInvoiceNo);
	initFormValidateBoxes("purchase_form_id");
	var arrOrderedLineItem = getOrderedLineItems(oVendorPurchaseOrderData.m_arrPurchaseOrderLineItems);
	for (var nIndex = 0; nIndex < arrOrderedLineItem.length; nIndex++) {
		if (arrOrderedLineItem[nIndex].m_nReceiveQty > 0) {
			var oLineItem = new PurchaseLineItem();
			var oItemData = purchase_getItemData(arrOrderedLineItem[nIndex].m_oItemData)
			oLineItem.m_strArticleNo = oItemData.m_strArticleNumber;
			oLineItem.m_strName = oItemData.m_strItemName;
			oLineItem.m_strDetail = oItemData.m_strDetail;
			oLineItem.m_nQuantity = arrOrderedLineItem[nIndex].m_nReceiveQty;
			oLineItem.m_nPrice = arrOrderedLineItem[nIndex].m_nPrice;
			oLineItem.m_nDiscount = arrOrderedLineItem[nIndex].m_nDiscount;
			oLineItem.m_nTax = arrOrderedLineItem[nIndex].m_nTax;
			oLineItem.m_nExcise = 0;
			oLineItem.m_nOtherCharges = 0;
			oLineItem.m_nAmount = purchase_getAmount(oLineItem);
			$('#purchase_table_articles').datagrid('appendRow', oLineItem);
		}
	}
	purchase_loadFooterDG(true);
}

function purchase_getItemData(m_oItemData) {
	assert.isObject(m_oItemData, "m_oItemData expected to be an Object.");
	assert(Object.keys(m_oItemData).length > 0,
			"m_oItemData cannot be an empty .");// checks for non emptyness
	var oData = m_oItemData;
	var oItemData = new ItemData();
	oItemData.m_strArticleNumber = m_oItemData.m_strArticleNumber
	ItemDataProcessor.list(oItemData, "", "", 1, 10, purchase_gotlist);
	return oData;
}

function purchase_gotlist(oResponse) {
	oData = oResponse.m_arrItems[0];

}
function purchase_initUser() {
	purchase_new();
}

function purchase_initAdmin() {
	document.getElementById("purchase_img_add").style.visibility = "visible";
	purchase_new();
	document.getElementById("purchase_img_addItem").style.visibility = "visible";
}

function purchase_edit() {
	createPopup('ProcessDialog', '', '', true);
	var oPurchaseData = new PurchaseData();
	oPurchaseData.m_nId = m_oPurchasememberData.m_nPurchaseId;
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	createPopup("dialog", "#purchase_button_create", "#purchase_button_cancel",
			true);
	purchase_init();
	document.getElementById("purchase_img_add").style.visibility = "visible";
	document.getElementById("purchase_img_addItem").style.visibility = "visible";
	document.getElementById("purchase_button_create").setAttribute('update',
			true);
	document.getElementById("purchase_button_create").innerHTML = "Update";
	PurchaseDataProcessor.get(oPurchaseData, purchase_gotPurchaseData);
}

function purchase_gotPurchaseData(oResponse) {
	HideDialog("ProcessDialog");
	var arrPurchase = oResponse.m_arrPurchase[0];
	purchase_populateFromCombobox(arrPurchase);
	m_oPurchasememberData.m_strFromName = arrPurchase.m_strFrom;
	m_oPurchasememberData.m_strInvoiceNo = arrPurchase.m_strInvoiceNo;
	$('#purchase_input_from').combobox('select',
			arrPurchase.m_oVendorData.m_nClientId);
	$("#purchase_input_invoiceNo").val(arrPurchase.m_strInvoiceNo);
	// $('#purchase_input_date').datebox('setValue', arrPurchase.m_strDate);
	$('#purchase_input_date').datepicker('setDate', arrPurchase.m_strDate);
	initFormValidateBoxes("purchase_form_id");
	var arrOrderedLineItem = getOrderedLineItems(arrPurchase.m_oPurchaseLineItems);
	for (var nIndex = 0; nIndex < arrPurchase.m_oPurchaseLineItems.length; nIndex++) {
		arrOrderedLineItem[nIndex].m_strArticleNo = arrOrderedLineItem[nIndex].m_oItemData.m_strArticleNumber;
		arrOrderedLineItem[nIndex].m_strName = arrOrderedLineItem[nIndex].m_oItemData.m_strItemName;
		arrOrderedLineItem[nIndex].m_strDetail = arrOrderedLineItem[nIndex].m_oItemData.m_strDetail;
		arrOrderedLineItem[nIndex].m_nAmount = purchase_getAmount(arrOrderedLineItem[nIndex]);
		$('#purchase_table_articles').datagrid('appendRow',
				arrOrderedLineItem[nIndex]);
	}
	purchase_loadFooterDG(true);
	purchase_appendRow();
}

function purchase_populateFromCombobox(arrPurchase) {
	assert.isObject(arrPurchase, "arrPurchase expected to be an Object.");
	assert(Object.keys(arrPurchase).length > 0,
			"arrPurchase cannot be an empty .");// checks for non emptyness
	var oVendorData = new VendorData();
	oVendorData.m_strCompanyName = arrPurchase.m_oVendorData.m_strCompanyName;
	VendorDataProcessor
			.getVendorSuggesstions(
					oVendorData,
					"",
					"",
					function(oResponse) {
						var arrVendorData = new Array();
						for (var nIndex = 0; nIndex < oResponse.m_arrVendorData.length; nIndex++) {
							arrVendorData
									.push(oResponse.m_arrVendorData[nIndex]);
							arrVendorData[nIndex].m_strFrom = encodeURIComponent(oResponse.m_arrVendorData[nIndex].m_strCompanyName
									+ " "
									+ "|"
									+ " "
									+ oResponse.m_arrVendorData[nIndex].m_strTinNumber);
						}
						$('#purchase_input_from').combobox('loadData',
								arrVendorData);
					});

}

function purchase_getAmount(oPurchaseOrderLineItem) {
	assert.isObject(oPurchaseOrderLineItem,
			"oPurchaseOrderLineItem expected to be an Object.");
	assert(Object.keys(oPurchaseOrderLineItem).length > 0,
			"oPurchaseOrderLineItem cannot be an empty .");// checks for non
	// emptyness
	var nAmount = 0;
	nAmount = oPurchaseOrderLineItem.m_nQuantity
			* oPurchaseOrderLineItem.m_nPrice;
	nAmount -= nAmount * (oPurchaseOrderLineItem.m_nDiscount / 100);
	nAmount += nAmount * (oPurchaseOrderLineItem.m_nExcise / 100);
	nAmount += nAmount * (oPurchaseOrderLineItem.m_nTax / 100);
	nAmount += nAmount * (oPurchaseOrderLineItem.m_nOtherCharges / 100);
	return nAmount.toFixed(2);
}

function purchase_init() {
	initFormValidateBoxes("purchase_form_id");
	// $("#purchase_input_date" ).datebox({required:true});
	$("#purchase_input_date").datepicker({
		minDate : '-6y',
		maxDate : '+6m'
	});
	var dDate = new Date();
	var dCurrentDate = dDate.getMonth() + 1 + '/' + dDate.getDate() + '/'
			+ dDate.getFullYear();
	// $('#purchase_input_date').datebox('setValue', dCurrentDate);
	$('#purchase_input_date').datepicker('setDate', dCurrentDate);
	purchase_initFromCombobox();
	purchase_populateLocationList()
	purchase_initializeDataGrid();
}

function purchase_cancel() {
	HideDialog("dialog");
}

function purchase_initFromCombobox() {
	$('#purchase_input_from').combobox({
		valueField : 'm_nClientId',
		textField : 'm_strFrom',
		loader : getFilteredVendorData,
		mode : 'remote',
		selectOnNavigation : false,
		formatter : function(row) {
			var opts = $(this).combobox('options');
			return decodeURIComponent(row[opts.textField]);
		},
		onSelect : function(row) {
			m_oPurchasememberData.m_arrFromDetails = row;
			purchase_setVendorInfo(m_oPurchasememberData.m_arrFromDetails);
		},
//		onChange: function(row) 
//		{
//			$('#purchase_input_from').combobox('setValue','');
//			$('#purchase_input_from').combobox('textbox').focus();
//		}
	});
	var toTextBox = $('#purchase_input_from').combobox('textbox');
	toTextBox[0].placeholder = "Please enter your vendor name";
	toTextBox.focus();
	toTextBox.bind('keydown', function(oEvent) {
		purchase_comboboxKeyboardNavigation(oEvent);
	});
}

var getFilteredVendorData = function(param, success, error) {
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if (param.q != undefined) {
		var strQuery = param.q.trim();
		var oVendorData = new VendorData();
		oVendorData.m_strCompanyName = strQuery;
		VendorDataProcessor
				.getVendorSuggesstions(
						oVendorData,
						"",
						"",
						function(oResponse) {
							var arrVendorData = new Array();
							for (var nIndex = 0; nIndex < oResponse.m_arrVendorData.length; nIndex++) {
								arrVendorData
										.push(oResponse.m_arrVendorData[nIndex]);
								arrVendorData[nIndex].m_strFrom = encodeURIComponent(oResponse.m_arrVendorData[nIndex].m_strCompanyName
										+ " "
										+ "|"
										+ " "
										+ oResponse.m_arrVendorData[nIndex].m_strTinNumber);
							}
							success(arrVendorData);
						});
	} else
		success(new Array());
}

function purchase_comboboxKeyboardNavigation(oEvent) {
	assert.isObject(oEvent, "oEvent expected to be an Object.");
	assert(Object.keys(oEvent).length > 0, "oEvent cannot be an empty .");// checks
	// for
	// non
	// emptyness

	if (oEvent.keyCode == 13) {
		purchase_setVendorInfo(m_oPurchasememberData.m_arrFromDetails);
	}
}

function purchase_setVendorInfo(oVendorData) {
	assert.isObject(oVendorData, "oVendorData expected to be an Object.");
	assert(Object.keys(oVendorData).length > 0,
			"oVendorData cannot be an empty .");// checks for non emptyness
	$("#purchase_input_from").combobox('setText', oVendorData.m_strCompanyName);
	
	$("#purchase_label_address").text(oVendorData.m_strAddress);
	$("#purchase_label_city").text(
			oVendorData.m_strCity + "-" + oVendorData.m_strPinCode);
	$("#purchase_label_phoneNumber").text(oVendorData.m_strMobileNumber);
	$("#purchase_label_email").text(oVendorData.m_strEmail);
	$("#purchase_label_tinNo").text(oVendorData.m_strTinNumber);
	$("#purchase_label_vatNo").text(oVendorData.m_strVatNumber);
	$("#purchase_label_cstNo").text(oVendorData.m_strCSTNumber);
	$('#purchase_input_invoiceNo').focus();
}

function purchase_addItem() {
	navigate("newItem", "widgets/inventorymanagement/item/itemAdmin.js");
}

function purchase_initializeDataGrid() {
	$('#purchase_table_articles')
			.datagrid(
					{
						columns : [ [
								{
									field : 'm_strArticleNo',
									title : 'Article# <img title="Add" src="images/add.PNG" align="right" style="visibility:hidden;" id="purchase_img_addItem" onClick="purchase_addItem ()"/>',
									width : 120,
									editor : {
										type : 'combobox',
										options : {
											valueField : 'm_strArticleDetail',
											textField : 'm_strArticleNo',
											hasDownArrow : false,
											panelWidth : 400,
											loader : getFilteredItemData,
											mode : 'remote',
											selectOnNavigation : false,
											formatter : function(row) {
												var opts = $(this).combobox(
														'options');
												return decodeURIComponent(row[opts.valueField]);
											},
											onSelect : function(row) {
												var opts = $(this).combobox(
														'options');
												var strDecodeDetails = decodeURIComponent(row[opts.valueField]);
												m_oPurchasememberData.m_arrArticleDetails = strDecodeDetails
														.split(" | ");
												purchase_setArticleDetails();
											}
										}
									}
								},
								{
									field : 'm_strName',
									title : 'Name',
									width : 210,
									editor : {
										type : 'combobox',
										options : {
											valueField : 'm_strArticleDetail',
											textField : 'm_strName',
											hasDownArrow : false,
											panelWidth : 400,
											loader : getFilteredItemData,
											mode : 'remote',
											selectOnNavigation : false,
											formatter : function(row) {
												var opts = $(this).combobox(
														'options');
												return decodeURIComponent(row[opts.valueField]);
											},
											onSelect : function(row) {
												var opts = $(this).combobox(
														'options');
												var strDecodeDetails = decodeURIComponent(row[opts.valueField]);
												m_oPurchasememberData.m_arrArticleDetails = strDecodeDetails
														.split(" | ");
												purchase_setArticleDetails();
											},
										}
									}
								},
								{
									field : 'm_strDetail',
									title : 'Detail',
									width : 80,
									editor : {
										'type' : 'text',
										'options' : {
											disabled : true
										}
									}
								},
								{
									field : 'm_nQuantity',
									title : 'Qty',
									align : 'right',
									width : 60,
									editor : {
										'type' : 'numberbox',
										'options' : {
											precision : '2',
											disabled : true
										}
									}
								},
								{
									field : 'm_nPrice',
									title : 'Price',
									align : 'right',
									width : 80,
									editor : {
										'type' : 'text'
									}
								},
								{
									field : 'm_nDiscount',
									title : 'Disc(%)',
									width : 50,
									align : 'right',
									editor : {
										'type' : 'text'
									}
								},
								{
									field : 'm_nExcise',
									title : 'Excise(%)',
									width : 60,
									align : 'right',
									editor : {
										'type' : 'text'
									}
								},
								{
									field : 'm_nTax',
									title : 'Tax(%)',
									width : 45,
									align : 'right',
									editor : {
										'type' : 'text'
									}
								},
								{
									field : 'm_nOtherCharges',
									title : 'Other Chgs(%)',
									width : 85,
									align : 'right',
									editor : {
										'type' : 'text'
									}
								},
								{
									field : 'm_nAmount',
									title : 'Amount',
									width : 90,
									align : 'right',
									editor : {
										'type' : 'numberbox',
										'options' : {
											precision : '2',
											disabled : true
										}
									}
								},
								{
									field : 'Actions',
									title : '',
									align : 'center',
									formatter : function(value, row, index) {
										if (m_oPurchasememberData.m_bIsForPucchase)
											return purchase_removeAction();
										else if (index != 0)
											return purchase_displayImages(row,
													index);
										else
											return purchase_removeAction();
									}
								}, ] ]
					});
	$('#purchase_table_articles').datagrid({
		onClickRow : function(rowIndex, rowData) {
			purchase_editRowDG(rowData, rowIndex);
		}
	})
}

var getFilteredItemData = function(param, success, error) {
	assert.isObject(param, "param expected to be an Object.");
	assert.isFunction(success, "success expected to be a Function.");
	if (param.q != undefined) {
		var strQuery = param.q.trim();
		var oItemData = new ItemData();
		oItemData.m_strArticleNumber = strQuery;
		oItemData.m_strItemName = strQuery;
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		ItemDataProcessor
				.getArticleSuggesstions(
						oItemData,
						"",
						"",
						function(oItemDataResponse) {
							var arrItemData = new Array();
							for (var nIndex = 0; nIndex < oItemDataResponse.m_arrItems.length; nIndex++) {
								arrItemData
										.push(oItemDataResponse.m_arrItems[nIndex]);
								arrItemData[nIndex].m_strArticleDetail = encodeURIComponent(arrItemData[nIndex].m_strArticleNumber
										+ " | "
										+ arrItemData[nIndex].m_strItemName
										+ " | "
										+ arrItemData[nIndex].m_strDetail
										+ " | "
										+ arrItemData[nIndex].m_nSellingPrice
										+ " | "
										+ arrItemData[nIndex].m_oApplicableTax.m_oTaxes[0].m_nPercentage);
							}
							success(arrItemData);
						});
	} else
		success(new Array());
}

function purchase_setArticleDetails() {
	var editors = $('#purchase_table_articles').datagrid('getEditors',
			m_oPurchasememberData.editIndex);
	var articleNoEditor = editors[0];
	var nameEditor = editors[1];
	var detailEditor = editors[2];
	var quantityEditor = editors[3];
	var priceEditor = editors[4];
	var discountEditor = editors[5];
	var excisePercentEditor = editors[6];
	var taxPercentEditor = editors[7];
	var otherChargesEditor = editors[8];
	var amountEditor = editors[9];
	var bIsArticleDuplicate = purchase_isArticleDuplicate(
			m_oPurchasememberData.m_arrArticleDetails[0],
			m_oPurchasememberData.editIndex);
	$(quantityEditor.target).numberbox('setValue', '');
	$(priceEditor.target).val('');
	$(discountEditor.target).val(0);
	$(excisePercentEditor.target).val(0);
	$(taxPercentEditor.target).val(0);
	$(otherChargesEditor.target).val(0);
	$(amountEditor.target).numberbox('setValue', '');
	if (!bIsArticleDuplicate) {
		$(articleNoEditor.target).combobox('setValue',
				m_oPurchasememberData.m_arrArticleDetails[0]);
		$(nameEditor.target).combobox('setValue',
				m_oPurchasememberData.m_arrArticleDetails[1]);
		$(detailEditor.target)
				.val(m_oPurchasememberData.m_arrArticleDetails[2]);
		$(quantityEditor.target).numberbox('enable');
		$(quantityEditor.target).focus();
	} else {
		informUser("Duplicate Article in Invoice", "kWarning");
		$(articleNoEditor.target).combobox('setValue', '');
		$(nameEditor.target).combobox('setValue', '');
		$(detailEditor.target).val('');
		$(quantityEditor.target).numberbox('disable');
		$(articleNoEditor.target).combobox('textbox').focus();
	}
}

function purchase_editRowDG(rowData, rowIndex) {
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	if (m_oPurchasememberData.editIndex != rowIndex) {
		if (purchase_endEdit()) {
			$('#purchase_table_articles').datagrid('selectRow', rowIndex)
					.datagrid('beginEdit', rowIndex);
			m_oPurchasememberData.editIndex = rowIndex;
			purchase_setEditing(m_oPurchasememberData.editIndex, true);
		} else
			$('#purchase_table_articles').datagrid('selectRow',
					m_oPurchasememberData.editIndex);
	}
}

function purchase_displayImages(oRow, nIndex) {
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	var oImage = '<table align="center">'
			+ '<tr>'
			+ '<td> <img title="Delete" src="images/delete.png" width="20"  id="deleteImageId" onClick="purchase_delete('
			+ nIndex + ')"/> </td>' + '</tr>' + '</table>'
	return oImage;
}

function purchase_removeAction(oRow, nIndex) {
	var oImage = '<table align="center">'
			+ '<tr>'
			+ '<td style="border-style:none">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>'
			+ '</tr>' + '</table>'
	return oImage;
}

function purchase_delete(nIndex) {
	assert.isNumber(nIndex, "nIndex expected to be a Number.");
	assert.isOk(nIndex > -1, "nIndex must be a positive value.");
	$('#purchase_table_articles').datagrid('deleteRow', nIndex);
	purchase_loadFooterDG(true);
	refreshDataGrid('#purchaseOrder_table_itemsDG');
	if ($('#purchaseOrder_table_itemsDG').datagrid('getRows').length == 0)
		purchaseOrder_appendRow();
}

function purchase_endEdit() {
	if (m_oPurchasememberData.editIndex == undefined) {
		return true
	}
	if ($('#purchase_table_articles').datagrid('validateRow',
			m_oPurchasememberData.editIndex)) {
		$('#purchase_table_articles').datagrid('endEdit',
				m_oPurchasememberData.editIndex);
		m_oPurchasememberData.editIndex = undefined;
		return true;
	} else {
		return false;
	}
}

function purchase_submit() {
	if (purchase_validate() && purchase_validateDGRow()
			&& purchase_validateArticleDetails())
		loadPage("include/process.html", "ProcessDialog",
				"purchase_submit_saveAndPrint ()");
}

function purchase_submit_saveAndPrint() {
	createPopup('ProcessDialog', '', '', true);
	disable("purchase_button_create");
	var oPurchaseData = purchase_getFormData();
	if (m_oPurchasememberData.m_bIsForPucchase)
		PurchaseDataProcessor.enterInvoice(oPurchaseData,
				m_oVendorPurchaseOrderMemberData.m_oVendorPurchaseOrderData,
				purchase_created);
	else if (document.getElementById("purchase_button_create").getAttribute(
			'update') == "false") {
		var oLocationData = new LocationData();
		oLocationData.m_nLocationId = document
				.getElementById('purchase_select_locationName').value;
		PurchaseDataProcessor.createWithLocation(oPurchaseData, oLocationData,
				purchase_created);
	} else {
		oPurchaseData.m_nId = m_oPurchasememberData.m_nPurchaseId;
		PurchaseDataProcessor.updatePurchase(oPurchaseData, purchase_updated);
	}
}
function purchase_validateArticleDetails() {
	var bIsValidArticle = false;
	var oPurchaseData = new PurchaseData();
	oPurchaseData.m_arrPurchaseLineItem = purchase_getLineItemDataArray();
	if (oPurchaseData.m_arrPurchaseLineItem.length > 0) {
		bIsValidArticle = true;
	} else
		informUser(" Article Details cannot be empty ", "kWarning");
	return bIsValidArticle;
}

function purchase_updated(oResponse) {
	HideDialog("ProcessDialog");
	if (oResponse.m_bSuccess) {
		informUser("Purchase updated successfully", "kSuccess");
		document.getElementById("purchaseList_div_listDetail").innerHTML = "";
		clearGridData("#purchaseList_table_purchaseListDG");
		navigate("", "widgets/inventorymanagement/purchase/purchaseList.js")
	} else
		enable("purchase_button_create");
	HideDialog("dialog");
}

function purchase_accept() {
	if (purchase_endEdit())
		$('#purchase_table_articles').datagrid('acceptChanges');
}

function purchase_created(oResponse) {
	HideDialog("ProcessDialog");
	if (oResponse.m_bSuccess) {
		informUser("Purchase Created Succesfully", "kSuccess");
		HideDialog("dialog");
		navigate('purchaselist',
				'widgets/inventorymanagement/purchase/purchaseList.js');
	} else {
		informUser(oResponse.m_strError_Desc, "kError");
		enable("purchase_button_create");
	}
}

function purchase_getFormData() {
	purchase_accept();
	var oPurchaseData = new PurchaseData();
	oPurchaseData.m_strFrom = $('#purchase_input_from').combobox('getText');
	oPurchaseData.m_strInvoiceNo = $("#purchase_input_invoiceNo").val();
	// var m_strDate = $('#purchase_input_date').datebox('getValue');
	var m_strDate = $('#purchase_input_date').val();
	oPurchaseData.m_strDate = FormatDate(m_strDate);
	oPurchaseData.m_arrPurchaseLineItem = purchase_getLineItemDataArray();
	oPurchaseData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oVendorData.m_nClientId = $('#purchase_input_from')
			.combobox('getValue');
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	// oPurchaseData.m_oVendorPurchaseOrderData = null;
	return oPurchaseData;
}

function purchase_getLineItemDataArray() {
	var oLineItemDataArray = new Array();
	var arrLineItemData = $('#purchase_table_articles').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++) {
		var oPurchaseLineItem = new PurchaseLineItem();
		oPurchaseLineItem.m_nSerialNumber = $('#purchase_table_articles')
				.datagrid('getRowIndex', arrLineItemData[nIndex]);
		oPurchaseLineItem.m_strArticleNo = arrLineItemData[nIndex].m_strArticleNo;
		oPurchaseLineItem.m_nQuantity = arrLineItemData[nIndex].m_nQuantity;
		oPurchaseLineItem.m_nPrice = arrLineItemData[nIndex].m_nPrice;
		oPurchaseLineItem.m_nDiscount = arrLineItemData[nIndex].m_nDiscount;
		oPurchaseLineItem.m_nExcise = arrLineItemData[nIndex].m_nExcise;
		oPurchaseLineItem.m_nTax = arrLineItemData[nIndex].m_nTax;
		oPurchaseLineItem.m_nOtherCharges = arrLineItemData[nIndex].m_nOtherCharges;
		if (oPurchaseLineItem.m_strArticleNo.trim() != "")
			oLineItemDataArray.push(oPurchaseLineItem);
	}
	return oLineItemDataArray;
}

function purchase_validate() {
	return validateForm("purchase_form_id")&& purchase_validateSelectField ();
}

function purchase_validateSelectField ()
{
	var bIsSelectFieldValid = true;
	if(!purchase_isValidClient())
	{
		informUser("Please Select Vendor Name", "kWarning");
		$('#purchase_input_from').combobox('textbox').focus ();
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function purchase_isValidClient()
{
	var bIsValid = false;
	try
	{
		var strClient = $('#purchase_input_from').combobox('getValue');
		if(strClient != null && strClient > 0)
			bIsValid = true;
	}
	catch(oException)
	{
		
	}
	return bIsValid;
}

function purchase_validateDGRow() {
	var bIsValidateDGRow = true;
	purchase_accept();
	var arrLineItemData = $('#purchase_table_articles').datagrid('getData').rows;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++) {
		var strArticleNumber = arrLineItemData[nIndex].m_strArticleNo;
		var nQuantity = arrLineItemData[nIndex].m_nQuantity;
		var nPrice = arrLineItemData[nIndex].m_nPrice;
		if (strArticleNumber.trim() != "" && (nQuantity == "" || nPrice == "")) {
			bIsValidateDGRow = false;
			informUser(nQuantity == "" ? "Quantity cannot be empty."
					: "Price cannot be empty.", "kWarning");
			break;
		}
	}
	return bIsValidateDGRow;
}

function purchase_appendRow() {
	if (purchase_endEdit()) {
		purchase_acceptDGchanges();
		$('#purchase_table_articles').datagrid('appendRow', {
			m_strArticleNo : ''
		});
		m_oPurchasememberData.editIndex = $('#purchase_table_articles')
				.datagrid('getRows').length - 1;
		$('#purchase_table_articles').datagrid('selectRow',
				m_oPurchasememberData.editIndex).datagrid('beginEdit',
				m_oPurchasememberData.editIndex);
		$('#m_strArticleNo').validatebox();
		purchase_setEditing(m_oPurchasememberData.editIndex, false);
	}
}

function purchase_setEditing(rowIndex, bIsEnabled) {
	assert.isBoolean(bIsEnabled, "bIsEnabled should be a boolean value");
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	var editors = $('#purchase_table_articles')
			.datagrid('getEditors', rowIndex);
	var articleNoEditor = editors[0];
	var nameEditor = editors[1];
	var quantityEditor = editors[3];
	var priceEditor = editors[4];
	var discountEditor = editors[5];
	var excisePercentEditor = editors[6];
	var taxPercentEditor = editors[7];
	var otherChargesEditor = editors[8];
	var amountEditor = editors[9];
	var articleNoTextbox = $(articleNoEditor.target).combobox('textbox');
	articleNoTextbox.focus();
	var nameTextbox = $(nameEditor.target).combobox('textbox');
	articleNoTextbox.bind('keydown', function(e) {
		purchase_keyboardNavigation(e)
	});
	articleNoTextbox.bind('change', function (e){
		purchase_articleCheck ()
	});
	
	nameTextbox.bind('keydown', function(e) {
		purchase_keyboardNavigation(e)
	});
	nameTextbox.bind('change', function (e){
	   purchase_nameCheck ()
	});
	if (bIsEnabled) {
		$(quantityEditor.target).numberbox('enable');
	}
	quantityEditor.target.bind('change', function() {
		purchase_calculateAmount(rowIndex, false);
	});
	quantityEditor.target.bind('blur', function() {
		if (Number(this.value) == 0)
			this.focus();
	});
	priceEditor.target.bind('change', function() {
		purchase_calculateAmount(rowIndex, false);
	});
	priceEditor.target.bind('blur', function() {
		if (Number(this.value) == 0)
			this.focus();
	});
	priceEditor.target.bind('keyup', function() {
		validateFloatNumber(this);
	});
	discountEditor.target.bind('keyup', function() {
		validateFloatNumber(this);
		validatePercentage(this);
	});
	discountEditor.target.bind('blur', function() {
		priceEditor.target.unbind('blur');
		purchase_calculateAmount(rowIndex, false);
	});
	discountEditor.target.bind('change', function() {
		purchase_calculateAmount(rowIndex, false);
	});
	excisePercentEditor.target.bind('keyup', function() {
		validateFloatNumber(this);
		validatePercentage(this);
	});
	excisePercentEditor.target.bind('blur', function() {
		priceEditor.target.unbind('blur');
		purchase_calculateAmount(rowIndex, false);
	});
	excisePercentEditor.target.bind('change', function() {
		purchase_calculateAmount(rowIndex, false);
	});
	taxPercentEditor.target.bind('keyup', function() {
		validateFloatNumber(this);
		validatePercentage(this);
	});
	taxPercentEditor.target.bind('blur', function() {
		priceEditor.target.unbind('blur');
		purchase_calculateAmount(rowIndex, false);
	});
	taxPercentEditor.target.bind('change', function() {
		purchase_calculateAmount(rowIndex, false);
	});
	otherChargesEditor.target.bind('keyup', function() {
		validateFloatNumber(this);
		validatePercentage(this);
	});
	otherChargesEditor.target.bind('blur', function() {
		priceEditor.target.unbind('blur');
		purchase_calculateAmount(rowIndex, true);
	});
	otherChargesEditor.target.bind('change', function() {
		purchase_calculateAmount(rowIndex, true);
	});

	function purchase_keyboardNavigation(e) {
		assert.isObject(e, "e expected to be an Object.");
		if (e.keyCode == 13) {
			purchase_setArticleDetails();
		}
	}

	function purchase_calculateAmount(rowIndex, bAppendRow) {
		assert.isBoolean(bAppendRow, "bAppendRow should be a boolean value");
		assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
		var nAmount = 0;
		nAmount = quantityEditor.target.val() * priceEditor.target.val();
		nAmount -= nAmount * (discountEditor.target.val() / 100);
		nAmount += nAmount * (excisePercentEditor.target.val() / 100);
		nAmount += nAmount * (taxPercentEditor.target.val() / 100);
		nAmount += nAmount * (otherChargesEditor.target.val() / 100);
		if (nAmount >= 0) {
			$(amountEditor.target).numberbox('setValue', nAmount);
			purchase_loadFooterDG(bAppendRow);
			if (bAppendRow
					&& rowIndex == $('#purchase_table_articles').datagrid(
							'getRows').length - 1)
				purchase_appendRow();
		}
	}
}

function purchase_articleCheck ()
{
	var editors = $('#purchase_table_articles').datagrid('getEditors',m_oPurchasememberData.editIndex);
	var articleNoEditor = editors[0];
	$(articleNoEditor.target).combobox('setValue','');
	$(articleNoEditor.target).combobox('textbox').focus();
}

function purchase_nameCheck()
{
	var editors = $('#purchase_table_articles').datagrid('getEditors', m_oPurchasememberData.editIndex);
	var nameEditor = editors[1];
	$(nameEditor.target).combobox('setValue','');
	$(nameEditor.target).combobox('textbox').focus();
}

function purchase_acceptDGchanges() {
	if (purchase_endEdit()) {
		$('#purchase_table_articles').datagrid('acceptChanges');
	}
}

function purchase_validatePurchaseDetails() {
	var strFromName = $("#purchase_input_from").val();
	var strInvoiceNo = $("#purchase_input_invoiceNo").val();
	if (strFromName != m_oPurchasememberData.m_strFromName
			&& strInvoiceNo != m_oPurchasememberData.m_strInvoiceNo) {
		purchase_getPurchaseList(strFromName, strInvoiceNo);
	}
}

function purchase_getPurchaseList(strFromName, strInvoiceNo) {
	assert.isString(strFromName, "strFromName expected to be a string.");
	assert.isString(strInvoiceNo, "strInvoiceNo expected to be a string.");
	if (strFromName != "" && strInvoiceNo != "") {
		var oPurchaseData = new PurchaseData();
		oPurchaseData.m_strFromName = strFromName;
		oPurchaseData.m_strInvoiceNo = strInvoiceNo;
		oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		PurchaseDataProcessor.list(oPurchaseData, "", "", purchase_listed);
	}
}

function purchase_listed(oResponse) {
	if (oResponse.m_arrPurchase.length > 0) {
		informUser("usermessage_purchase_purchasealreadyexists", "kWarning");
		$("#purchase_input_invoiceNo", "").val();
		document.getElementById("purchase_input_invoiceNo").focus();
	}
}

function purchase_loadFooterDG(bAccept) {
	assert.isBoolean(bAccept, "bAccept should be a boolean value");
	if (bAccept) {
		purchase_acceptDGchanges();
		var arrLineItemData = $('#purchase_table_articles').datagrid('getData').rows;
		var nTotal = 0;
		for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++) {
			if (parseFloat(arrLineItemData[nIndex].m_nAmount) > 0)
				nTotal = parseFloat(nTotal)
						+ parseFloat(arrLineItemData[nIndex].m_nAmount);
		}
		$('#purchase_table_articles').datagrid('reloadFooter', [ {
			m_nOtherCharges : '<b>Total</b>',
			m_nAmount : nTotal.toFixed(2)
		} ]);
	}
}

function purchase_isArticleDuplicate(articleNoEditor, rowIndex) {
	assert
			.isString(articleNoEditor,
					"articleNoEditor expected to be a String.");
	assert.isNumber(rowIndex, "rowIndex expected to be a Number.");
	var arrLineItemData = $('#purchase_table_articles').datagrid('getData').rows;
	var bIsArticleDuplicate = false;
	for (var nIndex = 0; nIndex < arrLineItemData.length; nIndex++) {
		if (articleNoEditor.toLowerCase() == arrLineItemData[nIndex].m_strArticleNo
				.toLowerCase()
				&& rowIndex != nIndex) {
			bIsArticleDuplicate = true;
			break;
		}
	}
	return bIsArticleDuplicate;
}

function purchase_addVendor() {
	navigate("VendorInfo", "widgets/vendormanagement/addVendor.js");
}

function vendorInfo_handleAfterSave() {
	// Handler Function for New Vendor Save.
}

function purchase_populateLocationList() {
	var oLocationData = new LocationData();
	LocationDataProcessor.list(oLocationData, "m_strName", "asc", "", "",
			function(oResponse) {
				purchase_prepareLocationNameDD("purchase_select_locationName",
						oResponse);
			});
}

function purchase_prepareLocationNameDD(strLocationNameDD, oResponse) {
	var arrOptions = new Array();
	var nDefaultLocation = -1;
	for (var nIndex = 0; nIndex < oResponse.m_arrLocations.length; nIndex++) {
		arrOptions.push(CreateOption(
				oResponse.m_arrLocations[nIndex].m_nLocationId,
				oResponse.m_arrLocations[nIndex].m_strName));
		if (oResponse.m_arrLocations[nIndex].m_bIsDefault)
			nDefaultLocation = oResponse.m_arrLocations[nIndex].m_nLocationId;
	}
	PopulateDD(strLocationNameDD, arrOptions);
	document.getElementById('purchase_select_locationName').value = nDefaultLocation;
}
	
