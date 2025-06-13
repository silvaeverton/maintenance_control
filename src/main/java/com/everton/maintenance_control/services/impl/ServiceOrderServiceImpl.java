package com.everton.maintenance_control.services.impl;

import com.everton.maintenance_control.dtos.OrderServiceStatusDTO;
import com.everton.maintenance_control.dtos.RequestServiceOrderDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.enums.StatusServiceOrder;
import com.everton.maintenance_control.exceptions.AlreadyModifiedException;
import com.everton.maintenance_control.exceptions.NotFoundException;
import com.everton.maintenance_control.model.Machine;
import com.everton.maintenance_control.model.ServiceOrder;
import com.everton.maintenance_control.model.Technician;
import com.everton.maintenance_control.repositories.MachineRepository;
import com.everton.maintenance_control.repositories.ServiceOrderRepository;
import com.everton.maintenance_control.repositories.TechnicianRepository;
import com.everton.maintenance_control.services.ServiceOrderService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderRepository repository;
    private final TechnicianRepository technicianRepository;
    private final MachineRepository machineRepository;


    @Override
    public ServiceOrder createOrder(RequestServiceOrderDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        if (dto.getTechnicianId() == null) {
            throw new IllegalArgumentException("Technician ID cannot be null");
        }
        if (dto.getMachineId() == null) {
            throw new IllegalArgumentException("Machine ID cannot be null");
        }
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (dto.getProblemDescription() == null || dto.getProblemDescription().isEmpty()) {
            throw new IllegalArgumentException("Problem description is required");
        }

        Technician technician = technicianRepository.findById(dto.getTechnicianId())
                .orElseThrow(() -> new NotFoundException("Technician not found", 404));
        Machine machine = machineRepository.findById(dto.getMachineId())
                .orElseThrow(() -> new NotFoundException("Machine not found", 404));

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setName(dto.getName());
        serviceOrder.setOpeningDate(LocalDateTime.now());
        serviceOrder.setClosingDate(null);
        serviceOrder.setProblemDescription(dto.getProblemDescription());
        serviceOrder.setMaintenanceType(dto.getMaintenanceType());
        serviceOrder.setTechnician(technician);
        serviceOrder.setMachine(machine);

        // Define o status
        if (dto.getStatus() != null) {
            try {
                StatusServiceOrder status = StatusServiceOrder.valueOf(String.valueOf(dto.getStatus()));
                serviceOrder.setStatus(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + dto.getStatus(), e);
            }
        } else {
            serviceOrder.setStatus(StatusServiceOrder.OPEN);
        }

        return repository.saveAndFlush(serviceOrder);
    }

    @Override
    public ServiceOrder closeOrder(Long idOrder) {
      ServiceOrder order =  repository.findById(idOrder).orElseThrow(
                ()-> new NotFoundException("Order not Found",404));

        if(order.getStatus() == StatusServiceOrder.FINISHED) {
            throw new AlreadyModifiedException("The order is already closed ",409);
        }
        order.setClosingDate(LocalDateTime.now());
        order.setStatus(StatusServiceOrder.FINISHED);
        return repository.saveAndFlush(order);
    }

    @Override
    public ResponseServiceOrderDTO findOrderById(Long idOrder) {
       ServiceOrder order = repository.findById(idOrder).orElseThrow(
               ()-> new NotFoundException(" Service Order not found",404));

       ResponseServiceOrderDTO dto = new ResponseServiceOrderDTO();
       dto.setId(order.getId());
       dto.setName(order.getName());
       dto.setOpeningDate(order.getOpeningDate());
       dto.setOpeningDate(order.getOpeningDate());
       dto.setMachineId(order.getMachine().getId());
       dto.setTechnicianId(order.getTechnician().getId());
       dto.setProblemDescription(order.getProblemDescription());
       dto.setMaintenanceType(order.getMaintenanceType());


        return dto;
    }

    @Override
    public List<ResponseServiceOrderDTO> allOrders() {
        List<ServiceOrder> list = repository.findAll();
        if(list.isEmpty()) {
            throw new NotFoundException("The Service List is Empty",404);
        }
       return list.stream().map(order -> {
            ResponseServiceOrderDTO dto = new ResponseServiceOrderDTO();
            dto.setId(order.getId());
            dto.setName(order.getName());
            dto.setOpeningDate(order.getOpeningDate());
            dto.setOpeningDate(order.getOpeningDate());
            dto.setMachineId(order.getMachine().getId());
            dto.setTechnicianId(order.getTechnician().getId());
            dto.setProblemDescription(order.getProblemDescription());
            dto.setMaintenanceType(order.getMaintenanceType());
            return dto;
        }).toList();

    }

    @Override
    public ServiceOrder updateOrder(Long idOrder, RequestServiceOrderDTO updateOrder) {
        ServiceOrder orderExist = getServiceOrderById(idOrder);

        Machine machine = machineRepository.findById(updateOrder.getMachineId()).orElseThrow(
                ()-> new NotFoundException("Machine not found", 404));

       Technician technician = technicianRepository.findById(updateOrder.getTechnicianId()).orElseThrow(
               ()-> new NotFoundException("Techniacian not foud", 404));

        if(updateOrder.getName() != null ) {orderExist.setName(updateOrder.getName());}
        if(updateOrder.getStatus() != null) {orderExist.setStatus(updateOrder.getStatus());}
        if(updateOrder.getProblemDescription() != null) {orderExist.setProblemDescription(updateOrder.getProblemDescription());}
        if(updateOrder.getMaintenanceType() != null) {orderExist.setMaintenanceType(updateOrder.getMaintenanceType());}
        if(updateOrder.getMachineId() != null) {orderExist.setMachine(machine);}
        if(updateOrder.getTechnicianId() != null) {orderExist.setTechnician(technician);}



        return repository.saveAndFlush(orderExist);
    }

    @Override
    public void deleteOrder(Long idOrder) {

        repository.deleteById(idOrder);

    }

    @Override
    public OrderServiceStatusDTO veryOrderStatus(Long idOrder) {
        ServiceOrder serviceOrder = getServiceOrderById(idOrder);

        OrderServiceStatusDTO dto = new OrderServiceStatusDTO();
       dto.setId(serviceOrder.getId());
       dto.setStatusServiceOrder(serviceOrder.getStatus());
       dto.setMachineId(serviceOrder.getMachine().getId());
       dto.setTechnicianId(serviceOrder.getTechnician().getId());
       dto.setProblemDescription(serviceOrder.getProblemDescription());


        return dto;
    }

    @Override
    public void executeService(Long idOrder) {
        ServiceOrder serviceOrder = getServiceOrderById(idOrder);

        serviceOrder.setStatus(StatusServiceOrder.IN_PROGRESS);

        repository.saveAndFlush(serviceOrder);

    }

    @Override
    public void finishService(Long idOrder) {
        ServiceOrder serviceOrder = repository.findById(idOrder).orElseThrow(
                ()-> new NotFoundException("OS not found", 404));

        serviceOrder.setStatus(StatusServiceOrder.FINISHED);

        repository.saveAndFlush(serviceOrder);
    }

    @Override
    public void canceledOrder(Long idOrder) {
        ServiceOrder serviceOrder = getServiceOrderById(idOrder);

        serviceOrder.setStatus(StatusServiceOrder.CANCELED);

        repository.saveAndFlush(serviceOrder);

    }

    @Override
    public List<ResponseServiceOrderDTO> findOrderByStatus(String status) {
        List<ServiceOrder> orderList = repository.findAll();

       return orderList.stream().filter( order -> order.getStatus().name().equalsIgnoreCase(status))
                .map(serviceOrder -> new ResponseServiceOrderDTO())
                .toList();



    }

    @Override
    public byte[] generateServiceOrderReport() {
        List<ServiceOrder> orders = repository.findAllWithTechnicianAndMachine();

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,Nome do Técnico,ID do Técnico,Tipo de Manutenção,Data Criação,Data Agendada,Descrição Problema,ID Máquina,Nome Máquina,Setor Máquina,Status OS\n");

        for (ServiceOrder order : orders) {
            csvBuilder.append(order.getId()).append(",");

            Technician technician = order.getTechnician();
            csvBuilder.append(technician != null ? technician.getName() : "Sem Técnico").append(",");
            csvBuilder.append(technician != null ? technician.getId() : "N/A").append(",");

            csvBuilder.append(order.getMaintenanceType()).append(",");
            csvBuilder.append(order.getOpeningDate()).append(",");
            csvBuilder.append(order.getScheduling() != null ? order.getScheduling() : "N/A").append(",");
            csvBuilder.append(order.getProblemDescription() != null ? order.getProblemDescription() : "N/A").append(",");

            Machine machine = order.getMachine();
            csvBuilder.append(machine != null ? machine.getId() : "N/A").append(",");
            csvBuilder.append(machine != null ? machine.getName() : "N/A").append(",");
            csvBuilder.append(machine != null ? machine.getSector() : "N/A").append(",");

            csvBuilder.append(order.getStatus()).append("\n");
        }

        return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] generateServiceOrderPDFById(Long id) {
        ServiceOrder order = repository.findWithTechnicianAndMachineById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        try {
            // Logo
            ImageData imageData = ImageDataFactory.create("src/main/resources/images/logo.png");
            Image logo = new Image(imageData);
            logo.setWidth(80);
            logo.setHeight(80);
            logo.setFixedPosition(450, 750);
            document.add(logo);

            // Título
            document.add(new Paragraph("RELATÓRIO TÉCNICO")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(20));

            document.add(new Paragraph("Emitido em: " + LocalDate.now())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(10));

            document.add(new Paragraph("\n"));

            // Dados do Técnico
            document.add(new Paragraph("Dados do Técnico")
                    .setBold()
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setFontSize(12));

            Table techTable = new Table(2);
            techTable.setWidth(UnitValue.createPercentValue(100));

            techTable.addCell(createStyledCell("Nome:", true));
            techTable.addCell(createStyledCell(order.getTechnician() != null ? order.getTechnician().getName() : "N/A", false));

            techTable.addCell(createStyledCell("Registro:", true));
            techTable.addCell(createStyledCell(order.getTechnician() != null ? order.getTechnician().getId().toString() : "N/A", false));

            document.add(techTable);
            document.add(new Paragraph("\n"));

            // Dados da OS
            document.add(new Paragraph("Dados da Ordem de Serviço")
                    .setBold()
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setFontSize(12));

            Table osTable = new Table(2);
            osTable.setWidth(UnitValue.createPercentValue(100));

            osTable.addCell(createStyledCell("ID:", true));
            osTable.addCell(createStyledCell(order.getId() != null ? order.getId().toString() : "N/A", false));

            osTable.addCell(createStyledCell("Tipo de Manutenção:", true));
            osTable.addCell(createStyledCell(order.getMaintenanceType() != null ? order.getMaintenanceType() : "N/A", false));

            osTable.addCell(createStyledCell("Data de Criação:", true));
            osTable.addCell(createStyledCell(order.getOpeningDate() != null ? order.getOpeningDate().toString() : "N/A", false));

            osTable.addCell(createStyledCell("Status:", true));
            osTable.addCell(createStyledCell(order.getStatus() != null ? order.getStatus().toString() : "N/A", false));

            osTable.addCell(createStyledCell("Máquina:", true));
            osTable.addCell(createStyledCell(order.getMachine() != null ? order.getMachine().getName() : "N/A", false));

            osTable.addCell(createStyledCell("Descrição do Problema:", true));
            osTable.addCell(createStyledCell(order.getProblemDescription() != null ? order.getProblemDescription() : "N/A", false));

            document.add(osTable);

            document.add(new Paragraph("\n\nAssinatura do Técnico: ____________________________")
                    .setTextAlignment(TextAlignment.LEFT));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }


    private Cell createStyledCell(String content, boolean isHeader) {
        Cell cell = new Cell().add(new Paragraph(content));
        cell.setBorder(new SolidBorder(ColorConstants.GREEN, 1));
        cell.setTextAlignment(TextAlignment.LEFT);
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            cell.setBold();
        }
        return cell;
    }




    private ServiceOrder getServiceOrderById(Long idOrder) {
        return repository.findById(idOrder).orElseThrow(()->
                new NotFoundException("Service Order not found",404));
    }

    private Paragraph createColoredField(String title, String value) {
        Text titleText = new Text(title).setFontColor(ColorConstants.BLUE).setBold();
        Text valueText = new Text(value).setFontColor(ColorConstants.BLACK);

        return new Paragraph().add(titleText).add(valueText);
    }



}
