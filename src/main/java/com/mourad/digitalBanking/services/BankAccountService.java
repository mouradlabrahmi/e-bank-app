package com.mourad.digitalBanking.services;

import com.mourad.digitalBanking.dtos.*;
import com.mourad.digitalBanking.entities.BankAccount;
import com.mourad.digitalBanking.entities.CurrentAccount;
import com.mourad.digitalBanking.entities.Customer;
import com.mourad.digitalBanking.entities.SavingAccount;
import com.mourad.digitalBanking.exceptions.BalanceNotSufficentException;
import com.mourad.digitalBanking.exceptions.BankAccountNotFoundException;
import com.mourad.digitalBanking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();

//    CustomerDTO getCustomer(Long customerId);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    CurrentBankAccountDTO getCurrentBankAccountDTO(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;

    List<BankAccountDTO> bankAccountList();

    List<AccountOperationDTO> accountOperationDTOList(String accountId);
    AccountHistoryDTO accountHistoryOperations(String accountId, int page, int pageSize) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}

