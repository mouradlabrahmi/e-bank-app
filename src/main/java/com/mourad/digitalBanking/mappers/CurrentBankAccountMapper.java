package com.mourad.digitalBanking.mappers;

import com.mourad.digitalBanking.dtos.AccountOperationDTO;
import com.mourad.digitalBanking.dtos.CurrentBankAccountDTO;
import com.mourad.digitalBanking.dtos.DebitDTO;
import com.mourad.digitalBanking.dtos.SavingBankAccountDTO;
import com.mourad.digitalBanking.entities.AccountOperation;
import com.mourad.digitalBanking.entities.BankAccount;
import com.mourad.digitalBanking.entities.CurrentAccount;
import com.mourad.digitalBanking.entities.SavingAccount;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.mourad.digitalBanking.mappers.CustomerMapper;

import java.util.Optional;

@Service @AllArgsConstructor
public class CurrentBankAccountMapper {
    private CustomerMapper customerMapper;
    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
        currentAccount.setCustomer(customerMapper.fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentBankAccount){
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentBankAccount, currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(customerMapper.fromCustomer(currentBankAccount.getCustomer()));
        currentBankAccountDTO.setType(currentBankAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);
        savingAccount.setCustomer(customerMapper.fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }
    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingBankAccount){
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingBankAccount, savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(customerMapper.fromCustomer(savingBankAccount.getCustomer()));
        savingBankAccountDTO.setType(savingBankAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

}
