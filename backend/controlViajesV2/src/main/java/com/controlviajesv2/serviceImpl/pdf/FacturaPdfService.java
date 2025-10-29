package com.controlviajesv2.serviceImpl.pdf;


import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Factura;
import com.controlviajesv2.entity.Servicio;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.repository.FacturaRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class FacturaPdfService {

    @Autowired
    private FacturaRepository facturaRepository;

    public byte[] generarPdf(Long facturaId) throws Exception {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        Empresa empresa = factura.getEmpresa();
        Usuario usuario = empresa.getUsuario();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, baos);
        document.open();

        // --- Título ---
        Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph(usuario.getNombre() +"   FACUTRA "+ factura.getNumeroFactura()  +"FECHA: "+factura.getFechaEmision(), tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" "));

        // --- Datos empresa ---
        // ---------------------------------para poder a;adir datos en plan como la de rigo en dos columnas hay que hacer una tabla  , algo asi PdfPTable infoTable = new PdfPTable(2); // 2 columnas
        Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
        document.add(new Paragraph("Empresa:", bold));
        document.add(new Paragraph(empresa.getNombre()));
        document.add(new Paragraph("CIF: " + empresa.getCif()));
        document.add(new Paragraph("Dirección: " + empresa.getDireccion()));
        document.add(new Paragraph(" "));

        // --- Datos factura ---
        document.add(new Paragraph("Fecha: " )); //+ factura.getFecha()
        document.add(new Paragraph("Importe total: " + factura.getTotalBruto() + " €"));
        document.add(new Paragraph(" "));

        // --- Tabla de servicios ---
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.addCell(new PdfPCell(new Phrase("Tipo de servicio", bold)));
        table.addCell(new PdfPCell(new Phrase("Origen", bold)));
        table.addCell(new PdfPCell(new Phrase("Destino", bold)));

        for (Servicio s : factura.getServicios()) {
            table.addCell(s.getTipoServicio());
            table.addCell(s.getOrigen());
            table.addCell(s.getDestino());
        }

        document.add(table);
        document.add(new Paragraph(" "));

        // --- Total ---
        Paragraph total = new Paragraph("Total: " + factura.getTotalFactura() + " €", bold);
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        document.close();
        return baos.toByteArray();
    }
}