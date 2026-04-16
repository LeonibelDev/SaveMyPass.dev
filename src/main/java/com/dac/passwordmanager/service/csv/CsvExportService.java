package com.dac.passwordmanager.service.csv;

import java.io.Writer;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
public class CsvExportService {

    public <T> String writeCsv(Writer writer, List<T> data) {
        try {
            StatefulBeanToCsv<T> beanToCvs = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();

            beanToCvs.write(data);
        } catch (Exception e) {
            throw new RuntimeException("Error writing CSV", e);
        }
        return null;
    }
}
