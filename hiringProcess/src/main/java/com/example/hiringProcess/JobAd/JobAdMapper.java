package com.example.hiringProcess.JobAd;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface JobAdMapper {

    JobAdMapper INSTANCE = Mappers.getMapper(JobAdMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    JobAdDetailsDTO jobAdToDetailsDTO(JobAd jobAd);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "jobTitle")
    @Mapping(source = "occupation.title", target = "occupationName")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "departmentName", expression = "java(jobAd.getDepartments().stream().findFirst().map(d -> d.getName()).orElse(null))")
    JobAdSummaryDTO jobAdToSummaryDTO(JobAd jobAd);
}

