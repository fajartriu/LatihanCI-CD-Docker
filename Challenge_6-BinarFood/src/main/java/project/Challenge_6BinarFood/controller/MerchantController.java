package project.Challenge_6BinarFood.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.Challenge_6BinarFood.dto.request.merchant.CreateMerchantRequest;
import project.Challenge_6BinarFood.dto.request.merchant.GetAllOpenMerchantRequest;
import project.Challenge_6BinarFood.dto.response.merchant.MerchantResponse;
import project.Challenge_6BinarFood.dto.response.web.WebResponse;
import project.Challenge_6BinarFood.service.merchant.MerchantService;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/merchant")
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('MERCHANT')")
    public WebResponse<MerchantResponse> createMerchant(@RequestBody CreateMerchantRequest request, Principal principal){
        try {
            MerchantResponse merchantResponse = merchantService.createMerchant(request, principal);
            return WebResponse.<MerchantResponse>builder().countRecord(1).data(merchantResponse).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<MerchantResponse>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @PostMapping(
            path = "/getAllMerchantOpenOrClose",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
//    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<List<MerchantResponse>> getAllMerchantOpen(@RequestBody GetAllOpenMerchantRequest request, Principal principal){
        try {
            List<MerchantResponse> allOpenMerchant = merchantService.getAllOpenMerchant(request, principal);
            return WebResponse.<List<MerchantResponse>>builder().countRecord(allOpenMerchant.size()).data(allOpenMerchant).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<List<MerchantResponse>>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @PatchMapping(
            path = "/{merchantId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('MERCHANT')")
    public WebResponse<MerchantResponse> updateMerchant(@PathVariable UUID merchantId, @RequestBody Map<String, Optional> field){
        try {
            merchantId = UUID.fromString(merchantId.toString());
            MerchantResponse merchantResponse = merchantService.updateMerchant(merchantId, field);
            return WebResponse.<MerchantResponse>builder().countRecord(1).data(merchantResponse).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<MerchantResponse>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "{merchantId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
//    @PreAuthorize("hasRole('CUSTOMER')" + "|| hasRole('MERCHANT')")
    public WebResponse<MerchantResponse> getMerchantById(@PathVariable UUID merchantId){
        try {
            MerchantResponse merchantById = merchantService.getMerchantById(merchantId);
            return WebResponse.<MerchantResponse>builder().countRecord(1).data(merchantById).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<MerchantResponse>builder().data(null).messages(e.getMessage()).build();
        }
    }

    @DeleteMapping(
            path = "/{merchantId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('MERCHANT')")
    public WebResponse<String> deleteMerchant(@PathVariable UUID merchantId){
        try {
            merchantService.deleteMerchant(merchantId);
            return WebResponse.<String>builder().data("Deleted : "+ merchantId).messages("Success").build();
        }catch (Exception e){
            return WebResponse.<String>builder().data(null).messages(e.getMessage()).build();
        }
    }

}