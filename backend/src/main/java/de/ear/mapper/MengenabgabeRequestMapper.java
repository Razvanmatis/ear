package de.ear.mapper;

import de.ear.entities.MengenabgabeEntity;
import de.ear.model.MengenabgabeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MengenabgabeRequestMapper {

    @Mapping(target = "id", ignore = true)
    MengenabgabeEntity toEntity(MengenabgabeRequest mengenabgabe);
}
