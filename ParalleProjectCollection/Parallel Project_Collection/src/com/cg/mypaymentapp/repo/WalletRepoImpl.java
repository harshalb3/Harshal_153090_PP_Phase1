package com.cg.mypaymentapp.repo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cg.mypaymentapp.DBUtil.DBUtil;
import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;

public class WalletRepoImpl implements WalletRepo 
{
	private Map<String, Customer> data; 
	private Map<String,ArrayList<String>> trans;
	
	
	public WalletRepoImpl()
	{
		data = new HashMap<String, Customer>();
	}

	public WalletRepoImpl(Map<String, Customer> data) 
	{
		super();
		this.data = data;
	}
	
	@Override
	public boolean save(Customer customer) 
	{
		
	data.put(customer.getMobileNo(), customer);
		return true;
	}
	

	@Override
	public Customer findOne(String mobileNo) 
	{
		
		Customer cust=null;
		
		if(data.containsKey(mobileNo))
		{
			return data.get(mobileNo);
		}
		
		else
		{
			System.out.println("Customer Not Found.");
			return null;
		}	
	
	}
	
	
	public Map<String, ArrayList<String>> getTrans() {
		return trans;
	}

	public void setTrans(Map<String, ArrayList<String>> trans) {
		this.trans = trans;
	}

	
	

	
	
}
