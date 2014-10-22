package com.springweb.dynamodb.demo.config;

import java.net.URI;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.support.RepositoryLinkBuilder;

import com.springweb.dynamodb.demo.domain.ReplyId;
import com.springweb.dynamodb.demo.domain.ThreadId;
import com.springweb.dynamodb.demo.domain.ThreadIdMarshaller;

/**
 * A custom RepositoryLinkBuilder which can render Amazon's textual ids to and
 * from url-safe strings
 * 
 * @author Pavan KS
 * 
 */
public class DemoRepositoryLinkBuilder extends RepositoryLinkBuilder {

	public static DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public DemoRepositoryLinkBuilder(ResourceMetadata metadata, URI baseUri) {
		super(metadata, baseUri);
	}

	@SuppressWarnings("deprecation")
	@Override
	public RepositoryLinkBuilder slash(Object object) {

		if (object instanceof ReplyId) {
			ReplyId replyId = (ReplyId) object;
			try {
				ThreadIdMarshaller threadIdMarshaller = new ThreadIdMarshaller();
				return slash(DATE_FORMAT.parse(replyId.getReplyDateTime())
						.getTime()
						+ "-"
						+ URLEncoder.encode(threadIdMarshaller.marshall(replyId
								.getThreadId())));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		if (object instanceof ThreadId) {
			ThreadId threadId = (ThreadId) object;
			return slash(URLEncoder.encode(threadId.getForumName()) + "_"
					+ URLEncoder.encode(threadId.getSubject()));

		}

		return super.slash(object);
	}

}
