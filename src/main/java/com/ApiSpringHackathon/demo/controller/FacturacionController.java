package com.ApiSpringHackathon.demo.controller;
import Services.Issue.SWIssueService;
import Services.Pdf.SWPdfService;
import java.io.IOException;
import java.util.Base64;

//import Utils.Responses.Storage.StorageResponse;
//import Services.Storage.SWStorageService;

import Utils.Responses.Pdf.PdfResponse;
import Utils.Responses.Stamp.SuccessV4Response;
import com.ApiSpringHackathon.demo.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;


@RestController
@RequestMapping("/api")
public class FacturacionController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping("/facturaCFDI")
    public ResponseEntity<String> facturaCFDI(){
        String xml  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/4\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/4 http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd http://www.sat.gob.mx/Pagos20 http://www.sat.gob.mx/sitio_internet/cfd/Pagos/Pagos20.xsd\" Version=\"4.0\" Serie=\"J\" Folio=\"36402\" Fecha=\"2025-04-21T12:10:00\"  FormaPago=\"99\" NoCertificado=\"30001000000500003416\" Certificado=\"MIIFsDCCA5igAwIBAgIUMzAwMDEwMDAwMDA1MDAwMDM0MTYwDQYJKoZIhvcNAQELBQAwggErMQ8wDQYDVQQDDAZBQyBVQVQxLjAsBgNVBAoMJVNFUlZJQ0lPIERFIEFETUlOSVNUUkFDSU9OIFRSSUJVVEFSSUExGjAYBgNVBAsMEVNBVC1JRVMgQXV0aG9yaXR5MSgwJgYJKoZIhvcNAQkBFhlvc2Nhci5tYXJ0aW5lekBzYXQuZ29iLm14MR0wGwYDVQQJDBQzcmEgY2VycmFkYSBkZSBjYWxpejEOMAwGA1UEEQwFMDYzNzAxCzAJBgNVBAYTAk1YMRkwFwYDVQQIDBBDSVVEQUQgREUgTUVYSUNPMREwDwYDVQQHDAhDT1lPQUNBTjERMA8GA1UELRMIMi41LjQuNDUxJTAjBgkqhkiG9w0BCQITFnJlc3BvbnNhYmxlOiBBQ0RNQS1TQVQwHhcNMjMwNTE4MTE0MzUxWhcNMjcwNTE4MTE0MzUxWjCB1zEnMCUGA1UEAxMeRVNDVUVMQSBLRU1QRVIgVVJHQVRFIFNBIERFIENWMScwJQYDVQQpEx5FU0NVRUxBIEtFTVBFUiBVUkdBVEUgU0EgREUgQ1YxJzAlBgNVBAoTHkVTQ1VFTEEgS0VNUEVSIFVSR0FURSBTQSBERSBDVjElMCMGA1UELRMcRUtVOTAwMzE3M0M5IC8gVkFEQTgwMDkyN0RKMzEeMBwGA1UEBRMVIC8gVkFEQTgwMDkyN0hTUlNSTDA1MRMwEQYDVQQLEwpTdWN1cnNhbCAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtmecO6n2GS0zL025gbHGQVxznPDICoXzR2uUngz4DqxVUC/w9cE6FxSiXm2ap8Gcjg7wmcZfm85EBaxCx/0J2u5CqnhzIoGCdhBPuhWQnIh5TLgj/X6uNquwZkKChbNe9aeFirU/JbyN7Egia9oKH9KZUsodiM/pWAH00PCtoKJ9OBcSHMq8Rqa3KKoBcfkg1ZrgueffwRLws9yOcRWLb02sDOPzGIm/jEFicVYt2Hw1qdRE5xmTZ7AGG0UHs+unkGjpCVeJ+BEBn0JPLWVvDKHZAQMj6s5Bku35+d/MyATkpOPsGT/VTnsouxekDfikJD1f7A1ZpJbqDpkJnss3vQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAFaUgj5PqgvJigNMgtrdXZnbPfVBbukAbW4OGnUhNrA7SRAAfv2BSGk16PI0nBOr7qF2mItmBnjgEwk+DTv8Zr7w5qp7vleC6dIsZFNJoa6ZndrE/f7KO1CYruLXr5gwEkIyGfJ9NwyIagvHHMszzyHiSZIA850fWtbqtythpAliJ2jF35M5pNS+YTkRB+T6L/c6m00ymN3q9lT1rB03YywxrLreRSFZOSrbwWfg34EJbHfbFXpCSVYdJRfiVdvHnewN0r5fUlPtR9stQHyuqewzdkyb5jTTw02D2cUfL57vlPStBj7SEi3uOWvLrsiDnnCIxRMYJ2UA2ktDKHk+zWnsDmaeleSzonv2CHW42yXYPCvWi88oE1DJNYLNkIjua7MxAnkNZbScNw01A6zbLsZ3y8G6eEYnxSTRfwjd8EP4kdiHNJftm7Z4iRU7HOVh79/lRWB+gd171s3d/mI9kte3MRy6V8MMEMCAnMboGpaooYwgAmwclI2XZCczNWXfhaWe0ZS5PmytD/GDpXzkX0oEgY9K/uYo5V77NdZbGAjmyi8cE2B2ogvyaN2XfIInrZPgEffJ4AB7kFA2mwesdLOCh0BLD9itmCve3A1FGR4+stO2ANUoiI3w3Tv2yQSg4bjeDlJ08lXaaFCLW2peEXMXjQUk7fmpb5MNuOUTW6BE=\" SubTotal=\"1000.00\" Moneda=\"MXN\" Total=\"1000.00\" TipoDeComprobante=\"I\" Exportacion=\"01\" MetodoPago=\"PPD\" LugarExpedicion=\"20928\">\n  <cfdi:Emisor Rfc=\"EKU9003173C9\" Nombre=\"ESCUELA KEMPER URGATE\" RegimenFiscal=\"624\" />\n  <cfdi:Receptor Rfc=\"AMI780504F88\" Nombre=\"AISLANTES MINERALES\" UsoCFDI=\"G03\" DomicilioFiscalReceptor=\"78395\" RegimenFiscalReceptor=\"601\" />\n  <cfdi:Conceptos>\n    <cfdi:Concepto ClaveProdServ=\"78101802\" NoIdentificacion=\"1\" Cantidad=\"1.000000\" ClaveUnidad=\"E54\" Unidad=\"Actividad\" Descripcion=\"Flete\" ValorUnitario=\"1000.00\" Importe=\"1000.00\" ObjetoImp=\"02\">\n      <cfdi:Impuestos>\n        <cfdi:Traslados>\n          <cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" />\n        </cfdi:Traslados>\n      </cfdi:Impuestos>\n    </cfdi:Concepto>\n  </cfdi:Conceptos>\n  <cfdi:Impuestos TotalImpuestosTrasladados=\"0.00\">\n    <cfdi:Traslados>\n      <cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\" Importe=\"0.00\" />\n    </cfdi:Traslados>\n  </cfdi:Impuestos>\n</cfdi:Comprobante>\n";
        try{
            // Inicializar el objeto con la información de la cuenta o el token de acceso especifica la URL base para acceder al entorno deseado
            SWIssueService sdk = new SWIssueService("eduardoavilat2002@gmail.com", "wmxUyUq9#DaN", "https://services.test.sw.com.mx");
            // Inicializar un objeto de respuesta para almacenar la respuesta
            SuccessV4Response response = null;
            //Envia el JSON acompañado de la versión de respuesta que requieras
            response = (SuccessV4Response) sdk.IssueXml(xml, "v4");
            // En response se mostrará la informacion de respuesta del servicio-
            System.out.println(response.Status);
            System.out.println(response.HttpStatusCode);
            System.out.println(response.messageDetail);
            System.out.println(response.message);
            System.out.println(response.cfdi);
            System.out.println(response.fechaTimbrado);
            System.out.println(response.uuid);
            // Verifica si el timbrado fue exitoso
            if (response.Status.equals("success")) {

                System.out.println();
                // Devuelve la respuesta con el XML timbrado y el Timbre Fiscal Digital (TFD)
                String xmlTimbrado = response.cfdi;
//                ResponseEntity<String> pdfResponse = generarPDF(xmlTimbrado);
//
//                Map<String, String> responseJson = new HashMap<>();
//                System.out.println(pdfResponse.getBody());
                return ResponseEntity.ok("CFDI Timbrado correctamente. TFD: " + response.messageDetail);
            } else {
                // Si hubo algún problema, devuelve el mensaje de error
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al timbrar CFDI: " + response.messageDetail);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok(e.toString());
        }
    }

//    @PostMapping("/getXML")
//    public ResponseEntity<String> getXMLByUUID(String UUID){
//        try {
//            //Instancia del servicio y autenticación
//            SWStorageService storage = new SWStorageService("user", "password","http://services.test.sw.com.mx","https://api.test.sw.com.mx", null, 0);
//            //Paso de parametro UUID
//            StorageResponse response = (StorageResponse) storage.getXml(UUID.fromString("c75f87db-e059-4a7c-a922-e4b9c871e8c1"));
//            //Imprimimos los datos de la respuesta de la solicitud
//            System.out.println(response.Status);
//            System.out.println(response.HttpStatusCode);
//            System.out.println(response.getData);
//            //En caso de obtener un error, este puede obtenerse de los campos
//            System.out.println(response.message);
//            System.out.println(response.messageDetail);
//
//        } catch (AuthException | GeneralException | IOException ex) {
//            Logger.getLogger(ExampleReadme.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    @PostMapping("/genPDF")
    public ResponseEntity<String> generarPDF(String xmlTimbrado0){
        try {

            String xmlTimbrado2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/4\"\n" +
                    "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "    xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/4 http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd\n" +
                    "                        http://www.sat.gob.mx/Pagos20 http://www.sat.gob.mx/sitio_internet/cfd/Pagos/Pagos20.xsd\"\n" +
                    "    Version=\"4.0\" Serie=\"J\" Folio=\"36397\" Fecha=\"2025-04-21T12:09:00\" FormaPago=\"99\"\n" +
                    "    NoCertificado=\"30001000000500003416\"\n" +
                    "    Certificado=\"MIIFsDCCA5igAwIBAgIUMzAwMDEwMDAwMDA1MDAwMDM0MTYwDQYJKoZIhvcNAQELBQAwggErMQ8wDQYDVQQDDAZBQyBVQVQxLjAsBgNVBAoMJVNFUlZJQ0lPIERFIEFETUlOSVNUUkFDSU9OIFRSSUJVVEFSSUExGjAYBgNVBAsMEVNBVC1JRVMgQXV0aG9yaXR5MSgwJgYJKoZIhvcNAQkBFhlvc2Nhci5tYXJ0aW5lekBzYXQuZ29iLm14MR0wGwYDVQQJDBQzcmEgY2VycmFkYSBkZSBjYWxpejEOMAwGA1UEEQwFMDYzNzAxCzAJBgNVBAYTAk1YMRkwFwYDVQQIDBBDSVVEQUQgREUgTUVYSUNPMREwDwYDVQQHDAhDT1lPQUNBTjERMA8GA1UELRMIMi41LjQuNDUxJTAjBgkqhkiG9w0BCQITFnJlc3BvbnNhYmxlOiBBQ0RNQS1TQVQwHhcNMjMwNTE4MTE0MzUxWhcNMjcwNTE4MTE0MzUxWjCB1zEnMCUGA1UEAxMeRVNDVUVMQSBLRU1QRVIgVVJHQVRFIFNBIERFIENWMScwJQYDVQQpEx5FU0NVRUxBIEtFTVBFUiBVUkdBVEUgU0EgREUgQ1YxJzAlBgNVBAoTHkVTQ1VFTEEgS0VNUEVSIFVSR0FURSBTQSBERSBDVjElMCMGA1UELRMcRUtVOTAwMzE3M0M5IC8gVkFEQTgwMDkyN0RKMzEeMBwGA1UEBRMVIC8gVkFEQTgwMDkyN0hTUlNSTDA1MRMwEQYDVQQLEwpTdWN1cnNhbCAxMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtmecO6n2GS0zL025gbHGQVxznPDICoXzR2uUngz4DqxVUC/w9cE6FxSiXm2ap8Gcjg7wmcZfm85EBaxCx/0J2u5CqnhzIoGCdhBPuhWQnIh5TLgj/X6uNquwZkKChbNe9aeFirU/JbyN7Egia9oKH9KZUsodiM/pWAH00PCtoKJ9OBcSHMq8Rqa3KKoBcfkg1ZrgueffwRLws9yOcRWLb02sDOPzGIm/jEFicVYt2Hw1qdRE5xmTZ7AGG0UHs+unkGjpCVeJ+BEBn0JPLWVvDKHZAQMj6s5Bku35+d/MyATkpOPsGT/VTnsouxekDfikJD1f7A1ZpJbqDpkJnss3vQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAFaUgj5PqgvJigNMgtrdXZnbPfVBbukAbW4OGnUhNrA7SRAAfv2BSGk16PI0nBOr7qF2mItmBnjgEwk+DTv8Zr7w5qp7vleC6dIsZFNJoa6ZndrE/f7KO1CYruLXr5gwEkIyGfJ9NwyIagvHHMszzyHiSZIA850fWtbqtythpAliJ2jF35M5pNS+YTkRB+T6L/c6m00ymN3q9lT1rB03YywxrLreRSFZOSrbwWfg34EJbHfbFXpCSVYdJRfiVdvHnewN0r5fUlPtR9stQHyuqewzdkyb5jTTw02D2cUfL57vlPStBj7SEi3uOWvLrsiDnnCIxRMYJ2UA2ktDKHk+zWnsDmaeleSzonv2CHW42yXYPCvWi88oE1DJNYLNkIjua7MxAnkNZbScNw01A6zbLsZ3y8G6eEYnxSTRfwjd8EP4kdiHNJftm7Z4iRU7HOVh79/lRWB+gd171s3d/mI9kte3MRy6V8MMEMCAnMboGpaooYwgAmwclI2XZCczNWXfhaWe0ZS5PmytD/GDpXzkX0oEgY9K/uYo5V77NdZbGAjmyi8cE2B2ogvyaN2XfIInrZPgEffJ4AB7kFA2mwesdLOCh0BLD9itmCve3A1FGR4+stO2ANUoiI3w3Tv2yQSg4bjeDlJ08lXaaFCLW2peEXMXjQUk7fmpb5MNuOUTW6BE=\"\n" +
                    "    SubTotal=\"1000.00\" Moneda=\"MXN\" Total=\"1000.00\" TipoDeComprobante=\"I\" Exportacion=\"01\"\n" +
                    "    MetodoPago=\"PPD\" LugarExpedicion=\"20928\"\n" +
                    "    Sello=\"r+FWeYfCKLVf6zbk8Il1YjgVWo7ohskueS/LPXpoWX56yTHOiiaqTN1OpxIyBTXwSYHYGL9rKBJ2gfI8O9krpM6ykGPwJsTHNvMu5O1FBWX8mQBxf0SPQwBQ7yAcbAOz/bUxa2PWpYwyI8WNjWkBI6k5XjhFaLKpoYZ1BAXTVgVb2y2TBfZUTInzx01GvpmaXkFUZbcVDA3D4AtQHO+4mJGGS1x0SLRzbqJpvip1U0bxga7C0MvreuaE0WapKV69M4F3hMk9QWcDjAn3wq5brhrwT0tNcsBGpp/yaVCg61laz5L75DQj7ZmMsKLMcty4l0wfrNZDPfGyu93S50tJRA==\">\n" +
                    "    <cfdi:Emisor Rfc=\"EKU9003173C9\" Nombre=\"ESCUELA KEMPER URGATE\" RegimenFiscal=\"624\" />\n" +
                    "    <cfdi:Receptor Rfc=\"AMI780504F88\" Nombre=\"AISLANTES MINERALES\" UsoCFDI=\"G03\"\n" +
                    "        DomicilioFiscalReceptor=\"78395\" RegimenFiscalReceptor=\"601\" />\n" +
                    "    <cfdi:Conceptos>\n" +
                    "        <cfdi:Concepto ClaveProdServ=\"78101802\" NoIdentificacion=\"1\" Cantidad=\"1.000000\"\n" +
                    "            ClaveUnidad=\"E54\" Unidad=\"Actividad\" Descripcion=\"Flete\" ValorUnitario=\"1000.00\"\n" +
                    "            Importe=\"1000.00\" ObjetoImp=\"02\">\n" +
                    "            <cfdi:Impuestos>\n" +
                    "                <cfdi:Traslados>\n" +
                    "                    <cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\"\n" +
                    "                        TasaOCuota=\"0.000000\" Importe=\"0.00\" />\n" +
                    "                </cfdi:Traslados>\n" +
                    "            </cfdi:Impuestos>\n" +
                    "        </cfdi:Concepto>\n" +
                    "    </cfdi:Conceptos>\n" +
                    "    <cfdi:Impuestos TotalImpuestosTrasladados=\"0.00\">\n" +
                    "        <cfdi:Traslados>\n" +
                    "            <cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.000000\"\n" +
                    "                Importe=\"0.00\" />\n" +
                    "        </cfdi:Traslados>\n" +
                    "    </cfdi:Impuestos>\n" +
                    "    <cfdi:Complemento>\n" +
                    "        <tfd:TimbreFiscalDigital\n" +
                    "            xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\"\n" +
                    "            Version=\"1.1\" UUID=\"cc719516-48da-4fd1-8583-c4cefeef529d\"\n" +
                    "            FechaTimbrado=\"2025-04-21T15:17:49\" RfcProvCertif=\"SPR190613I52\"\n" +
                    "            SelloCFD=\"r+FWeYfCKLVf6zbk8Il1YjgVWo7ohskueS/LPXpoWX56yTHOiiaqTN1OpxIyBTXwSYHYGL9rKBJ2gfI8O9krpM6ykGPwJsTHNvMu5O1FBWX8mQBxf0SPQwBQ7yAcbAOz/bUxa2PWpYwyI8WNjWkBI6k5XjhFaLKpoYZ1BAXTVgVb2y2TBfZUTInzx01GvpmaXkFUZbcVDA3D4AtQHO+4mJGGS1x0SLRzbqJpvip1U0bxga7C0MvreuaE0WapKV69M4F3hMk9QWcDjAn3wq5brhrwT0tNcsBGpp/yaVCg61laz5L75DQj7ZmMsKLMcty4l0wfrNZDPfGyu93S50tJRA==\"\n" +
                    "            NoCertificadoSAT=\"30001000000500003456\"\n" +
                    "            SelloSAT=\"A9OQlvJXLniEpvALHlAouQRBGn/qOj9r7mV+S9a9InVrGrehB5KdqAhSv4PmPWrG29fMq2mP20cjHftgZXOsqc2d5yJRnoZqLqwv2zhpE0EmlB5e2oyBg1h80c5x+vv8cj5aqUz74oQjFfJ0igzKubtJyUW96wwaf8fpclxBLXy6BqHfpNet44ZXaI4WLUWKcIw5neQRSMYIvhem/0zL+XZquVRHDkb1YEZz7qKKLkiUfyRF7c7tChw0L8MhWYfpHi4vIhPBZJbRmiNEu9nk5V0s4FXamLvTa0hrw1K/lsvPNQzn4F4uVQfT9/2TJwF1iPS48SV/8NAoh5Ba8uuk4A==\"\n" +
                    "            xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\"\n" +
                    "            xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" />\n" +
                    "    </cfdi:Complemento>\n" +
                    "</cfdi:Comprobante>";

            String logoBase64 = convertirImagenABase64("D:\\HackathonGuadalajara\\BackEnd\\ApiSpring\\src\\main\\java\\com\\ApiSpringHackathon\\demo\\utils\\img\\LogoFlecha.png");
            // Creamos una instancia de tipo PDF y realizamos autenticación
            SWPdfService pdf = new SWPdfService("eduardoavilat2002@gmail.com", "wmxUyUq9#DaN", "https://api.test.sw.com.mx", "https://services.test.sw.com.mx");
            // Creamos un arreglo de objetos donde se mencionan las observaciones y/o datos extras
            HashMap<String, String> extras = new HashMap<>();
            extras.put("Observaciones", "Entregar de 9am a 6pm");
            PdfResponse response = null;
            // Llamas al método para generar el PDF
            response = (PdfResponse) pdf.GeneratePdf(xmlTimbrado2, "cfdi40", logoBase64, extras);
            // Imprimes resultados del servicio
            System.out.println(response.Status);
            System.out.println(response.contentB64);
            System.out.println(response.message);
            System.out.println(response.messageDetail);
            if(response.Status.equals("success")){
                System.out.println(response.contentB64);
                System.out.println(response.message);
                System.out.println(response.messageDetail);
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

    public static String convertirImagenABase64(String pathImagen) throws IOException {
        // Leemos la imagen en un arreglo de bytes
        byte[] imageBytes = Files.readAllBytes(Paths.get(pathImagen));

        return Base64.getEncoder().encodeToString(imageBytes);
    }

}