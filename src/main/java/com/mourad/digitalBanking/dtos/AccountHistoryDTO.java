package com.mourad.digitalBanking.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int pageSize;
    private int totalPages;
    private int currentPage;
    private List<AccountOperationDTO> accountOperationDTOS;
}
