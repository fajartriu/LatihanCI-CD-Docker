package project.Challenge_6BinarFood.service.invoice;

import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;
import project.Challenge_6BinarFood.dto.response.invoice.InvoiceResponse;
import project.Challenge_6BinarFood.dto.response.invoice.ReportMerchantResponse;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public interface InvoiceService {
    byte[] exportInvoiceUser(Principal principal, String reportFormat) throws FileNotFoundException, JRException;
//    List<ReportMerchantResponse> generateReportMerchant(UUID merchantID, LocalDate date);

    byte[] exportReportMerchant(UUID merchantId,String reportFormat) throws FileNotFoundException, JRException;

    byte[] exportReportMerchantWeek(UUID merchantId, String reportFormat, LocalDate date) throws FileNotFoundException, JRException;

    byte[] exportReportMerchantMonth(UUID merchantId, String reportFormat, String month) throws FileNotFoundException, JRException;

    byte[] exportReportMerchantCustom(UUID merchantId, String reportFormat, LocalDate startDate, LocalDate endDate) throws FileNotFoundException, JRException;
    void updateComplete(UUID orderId);
    byte[] generateReport(List<ReportMerchantResponse> report, String reportFormat) throws FileNotFoundException, JRException;

    byte[] generateReportUser(List<InvoiceResponse> report, String reportFormat) throws FileNotFoundException, JRException;

    List<InvoiceResponse> invoiceOrderByUser(String token);
}
