package org.architecture.report;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.architecture.statistic.*;
import org.architecture.system.Request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {

    private final String path;

    public ReportGenerator(String path) {
        this.path = path;
    }

    public void generateXLSXReport(Statistic statistic, String filename, int... steps) {
        File file = new File(path + "\\" + filename);
        try (Workbook wb = new SXSSFWorkbook(); FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            createSourceReportSheet(wb, statistic);
            createDeviceReportSheet(wb, statistic);
            for (int step : steps) {
                if (step < statistic.getSystemStatistic().getStepCount()) {
                    createStepSheet(wb, statistic.getSystemStatistic().getStep(step));
                }
            }
            wb.write(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createStepSheet(Workbook wb, SystemStep step) {
        final String[] headers = {"Шаг №", "Время", "", "", "", "Лог"};

        Map<Integer, SXSSFRow> rows = new HashMap<>();

        SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("Step " + step.getStepCount());
        rows.put(0, sheet.createRow(0));
        for (int i = 0; i < headers.length; i++) {
            rows.get(0).createCell(i).setCellValue(headers[i]);
        }
        rows.put(1, sheet.createRow(1));
        rows.get(1).createCell(0).setCellValue(step.getStepCount());
        rows.get(1).createCell(1).setCellValue(step.getTime());

        String[] logs = step.getLog().split("\n");
        for (int i = 1; i < logs.length + 1; i++) {
            getOrCreateRow(rows, sheet, i).createCell(5).setCellValue(logs[i - 1]);
        }
        getOrCreateRow(rows, sheet, 3).createCell(0).setCellValue("Буффер");
        final String[] headersBuffer = {"Размер", "Указатель", "Последний добавленный", "Количество заявок"};
        for (int i = 0; i < headersBuffer.length; i++) {
            getOrCreateRow(rows, sheet, 4).createCell(i).setCellValue(headersBuffer[i]);
        }
        BufferStepStatistic buffer = step.getBuffer();
        getOrCreateRow(rows, sheet, 5).createCell(0).setCellValue(buffer.getBufferSize());
        getOrCreateRow(rows, sheet, 5).createCell(1).setCellValue(buffer.getPointer());
        getOrCreateRow(rows, sheet, 5).createCell(2).setCellValue(buffer.getLastAdded());
        getOrCreateRow(rows, sheet, 5).createCell(3).setCellValue(buffer.getCountRequest());
        getOrCreateRow(rows, sheet, 6).createCell(0).setCellValue("Ячейка №");
        getOrCreateRow(rows, sheet, 6).createCell(1).setCellValue("Заявка");
        for (int i = 0; i < buffer.getRequests().length; i++) {
            getOrCreateRow(rows, sheet, 7 + i).createCell(0).setCellValue(i);
            Request request = buffer.getRequests()[i];
            if (request != null) {
                getOrCreateRow(rows, sheet, 7 + i).createCell(1).setCellValue(request.getRequestId());
            } else {
                getOrCreateRow(rows, sheet, 7 + i).createCell(1).setCellValue("-");
            }
        }


        int startSources = 7 + buffer.getRequests().length + 1;
        getOrCreateRow(rows, sheet, startSources).createCell(1).setCellValue("Приемники");
        final String[] headersDevices = {"Прибор №", "Свободен", "Номер заявки"};
        for (int i = 0; i < headersDevices.length; i++) {
            getOrCreateRow(rows, sheet, startSources + 1).createCell(i).setCellValue(headersDevices[i]);
        }
        for (int i = 0; i < step.getDevices().length; i++) {
            DeviceStepStatistic device = step.getDevices()[i];
            getOrCreateRow(rows, sheet, startSources + 2 + i).createCell(0).setCellValue(device.getDeviceId());
            getOrCreateRow(rows, sheet, startSources + 2 + i).createCell(1).setCellValue(device.isFree());
            if (device.getRequestId().isPresent()) {
                getOrCreateRow(rows, sheet, startSources + 2 + i).createCell(2).setCellValue(device.getRequestId().get());
            } else {
                getOrCreateRow(rows, sheet, startSources + 2 + i).createCell(2).setCellValue("-");
            }
        }
    }

    private SXSSFRow getOrCreateRow(Map<Integer, SXSSFRow> rows, SXSSFSheet sheet, int i) {
        if (rows.containsKey(i)) return rows.get(i);
        rows.put(i, sheet.createRow(i));
        return rows.get(i);
    }

    private void createSourceReportSheet(Workbook wb, Statistic statistic) {
        final String[] headers = {"№ Источника", "Количество заявок", "Pотк", "Tпреб", "Tбп", "Tобсл", "Дбп", "Добсл", "отказано", "завершено"};
        SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("Source report");
        int rowCount = 0;
        SXSSFRow row = sheet.createRow(rowCount++);
        for (int i = 0; i < headers.length; i++) {
            row.createCell(i).setCellValue(headers[i]);
        }
        for (int i = 0; i < statistic.getSourcesStatistic().length; rowCount++, i++) {
            row = sheet.createRow(rowCount);
            SourceStatistic sourceStatistic = statistic.getSourcesStatistic()[i];
            createSourceRow(row, i, sourceStatistic);

        }
    }

    private void createDeviceReportSheet(Workbook wb, Statistic statistic) {
        final String[] headers = {"№ прибора", "Коэффициент использования"};
        SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("Device report");
        int rowCount = 0;
        SXSSFRow row = sheet.createRow(rowCount++);
        for (int i = 0; i < headers.length; i++) {
            row.createCell(i).setCellValue(headers[i]);
        }
        for (int i = 0; i < statistic.getDevicesStatistic().length; rowCount++, i++) {
            row = sheet.createRow(rowCount);
            createDeviceRow(row, i, statistic.getDevicesStatistic()[i], statistic.getFullTime());
        }
    }

    private void createSourceRow(SXSSFRow row, int i, SourceStatistic sourceStatistic) {
        row.createCell(0, CellType.STRING).setCellValue("И" + i);
        row.createCell(1, CellType.STRING).setCellValue(String.valueOf(sourceStatistic.getGeneratedRequests()));
        row.createCell(2, CellType.NUMERIC).setCellValue(sourceStatistic.getProbabilityOfReject());
        row.createCell(3, CellType.NUMERIC).setCellValue(sourceStatistic.getAverageRequestExistingTime());
        row.createCell(4, CellType.NUMERIC).setCellValue(sourceStatistic.getAverageWaitingTime());
        row.createCell(5, CellType.NUMERIC).setCellValue(sourceStatistic.getAverageServiceTime());
        row.createCell(6, CellType.NUMERIC).setCellValue(sourceStatistic.getWaitingTimeDispersion());
        row.createCell(7, CellType.NUMERIC).setCellValue(sourceStatistic.getServiceTimeDispersion());
        row.createCell(8, CellType.NUMERIC).setCellValue(sourceStatistic.getRejectedRequests());
        row.createCell(9, CellType.NUMERIC).setCellValue(sourceStatistic.getCompletedRequests());

    }

    private void createDeviceRow(SXSSFRow row, int i, DeviceStatistic deviceStatistic, float fullTime) {
        row.createCell(0, CellType.STRING).setCellValue("П" + i);
        row.createCell(1, CellType.NUMERIC).setCellValue(deviceStatistic.getBusyRate(fullTime));

    }


}
