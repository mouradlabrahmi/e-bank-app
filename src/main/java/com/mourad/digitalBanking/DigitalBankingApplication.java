package com.mourad.digitalBanking;

import com.mourad.digitalBanking.entities.AccountOperation;
import com.mourad.digitalBanking.entities.CurrentAccount;
import com.mourad.digitalBanking.entities.Customer;
import com.mourad.digitalBanking.entities.SavingAccount;
import com.mourad.digitalBanking.enums.AccountStatus;
import com.mourad.digitalBanking.enums.OperationType;
import com.mourad.digitalBanking.repositories.AccountOperationRepository;
import com.mourad.digitalBanking.repositories.BankAccountRepository;
import com.mourad.digitalBanking.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class DigitalBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingApplication.class, args);
	}

//	@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			Stream.of("Hassan", "Mourad", "Aicha").forEach(name->{
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount =new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
//				currentAccount.setId(Math.random()*1000);
				currentAccount.setBalance(Math.random()*1000);
				currentAccount.setOverDraft(10);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.ACTIVATED);
				currentAccount.setCustomer(customer);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount =new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*1000);
				savingAccount.setInterestRate(5.5);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.ACTIVATED);
				savingAccount.setCustomer(customer);
				bankAccountRepository.save(savingAccount);
			});
			bankAccountRepository.findAll().forEach(acc->{
				for (int i = 0; i<10 ; i++){
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
					accountOperation.setAmount(Math.random()*70000);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}
}
