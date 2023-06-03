package com.mourad.digitalBanking.dtos;

import com.mourad.digitalBanking.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingBankAccountDTO extends BankAccountDTO{
//    private String id;
//    private double balance;
//    private Date createdAt;
//    private AccountStatus status;
//    private CustomerDTO customerDTO;
    private double interestRate;
}
