package com.cg.mypaymentapp.repo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import com.cg.mypaymentapp.beans.Customer;

public interface WalletRepo 
{
    public boolean save(Customer customer);
	
	public Customer findOne(String mobileNo);
	
	public Map<String, ArrayList<String>> getTrans();
	
	public void setTrans(Map<String, ArrayList<String>> trans);
}
