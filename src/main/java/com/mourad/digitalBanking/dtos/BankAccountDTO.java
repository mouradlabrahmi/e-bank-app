package com.mourad.digitalBanking.dtos;

import com.mourad.digitalBanking.entities.AccountOperation;
import com.mourad.digitalBanking.entities.Customer;
import com.mourad.digitalBanking.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
public abstract class BankAccountDTO {

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;

    private CustomerDTO customerDTO;
    private String type;

}