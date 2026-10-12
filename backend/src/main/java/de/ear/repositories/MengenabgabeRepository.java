package de.ear.repositories;

import de.ear.entities.MengenabgabeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MengenabgabeRepository extends JpaRepository<MengenabgabeEntity, Long> {
}
