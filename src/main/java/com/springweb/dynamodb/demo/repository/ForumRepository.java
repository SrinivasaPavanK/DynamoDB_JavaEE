package com.springweb.dynamodb.demo.repository;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springweb.dynamodb.demo.domain.Forum;

/**
 * Repository to manage {@link Forum} instances.
 * 
 * Assumes we do not need paging or sorting for Forums, so we use CrudRepository
 * here. Could change to PagingAndSortingRepository if we did need this
 * funtionality.
 * 
 * Requires @EnableScan so that find by non-hash key (by scanning) is enabled,
 * as scanning is disabled by default as it is an expensive operation if large
 * datasets.
 * 
 * @author Pavan KS
 */
@EnableScan
public interface ForumRepository extends CrudRepository<Forum, String> {

	public List<Forum> findByCategory(@Param("category") String category);

	public List<Forum> findByThreads(@Param("threads") Integer threads);

}
