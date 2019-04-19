package com.techmust.inventory.location;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class LocationDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	 public ArrayList<LocationData> m_arrLocations ;
	 public long m_nRowCount;
	 public LocationDataResponse ()
	 {
		 m_arrLocations = new ArrayList<LocationData> ();
		 m_nRowCount = 0;
	 }
}
