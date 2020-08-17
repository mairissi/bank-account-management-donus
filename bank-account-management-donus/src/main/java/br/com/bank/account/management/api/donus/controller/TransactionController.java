package br.com.bank.account.management.api.donus.controller;

import br.com.bank.account.management.api.donus.common.exception.CustomErrorResponse;
import br.com.bank.account.management.api.donus.controller.dto.TransactionResponse;
import br.com.bank.account.management.api.donus.service.transaction.TransactionService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Tag(name = "Transactions", description = "Retrieve informations about transactions")
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Get all transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found transactions",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TransactionResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not found any transactions",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionResponse>> findAll(){
        log.info("Start find all transactions");
        var response = transactionService.findAll()
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionResponse.class))
                .collect(Collectors.toList());
        if (response.isEmpty()) {throw new NotFoundException("Did not found any transactions");}
        log.info("Finish find all transactions");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get a transaction by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the transaction",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> findById(
            @Parameter(description = " Transaction's ID") @PathVariable Long id){
        log.info("Start find transaction by id");
        var response = transactionService.findById(id)
                .map(t -> modelMapper.map(t, TransactionResponse.class))
                .orElseThrow(() -> new NotFoundException("Transaction not found for id:" + id));
        log.info("Finish find transaction by id");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all transactions for an account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the transactions",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TransactionResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(value = "/accounts/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionResponse>> findTransactionsByAccountId(
            @Parameter(description = " Account's ID") @PathVariable Long accountId){
        log.info("Start find all transactions by account id");
        var response = transactionService.findByAccountId(accountId)
                .stream()
                .map(t -> modelMapper.map(t, TransactionResponse.class))
                .collect(Collectors.toList());

        if (response.isEmpty()) {throw new NotFoundException("Not found transactions for accountId: " + accountId);}

        log.info("Finish find all transactions by account id");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
