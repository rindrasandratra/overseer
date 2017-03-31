package com.capgemini.overseer.interfaces;

import java.util.Enumeration;

public interface IMapper {
	public void loadPropertyFile();
	public Enumeration<Object> fetchKeys();
}
