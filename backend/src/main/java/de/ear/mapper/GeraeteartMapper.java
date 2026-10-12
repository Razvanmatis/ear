package de.ear.mapper;

import de.ear.model.Geraeteart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GeraeteartMapper {

    @Mapping(target = "gruppe", expression = "java(getGruppeName(geraeteart))")
    @Mapping(target = "kategorie", source = "kategorie.name")
    Geraeteart toModel(de.ear.soap.generated.Geraeteart geraeteart);

    default String getGruppeName(de.ear.soap.generated.Geraeteart geraeteart) {
        if (geraeteart.getGruppe() == null
                || geraeteart.getGruppe().getValue() == null) {
            return null;
        }
        return geraeteart.getGruppe().getValue().getName();
    }
}
