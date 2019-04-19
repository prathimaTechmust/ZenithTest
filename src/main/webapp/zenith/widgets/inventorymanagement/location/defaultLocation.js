navigate ("defaultLocation", "widgets/inventorymanagement/location/location.js");

function location_loaded ()
{
	loadPage ("inventorymanagement/location/defaultLocation.html", "dialog", "location_setDefault()");
}

function defaultLocation_cancel ()
{
	HideDialog ("dialog");
}