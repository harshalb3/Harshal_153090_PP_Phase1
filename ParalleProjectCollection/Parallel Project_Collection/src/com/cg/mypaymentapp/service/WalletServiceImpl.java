package com.cg.mypaymentapp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Transactions;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService 
{

	private WalletRepo repo;
	private Customer customer;
	private Wallet wallet;
	private Map<String, ArrayList<String>> data;
	
	public WalletServiceImpl() 
	{
		repo = new WalletRepoImpl();
		data=new HashMap<String,ArrayList<String>>();
	}
	public WalletServiceImpl(Map<String, Customer> data)
	{
		repo= new WalletRepoImpl(data);
	}
	public WalletServiceImpl(WalletRepo repo) 
	{
		super();
		this.repo = repo;
	}

	

	@Override
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) 
	{
		boolean check = false; 
		wallet = new Wallet(amount);
		customer = new Customer(name, mobileNo, wallet, new Transactions());
		if(isValid(customer))
		check = repo.save(customer);
		if (check) 
		{
			return customer;
		} else 
		{
			System.out.println("Data not saved.");
			return null;  
		}
		
	}

	@Override
	public Customer showBalance(String mobileNo) 
	{
		
		Customer customer=repo.findOne(mobileNo);
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no.");
	}

	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) throws InvalidInputException
	{
		if(sourceMobileNo==null||amount.compareTo(BigDecimal.ZERO)<=0||sourceMobileNo.trim().isEmpty()||targetMobileNo.trim().isEmpty()||targetMobileNo==null)
			throw new InvalidInputException("Inputs cannot be empty.");
		Customer cust=new Customer();
		Customer cust1=new Customer();
		BigDecimal bal1;
		BigDecimal bal2;
		
		cust=repo.findOne(sourceMobileNo);
		cust1=repo.findOne(targetMobileNo);
		
		
		if(cust!=null && cust1!=null)
		{
			bal1=cust.getWallet().getBalance();
			
			bal1=bal1.subtract(amount);
			
			Wallet wall1=new Wallet(bal1);
			
			cust.setWallet(wall1);
			
			cust.getTransaction().getTransaction().add(amount+" was transfered to "+targetMobileNo);
		
			bal2=cust1.getWallet().getBalance();
			
			bal2=bal2.add(amount);
			
			Wallet wall2=new Wallet(bal2);
			
			cust1.setWallet(wall2);

			cust1.getTransaction().getTransaction().add(amount+" was added to your account from "+sourceMobileNo);
			
			return cust;
		}
		
		else
		{
			System.out.println("Transfer Failed");
			return null;
		}
	}

	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) 
	{
		if(mobileNo==null||amount.compareTo(BigDecimal.ZERO)<=0||mobileNo.trim().isEmpty())
			throw new InvalidInputException("Inputs cannot be empty.");
		Customer cust=new Customer();
	
		cust=repo.findOne(mobileNo);
		
		BigDecimal bal1=null;

		if(cust!=null)
		{
			bal1=cust.getWallet().getBalance();
			
			bal1=bal1.add(amount);
			
			Wallet wall1=new Wallet(bal1);
			
			cust.setWallet(wall1);
			
			cust.getTransaction().getTransaction().add(amount+" depositied");
			return cust;
		}
		
		else {
			System.out.println("Customer not found with mobile number : "+mobileNo);
			return null;
		}
	
	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) 
	{
		if(mobileNo==null||amount.compareTo(BigDecimal.ZERO)<=0||mobileNo.trim().isEmpty())
			throw new InvalidInputException("Inputs cannot be empty.");
		Customer cust=new Customer();
		
		cust=repo.findOne(mobileNo);
		
		BigDecimal bal1=null;

		if(cust!=null)
		{
			bal1=cust.getWallet().getBalance();
			if(bal1.compareTo(BigDecimal.ZERO)<=0||bal1.subtract(amount).compareTo(BigDecimal.ZERO)<=0)
				throw new InsufficientBalanceException("Insufficient Balance");
			bal1=bal1.subtract(amount);
			
			Wallet wall1=new Wallet(bal1);
			
			cust.setWallet(wall1);
			
			cust.getTransaction().getTransaction().add(amount+" withdrawn");
			
			return cust;
		}
		
		else {
			System.out.println("Customer not found with mobile number : "+mobileNo);
			return null;
		}
		
	}
	
	@Override
	public List<String> showTransaction(String mobileNo) 
	{
	
		customer=repo.findOne(mobileNo);
		
		return customer.getTransaction().getTransaction();
		
		
	}
	
	
	
	public boolean isValid(Customer customer) throws InvalidInputException, InsufficientBalanceException
	{
		if(customer.getName() == null || customer.getName() == "")
		{
			throw new InvalidInputException("User Name cannot be null or empty.");
			
		}
		if(customer.getMobileNo() == null || customer.getMobileNo() == "")
			throw new InvalidInputException("User Mobile Number cannot be null or empty.");
		
		BigDecimal value = BigDecimal.ZERO;
		
		if(customer.getWallet().getBalance() == null ||customer.getWallet().getBalance().compareTo(value)==-1)
			throw new InvalidInputException("Wallet Balance cannot be Null.");
		
		if(!(customer.getName().matches("^([A-Z]{1}\\w+)$")))
		{
			throw new InvalidInputException("Invalid Name");
		}
		if(!(customer.getMobileNo().length()==10))
			throw new InvalidInputException("Mobile Number is not 10 digit.");
		
		if(!(customer.getMobileNo().matches("^[7-9]{1}[0-9]{9}$")))
		{
			throw new InvalidInputException("Invalid Number");
		}
		
		return true;
	}
	
	
//	public boolean isValid(Customer customer) throws InvalidInputException
//	{
//		Scanner sc=new Scanner(System.in);
//		if(customer.getName().matches("[A-Z][a-z]*")){
//			if(String.valueOf(customer.getMobileNo()).matches("[6-9][0-9]{9}") && customer.getMobileNo()!=null)
//			{
//			return true;
//			}
//			else
//			{
//			System.err.println("Invalid phone number");
//			System.out.println("Enter the phone number again");
//			customer.setMobileNo(sc.next());
//			return false;
//			}
//		}
//		else
//		{
//			System.err.println("Name should contain alphabets");
//			System.out.println("Enter correct name :");
//			customer.setName(sc.next());
//			return false;
//		}
//		}

}
