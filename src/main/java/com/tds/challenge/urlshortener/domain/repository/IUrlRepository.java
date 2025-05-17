package com.tds.challenge.urlshortener.domain.repository;

import com.tds.challenge.urlshortener.domain.model.entity.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUrlRepository extends MongoRepository<Url, String> {
}
