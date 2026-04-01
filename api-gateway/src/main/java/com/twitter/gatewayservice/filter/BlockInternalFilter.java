package com.twitter.gatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Blocks any request to /internal/** paths with 403 Forbidden.
 *
 * /internal/** endpoints are service-to-service only (e.g. auth-service → user-service).
 * They must never be reachable from external clients through the gateway.
 *
 * In production, network-level rules (VPC, firewall) would handle this too —
 * this filter is a defence-in-depth measure at the application layer.
 */
@Component
@Slf4j
public class BlockInternalFilter extends AbstractGatewayFilterFactory<BlockInternalFilter.Config> {

    public BlockInternalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            log.warn("Blocked external access attempt to internal path: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        };
    }

    public static class Config {}
}
