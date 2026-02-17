package com.controlviajesv2.serviceImpl.pdf;


import com.controlviajesv2.entity.Empresa;
import com.controlviajesv2.entity.Factura;
import com.controlviajesv2.entity.Servicio;
import com.controlviajesv2.entity.Usuario;
import com.controlviajesv2.repository.FacturaRepository;
import com.controlviajesv2.serviceImpl.FacturaServiceImpl;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;



@Service
public class FacturaPdfService {

    @Autowired
    private FacturaRepository facturaRepository;
    private static final Logger logger = LoggerFactory.getLogger(FacturaServiceImpl.class);


    public byte[] generarPdf(Long facturaId) throws Exception {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));


        Empresa empresa = factura.getEmpresa();
        Usuario usuario = empresa.getUsuario();



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, baos);
        document.open();


        //fuentes y estilos que se van a usar en el docuento
        Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 11);
        Font boldBlue = new Font(Font.HELVETICA, 12, Font.BOLD);

        // --- Título ---
        Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Chunk tituloFactura = new Chunk(usuario.getNombre() +"   FACUTRA "+ factura.getNumeroFactura()  +"   FECHA: "+factura.getFechaEmision(), tituloFont);
        tituloFactura.setUnderline(0.1f, -2f);//esto sirve para que este subrayado
        Paragraph titulo = new Paragraph( tituloFactura);

        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" "));





// --- Tabla principal de 2 columnas ---
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10f);
        infoTable.setSpacingAfter(10f);
        infoTable.setWidths(new float[]{1, 1}); // equilibrio entre columnas

// --- Columna 1: Usuario ---
        PdfPTable usuarioTable = new PdfPTable(1);
        usuarioTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        usuarioTable.addCell(new Phrase(usuario.getNombre()));
        usuarioTable.addCell(new Phrase(usuario.getNif()));
        usuarioTable.addCell(new Phrase(usuario.getEmail()));
        usuarioTable.addCell(new Phrase(usuario.getDireccion().getCalle()+" "+usuario.getDireccion().getNumero()));
        usuarioTable.addCell(new Phrase(usuario.getDireccion().getCodigoPostal()+", "+usuario.getDireccion().getCiudad()));



// --- Columna 2: Empresa ---
        PdfPTable empresaTable = new PdfPTable(1);
        empresaTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        //empresaTable.addCell(new Phrase("Empresa:", bold));
        empresaTable.addCell(new Phrase(empresa.getNombre()));
       // empresaTable.addCell(new Phrase( empresa.getDireccion()));
        empresaTable.addCell(new Phrase( empresa.getCif()));
        empresaTable.addCell(new Phrase(empresa.getDireccion().getCalle()+" "+empresa.getDireccion().getNumero()));
        empresaTable.addCell(new Phrase(empresa.getDireccion().getCodigoPostal()+", "+empresa.getDireccion().getCiudad()));

// --- Añadir las tablas como celdas ---
        PdfPCell usuarioCell = new PdfPCell(usuarioTable);
        usuarioCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell empresaCell = new PdfPCell(empresaTable);
        empresaCell.setBorder(Rectangle.NO_BORDER);

        infoTable.addCell(usuarioCell);
        infoTable.addCell(empresaCell);

// --- Añadir al documento ---
        document.add(infoTable);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE); // doble salto de línea

//_____________________SERVICIOS___________________________________



        boldBlue.setColor(0, 70, 140); // azul oscuro profesional

        document.add(new Paragraph("SERVICIOS", boldBlue));
        document.add(Chunk.NEWLINE);

        int contador = 1;
        for (Servicio s : factura.getServicios()) {

            // Creamos una tabla de 1 columna con fondo y borde para el bloque del servicio
            PdfPTable servicioTable = new PdfPTable(1);
            servicioTable.setWidthPercentage(100);
            servicioTable.setSpacingBefore(8f);
            servicioTable.setSpacingAfter(8f);

            // Configuración del borde (gris claro)
            PdfPCell cell = new PdfPCell();
            cell.setBorderColor(new Color(180, 180, 180));
            cell.setPadding(8f);
            cell.setBorderWidth(0.8f);

            // --- Contenido del bloque ---
            Paragraph contenido = new Paragraph();
            contenido.add(new Chunk(
                    contador + "           Servicio "+ s.getTipoServicio()+"       " + s.getFechaServicio() +
                            "             para            " + s.getClienteFinal() + "\n", bold
            ));
            contenido.add(new Chunk("SALIDA: " + s.getOrigen() + "\n", normal));
            contenido.add(new Chunk("DESTINO: " + s.getDestino() + "\n", normal));
            contenido.add(new Chunk("CONDUCTOR: " + (s.getConductor() != null ? s.getConductor() : "-") + "\n", normal));
            contenido.add(new Chunk("\nKm: " + s.getKm() + " km                                                            Precio: " + s.getPrecioKm() +  " € \n", normal));


            if (s.getHorasEspera() != null) {
                contenido.add(new Chunk("Tiempo Espera: " + s.getHorasEspera() + "h                                          Precio: " + s.getImporteEspera() +  " € \n", normal));
            }





// Tabla interna para alinear  completamente a la derecha
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);

            PdfPCell emptyCell = new PdfPCell(new Phrase("")); // celda izquierda vacía
            emptyCell.setBorder(Rectangle.NO_BORDER);
            totalTable.addCell(emptyCell);

            PdfPCell totalCell = new PdfPCell(new Phrase("Total: " + s.getImporteServicio() + " €", bold));
            totalCell.setBorder(Rectangle.NO_BORDER);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalCell);

            // --- Añadir contenido y total a la celda principal ---
            cell.addElement(contenido);
            cell.addElement(totalTable);

            servicioTable.addCell(cell);

            // --- Añadir tabla (bloque) al documento ---
            document.add(servicioTable);

            contador++;
        }





        // --- Total ---
        Paragraph total = new Paragraph("Total: " + factura.getTotalFactura() + " €", bold);
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        document.close();
        return baos.toByteArray();
    }
}