package ru.exchangerates.data.remote.dto.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ValCurs", strict = false)
public class ValCursDto {
    @Attribute(name = "ID")
    private String id;

    @Attribute(name = "DateRange1")
    private String dateRange1;

    @Attribute(name = "DateRange2")
    private String dateRange2;

    @Attribute(name = "name")
    private String name;

    @ElementList(name = "Record", inline = true, required = false)
    private List<RecordDto> recordDto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateRange1() {
        return dateRange1;
    }

    public void setDateRange1(String dateRange1) {
        this.dateRange1 = dateRange1;
    }

    public String getDateRange2() {
        return dateRange2;
    }

    public void setDateRange2(String dateRange2) {
        this.dateRange2 = dateRange2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecordDto> getRecord() {
        return recordDto;
    }

    public void setRecord(List<RecordDto> recordDto) {
        this.recordDto = recordDto;
    }

    @Override
    public String toString() {
        return "ValCurs{" +
                "id='" + id + '\'' +
                ", dateRange1='" + dateRange1 + '\'' +
                ", dateRange2='" + dateRange2 + '\'' +
                ", name='" + name + '\'' +
                ", record=" + recordDto +
                '}';
    }
}

