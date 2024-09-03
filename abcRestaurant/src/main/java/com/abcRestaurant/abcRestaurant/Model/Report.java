package com.abcRestaurant.abcRestaurant.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Report")

public class Report {

    private ObjectId id;
    private String ReportId;
    private String fileName;
    private String reportType;
    private LocalDateTime creationDate;
}
