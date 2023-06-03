package com.mourad.digitalBanking.services;

import com.mourad.digitalBanking.dtos.*;
import com.mourad.digitalBanking.entities.*;
import com.mourad.digitalBanking.enums.OperationType;
import com.mourad.digitalBanking.exceptions.BalanceNotSufficentException;
import com.mourad.digitalBanking.exceptions.BankAccountNotFoundException;
import com.mourad.digitalBanking.exceptions.CustomerNotFoundException;
import com.mourad.digitalBanking.mappers.CurrentBankAccountMapper;
import com.mourad.digitalBanking.mappers.CustomerMapper;
import com.mourad.digitalBanking.repositories.AccountOperationRepository;
import com.mourad.digitalBanking.repositories.BankAccountRepository;
import com.mourad.digitalBanking.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional @AllArgsConstructor
@Slf4j
public class BankAccountServiceImpt implements BankAccountService{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
//    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private CustomerMapper customerMapper;
    private CurrentBankAccountMapper currentBankAccountMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = customerMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.fromCustomer(savedCustomer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO){
        Customer customer = customerMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return currentBankAccountMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return currentBankAccountMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer:customers){
            customerDTOS.add(customerMapper.fromCustomer(customer));
        }
        return customerDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
//        CustomerDTO customerDTO = new CustomerDTO();
        CustomerDTO customerDTO = customerMapper.fromCustomer(customer);
        return customerDTO;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Account not found"));
        if(bankAccount instanceof CurrentAccount){
            return currentBankAccountMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        }
        else return currentBankAccountMapper.fromSavingBankAccount((SavingAccount)bankAccount);
    }
    @Override
    public CurrentBankAccountDTO getCurrentBankAccountDTO(String accountId) throws BankAccountNotFoundException {
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
        BankAccount currentAccount = bankAccountRepository.findById(accountId).
                orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));;
        return currentBankAccountMapper.fromCurrentBankAccount((CurrentAccount) currentAccount);
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Account not found"));
        if(bankAccount.getBalance()<amount){
            throw new BalanceNotSufficentException("Solde insuffisant");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Account not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource, amount, "Transfer to"+accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from"+accountIdSource);

    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof CurrentAccount) {
                return currentBankAccountMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            } else return currentBankAccountMapper.fromSavingBankAccount((SavingAccount) bankAccount);
        }).collect(Collectors.toList());
    }
    @Override
    public List<AccountOperationDTO> accountOperationDTOList(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(operation-> currentBankAccountMapper.fromAccountOperation(operation))
                .collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO accountHistoryOperations(String accountId, int page, int pageSize) throws BankAccountNotFoundException {
        Page<AccountOperation> accountOperations = accountOperationRepository
                .findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, pageSize));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                () -> new BankAccountNotFoundException("bank account not found")
        );
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setAccountOperationDTOS(accountOperations.getContent().stream()
                        .map(op->currentBankAccountMapper.fromAccountOperation(op))
                .collect(Collectors.toList()));
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(pageSize);
        return  accountHistoryDTO;

    }
    @Override
    public List<CustomerDTO> searchCustomers(String keyword){
        List<Customer> customers = customerRepository.findByNameContains(keyword);
        return customers.stream().map(
                c -> customerMapper.fromCustomer(c))
                .collect(Collectors.toList());
    }
}
