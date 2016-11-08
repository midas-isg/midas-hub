package edu.pitt.isg.midas.hub.affiliation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliationRepository extends CrudRepository<Affiliation, Long> {
}
