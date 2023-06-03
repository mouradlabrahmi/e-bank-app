package com.mourad.digitalBanking.web;

import com.mourad.digitalBanking.dtos.*;
import com.mourad.digitalBanking.entities.BankAccount;
import com.mourad.digitalBanking.exceptions.BalanceNotSufficentException;
import com.mourad.digitalBanking.exceptions.BankAccountNotFoundException;
import com.mourad.digitalBanking.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestControler {
    private BankAccountService bankAccountService;
    @GetMapping("/accounts")
    public List<BankAccountDTO> bankAccounts(){
        return bankAccountService.bankAccountList();
    }
//    @GetMapping("/accounts/{currentAccountId}")
//    public CurrentBankAccountDTO currentBankAccountDTO(@PathVariable String currentAccountId) throws BankAccountNotFoundException {
//        return bankAccountService.getCurrentBankAccountDTO(currentAccountId);
//    }
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO BankAccountDTO(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }
//    @PostMapping("/accounts")
//    public CurrentBankAccountDTO saveCurrentAccount(@RequestBody CurrentBankAccountDTO currentBankAccountDTO){
//        return bankAccountService.saveCurrentBankAccount(currentBankAccountDTO);
//    }
    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDTO> getBankAccountOperations(@PathVariable String id){
        return bankAccountService.accountOperationDTOList(id);
    }
//    @PostMapping("/accounts")
//    public BankAccountDTO saveBankAccountDTO(BankAccountDTO bankAccountDTO){
//        if(bankAccountDTO.getType()=="SA"){
//            return bankAccountService.saveSavingBankAccount(bankAccountDTO);
//        }
//        return bankAccountService.save
//    }
    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDTO getBankHistory(
            @PathVariable String id,
            @RequestParam (name="page",defaultValue = "0") int page,
            @RequestParam (name="size",defaultValue = "5") int pageSize
    ) throws BankAccountNotFoundException {
        return bankAccountService.accountHistoryOperations(id, page, pageSize);
    }
    @PostMapping("/accounts/debit")
    public DebitDTO debitDTO(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficentException {
        bankAccountService.debit(
                debitDTO.getAccountId(),
                debitDTO.getAmount(),
                debitDTO.getDescription()
        );
        return debitDTO;
    }
    @PostMapping("/accounts/credit")
    public CreditDTO creditDTO(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        bankAccountService.credit(
                creditDTO.getAccountId(),
                creditDTO.getAmount(),
                creditDTO.getDescription()
        );
        return creditDTO;
    }
    @PostMapping("/accounts/transfer")
    public TransferDTO transferDTO(@RequestBody TransferDTO transferDTO) throws BankAccountNotFoundException, BalanceNotSufficentException {
        bankAccountService.transfer(
                transferDTO.getAccountSource(),
                transferDTO.getAccountDest(),
                transferDTO.getAmount()
        );
        return transferDTO;
    }
}
