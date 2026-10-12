package de.ear.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MengenabgabeEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mengenabgabe_seq"
    )
    @SequenceGenerator(
            name = "mengenabgabe_seq",
            sequenceName = "mengenabgabe_seq",
            allocationSize = 1
    )
    private Long id;

    private BigDecimal menge;
    private Integer einheit;
    private Integer jahr;
    private Integer monat;
    private Integer geraeteartnummer;
    private Integer registrierungsnummer;
    private Boolean wahrheitserklaerung;

    public MengenabgabeEntity(
            BigDecimal menge,
            Integer einheit,
            Integer jahr,
            Integer monat,
            Integer geraeteartnummer,
            Integer registrierungsnummer,
            Boolean wahrheitserklaerung) {
        this.menge = menge;
        this.einheit = einheit;
        this.jahr = jahr;
        this.monat = monat;
        this.geraeteartnummer = geraeteartnummer;
        this.registrierungsnummer = registrierungsnummer;
        this.wahrheitserklaerung = wahrheitserklaerung;
    }
}
