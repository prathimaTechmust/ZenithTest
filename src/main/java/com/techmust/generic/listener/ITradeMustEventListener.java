package com.techmust.generic.listener;

public interface ITradeMustEventListener <T>
{
	public final String kNew = "New";
	public void handle (String strEventName, T t);
	public void register (T t);
}
