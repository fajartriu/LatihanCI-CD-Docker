package project.Challenge_6BinarFood.service.invoice;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ResponseStatusException;
import project.Challenge_6BinarFood.dto.data.InvoiceOrder;
import project.Challenge_6BinarFood.dto.request.invoice.ReportMerchantRequest;
import project.Challenge_6BinarFood.dto.response.invoice.InvoiceResponse;
import project.Challenge_6BinarFood.dto.response.invoice.ReportMerchantResponse;
import project.Challenge_6BinarFood.model.order.Order;
import project.Challenge_6BinarFood.model.user.User;
import project.Challenge_6BinarFood.repository.merchant.MerchantRepository;
import project.Challenge_6BinarFood.repository.order.OrderDetailRepository;
import project.Challenge_6BinarFood.repository.order.OrderRepository;
import project.Challenge_6BinarFood.service.merchant.MerchantServiceImpl;
import project.Challenge_6BinarFood.service.user_auth.AuthenticationServiceImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class InvoiceServiceImpl implements InvoiceService{
    private final static Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);
    private final MerchantRepository merchantRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AuthenticationServiceImpl authenticationService;

    private final OrderRepository orderRepository;

    @Override
    public byte[] exportInvoiceUser(Principal principal, String reportFormat) throws FileNotFoundException, JRException {
        String path = "report";
        User idUser = authenticationService.getIdUser(principal.getName());
        List<InvoiceOrder> allUserOrderDetailInvoice = orderDetailRepository.getAllUserOrderDetailInvoice(idUser.getId());
        List<InvoiceResponse> invoiceResponses = new ArrayList<>();

        if (!allUserOrderDetailInvoice.isEmpty()){
            Double sumTotal = 0.0;
            for (InvoiceOrder invoiceOrderRequest : allUserOrderDetailInvoice) {
                if (!invoiceOrderRequest.isCompleted()){
                    updateComplete(invoiceOrderRequest.getOrderId());
                    sumTotal+=invoiceOrderRequest.getTotalPrice();
                    invoiceResponses.add(new InvoiceResponse(invoiceOrderRequest.getOrderTime().toLocalDate(), invoiceOrderRequest.getOrderTime().toLocalTime(),
                            invoiceOrderRequest.getUsername(), invoiceOrderRequest.getProductName(),
                            invoiceOrderRequest.getMerchantName(), invoiceOrderRequest.getDestinationAddress(),
                            true, invoiceOrderRequest.getPrice(),
                            invoiceOrderRequest.getQuantity(), invoiceOrderRequest.getPrice(),
                            sumTotal
                    ));
                }else{
                    sumTotal+=invoiceOrderRequest.getTotalPrice();
                    invoiceResponses.add(new InvoiceResponse(invoiceOrderRequest.getOrderTime().toLocalDate(), invoiceOrderRequest.getOrderTime().toLocalTime(),
                            invoiceOrderRequest.getUsername(), invoiceOrderRequest.getProductName(),
                            invoiceOrderRequest.getMerchantName(), invoiceOrderRequest.getDestinationAddress(),
                            invoiceOrderRequest.isCompleted(), invoiceOrderRequest.getPrice(),
                            invoiceOrderRequest.getQuantity(), invoiceOrderRequest.getPrice(),
                            sumTotal
                    ));
                }
            }
        }
        else {
            logger.info("empty");
        }
        return generateReportUser(invoiceResponses, reportFormat);
    }

    @Override
    public byte[] exportReportMerchant(UUID merchantId, String reportFormat) throws FileNotFoundException, JRException {
        String path = "report";
        List<ReportMerchantRequest> allReportMerchant = merchantRepository.getAllReportMerchant(merchantId);
        List<ReportMerchantResponse> reportMerchantResponses = new ArrayList<>();

        if (!allReportMerchant.isEmpty()) {
            Double sumTotal = 0.0;
            for (ReportMerchantRequest reportMerchantRequest : allReportMerchant) {
                sumTotal+=reportMerchantRequest.getTotal_price();
                reportMerchantResponses.add(new ReportMerchantResponse(reportMerchantRequest.getId_merchant(),
                        reportMerchantRequest.getMerchant_name(), reportMerchantRequest.getMerchant_location(),
                        reportMerchantRequest.getOrder_time().toLocalDate(), reportMerchantRequest.getOrder_time().toLocalTime(),
                        reportMerchantRequest.getId_order_detail(), reportMerchantRequest.getUsername(),
                        reportMerchantRequest.getDestination_address(), reportMerchantRequest.getId_product(),
                        reportMerchantRequest.getProduct_name(), reportMerchantRequest.isCompleted(),
                        reportMerchantRequest.getPrice(), reportMerchantRequest.getQuantity(),
                        reportMerchantRequest.getTotal_price(), sumTotal
                ));
            }
        }

        return generateReport(reportMerchantResponses, reportFormat);
    }

    @Override
    public byte[] exportReportMerchantWeek(UUID merchantId, String reportFormat, LocalDate date) throws FileNotFoundException, JRException {
        String path = "report";
        List<ReportMerchantRequest> allReportMerchant = merchantRepository.getAllReportMerchant(merchantId);
        List<ReportMerchantResponse> reportMerchantResponses = new ArrayList<>();

        if (!allReportMerchant.isEmpty()){
            Double sumTotal = 0.0;
            for (ReportMerchantRequest reportMerchantRequest : allReportMerchant) {
                if ((reportMerchantRequest.getOrder_time().toLocalDate()).isBefore(date.plusDays(1))
                        && (reportMerchantRequest.getOrder_time().toLocalDate()).isAfter(date.minusDays(8))){
                    sumTotal = checkingCompleted(reportMerchantResponses, sumTotal, reportMerchantRequest);
                }

            }
        }

        return generateReport(reportMerchantResponses, reportFormat);
    }

    @Override
    public byte[] exportReportMerchantMonth(UUID merchantId, String reportFormat, String month)
            throws FileNotFoundException, JRException {
        String path = "report";
        List<ReportMerchantRequest> allReportMerchant = merchantRepository.getAllReportMerchant(merchantId);
        List<ReportMerchantResponse> reportMerchantResponses = new ArrayList<>();

        if (!allReportMerchant.isEmpty()){
            Double sumTotal = 0.0;
            for (ReportMerchantRequest reportMerchantRequest : allReportMerchant) {
                if ((reportMerchantRequest.getOrder_time().toLocalDate()).getMonth().toString().toLowerCase().equals(month)){
                    sumTotal = checkingCompleted(reportMerchantResponses, sumTotal, reportMerchantRequest);
                }

            }
        }

        return generateReport(reportMerchantResponses, reportFormat);
    }

    @Override
    public byte[] exportReportMerchantCustom(UUID merchantId, String reportFormat, LocalDate startDate, LocalDate endDate)
            throws FileNotFoundException, JRException {
        String path = "report";
        List<ReportMerchantRequest> allReportMerchant = merchantRepository.getAllReportMerchant(merchantId);
        List<ReportMerchantResponse> reportMerchantResponses = new ArrayList<>();

        if (!allReportMerchant.isEmpty()){
            Double sumTotal = 0.0;
            for (ReportMerchantRequest reportMerchantRequest : allReportMerchant) {
                if ((reportMerchantRequest.getOrder_time().toLocalDate()).isBefore(endDate)
                        && (reportMerchantRequest.getOrder_time().toLocalDate()).isAfter(startDate)){
                    sumTotal = checkingCompleted(reportMerchantResponses, sumTotal, reportMerchantRequest);
                }

            }
        }

        return generateReport(reportMerchantResponses, reportFormat);
    }

    @Override
    public void updateComplete(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Order is not found"));
        order.setCompleted(true);
        order.setOrder_time(order.getOrder_time());
        order.setDestination_address(order.getDestination_address());
        orderRepository.save(order);
    }

    @Override
    public byte[] generateReport(List<ReportMerchantResponse> report, String reportFormat) throws FileNotFoundException, JRException{
        JasperReport jasperReport;

        try {
            jasperReport = (JasperReport)
                    JRLoader.loadObject(ResourceUtils.getFile("MerchantTemplatesReports"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:reports/MerchantTemplatesReports.jrxml");
                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReport, "MerchantTemplatesReports");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(report);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("total", "Total Product: "+report.size());
        JasperPrint jasperPrint = null;
        byte[] reportContent;

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            switch (reportFormat) {
                case "pdf" -> reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
                case "xml" -> reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
                default -> throw new RuntimeException("Unknown report format");
            }
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return reportContent;
    }

    @Override
    public byte[] generateReportUser(List<InvoiceResponse> report, String reportFormat) throws FileNotFoundException, JRException{
        JasperReport jasperReport;

        try {
            jasperReport = (JasperReport)
                    JRLoader.loadObject(ResourceUtils.getFile("InvoiceUsersTemplatesReports"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:reports/InvoiceUsersTemplatesReports.jrxml");
                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReport, "InvoiceUsersTemplatesReports");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(report);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("total", "Total Product: "+report.size());
        JasperPrint jasperPrint = null;
        byte[] reportContent;

        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            switch (reportFormat) {
                case "pdf" -> reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
                case "xml" -> reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
                default -> throw new RuntimeException("Unknown report format");
            }
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return reportContent;
    }

    public Double checkingCompleted(List<ReportMerchantResponse> reportMerchantResponses, Double sumTotal, ReportMerchantRequest reportMerchantRequest) {
        if (reportMerchantRequest.isCompleted()){
            sumTotal+=reportMerchantRequest.getTotal_price();
            reportMerchantResponses.add(new ReportMerchantResponse(reportMerchantRequest.getId_merchant(),
                    reportMerchantRequest.getMerchant_name(), reportMerchantRequest.getMerchant_location(),
                    reportMerchantRequest.getOrder_time().toLocalDate(), reportMerchantRequest.getOrder_time().toLocalTime(),
                    reportMerchantRequest.getId_order_detail(), reportMerchantRequest.getUsername(),
                    reportMerchantRequest.getDestination_address(), reportMerchantRequest.getId_product(),
                    reportMerchantRequest.getProduct_name(), true,
                    reportMerchantRequest.getPrice(), reportMerchantRequest.getQuantity(),
                    reportMerchantRequest.getTotal_price(), sumTotal
            ));
        }
        return sumTotal;
    }

    @Override
    public List<InvoiceResponse> invoiceOrderByUser(String token){
        User idUser = authenticationService.getIdUser(token);
        List<InvoiceOrder> allUserOrderDetailInvoice = orderDetailRepository.getAllUserOrderDetailInvoice(idUser.getId());
        List<InvoiceResponse> invoiceResponses = new ArrayList<>();

        if (!allUserOrderDetailInvoice.isEmpty()){
            Double sumTotal = 0.0;
            for (InvoiceOrder invoiceOrderRequest : allUserOrderDetailInvoice) {
                if (!invoiceOrderRequest.isCompleted()){
                    updateComplete(invoiceOrderRequest.getOrderId());
                    sumTotal+=invoiceOrderRequest.getTotalPrice();
                    invoiceResponses.add(new InvoiceResponse(invoiceOrderRequest.getOrderTime().toLocalDate(), invoiceOrderRequest.getOrderTime().toLocalTime(),
                            invoiceOrderRequest.getUsername(), invoiceOrderRequest.getProductName(),
                            invoiceOrderRequest.getMerchantName(), invoiceOrderRequest.getDestinationAddress(),
                            true, invoiceOrderRequest.getPrice(),
                            invoiceOrderRequest.getQuantity(), invoiceOrderRequest.getPrice(),
                            sumTotal
                    ));
                }else{
                    sumTotal+=invoiceOrderRequest.getTotalPrice();
                    invoiceResponses.add(new InvoiceResponse(invoiceOrderRequest.getOrderTime().toLocalDate(), invoiceOrderRequest.getOrderTime().toLocalTime(),
                            invoiceOrderRequest.getUsername(), invoiceOrderRequest.getProductName(),
                            invoiceOrderRequest.getMerchantName(), invoiceOrderRequest.getDestinationAddress(),
                            invoiceOrderRequest.isCompleted(), invoiceOrderRequest.getPrice(),
                            invoiceOrderRequest.getQuantity(), invoiceOrderRequest.getPrice(),
                            sumTotal
                    ));
                }
            }
        }
        else {
            logger.info("empty");
        }
        return invoiceResponses;
    }

//    @Override
//    public List<InvoiceResponse> invoiceOrderByUser(Principal principal){
//        User idUser = authenticationService.getIdUser(principal.getName());
//        List<InvoiceOrder> allUserOrderDetailInvoice = orderDetailRepository.getAllUserOrderDetailInvoice(idUser.getId());
//        List<InvoiceResponse> invoiceResponses = new ArrayList<>();
//
//        if (!allUserOrderDetailInvoice.isEmpty()){
//            Double sumTotal = 0.0;
//            for (InvoiceOrder invoiceOrderRequest : allUserOrderDetailInvoice) {
//                if (!invoiceOrderRequest.isCompleted()){
//                    updateComplete(invoiceOrderRequest.getOrderId());
//                    sumTotal+=invoiceOrderRequest.getTotalPrice();
//                    invoiceResponses.add(new InvoiceResponse(invoiceOrderRequest.getOrderTime().toLocalDate(), invoiceOrderRequest.getOrderTime().toLocalTime(),
//                            invoiceOrderRequest.getUsername(), invoiceOrderRequest.getProductName(),
//                            invoiceOrderRequest.getMerchantName(), invoiceOrderRequest.getDestinationAddress(),
//                            true, invoiceOrderRequest.getPrice(),
//                            invoiceOrderRequest.getQuantity(), invoiceOrderRequest.getPrice(),
//                            sumTotal
//                    ));
//                }else{
//                    sumTotal+=invoiceOrderRequest.getTotalPrice();
//                    invoiceResponses.add(new InvoiceResponse(invoiceOrderRequest.getOrderTime().toLocalDate(), invoiceOrderRequest.getOrderTime().toLocalTime(),
//                            invoiceOrderRequest.getUsername(), invoiceOrderRequest.getProductName(),
//                            invoiceOrderRequest.getMerchantName(), invoiceOrderRequest.getDestinationAddress(),
//                            invoiceOrderRequest.isCompleted(), invoiceOrderRequest.getPrice(),
//                            invoiceOrderRequest.getQuantity(), invoiceOrderRequest.getPrice(),
//                            sumTotal
//                    ));
//                }
//            }
//        }
//        else {
//            logger.info("empty");
//        }
//        return invoiceResponses;
//    }


}