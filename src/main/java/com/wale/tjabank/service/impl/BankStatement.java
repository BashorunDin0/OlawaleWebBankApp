package com.wale.tjabank.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wale.tjabank.dto.EmailDetails;
import com.wale.tjabank.entity.Transaction;
import com.wale.tjabank.entity.User;
import com.wale.tjabank.repository.TransactionRepository;
import com.wale.tjabank.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private EmailService emailService;

    private static final String FILE = "C:\\Users\\user\\Documents\\MyStatement.pdf";

/**
 * retrieve list of transactions within a date range given on account number
 * generate a pdf file of transactions
 * send the file via email
 */

public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
    LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
    LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
    List<Transaction> transactionList = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
            .filter(transaction -> transaction.getCreatedAt().isEqual(start)).filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();
    User user = userRepository.findByAccountNumber(accountNumber);
    String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
    Rectangle statementSize = new Rectangle(PageSize.A4);
    Document document = new Document(statementSize);
    log.info("setting size of document");
    OutputStream outputStream = new FileOutputStream(FILE);
    PdfWriter.getInstance(document, outputStream);
    document.open();

    PdfPTable bankInfoTable = new PdfPTable(1);
    PdfPCell bankName = new PdfPCell(new Phrase("The Olawale Bank"));
    bankName.setBorder(0);
    bankName.setBackgroundColor(BaseColor.BLUE);
    bankName.setPadding(20f);

    PdfPCell bankAddress = new PdfPCell( new Phrase("15, Okwisoko, Effurun, Delta State, Nigeria"));
    bankAddress.setBorder(0);
    bankInfoTable.addCell(bankName);
    bankInfoTable.addCell(bankAddress);

    PdfPTable statementInfo = new PdfPTable(2);
    PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
    customerInfo.setBorder(0);
    PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
    statement.setBorder(0);
    PdfPCell stopDate = new PdfPCell(new Phrase("End Date:" + endDate));
    stopDate.setBorder(0);
    PdfPCell name = new PdfPCell(new Phrase("Customer Name:" + customerName));
    name.setBorder(0);
    PdfPCell space = new PdfPCell();
    space.setBorder(0);
    PdfPCell address = new PdfPCell(new Phrase("Customer Address:" + user.getAddress()));
    address.setBorder(0);

    PdfPTable transactionTable = new PdfPTable(4);
    PdfPCell date = new PdfPCell(new Phrase("Date"));
    date.setBackgroundColor(BaseColor.BLUE);
    date.setBorder(0);
    PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
    transactionType.setBackgroundColor(BaseColor.BLUE);
    transactionType.setBorder(0);
    PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
    transactionAmount.setBackgroundColor(BaseColor.BLUE);
    transactionAmount.setBorder(0);
    PdfPCell status = new PdfPCell(new Phrase("STATUS"));
    status.setBackgroundColor(BaseColor.BLUE);
    status.setBorder(0);

    transactionTable.addCell(date);
    transactionTable.addCell(transactionType);
    transactionTable.addCell(transactionAmount);
    transactionTable.addCell(status);

    transactionList.forEach(transaction -> {
        transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
        transactionTable.addCell(new Phrase(transaction.getTransactionType()));
        transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
        transactionTable.addCell(new Phrase(transaction.getStatus()));

    });

    statementInfo.addCell(customerInfo);
    statementInfo.addCell(statement);
    statementInfo.addCell(endDate);
    statementInfo.addCell(name);
    statementInfo.addCell(space);
    statementInfo.addCell(address);

    document.add(bankInfoTable);
    document.add(statementInfo);
    document.add(transactionTable);

    document.close();

    EmailDetails emailDetails = EmailDetails.builder()
            .recipient(user.getEmail())
            .subject("STATEMENT OF ACCOUNT")
            .messageBody("kindly find your requested account statement")
            .attachment(FILE)
            .build();

    emailService.sendEmailWithAttachment(emailDetails);










    return transactionList;
}



}
