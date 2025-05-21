package com.ApiSpringHackathon.demo.controller;
import Exceptions.AuthException;
import Exceptions.GeneralException;
import Services.Issue.SWIssueService;
import Services.Pdf.SWPdfService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//import Utils.Responses.Storage.StorageResponse;
//import Services.Storage.SWStorageService;

import Services.Resend.SWResendService;
import Services.Stamp.SWStampServiceV4;
import Services.Storage.SWStorageService;
import Utils.Responses.Pdf.PdfResponse;
import Utils.Responses.Resend.ResendResponse;
import Utils.Responses.Stamp.SuccessV1Response;
import Utils.Responses.Stamp.SuccessV4Response;
import Utils.Responses.Storage.StorageData;
import Utils.Responses.Storage.StorageResponse;
import com.ApiSpringHackathon.demo.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api")
public class FacturacionController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private String xml;
    private String uuid;
    private String pdfBase64;
    private String email;
//@RequestBody String email,String rfc, String servicio, String token, String fechaHora, String nombreCompleto, String regimenFiscal, String Codigo_Postal, String usoCFDI
    @PostMapping("/facturaCFDI")
    public ResponseEntity<String> facturaCFDI(@RequestBody Map<String, Object> requestData){

        Map<String, Object> data = (Map<String, Object>) requestData.get("data");
        if (data != null) {
            try{
            // Acceder a las propiedades del mapa anidado
                System.out.println("Email: " + data.get("email"));
                System.out.println("RFC: " + data.get("rfc"));
                System.out.println("Servicio: " + data.get("servicio"));
                System.out.println("Token: " + data.get("token"));
                System.out.println("Fecha y Hora: " + data.get("fechaHora"));
                System.out.println("Nombre Completo: " + data.get("nombreCompleto"));
                System.out.println("Régimen Fiscal: " + data.get("regimenFiscal"));
                System.out.println("Código Postal: " + data.get("Codigo_Postal"));
                System.out.println("Uso CFDI: " + data.get("usoCfdi"));

                this.email = (String) data.get("email");
                String rfc = (String) data.get("rfc");
                String servicio = (String) data.get("servicio");
                String Token = (String) data.get("token");
                LocalDateTime fechaActual = LocalDateTime.now();
                // Formatear la fecha al formato deseado
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                String fechaFormateada = fechaActual.format(formatter);
                // Imprimir la fecha formateada
                System.out.println(fechaFormateada);
                String nombreCompleto = (String) data.get("nombreCompleto");
                String regimenFiscal = (String) data.get("regimenFiscal");
                String Codigo_Postal = (String) data.get("Codigo_Postal");
                String usoCfdi = (String) data.get("usoCfdi");
                Random random = new Random();
                int folio = 10000 + random.nextInt(90000);
                String Folio = String.valueOf(folio);

                //String xml  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/4\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/4 http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd http://www.sat.gob.mx/Pagos20 http://www.sat.gob.mx/sitio_internet/cfd/Pagos/Pagos20.xsd\" Version=\"4.0\" Serie=\"J\" Folio=\"36434\" Fecha=\"2025-04-23T12:10:00\"  FormaPago=\"99\" NoCertificado=\"30001000000500003416\" Certificado=\"MIIFsDCCA5igAwIBAgIUMzAwMDEwMDAwMDA1MDAwMDM0MTYwDQYJKoZIhvcNAQELBQAwggErMQ8wDQYDVQQDDAZBQyBVQVQxLjAsBgNVBAoMJVNFUlZJQ0lPIERFIEFETUlOSVNUUkFDSU9OIFRSSUJVVEFSSUExGjAYBgNVBAsMEVNBVC1JRVMgQXV0aG9yaXR5MSgwJgYJKoZIhvcNAQkBFhlvc2Nhci5tYXJ0aW5lekBzYXQuZ29iLm14MR0wGwYDVQQJDBQzcmEgY2VycmFkYSBkZSBjYWxpejEOMAwGA1UEEQwFMDYzNzAxCzAJBgNVBAYTAk1YMRkwFwYDVQQIDBBDSVVEQUQgREUgTUVYSUNPMREwDwYDVQQHDAhDT1lPQUNBTjERMA8GA1UELRMIMi41LjQuNDUxJTAjBgkqhkiG9w0BCQITFnJlc3BvbnNhYmxlOiBBQ0RNQS1TQVQwHhcNMjMwNTE4MTE0MzUxWhcNMjcwNTE4MTE0MzUxWjCB1zEnMCUGA1UEAxMeRVNDVUVMQSBLRU1QRVIgVVJHQVRFIFNBIERFIENWMScwJQYDVQQpEx5FU0NVRUxBIEtFTVBFUiBVUkdBVEUgU0EgREUgQ1YxJzAlBgNVBAoTHkVTQ1VFTEEgS0VNUEVSIFVSR0FURSBTQSBERSBDVjElMCMGA1UELRMcRUtVOTAwMzE3M0M5IC8gVkFEQTgwMDkyN0RKMzEeMBwGA1UEBRMVIC8gVkFEQTgwMDkyN0hTUlNSTDA1MRMwEQYDVQQLEwpTdWN1cnNhbCAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtmecO6n2GS0zL025gbHGQVxznPDICoXzR2uUngz4DqxVUC/w9cE6FxSiXm2ap8Gcjg7wmcZfm85EBaxCx/0J2u5CqnhzIoGCdhBPuhWQnIh5TLgj/X6uNquwZkKChbNe9aeFirU/JbyN7Egia9oKH9KZUsodiM/pWAH00PCtoKJ9OBcSHMq8Rqa3KKoBcfkg1ZrgueffwRLws9yOcRWLb02sDOPzGIm/jEFicVYt2Hw1qdRE5xmTZ7AGG0UHs+unkGjpCVeJ+BEBn0JPLWVvDKHZAQMj6s5Bku35+d/MyATkpOPsGT/VTnsouxekDfikJD1f7A1ZpJbqDpkJnss3vQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAFaUgj5PqgvJigNMgtrdXZnbPfVBbukAbW4OGnUhNrA7SRAAfv2BSGk16PI0nBOr7qF2mItmBnjgEwk+DTv8Zr7w5qp7vleC6dIsZFNJoa6ZndrE/f7KO1CYruLXr5gwEkIyGfJ9NwyIagvHHMszzyHiSZIA850fWtbqtythpAliJ2jF35M5pNS+YTkRB+T6L/c6m00ymN3q9lT1rB03YywxrLreRSFZOSrbwWfg34EJbHfbFXpCSVYdJRfiVdvHnewN0r5fUlPtR9stQHyuqewzdkyb5jTTw02D2cUfL57vlPStBj7SEi3uOWvLrsiDnnCIxRMYJ2UA2ktDKHk+zWnsDmaeleSzonv2CHW42yXYPCvWi88oE1DJNYLNkIjua7MxAnkNZbScNw01A6zbLsZ3y8G6eEYnxSTRfwjd8EP4kdiHNJftm7Z4iRU7HOVh79/lRWB+gd171s3d/mI9kte3MRy6V8MMEMCAnMboGpaooYwgAmwclI2XZCczNWXfhaWe0ZS5PmytD/GDpXzkX0oEgY9K/uYo5V77NdZbGAjmyi8cE2B2ogvyaN2XfIInrZPgEffJ4AB7kFA2mwesdLOCh0BLD9itmCve3A1FGR4+stO2ANUoiI3w3Tv2yQSg4bjeDlJ08lXaaFCLW2peEXMXjQUk7fmpb5MNuOUTW6BE=\" SubTotal=\"1000.00\" Moneda=\"MXN\" Total=\"1000.00\" TipoDeComprobante=\"I\" Exportacion=\"01\" MetodoPago=\"PPD\" LugarExpedicion=\"20928\">\n  <cfdi:Emisor Rfc=\"EKU9003173C9\" Nombre=\"ESCUELA KEMPER URGATE\" RegimenFiscal=\"624\" />\n  <cfdi:Receptor Rfc=\"AMI780504F88\" Nombre=\"AISLANTES MINERALES\" UsoCFDI=\"G03\" DomicilioFiscalReceptor=\"78395\" RegimenFiscalReceptor=\"601\" />\n  <cfdi:Conceptos>\n    <cfdi:Concepto ClaveProdServ=\"78101802\" NoIdentificacion=\"1\" Cantidad=\"1.000000\" ClaveUnidad=\"E54\" Unidad=\"Actividad\" Descripcion=\"Flete\" ValorUnitario=\"1000.00\" Importe=\"1000.00\" ObjetoImp=\"02\">\n      <cfdi:Impuestos>\n        <cfdi:Traslados>\n          <cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" />\n        </cfdi:Traslados>\n      </cfdi:Impuestos>\n    </cfdi:Concepto>\n  </cfdi:Conceptos>\n  <cfdi:Impuestos TotalImpuestosTrasladados=\"0.00\">\n    <cfdi:Traslados>\n      <cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" />\n    </cfdi:Traslados>\n  </cfdi:Impuestos>\n</cfdi:Comprobante>\n";
                String xml = String.format(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/4\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/4 http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd http://www.sat.gob.mx/Pagos20 http://www.sat.gob.mx/sitio_internet/cfd/Pagos/Pagos20.xsd\" Version=\"4.0\" Serie=\"J\" Folio=\"%s\" Fecha=\"%s\"  FormaPago=\"99\" NoCertificado=\"30001000000500003416\" Certificado=\"MIIFsDCCA5igAwIBAgIUMzAwMDEwMDAwMDA1MDAwMDM0MTYwDQYJKoZIhvcNAQELBQAwggErMQ8wDQYDVQQDDAZBQyBVQVQxLjAsBgNVBAoMJVNFUlZJQ0lPIERFIEFETUlOSVNUUkFDSU9OIFRSSUJVVEFSSUExGjAYBgNVBAsMEVNBVC1JRVMgQXV0aG9yaXR5MSgwJgYJKoZIhvcNAQkBFhlvc2Nhci5tYXJ0aW5lekBzYXQuZ29iLm14MR0wGwYDVQQJDBQzcmEgY2VycmFkYSBkZSBjYWxpejEOMAwGA1UEEQwFMDYzNzAxCzAJBgNVBAYTAk1YMRkwFwYDVQQIDBBDSVVEQUQgREUgTUVYSUNPMREwDwYDVQQHDAhDT1lPQUNBTjERMA8GA1UELRMIMi41LjQuNDUxJTAjBgkqhkiG9w0BCQITFnJlc3BvbnNhYmxlOiBBQ0RNQS1TQVQwHhcNMjMwNTE4MTE0MzUxWhcNMjcwNTE4MTE0MzUxWjCB1zEnMCUGA1UEAxMeRVNDVUVMQSBLRU1QRVIgVVJHQVRFIFNBIERFIENWMScwJQYDVQQpEx5FU0NVRUxBIEtFTVBFUiBVUkdBVEUgU0EgREUgQ1YxJzAlBgNVBAoTHkVTQ1VFTEEgS0VNUEVSIFVSR0FURSBTQSBERSBDVjElMCMGA1UELRMcRUtVOTAwMzE3M0M5IC8gVkFEQTgwMDkyN0RKMzEeMBwGA1UEBRMVIC8gVkFEQTgwMDkyN0hTUlNSTDA1MRMwEQYDVQQLEwpTdWN1cnNhbCAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtmecO6n2GS0zL025gbHGQVxznPDICoXzR2uUngz4DqxVUC/w9cE6FxSiXm2ap8Gcjg7wmcZfm85EBaxCx/0J2u5CqnhzIoGCdhBPuhWQnIh5TLgj/X6uNquwZkKChbNe9aeFirU/JbyN7Egia9oKH9KZUsodiM/pWAH00PCtoKJ9OBcSHMq8Rqa3KKoBcfkg1ZrgueffwRLws9yOcRWLb02sDOPzGIm/jEFicVYt2Hw1qdRE5xmTZ7AGG0UHs+unkGjpCVeJ+BEBn0JPLWVvDKHZAQMj6s5Bku35+d/MyATkpOPsGT/VTnsouxekDfikJD1f7A1ZpJbqDpkJnss3vQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAFaUgj5PqgvJigNMgtrdXZnbPfVBbukAbW4OGnUhNrA7SRAAfv2BSGk16PI0nBOr7qF2mItmBnjgEwk+DTv8Zr7w5qp7vleC6dIsZFNJoa6ZndrE/f7KO1CYruLXr5gwEkIyGfJ9NwyIagvHHMszzyHiSZIA850fWtbqtythpAliJ2jF35M5pNS+YTkRB+T6L/c6m00ymN3q9lT1rB03YywxrLreRSFZOSrbwWfg34EJbHfbFXpCSVYdJRfiVdvHnewN0r5fUlPtR9stQHyuqewzdkyb5jTTw02D2cUfL57vlPStBj7SEi3uOWvLrsiDnnCIxRMYJ2UA2ktDKHk+zWnsDmaeleSzonv2CHW42yXYPCvWi88oE1DJNYLNkIjua7MxAnkNZbScNw01A6zbLsZ3y8G6eEYnxSTRfwjd8EP4kdiHNJftm7Z4iRU7HOVh79/lRWB+gd171s3d/mI9kte3MRy6V8MMEMCAnMboGpaooYwgAmwclI2XZCczNWXfhaWe0ZS5PmytD/GDpXzkX0oEgY9K/uYo5V77NdZbGAjmyi8cE2B2ogvyaN2XfIInrZPgEffJ4AB7kFA2mwesdLOCh0BLD9itmCve3A1FGR4+stO2ANUoiI3w3Tv2yQSg4bjeDlJ08lXaaFCLW2peEXMXjQUk7fmpb5MNuOUTW6BE=\" SubTotal=\"1000.00\" Moneda=\"MXN\" Total=\"1000.00\" TipoDeComprobante=\"I\" Exportacion=\"01\" MetodoPago=\"PPD\" LugarExpedicion=\"20928\"><cfdi:Emisor Rfc=\"EKU9003173C9\" Nombre=\"ESCUELA KEMPER URGATE\" RegimenFiscal=\"624\" /><cfdi:Receptor Rfc=\"%s\" Nombre=\"%s\" UsoCFDI=\"%s\" DomicilioFiscalReceptor=\"%s\" RegimenFiscalReceptor=\"%s\" /><cfdi:Conceptos><cfdi:Concepto ClaveProdServ=\"78101802\" NoIdentificacion=\"1\" Cantidad=\"1.000000\" ClaveUnidad=\"E54\" Unidad=\"Actividad\" Descripcion=\"Flete\" ValorUnitario=\"1000.00\" Importe=\"1000.00\" ObjetoImp=\"02\"><cfdi:Impuestos><cfdi:Traslados><cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" /></cfdi:Traslados></cfdi:Impuestos></cfdi:Concepto></cfdi:Conceptos><cfdi:Impuestos TotalImpuestosTrasladados=\"0.00\"><cfdi:Traslados><cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" /></cfdi:Traslados></cfdi:Impuestos></cfdi:Comprobante>",
                        Folio,fechaFormateada,rfc, nombreCompleto, usoCfdi, Codigo_Postal, regimenFiscal
                );
                System.out.println(xml);

                // Inicializar el objeto con la información de la cuenta o el token de acceso especifica la URL base para acceder al entorno deseado
                SWIssueService sdk = new SWIssueService("eduardoavilat2002@gmail.com", "wmxUyUq9#DaN", "https://services.test.sw.com.mx");
                // Inicializar un objeto de respuesta para almacenar la respuesta
                SuccessV4Response response = null;
                //Envia el JSON acompañado de la versión de respuesta que requieras
                response = (SuccessV4Response) sdk.IssueXml(xml, "v4");
                // En response se mostrará la informacion de respuesta del servicio-

                // Verifica si el timbrado fue exitoso
                if (response.Status.equals("success")) {
                    this.xml = response.cfdi;
                    this.uuid = response.uuid;
                    return ResponseEntity.ok(response.uuid);
                } else {
                    // Si hubo algún problema, devuelve el mensaje de error
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al timbrar CFDI: " + response.messageDetail);
                }
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.ok(e.toString());
            }
        } else {

            return ResponseEntity.ok("El objeto 'data' no está presente en la solicitud.");
        }
    }

    @PostMapping("/genPDF")
    public ResponseEntity<String> generarPDF(String xmlTimbrado0){
        try {
//            String xmlTimbrado2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/4\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/4 http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd http://www.sat.gob.mx/Pagos20 http://www.sat.gob.mx/sitio_internet/cfd/Pagos/Pagos20.xsd\" Version=\"4.0\" Serie=\"J\" Folio=\"36409\" Fecha=\"2025-04-23T12:10:00\" FormaPago=\"99\" NoCertificado=\"30001000000500003416\" Certificado=\"MIIFsDCCA5igAwIBAgIUMzAwMDEwMDAwMDA1MDAwMDM0MTYwDQYJKoZIhvcNAQELBQAwggErMQ8wDQYDVQQDDAZBQyBVQVQxLjAsBgNVBAoMJVNFUlZJQ0lPIERFIEFETUlOSVNUUkFDSU9OIFRSSUJVVEFSSUExGjAYBgNVBAsMEVNBVC1JRVMgQXV0aG9yaXR5MSgwJgYJKoZIhvcNAQkBFhlvc2Nhci5tYXJ0aW5lekBzYXQuZ29iLm14MR0wGwYDVQQJDBQzcmEgY2VycmFkYSBkZSBjYWxpejEOMAwGA1UEEQwFMDYzNzAxCzAJBgNVBAYTAk1YMRkwFwYDVQQIDBBDSVVEQUQgREUgTUVYSUNPMREwDwYDVQQHDAhDT1lPQUNBTjERMA8GA1UELRMIMi41LjQuNDUxJTAjBgkqhkiG9w0BCQITFnJlc3BvbnNhYmxlOiBBQ0RNQS1TQVQwHhcNMjMwNTE4MTE0MzUxWhcNMjcwNTE4MTE0MzUxWjCB1zEnMCUGA1UEAxMeRVNDVUVMQSBLRU1QRVIgVVJHQVRFIFNBIERFIENWMScwJQYDVQQpEx5FU0NVRUxBIEtFTVBFUiBVUkdBVEUgU0EgREUgQ1YxJzAlBgNVBAoTHkVTQ1VFTEEgS0VNUEVSIFVSR0FURSBTQSBERSBDVjElMCMGA1UELRMcRUtVOTAwMzE3M0M5IC8gVkFEQTgwMDkyN0RKMzEeMBwGA1UEBRMVIC8gVkFEQTgwMDkyN0hTUlNSTDA1MRMwEQYDVQQLEwpTdWN1cnNhbCAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtmecO6n2GS0zL025gbHGQVxznPDICoXzR2uUngz4DqxVUC/w9cE6FxSiXm2ap8Gcjg7wmcZfm85EBaxCx/0J2u5CqnhzIoGCdhBPuhWQnIh5TLgj/X6uNquwZkKChbNe9aeFirU/JbyN7Egia9oKH9KZUsodiM/pWAH00PCtoKJ9OBcSHMq8Rqa3KKoBcfkg1ZrgueffwRLws9yOcRWLb02sDOPzGIm/jEFicVYt2Hw1qdRE5xmTZ7AGG0UHs+unkGjpCVeJ+BEBn0JPLWVvDKHZAQMj6s5Bku35+d/MyATkpOPsGT/VTnsouxekDfikJD1f7A1ZpJbqDpkJnss3vQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAFaUgj5PqgvJigNMgtrdXZnbPfVBbukAbW4OGnUhNrA7SRAAfv2BSGk16PI0nBOr7qF2mItmBnjgEwk+DTv8Zr7w5qp7vleC6dIsZFNJoa6ZndrE/f7KO1CYruLXr5gwEkIyGfJ9NwyIagvHHMszzyHiSZIA850fWtbqtythpAliJ2jF35M5pNS+YTkRB+T6L/c6m00ymN3q9lT1rB03YywxrLreRSFZOSrbwWfg34EJbHfbFXpCSVYdJRfiVdvHnewN0r5fUlPtR9stQHyuqewzdkyb5jTTw02D2cUfL57vlPStBj7SEi3uOWvLrsiDnnCIxRMYJ2UA2ktDKHk+zWnsDmaeleSzonv2CHW42yXYPCvWi88oE1DJNYLNkIjua7MxAnkNZbScNw01A6zbLsZ3y8G6eEYnxSTRfwjd8EP4kdiHNJftm7Z4iRU7HOVh79/lRWB+gd171s3d/mI9kte3MRy6V8MMEMCAnMboGpaooYwgAmwclI2XZCczNWXfhaWe0ZS5PmytD/GDpXzkX0oEgY9K/uYo5V77NdZbGAjmyi8cE2B2ogvyaN2XfIInrZPgEffJ4AB7kFA2mwesdLOCh0BLD9itmCve3A1FGR4+stO2ANUoiI3w3Tv2yQSg4bjeDlJ08lXaaFCLW2peEXMXjQUk7fmpb5MNuOUTW6BE=\" SubTotal=\"1000.00\" Moneda=\"MXN\" Total=\"1000.00\" TipoDeComprobante=\"I\" Exportacion=\"01\" MetodoPago=\"PPD\" LugarExpedicion=\"20928\" Sello=\"gD2HmmLOXI0UtRfEd7XWTqAGRRI6HA4CvxPY97DrQTSzw1YqQXrVN3EOkgVx9qDZVSUA6RqApsSQ31UtfCrw3LDURk8lfSii3/N6YTdF4unMvi5adMZWYudEhDoMo5EplpjBpdnHE1Ik3OPHGY1tQdlGfeOK0UxFeW4QPN5FTdNVqNfWI37VZBuIbTqDBwtSN5UY04ctDnygWEzjX0VdMx4uiMnFSpW6UOsso2cg5HbrQy1pvTPUd4Jh/0Ak0pjK4nI+xmTwK7TPPL06tCze4KMcFzwUpwqVHm0Lk9uj51YHP32xvNQHxNgN1ES0EerA5CfE5czzTocvp0BdttNgJg==\"><cfdi:Emisor Rfc=\"EKU9003173C9\" Nombre=\"ESCUELA KEMPER URGATE\" RegimenFiscal=\"624\" /><cfdi:Receptor Rfc=\"AMI780504F88\" Nombre=\"AISLANTES MINERALES\" UsoCFDI=\"G03\" DomicilioFiscalReceptor=\"78395\" RegimenFiscalReceptor=\"601\" /><cfdi:Conceptos><cfdi:Concepto ClaveProdServ=\"78101802\" NoIdentificacion=\"1\" Cantidad=\"1.000000\" ClaveUnidad=\"E54\" Unidad=\"Actividad\" Descripcion=\"Flete\" ValorUnitario=\"1000.00\" Importe=\"1000.00\" ObjetoImp=\"02\"><cfdi:Impuestos><cfdi:Traslados><cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" /></cfdi:Traslados></cfdi:Impuestos></cfdi:Concepto></cfdi:Conceptos><cfdi:Impuestos TotalImpuestosTrasladados=\"0.00\"><cfdi:Traslados><cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" /></cfdi:Traslados></cfdi:Impuestos><cfdi:Complemento><tfd:TimbreFiscalDigital xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" UUID=\"b751a820-11b0-4e7e-9592-28b2855d5ff4\" FechaTimbrado=\"2025-04-23T19:41:11\" RfcProvCertif=\"SPR190613I52\" SelloCFD=\"gD2HmmLOXI0UtRfEd7XWTqAGRRI6HA4CvxPY97DrQTSzw1YqQXrVN3EOkgVx9qDZVSUA6RqApsSQ31UtfCrw3LDURk8lfSii3/N6YTdF4unMvi5adMZWYudEhDoMo5EplpjBpdnHE1Ik3OPHGY1tQdlGfeOK0UxFeW4QPN5FTdNVqNfWI37VZBuIbTqDBwtSN5UY04ctDnygWEzjX0VdMx4uiMnFSpW6UOsso2cg5HbrQy1pvTPUd4Jh/0Ak0pjK4nI+xmTwK7TPPL06tCze4KMcFzwUpwqVHm0Lk9uj51YHP32xvNQHxNgN1ES0EerA5CfE5czzTocvp0BdttNgJg==\" NoCertificadoSAT=\"30001000000500003456\" SelloSAT=\"SdGyTG6Cx8fU+XG1B3V2edq7FePwLqjph4JaNn1nygtetnt6Ismvp7m92Nv/9BTojXMjvVXbzPhlSU+HzHY6DIkpVS3W6uJxzjucQwO/ZTwBmQsVrKTq9Je6PDD3uQPFZYSQH0qNKBQyiwr5OsWLjQMcn6maxl7gf55X7NY7L41ieQk6Q7EeMfcikLiu7aFWWhLe312hlo1UnV7DcpgPq2uVy3lS0+TMCiscnT1a8sq0kfLnkX/fCiiOX4kVvti/70CosqS+6HxYfRMxCTBFsUxbqD/F/ez+FRzdsJ5Y8+KWkW2X+nIWBiotjNsD4+EdG1BsPxhFAghGhKwG2F6VDw==\" xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" /></cfdi:Complemento></cfdi:Comprobante>\n";
            String xml = this.xml;
            String logoBase64 = convertirImagenABase64("D:\\HackathonGuadalajara\\BackEnd\\ApiSpring\\src\\main\\java\\com\\ApiSpringHackathon\\demo\\utils\\img\\LogoFlecha.png");
            // Creamos una instancia de tipo PDF y realizamos autenticación
            SWPdfService pdf = new SWPdfService("eduardoavilat2002@gmail.com", "wmxUyUq9#DaN", "https://api.test.sw.com.mx", "https://services.test.sw.com.mx");
            // Creamos un arreglo de objetos donde se mencionan las observaciones y/o datos extras
            HashMap<String, String> extras = new HashMap<>();
            extras.put("Observaciones", "Entregar de 9am a 6pm");
            PdfResponse response = null;
            // Llamas al método para generar el PDF
            response = (PdfResponse) pdf.GeneratePdf(xml, "cfdi40", logoBase64, extras);
            // Imprimes resultados del servicio
            System.out.println(response.Status);
            System.out.println(response.contentB64);
            System.out.println(response.message);
            System.out.println(response.messageDetail);
            if(response.Status.equals("success")){
                System.out.println(response.contentB64);
                System.out.println(response.message);
                System.out.println(response.messageDetail);
                this.pdfBase64 = response.contentB64;
                return ResponseEntity.ok(response.contentB64);

            }else{
                System.out.println(response.message);
                System.out.println(response.messageDetail);
//                System.out.println(response.);
                return ResponseEntity.ok(response.message);

            }
//            // Realizamos la petición de generación al servicio
//            Object responseObj = pdf.GeneratePdf(xmlTimbrado, "cfdi40", "logoB64", extras);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }
//
//    @PostMapping("/getXML")
//    public ResponseEntity<String> getXMLByUUID(String UUID){
//        try {
//            //Instancia del servicio y autenticación
//            SWStorageService storage = new SWStorageService("eduardoavilat2002@gmail.com", "wmxUyUq9#DaN","http://services.test.sw.com.mx", "https://api.test.sw.com.mx",null, 0);
//            //Paso de parametro UUID
//            StorageResponse response = (StorageResponse) storage.getXml(UUID.fromString("25b5e4dd-cba0-4ba1-a992-416af3e52235"));
//            //Imprimimos los datos de la respuesta de la solicitud
//            System.out.println(response.Status);
//            System.out.println(response.HttpStatusCode);
//
//            List<StorageData.Records> records = response.getData().data.getRecords();
//
//            for (StorageData.Records record : records) {
//                System.out.println("UUID: " + record.getUuid());
//                System.out.println("URL XML: " + record.getUrlXml());
//                System.out.println("Total: " + record.getTotal());
//                System.out.println("-----------------------------");
//            }
//
//            //En caso de obtener un error, este puede obtenerse de los campos
//            System.out.println(response.message);
//            System.out.println(response.messageDetail);
//            return ResponseEntity.ok("XML: " + records.toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.ok(e.toString());
//        }
//    }
@PostMapping("/genPDFUUID")
public ResponseEntity<String> generarPDFUUID(){
    try {
        String uuid = this.uuid;
//        String logoBase64 = convertirImagenABase64("D:\\HackathonGuadalajara\\BackEnd\\ApiSpring\\src\\main\\java\\com\\ApiSpringHackathon\\demo\\utils\\img\\LogoFlecha.png");
        //Creamos una instancia de tipo PDF y realizamos autenticación
        SWPdfService pdf = new SWPdfService("eduardoavilat2002@gmail.com", "wmxUyUq9#DaN", "https://api.test.sw.com.mx", "http://services.test.sw.com.mx");
        PdfResponse response = null;
        response = (PdfResponse) pdf.RegeneratePdf(uuid);
        //Imprimimos el resultado de la generacion
        System.out.println(response.Status);
        System.out.println(response.contentB64);
        //En caso de obtener un error, este puede obtenerse de los campos
        System.out.println(response.message);
        System.out.println(response.messageDetail);
        return ResponseEntity.ok("Peticion realizada correctamente");
    } catch (AuthException | GeneralException | IOException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
    }
}

    @PostMapping("/sendFacEmail")
    public ResponseEntity<String> sendFacEmail(){//String emails, String XML
        try {
            String uuid = this.uuid;
            //Creamos una instancia de tipo Resend y realizamos autenticación
            SWResendService app = new SWResendService("eduardoavilat2002@gmail.com", "wmxUyUq9#DaN", "https://services.test.sw.com.mx", "https://api.test.sw.com.mx", null, 0);
            ResendResponse response = null;
            response = (ResendResponse) app.ResendEmail(UUID.fromString(uuid),
                    this.email);
            //Imprimimos el resultado de la solicitud
            System.out.println(response.Status);
            System.out.println(response.data);
            //En caso de obtener un error, este puede obtenerse de los campos
            System.out.println(response.message);   
            System.out.println(response.messageDetail);
            return ResponseEntity.ok("Email enviado correctamente: " + response.data);
        } catch (Exception e) {
            e.printStackTrace();
//          return ResponseEntity.ok(e.toString());
        }

        return null;
    }


    public static String convertirImagenABase64(String pathImagen) throws IOException {
        // Leemos la imagen en un arreglo de bytes
        byte[] imageBytes = Files.readAllBytes(Paths.get(pathImagen));

        return Base64.getEncoder().encodeToString(imageBytes);
    }

}