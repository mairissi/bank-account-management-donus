package br.com.bank.account.management.api.donus.controller;

import br.com.bank.account.management.api.donus.common.exception.CustomErrorResponse;
import br.com.bank.account.management.api.donus.controller.dto.*;
import br.com.bank.account.management.api.donus.model.Customer;
import br.com.bank.account.management.api.donus.service.account.AccountService;
import br.com.bank.account.management.api.donus.service.customer.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Tag(name = "Accounts operation", description = "Everything about account including functions such as deposit, withdraw and transfers")
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found accounts",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not found any accounts",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountResponse>> findAll(){
        log.info("Start find all accounts");
        var response = accountService.findAll()
                .stream()
                .map(acc -> modelMapper.map(acc, AccountResponse.class))
                .collect(Collectors.toList());
        if (response.isEmpty()) {throw new NotFoundException("Did not found any accounts");}
        log.info("Finish find all accounts");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get an account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> findById(
            @Parameter(description = " Account's ID") @PathVariable Long id){
        log.info("Start find account by id");
        var response = accountService.findById(id)
                .map(acc -> modelMapper.map(acc, AccountResponse.class))
                .orElseThrow(() -> new NotFoundException("Account not found for id: " + id));
        log.info("Finish find account by id");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get an account by account number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Account not found. Invalid account number",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(value = "/accountNumber/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> findAccountByNumber(
            @Parameter(description = " Number account") @PathVariable String number){
        log.info("Start find account by account number");
        var response = accountService.findByAccountNumber(number)
                .map(acc -> modelMapper.map(acc, AccountResponse.class))
                .orElseThrow(() -> new NotFoundException("Account not found for accountNumber: " + number));
        log.info("Finish find account by account number");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get an account by users CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Account not found. Invalid CPF.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> findAccountByCpf(
            @Parameter(description = " User account's CPF") @PathVariable String cpf){
        var response = customerService.findByCpf(cpf)
                .map((cust) -> accountService.findByCustomer(cust)
                        .map(acc -> modelMapper.map(acc, AccountResponse.class))
                        .orElseThrow(() -> new NotFoundException("Account not found for cpf:" + cpf)))
                .orElseThrow(() -> new NotFoundException("User not found for cpf: " + cpf));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the account",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "412", description = "CPF informed already exist",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content ) })

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createAccount(
            @Parameter(description = " User to be created") @Valid @RequestBody CustomerRequest customerRequest){
        log.info("Start create account method");
        Customer customer = new Customer();
        customer.setCpf(customerRequest.getCpf());
        customer.setFullName(customerRequest.getFullName());
        customerService.createOrUpdate(customer);
        accountService.create(customer);
        log.info("Finish create account method");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Make a transfer from one account to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer was successful",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TransactionResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Source or target account not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "412", description = "Insufficient balance for the transaction",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content ) })

    @PostMapping(value = "/{id}/transfer",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionResponse>> transferTransaction(
            @Parameter(description = " Source account's ID") @PathVariable Long id,
            @Parameter(description = " Target account and amount") @Valid @RequestBody TransferRequest transferRequest){
        log.info("Start transfer transaction");
        var transactions = accountService.transfer(
                id,
                transferRequest.getAmount(),
                transferRequest.getTargetAccountId());
        var response = transactions
                .stream()
                .map(t -> modelMapper.map(t, TransactionResponse.class))
                .collect(Collectors.toList());
        log.info("Finish transfer transaction");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Make a withdraw from an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdraw was successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "412", description = "Insufficient balance for the transaction",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content ) })

    @PostMapping(value = "/{id}/withdraw",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> withdrawTransaction (
            @Parameter(description = " Account's ID") @PathVariable Long id,
            @Parameter(description = " Withdrawal amount") @Valid @RequestBody TransactionRequest transactionRequest){
        log.info("Start withdraw transaction");
        var transaction = accountService.withdrawTransaction(id, transactionRequest.getAmount());
        var response = modelMapper.map(transaction, TransactionResponse.class);
        log.info("Finish withdraw transaction");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Make a deposit to an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit was successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @PostMapping(value = "{id}/deposit",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> depositTransaction(
            @Parameter(description = " Account's ID") @PathVariable Long id,
            @Parameter(description = " Deposit amount") @Valid @RequestBody TransactionRequest transactionRequest){
        log.info("Start deposit transaction");
        var transaction = accountService.depositTransaction(id, transactionRequest.getAmount());
        var response = modelMapper.map(transaction, TransactionResponse.class);
        log.info("Finish deposit transaction");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
