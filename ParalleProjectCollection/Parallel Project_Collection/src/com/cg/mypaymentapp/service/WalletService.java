package com.cg.mypaymentapp.service;

import java.math.BigDecimal;
import java.util.List;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;

public interface WalletService
{
	public Customer createAccount(String name ,String mobileNo, BigDecimal amount);
	
	public Customer showBalance (String mobileNo);
	
	public Customer fundTransfer (String sourceMobileNo,String targetMobileNo, BigDecimal amount);
	
	public Customer depositAmount (String mobileNo,BigDecimal amount );
	
	public Customer withdrawAmount(String mobileNo, BigDecimal amount);
	
	public List<String> showTransaction(String mobileNo);
	
	public boolean isValid(Customer customer) throws InvalidInputException, InsufficientBalanceException;
}
