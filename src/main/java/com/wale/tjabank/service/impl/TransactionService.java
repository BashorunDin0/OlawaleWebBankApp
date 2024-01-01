package com.wale.tjabank.service.impl;

import com.wale.tjabank.dto.TransactionDto;
import com.wale.tjabank.entity.Transaction;

public interface TransactionService {
   public void saveTransaction(TransactionDto transactionDto);
}
