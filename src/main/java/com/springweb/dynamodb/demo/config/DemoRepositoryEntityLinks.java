package com.springweb.dynamodb.demo.config;

import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.LinkBuilder;

/**
 * Allows us to configure a custom To RepositoryLinkBuilder which can render
 * Amazon's textual ids to and from url-safe strings
 * 
 * @author Pavan KS
 * 
 */
public class DemoRepositoryEntityLinks extends RepositoryEntityLinks {

	private ResourceMappings resourceMappings;
	private RepositoryRestConfiguration config;

	public DemoRepositoryEntityLinks(
			Repositories repositories,
			ResourceMappings mappings,
			RepositoryRestConfiguration config,
			HateoasPageableHandlerMethodArgumentResolver resolver,
			org.springframework.plugin.core.PluginRegistry<BackendIdConverter, Class<?>> idConverters) {
		super(repositories, mappings, config, resolver, idConverters);
		this.resourceMappings = mappings;
		this.config = config;
	}

	@Override
	public LinkBuilder linkFor(Class<?> type) {
		ResourceMetadata metadata = resourceMappings.getMappingFor(type);
		return new DemoRepositoryLinkBuilder(metadata, config.getBaseUri());
	}

}
