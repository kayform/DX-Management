package com.k4m.eXperdb.webconsole.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionManager extends DefaultTransactionDefinition{
	 /**
	 * 
	 */
	 private PlatformTransactionManager transactionManager; 
	 
	 private static final long serialVersionUID = 1L;
	 
	 public TransactionManager(PlatformTransactionManager transactionManager) {
		 this.transactionManager = transactionManager;
	 }
	 		 
	 private TransactionStatus status = null;
	 
	 public void begin() throws TransactionException {
		 status = transactionManager.getTransaction(this);
	 }
	 
	 public void commit() throws TransactionException {
		 transactionManager.commit(status);
	 }
	 
	 public void rollback() throws TransactionException {
		 transactionManager.rollback(status);
	 }
}
