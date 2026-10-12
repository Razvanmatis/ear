package de.ear.mapper;

import de.ear.entities.MengenabgabeEntity;
import de.ear.model.Mengenabgabe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MengenabgabeMapper {

    Mengenabgabe toModel(MengenabgabeEntity entity);
}
