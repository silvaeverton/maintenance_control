package com.everton.maintenance_control.controllers;

import com.everton.maintenance_control.dtos.OrderServiceStatusDTO;
import com.everton.maintenance_control.dtos.RequestServiceOrderDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.model.ServiceOrder;
import com.everton.maintenance_control.services.ServiceOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/serviceOrder")
@Tag(name = "Service Order Controller", description = "Endpoints para gerenciamento de ordens de serviço")
public class ServiceOrderController {

    private final ServiceOrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar O.S", description = " Cadastro de O.S no sistema")
    public ServiceOrder createOrder(@Valid @RequestBody RequestServiceOrderDTO dto) {
        return service.createOrder(dto);
    }

    @GetMapping("/{idOrder}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar O.S por ID",description = "Busca detalhes da O.S por ID")
    public ResponseServiceOrderDTO findOrderById(@PathVariable("idOrder") Long idOrder) {
        return service.findOrderById(idOrder);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar todas O.S",description = " Lista todas O.S no Sistema")
    public List<ResponseServiceOrderDTO> allOrders() {
        return service.allOrders();
    }

    @PutMapping("/{idOrder}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar O.S",description = "Busca O.S por Id e realiza a atualização dos dados")
    public ServiceOrder updateOrder(@PathVariable("idOrder") Long idOrder,@Valid @RequestBody RequestServiceOrderDTO updateOrder) {
        return service.updateOrder(idOrder, updateOrder);
    }

    @DeleteMapping("/{idOrder}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deletar O.S",description = "Busca O.S por Id e realiza o delete")
    public void deleteOrder(@PathVariable("idOrder") Long idOrder) {
        service.deleteOrder(idOrder);
    }

    @GetMapping("/{idOrder}/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Verificar Status", description = "Busca O.S por Id e retorna o status")
    public OrderServiceStatusDTO veryOrderStatus(@PathVariable("idOrder") Long idOrder) {
        return service.veryOrderStatus(idOrder);
    }

    @PatchMapping("/{idOrder}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "executar O.S",description = "Busca O.S por Id e altera o status para Iniciado")
    public void executeService(@PathVariable("idOrder") Long idOrder) {
        service.executeService(idOrder);
    }

    @PatchMapping("/{idOrder}/finish")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Finalizar Seviço",description = "Busca O.S por Id e altera o status para finalizado")
    public void finishService(@PathVariable("idOrder") Long idOrder) {
        service.finishService(idOrder);
    }

    @PatchMapping("/{idOrder}/cancellation")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cancelar O.S",description = "Busca O.S por Id e altera o status para cancelado")
    public void canceledOrder(@PathVariable("idOrder") Long idOrder) {
        service.canceledOrder(idOrder);
    }

    @GetMapping("/statusOder")
    @Operation(summary = "Buscar por Status",description = "Buscar O.S pelo status desejado")
    public List<ResponseServiceOrderDTO> findOrderByStatus(@Valid @RequestParam String status) {
        return service.findOrderByStatus(status);
    }

    @GetMapping("/report")
    @Operation(summary = "Download O.S em csv",description = "Buscar todos as O.S no banco de dado e exporta csv.")
    public ResponseEntity<byte[]> downloadReport() {

        byte[] report = service.generateServiceOrderReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=service_orders_report.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(report);
    }

    @GetMapping("/serviceOrder/report/pdf/{id}")
    @Operation(summary = "Download de O.S PDF",description = "Busca a os por id e efetua o download via PDF")
    public ResponseEntity<byte[]> generatePdfReport(@PathVariable Long id) {
        byte[] pdfBytes = service.generateServiceOrderPDFById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


}
