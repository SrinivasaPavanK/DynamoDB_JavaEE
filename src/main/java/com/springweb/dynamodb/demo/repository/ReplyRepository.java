package com.springweb.dynamodb.demo.repository;

import com.springweb.dynamodb.demo.repository.EnableScan;
import com.springweb.dynamodb.demo.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.springweb.dynamodb.demo.domain.Reply;
import com.springweb.dynamodb.demo.domain.ReplyId;
import com.springweb.dynamodb.demo.domain.ThreadId;

/**
 * Repository to manage {@link Reply} instances.
 * 
 * @author Pavan KS
 */
public interface ReplyRepository extends
		PagingAndSortingRepository<Reply, ReplyId> {

	public Page<Reply> findByThreadId(@Param("threadId") ThreadId threadId,
			Pageable pageable);

	public Page<Reply> findByThreadIdOrderByReplyDateTimeDesc(
			@Param("threadId") ThreadId threadId, Pageable pageable);

	@EnableScan
	@EnableScanCount
	public Page<Reply> findByMessage(@Param("message") String message,
			Pageable pageable);

	@EnableScan
	public Slice<Reply> findSliceByMessage(@Param("message") String message,
			Pageable pageable);

	@EnableScan
	@EnableScanCount
	public Page<Reply> findAll(Pageable pageable);

	@EnableScan
	public Page<Reply> findByReplyDateTime(
			@Param("replyDateTime") String replyDateTime, Pageable pageable);

	public Page<Reply> findByThreadIdAndReplyDateTimeAfterOrderByReplyDateTimeDesc(
			@Param("threadId") ThreadId threadId,
			@Param("replyDateTime") String replyDateTime, Pageable pageable);

}
