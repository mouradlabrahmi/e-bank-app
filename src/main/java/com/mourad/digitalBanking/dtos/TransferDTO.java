package com.mourad.digitalBanking.dtos;

import lombok.Data;

@Data
public class TransferDTO {
    private String accountSource;
    private String accountDest;
    private double amount;

}
