package com.revature.bank.model;

import java.io.Serializable;

public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3309683700008754768L;
	
	private int transactionID;
	private int accountID1;
	private int accountID2;
	private String transType;
	private double amount;
	
	public Transaction() {
		super();
	}

	public Transaction(int transactionID, int accountID1, int accountID2, String transType, double amount) {
		super();
		this.transactionID = transactionID;
		this.accountID1 = accountID1;
		this.accountID2 = accountID2;
		this.transType = transType;
		this.amount = amount;
	}
	
	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public int getAccountID1() {
		return accountID1;
	}

	public void setAccountID1(int accountID1) {
		this.accountID1 = accountID1;
	}

	public int getAccountID2() {
		return accountID2;
	}

	public void setAccountID2(int accountID2) {
		this.accountID2 = accountID2;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountID1;
		result = prime * result + accountID2;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((transType == null) ? 0 : transType.hashCode());
		result = prime * result + transactionID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (accountID1 != other.accountID1)
			return false;
		if (accountID2 != other.accountID2)
			return false;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (transType == null) {
			if (other.transType != null)
				return false;
		} else if (!transType.equals(other.transType))
			return false;
		if (transactionID != other.transactionID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transaction [transactionID=" + transactionID + ", accountID1=" + accountID1 + ", accountID2="
				+ accountID2 + ", transType=" + transType + ", amount=" + amount + "]";
	}

	

}
