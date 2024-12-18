package org.aicha.hunters_leagues.service;

import org.aicha.hunters_leagues.domain.Species;
import org.aicha.hunters_leagues.exception.exps.DuplicateResourceException;
import org.aicha.hunters_leagues.exception.exps.ResourceNotFoundException;
import org.aicha.hunters_leagues.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service

public class SpeciesService {

    private final SpeciesRepository speciesRepository;

    private final HuntService huntService;

    @Autowired
    public SpeciesService(SpeciesRepository speciesRepository, @Lazy HuntService huntService) {
        this.speciesRepository = speciesRepository;
        this.huntService = huntService;
    }


    public Page<Species> getSpeciesByCategory(Species species , Pageable pageable) {
        if (species.getCategory() == null){
            return speciesRepository.findAll(pageable);
        }
        return speciesRepository.findByCategory(species.getCategory(),pageable);
    }


    public Species addSpecies(Species species) {
        if (speciesRepository.existsByName(species.getName())) {
            throw new DuplicateResourceException("Species with name '" + species.getName() + "' already exists.");
        }
        return speciesRepository.save(species);
    }

    @Transactional
    public Species deleteSpeciesById(Species species) {
        Species speciesToDelete = speciesRepository.findById(species.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Species with id '" + species.getId() + "' does not exist."));
        try {
            huntService.deleteBySpecies(species.getId());
            speciesRepository.deleteById(species.getId());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Cannot delete species as it is referenced in other records.");
        }
        return speciesToDelete;
    }

    public Species updateSpecies(UUID id, Species updatedSpecies) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Species with id '" + id + "' does not exist."));

        species.setName(updatedSpecies.getName());
        species.setCategory(updatedSpecies.getCategory());
        species.setMinimumWeight(updatedSpecies.getMinimumWeight());
        species.setDifficulty(updatedSpecies.getDifficulty());
        species.setPoints(updatedSpecies.getPoints());

        return speciesRepository.save(species);
    }

    public Species findById( UUID speciesId) {
        return speciesRepository.findById(speciesId)
                .orElseThrow(() -> new ResourceNotFoundException("Species with id '" + speciesId + "' does not exist."));
    }
}
