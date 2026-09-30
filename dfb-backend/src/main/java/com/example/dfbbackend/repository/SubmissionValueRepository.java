package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.SubmissionValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionValueRepository
        extends JpaRepository<SubmissionValue, Long> {

    List<SubmissionValue>
    findBySubmission_IdOrderByField_DisplayOrderAsc(
            Long submissionId
    );
}