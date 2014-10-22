package com.springweb.dynamodb.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.springweb.dynamodb.demo.domain.Thread;
import com.springweb.dynamodb.demo.domain.ThreadId;

/**
 * Repository to manage {@link Thread} instances.
 * 
 * @author Pavan KS
 */
public interface ThreadRepository extends
		PagingAndSortingRepository<Thread, ThreadId> {

	public Page<Thread> findByForumName(@Param("forumName") String forumName,
			Pageable pageable);

	public Thread findByForumNameAndSubject(
			@Param("forumName") String forumName,
			@Param("subject") String subject);

	public Thread findByThreadId(@Param("threadId") ThreadId threadId);

}
