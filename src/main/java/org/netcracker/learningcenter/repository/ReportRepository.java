package org.netcracker.learningcenter.repository;

import org.netcracker.learningcenter.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReportRepository  extends MongoRepository<Report,String> {
     Optional<Report> findByRequestId(String id);
}
