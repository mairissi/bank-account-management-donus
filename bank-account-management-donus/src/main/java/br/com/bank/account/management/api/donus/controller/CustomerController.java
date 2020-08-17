package br.com.bank.account.management.api.donus.controller;

import br.com.bank.account.management.api.donus.common.exception.CustomErrorResponse;
import br.com.bank.account.management.api.donus.controller.dto.CustomerRequest;
import br.com.bank.account.management.api.donus.controller.dto.CustomerResponse;
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
@Tag(name = "User", description = "Everythign about user")
@RequestMapping("/users")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CustomerResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not found any users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerResponse>> findAll(){
        log.info("Start find all users");
        var response = customerService.findAll()
                .stream()
                .map(acc -> modelMapper.map(acc, CustomerResponse.class))
                .collect(Collectors.toList());
        if (response.isEmpty()) {throw new NotFoundException("Did not found any users");}
        log.info("Finish find all users");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> findById(
            @Parameter(description = " User's ID") @PathVariable Long id){
        log.info("Start find user by id");
        var customer = customerService.findById(id)
                .map(c -> modelMapper.map(c, CustomerResponse.class))
                .orElseThrow(() -> new NotFoundException("User not found for id:" + id));
        log.info("Finish find user by id");
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @Operation(summary = "Update user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Updated the user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "412", description = "CPF informed already exist",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content ) })

    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCustomer(@Parameter(description = " User's ID") @PathVariable Long id,
             @Parameter(description = " Updated user information") @Valid @RequestBody CustomerRequest customerRequest){
        log.info("Start update customer method");
        return customerService.findById(id)
                .map(c -> {
                    c.setCpf(customerRequest.getCpf());
                    c.setFullName(customerRequest.getFullName());
                    customerService.createOrUpdate(c);
                    log.info("Finish update customer method");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFoundException("User not found for id: " + id));
    }

    @Operation(summary = "Get an user by CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })

    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> findByCpf(
            @Parameter(description = " User's CPF") @PathVariable String cpf){
        log.info("Start find user by cpf");
        var customer = customerService.findByCpf(cpf)
                    .map((cust) -> modelMapper.map(cust, CustomerResponse.class))
                    .orElseThrow(() -> new NotFoundException("User not found for cpf:" + cpf));
        log.info("Finish find user by cpf");
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

}
