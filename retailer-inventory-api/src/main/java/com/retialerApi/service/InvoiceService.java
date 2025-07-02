package com.retialerApi.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.retialerApi.entity.OrderItem;
import com.retialerApi.entity.orders; // ✅ Correct entity

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class InvoiceService {

    public ByteArrayInputStream generateInvoice(orders order) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph para = new Paragraph("🧾 Invoice - Order #" + order.getId(), fontTitle);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            // Customer Details
            document.add(new Paragraph("👤 Customer: " + order.getCustomerName()));
            document.add(new Paragraph("📅 Order Date: " + order.getOrderDate().toString()));
            document.add(Chunk.NEWLINE);

            // Table Header
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 1, 1});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell h1 = new PdfPCell(new Phrase("Product Name", headFont));
            PdfPCell h2 = new PdfPCell(new Phrase("Qty", headFont));
            PdfPCell h3 = new PdfPCell(new Phrase("Price", headFont));

            table.addCell(h1);
            table.addCell(h2);
            table.addCell(h3);

            // Table Data
            for (OrderItem item : order.getItems()) {
                table.addCell(item.getProductName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("₹" + item.getPrice());
            }


            document.add(table);

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("✅ Thank you for your order!", fontTitle));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
