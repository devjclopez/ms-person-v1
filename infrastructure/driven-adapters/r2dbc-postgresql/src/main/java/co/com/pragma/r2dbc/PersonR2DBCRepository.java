package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.PersonEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonR2DBCRepository extends ReactiveCrudRepository<PersonEntity, Long>,
    ReactiveQueryByExampleExecutor<PersonEntity> {

  Mono<PersonEntity> findByIdNumber(String document);

  Mono<Boolean> existsByIdNumber(String email);
}